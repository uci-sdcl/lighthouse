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
package edu.uci.lighthouse.parser;

import java.util.Properties;

public class ParserFactory {

	private final static String propertiesFile = "/parser.properties";
	
	public static IParser getDefaultParser() throws ParserException{
		IParser parser = null;
		try {
			Properties properties = new Properties();		
			properties.load(ParserFactory.class.getResourceAsStream(propertiesFile));
			String classFqn = properties.getProperty("parser");
			if (classFqn == null) {
				throw new ParserException("Parser class not found.");
			} else {
				Class<?> aClass = Class.forName(classFqn);
				parser = (IParser) aClass.newInstance();
			}
		} catch (Exception e) {
			throw new ParserException(e);
		}
		return parser;
	}
	
}
