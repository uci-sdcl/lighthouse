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
package edu.uci.lighthouse.services.persistence;

import edu.uci.lighthouse.model.io.IPersistable;
import edu.uci.lighthouse.services.ILighthouseService;

public interface IPersistenceService extends ILighthouseService {
	
	public void save(IPersistable obj) throws PersistenceException;
	
	public IPersistable load(IPersistable obj) throws PersistenceException;
	
}
