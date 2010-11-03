package edu.uci.lighthouse.views.filters;

import java.util.Collection;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.zest.core.viewers.EntityConnectionData;

import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseInterface;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseRelationship;

public class ActiveClassFilter extends ViewerFilter implements IFilter{

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof LighthouseClass || element instanceof LighthouseInterface) {
			LighthouseEntity aEntity = getLighthouseEntityFromEditor();
			if (aEntity != null) {
				if (aEntity.equals(element)
						|| belongsToDistance(aEntity, (LighthouseEntity) element)) {
					return true;
				}
			}
		} else if (element instanceof EntityConnectionData) {
			EntityConnectionData conn = (EntityConnectionData) element;
			LighthouseEntity aEntity = getLighthouseEntityFromEditor();
			if (aEntity != null) {
				if (conn.source.equals(aEntity) || conn.dest.equals(aEntity)) {
					return belongsToDistance((LighthouseEntity) conn.source,
							(LighthouseEntity) conn.dest);
				}
			}
//			return true;
		} else if (element instanceof LighthouseRelationship) {
			// FIXME: try to do something similar the if block above
			return true;
		}
		return false;
	}

	public LighthouseEntity getLighthouseEntityFromEditor() {
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		IWorkbenchPage activePage = window.getActivePage();
		if (activePage != null) {
			IEditorPart editor = activePage.getActiveEditor();
			if (editor != null) {
				IJavaElement jFile = JavaUI.getEditorInputJavaElement(editor
						.getEditorInput());
				if (jFile instanceof ICompilationUnit) {
					IType type = ((ICompilationUnit) jFile).findPrimaryType();
					String fqn = jFile.getJavaProject().getElementName() + "."
							+ type.getFullyQualifiedName();
					LighthouseEntity entity = LighthouseModel.getInstance()
							.getEntity(fqn);
					if (entity instanceof LighthouseClass || entity instanceof LighthouseInterface) {
						return entity;
					}
				}
			}
		}
		return null;
	}

	private boolean belongsToDistance(LighthouseEntity from, LighthouseEntity to) {
		LighthouseModel model = LighthouseModel.getInstance();
		Collection<LighthouseEntity> related = model.getConnectTo(from);
		// test this because is faster
		for (LighthouseEntity toEntity : related) {
			if (toEntity.equals(to)) {
				return true;
			}
		}
		// otherwise test everything
		// for (LighthouseClass fromClass : model.getAllClasses()) {
		related = model.getConnectTo(to);
		for (LighthouseEntity fromEntity : related) {
			if (fromEntity.equals(from)) {
				return true;
			}
		}
		// }
		return false;
	}

}
