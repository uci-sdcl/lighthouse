package edu.uci.lighthouse.ui.views;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.part.ViewPart;

public class LinkedWithEditorView extends ViewPart {

	private Text text;

	private boolean linking;

	private IPartListener2 partListener2 = new IPartListener2() {

		@Override
		public void partActivated(IWorkbenchPartReference ref) {
			if (ref.getPart(true) instanceof IEditorPart) {
				editorActivated(getViewSite().getPage().getActiveEditor());
			}
		}

		@Override
		public void partBroughtToTop(IWorkbenchPartReference ref) {
			// TODO Auto-generated method stub
			if (ref.getPart(true) == LinkedWithEditorView.this) {
				editorActivated(getViewSite().getPage().getActiveEditor());
			}
		}

		@Override
		public void partClosed(IWorkbenchPartReference partRef) {
			// TODO Auto-generated method stub

		}

		@Override
		public void partDeactivated(IWorkbenchPartReference partRef) {
			// TODO Auto-generated method stub

		}

		@Override
		public void partHidden(IWorkbenchPartReference partRef) {
			// TODO Auto-generated method stub

		}

		@Override
		public void partInputChanged(IWorkbenchPartReference ref) {
			// TODO Auto-generated method stub

		}

		@Override
		public void partOpened(IWorkbenchPartReference ref) {
			// TODO Auto-generated method stub
			if (ref.getPart(true) == LinkedWithEditorView.this) {
				editorActivated(getViewSite().getPage().getActiveEditor());
			}
		}

		@Override
		public void partVisible(IWorkbenchPartReference ref) {
			if (ref.getPart(true) == LinkedWithEditorView.this) {
				editorActivated(getViewSite().getPage().getActiveEditor());
			}// TODO Auto-generated method stub

		}

	};

	public LinkedWithEditorView() {
	}

	public void createPartControl(Composite parent) {
		text = new Text(parent, SWT.READ_ONLY);
		getViewSite().getActionBars().getToolBarManager().add(
				new Action("Link With Editor", IAction.AS_CHECK_BOX) {

					public void run() {
						toggleLinking(isChecked());
					}
				});

		getSite().getPage().addPartListener(partListener2);
	}

	public void dispose() {
		getSite().getPage().removePartListener(partListener2);
	}

	protected void toggleLinking(boolean checked) {
		this.linking = checked;

		if (checked)
			editorActivated(getSite().getPage().getActiveEditor());

	}

	private void editorActivated(IEditorPart editor) {
		if (!linking || !getViewSite().getPage().isPartVisible(this))
			return;

		IEditorInput input = editor.getEditorInput();

		IFile file = ResourceUtil.getFile(input);
		if (file != null)
			text.setText(file.getLocation().toOSString());

	}

	public void setFocus() {
		text.setFocus();
	}
}