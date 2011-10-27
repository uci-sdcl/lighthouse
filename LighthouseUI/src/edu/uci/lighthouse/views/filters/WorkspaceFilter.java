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
package edu.uci.lighthouse.views.filters;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseModel;

public class WorkspaceFilter extends ViewerFilter{
	
	private int numOfHops = 0;
	private static Logger logger = Logger.getLogger(WorkspaceFilter.class);
	
	public WorkspaceFilter(int numOfHops){
		this.numOfHops = numOfHops;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof LighthouseClass) {
			LighthouseClass aClass = (LighthouseClass) element;
			IWorkbenchWindow window = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow();
			IWorkbenchPage[] pages = window.getPages();
			for (IWorkbenchPage page : pages) {
				IEditorReference[] editors = page.getEditorReferences();
				for (IEditorReference editor : editors) {
					try {
						IFile file = (IFile) editor.getEditorInput()
								.getAdapter(IFile.class);
						if (isJavaFile(file)) {
							LighthouseModel model = LighthouseModel
									.getInstance();
							String classFqn = getClassFullyQualifiedName(file);
							if (classFqn != null){
								 LighthouseEntity entity = model.getEntity(classFqn);
								if (entity != null){
									LighthouseClass iClass = (LighthouseClass) entity;
									if (iClass.equals(aClass) || belongsToDistance(aClass,iClass,numOfHops)){
										return true;
									}
								}
							}
						
						}
					} catch (PartInitException e) {
						logger.error(e,e);
					}
				}
			}
		}
		return false;
	}
	
	private boolean belongsToDistance(LighthouseClass fromClass,
			LighthouseClass hopClass, int numOfHops2) {
		
		LighthouseModel model = LighthouseModel.getInstance();
	
		
		return false;
	}

	private boolean isJavaFile(IFile file){
		if(file.getFileExtension().equalsIgnoreCase("java")) {
			return true;
		}
		return false;
	}
	
	private String getClassFullyQualifiedName(IFile iFile) {
		String result = null;
		try {
			/*
			 * When the Java file is out of sync with eclipse, get the fully
			 * qualified name from ICompilationUnit doesn't work. So we decide to do
			 * this manually, reading the file from the file system and parsing it.
			 */
			BufferedReader d = new BufferedReader(new InputStreamReader(new FileInputStream(iFile.getLocation().toOSString())));
			while (d.ready()) {
				String line = d.readLine();
				if (line.contains("package")) {
					String[] tokens = line.split("package\\s+|;");
					for (String token : tokens) {
						if (token.matches("[\\w\\.]+")) {
							result = token;
							break;
						}
					}
					break;
				}
			}
			/*
			 * Java files should have at least one class with the same name of
			 * the file
			 */
			String fileNameWithoutExtension = iFile.getName().replaceAll(
					".java", "");
			result += "."+fileNameWithoutExtension;			
		} catch (Exception e) {
			logger.error(e,e);
		}
		return result;
	}

}
