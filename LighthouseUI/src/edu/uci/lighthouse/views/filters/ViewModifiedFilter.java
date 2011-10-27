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
package edu.uci.lighthouse.views.filters;

import java.util.Collection;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.zest.core.viewers.EntityConnectionData;

import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.LighthouseRelationship;

public class ViewModifiedFilter extends ViewerFilter{

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		//TODO Filter the connections
		if (element instanceof LighthouseClass) {
			LighthouseClass aClass = (LighthouseClass) element;
			return classHasEvents(aClass);
		} else if (element instanceof LighthouseRelationship || element instanceof EntityConnectionData){
			/* Not seeing purpose to keep this code. If find a situation that is need, please relate it here. */
			/*
			LighthouseRelationship relationship = (LighthouseRelationship) element;
			LighthouseModelManager modelManager = new LighthouseModelManager((LighthouseModel) parentElement);
			//FIXME: get from class cache
			LighthouseClass classFrom = modelManager.getMyClass(relationship.getFromEntity());
			LighthouseClass classTo = modelManager.getMyClass(relationship.getToEntity());
			return classHasEvents(classFrom) && classHasEvents(classTo);
			*/
			return true;
		} 
		return false;
	}

	private boolean classHasEvents(LighthouseClass aClass){
		LighthouseModel model = LighthouseModel.getInstance();
		Collection<LighthouseEvent> classEvents = model.getEvents(aClass);
		if (classEvents.size() > 0) {
			return true;
		} else {				
			Collection<LighthouseEntity> entities = model
					.getMethodsAndAttributesFromClass(aClass);
			for (LighthouseEntity entity : entities) {
				Collection<LighthouseEvent> entitiesEvents = model.getEvents(entity);
				if (entitiesEvents.size() > 0) {
					return true;
				}
			}
		}
		return false;
	}
	
}
