package edu.uci.lighthouse.ui.views;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.ide.ResourceUtil;

import edu.uci.lighthouse.views.filters.OpenEditorFilter;

public class EditorListener implements IPartListener2{

	private Collection<IEditorSelectionListener> listeners;
	
	private static Logger logger = Logger.getLogger(EditorListener.class);
	
	public EditorListener(){
		listeners = new ArrayList<IEditorSelectionListener>();
	}
	
	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
		if (partRef instanceof IEditorReference) {
			logger.info("partActivated");
			fireSelectionChanged(((IEditorReference) partRef).getEditor(true));
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {	
	}

	@Override
	public void partClosed(IWorkbenchPartReference partRef) {
		if (partRef instanceof IEditorReference) {
			logger.info("partClosed");
			if (OpenEditorFilter.getLighthouseClassesFromEditor().size() == 0) {
				fireEditorClosed();
			}
		}
	}

	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {		
	}

	@Override
	public void partHidden(IWorkbenchPartReference partRef) {			
	}

	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
		if (partRef instanceof IEditorReference) {
			logger.info("partInputChanged");
			fireSelectionChanged(((IEditorReference) partRef).getEditor(true));
		}
	}

	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
		logger.info("partOpened");
	}

	@Override
	public void partVisible(IWorkbenchPartReference partRef) {		
	}
	
	public void fireSelectionChanged(IEditorPart editor) {
		IFile file = ResourceUtil.getFile(editor.getEditorInput());
		if (file != null) {
			for (IEditorSelectionListener l : listeners) {
				l.selectionChanged(file);
			}
		}
	}
	
	public void fireEditorClosed() {
			for (IEditorSelectionListener l : listeners) {
				l.editorClosed();
			}
	}
	
	public void addEditorSelectionListener(IEditorSelectionListener listener){
		listeners.add(listener);
	}
	
	public void removeEditorSelectionListener(IEditorSelectionListener listener){
		listeners.remove(listener);
	}
}
