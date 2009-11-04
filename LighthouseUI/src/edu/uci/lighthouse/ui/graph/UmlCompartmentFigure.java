package edu.uci.lighthouse.ui.graph;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridLayout;

import edu.uci.lighthouse.ui.swt.util.ColorFactory;

public class UmlCompartmentFigure extends Figure {
	
	public UmlCompartmentFigure(){
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		layout.numColumns = 2;		
		setLayoutManager(layout);	
	}

}
