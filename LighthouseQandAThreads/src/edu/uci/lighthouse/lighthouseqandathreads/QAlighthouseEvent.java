package edu.uci.lighthouse.lighthouseqandathreads;

import edu.uci.lighthouse.model.IPluginLighthouseEvent;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseEvent.TYPE;

public class QAlighthouseEvent extends LighthouseEvent implements IPluginLighthouseEvent{

	public QAlighthouseEvent(TYPE modify, LighthouseAuthor author,
			LighthouseEntity entity) {
		super(modify,author,entity);
	}

}
