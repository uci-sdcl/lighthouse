package edu.lighthouse.core.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import edu.uci.lighthouse.core.controller.PushModel;
import edu.uci.lighthouse.model.LighthouseModel;

public class ImportHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil
		.getActiveMenuSelection(event);
		
//		Object itemSelected = selection.getFirstElement();
		
		Collection<IJavaProject> javaProjects = new ArrayList<IJavaProject>();
		
		for (Iterator iterator = selection.iterator(); iterator.hasNext();) {
			Object itemSelected =  iterator.next();
			if (itemSelected instanceof IJavaProject) {
				IJavaProject jProject = (IJavaProject) itemSelected;
				javaProjects.add(jProject);
			}
		}
		Shell shell = PlatformUI.getWorkbench()
		.getActiveWorkbenchWindow().getShell();
		
		PushModel pushModel = new PushModel(LighthouseModel.getInstance());
		try {
			pushModel.importJavaProject(javaProjects);
			MessageDialog.openInformation(shell,"Lighthouse", "Project imported successfully!");
		} catch (Exception e) {
			MessageDialog.openError(shell,"Database Connection", "Imposible to connect to server. Please, check your connection settings.");
		}
		
		
		return null;
	}



}
