package edu.uci.lighthouse.ui.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseRelationship;

public class LighthouseEntityContentProvider implements IGraphEntityContentProvider{

	private static Logger logger = Logger.getLogger(LighthouseEntityContentProvider.class);
	
	@Override
	public Object[] getConnectedTo(Object entity) {
		List<LighthouseEntity> result = new ArrayList<LighthouseEntity>();
		LighthouseModel model = LighthouseModel.getInstance();
		Collection<LighthouseRelationship> fromList = model.getRelationshipsFrom((LighthouseEntity)entity);
		for (LighthouseRelationship r : fromList) {	
			if (r.getFromEntity() != r.getToEntity()){
			result.add(r.getToEntity());
			}
		}
		return result.toArray();
	}

	@Override
	public Object[] getElements(Object inputElement) {
		List<LighthouseClass> result = new ArrayList<LighthouseClass>();
		if (inputElement instanceof LighthouseModel) {
			LighthouseModel model = (LighthouseModel) inputElement;
			for (LighthouseClass c : model.getAllClasses()) {
				result.add(c);
			}
		}
		return result.toArray();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {	
		logger.info("inputChanged");
	}

}