package edu.uci.lighthouse.expertise.ui.figures;



import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import edu.uci.lighthouse.expertise.Activator;
import edu.uci.lighthouse.ui.figures.CompartmentFigure;
import edu.uci.lighthouse.ui.figures.ILighthouseClassFigure.MODE;

//Created by Alex Taubman and Tiago Proenca

public class ExpertiseFigure extends CompartmentFigure {
	
	private Image icon;
	private FlowLayout layout;
	
	public ExpertiseFigure() {
		
		//set up layout and import image
		layout = new FlowLayout();
		layout.setMajorAlignment(FlowLayout.ALIGN_CENTER);
		layout.setMinorSpacing(25);
		setLayoutManager(layout);
		icon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/person.png").createImage();

	}

	@Override
	public boolean isVisible(MODE mode) {
		//only display in modes three and four
		return mode.equals(mode.THREE) || mode.equals(mode.FOUR);
	}

	@Override
	public void populate(MODE mode) {
		
		removeAll();
		
		//create listeners to listen for mouse clicks
		ClickListener click1 = new ClickListener();
		ClickListener click2 = new ClickListener();
		ClickListener click3 = new ClickListener();
		
	
		//put the icon in each of three labels
		Label expertise1 = new Label(icon);	
		Label expertise2 = new Label(icon);
		Label expertise3 = new Label(icon);
		
		//set labels with mouse listeners
		expertise1.addMouseListener(click1);
		expertise2.addMouseListener(click2);
		expertise3.addMouseListener(click3);
		click1.setType(this, "Expertise person 1");
		click2.setType(this, "Expertise person 2");
		click3.setType(this, "Expertise person 3");
		
//		int labelWidth = icon.getBounds().width;
//		
//		int labelHeight = icon.getBounds().height;
//		int extraWidth = figureWidth - 3 * labelWidth;
//		System.out.println(figureWidth);
//		Label emptySpace = new Label();
//		emptySpace.setSize(extraWidth/2, labelHeight);
		
		
		//display labels
		add(expertise1);
//		add(emptySpace);
		add(expertise2);
//		add(emptySpace);
		add(expertise3);
		
//		invalidate();
		
	}
	
	
	
}
