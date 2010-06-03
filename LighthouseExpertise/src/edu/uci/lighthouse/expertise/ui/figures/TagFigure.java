package edu.uci.lighthouse.expertise.ui.figures;

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import edu.uci.lighthouse.expertise.Activator;
import edu.uci.lighthouse.ui.figures.CompartmentFigure;
import edu.uci.lighthouse.ui.figures.ILighthouseClassFigure.MODE;

//Created by Alex Taubman

public class TagFigure extends CompartmentFigure {
	
	private Image pencil;
	private Image bug;
	private Image lock;
	private Image light;
	private Image glasses;
	private FlowLayout layout;
	
	public TagFigure() {
		
		//set up layout and import images
		layout = new FlowLayout();
		setLayoutManager(layout);
		bug = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/bug.jpg").createImage();
		lock = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/lock.jpg").createImage();
		light = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/light.jpg").createImage();
		glasses = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/glasses.jpg").createImage();
		pencil = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/pencil.jpg").createImage();
	
	}

	@Override
	public boolean isVisible(MODE mode) {
		
		//display only in modes three and four
		return mode.equals(mode.THREE) || mode.equals(mode.FOUR);
	}

	@Override
	public void populate(MODE mode) {
		
		removeAll();
		//create listeners to listen for mouse clicks
		ClickListener click1 = new ClickListener();
		ClickListener click2 = new ClickListener();
		ClickListener click3 = new ClickListener();
		ClickListener click4 = new ClickListener();
		ClickListener click5 = new ClickListener();
		
		
		//assign each image to label
		Label bugLabel = new Label(bug);	
		Label lockLabel = new Label(lock);	
		Label lightLabel = new Label(light);	
		Label glassesLabel = new Label(glasses);	
		Label pencilLabel = new Label(pencil);	
		
		//set labels with mouse listeners
		bugLabel.addMouseListener(click1);
		lockLabel.addMouseListener(click2);
		lightLabel.addMouseListener(click3);
		glassesLabel.addMouseListener(click4);
		pencilLabel.addMouseListener(click5);
		click1.setType(this, "Bug label");
		click2.setType(this, "Lock label");
		click3.setType(this, "Light label");
		click4.setType(this, "Glasses label");
		click5.setType(this, "Pencil label");
		
		//display each label
		add(bugLabel);
		add(lockLabel);
		add(lightLabel);
		add(glassesLabel);
		add(pencilLabel);
	}
	
}
