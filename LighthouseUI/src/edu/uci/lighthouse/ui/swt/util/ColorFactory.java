package edu.uci.lighthouse.ui.swt.util;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;

public final class ColorFactory implements ColorConstants{
	
	public static final Color lightPurple = new Color(null, 197, 168, 212);
	public static final Color purple = new Color(null, 128, 0, 128);
	public static final Color aliceBlue = new Color(null,240,248,255);//240 248 255
	
	public static final Color classGradientBackground = new Color(null, 173, 214, 173);
	public static final Color classBorder = new Color(null, 63, 159, 63);
	
	public static final Color classBackground = ColorConstants.white;
	public static final Color classHighlight = ColorConstants.tooltipBackground;
	public static final Color classLinkWithEditor = aliceBlue;
	
	public static final Color classEventSeparator = new Color(null,239,239,239);
	
	public static final Color fieldReturn = new Color(null,149,125,71);
	
	private ColorFactory() {
		throw new AssertionError();
	}
}
