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

/**
 * this interface is used to get around the problem of extending Observable when a Class
 * already extends another Class.  The idea is to have the implementer have an ObservablePoint A field and 
 * to call A.changed() when the implementer changes. 
 * @author lee
 *
 */
public interface IHasObservablePoint {

	public void observeMe(Observer observer);
	
	public ObservablePoint getObservablePoint();
	
}
