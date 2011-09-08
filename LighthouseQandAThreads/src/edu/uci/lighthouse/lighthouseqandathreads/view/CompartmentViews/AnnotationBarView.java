package edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class AnnotationBarView extends Composite{
	
	Control mainView;
	public AnnotationBarView(Composite parent, int style, Control mainView, Control annotation) {
		super(parent, style);
		
		this.setLayout(new GridLayout(1, false));
		
		Color postBack = new Color(this.getDisplay(), 255, 212, 102);

		this.setBackground(postBack);
		
		
		
		
		GridData mainData = new GridData(SWT.FILL,SWT.FILL, true, true);
		
		mainView.setParent(this);
		mainView.setLayoutData(mainData);
		
		mainView.setBackground(postBack);
		
		GridData data = new GridData(SWT.FILL,SWT.FILL, true, true);
		
		data.horizontalAlignment = SWT.END;
		annotation.setParent(this);
		annotation.setLayoutData(data);
		annotation.setBackground(postBack);

		
	}

}
