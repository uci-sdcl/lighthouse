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
package edu.uci.lighthouse.core.util;

import java.util.Hashtable;
import java.util.Properties;

import org.osgi.framework.BundleContext;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

import edu.uci.lighthouse.core.listeners.IPluginListener;
import edu.uci.lighthouse.core.preferences.DatabasePreferences;

@Deprecated
public class SSHTunnel implements IPluginListener {

	private Session session;
	private JSch jsch;
	private Properties sshProperties;
	private boolean running;
	private int localPort;

	public SSHTunnel(Properties sshProperties) {
		this.sshProperties = sshProperties;
		localPort = Integer
		.parseInt(sshProperties
				.getProperty(DatabasePreferences.DB_PORT));
		jsch = new JSch();
	}

	@Override
	public void start(BundleContext context) throws Exception {
		running = false;
		session = jsch.getSession(sshProperties
				.getProperty(DatabasePreferences.SSH_USERNAME), sshProperties
				.getProperty(DatabasePreferences.SSH_HOST), Integer
				.parseInt(sshProperties
						.getProperty(DatabasePreferences.SSH_PORT)));
		LocalUserInfo lui = new LocalUserInfo(sshProperties
				.getProperty(DatabasePreferences.SSH_PASSWD));
		session.setUserInfo(lui);

		final Hashtable config = new Hashtable();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);

		session.connect();
		int assigned_port = session.setPortForwardingL(localPort,
				sshProperties.getProperty(DatabasePreferences.DB_HOST), Integer
				.parseInt(sshProperties
						.getProperty(DatabasePreferences.DB_PORT)));
		running = true;
	}
	
	public boolean isRunning(){
		return running;
	}

	public int getLocalPort() {
		return localPort;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (session != null) {
			session.disconnect();
		}
	}
	
	public void restart() throws Exception {
		stop(null);
		start(null);
	}

	class LocalUserInfo implements UserInfo, UIKeyboardInteractive {
		String passwd;

		public LocalUserInfo(String passwd) {
			this.passwd = passwd;
		}

		public String getPassword() {
			return passwd;
		}

		public boolean promptYesNo(String str) {
			return false;
		}

		public String getPassphrase() {
			return null;
		}

		public boolean promptPassphrase(String message) {
			return false;
		}

		public boolean promptPassword(String message) {
			return true;
		}

		public void showMessage(String message) {

		}

		@Override
		public String[] promptKeyboardInteractive(String arg0, String arg1,
				String arg2, String[] arg3, boolean[] arg4) {
			return new String[] { passwd };
		}
	}

}
