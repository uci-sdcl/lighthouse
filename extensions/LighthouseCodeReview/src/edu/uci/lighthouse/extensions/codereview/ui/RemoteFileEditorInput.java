package edu.uci.lighthouse.extensions.codereview.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

import edu.uci.lighthouse.extensions.codereview.model.CodeReview;
import edu.uci.lighthouse.extensions.codereview.model.CodeReviewModel;
import edu.uci.lighthouse.extensions.codereview.model.FileSnapshot;
import edu.uci.lighthouse.model.LighthouseAuthor;

public class RemoteFileEditorInput extends PlatformObject implements IStorageEditorInput, IFileEditorInput {
	private FileSnapshot fs;
	private IFile iFile;

	public RemoteFileEditorInput(IFile file, FileSnapshot fs) {
//		super(file);
		this.iFile = file;
		this.fs = fs;
	}

	@Override
	public String getName() {
		CodeReview codeReview = CodeReviewModel.getInstance().getReview(fs);
		LighthouseAuthor reviewee = codeReview.getReviewee();
		return reviewee+": "+iFile.getName();
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
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return getName();
	}

	@Override
	public IStorage getStorage() throws CoreException {
		return iFile;
	}
	
	@Override
	public boolean equals(Object obj) {
		 return obj instanceof RemoteFileEditorInput &&
		         iFile.equals(((RemoteFileEditorInput)obj).iFile);
	}

	@Override
	public IFile getFile() {
		return iFile;
	}
}
