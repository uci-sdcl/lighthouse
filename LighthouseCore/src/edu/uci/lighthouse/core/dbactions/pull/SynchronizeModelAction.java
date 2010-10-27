package edu.uci.lighthouse.core.dbactions.pull;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.tigris.subversion.svnclientadapter.ISVNInfo;

import edu.uci.lighthouse.core.controller.WorkingCopy;
import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.core.util.WorkbenchUtility;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.jpa.JPAException;

public class SynchronizeModelAction extends CheckoutAction {

	private static final long serialVersionUID = 813072327005798103L;

	public SynchronizeModelAction(Map<IFile, ISVNInfo> svnFiles) {
		super(svnFiles);
	}

	@Override
	public void run() throws JPAException {
		LighthouseModel.getInstance().clear();
		super.run();
		WorkbenchUtility.updateProjectIcon();
	}

	@Override
	protected WorkingCopy getWorkingCopy() {
		// Since the model is empty, does not make sense to verify if the files
		// belong to the import projects. So, we override this method removing
		// that check.
		//TODO (tproenca): Update this comment.
		if (workingCopy == null) {
			workingCopy = new WorkingCopy();
			Set<Entry<IFile, ISVNInfo>> entrySet = getSVNFiles().entrySet();
			for (Entry<IFile, ISVNInfo> entry : entrySet) {
				String fqn = ModelUtility.getClassFullyQualifiedName(entry
						.getKey());
				if (fqn != null) {
					ISVNInfo svnInfo = entry.getValue();
					Date revision = svnInfo
					.getLastChangedDate();
					if (revision == null) {
						revision = new Date(0);
					} else {
						revision = new Date(revision
								.getTime());
					}
					workingCopy.put(fqn, revision);
				}
			}
		}
		return workingCopy;
	}
}
