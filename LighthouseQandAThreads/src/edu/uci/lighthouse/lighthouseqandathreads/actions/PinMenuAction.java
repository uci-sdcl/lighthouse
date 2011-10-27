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
package edu.uci.lighthouse.lighthouseqandathreads.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import edu.uci.lighthouse.lighthouseqandathreads.Activator;
import edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews.CompartmentThreadView;

public class PinMenuAction extends Action{
	
	private static final String DESCRIPTION = "Pin";
	private static final String DESCRIPTION2 = "Unpin";
	private CompartmentThreadView view;
	
	public PinMenuAction(CompartmentThreadView view){
		init();
		this.setText("pin window");
		this.view = view;
	}

	protected void init() {
		setToolTipText(DESCRIPTION);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
		"/icons/pin.png"));

	}
	
	public void run(){
		view.flipPin();
		if(view.isPin()){
			setToolTipText(DESCRIPTION2);
			setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
			"/icons/unpin.png"));
		}else{
			setToolTipText(DESCRIPTION);
			setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
			"/icons/pin.png"));
		}
		
	}
}
