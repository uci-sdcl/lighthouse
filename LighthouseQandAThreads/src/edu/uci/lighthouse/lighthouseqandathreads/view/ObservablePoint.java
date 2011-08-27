package edu.uci.lighthouse.lighthouseqandathreads.view;

import java.util.Observable;

public class ObservablePoint extends Observable{

	public void changed(){
		setChanged();
		notifyObservers();
	    clearChanged();
	}
	
}
