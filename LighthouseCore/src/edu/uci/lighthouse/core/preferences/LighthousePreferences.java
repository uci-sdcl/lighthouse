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
package edu.uci.lighthouse.core.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import edu.uci.lighthouse.core.controller.Controller;

public class LighthousePreferences extends PreferencePage implements
		IWorkbenchPreferencePage {

	private static final String ICON = "$nl$/icons/full/obj16/refresh_tab.gif";
	private Button btSynchronizeModel;
	private Image btImage;
	
	@Override
	public void init(IWorkbench workbench) {
		noDefaultAndApplyButton();
		setDescription("Expand the tree to edit preferences for a specific feature.");
	}

	@Override
	protected Control createContents(Composite parent) {
		btImage = AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.debug.ui", ICON).createImage();
		
		btSynchronizeModel = new Button(parent, SWT.PUSH);
		btSynchronizeModel.setText("Synchronize model with database");
		btSynchronizeModel.setImage(btImage);
		btSynchronizeModel.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseDown(MouseEvent e) {
				getShell().close();
				Controller.getInstance().synchronizeModelWithDatabase();
			}});
		return btSynchronizeModel;
	}

	@Override
	public void dispose() {
		btImage.dispose();
		btImage = null;
		super.dispose();
	}


}
