package edu.uci.lighthouse.core.preferences;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
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

import edu.uci.lighthouse.core.Activator;
import edu.uci.lighthouse.core.util.JPAConnectionUtility;

public class DatabasePreferences extends PreferencePage implements
IWorkbenchPreferencePage{

	private Text dbHost;
	private Text dbUsername;
	private Text dbPassword;
	private Text dbDatabase;
	private Text dbPort;
	
	private Button chkTunnel;
	
	private Text sshHost;
	private Text sshUser;
	private Text sshPassword;
	private Text sshPort;
	
	private static final String ROOT = "edu.uci.lighthouse.core.preferences";
	
	public static final String DB_HOST = ROOT+ ".dbHost";
	public static final String DB_USERNAME = ROOT+ ".dbUsername";
	public static final String DB_PASSWD = ROOT+ ".dbPassword";
	public static final String DB_DATABASE = ROOT+ ".dbDatabase";
	public static final String DB_PORT = ROOT+ ".dbPort";
	
	public static final String USES_TUNNEL = ROOT+ ".sshTunnel";
	
	public static final String SSH_HOST = ROOT+ ".sshHost";
	public static final String SSH_USERNAME = ROOT+ ".sshUser";
	public static final String SSH_PASSWD = ROOT+ ".sshPassword";
	public static final String SSH_PORT = ROOT+ ".sshPort";
	
	@Override
	public void init(IWorkbench workbench) {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		setPreferenceStore(store);
		setDescription("Enter MySQL connection details below:");
	}
	
	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		
		Composite dbComposite = getDBComposite(composite);
		dbComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		chkTunnel = new Button(composite, SWT.CHECK);
		chkTunnel.setText("Connect using SSH tunnel");
		
		Composite sshComposite = getSSHComposite(composite);
		sshComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		performLoad();
		
		return composite;
	}
	
	@Override
	protected void performApply() {
		super.performApply();
		if (JPAConnectionUtility.canConnect()){
			MessageDialog.openInformation(getShell(), "Database Connection", "The connection is working properly!");
		} else {
			MessageDialog.openError(getShell(), "Database Connection", "Imposible to connect to server. Please, check your connection settings.");
		}
	}

	@Override
	public boolean performOk() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		
		store.setValue(DB_HOST, dbHost.getText());
		store.setValue(DB_USERNAME, dbUsername.getText());
		store.setValue(DB_PASSWD, dbPassword.getText());
		store.setValue(DB_DATABASE, dbDatabase.getText());
		store.setValue(DB_PORT, dbPort.getText());
		
		store.setValue(USES_TUNNEL, chkTunnel.getSelection());

		store.setValue(SSH_HOST, sshHost.getText());
		store.setValue(SSH_USERNAME, sshUser.getText());
		store.setValue(SSH_PASSWD, sshPassword.getText());
		store.setValue(SSH_PORT, sshPort.getText());
		
		
		return super.performOk();
	}
	
	protected void performLoad() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		
		dbHost.setText(store.getString(DB_HOST));
		dbUsername.setText(store.getString(DB_USERNAME));
		dbPassword.setText(store.getString(DB_PASSWD));
		dbDatabase.setText(store.getString(DB_DATABASE));
		dbPort.setText(store.getString(DB_PORT));
		
		
		chkTunnel.setSelection(store.getBoolean(USES_TUNNEL));

		sshHost.setText(store.getString(SSH_HOST));
		sshUser.setText(store.getString(SSH_USERNAME));
		sshPassword.setText(store.getString(SSH_PASSWD));
		sshPort.setText(store.getString(SSH_PORT));
	}
	
	@Override
	protected void performDefaults() {

		dbHost.setText("");
		dbUsername.setText("");
		dbPassword.setText("");
		dbDatabase.setText("");
		dbPort.setText("");
		
		chkTunnel.setSelection(false);

		sshHost.setText("");
		sshUser.setText("");
		sshPassword.setText("");
		sshPort.setText("");
		
		super.performDefaults();
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
	
	public static Map<String,String> getDatabaseSettings(){
		Map<String,String> dbSettings = new HashMap<String,String>();
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		
		String connUrl = "jdbc:mysql://"+store.getString(DB_HOST)+":"+store.getString(DB_PORT)+"/"+store.getString(DB_DATABASE);
		
		dbSettings.put("hibernate.connection.url", connUrl);
		dbSettings.put("hibernate.connection.username", store.getString(DB_USERNAME));
		dbSettings.put("hibernate.connection.password", store.getString(DB_PASSWD));
		
		return dbSettings;
	}
	
	public static Map<String, String> getSSHTunnelSettings(){
		Map<String,String> sshTunnelSettings = new HashMap<String,String>();
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		
		sshTunnelSettings.put(SSH_HOST, store.getString(SSH_HOST));
		sshTunnelSettings.put(SSH_USERNAME, store.getString(SSH_USERNAME));
		sshTunnelSettings.put(SSH_PASSWD, store.getString(SSH_PASSWD));
		sshTunnelSettings.put(SSH_PORT, store.getString(SSH_PORT));
		
		return sshTunnelSettings;
	}
	
	public static boolean isConnectingUsingSSH(){
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		return store.getBoolean(USES_TUNNEL);
	}
}
