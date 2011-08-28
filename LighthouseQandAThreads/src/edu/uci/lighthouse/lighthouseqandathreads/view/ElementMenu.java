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
		
		GridData compsiteData = new GridData(GridData.FILL_HORIZONTAL);

		compsiteData.horizontalAlignment = SWT.RIGHT;
		this.setBackground(ColorConstants.black);		
		compsiteData.horizontalAlignment = SWT.CENTER;
		this.setLayoutData(compsiteData);
		setLayout(new GridLayout(3, false));
		
		

	}
	


	public void addButton(Button button){
		button.setParent(this);
		
		this.layout();
	}
}
