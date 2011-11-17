package edu.uci.lighthouse.extensions.codereview.ui;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.MarkerUtilities;


public class AddRequestAction implements IWorkbenchWindowActionDelegate {

	@Override
	public void run(IAction action) {
		IEditorPart editor =  PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (editor instanceof ITextEditor) {
		  ISelectionProvider selectionProvider = ((ITextEditor)editor).getSelectionProvider();
		  ISelection selection = selectionProvider.getSelection();
		  if (selection instanceof ITextSelection) {			  
		    ITextSelection textSelection = (ITextSelection)selection;
		    int offset = textSelection.getOffset(); // etc.
			HashMap<String, Object> map = new HashMap<String, Object>();
			MarkerUtilities.setCharStart(map, offset);
			MarkerUtilities.setCharEnd(map, offset + textSelection.getLength());
			try {
				MarkerUtilities.createMarker((IFile)editor.getEditorInput().getAdapter(IFile.class), map, "LighthouseCodeReview.requestMarker");
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		}
		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub

	}

}
