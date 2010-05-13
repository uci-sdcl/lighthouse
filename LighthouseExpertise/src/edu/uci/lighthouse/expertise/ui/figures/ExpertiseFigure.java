package edu.uci.lighthouse.expertise.ui.figures;


import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import edu.uci.lighthouse.expertise.Activator;
import edu.uci.lighthouse.ui.figures.CompartmentFigure;
import edu.uci.lighthouse.ui.figures.UmlClassBorder;
import edu.uci.lighthouse.ui.figures.ILighthouseClassFigure.MODE;
import org.eclipse.swt.graphics.Color;
import edu.uci.lighthouse.ui.swt.util.ColorFactory;

//Created by Alex Taubman and Tiago Proenca

public class ExpertiseFigure extends CompartmentFigure 
{

	private Image icon;
	private FlowLayout layout;

	public ExpertiseFigure() 
	{
		//setup layout and import images
		layout = new FlowLayout();
		
		//make the border color red
		UmlClassBorder border = new UmlClassBorder(ColorFactory.red);
		this.setBorder(border);
		setLayoutManager(layout);
		icon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/smallperson.jpg").createImage();

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
		ClickListener click = new ClickListener();
		
		//create and add label with picture and phrase
		Label personLabel = new Label("Tiago requests help!", icon);		
		add(personLabel);
		
		//set labels with mouse listeners
		click.setType("Help person");
		personLabel.addMouseListener(click);
		
	}

}
