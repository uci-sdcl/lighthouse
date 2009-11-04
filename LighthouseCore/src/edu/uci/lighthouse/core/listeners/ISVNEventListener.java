package edu.uci.lighthouse.core.listeners;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.tigris.subversion.svnclientadapter.ISVNInfo;

public interface ISVNEventListener {

	public void checkout(Map<IFile, ISVNInfo> svnFiles);
	
	public void update(Map<IFile, ISVNInfo> svnFiles);
	
	public void commit(Map<IFile, ISVNInfo> svnFiles);
	
}
