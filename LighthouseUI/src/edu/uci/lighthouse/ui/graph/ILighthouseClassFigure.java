package edu.uci.lighthouse.ui.graph;

import edu.uci.lighthouse.model.LighthouseEntity;

public interface ILighthouseClassFigure {
	/** Lighthouse diagram modes */
	public static enum MODE {ONE, TWO, THREE, FOUR};
	
	/** Populate the figure with the given mode */
	public void populate(MODE level);
	
	/** Return the current diagram mode */
	public MODE getCurrentLevel();
	
	/** Returns the <code>LighthouseEntity</code> related with the class for the given position. */
	public LighthouseEntity findLighthouseEntityAt(int x, int y);
}
