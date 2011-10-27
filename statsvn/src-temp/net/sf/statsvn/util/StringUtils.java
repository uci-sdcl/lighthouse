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

public class StringUtils {
    /**
     * This method is a 1.4 replacement of the String.replace(CharSequence, CharSequence) found in 1.5.
     * @param originalPattern
     * @param newPattern
     * @param originalString
     * @return
     */
    public static String replace(final String originalPattern, final String newPattern, final String originalString) {
        if ((originalPattern == null) || (originalPattern.length() == 0) || (originalString == null)) {
            return originalString;
        }

        final StringBuffer newString = new StringBuffer(originalString.length());
        int index = 0;
        final int originalLength = originalPattern.length();
        int previousIndex = 0;

        while ((index = originalString.indexOf(originalPattern, index)) != -1) {
            newString.append(originalString.substring(previousIndex, index)).append(newPattern);
            index += originalLength;
            previousIndex = index;
        }

        if (previousIndex == 0) {
            newString.append(originalString);
        } else if (previousIndex != originalString.length()) {
            newString.append(originalString.substring(previousIndex));
        }

        return newString.toString();
    }
}
