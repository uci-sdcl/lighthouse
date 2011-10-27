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
package edu.uci.lighthouse.core.controller;

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;

import edu.uci.lighthouse.core.parser.LighthouseParser;
import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.core.util.WorkbenchUtility;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.LighthouseRelationship;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.LHEventDAO;
import edu.uci.lighthouse.parser.ParserException;

/**
 * This is a facade used to send events to the database
 */
public class PushModel {
	
	private LighthouseModel model;
	
	private static PushModel instance;
	
	private PushModel(LighthouseModel model) {
		this.model = model;
	}
	
	public static PushModel getInstance() {
		if (instance == null){
			instance = new PushModel(LighthouseModel.getInstance());
		}
		return instance;
	}
	
	public void saveEventsInDatabase(Collection<LighthouseEvent> listEvents) throws JPAException {
		System.out.println("saving events");
		LHEventDAO dao = new LHEventDAO();
		dao.saveListEvents(listEvents,null);		
	}
	
	public void importEventsToDatabase(Collection<LighthouseEvent> listEvents, IProgressMonitor monitor) throws JPAException {
		LHEventDAO dao = new LHEventDAO();
		dao.saveListEvents(listEvents,monitor);
	}
	
	/**
	 * Parses the collection of <code>IFile</code> and populates the model. It
	 * returns a collection of <code>LighthouseEvent</code> generated during the
	 * process.
	 * 
	 * @param iFiles
	 *            a collection of <code>IFile</code> to be imported to the
	 *            model.
	 * @return a collection of <code>LighthouseEvent</code> generated during the
	 *         import process.
	 * @throws ParserException
	 * @throws JPAException
	 */
	public Collection<LighthouseEvent> addIFilesToModel(Collection<IFile> iFiles)
			throws ParserException, JPAException {
		if (iFiles.size() > 0) {
			LighthouseParser parser = new LighthouseParser();
			parser.execute(iFiles);
			Collection<LighthouseRelationship> listLighthouseRel = parser
					.getListRelationships();
			Collection<LighthouseEntity> listEntities = parser
					.getListEntities();
			LighthouseModelManager modelManager = new LighthouseModelManager(
					model);
			Collection<LighthouseEvent> listEvents = modelManager
					.createEventsAndSaveInLhModel(ModelUtility.getAuthor(),
							listEntities, listLighthouseRel);
			return listEvents;
		}
		return null;
	}

}
