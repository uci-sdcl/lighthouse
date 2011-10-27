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
package net.sf.statsvn.util;

import java.io.Serializable;
import java.util.Comparator;

public class FilenameComparator implements Comparator, Serializable {
	private static final long serialVersionUID = 3456265631104179922L;

	public int compare(final Object arg0, final Object arg1) {
		if (arg0 == null || arg1 == null) {
			return 0;
		}

		final String s0 = arg0.toString().replace('/', '\t');
		final String s1 = arg1.toString().replace('/', '\t');

		return s0.compareTo(s1);
	}
}
