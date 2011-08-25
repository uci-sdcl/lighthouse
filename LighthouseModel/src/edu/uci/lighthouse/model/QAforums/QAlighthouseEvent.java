package edu.uci.lighthouse.model.QAforums;

import java.io.Serializable;

import javax.persistence.Entity;

import edu.uci.lighthouse.model.PluginLighthouseEvent;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseEvent.TYPE;

@Entity
public class QAlighthouseEvent extends  PluginLighthouseEvent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1684650099628218050L;

	public QAlighthouseEvent(TYPE modify, LighthouseAuthor author,
			LighthouseEntity entity) {
		super(modify,author,entity);
	}

}
