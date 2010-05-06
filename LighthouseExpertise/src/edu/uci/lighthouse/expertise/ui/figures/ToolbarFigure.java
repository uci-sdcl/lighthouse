package edu.uci.lighthouse.expertise.ui.figures;

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import edu.uci.lighthouse.expertise.Activator;
import edu.uci.lighthouse.ui.figures.CompartmentFigure;
import edu.uci.lighthouse.ui.figures.ILighthouseClassFigure.MODE;


public class ToolbarFigure extends CompartmentFigure {
	
	private Image pencil;
	private Image bug;
	private Image lock;
	private Image light;
	private Image glasses;
	private FlowLayout layout;
	
	public ToolbarFigure() {
		layout = new FlowLayout();
		setLayoutManager(layout);
		bug = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/bug.png").createImage();
		lock = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/lock.png").createImage();
		light = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/light.png").createImage();
		glasses = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/glasses.png").createImage();
		pencil = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/pencil.png").createImage();
	
	}

	@Override
	public boolean isVisible(MODE mode) {
		//QUESTION: How do I get the current level?
		//if (level == mode.THREE || level == mode.FOUR)
		//	return true;
		return true;
	}

	@Override
	public void populate(MODE mode) {
		/* It is display equals in any mode. */
		removeAll();
		
		
		Label bugLabel = new Label(bug);	
		Label lockLabel = new Label(lock);	
		Label lightLabel = new Label(light);	
		Label glassesLabel = new Label(glasses);	
		Label pencilLabel = new Label(pencil);	
		
		
		
		add(bugLabel);
		add(lockLabel);
		add(lightLabel);
		add(glassesLabel);
		add(pencilLabel);
	}
	
}
