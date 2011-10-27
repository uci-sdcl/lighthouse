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
package edu.uci.lighthouse.expertise;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;




import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.ToolTipHelper;

import org.eclipse.draw2d.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.tigris.subversion.subclipse.core.SVNProviderPlugin;
import org.tigris.subversion.subclipse.ui.SVNUIPlugin;

import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.LHAuthorDAO;
import edu.uci.lighthouse.ui.figures.CompartmentFigure;
import edu.uci.lighthouse.ui.figures.ILighthouseClassFigure.MODE;

import org.tigris.subversion.subclipse.ui.SVNUIPlugin;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;

public class ExpertiseFigure extends CompartmentFigure {
	private int NUM_COLUMNS = 1;
	private List<LighthouseAuthor> cacheOfAuthors;
	
	MODE mode;
	private ExpertisePanel panel;

	public ExpertiseFigure() {
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.numColumns = NUM_COLUMNS;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
			
		setLayoutManager(layout);
		panel = new ExpertisePanel();
		GridData data = new GridData(SWT.FILL,SWT.FILL,true,true);
		this.add(panel, data);
		
		Image icon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
				"/icons/expert_contrib.png").createImage();
		
		OneExpertPanel oep = new OneExpertPanel(icon,"fred", "top contributor");
		
		Image icon2 = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
		"/icons/expert_originator.png").createImage();
		OneExpertPanel oep2 = new OneExpertPanel(icon2, "fred","originator");
		Image icon3 = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
		"/icons/expert_user.png").createImage();
		OneExpertPanel oep3 = new OneExpertPanel(icon3, "fred","top user");
		
		panel.add(oep);
		panel.add(oep2);
		panel.add(oep3);

	//	IWorkspace workspace = ResourcesPlugin.getWorkspace();
	//	IWorkspaceRoot root = workspace.getRoot();
	//	SvnConfigurationOptions.setSvnUsername("lmartie");
	//	SvnConfigurationOptions.setSvnPassword("ATOmbomb098/");
	//	SvnConfigurationOptions.getProcessor().getInfoProcessor().getRepositoryUrl();
	}

	public boolean isVisible(MODE mode) {
		return true;
	}
	
	
	public void populate(MODE mode) {
		this.mode = mode;
		try {
			cacheOfAuthors = new LHAuthorDAO().list();
			for(LighthouseAuthor author: cacheOfAuthors){
				String authorName = author.getName();
				Label nameLabel = new Label(authorName);
				LighthouseEntity entity = this.getUmlClass();
				if(entity instanceof LighthouseClass){
					LighthouseClass clazz = (LighthouseClass)entity;
					
					
				}
			//	panel.add(nameLabel);
			}
			
		} catch (JPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}
	
	private class OneExpertPanel extends Panel{
		
		String name;
		String metric;
		Image icon;
		
		public OneExpertPanel(Image icon, String name, String metric){
			GridLayout layout = new GridLayout();
			layout.horizontalSpacing = 0;
			layout.verticalSpacing = 0;
			layout.numColumns = 2;
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			this.setLayoutManager(layout);
			
			this.icon = icon;
			this.metric = metric;
			this.name = name;
			
			ImageFigure imageFigure = new ImageFigure(icon);
			this.add(imageFigure);
			
			Label nameLabel = new Label(name);
			this.add(nameLabel);

		
		}

	}
	
	private class ExpertisePanel extends Panel {
		private FlowLayout ExpertisePanel_layout;

		public ExpertisePanel() {
			
			
			ExpertisePanel_layout = new FlowLayout();
			ExpertisePanel_layout.setMajorAlignment(FlowLayout.ALIGN_LEFTTOP);
			ExpertisePanel_layout.setMinorSpacing(25);
			//this.setBackgroundColor(ColorConstants.black);
			
			setLayoutManager(ExpertisePanel_layout);
			
		}


	}
	

}
