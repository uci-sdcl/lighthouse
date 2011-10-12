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
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.Panel;

import org.eclipse.draw2d.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.LHAuthorDAO;
import edu.uci.lighthouse.ui.figures.CompartmentFigure;
import edu.uci.lighthouse.ui.figures.ILighthouseClassFigure.MODE;

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
	//	IWorkspace workspace = ResourcesPlugin.getWorkspace();
	//	IWorkspaceRoot root = workspace.getRoot();
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
				panel.add(nameLabel);
			}
			
		} catch (JPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private class ExpertisePanel extends Panel {
		private FlowLayout ExpertisePanel_layout;

		public ExpertisePanel() {
			ExpertisePanel_layout = new FlowLayout();
			ExpertisePanel_layout.setMajorAlignment(FlowLayout.ALIGN_LEFTTOP);
			ExpertisePanel_layout.setMinorSpacing(25);
			this.setBackgroundColor(ColorConstants.black);
			setLayoutManager(ExpertisePanel_layout);			
		}


	}
	

}
