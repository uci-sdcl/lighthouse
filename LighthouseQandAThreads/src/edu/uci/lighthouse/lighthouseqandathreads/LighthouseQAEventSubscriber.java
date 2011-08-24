package edu.uci.lighthouse.lighthouseqandathreads;

import java.io.Serializable;
import java.util.List;
import java.util.Observable;

import edu.uci.lighthouse.core.data.ISubscriber;
import edu.uci.lighthouse.model.LighthouseEvent;

public class LighthouseQAEventSubscriber  implements ISubscriber, Serializable{

	public void receive(List<LighthouseEvent> events) {
		System.out.println(events.size());
	}

}
