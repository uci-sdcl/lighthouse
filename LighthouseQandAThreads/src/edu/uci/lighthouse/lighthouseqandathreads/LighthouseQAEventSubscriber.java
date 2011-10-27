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
package edu.uci.lighthouse.lighthouseqandathreads;

import java.io.Serializable;
import java.util.List;
import java.util.Observable;

import javax.persistence.Entity;

import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;

///import org.eclipse.zest.core.widgets.GraphNode;

import edu.uci.lighthouse.core.data.ISubscriber;
import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
//import edu.uci.lighthouse.ui.utils.GraphUtils;
import edu.uci.lighthouse.ui.utils.GraphUtils;
import edu.uci.lighthouse.ui.views.EmergingDesignView;


public class LighthouseQAEventSubscriber  implements ISubscriber{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6013962858601965104L;

	public void receive(List<LighthouseEvent> events) {
		System.out.println("[LighthouseQAEventSubscriber.recieve]");
		for(LighthouseEvent event: events){
			if(event instanceof LighthouseEvent){
				//refresh updates sent by others
				
				if(event.getArtifact() != null && event.getArtifact() instanceof LighthouseEntity){
					LighthouseEntity entity = (LighthouseEntity)event.getArtifact();
					System.out.println("artifact is a lighthouse entity");
					if(entity != null && GraphUtils.getGraphViewer().findGraphItem(entity) != null){
						//update model
						LighthouseModelManager manager = new LighthouseModelManager(LighthouseModel.getInstance());
						LighthouseEntity clazz = manager.getMyClass(entity);
						
						if(clazz instanceof LighthouseClass){
							LighthouseClass theClazz = (LighthouseClass)clazz;
							theClazz.setForum(((LighthouseClass)entity).getForum());
						}
						System.out.println("[Rebuilding figure]");
						//rebuild figure
						GraphUtils.rebuildFigureForEntity(entity);
					}
				}
			}
		}
	}

}
