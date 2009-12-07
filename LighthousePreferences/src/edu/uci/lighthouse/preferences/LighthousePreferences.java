package edu.uci.lighthouse.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class LighthousePreferences extends PreferencePage implements
		IWorkbenchPreferencePage {
	
	private StringFieldEditor dbHost;
	private StringFieldEditor dbUsername;
	private StringFieldEditor dbPassword;
	private StringFieldEditor dbDatabase;
	private StringFieldEditor dbPort;
	
	private StringFieldEditor sshHost;
	private StringFieldEditor sshUser;
	private StringFieldEditor sshPassword;
	private StringFieldEditor sshPort;
	/*
	private StringFieldEditor dbHost;
	private StringFieldEditor dbUsername;
	private StringFieldEditor dbPassword;
	private StringFieldEditor dbDatabase;
	private StringFieldEditor dbPort;
	
	private StringFieldEditor sshHost;
	private StringFieldEditor sshUser;
	private StringFieldEditor sshPassword;
	private StringFieldEditor sshPort;
	*/

	@Override
	protected Control createContents(Composite parent) {
		Label label = new Label(parent, SWT.None);
		label.setText("Enter connection details below:");
		
		final TabFolder tabFolder = new TabFolder (parent, SWT.BORDER);
		
		TabItem item;
		
		item = new TabItem (tabFolder, SWT.NONE);
		item.setText ("Standard");
		Composite dbComposite = getDBComposite(tabFolder);
		item.setControl(dbComposite);
		
		
		
		item = new TabItem (tabFolder, SWT.NONE);
		item.setText ("SSH");
		Composite sshComposite = getSSHComposite(tabFolder);
		item.setControl(sshComposite);
		
			
		tabFolder.pack ();
		return tabFolder;
	}
	
	private Composite getDBComposite(Composite parent){
		Composite composite = new Composite(parent, SWT.NONE);

		dbHost = new StringFieldEditor("dbHost","Host",parent);
		dbUsername = new StringFieldEditor("dbUsername","Username",parent);
		dbPassword = new StringFieldEditor("dbPassword","Password",parent);
		dbDatabase = new StringFieldEditor("dbDatabase","Database",parent);
		dbPort = new StringFieldEditor("dbPort","Port",parent);
		
		return composite;
	}
	
	private Composite getSSHComposite(Composite parent){
		Composite composite = new Composite(parent, SWT.NONE);
		
		sshHost = new StringFieldEditor("sshHost","Host",parent);
		sshUser = new StringFieldEditor("sshUser","User",parent);
		sshPassword = new StringFieldEditor("sshPassword","Password",parent);
		sshPort = new StringFieldEditor("sshPort","Port",parent);
		
		return composite;
	}

	@Override
	public void init(IWorkbench workbench) {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		setPreferenceStore(store);
	}

	@Override
	protected void performApply() {
		super.performApply();
	}

	@Override
	protected void performDefaults() {
		super.performDefaults();
	}

	@Override
	public boolean performOk() {
		return super.performOk();
	}
	
}
