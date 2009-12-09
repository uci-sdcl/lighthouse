package edu.uci.lighthouse.core.preferences;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import edu.uci.lighthouse.core.Activator;
import edu.uci.lighthouse.model.LighthouseAuthor;

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
//		StringFieldEditor feName = new StringFieldEditor("name","Name:",getFieldEditorParent());
//		addField(feName);
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
	
	public static Map<String, String> getUserSettings(){
		Map<String, String> userSettings = new HashMap<String,String>();
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		
		userSettings.put(USERNAME,store.getString(USERNAME));
		
		return userSettings;
	}
}
