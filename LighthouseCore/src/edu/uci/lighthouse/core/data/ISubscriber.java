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
package edu.uci.lighthouse.core.data;

import java.io.Serializable;
import java.util.List;
import java.util.Observer;

import edu.uci.lighthouse.model.LighthouseEvent;


/**
 * Classes that want to receive LighthouseEvents when the are pulled from 
 * the Database Model should implement this interface. 
 * @author lee
 *
 */
public interface ISubscriber extends Serializable{

	public void receive(List<LighthouseEvent> events);
}
