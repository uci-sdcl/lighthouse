package edu.uci.lighthouse.ui.figures;

import org.eclipse.draw2d.IFigure;

import edu.uci.lighthouse.model.LighthouseEntity;

public interface ILighthouseClassFigure extends IFigure {
	/** Lighthouse diagram modes */
	public static enum MODE {ONE, TWO, THREE, FOUR};
	
	/** Populate the figure for the given mode */
	public void populate(MODE mode);
	
	/** Return the current diagram mode */
	public MODE getCurrentLevel();
	
	/** Returns the <code>LighthouseEntity</code> related with the class for the given position. */
	public LighthouseEntity findLighthouseEntityAt(int x, int y);
}
