package edu.uci.lighthouse.core.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

import edu.uci.lighthouse.core.controller.PullModel;
import edu.uci.lighthouse.core.dbactions.DatabaseActionsBuffer;
import edu.uci.lighthouse.core.dbactions.IDatabaseAction;
import edu.uci.lighthouse.core.dbactions.pull.FetchNewEventsAction;
import edu.uci.lighthouse.core.listeners.IPluginListener;
import edu.uci.lighthouse.core.preferences.DatabasePreferences;
import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.core.widgets.StatusWidget;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.JPAUtility;


/**
 * Fetched new data from the database between specific time intervals defined by TIMEOUT constant.
 * @author tproenca
 *
 */
public class DatabaseActionsThread extends Thread implements IPluginListener{

	private final long TIMEOUT = 5000;
	private final int MAX_MULTIPLIER = 180; // 180 times
	private int backoffMultiplier = 1;
	private boolean running = false;
	private boolean suspended = false;
	private DatabaseActionsBuffer buffer;
		
	private static Logger logger = Logger.getLogger(DatabaseActionsThread.class);
	
	/**@author lee*/
	ArrayList<ISubscriber> subscribers = new ArrayList<ISubscriber>();
	
	public DatabaseActionsThread(DatabaseActionsBuffer buffer) {
		super(DatabaseActionsThread.class.getName());
		this.buffer = buffer;
		StatusWidget.getInstance().setStatus(Status.CANCEL_STATUS);
	}

	@Override
	public void run() {
		JPAUtility.initializeEntityManagerFactory(DatabasePreferences.getDatabaseSettings());
		while (running) {
			try {
				if (suspended) {
					synchronized (this) {
						while (suspended) {
							wait();
						}
					}
				}
				
				processBuffer();

				long adjustedTimeout = TIMEOUT * backoffMultiplier;
				if (backoffMultiplier > 1) {
					logger.debug("Next thread iteration in "
							+ (adjustedTimeout / 1000) + "s");
				}

				sleep(adjustedTimeout);
			} catch (InterruptedException e) {
				logger.error(e);
			} 
		}
		JPAUtility.shutdownEntityManagerFactory();
	}

	@Override
	public void start(BundleContext context) throws Exception {
		logger.info("Starting thread...");
		running = true;
		this.start();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		logger.info("Stopping thread...");
		running = false;
		interrupt();
	}
	
	private void processBuffer() {
		try {
			while (!buffer.isEmpty()) {
				logger.debug("Executing "+buffer.size()+" database actions.");
				IDatabaseAction databaseAction = buffer.peek();
				databaseAction.run();
				buffer.poll();
			}
			
			/**@author lee*/
			sendLighthouseEventsToSubscribers();
			
			if (ModelUtility.hasImportedProjects(ResourcesPlugin.getWorkspace())) {
				StatusWidget.getInstance().setStatus(Status.OK_STATUS);
				buffer.offer(new FetchNewEventsAction());
			}
			backoffMultiplier = 1;
		} catch (Exception ex) {
			backoffMultiplier = backoffMultiplier > MAX_MULTIPLIER ? 1 : backoffMultiplier*2;
			if (ex.getCause() != null && "org.hibernate.exception.JDBCConnectionException".equals(ex.getCause().getClass().getName())) {
				StatusWidget.getInstance().setStatus(Status.CANCEL_STATUS);
			}
			logger.error(ex, ex);
		} 
	}
	
	public synchronized void pause() {
		suspended = true;
		StatusWidget.getInstance().setStatus(Status.CANCEL_STATUS);
		interrupt();
	}
	
	public synchronized void play() {
		suspended = false;
		StatusWidget.getInstance().setStatus(Status.OK_STATUS);
		notify();
	}
	
	/**
	 * Sends LighthouseEvents to subscribers
	 * @author lee
	 */
	private void sendLighthouseEventsToSubscribers(){
		LighthouseAuthor author = ModelUtility.getAuthor();
		PullModel pullModel = PullModel.getInstance();
		List<LighthouseEvent> events;
		try {
			events = pullModel.getNewEventsFromDB(author);
			for(ISubscriber sub: subscribers){
				sub.recive(events);
			}
		} catch (JPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * @author lee
	 * @param subscriber
	 */
	public void subscribeToLighthouseEvents(ISubscriber subscriber){
		this.subscribers.add(subscriber);
	}
}
