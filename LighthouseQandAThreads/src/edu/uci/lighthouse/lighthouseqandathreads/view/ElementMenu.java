package edu.uci.lighthouse.lighthouseqandathreads.view;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ElementMenu extends Composite {

	public ElementMenu(Composite parent, int style) {
		super(parent, style);

		GridData compsiteData = new GridData(GridData.FILL_HORIZONTAL);

		compsiteData.horizontalAlignment = SWT.RIGHT;
		this.setLayoutData(compsiteData);
		setLayout(new GridLayout(3, false));

	}

	public void addLabel(Label label) {
		label.setParent(this);
		this.layout();
	}

	public void addButton(Button button) {
		button.setParent(this);
		this.layout();
	}
}
