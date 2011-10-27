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
import java.util.List;

/**
 * 
 * Performs svn propget queries. 
 * 
 * @author jkealey
 *
 */
public interface ISvnPropgetProcessor {

    /**
     * Returns the list of binary files in the working directory.
     * 
     * @return the list of binary files
     */
    public abstract List getBinaryFiles();

    /**
     * It was first thought that a the mime-type of a file's previous revision
     * could be found. This is not the case. Leave revision null until future
     * upgrade of svn propget command line.
     * 
     * @param revision
     *            the revision to query
     * @param filename
     *            the filename
     * @return if that version of a file is binary
     */
    public abstract boolean isBinaryFile(final String revision, final String filename);

    
    /**
     * Loads the list of binary files from the input stream equivalent to an svn
     * propget command.
     * 
     * @param path
     *            a file on disk which contains the results of an svn propget
     */ 
    public void loadBinaryFiles(final String path) throws IOException;
}
