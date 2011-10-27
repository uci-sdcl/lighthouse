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
package edu.uci.lighthouse.lighthouseqandathreads.markers;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.internal.ide.IMarkerImageProvider;

public class QuestionMarkerImageProvider implements IMarkerImageProvider{

	public static String ID = "edu.uci.lighthouse.LighthouseQandAThreads.imageprovider";
	
	
	public String getImagePath(IMarker marker) {
		return "/icons/question.png";
	}

}
