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
package edu.uci.lighthouse.model.io;

import javax.persistence.PersistenceException;

import edu.uci.lighthouse.model.LighthouseAbstractModel;
import edu.uci.lighthouse.model.LighthouseFile;
import edu.uci.lighthouse.model.LighthouseModel;

public class PersistenceService {

	public static IPersistence getService(LighthouseModel model, int type) throws PersistenceException{
		return getService((LighthouseAbstractModel)model, type);
	}
	
	public static IPersistence getService(LighthouseFile model, int type) throws PersistenceException{
		return getService((LighthouseAbstractModel)model, type);
	}
	
	private static IPersistence getService(LighthouseAbstractModel model, int type) throws PersistenceException {
		switch (type) {
		case IPersistence.XML:
			if (model instanceof LighthouseModel) {
				return new LighthouseModelXMLPersistence(
						(LighthouseModel) model);
			} else if (model instanceof LighthouseFile) {
				return new LighthouseFileXMLPersistence((LighthouseFile) model);
			}
		case IPersistence.BINARY:
			if (model instanceof LighthouseModel) {
				return new BinaryPersistence((LighthouseModel) model);
			} else if (model instanceof LighthouseFile) {
				return new BinaryPersistence((LighthouseFile) model);
			}
			break;
		default:
			throw new PersistenceException("Persistence type not supported");
		}
		return null;
	}
}
