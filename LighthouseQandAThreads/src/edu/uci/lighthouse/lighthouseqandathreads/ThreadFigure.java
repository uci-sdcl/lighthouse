package edu.uci.lighthouse.lighthouseqandathreads;

import edu.uci.lighthouse.ui.figures.CompartmentFigure;
import edu.uci.lighthouse.ui.figures.ILighthouseClassFigure.MODE;
import edu.uci.lighthouse.ui.figures.CompartmentFigure;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ChangeEvent;
import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.Panel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.draw2d.ActionListener;
import org.eclipse.jface.dialogs.MessageDialog;

import org.eclipse.draw2d.MouseListener;

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
			NewQuestionDialog nqDialog = new NewQuestionDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell()
					, "Question Box", null,
					"Question", MessageDialog.INFORMATION, SWT.OK);
			
			int response = nqDialog.open();
		}
	}
	

}
