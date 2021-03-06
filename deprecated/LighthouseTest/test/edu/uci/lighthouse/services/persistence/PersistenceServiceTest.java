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

import java.io.File;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.uci.lighthouse.model.io.IPersistable;
import edu.uci.lighthouse.services.LighthouseServiceFactory;

public class PersistenceServiceTest {

	IPersistenceService svc = (IPersistenceService) LighthouseServiceFactory.getService("GenericPersistenceService");
	private PersistableList list;
	
	@Before
	public void setUp() throws Exception {
		list = new PersistableList();
		list.add("Test");
	}

	@After
	public void tearDown() throws Exception {
		list = null;
	}

	@Test
	public void testSave() throws PersistenceException {
		svc.save(list);
		File file = new File(list.getFileName());
		Assert.assertTrue(file.exists());
		Assert.assertTrue(file.length() > 0);
	}

	@Test
	public void testLoad() throws PersistenceException {
		PersistableList newList = (PersistableList) svc.load(list);
		Assert.assertEquals(list,newList);
	}
}

class PersistableList extends ArrayList<String> implements IPersistable {
	private static final long serialVersionUID = 8149234276440392841L;
	@Override
	public String getFileName() {
		return "persistable.obj";
	}
}