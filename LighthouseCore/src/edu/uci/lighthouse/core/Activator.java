package edu.uci.lighthouse.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import edu.uci.lighthouse.core.controller.Controller;
import edu.uci.lighthouse.core.listeners.IPluginListener;
import edu.uci.lighthouse.core.listeners.JavaFileChangedReporter;
import edu.uci.lighthouse.core.listeners.SVNEventReporter;
import edu.uci.lighthouse.core.preferences.UserPreferences;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.jpa.JPAUtilityException;
import edu.uci.lighthouse.model.jpa.LHAuthorDAO;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin implements IPropertyChangeListener {

	private static Logger logger = Logger.getLogger(Activator.class);
	
	// The plug-in ID
	public static final String PLUGIN_ID = "lighthouse-core"; 

	// The shared instance
	private static Activator plugin;
	
//	IResourceChangeListener listener;
	Collection<IPluginListener> listeners = new LinkedList<IPluginListener>();
	
	private LighthouseAuthor author;
	
	/**
	 * The constructor
	 */
	public Activator() {
		Controller controller = new Controller();
		
		JavaFileChangedReporter jReporter = new JavaFileChangedReporter();
		jReporter.addJavaFileStatusListener(controller);
		
		SVNEventReporter svnReporter = new SVNEventReporter();
		svnReporter.addSVNEventListener(controller);
		
		listeners.add(controller);
		listeners.add(jReporter);
		listeners.add(svnReporter);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		logger.debug("Core Started");
		
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(this);
		
		// Starting listeners
		for (IPluginListener listener : listeners) {
			listener.start(context);
		}
		
		
		
//		JavaCore.addElementChangedListener(new JavaFileChangedReporter(),
//				ElementChangedEvent.POST_CHANGE);

//		IWorkspace workspace = ResourcesPlugin.getWorkspace();		
//		createLighthouseModel(workspace);
		
//		new PullModel().run();
				
/*
		IResourceChangeListener listener = new IResourceChangeListener() {
			public void resourceChanged(IResourceChangeEvent event) {
				IResourceDelta delta = event.getDelta();
				System.out.println("Resource:"+event.getDelta().getKind()+" "+delta.getResource().getLocationURI().getPath()+"("+(delta.getResource().getType()==IResource.FILE )+")");
			}
		};
		workspace.addResourceChangeListener(listener,IResourceChangeEvent.POST_CHANGE);		
		JavaCore.addElementChangedListener(new JavaChangeReporter(), ElementChangedEvent.POST_CHANGE);
		*/
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
//		IWorkspace workspace = ResourcesPlugin.getWorkspace();
//		workspace.addResourceChangeListener(listener);	
		
		Activator.getDefault().getPreferenceStore().removePropertyChangeListener(this);
		
		// Stopping listeners
		for (IPluginListener listener : listeners) {
			listener.stop(context);
		}
		
		plugin = null;
		super.stop(context);
	}
	
	private void createLighthouseModel(final IWorkspace workspace) throws JPAUtilityException{
//		IProject[] projects = workspace.getRoot().getProjects();
//		final Collection<IFile> files = new LinkedList<IFile>();
//		for (int i = 0; i < projects.length; i++) {
//			if (projects[i].isOpen()) {
//				files.addAll(getFilesFromProject(projects[i]));
//			}
//		}
//		if (files.size()>0) {
//			LighthouseParser parser = new LighthouseParser();
//			final LighthouseModel lighthouseModel = LighthouseModel.getInstance();
//			parser.executeInAJob(lighthouseModel,files, new IParserAction() {
//				@Override
//				public void doAction() throws JPAUtilityException {
//					new LighthouseModelManagerPersistence(lighthouseModel).saveAllIntoDataBase();
//					lighthouseModel.fireModelChanged();
//					final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();;
//					//listener = new FileChangedListener(window);
//					workspace.addResourceChangeListener(listener);						
//				}
//			}); 
//		}		
	}
	
	private Collection<IFile> getFilesFromProject(IProject project) {
		final Collection<IFile> files = new HashSet<IFile>();
		try {
			project.getFolder("src").accept(new IResourceVisitor() {
				@Override
				public boolean visit(IResource resource) throws CoreException {
					if (resource.getType() == IResource.FILE && resource.getFileExtension().equalsIgnoreCase("java")) {
						files.add((IFile) resource);
						return false;
					} else {
						return true;
					}
				}
			});
		} catch (CoreException e) {
			// TODO handle exception
		}
		return files;
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	public LighthouseAuthor getAuthor(){
		if (author == null){
			Map<String, String> userSettings = UserPreferences.getUserSettings();
			String userName = userSettings.get(UserPreferences.USERNAME);
			if (userName != null && !"".equals(userName)) {
				author =  new LighthouseAuthor(userName);
				try {
					new LHAuthorDAO().save(author);
				} catch (final JPAUtilityException e) {
					logger.error(e);
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							Shell shell = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getShell();
							MessageDialog.openError(shell,"Database Connection", "Imposible to connect to server. Please, check your connection settings.");
						}
					});
				}
			}
		}
		return author;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (UserPreferences.USERNAME.equals(event.getProperty())) {
			author = null;
		}
		
	}

}
