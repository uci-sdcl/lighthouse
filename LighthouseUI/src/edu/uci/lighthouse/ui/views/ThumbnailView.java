package edu.uci.lighthouse.ui.views;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * 
 * @author tproenca
 *
 */
public class ThumbnailView extends ViewPart{
	
	/** */
	Composite composite;
	
	/** */
	FigureCanvas thumbnail;
	
	/** */
	ScrollableThumbnail tb;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		FormData data;		
		parent.setLayout(new FormLayout());			
		data = new FormData();
		data.top = new FormAttachment(100,-130);
		data.left = new FormAttachment(100,-130);
		data.right = new FormAttachment(100,-30);
		data.bottom = new FormAttachment(100,-30);
		
		thumbnail = new FigureCanvas(parent, SWT.NONE);
		thumbnail.setBackground(ColorConstants.white);
		thumbnail.setLayoutData(data);
		
		tb = new ScrollableThumbnail();
		tb.setBorder(new LineBorder(1));
		thumbnail.setContents(tb);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		if (composite != null){
			composite.setFocus();
		}
	}
	
	/**
	 * @param port
	 */
	protected void setViewport(Viewport port){	
		if (tb != null) {
			tb.setViewport(port);	
		}		
	}
	
	/**
	 * @param fig
	 */
	protected void setSource(IFigure fig){
		if (tb != null){
			tb.setSource(fig);	
		}		
	}
	
	/**
	 * @param composite
	 */
	protected void setMainComposite(Composite composite){
		FormData data = new FormData();
		data.top = new FormAttachment(0,0);
		data.left = new FormAttachment(0,0);
		data.right = new FormAttachment(100,0);
		data.bottom = new FormAttachment(100,0);
		composite.setLayoutData(data);
		this.composite = composite;
	}	
}
