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
package edu.uci.lighthouse.services.persistence;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.osgi.framework.BundleContext;

public class ThreadTest extends TestCase {

//	public void testThread() throws Exception {
//		DummieThread t = new DummieThread();
//		t.start(null);
//		
//		Thread.sleep(5000);
//		t.pause();
//		Thread.sleep(7000);
//		t.play();
//		Thread.sleep(10000);
//	}
//	
	public void testName() {
		String fqn = "Atm.edu.deitel.ATM";
		assertTrue(existsInWorkspace("Atm.edu.deitel.ATM"));
		assertTrue(existsInWorkspace("Atm.edu.deitel.Tiago"));
	}
	
	private boolean existsInWorkspace(String fqn) {
		int index = fqn.indexOf(".");
		if (index != -1) {
			String projectName = fqn.substring(0,index);
			String relativePath = File.separator + fqn.substring(index + 1).replaceAll("\\.", File.separator) + ".java";
			
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IProject[] projects = workspace.getRoot().getProjects();
			for (IProject project : projects) {
				if (project.isOpen() && projectName.equals(project.getName())) {
					try {
						IJavaProject jProject = (IJavaProject) project
						.getNature(JavaCore.NATURE_ID);
						String[] sourceFolders = getSourceFolders(jProject);
						for (String srcFolder : sourceFolders) {
							String fileName = project.getLocation().toOSString().replaceAll(File.separator+projectName, "") + srcFolder + relativePath;
							File file = new File(fileName);
							if (file.exists()) {
								return true;
							}
						}
					} catch (CoreException e) {
						//logger.error(e, e);
					}
				}
			}
		}
		return false;		
	}
	
	public static String[] getSourceFolders(IJavaProject project) {
		List<String> result = new LinkedList<String>();
		try {
			IClasspathEntry[] classPaths = project.getRawClasspath();
			for (IClasspathEntry cp : classPaths) {
				if (cp.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
					result.add(cp.getPath().toString());
				}
			}
		} catch (JavaModelException e) {
			//logger.error(e,e);
		}
		return result.toArray(new String[0]);
	}
	
}

class DummieThread extends Thread {
	
	boolean running = false;
	
	boolean suspended = false;
	
	public DummieThread() {
		super("Dummie");
	}
	
	@Override
	public void run() {
		while (running) {
			try {
				if (suspended) {
					synchronized (this) {
						while (suspended) {
							wait();
							System.out.println("Ressucitando...");
						}
					}
				}
				// Action
				System.out.println("Doing some action...");
				
				sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void start(BundleContext context) throws Exception {
		running = true;
		this.start();
	}

	public void stop(BundleContext context) throws Exception {
		running = false;
	}
	
	public void pause() {
		suspended = true;
		interrupt();
	}
	
	public synchronized void play() {
		suspended = false;
		notify();
		System.out.println("Notificando...");
	}
}
