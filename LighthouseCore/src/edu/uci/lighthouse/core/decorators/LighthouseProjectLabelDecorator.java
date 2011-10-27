/*******************************************************************************
* Copyright (c) {2009,2011} {Software Design and Collaboration Laboratory (SDCL)
*				, University of California, Irvine}.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    {Software Design and Collaboration Laboratory (SDCL)
*	, University of California, Irvine}
*			- initial API and implementation and/or initial documentation
*******************************************************************************/
package edu.uci.lighthouse.core.decorators;

import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import edu.uci.lighthouse.core.Activator;
import edu.uci.lighthouse.model.LighthouseModel;

public class LighthouseProjectLabelDecorator implements ILightweightLabelDecorator {

	public static final String DECORATOR_ID = "edu.uci.lighthouse.decorators.project";
	private static final String ICON = "/icons/lhImported.png";
	
	@Override
	public void decorate(Object element, IDecoration decoration) {
		if (element instanceof IProject) {
			IProject project = (IProject) element;
			LighthouseModel model = LighthouseModel.getInstance();
			Collection<String> projectNames = model.getProjectNames();
			if (projectNames.contains(project.getName())) {
				decoration.addOverlay(AbstractUIPlugin
						.imageDescriptorFromPlugin(Activator.PLUGIN_ID, ICON));
			}
		}
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}
}
