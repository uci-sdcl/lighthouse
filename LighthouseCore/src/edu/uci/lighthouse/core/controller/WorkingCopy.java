package edu.uci.lighthouse.core.controller;

import java.util.Date;
import java.util.HashMap;

import edu.uci.lighthouse.core.util.WorkbenchUtility;
import edu.uci.lighthouse.services.persistence.IPersistable;

public class WorkingCopy extends HashMap<String, Date> implements IPersistable {

	private static final long serialVersionUID = -1572792397908630831L;
	
	private static final String filename = "workingCopy.bin";
	
	public String getFileName() {
		return WorkbenchUtility.getMetadataDirectory() + filename;
	}
}
