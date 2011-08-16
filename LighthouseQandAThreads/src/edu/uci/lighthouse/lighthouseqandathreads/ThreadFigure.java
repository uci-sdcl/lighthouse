package edu.uci.lighthouse.lighthouseqandathreads;

import edu.uci.lighthouse.ui.figures.CompartmentFigure;
import edu.uci.lighthouse.ui.figures.ILighthouseClassFigure.MODE;
import edu.uci.lighthouse.ui.figures.CompartmentFigure;
import org.eclipse.draw2d.Panel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.draw2d.Button;

public class ThreadFigure extends CompartmentFigure {
	Button questionButton;

	public ThreadFigure() {
		Image icon = 
			AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/question.png").createImage();
		questionButton =  new org.eclipse.draw2d.Button(icon);
		this.add(questionButton);

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
