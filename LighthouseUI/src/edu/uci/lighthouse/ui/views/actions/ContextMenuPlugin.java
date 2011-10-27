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
package edu.uci.lighthouse.ui.views.actions;

import org.eclipse.jface.action.Action;

import edu.uci.lighthouse.ui.views.IContextMenuAction;
import edu.uci.lighthouse.ui.views.IHightlightAction;

/**
 * Plug-ins can extend the context menu by extending Lighthouse from this point
 * @author lee
 *
 */
public abstract class ContextMenuPlugin extends Action{

	public static String ID = "edu.uci.lighthouse.ui.figures.compartment";
	

}
