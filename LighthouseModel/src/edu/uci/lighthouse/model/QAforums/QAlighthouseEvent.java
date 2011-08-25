package edu.uci.lighthouse.model.QAforums;

import javax.persistence.Entity;

import edu.uci.lighthouse.model.IPluginLighthouseEvent;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseEvent.TYPE;

@Entity
public class QAlighthouseEvent extends LighthouseEvent implements IPluginLighthouseEvent{

	public QAlighthouseEvent(TYPE modify, LighthouseAuthor author,
			LighthouseEntity entity) {
		super(modify,author,entity);
	}

}
