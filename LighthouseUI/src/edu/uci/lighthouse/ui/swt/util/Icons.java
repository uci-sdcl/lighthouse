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
package edu.uci.lighthouse.ui.swt.util;

final public class Icons {
	public static final String CLASS = "/icons/class_obj.gif";
	public static final String PRIVATE_FIELD = "/icons/field_private_obj.gif";
	public static final String PUBLIC_METHOD = "/icons/methpub_obj.gif";
	public static final String COLLAPSE = "/icons/icon_collapse_all.png";
	
	public static final String EVENT_ADD = "/icons/plus_black_16.gif";
	public static final String EVENT_REMOVE = "/icons/minus_black_16.gif";
	public static final String EVENT_MODIFY = "/icons/triangle_black_16.gif";
	
	public static final String ACTION_LINK_WITH_EDITOR = "/icons/synced.gif";
	public static final String ACTION_CHANGE_LAYOUT = "/icons/frlayout.gif";
	
	public static final String IN_REPOSITORY = "/icons/in_repository.gif";
	
	private Icons() {
		throw new AssertionError();
	}
}
