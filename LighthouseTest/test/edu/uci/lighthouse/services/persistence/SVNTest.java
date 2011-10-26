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

import junit.framework.TestCase;

import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.SVNProviderPlugin;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNStatus;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNStatusKind;

public class SVNTest extends TestCase {

	public void testSVN() throws SVNException, SVNClientException {
		ISVNClientAdapter svnAdapter = SVNProviderPlugin.getPlugin()
		.getSVNClient();
		 ISVNStatus status = svnAdapter.getSingleStatus(new File("test.xml"));
		 System.out.println(status.getTextStatus());
		 assertEquals(status.getTextStatus(), SVNStatusKind.UNVERSIONED);
	}
	
}
