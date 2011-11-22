package edu.uci.lighthouse.extensions.codereview.delegates;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

import edu.uci.lighthouse.extensions.codereview.model.CodeReviewFacade;
import edu.uci.lighthouse.extensions.codereview.ui.AddCodeReviewDialog;

public class AddCodeReviewActionDelegate implements IEditorActionDelegate {

	private static Logger logger = Logger
			.getLogger(AddCodeReviewActionDelegate.class);

	@Override
	public void run(IAction action) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();
		AddCodeReviewDialog reviewDialog = new AddCodeReviewDialog();
		WizardDialog wd = new WizardDialog(shell, reviewDialog);
		wd.open();
		
		if (reviewDialog.isFinished()) {
			ITextEditor editor = getTextEditor();
			ITextSelection selection = (ITextSelection) editor
					.getSelectionProvider().getSelection();
			IDocument document = editor.getDocumentProvider().getDocument(
					editor.getEditorInput());
			String filename = getTextEditor().getEditorInput().getName();
			logger.debug("reviewDialog.isFinished()");

			CodeReviewFacade crf = new CodeReviewFacade();
			if (reviewDialog.getReview() == null) {
				crf.addReview(reviewDialog.getReviewer(), document, filename, selection,
						reviewDialog.getComment());
			} else {
				crf.addSelection(reviewDialog.getReview(), document, filename, selection,
						reviewDialog.getComment());
			}
		}
	}

	private ITextEditor getTextEditor() {
		IEditorPart editor = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (editor instanceof ITextEditor) {
			return (ITextEditor) editor;
		}
		return null;
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// Do nothing
	}

	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		// Do nothing
	}

}
