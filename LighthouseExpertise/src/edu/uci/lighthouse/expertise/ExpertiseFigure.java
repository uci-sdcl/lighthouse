package edu.uci.lighthouse.expertise;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;



import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.Panel;

import org.eclipse.draw2d.Label;
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
	private Image icon;
	private List<LighthouseAuthor> cacheOfAuthors;
	
	static{

	}
	
	MODE mode;


	public ExpertiseFigure() {
		GridLayout layout = new GridLayout();
		layout.numColumns = NUM_COLUMNS;			
		setLayoutManager(layout);
		icon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
				"/icons/question.png").createImage();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
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
				this.add(nameLabel);
			}
			
		} catch (JPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private class Listener implements MouseMotionListener{


		@Override
		public void mouseDragged(MouseEvent arg0) {
						
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseHover(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	}
	

	

	private class QuestionPanel extends Panel {
		private FlowLayout QuestionPanel_layout;
	}

}
