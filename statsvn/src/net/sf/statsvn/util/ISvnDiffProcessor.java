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

import java.io.IOException;
import java.util.Vector;

/**
 * Performs svn diff queries. 
 * @author jkealey
 *
 */
public interface ISvnDiffProcessor {

    /**
     * Returns line count differences between two revisions of a file.
     * 
     * @param oldRevNr
     *            old revision number
     * @param newRevNr
     *            new revision number
     * @param filename
     *            the filename
     * @return A int[2] array of [lines added, lines removed] is returned.
     * @throws IOException
     *             problem parsing the stream
     * @throws BinaryDiffException
     *             if the error message is due to trying to diff binary files.
     */
    public abstract int[] getLineDiff(final String oldRevNr, final String newRevNr, final String filename) throws IOException, BinaryDiffException;

    /**
    * Returns line count differences for all files in a particular revision.
    * 
    * @param newRevNr
    *            new revision number
    * @return A vector of object[3] array of [filename, int[2](lines added, lines removed), isBinary] is returned.
    * @throws IOException
    *             problem parsing the stream
    * @throws BinaryDiffException
    *             if the error message is due to trying to diff binary files.
    */
    public abstract Vector getLineDiff(final String newRevNr) throws IOException, BinaryDiffException;

}
