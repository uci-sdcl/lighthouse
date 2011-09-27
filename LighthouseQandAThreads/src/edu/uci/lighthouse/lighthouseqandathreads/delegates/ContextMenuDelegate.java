package edu.uci.lighthouse.lighthouseqandathreads.delegates;

import java.util.ResourceBundle;

import org.eclipse.core.internal.resources.Marker;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.texteditor.*;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;

import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.lighthouseqandathreads.PersistAndUpdate;
import edu.uci.lighthouse.lighthouseqandathreads.markers.CallBackMarkerCreator;
import edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews.CompartmentNewPostView;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.QAforums.LHforum;
import edu.uci.lighthouse.model.QAforums.LHthreadCreator;
import edu.uci.lighthouse.ui.utils.GraphUtils;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;

public class ContextMenuDelegate implements IEditorActionDelegate{

	public static String ID="edu.uci.lighthouse.lighthouseqandathreads.delegates.ContextMenuDelegate";
	
	 private ResourceBundle resourceBundle;
	 private Shell postShell; 
	 private CompartmentNewPostView npv;
	 private CallBackMarkerCreator callback;
	 
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
		callback = new CallBackMarkerCreator(offset,offset+length,"",startLine,file, part, javaEditor);

		openPostView(name,callback);
		
		
		

		
	}
	
	private void openPostView(String className, CallBackMarkerCreator callback2){
		LighthouseAuthor author = ModelUtility.getAuthor();
		LHthreadCreator tm = new LHthreadCreator(author);

		Display display = Display.getDefault();
		
		
		postShell = new Shell(GraphUtils.getGraphViewer()
				.getGraphControl().getDisplay().getActiveShell());

		GridLayout layout = new GridLayout(1, false);
		layout.horizontalSpacing = 1;
		layout.verticalSpacing = 1;
		layout.marginWidth = 1;
		layout.marginHeight = 1;
		
		
		postShell.setLayout(layout);
		
		LighthouseModel model = LighthouseModel.getInstance();
		LighthouseEntity entity = model.getEntity(className);
	
		if(entity != null && entity instanceof LighthouseClass){
			LighthouseClass clazz = (LighthouseClass)entity;
			LHforum forum = clazz.getForum();
			PersistAndUpdate pu = new PersistAndUpdate(clazz);
			npv = new CompartmentNewPostView(postShell, SWT.None,forum,tm,pu,callback2);
			
			
			postShell.setBackground(ColorConstants.black);
			
			postShell.setSize(postShell.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			
			postShell.setText(clazz.getFullyQualifiedName());
			

			
			postShell.open();
			
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
