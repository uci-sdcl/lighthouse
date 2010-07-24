package edu.uci.lighthouse.core.commands;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.IStructuredSelection;

import edu.uci.lighthouse.model.LighthouseModel;

public class ProjectPropertyTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if (receiver instanceof IStructuredSelection) {
			LighthouseModel model = LighthouseModel.getInstance();
			Collection<String> projectNames = model.getProjectNames();
			IStructuredSelection selections = (IStructuredSelection) receiver;
			for (Iterator itSelections = selections.iterator(); itSelections
					.hasNext();) {
				Object selection = itSelections.next();
				if (selection instanceof IJavaProject) {
					IJavaProject jProject = (IJavaProject) selection;
					if (projectNames.contains(jProject.getElementName())) {
						return false;
					}
				}
			}
		}
		return true;
	}

}
