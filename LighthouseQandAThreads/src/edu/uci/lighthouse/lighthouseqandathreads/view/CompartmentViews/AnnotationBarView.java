package edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class AnnotationBarView extends Composite{
	
	Control mainView;
	public AnnotationBarView(Composite parent, int style, Control mainView, Control annotation) {
		super(parent, style);
		
		this.setLayout(new GridLayout(1, false));
		mainView.setParent(this);
		
		GridData data = new GridData();
		
		data.horizontalAlignment = SWT.END;
		annotation.setParent(this);
		annotation.setLayoutData(data);
		
	}

}
