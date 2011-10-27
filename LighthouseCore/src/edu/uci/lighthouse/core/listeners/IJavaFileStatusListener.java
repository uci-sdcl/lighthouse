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
package edu.uci.lighthouse.core.listeners;

import org.eclipse.core.resources.IFile;

/**
 * This interface is used for implementers receive events related to the java
 * files.
 * 
 * @author tproenca
 * @see JavaFileChangedReporter
 */
public interface IJavaFileStatusListener {

	/**
	 * Is called when a file is opened. The flag <code>hasErrors</code>
	 * indicates whether this file has compilation errors or not.
	 * 
	 * @param iFile
	 *            the file existing in the workspace
	 * @param hasErrors
	 *            <code>true</code> if the file has compilation errors
	 */
	public void open(IFile iFile, boolean hasErrors);

	/**
	 * Is called when a file is closed. The flag <code>hasErrors</code>
	 * indicates whether this file has compilation errors or not.
	 * 
	 * @param iFile
	 *            the file existing in the workspace
	 * @param hasErrors
	 *            <code>true</code> if the file has compilation errors
	 */
	public void close(IFile iFile, boolean hasErrors);

	/**
	 * Is called when a file is added. The flag <code>hasErrors</code> indicates
	 * whether this file has compilation errors or not.
	 * 
	 * @param iFile
	 *            the file existing in the workspace
	 * @param hasErrors
	 *            <code>true</code> if the file has compilation errors
	 */
	public void add(IFile iFile, boolean hasErrors);

	/**
	 * Is called when a file is removed. The flag <code>hasErrors</code>
	 * indicates whether this file has compilation errors or not.
	 * 
	 * @param iFile
	 *            the file existing in the workspace
	 * @param hasErrors
	 *            <code>true</code> if the file has compilation errors
	 */
	public void remove(IFile iFile, boolean hasErrors);

	/**
	 * Is called when the user modifies the file and save it. The flag
	 * <code>hasErrors</code> indicates whether this file has compilation errors
	 * or not.
	 * 
	 * @param iFile
	 *            the file existing in the workspace
	 * @param hasErrors
	 *            <code>true</code> if the file has compilation errors
	 */
	public void change(IFile iFile, boolean hasErrors);

}
