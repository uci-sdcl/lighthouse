package edu.uci.lighthouse.core.listeners;

import org.eclipse.core.resources.IFile;

public interface IJavaFileStatusListener {

	public void open(IFile file, boolean hasErrors);
	
	public void close(IFile file, boolean hasErrors);
	
	public void change(IFile file, boolean hasErrors);
	
}
