/*******************************************************************************
* Copyright (c) {2009,2011} {Software Design and Collaboration Laboratory (SDCL)
*				, University of California, Irvine}.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    {Software Design and Collaboration Laboratory (SDCL)
*	, University of California, Irvine}
*			- initial API and implementation and/or initial documentation
*******************************************************************************/
package edu.uci.lighthouse.ui.views;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.ide.ResourceUtil;

public class EditorListener implements IPartListener2{

	private Collection<IEditorSelectionListener> listeners;
	
	public EditorListener(){
		listeners = new ArrayList<IEditorSelectionListener>();
	}
	
	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
		if (partRef instanceof IEditorReference) {
			fireSelectionChanged(((IEditorReference) partRef).getEditor(true));
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {	
	}

	@Override
	public void partClosed(IWorkbenchPartReference partRef) {	
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
			fireSelectionChanged(((IEditorReference) partRef).getEditor(true));
		}
	}

	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
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
	
	public void addEditorSelectionListener(IEditorSelectionListener listener){
		listeners.add(listener);
	}
	
	public void removeEditorSelectionListener(IEditorSelectionListener listener){
		listeners.remove(listener);
	}
}
