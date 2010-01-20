package edu.uci.lighthouse.ui.views;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.IFigureProvider;

import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseRelationship;
import edu.uci.lighthouse.ui.graph.UmlClassFigureTest;

public class LighthouseLabelProvider extends LabelProvider implements IFigureProvider {

	@Override
	public IFigure getFigure(Object element) {
		
		if (element instanceof LighthouseClass){
			IFigure fig = new UmlClassFigureTest((LighthouseClass)element);
			fig.setSize(-1,-1);
			return fig;
			//return new Label(element.toString());
		}
		//System.out.println(element.toString());
		return null;
		//return null;
	}

	@Override
	public String getText(Object element) {
		if ((element instanceof EntityConnectionData)||element instanceof LighthouseRelationship){
			return "";
		} else if (element instanceof LighthouseEntity){
			return ((LighthouseEntity) element).getShortName();
		}
		return super.getText(element);
	}

}
