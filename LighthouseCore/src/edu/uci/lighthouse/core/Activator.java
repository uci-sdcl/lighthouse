package edu.uci.lighthouse.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import edu.uci.lighthouse.core.listeners.FileChangedListener;
import edu.uci.lighthouse.core.parser.IParserAction;
import edu.uci.lighthouse.core.parser.LighthouseParser;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManagerPersistence;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	private static Logger logger = Logger.getLogger(Activator.class);
	
	// The plug-in ID
	public static final String PLUGIN_ID = "lighthouse-core"; 

	// The shared instance
	private static Activator plugin;
	
	IResourceChangeListener listener;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		logger.debug("Core Started");

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
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.addResourceChangeListener(listener);		
		plugin = null;
		super.stop(context);
	}

	private void createLighthouseModel(final IWorkspace workspace){
		IProject[] projects = workspace.getRoot().getProjects();
		final Collection<IFile> files = new LinkedList<IFile>();
		for (int i = 0; i < projects.length; i++) {
			if (projects[i].isOpen()) {
				files.addAll(getFilesFromProject(projects[i]));
			}
		}
		if (files.size()>0) {
			LighthouseParser parser = new LighthouseParser();
			final LighthouseModel lighthouseModel = LighthouseModel.getInstance();
			parser.executeInAJob(lighthouseModel,files, new IParserAction() {
				@Override
				public void doAction() {
					new LighthouseModelManagerPersistence(lighthouseModel).saveAllIntoDataBase();
					lighthouseModel.fireModelChanged();
					final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();;
					listener = new FileChangedListener(window);
					workspace.addResourceChangeListener(listener);						
				}
			});
		}		
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
	
}
