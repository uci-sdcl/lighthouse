package edu.uci.lighthouse.ui.views;

import org.eclipse.core.resources.IFile;

public interface IEditorSelectionListener {
	public void selectionChanged(IFile editedFile);
}
