package edu.uci.lighthouse.views.filters;

import java.util.Collection;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseModel;

public class ViewModifiedFilter extends ViewerFilter{

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		//TODO Filter the connections
//		LighthouseModel model = (LighthouseModel) parentElement;
//		if (element instanceof LighthouseClass) {
//			LighthouseClass aClass = (LighthouseClass) element;
//			if (aClass.getEvents().size() > 0) {
//				return true;
//			} else {
//				Collection<LighthouseEntity> entities = model
//						.getMethodsAndAttributesFromClass(aClass);
//				for (LighthouseEntity entity : entities) {
//					if (entity.getEvents().size() > 0) {
//						return true;
//					}
//				}
//			}
//		}
		return false;
	}

}
