package edu.uci.lighthouse.core.util;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class WorkbenchUtility {

	public static IEditorPart getActiveEditor(){
		class UITask implements Runnable {
			IEditorPart activeEditor;
			public void run() {
				IWorkbenchWindow window = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow();
				IWorkbenchPage[] pages = window.getPages();
				for (IWorkbenchPage page : pages) {
					activeEditor = page.getActiveEditor();
				}
			}
		}
		UITask task = new UITask();
		Display.getDefault().syncExec(task);
		return task.activeEditor;
	}
	
	public static IStatusLineManager getStatusLineManager(){
		IEditorPart editor = getActiveEditor();
		if (editor != null){
			return editor.getEditorSite().getActionBars().getStatusLineManager();
		}
		return null;
	}
	
}
