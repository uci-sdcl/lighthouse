/*******************************************************************************
* Copyright (c) {2009,2011} {Software Design and Collaboration Laboratory (SDCL)
*				, University of California, Irvine}.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    {Software Design and Collaboration Laboratory (SDCL)
*	, University of California, Irvine}
*			- initial API and implementation and/or initial documentation
*******************************************************************************/
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
