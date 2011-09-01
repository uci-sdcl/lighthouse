package edu.uci.lighthouse.lighthouseqandathreads.view;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import edu.uci.lighthouse.lighthouseqandathreads.Activator;
import org.eclipse.draw2d.geometry.Rectangle;

public class VisualSummaryView extends Panel{
	private Image icon;
	private FlowLayout layout;
	private int total;
	private int answered;

	public VisualSummaryView(int total, int answered, int responses){
	//	layout = new org.eclipse.draw2d.XYLayout();
	//	setLayoutManager(layout);
		
		layout = new FlowLayout();
		layout.setMajorAlignment(FlowLayout.ALIGN_LEFTTOP);
		layout.setMinorSpacing(5);
		
		icon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
		"/icons/visualSummary.png").createImage();
		this.total = total;
		this.answered = answered;
		
		

	//	Label imageLabel = new Label();
	//	imageLabel.setIcon(icon);
		
	//	this.add(imageLabel, new Rectangle(0,0,50,50));
		
	//	Label textLabel = new Label(answered+"/"+total);
	//	this.add(textLabel, new Rectangle(0,0,50,50));
		
		createStatGrid("threads",total);
		createStatGrid("answers",answered);
		createStatGrid("responses",responses);
		
	}
	
	public void createStatGrid(String name, int number){
		Panel panel = new Panel();
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		 panel.setLayoutManager(layout);
		 Label statLabel = new Label(number+"");
		 panel.add(statLabel);
		 Label textLabel = new Label(name);
		 textLabel.add(textLabel);
		 this.add(panel);
	}
	
	public void setQuestionTotal(int total){
		this.total = total;
	}
	
	public void setQuestionAnswered(int answered){
		this.answered = answered;
	}
	
	

}
