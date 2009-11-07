package edu.uci.lighthouse.core.listeners;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.tigris.subversion.svnclientadapter.ISVNInfo;

public interface ISVNEventListener {
	
	public void svnImport(Map<IFile, ISVNInfo> svnFiles);

	public void svnCheckout(Map<IFile, ISVNInfo> svnFiles);
	
	public void svnUpdate(Map<IFile, ISVNInfo> svnFiles);
	
	public void svnCommit(Map<IFile, ISVNInfo> svnFiles);
	
}
