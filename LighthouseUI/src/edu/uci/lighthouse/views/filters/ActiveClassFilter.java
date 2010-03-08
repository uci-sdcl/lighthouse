package edu.uci.lighthouse.views.filters;

import java.util.Collection;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.zest.core.viewers.EntityConnectionData;

import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseRelationship;

public class ActiveClassFilter extends ViewerFilter {

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof LighthouseClass) {
			LighthouseClass aClass = getLighthouseClassFromEditor();
			if (aClass != null) {
				if (aClass.equals(element)
						|| belongsToDistance(aClass, (LighthouseClass) element)) {
					return true;
				}
			}
		} else if (element instanceof EntityConnectionData) {
			EntityConnectionData conn = (EntityConnectionData) element;
			LighthouseClass aClass = getLighthouseClassFromEditor();
			if (aClass != null) {
				if (conn.source.equals(aClass) || conn.dest.equals(aClass)) {
					return belongsToDistance((LighthouseClass) conn.source,
							(LighthouseClass) conn.dest);
				}
			}
//			return true;
		} else if (element instanceof LighthouseRelationship) {
			// FIXME: try to do something similar the if block above
			return true;
		}
		return false;
	}

	private LighthouseClass getLighthouseClassFromEditor() {
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		IEditorPart editor = window.getActivePage().getActiveEditor();
		if (editor != null) {
			IJavaElement jFile = JavaUI.getEditorInputJavaElement(editor
					.getEditorInput());
			if (jFile instanceof ICompilationUnit) {
				IType type = ((ICompilationUnit) jFile).findPrimaryType();
				String fqn = jFile.getJavaProject().getElementName() + "."
						+ type.getFullyQualifiedName();
				LighthouseEntity entity = LighthouseModel.getInstance()
						.getEntity(fqn);
				if (entity instanceof LighthouseClass) {
					return (LighthouseClass) entity;
				}
			}
		}
		return null;
	}

	private boolean belongsToDistance(LighthouseClass from, LighthouseClass to) {
		LighthouseModel model = LighthouseModel.getInstance();
		Collection<LighthouseClass> related = model.getConnectTo(from);
		// test this because is faster
		for (LighthouseClass toClass : related) {
			if (toClass.equals(to)) {
				return true;
			}
		}
		// otherwise test everything
		// for (LighthouseClass fromClass : model.getAllClasses()) {
		related = model.getConnectTo(to);
		for (LighthouseClass fromClass : related) {
			if (fromClass.equals(from)) {
				return true;
			}
		}
		// }
		return false;
	}

}
