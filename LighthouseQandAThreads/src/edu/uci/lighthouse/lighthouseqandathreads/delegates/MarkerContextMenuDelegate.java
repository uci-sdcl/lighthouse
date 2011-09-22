package edu.uci.lighthouse.lighthouseqandathreads.delegates;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import edu.uci.lighthouse.core.util.ModelUtility;

public class MarkerContextMenuDelegate implements IEditorActionDelegate{

	public static String ID="edu.uci.lighthouse.lighthouseqandathreads.delegates.MarkerContextMenuDelegate";

	
	@Override
	public void run(IAction action) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		IEditorPart part = page.getActiveEditor();
	
		CompilationUnitEditor javaEditor = (CompilationUnitEditor)part;
		ISelectionProvider provider = javaEditor.getSelectionProvider();
		ITextSelection selection = (ITextSelection)provider.getSelection();
		int offset = selection.getOffset();
		int length = selection.getLength();
		int startLine = selection.getStartLine();
		IEditorInput input = part.getEditorInput();
	
		IFile file = (IFile) input
		.getAdapter(IFile.class);
	
		ModelUtility mu = new ModelUtility();
		String name = mu.getClassFullyQualifiedName(file);

		
		
		
	}
	
	public void openReplyView(){
		
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
