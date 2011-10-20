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
