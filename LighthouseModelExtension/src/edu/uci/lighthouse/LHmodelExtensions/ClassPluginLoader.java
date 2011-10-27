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
package edu.uci.lighthouse.LHmodelExtensions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;

public class ClassPluginLoader {

	private static ClassPluginLoader loader = null;

	private ClassPluginLoader() {

	}

	public static ClassPluginLoader getInstance() {
		if (loader == null) {
			loader = new ClassPluginLoader();
		}
		return loader;
	}

	public List<LHclassPluginExtension> loadClassPluginExtensions() {

		final ArrayList<LHclassPluginExtension> listOfExt = new ArrayList<LHclassPluginExtension>();
		
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(LHclassPluginExtension.LHclassPluginExtension_ID);
		try {
			for (IConfigurationElement e : config) {
				
				
				
				final Object o = e.createExecutableExtension("class");
				
				if (o instanceof LHclassPluginExtension) {
					
					System.out.println("Evaluating classExtension");

					
					ISafeRunnable runnable = new ISafeRunnable() {
						
				
						public void handleException(Throwable exception) {
							System.out.println("Exception in classExtension");
						}
				
						public void run() throws Exception {
							listOfExt.add((LHclassPluginExtension)o);
						}
					};
					SafeRunner.run(runnable);
				}
			}
		} catch (CoreException ex) {
			System.out.println(ex.getMessage());
		}

		return listOfExt;
	}
}
