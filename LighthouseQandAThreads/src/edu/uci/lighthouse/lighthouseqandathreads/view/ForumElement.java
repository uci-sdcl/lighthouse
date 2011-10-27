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

import java.util.Observer;

import org.eclipse.swt.widgets.Composite;

public class ForumElement extends Composite implements IHasObservablePoint{
	private ObservablePoint observablePoint = new ObservablePoint();

	
	public ForumElement(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void observeMe(Observer observer) {
		observablePoint.addObserver(observer);
		
	}

	@Override
	public ObservablePoint getObservablePoint() {
		return this.observablePoint;
	}

}
