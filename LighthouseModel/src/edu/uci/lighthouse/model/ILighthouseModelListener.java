package edu.uci.lighthouse.model;

/**
 * 
 * @author tproenca
 *
 */
public interface ILighthouseModelListener {
	
	/** */
	public void modelChanged();
	
	/** */
	public void classChanged(LighthouseEntity aClass, LighthouseEvent.TYPE type);
	
	/** */
	public void relationshipChanged(LighthouseRelationship relationship, LighthouseEvent.TYPE type);
}
