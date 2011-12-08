package edu.uci.lighthouse.extensions.codereview.ui;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

public class FileSnapshotEditorInput  extends PlatformObject implements IStorageEditorInput {

	private FileSnapshotStorage storage;
	
	public FileSnapshotEditorInput(FileSnapshotStorage storage) {
		this.storage = storage;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return storage.getReviewee().getName()+": "+storage.getName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return getName();
	}

	@Override
	public IStorage getStorage() {
		return storage;
	}

	@Override
	public boolean equals(Object obj) {
		 return obj instanceof FileSnapshotEditorInput &&
		         getStorage().equals(((FileSnapshotEditorInput)obj).getStorage());
	}
	
	
}
