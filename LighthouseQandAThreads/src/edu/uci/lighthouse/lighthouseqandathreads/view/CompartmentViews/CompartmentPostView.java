package edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews;

import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Panel;

public class CompartmentPostView extends Panel{

	private int displayLength = 100;
	private int NUM_COLUMNS = 1;
	
	public CompartmentPostView(){
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.numColumns = NUM_COLUMNS;			
		layout.marginHeight = 0;
		layout.marginWidth = 0; 
		
		setLayoutManager(layout);
	}
	public CompartmentPostView(String message){
		
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.numColumns = NUM_COLUMNS;			
		layout.marginHeight = 0;
		layout.marginWidth = 0; 
		
		setLayoutManager(layout);
		
		Label messageLabel = new Label(message.length() >= displayLength ? message.substring(0, displayLength) : message);
		this.add(messageLabel);
	}
}
