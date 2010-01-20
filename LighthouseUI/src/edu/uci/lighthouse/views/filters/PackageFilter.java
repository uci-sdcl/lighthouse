package edu.uci.lighthouse.views.filters;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseRelationship;

public class PackageFilter extends ViewerFilter{

	String packageName;
	
	public PackageFilter(String packageName){
		this.packageName = packageName;
	}
	
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof LighthouseClass) {
			LighthouseClass aClass = (LighthouseClass) element;
			if (packageName.equals(aClass.getPackageName())){
				return true;
			}
		} else if (element instanceof LighthouseRelationship){
			return true;
		}
		return false;
	}
}
