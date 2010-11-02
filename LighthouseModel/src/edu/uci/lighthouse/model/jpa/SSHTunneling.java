package edu.uci.lighthouse.model.jpa;

import java.io.IOException;
import java.util.Hashtable;

import org.dom4j.DocumentException;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

@Deprecated
public class SSHTunneling {

	private String host = "calico.ics.uci.edu"; // "128.195.020.103"
	private String user = "lighthouse";
	private String password = "light99";
	private int port = 22;

	private int tunnelLocalPort = 3306;
	private String tunnelRemoteHost = "127.0.0.1";
	private int tunnelRemotePort = 3306;

	Session session;
	JSch jsch;

	public static void main(String[] args) {
		SSHTunneling t = new SSHTunneling();
		try {
			t.go();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void go() throws JSchException {
		jsch = new JSch();
		session = jsch.getSession(user, host, port);
		// session=jsch.getSession(user, "127.0.0.1", port);
		localUserInfo lui = new localUserInfo();
		session.setUserInfo(lui);
		// session.setPassword(password);

		final Hashtable config = new Hashtable();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		// jsch.addIdentity("ssh/id_dsa");

		session.connect();
		int assigned_port = session.setPortForwardingL(tunnelLocalPort,
				tunnelRemoteHost, tunnelRemotePort);
		// int assigned_port =
		// session.setPortForwardingL(8080,"www.google.com",80);

		System.out.println("localhost:" + assigned_port + " -> "
				+ tunnelRemoteHost + ":" + tunnelRemotePort);

		// List<LighthouseEvent> lEvents = new LHEventDAO().list();
		// System.out.println(lEvents);

	}

	public void testUpdateModelFromDelta() throws DocumentException,
			IOException, JPAException {

	}

	class localUserInfo implements UserInfo, UIKeyboardInteractive {
		String passwd = "light99";

		public String getPassword() {

			passwd = "light99";

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
			System.out.println(message);
		}

		@Override
		public String[] promptKeyboardInteractive(String arg0, String arg1,
				String arg2, String[] arg3, boolean[] arg4) {
			System.out.println("arg0: " + arg0);
			System.out.println("arg1: " + arg1);
			System.out.println("arg2: " + arg2);
			for (String s : arg3) {
				System.out.println("arg3: " + s);
			}
			for (boolean b : arg4) {
				System.out.println("arg4: " + b);
			}

			return new String[] { passwd };
		}
	}

}
