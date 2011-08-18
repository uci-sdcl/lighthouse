package edu.uci.lighthouse.lighthouseqandathreads.model;

import java.util.Observable;
import java.util.Observer;

import edu.uci.lighthouse.lighthouseqandathreads.NewQuestionDialog;


public class FakeController implements Observer{
	NewQuestionDialog nqDialog;

	
	public FakeController(NewQuestionDialog dialog){
		nqDialog = dialog;
		FakeDataBase.getInstance().addObserver(this);
		
	}
	
	public void stopObserving(){
		FakeDataBase.getInstance().deleteObserver(this);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		nqDialog.clearTree();
		nqDialog.populateTree(FakeDataBase.getInstance().getForum());
		
	}

}
