package edu.uci.lighthouse.extensions.codereview.ui;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.extensions.codereview.Activator;
import edu.uci.lighthouse.extensions.codereview.model.CodeReview;
import edu.uci.lighthouse.extensions.codereview.model.CodeSelection;
import edu.uci.lighthouse.extensions.codereview.model.FileSnapshot;
import edu.uci.lighthouse.model.LighthouseAuthor;

public class CodeReviewLabelProvider extends StyledCellLabelProvider {
	
	//@Override
	public Image getImage(Object element) {
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		ImageRegistry imageRegistry = Activator.getDefault().getImageRegistry();
		if (element instanceof CodeReview) {
			CodeReview review = (CodeReview) element;
			LighthouseAuthor me = ModelUtility.getAuthor();
			return me.equals(review.getReviewee()) ? imageRegistry.get(ICodeReviewImages.IMG_ME) : imageRegistry.get(ICodeReviewImages.IMG_REVIEW);
		} else if (element instanceof FileSnapshot) {
			return sharedImages.getImage(ISharedImages.IMG_OBJ_FILE);
		} else if (element instanceof CodeSelection) {
			return imageRegistry.get(ICodeReviewImages.IMG_SELECTION);
		}
		return null;		
	}
	
	@Override
	public void update(ViewerCell cell) {
		Object obj = cell.getElement();
		StyledString styledString = new StyledString(obj.toString());
		 
		if(obj instanceof CodeReview) {
			CodeReview r = (CodeReview) obj;
			LighthouseAuthor me = ModelUtility.getAuthor();
			
			if (me.equals(r.getReviewee())) {
				styledString.append(" > " +
						r.getReviewer() +
						"", StyledString.DECORATIONS_STYLER);
			} else {
				styledString.append(" < " +
						r.getReviewee() +
						"", StyledString.DECORATIONS_STYLER);
			}
			
	
		}
		 
		cell.setText(styledString.toString());
		cell.setStyleRanges(styledString.getStyleRanges());
		cell.setImage(getImage(obj));
		super.update(cell);		
	}
}
