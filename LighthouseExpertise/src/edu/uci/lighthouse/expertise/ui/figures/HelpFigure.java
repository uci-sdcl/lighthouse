/*******************************************************************************
* Copyright (c) {2009,2011} {Software Design and Collaboration Laboratory (SDCL)
*				, University of California, Irvine}.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    {Software Design and Collaboration Laboratory (SDCL)
*	, University of California, Irvine}
*			- initial API and implementation and/or initial documentation
*******************************************************************************/
package edu.uci.lighthouse.expertise.ui.figures;


import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import edu.uci.lighthouse.expertise.Activator;
import edu.uci.lighthouse.ui.figures.CompartmentFigure;
import edu.uci.lighthouse.ui.figures.UmlClassBorder;
import edu.uci.lighthouse.ui.figures.ILighthouseClassFigure.MODE;

//Created by Alex Taubman

public class HelpFigure extends CompartmentFigure 
{

	private Image icon;
	private FlowLayout layout;
	//create listeners to listen for mouse clicks
	ClickListener click = new ClickListener();
	MODE mode;

	public HelpFigure() 
	{
		//setup layout and import images
		layout = new FlowLayout();
		
		//make the border color red
		UmlClassBorder border = new UmlClassBorder(ColorConstants.red);
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
	public void populate(MODE mo) {
		mode = mo;

		removeAll();
		//create listeners to listen for mouse clicks
		
		//create and add label with picture and phrase
		Label personLabel = new Label("HELP!", icon);		
		add(personLabel);
		
		//add contact figure
		ContactFigure contact = new ContactFigure();
		contact.populate(mode);
		add(contact);
		
		//set labels with mouse listeners
		click.setType(this, "Help person");
		
		personLabel.addMouseListener(click);
		
	}
	
	public void promptQuestion()
	{
		click.mouseReleased(null);
	}
	
	public void changeText(String text)
	{
		
		//populate method but with new text
		
		removeAll();
		//create and add label with picture and phrase
		Label personLabel = new Label(text, icon);		
		add(personLabel);
		
		//add contact figure
		ContactFigure contact = new ContactFigure();
		contact.populate(mode);
		add(contact);
		
		//set labels with mouse listeners
		click.setType(this, "Help person");
		personLabel.addMouseListener(click);
	}

}
