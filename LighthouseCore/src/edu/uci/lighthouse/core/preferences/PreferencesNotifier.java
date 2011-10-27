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
package edu.uci.lighthouse.core.preferences;

import java.util.ArrayList;
import java.util.List;

public class PreferencesNotifier {

	private static PreferencesNotifier instance;
	
	private List<IPreferencesChangeListener> listeners = new ArrayList<IPreferencesChangeListener>();

	private PreferencesNotifier() {
	}
	
	public static PreferencesNotifier getInstance() {
		if (instance == null) {
			instance = new PreferencesNotifier();
		}
		return instance;
	}
	
	public void addPreferencesChangeListener(IPreferencesChangeListener listener) {
		listeners.add(listener);
	}
	
	public void removePreferencesChangeListener(IPreferencesChangeListener listener) {
		listeners.remove(listener);
	}
	
	protected void fireUserChanged() {
		for (IPreferencesChangeListener listener : listeners) {
			listener.userChanged();
		}
	}
	
	protected void fireDbSettingsChanged() {
		for (IPreferencesChangeListener listener : listeners) {
			listener.dbSettingsChanged();
		}
	}
}
