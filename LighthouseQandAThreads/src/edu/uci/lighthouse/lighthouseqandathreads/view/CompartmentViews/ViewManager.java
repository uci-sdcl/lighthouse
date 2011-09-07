package edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Shell;

public class ViewManager {
	
	private ViewManager instance;
	
	private ArrayList<Shell> openShells = new ArrayList<Shell>();
	private ViewManager(){}
	
	public ViewManager getInstance(){
		if(instance == null)
			instance = new ViewManager();
		
		return instance;
	}
	
	
	public void addShell(Shell shell){
		
		
		for(Shell openShell: openShells){
			openShell.close();
		}
		
		openShells.add(shell);
	}

}
