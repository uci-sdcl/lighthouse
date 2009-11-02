package edu.uci.lighthouse.core.listeners;

import org.eclipse.core.resources.IFile;
import org.tigris.subversion.svnclientadapter.ISVNInfo;

public interface ISVNEventListener {

	public void checkout(IFile iFile, ISVNInfo info);
	
	public void update(IFile iFile, ISVNInfo info);
	
	public void commit(IFile iFile, ISVNInfo info);
	
}
