package edu.uci.lighthouse.model.QAforums;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrimaryKeyJoinColumn;

import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.PluginLighthouseEvent;
import edu.uci.lighthouse.model.LighthouseEvent.TYPE;


@Entity
@DiscriminatorValue("QA")
@PrimaryKeyJoinColumn(name="QA_ID")
public class QAlighthouseEvent extends  PluginLighthouseEvent{


	/**
	 * 
	 */
	private static final long serialVersionUID = -1684650099628218050L;

	public QAlighthouseEvent(TYPE modify, LighthouseAuthor author,
			LighthouseEntity entity) {
		super(modify,author,entity);
	}

}
