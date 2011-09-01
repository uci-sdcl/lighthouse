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


public class LighthouseQAEventSubscriber  implements ISubscriber, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6013962858601965104L;

	public void receive(List<LighthouseEvent> events) {
		System.out.println("[LighthouseQAEventSubscriber.receive]");
		
		for(LighthouseEvent event: events){
			if(event instanceof LighthouseEvent){
				//refresh updates sent by others
				
				if(event.getArtifact() != null && event.getArtifact() instanceof LighthouseEntity){
					LighthouseEntity entity = (LighthouseEntity)event.getArtifact();
					if(entity != null && GraphUtils.getGraphViewer().findGraphItem(entity) != null){
						//update model
						LighthouseModelManager manager = new LighthouseModelManager(LighthouseModel.getInstance());
						LighthouseEntity clazz = manager.getMyClass(entity);
						
						if(clazz instanceof LighthouseClass){
							LighthouseClass theClazz = (LighthouseClass)clazz;
							theClazz.setForum(((LighthouseClass)entity).getForum());
						}
						//rebuild figure
						GraphUtils.rebuildFigureForEntity(entity);
					}
				}
			}
		}
	}

}
