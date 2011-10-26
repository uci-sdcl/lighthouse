/*******************************************************************************
 * Copyright (c) {2009,2011} {Software Design and Collaboration Laboratory (SDCL)
 *				, University of California, Irvine}.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    {Software Design and Collaboration Laboratory (SDCL)
 *	, University of California, Irvine} 
 *			- initial API and implementation and/or initial documentation
 *******************************************************************************/ 
package edu.uci.lighthouse.ui.swt.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;

public final class FontFactory {

	public static Font classTitleBold = new Font(null, "", 10, SWT.BOLD);
	
	private FontFactory() {
		throw new AssertionError();
		
	}
}
