package edu.uci.lighthouse.lighthouseqandathreads;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.QAforums.LHforum;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.LHEntityDAO;


public class Controller implements Observer{
	NewQuestionDialog nqDialog;
	LHforum forum;
	LighthouseEntity entity;
	
	public Controller(NewQuestionDialog dialog, LHforum forum, LighthouseEntity entity){
		nqDialog = dialog;
		//nqDialog.getObservablePoint().addObserver(this);
		//FakeDataBase.getInstance().addObserver(this);
		this.forum = forum;
		this.forum.addObserver(this);
		this.entity = entity;
		
		
	}
	
	private void populateTree(LHforum forum){
		nqDialog.clearTree();
		nqDialog.populateTree(forum);
	}
	

	
	public void stopObserving(){
		//FakeDataBase.getInstance().deleteObserver(this);
		forum.deleteObserver(this);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		
		LHEntityDAO entityDAO = new LHEntityDAO();
		try {
			entityDAO.save(entity);
		} catch (JPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*if(DataBase update){
			//new reply Post to Post
			
		}else if(Dialog update){
			//loaded?
		}*/
		
		populateTree(forum);
		
		//nqDialog.populateTree(FakeDataBase.getInstance().getForum());

	}
	
	
}
