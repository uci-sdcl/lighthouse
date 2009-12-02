
package edu.uci.lighthouse.core.listeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IJavaModelMarker;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.BundleContext;

import edu.uci.lighthouse.core.parser.JavaCompilerUtil;

public class JavaFileChangedReporter implements IResourceChangeListener, IElementChangedListener, IPluginListener{

	Collection<IJavaFileStatusListener> listeners = new LinkedList<IJavaFileStatusListener>();
	
//	Collection<IFile> openedFiles = new ArrayList<IFile>();
	
	private static Logger logger = Logger.getLogger(JavaFileChangedReporter.class);
	
	@Override
	public void start(BundleContext context) throws Exception {
		JavaCore.addElementChangedListener(this,
				ElementChangedEvent.POST_CHANGE);
		logger.debug("Starting JavaFileChangedReporter");
		JavaCore.addPreProcessingResourceChangedListener(this, IResourceChangeEvent.POST_BUILD);
		findActiveOpenFileInWorkspace(); //FIXME: find a way to load eclipse workbench first to guarantee the file will be found
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		JavaCore.removePreProcessingResourceChangedListener(this);
		JavaCore.removeElementChangedListener(this);
	}
	
	private void findActiveOpenFileInWorkspace(){
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		IWorkbenchPage[] pages = window.getPages();
		logger.debug("pages length: "+pages.length);
		for (IWorkbenchPage page : pages) {
			IEditorPart editor = page.getActiveEditor();
			logger.debug("editor: "+editor);
			if (editor != null) {
				IFile file = (IFile) editor.getEditorInput().getAdapter(IFile.class);				
				if (isJavaFile(file)){
//					ICompilationUnit icu = JavaCore.createCompilationUnitFrom(file);
//					openedFiles.add(file);
					try {
						boolean hasErrors = IMarker.SEVERITY_ERROR == file.findMaxProblemSeverity(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
						fireOpen(file, hasErrors);
					} catch (CoreException e) {
						logger.error(e);
					}
				}
			}
		}
	}

	private void findOpenFilesInWorkspace() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage[] pages = window.getPages();
		for (IWorkbenchPage page : pages) {
			IEditorReference[] editors = page.getEditorReferences();
			for (IEditorReference editor : editors) {
				try {
					IFile file = (IFile) editor.getEditorInput().getAdapter(IFile.class);
					if (isJavaFile(file)){
//						ICompilationUnit icu = JavaCore.createCompilationUnitFrom(file);
//						openedFiles.add(file);
						fireOpen(file, JavaCompilerUtil.hasErrors(file));
					}
				} catch (PartInitException e) {
					logger.error(e);
				}
			}
		}
	}
	
	private boolean isFileOpenedInEditor(final IFile file){
		class CompareFiles implements Runnable {
			boolean equals = false;
			public void run() {
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				IWorkbenchPage[] pages = window.getPages();
				for (IWorkbenchPage page : pages) {
					IEditorReference[] editors = page.getEditorReferences();
					for (IEditorReference editor : editors) {
						try {
							IFile editorFile = (IFile) editor.getEditorInput().getAdapter(IFile.class);
							if (editorFile.equals(file)){
								equals = true;
								break;
							}
						} catch (PartInitException e) {
							logger.error(e);
						}
					}
				}
			}
		}
		CompareFiles compareTask = new CompareFiles();
		Display.getDefault().syncExec(compareTask);
		return compareTask.equals;
	}
	
	private boolean isJavaFile(IFile file){
		return file.getFileExtension().equalsIgnoreCase("java");
	}
	
	private boolean isJavaFile(IResource resource){
		return resource.getType() == IResource.FILE && resource.getFileExtension().equalsIgnoreCase("java");
	}
	
	@Override
	public void elementChanged(ElementChangedEvent event) {
		IJavaElementDelta delta = event.getDelta();
		logger.debug(delta);
		traverseDeltaTree(delta);
	}
	
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		 try {
			 IResourceDelta delta = event.getDelta();
			delta.accept(new JavaFileResourceDeltaVisitor());
		} catch (CoreException e) {
			logger.error(e);
			e.printStackTrace();
		}		
	}
	
	private void traverseDeltaTree(IJavaElementDelta delta) {
		if (delta.getElement().getElementType() == IJavaElement.COMPILATION_UNIT) {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			ICompilationUnit icu = (ICompilationUnit) delta.getElement();
			IFile iFile = workspace.getRoot().getFile(icu.getPath());
			if (icu.getResource().exists()) {
				try {
					boolean hasErrors = IMarker.SEVERITY_ERROR == icu
							.getResource().findMaxProblemSeverity(
									IMarker.PROBLEM, true,
									IResource.DEPTH_INFINITE);

					if ((delta.getFlags() & IJavaElementDelta.F_PRIMARY_WORKING_COPY) != 0) {
						if (icu.isWorkingCopy()) {
//							openedFiles.add(iFile);
							fireOpen(iFile, hasErrors);
						} else {
//							if (openedFiles.contains(iFile)) {
//								openedFiles.remove(iFile);
								fireClose(iFile, hasErrors);
//							}
						}
					}
				} catch (Exception e) {
					logger.error(e);
				}
			}
		} else {
			for (IJavaElementDelta child : delta.getAffectedChildren()) {
				traverseDeltaTree(child);
			}
		}
	}
	
//	private void traverseDeltaTree(IJavaElementDelta delta) {
//		if (delta.getElement().getElementType() == IJavaElement.COMPILATION_UNIT) {
//			IWorkspace workspace = ResourcesPlugin.getWorkspace();
//			ICompilationUnit icu = (ICompilationUnit) delta.getElement();
//			IFile iFile = workspace.getRoot().getFile(icu.getPath());
//
//			try {
//				boolean hasErrors = JavaCompilerUtil.hasErrors(iFile);
//
//				if (!icu.exists()) {
//					if (delta.getKind() == IJavaElementDelta.REMOVED) {
//						fireRemove(iFile, hasErrors);
//					}
//				} else if (icu.getChildren().length > 0
//						&& !"ModalContext".equals(Thread.currentThread()
//								.getName())) {
//
//					if ((delta.getFlags() & IJavaElementDelta.F_PRIMARY_RESOURCE) != 0) {
//						if (!openedFiles.contains(iFile)) {
//							fireOpen(iFile, hasErrors);
//							fireChange(iFile, hasErrors);
//							if (icu.isWorkingCopy()) {
//								openedFiles.add(iFile);
//							} else {
//								fireClose(iFile, hasErrors);
//							}
//						} else {
//							fireChange(iFile, hasErrors);
//						}
//					} else if ((delta.getFlags() & IJavaElementDelta.F_PRIMARY_WORKING_COPY) != 0) {
//						if (icu.isWorkingCopy()) {
//							openedFiles.add(iFile);
//							fireOpen(iFile, hasErrors);
//						} else {
//							if (openedFiles.contains(iFile)) {
//								openedFiles.remove(iFile);
//								fireClose(iFile, hasErrors);
//							}
//						}
//					}
//				}
//			} catch (Exception e) {
//				logger.error(e);
//			}
//		} else {
//			for (IJavaElementDelta child : delta.getAffectedChildren()) {
//				traverseDeltaTree(child);
//			}
//		}
//	}
	
	public void addJavaFileStatusListener(IJavaFileStatusListener listener){
		listeners.add(listener);
	}
	
	public void removeJavaFileStatusListener(IJavaFileStatusListener listener){
		listeners.remove(listener);
	}
	
	protected void fireOpen(IFile iFile, boolean hasErrors){
		logger.info("Opening "+iFile.getName()+" (errors:"+hasErrors+")");
		for (IJavaFileStatusListener listener : listeners) {
			listener.open(iFile, hasErrors);
		}
	}

	protected void fireClose(IFile iFile, boolean hasErrors){
		logger.info("Closing "+iFile.getName()+" (errors:"+hasErrors+")");
		for (IJavaFileStatusListener listener : listeners) {
			listener.close(iFile, hasErrors);
		}
	}
	
	protected void fireAdd(IFile iFile, boolean hasErrors){
		logger.info("Add "+iFile.getName()+" (errors:"+hasErrors+")");
		for (IJavaFileStatusListener listener : listeners) {
			listener.add(iFile, hasErrors);
		}
	}
	
	protected void fireRemove(IFile iFile, boolean hasErrors){
		logger.info("Removed "+iFile.getName()+" (errors:"+hasErrors+")");
		for (IJavaFileStatusListener listener : listeners) {
			listener.remove(iFile, hasErrors);
		}
	}

	protected void fireChange(IFile iFile, boolean hasErrors){
		logger.info("Changing "+iFile.getName()+" (errors:"+hasErrors+")");
		for (IJavaFileStatusListener listener : listeners) {
			listener.change(iFile, hasErrors);
		}
	}	
	
	class JavaFileResourceDeltaVisitor implements IResourceDeltaVisitor {
		@Override
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();	
			if(isJavaFile(delta.getResource())){
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				IFile iFile = workspace.getRoot().getFile(resource.getFullPath());
				
				boolean hasErrors = false;
				if (resource.exists()){
					hasErrors = IMarker.SEVERITY_ERROR == resource.findMaxProblemSeverity(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
				}
				
				if(delta.getKind() == IResourceDelta.ADDED){
					fireAdd(iFile, hasErrors);
				}else if(delta.getKind() == IResourceDelta.CHANGED) {
//					if (!openedFiles.contains(iFile)) {
//						fireOpen(iFile, hasErrors);
//						fireChange(iFile, hasErrors);
//						if (isFileOpenedInEditor(iFile)) {
//							openedFiles.add(iFile);
//						} else {
//							fireClose(iFile, hasErrors);
//						}
//					} else {
						fireChange(iFile, hasErrors);
//					}
				}else if(delta.getKind() == IResourceDelta.REMOVED){
					fireRemove(iFile, false);
				}
			}
			return true;
		}
	}
}
