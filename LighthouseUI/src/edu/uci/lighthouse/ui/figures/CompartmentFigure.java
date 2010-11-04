package edu.uci.lighthouse.ui.figures;

import org.eclipse.draw2d.Panel;

import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.ui.figures.ILighthouseClassFigure.MODE;

public abstract class CompartmentFigure extends Panel {

	private LighthouseEntity umlClass;
	
	/** Indicates if the figure should be shown for the given mode */
	public abstract boolean isVisible(MODE mode);
	
	/** Populate the figure for the given mode */
	public abstract void populate(MODE mode);
	
	protected void setUmlClass(LighthouseEntity umlClass) {
		this.umlClass = umlClass;
	}

	protected LighthouseEntity getUmlClass() {
		return umlClass;
	}
	
}
