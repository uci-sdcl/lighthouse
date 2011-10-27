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
package net.sf.statsvn.input;

import java.io.IOException;

import junit.framework.TestCase;
import net.sf.statcvs.input.LogSyntaxException;
import net.sf.statcvs.output.ConfigurationException;
import net.sf.statcvs.output.ConfigurationOptions;
import net.sf.statsvn.Main;
import net.sf.statsvn.output.SvnCommandLineParser;

/**
 * High-level scenarios to verify parsing without actually needing a server.
 *
 * @author jkealey
 *
 */
public class SvnLogfileParserTest extends TestCase {

	protected final static String sRoot = "./tests-src/net/sf/statsvn/input/samplefiles/";

	protected RepositoryFileManager repFileMan;

	public void testJUCMNav1() throws ConfigurationException, IOException, LogSyntaxException {
		final String[] args = { "-title", "jUCMNav", "-output-dir", sRoot + "stats", sRoot + "seg.jUCMNav.log", sRoot, "-cache-dir", sRoot };
		new SvnCommandLineParser(args).parse();
		repFileMan = new DummyRepositoryFileManager(ConfigurationOptions.getCheckedOutDirectory(), sRoot + "seg.jUCMNav.info", sRoot + "seg.jUCMNav.propget",
		        sRoot + "seg.jUCMNav.linecounts");
		Main.generateDefaultHTMLSuite(repFileMan);
	}
}
