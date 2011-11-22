package edu.uci.lighthouse.extensions.codereview.ui;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import edu.uci.lighthouse.extensions.codereview.model.CodeReview;
import edu.uci.lighthouse.extensions.codereview.model.CodeSelection;
import edu.uci.lighthouse.extensions.codereview.model.FileSnapshot;

public class CodeReviewLabelProvider extends StyledCellLabelProvider {
	
	//@Override
	public Image getImage(Object element) {
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		if (element instanceof CodeReview) {
			return sharedImages.getImage(ICodeReviewImages.IMG_REVIEW);
		} else if (element instanceof FileSnapshot) {
			return sharedImages.getImage(ISharedImages.IMG_OBJ_FILE);
		} else if (element instanceof CodeSelection) {
			return sharedImages.getImage(ICodeReviewImages.IMG_SELECTION);
		}
		//return super.getImage(element);
		return null;
	}
	
	@Override
	public void update(ViewerCell cell) {
		Object obj = cell.getElement();
		StyledString styledString = new StyledString(obj.toString());
		 
		if(obj instanceof CodeReview) {
			CodeReview r = (CodeReview) obj;
		styledString.append(" [" +
		r.getReviewer() +
		"]", StyledString.DECORATIONS_STYLER);
		}
		 
		cell.setText(styledString.toString());
		cell.setStyleRanges(styledString.getStyleRanges());
		cell.setImage(getImage(obj));
		super.update(cell);		
	}

//	@Override
//	public String getText(Object element) {
//		if (element instanceof CodeReview) {
//			CodeReview r = (CodeReview) element;
//			return r.toString() + " (" + r.getReviewer() + ")";
//		}
//		return element.toString();
//	}
	


}
