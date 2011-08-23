package edu.uci.lighthouse.lighthouseqandathreads;

import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.lighthouseqandathreads.model.Controller;
import edu.uci.lighthouse.lighthouseqandathreads.model.FakeDataBase;
import edu.uci.lighthouse.lighthouseqandathreads.model.TeamMember;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.ui.figures.CompartmentFigure;
import edu.uci.lighthouse.ui.figures.ILighthouseClassFigure.MODE;
import edu.uci.lighthouse.ui.figures.CompartmentFigure;

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.draw2d.ActionListener;
import org.eclipse.jface.dialogs.MessageDialog;

import org.eclipse.draw2d.MouseListener;


public class ThreadFigure extends CompartmentFigure {

	private FlowLayout layout;
	private Image icon;
	private Button questionButton;

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

		QuestionButton questionButton = new QuestionButton(icon);
		this.add(questionButton, new Rectangle(0, 0, 10, 10));
		


	}

	private class QuestionButton extends Button {
		public QuestionButton(Image icon) {
			super(icon);
		}

		public void handleMouseReleased(MouseEvent event) {

			LighthouseEntity le = getUmlClass();
			LighthouseAuthor author = ModelUtility.getAuthor();
			TeamMember tm = new TeamMember(author);
			
			Display display = Display.getDefault();
			
			NewQuestionDialog nqDialog = new NewQuestionDialog(display.getActiveShell(), "Forum",
					null, le.getFullyQualifiedName(),
					MessageDialog.INFORMATION, SWT.OK,tm);

			controller = new Controller(nqDialog);
			
			int response = nqDialog.open();
			controller.stopObserving();
			

		}
	}

}
