package edu.uci.lighthouse.views.filters;

import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModel;

public interface IClassFilter extends IFilter {

	public boolean select(LighthouseModel model, LighthouseEntity entity);
	
	public boolean select(LighthouseModel model, LighthouseEvent event);
	
}
