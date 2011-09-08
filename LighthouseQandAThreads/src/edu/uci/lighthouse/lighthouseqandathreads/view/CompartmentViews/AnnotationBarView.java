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
		
		Color threadBack2 = new Color(this.getDisplay(), 231, 232, 130);

		this.setBackground(threadBack2);
		
		
		
		
		GridData mainData = new GridData(SWT.FILL,SWT.FILL, true, true);
		
		mainView.setParent(this);
		mainView.setLayoutData(mainData);
		
		GridData data = new GridData(SWT.FILL,SWT.FILL, true, true);
		
		data.horizontalAlignment = SWT.END;
		annotation.setParent(this);
		annotation.setLayoutData(data);

		
	}

}
