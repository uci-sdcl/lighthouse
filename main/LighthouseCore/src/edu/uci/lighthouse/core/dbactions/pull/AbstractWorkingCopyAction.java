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
package edu.uci.lighthouse.core.dbactions.pull;

import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IFile;
import org.tigris.subversion.svnclientadapter.ISVNInfo;

import edu.uci.lighthouse.core.controller.WorkingCopy;
import edu.uci.lighthouse.core.dbactions.IDatabaseAction;
import edu.uci.lighthouse.core.util.ModelUtility;

@SuppressWarnings("serial")
public abstract class AbstractWorkingCopyAction implements IDatabaseAction {

	private Map<IFile, ISVNInfo> svnFiles;
	
	protected WorkingCopy workingCopy;

	public AbstractWorkingCopyAction(Map<IFile, ISVNInfo> svnFiles) {
		this.svnFiles = svnFiles;
	}

	public Map<IFile, ISVNInfo> getSVNFiles() {
		return svnFiles;
	}
	
	protected WorkingCopy getWorkingCopy() {
		if (workingCopy == null) {
			workingCopy = new WorkingCopy();
			for (Entry<IFile, ISVNInfo> entry : svnFiles.entrySet()) {
				if (ModelUtility.belongsToImportedProjects(entry.getKey(),
						checkDatabase())) {
					String fqn = ModelUtility.getClassFullyQualifiedName(entry
							.getKey());
					if (fqn != null) {
						ISVNInfo svnInfo = entry.getValue();
						workingCopy.put(fqn, svnInfo.getLastChangedDate());
					}
				}
			}
		}
		return workingCopy;
	}
	
	protected abstract boolean checkDatabase();
}
