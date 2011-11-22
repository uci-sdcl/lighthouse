package edu.uci.lighthouse.extensions.codereview.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.extensions.codereview.model.CodeReview;
import edu.uci.lighthouse.extensions.codereview.model.CodeReviewModel;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.LHAuthorDAO;

public class AddCodeReviewDialog extends Wizard {

	private DialogPage page;
	private LighthouseAuthor reviewer;
	private CodeReview review;
	private String comment;
	private boolean finished = false;
	private NewReview fakeReview = new NewReview();

	private static Logger logger = Logger.getLogger(AddCodeReviewDialog.class);

	public AddCodeReviewDialog() {
		setWindowTitle("Code Review");
	}

	@Override
	public boolean performFinish() {
		reviewer = (LighthouseAuthor) ((IStructuredSelection) page.cvReviewers
				.getSelection()).getFirstElement();
		comment = page.txtComment.getText();
		CodeReview selectedReview = (CodeReview) ((IStructuredSelection) page.cvReviews
				.getSelection()).getFirstElement();
		if (!fakeReview.equals(selectedReview)) {
			review = selectedReview;
		}
		finished = true;
		return finished;
	}

	@Override
	public void addPages() {
		page = new DialogPage();
		addPage(page);
	}

	public boolean isFinished() {
		return finished;
	}

	public DialogPage getPage() {
		return page;
	}

	public LighthouseAuthor getReviewer() {
		return reviewer;
	}

	public CodeReview getReview() {
		return review;
	}

	public String getComment() {
		return comment;
	}

	private static class NewReview extends CodeReview {
		@Override
		public String toString() {
			return "New Review";
		}

		@Override
		public boolean equals(Object obj) {
			return (obj instanceof NewReview);
		}
	}

	private class DialogPage extends WizardPage {
		private ComboViewer cvReviews;
		private ComboViewer cvReviewers;
		private Text txtComment;

		protected DialogPage() {
			super("Code Review");
			setTitle("Add Code Review");
			setDescription("Add or append a request for code review.");
			setPageComplete(false);
		}

		@Override
		public void createControl(Composite parent) {
			Composite composite = new Composite(parent, SWT.NULL);
			composite.setLayout(new FormLayout());
			FormData fData;
			Label label;

			label = new Label(composite, SWT.NULL);
			label.setText("Select a review:");
			fData = new FormData();
			fData.top = fData.left = new FormAttachment(0, 5);
			label.setLayoutData(fData);

			cvReviews = new ComboViewer(composite);
			fData = new FormData();
			fData.top = new FormAttachment(label, 5);
			fData.left = new FormAttachment(0, 10);
			fData.right = new FormAttachment(100, -10);
			cvReviews.getCombo().setLayoutData(fData);
			cvReviews.getCombo().addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					CodeReview selection = (CodeReview) ((IStructuredSelection) cvReviews
							.getSelection()).getFirstElement();
					boolean newReviewSelected = fakeReview.equals(selection);
					if (!newReviewSelected) {
						cvReviewers.setSelection(new StructuredSelection(
								selection.getReviewer()), true);
					}
					cvReviewers.getCombo().setEnabled(newReviewSelected);
					setPageComplete(isComplete());
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
			cvReviews.setContentProvider(new ArrayContentProvider());
			populateReviews(cvReviews);

			label = new Label(composite, SWT.NULL);
			label.setText("Select a reviewer:");
			fData = new FormData();
			fData.top = new FormAttachment(cvReviews.getCombo(), 5);
			fData.left = new FormAttachment(0, 10);
			label.setLayoutData(fData);

			cvReviewers = new ComboViewer(composite);
			fData = new FormData();
			fData.top = new FormAttachment(label, 5);
			fData.left = new FormAttachment(0, 10);
			fData.right = new FormAttachment(100, -10);
			cvReviewers.getCombo().setLayoutData(fData);
			cvReviewers.getCombo().setEnabled(false);
			cvReviewers.getCombo().addSelectionListener(
					new SelectionListener() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							setPageComplete(isComplete());
						}

						@Override
						public void widgetDefaultSelected(SelectionEvent e) {
						}
					});
			cvReviewers.setContentProvider(new ArrayContentProvider());
			populateReviewers(cvReviewers);

			label = new Label(composite, SWT.NULL);
			label.setText("Your comment:");
			fData = new FormData();
			fData.top = new FormAttachment(cvReviewers.getCombo(), 15);
			fData.left = new FormAttachment(0, 10);
			label.setLayoutData(fData);

			txtComment = new Text(composite, SWT.MULTI | SWT.BORDER);
			fData = new FormData();
			fData.top = new FormAttachment(label, 5);
			fData.left = new FormAttachment(0, 10);
			fData.right = new FormAttachment(100, -10);
			fData.bottom = new FormAttachment(100, 0);
			txtComment.setLayoutData(fData);
			txtComment.addKeyListener(new KeyListener() {
				@Override
				public void keyReleased(KeyEvent e) {
				}

				@Override
				public void keyPressed(KeyEvent e) {
					setPageComplete(isComplete());
				}
			});

			setControl(composite);
		}

		private boolean isComplete() {
			return cvReviews.getCombo().getSelectionIndex() != -1
					&& cvReviewers.getCombo().getSelectionIndex() != -1
					&& txtComment.getText().trim().length() > 0;
		}

		private void populateReviewers(ComboViewer cv) {
			List<LighthouseAuthor> authors = new ArrayList<LighthouseAuthor>();
			try {
				authors.addAll(new LHAuthorDAO().list());
				authors.remove(ModelUtility.getAuthor());
				cv.setInput(authors);
			} catch (JPAException e) {
				logger.error(e, e);
			}
		}

		private void populateReviews(ComboViewer cv) {
			Collection<CodeReview> reviews = new ArrayList<CodeReview>();
			reviews.add(fakeReview);
			Collection<CodeReview> allReviews = CodeReviewModel.getInstance()
					.getReviews();
			LighthouseAuthor me = ModelUtility.getAuthor();
			for (CodeReview r : allReviews) {
				if (r.getReviewee().equals(me)) {
					reviews.add(r);
				}
			}
			cv.setInput(reviews);
		}
	}
}
