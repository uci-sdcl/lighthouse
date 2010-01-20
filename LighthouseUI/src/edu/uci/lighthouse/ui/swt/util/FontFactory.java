package edu.uci.lighthouse.ui.swt.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;

public final class FontFactory {

	public static Font classTitleBold = new Font(null, "", 10, SWT.BOLD);
	
	private FontFactory() {
		throw new AssertionError();
		
	}
}
