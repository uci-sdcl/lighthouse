package edu.uci.lighthouse.expertise.ui.figures;


import java.util.ArrayList;

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

//Created by Alex Taubman

public class ContactFigure extends CompartmentFigure 
{

	private Image AIMIcon;
	private Image MSNIcon;
	private Image emailIcon;
	private FlowLayout layout;

	public ContactFigure() 
	{
		//setup layout and import images
		layout = new FlowLayout();
		layout.setHorizontal(true);
		layout.setMajorAlignment(FlowLayout.ALIGN_RIGHTBOTTOM);
		setLayoutManager(layout);
		
		AIMIcon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/aimIcon.jpg").createImage();
		MSNIcon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/msnIcon.jpg").createImage();
		emailIcon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/emailIcon.jpg").createImage();
		

	}

	@Override
	public boolean isVisible(MODE mode) {
		//only display in modes three and four
		return mode.equals(mode.THREE) || mode.equals(mode.FOUR);
	}

	@Override
	public void populate(MODE mo) {
		

		removeAll();
		
		//add contact icons
		Label AIMLabel = new Label(AIMIcon);	
		add(AIMLabel, FlowLayout.ALIGN_CENTER);
		Label MSNLabel = new Label(MSNIcon);		
		add(MSNLabel, FlowLayout.ALIGN_CENTER);
		Label eMailLabel = new Label(emailIcon);		
		add(eMailLabel, FlowLayout.ALIGN_CENTER);
		
		ClickListener click1 = new ClickListener();
		ClickListener click2 = new ClickListener();
		ClickListener click3 = new ClickListener();
		//set labels with mouse listeners
		click1.setType(this, "AIM");
		AIMLabel.addMouseListener(click1);
		click2.setType(this, "MSN");
		MSNLabel.addMouseListener(click2);
		click3.setType(this, "eMail");
		eMailLabel.addMouseListener(click3);
		
		
		
		



	}

	

}
