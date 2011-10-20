package edu.uci.lighthouse.ui.views;

import org.eclipse.swt.graphics.Color;

public interface IHightlightAction {
	
	public static int HIGHLIGHT = 3;
	
	public static int LINK_WITH_EDITOR = 2;
	
	public static int SOFT_LOCK = 1;

	public int getPriority();
	
	public Color getColor();
	
}
