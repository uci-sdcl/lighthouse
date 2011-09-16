package edu.uci.lighthouse.lighthouseqandathreads.delegates;

import java.util.ResourceBundle;

import org.eclipse.core.internal.resources.Marker;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class ContextMenuDelegate implements IEditorActionDelegate{

	public static String ID="edu.uci.lighthouse.lighthouseqandathreads.delegates.ContextMenuDelegate";
	
	 private ResourceBundle resourceBundle;
	 
	@Override
	public void run(IAction action) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		IEditorPart part = page.getActiveEditor();
		IEditorInput input = part.getEditorInput();
		IFile file = (IFile) input
		.getAdapter(IFile.class);
		
		IMarker marker;
		try {
			marker = file.createMarker(IMarker.BOOKMARK);
			marker.setAttribute(IMarker.MESSAGE, "This a bookmark");
			marker.setAttribute(IMarker.LOCATION, "line 2");
			marker.setAttribute(IMarker.CHAR_START, 20);
			marker.setAttribute(IMarker.CHAR_END, 21);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		// TODO Auto-generated method stub
		
	}

}
