package edu.uci.lighthouse.model.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import org.dom4j.DocumentException;

import edu.uci.lighthouse.model.BuildLHBaseFile;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseDelta;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseFile;
import edu.uci.lighthouse.model.io.LighthouseFileXMLPersistence;
import edu.uci.lighthouse.model.io.LighthouseModelXMLPersistence;
import edu.uci.lighthouse.test.util.LHTestDataFiles;
import edu.uci.lighthouse.test.util.LighthouseModelTest;

public class LighthouseFileManagerTest extends TestCase {

	public void testGetBaseLHFile() throws DocumentException, ParseException {
		LighthouseModelTest xmlModel = new LighthouseModelTest();
		new LighthouseModelXMLPersistence(xmlModel).load(LHTestDataFiles.XML_UPDATED_MODEL);
		
		for (LighthouseEvent event : xmlModel.getListEvents()) {
			event.setCommitted(true);
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date revisionTime = formatter.parse("2009-10-22 01:26:35");
		
		LighthouseAuthor author = new LighthouseAuthor("Max");
		
		LighthouseFile lhFile = BuildLHBaseFile.execute(xmlModel,"edu.prenticehall.deitel.ATM",revisionTime,author);
		
		LighthouseFile xmlLHFile = new LighthouseFile();
		new LighthouseFileXMLPersistence(xmlLHFile).load(LHTestDataFiles.XML_ATM_JAVA_MODIFIED);
		
		LighthouseDelta delta = new LighthouseDelta(author, lhFile, xmlLHFile);

		assertEquals(true, delta.getEvents().size()==0);
	}
	
}
