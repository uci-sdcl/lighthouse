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

	private StringFieldEditor SVNUsername;
	
	private PreferencesNotifier notifier = PreferencesNotifier.getInstance();

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
		notifier.fireUserChanged();
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
