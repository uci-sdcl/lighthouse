package edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Shell;

import edu.uci.lighthouse.model.QAforums.LHforum;

public class ViewManager {
	
	private static ViewManager instance;
	
	private ArrayList<CompartmentThreadView> openShells = new ArrayList<CompartmentThreadView>();
	private ViewManager(){}
	
	public static ViewManager getInstance(){
		if(instance == null)
			instance = new ViewManager();
		
		return instance;
	}
	
	public void updateShells(LHforum forum){
		for(CompartmentThreadView view: openShells){
			view.updateView(forum);
		}
	}
	
	public void clearViews(){
		openShells.clear();
	}
	
	
	public void addCompartmentThreadView(CompartmentThreadView shell){
		
		openShells.add(shell);
	}

}
