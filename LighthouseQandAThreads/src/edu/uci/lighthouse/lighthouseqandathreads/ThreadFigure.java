package edu.uci.lighthouse.lighthouseqandathreads;

import edu.uci.lighthouse.ui.figures.CompartmentFigure;
import edu.uci.lighthouse.ui.figures.ILighthouseClassFigure.MODE;
import edu.uci.lighthouse.ui.figures.CompartmentFigure;

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Panel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.geometry.Rectangle;

public class ThreadFigure extends CompartmentFigure {
	Button questionButton;
	private FlowLayout layout;

	public ThreadFigure() {
		layout = new FlowLayout();
		layout.setMajorAlignment(FlowLayout.ALIGN_CENTER);
		layout.setMinorSpacing(25);
		setLayoutManager(layout);
		Image icon = 
			AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/question.png").createImage();
		questionButton =  new org.eclipse.draw2d.Button(icon);
		this.add(questionButton, new Rectangle(0, 0, 10, 10));

	}

	@Override
	public boolean isVisible(MODE mode) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void populate(MODE mode) {
		
	}

}
