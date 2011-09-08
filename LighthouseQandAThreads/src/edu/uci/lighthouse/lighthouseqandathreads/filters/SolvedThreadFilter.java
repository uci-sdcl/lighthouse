package edu.uci.lighthouse.lighthouseqandathreads.filters;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.QAforums.LHforum;

public class SolvedThreadFilter extends ViewerFilter{

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof LighthouseClass) {
			LighthouseClass aClass = (LighthouseClass) element;
			LHforum forum = aClass.getForum();
			
			return forum.hasSolvedThreads();
			
		} 
		return false;
	}

}