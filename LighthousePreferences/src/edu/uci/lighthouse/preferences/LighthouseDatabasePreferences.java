package edu.uci.lighthouse.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class LighthouseDatabasePreferences extends PreferencePage implements
		IWorkbenchPreferencePage {
	
	private Text dbHost;
	private Text dbUsername;
	private Text dbPassword;
	private Text dbDatabase;
	private Text dbPort;
	
	private Text sshHost;
	private Text sshUser;
	private Text sshPassword;
	private Text sshPort;
	
	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		
		Composite dbComposite = getDBComposite(composite);
		dbComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Button chkTunnel = new Button(composite, SWT.CHECK);
		chkTunnel.setText("Connect using SSH tunnel");
		
		Composite sshComposite = getSSHComposite(composite);
		sshComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return composite;
	}
	
	@Override
	public void init(IWorkbench workbench) {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		setPreferenceStore(store);
		setDescription("Enter connection details below:");
	}


	private Composite getDBComposite(Composite parent){
		Group group = new Group(parent,SWT.NONE);
		group.setLayout(new GridLayout(2,false));
		group.setText("Database Settings");
		
		Label label;
		
		label = new Label(group, SWT.NONE);
		label.setText("Host:");
		dbHost = new Text(group, SWT.SINGLE | SWT.BORDER);
		dbHost.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		label = new Label(group, SWT.NONE);
		label.setText("Username:");
		dbUsername = new Text(group, SWT.SINGLE | SWT.BORDER);
		dbUsername.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		label = new Label(group, SWT.NONE);
		label.setText("Password:");
		dbPassword = new Text(group, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
		dbPassword.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		label = new Label(group, SWT.NONE);
		label.setText("Dabase:");
		dbDatabase = new Text(group, SWT.SINGLE | SWT.BORDER);
		dbDatabase.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		label = new Label(group, SWT.NONE);
		label.setText("Port:");
		dbPort = new Text(group, SWT.SINGLE | SWT.BORDER);
		dbPort.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		return group;
	}
	
	private Composite getSSHComposite(Composite parent){
		Group group = new Group(parent,SWT.NONE);
		group.setLayout(new GridLayout(2,false));
		group.setText("SSH Tunnel Settings");
		
		Label label;
		
		label = new Label(group, SWT.NONE);
		label.setText("Host:");
		sshHost = new Text(group, SWT.SINGLE | SWT.BORDER);
		sshHost.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		label = new Label(group, SWT.NONE);
		label.setText("Username:");
		sshUser = new Text(group, SWT.SINGLE | SWT.BORDER);
		sshUser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		label = new Label(group, SWT.NONE);
		label.setText("Password:");
		sshPassword = new Text(group, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
		sshPassword.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		label = new Label(group, SWT.NONE);
		label.setText("Port:");
		sshPort =new Text(group, SWT.SINGLE | SWT.BORDER);
		sshPort.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		return group;
	}
}
