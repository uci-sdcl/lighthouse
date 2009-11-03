
package edu.uci.lighthouse.core.listeners;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IProblemRequestor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.BundleContext;

public class JavaFileChangedReporter implements IElementChangedListener, IPluginListener{

	Collection<IJavaFileStatusListener> listeners = new LinkedList<IJavaFileStatusListener>();
	
	private static Logger logger = Logger.getLogger(JavaFileChangedReporter.class);
	
	public JavaFileChangedReporter(){	
//		findActiveOpenFileInWorkspace();
//		findOpenFilesInWorkspace();
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		JavaCore.addElementChangedListener(this,
				ElementChangedEvent.POST_CHANGE);
		findActiveOpenFileInWorkspace();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
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
					ICompilationUnit icu = JavaCore.createCompilationUnitFrom(file);
					fireOpen(file, hasErrors(icu));
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
						ICompilationUnit icu = JavaCore.createCompilationUnitFrom(file);
						fireOpen(file, hasErrors(icu));
					}
				} catch (PartInitException e) {
					logger.error(e);
				}
			}
		}
	}
	
	private boolean isJavaFile(IFile file){
		if(file.getFileExtension().equalsIgnoreCase("java")) {
			return true;
		}
		return false;
	}
	
	@Override
	public void elementChanged(ElementChangedEvent event) {
		IJavaElementDelta delta = event.getDelta();
		logger.debug(delta);
		traverseDeltaTree(delta);
	}

	private void traverseDeltaTree(IJavaElementDelta delta) {
		if (delta.getElement().getElementType() == IJavaElement.COMPILATION_UNIT) {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			ICompilationUnit icu = (ICompilationUnit) delta.getElement();
			IFile file = workspace.getRoot().getFile(icu.getPath());

			logger.debug("Thread name: "+Thread.currentThread().getName());
			try {
				logger.debug("ICU opened: "+icu.isOpen()+" "+icu.isConsistent() + " "+icu.isStructureKnown()+" "+icu.isWorkingCopy());
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if ((delta.getFlags() & IJavaElementDelta.F_PRIMARY_RESOURCE) != 0) {
				fireChange(file, hasErrors(icu));
			} else if ((delta.getFlags() & IJavaElementDelta.F_PRIMARY_WORKING_COPY) != 0) {
				if (icu.isWorkingCopy()) {
					fireOpen(file, hasErrors(icu));
				} else {
					fireClose(file, hasErrors(icu));
				}
				
			}
		} else {
			for (IJavaElementDelta child : delta.getChangedChildren()) {
				traverseDeltaTree(child);
			}
		}
	}
	
	private boolean hasErrors(ICompilationUnit icu){		
//		WorkingCopyProblemRequestor workingCopy = new WorkingCopyProblemRequestor();
//		try {			
//			icu.getWorkingCopy(workingCopy, null);
//		} catch (JavaModelException e) {
//			logger.error(e);
//		}
//		return workingCopy.hasProblems;
		return false;
	}
	
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

	protected void fireChange(IFile iFile, boolean hasErrors){
		logger.info("Changing "+iFile.getName()+" (errors:"+hasErrors+")");
		for (IJavaFileStatusListener listener : listeners) {
			listener.change(iFile, hasErrors);
		}
	}	
	
	class WorkingCopyProblemRequestor extends WorkingCopyOwner implements IProblemRequestor {
		
		boolean hasProblems = false;
		
		@Override
		public void acceptProblem(IProblem problem) {
			if (problem.isError()) {
				hasProblems = true;
			}
		}
		
		@Override
		public IProblemRequestor getProblemRequestor(
				ICompilationUnit workingCopy) {
			return this;
		}

		@Override
		public void beginReporting() {
		}

		@Override
		public void endReporting() {
		}

		@Override
		public boolean isActive() {
			return true;
		}		
	}
}
