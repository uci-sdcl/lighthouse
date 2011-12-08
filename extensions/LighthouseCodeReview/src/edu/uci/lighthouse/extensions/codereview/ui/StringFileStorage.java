package edu.uci.lighthouse.extensions.codereview.ui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class StringFileStorage implements IStorage {

	private String filename;
	private String content;
	
	public StringFileStorage(String filename, String content) {
		this.filename = filename;
		this.content = content;
	}
	
	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getContents() throws CoreException {
		// TODO Auto-generated method stub
		return new ByteArrayInputStream(content.getBytes());
	}

	@Override
	public IPath getFullPath() {
		return Path.fromOSString(filename);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return filename;
	}

	@Override
	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return true;
	}

}
