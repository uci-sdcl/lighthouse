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
	public void classChanged(LighthouseClass aClass, LighthouseEvent.TYPE type);
	
	/** */
	public void relationshipChanged(LighthouseRelationship relationship, LighthouseEvent.TYPE type);
}
