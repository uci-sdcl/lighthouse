package edu.uci.lighthouse.lighthouseqandathreads.view;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import edu.uci.lighthouse.lighthouseqandathreads.Activator;
import org.eclipse.draw2d.geometry.Rectangle;

public class VisualSummaryView extends Panel{
	private Image icon;
	private XYLayout layout;
	private int total;
	private int answered;
	private int responses;
	
	public VisualSummaryView(int total, int answered, int responses){
		layout = new org.eclipse.draw2d.XYLayout();
		setLayoutManager(layout);
		
			
		icon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
		"/icons/visualSummary.png").createImage();
		this.total = total;
		this.answered = answered;
		this.responses = responses;
		

		Label imageLabel = new Label();
		imageLabel.setIcon(icon);
		
		//this.add(imageLabel, new Rectangle(0,0,50,50));
		
		
		Label textLabel = new Label("["+answered+"] ["+responses+"] ["+total+"]");
		Label label = new Label(""+"a"+"   "+"r"+"   "+"t"+" ");
		this.add(textLabel, new Rectangle(0,0,100,30));
		this.add(label, new Rectangle(0,15,100,30));
	/*	
		this.add(createStatGrid("T",total,ColorConstants.blue),new Rectangle(0,0,50,50));
		this.add(createStatGrid("A",answered,ColorConstants.white),new Rectangle(60,0,60,50));
		this.add(createStatGrid("R",responses,ColorConstants.white),new Rectangle(90,0,60,50));*/
		
	}
	
	public Figure createStatGrid(String name, int number, Color backColor){
		Figure panel = new Figure();
		
		GridLayout gLayout = new GridLayout();
		gLayout.numColumns = 1;
		 
		panel.setLayoutManager(gLayout);
		

		 
		 Label statLabel = new Label(number+"");

		 
		 panel.add(statLabel);
		 

		 
		 Label textLabel = new Label(name);
		 panel.add(textLabel);
		 
		
		 
		 return panel;
	}
	
	public void setQuestionTotal(int total){
		this.total = total;
	}
	
	public void setQuestionAnswered(int answered){
		this.answered = answered;
	}
	
	

}
