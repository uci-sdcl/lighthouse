package edu.uci.lighthouse.model.QAforums;

import java.io.Serializable;
import java.util.List;
import java.util.Observable;

import edu.uci.lighthouse.model.ISubscriber;
import edu.uci.lighthouse.model.LighthouseEvent;

public class LighthouseQAEventSubscriber  implements ISubscriber, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6013962858601965104L;

	public void receive(List<LighthouseEvent> events) {
		System.out.println(events.size());
	}

}
