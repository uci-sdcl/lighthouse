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

import java.util.Properties;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import edu.uci.lighthouse.core.Activator;

public class UserPreferences extends FieldEditorPreferencePage implements
IWorkbenchPreferencePage {

	StringFieldEditor SVNUsername;

	public static final String USERNAME = "svnUsername";
	
	public UserPreferences(){
		super(FieldEditorPreferencePage.GRID);
	}
	
	@Override
	public void init(IWorkbench workbench) {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		setPreferenceStore(store);
		setDescription("Enter user information:");
	}

	@Override
	protected void createFieldEditors() {
		SVNUsername = new StringFieldEditor(USERNAME,"Username:",getFieldEditorParent());
		addField(SVNUsername); 
	}
	
	@Override
	public boolean performOk() {
		if ("".equals(SVNUsername.getStringValue())){
			MessageDialog.openError(getShell(), "Lighthouse User Preferences", "Username is required!");
			return false;
		}
		return super.performOk();
	}
	
	public static Properties getUserSettings(){
		Properties userSettings = new Properties();
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		userSettings.setProperty(USERNAME,store.getString(USERNAME));
		return userSettings;
	}
	
	public static void clear() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setValue(USERNAME, "");
	}
}
