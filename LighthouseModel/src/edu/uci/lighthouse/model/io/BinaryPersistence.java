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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;

import edu.uci.lighthouse.model.LighthouseAbstractModel;
import edu.uci.lighthouse.model.LighthouseFile;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;

public class BinaryPersistence implements IPersistence{
	
	private static Logger logger = Logger.getLogger(BinaryPersistence.class);
	
	private static final String defaultFileName = "lighthouse-model.bin";
	
	private LighthouseAbstractModel model;

	protected BinaryPersistence(LighthouseModel model){
		this.model = model;
	}
	
	protected BinaryPersistence(LighthouseFile model){
		this.model = model;
	}
	
	@Override
	public void load() throws PersistenceException {
		load(defaultFileName);
	}

	@Override
	public void load(String fileName) throws PersistenceException {
		try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(
					fileName));
			Object inModel = (LighthouseModel) is.readObject();
			if (inModel instanceof LighthouseModel) {
				LighthouseModelManager manager = new LighthouseModelManager((LighthouseModel)model);
				manager.modelCopy((LighthouseModel)inModel);
			} else if (inModel instanceof LighthouseFile) {
				//TODO: Implement for LighthouseFile
			}
			is.close();
		} catch (Exception e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public void save() throws PersistenceException {
		save(defaultFileName);
	}

	@Override
	public void save(String fileName) throws PersistenceException {
		try {
			ObjectOutputStream os = new ObjectOutputStream(
					new FileOutputStream(fileName));
			os.writeObject(model);
			os.close();
		} catch (Exception e) {
			throw new PersistenceException(e);
		}
	}

}
