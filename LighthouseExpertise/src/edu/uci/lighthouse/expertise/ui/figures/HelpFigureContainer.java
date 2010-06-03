package edu.uci.lighthouse.expertise.ui.figures;


import java.util.ArrayList;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Label;

import edu.uci.lighthouse.ui.figures.CompartmentFigure;

import edu.uci.lighthouse.ui.figures.ILighthouseClassFigure.MODE;


//Created by Alex Taubman

public class HelpFigureContainer extends CompartmentFigure 
{

	
	private FlowLayout layout;
	private MODE mode;

	private ArrayList<HelpFigure> figures = new ArrayList<HelpFigure>();

	public HelpFigureContainer() 
	{
		//setup layout and import images
		layout = new FlowLayout();
		layout.setHorizontal(false);
		setLayoutManager(layout);

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
		
		//set label that user clicks to add new question
		Label personLabel = new Label("Add New Question");		
		add(personLabel);
		ClickListener click = new ClickListener();
		//set labels with mouse listeners
		click.setType(this, "New Question");
		personLabel.addMouseListener(click);
		
		//add first container
		addNewContainer(true);
		
		



	}

	public void addNewContainer(boolean first)
	{

		HelpFigure toAdd = new HelpFigure();
		toAdd.populate(mode);
		figures.add(toAdd);
		add(toAdd);
		
		//ask for initial text when not the first to be added
		if (!first)
			toAdd.promptQuestion();
		


	}

}
