package edu.uci.lighthouse.model;

import java.util.LinkedHashSet;
import java.util.List;

public class LighthouseModelUtil {

	// used by BuildLHBaseFile and PullModel
	public static boolean isValidRelationship(Object artifact, LinkedHashSet<LighthouseEntity> listEntitiesInside) {
		boolean result = true; 
		LighthouseRelationship rel = (LighthouseRelationship) artifact;
		if (!listEntitiesInside.contains(rel.getFromEntity())) {
			if (	rel.getType()==LighthouseRelationship.TYPE.CALL 
					|| rel.getType()==LighthouseRelationship.TYPE.USES
					|| rel.getType()==LighthouseRelationship.TYPE.HOLDS
					|| rel.getType()==LighthouseRelationship.TYPE.RECEIVES
					|| rel.getType()==LighthouseRelationship.TYPE.RETURN) {
				result = false;
			}
		}
		return result;
	}
	
	// used by BuildLHBaseFile and PullModel
	public static boolean wasEventRemoved(List<LighthouseEvent> listEvents, LighthouseEvent paramEvent) {
		for (LighthouseEvent event : listEvents) {
			if (event.getArtifact().equals(paramEvent.getArtifact())) {
				if (	event.isCommitted() &&
						event.getType() == LighthouseEvent.TYPE.REMOVE && 
						(	event.getCommittedTime().after(paramEvent.getCommittedTime()) ||
							event.getCommittedTime().equals(paramEvent.getCommittedTime()) ) ) {
					return true;
				}
			}
		}
		return false;
	}
	
}
