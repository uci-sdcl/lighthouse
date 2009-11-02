package edu.uci.lighthouse.model.test;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.dom4j.DocumentException;

import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseModelManagerPersistence;
import edu.uci.lighthouse.model.io.LighthouseModelXMLPersistence;
import edu.uci.lighthouse.model.jpa.JPAUtilityException;
import edu.uci.lighthouse.test.util.LHTestDataFiles;
import edu.uci.lighthouse.test.util.LighthouseModelTest;
import junit.framework.TestCase;

public class LighthouseModelManagerPersistenceTest extends TestCase {

	public void testUpdateCommittedEntities() throws JPAUtilityException, DocumentException {
		LighthouseModelTest xmlModel = new LighthouseModelTest();
		new LighthouseModelXMLPersistence(xmlModel).load(LHTestDataFiles.XML_LH_MODEL);
		
		// Save LH Model Into the database in order to help the
		// BuilderRelationship to handle the External classes
		new LighthouseModelManagerPersistence(xmlModel).saveAllIntoDataBase();

		List<String> listClazzFqn = new ArrayList<String>();
		listClazzFqn.add("edu.prenticehall.deitel.Screen");
		listClazzFqn.add("edu.prenticehall.deitel.Transaction");

		LinkedHashSet<LighthouseEntity> listEntities = new LighthouseModelManagerPersistence(xmlModel).getEntitiesInsideClass(listClazzFqn);
		
		assertEquals(true, listEntities.size()==12);
	}
			
}
