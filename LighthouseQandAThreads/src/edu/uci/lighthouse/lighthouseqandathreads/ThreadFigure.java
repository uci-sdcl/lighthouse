package edu.uci.lighthouse.lighthouseqandathreads;

import java.util.List;

import edu.uci.lighthouse.LHmodelExtensions.LHclassPluginExtension;
import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.QAforums.LHforum;
import edu.uci.lighthouse.model.QAforums.LHthreadCreator;
import edu.uci.lighthouse.model.QAforums.TeamMember;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.LHEntityDAO;
import edu.uci.lighthouse.ui.figures.CompartmentFigure;
import edu.uci.lighthouse.ui.figures.ILighthouseClassFigure.MODE;
import edu.uci.lighthouse.ui.figures.CompartmentFigure;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ChangeEvent;
import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.Panel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.draw2d.ActionListener;
import org.eclipse.jface.dialogs.MessageDialog;

import org.eclipse.draw2d.MouseListener;

import org.eclipse.draw2d.Label;

public class ThreadFigure extends CompartmentFigure {

	private FlowLayout layout;
	private Image icon;
	private Button questionButton;
	private LighthouseEntity le;
    private LighthouseClass clazz;
	private LHforum forum;

	private Controller controller;

	public ThreadFigure() {
		layout = new FlowLayout();
		layout.setMajorAlignment(FlowLayout.ALIGN_LEFTTOP);
		layout.setMinorSpacing(25);
		setLayoutManager(layout);
		icon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
				"/icons/question.png").createImage();
		


	}

	public boolean isVisible(MODE mode) {
		return true;
	}

	public void populate(MODE mode) {
		
		le = getUmlClass();			
	    clazz = (LighthouseClass) le;
		forum = clazz.getForum();
		if(forum == null){
			forum = new LHforum();
			clazz.setForum(forum);		
		}

		QuestionButton questionButton = new QuestionButton(icon);
		this.add(questionButton, new Rectangle(0, 0, 10, 10));
		
		
		
		Panel panel = new Panel();
		panel.setLayoutManager(layout);
		
		Label label = new Label(forum.countSolvedThreads()+"/"+forum.countThreads()+"");
		label.setBackgroundColor(ColorConstants.lightGreen);
		label.setOpaque(true);
		panel.add(new ImageFigure(icon));
		panel.add(label);
		this.add(panel);


	}
	

	private class QuestionButton extends Button {
		public QuestionButton(Image icon) {
			super(icon);
		}

		public void handleMouseReleased(MouseEvent event) {

			
			/*List<LHclassPluginExtension> exts = clazz.getExtensions();
			LHforum forum = null;

			for (LHclassPluginExtension ext : exts) {
				if (ext instanceof LHforum) {
					forum = (LHforum) ext;
					break;
				}
			}*/


			
			
				LighthouseAuthor author = ModelUtility.getAuthor();
				LHthreadCreator tm = new LHthreadCreator(author);

				Display display = Display.getDefault();

				NewQuestionDialog nqDialog = new NewQuestionDialog(
						display.getActiveShell(), "Forum", null,
						le.getFullyQualifiedName(), MessageDialog.INFORMATION,
						SWT.OK, tm, forum);

				controller = new Controller(nqDialog, forum, clazz);

				int response = nqDialog.open();
				controller.stopObserving();
			

		}
	}

}
