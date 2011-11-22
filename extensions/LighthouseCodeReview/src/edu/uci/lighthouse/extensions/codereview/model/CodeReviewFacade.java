package edu.uci.lighthouse.extensions.codereview.model;

import org.apache.log4j.Logger;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;

import edu.uci.lighthouse.core.dbactions.DatabaseActionsBuffer;
import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.extensions.codereview.dbactions.SaveReviewAction;
import edu.uci.lighthouse.extensions.codereview.model.CodeReview.StatusType;
import edu.uci.lighthouse.model.LighthouseAuthor;

public class CodeReviewFacade {
	
	private DatabaseActionsBuffer dbService = DatabaseActionsBuffer.getInstance();
//	private CodeReviewModel model = CodeReviewModel.getInstance();
//	private CodeReviewModelManager modelManager = new CodeReviewModelManager(CodeReviewModel.getInstance());

//	private static Logger logger = Logger.getLogger(CodeReviewFacade.class);
	
	public void addReview(LighthouseAuthor reviewer, IDocument document, String filename,
			ITextSelection selection, String comment) {
		CodeReview review = new CodeReview(ModelUtility.getAuthor());
		review.setReviewer(reviewer);
		
		CodeSelection codeSelection = new CodeSelection(selection);
		codeSelection.addComment(new Comment(review.getReviewee(), comment));

		FileSnapshot fs = new FileSnapshot();
		fs.addCodeSelection(codeSelection);
		fs.setContent(document.get());
		fs.setFilename(filename);
					

		review.addFileSnapshot(fs);
		
//		modelManager.addReview(review);
		dbService.offer(new SaveReviewAction(review));
	}
	
	public void addSelection(CodeReview review, IDocument document, String filename,
			ITextSelection selection, String comment) {
		CodeSelection codeSelection = new CodeSelection(selection);
		codeSelection.addComment(new Comment(review.getReviewee(), comment));

		FileSnapshot fs = getFileSnapshotFromFilename(review, filename);
		if (fs == null) {
			fs = new FileSnapshot();
			fs.setFilename(filename);
			review.addFileSnapshot(fs);
		}
		// Update the content
		fs.setContent(document.get());
		fs.addCodeSelection(codeSelection);
		
		dbService.offer(new SaveReviewAction(review));
	}
	
	public void addComment(CodeSelection codeSelection, String comment){
		CodeReview review = CodeReviewModel.getInstance().getReview(codeSelection);
		codeSelection.addComment(new Comment(review.getReviewee(), comment));
		
		dbService.offer(new SaveReviewAction(review));
	}
	
	public void updateSnaphot(FileSnapshot fs, IDocument document){
		CodeReview review = CodeReviewModel.getInstance().getReview(fs);
		fs.setContent(document.get());
		
		dbService.offer(new SaveReviewAction(review));
	}
	
	private FileSnapshot getFileSnapshotFromFilename(CodeReview review, String filename){
		for (FileSnapshot fs: review.getFilesSnapshot()) {
			if (fs.getFilename().equals(filename)) {
				return fs;
			}
		}
		return null;
	}
	
	public void setClose(CodeReview review) {
		if (review.getReviewee().equals(ModelUtility.getAuthor())) {
			review.setStatus(StatusType.CLOSE);
			
//			modelManager.removeReview(review);
			dbService.offer(new SaveReviewAction(review));
		}
	}

	public void setActive(CodeReview review) {
		if (!review.getReviewer().equals(ModelUtility.getAuthor())) {
			review.setStatus(StatusType.ACTIVE);
			
			dbService.offer(new SaveReviewAction(review));
		}		
	}
}
