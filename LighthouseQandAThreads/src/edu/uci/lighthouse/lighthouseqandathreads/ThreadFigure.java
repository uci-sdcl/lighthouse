package edu.uci.lighthouse.lighthouseqandathreads;

import edu.uci.lighthouse.lighthouseqandathreads.model.Forum;
import edu.uci.lighthouse.lighthouseqandathreads.model.Post;
import edu.uci.lighthouse.lighthouseqandathreads.model.TeamMember;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.ui.figures.CompartmentFigure;
import edu.uci.lighthouse.ui.figures.ILighthouseClassFigure.MODE;
import edu.uci.lighthouse.ui.figures.CompartmentFigure;
import edu.uci.lighthouse.core.util.ModelUtility;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ChangeEvent;
import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.Panel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.draw2d.ActionListener;
import org.eclipse.jface.dialogs.MessageDialog;

import org.eclipse.draw2d.MouseListener;

import edu.uci.lighthouse.lighthouseqandathreads.model.Thread;

public class ThreadFigure extends CompartmentFigure {
	
	private FlowLayout layout;
	Image icon;
	Button questionButton;

	public ThreadFigure() {
		layout = new FlowLayout();
		layout.setMajorAlignment(FlowLayout.ALIGN_LEFTTOP);
		layout.setMinorSpacing(25);
		setLayoutManager(layout);
		icon = 
			AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/question.png").createImage();

	}

	public boolean isVisible(MODE mode) {
		return true;
	}

	public void populate(MODE mode) {
		
		QuestionButton questionButton =  new QuestionButton(icon);		
		this.add(questionButton, new Rectangle(0, 0, 10, 10));

	}
	

	private class QuestionButton extends Button{
		public QuestionButton(Image icon) {
			super(icon);
		}

		public void handleMouseReleased(MouseEvent event){
			
			LighthouseEntity le = getUmlClass();
			LighthouseAuthor author = ModelUtility.getAuthor();
			TeamMember tm = new TeamMember(author);
			
			
			NewQuestionDialog nqDialog = new NewQuestionDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell()
					, "Threads Box", null,
					le.getFullyQualifiedName()+" Threads", MessageDialog.INFORMATION, SWT.OK);
			
			nqDialog.setTestForm(getTestForum(tm));
			int response = nqDialog.open();
			
			if(response == nqDialog.OK){
				String fullyQualifiedName = le.getFullyQualifiedName();
				String question = fullyQualifiedName+"\n"+nqDialog.getQuestion();
				Post post = new Post(true, question,tm);
				System.out.println(tm.getAuthor().getName()+" "+question);
			}
		}
	}
	
	
	//TEST:
	private Forum getTestForum(TeamMember tm){
		Post rootPost = new Post(true,"Bill: Can you make a getFoo()?", tm);
		
		Post response = new Post(false,"Okay!",tm);
		rootPost.addResponse(response);
		
		Thread thread = new Thread("NeedInterface",rootPost);
		
		Forum forum = new Forum();
		forum.addThread(thread);
		
		return forum;
	}
	

}
