package edu.uci.lighthouse.core.controller;

import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.osgi.framework.BundleContext;
import org.tigris.subversion.svnclientadapter.ISVNInfo;

import edu.uci.lighthouse.core.listeners.IJavaFileStatusListener;
import edu.uci.lighthouse.core.listeners.IPluginListener;
import edu.uci.lighthouse.core.listeners.ISVNEventListener;

public class Controller2 implements ISVNEventListener, IJavaFileStatusListener,
		IPluginListener, IPropertyChangeListener {

	/** Log4j logger instance. */
	private static Logger logger = Logger.getLogger(Controller2.class);
	
	private WorkingCopy projectWorkingCopy;
	
	/** Controller instance (singleton pattern). */
	private static Controller2 instance;

	/**
	 * This constructor does not create an instance.
	 */
	private Controller2() {
		// Singleton pattern
	}

	@Override
	public void start(BundleContext context) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void open(IFile iFile, boolean hasErrors) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close(IFile iFile, boolean hasErrors) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(IFile iFile, boolean hasErrors) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(IFile iFile, boolean hasErrors) {
		// TODO Auto-generated method stub

	}

	@Override
	public void change(IFile iFile, boolean hasErrors) {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkout(Map<IFile, ISVNInfo> svnFiles) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Map<IFile, ISVNInfo> svnFiles) {
		// TODO Auto-generated method stub

	}

	@Override
	public void commit(Map<IFile, ISVNInfo> svnFiles) {
		// TODO Auto-generated method stub

	}

	@Override
	public void conflict(Map<IFile, ISVNInfo> svnFiles) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Returns a <code>Controller2</code> instance.
	 * @return a Controller2 instance.
	 */
	public static Controller2 getInstance(){
		if (instance == null) {
			instance = new Controller2();
		}
		return instance;
	}
}
