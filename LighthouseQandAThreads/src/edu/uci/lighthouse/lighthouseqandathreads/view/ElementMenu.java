package edu.uci.lighthouse.lighthouseqandathreads.view;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;


public class ElementMenu extends Composite{

	
	public ElementMenu(Composite parent, int style) {
		super(parent, style);
		
		GridData compsiteData = new GridData();
		this.setBackground(ColorConstants.black);		
		compsiteData.horizontalAlignment = SWT.CENTER;
		this.setLayoutData(compsiteData);
		setLayout(new GridLayout(3, false));
		
		
		poistionOnParent();
	}
	
	private void poistionOnParent(){
		Point point = this.getParent().getSize();
		int x = point.x - this.getSize().x;
		this.setLocation(x, 0);
		this.getParent().layout();
	}

	public void addButton(Button button){
		button.setParent(this);
		poistionOnParent();
		this.layout();
	}
}
