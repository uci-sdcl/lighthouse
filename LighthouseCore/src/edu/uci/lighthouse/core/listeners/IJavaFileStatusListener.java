package edu.uci.lighthouse.core.listeners;

import org.eclipse.core.resources.IFile;

public interface IJavaFileStatusListener {

	public void open(IFile iFile, boolean hasErrors);
	
	public void close(IFile iFile, boolean hasErrors);
	
	public void removed(IFile iFile, boolean hasErrors);
	
	public void change(IFile iFile, boolean hasErrors);
	
}
