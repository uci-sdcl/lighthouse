package edu.uci.lighthouse.extensions.codereview.ui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import edu.uci.lighthouse.extensions.codereview.model.CodeReview;
import edu.uci.lighthouse.extensions.codereview.model.CodeReviewModel;
import edu.uci.lighthouse.extensions.codereview.model.FileSnapshot;
import edu.uci.lighthouse.model.LighthouseAuthor;

public class FileSnapshotStorage implements IStorage {

	private FileSnapshot fileSnapshot;
	private LighthouseAuthor reviewee;
	
	public FileSnapshotStorage(FileSnapshot fileSnapshot) {
		this.fileSnapshot = fileSnapshot;
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public InputStream getContents() throws CoreException {
		return new ByteArrayInputStream(fileSnapshot.getContent().getBytes());
	}

	@Override
	public IPath getFullPath() {
		return Path.fromOSString(fileSnapshot.getFilename());
	}

	@Override
	public String getName() {
		return fileSnapshot.getFilename();
	}

	@Override
	public boolean isReadOnly() {
		return true;
	}
	
	public LighthouseAuthor getReviewee() {
		if (reviewee == null) {
			CodeReview codeReview = CodeReviewModel.getInstance().getReview(fileSnapshot);
			reviewee = codeReview.getReviewee();
		}
		return reviewee;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof FileSnapshotStorage &&
				fileSnapshot.getId().equals(((FileSnapshotStorage)obj).fileSnapshot.getId());
	}
}
