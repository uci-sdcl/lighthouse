package edu.uci.lighthouse.model.QAforums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class ForumThread extends Observable implements Serializable, Observer{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4548381815095491038L;

	@Id
    @GeneratedValue
    int id;
	
	 @OneToOne(cascade = CascadeType.ALL)
	private Post rootQuestion;
	
	 @OneToOne(cascade = CascadeType.ALL)
	private Solution solution;
	 
	 @OneToOne(cascade = CascadeType.ALL)
	public LHthreadCreator threadCreator;

	public ForumThread(){
		if(rootQuestion != null){
			rootQuestion.addObserver(this);
		}
	}
	public ForumThread(Post question) {
		rootQuestion = question;
		rootQuestion.addObserver(this);
	}
	

	public Post getRootQuestion(){
		return rootQuestion;
	}
	
	public boolean hasSolution(){
		return solution != null;
	}
	
	public Solution getSolution(){
		return solution;
	}
	
	public void update(Observable o, Object arg) {
		ThreadChanged();
	}
	
	private void ThreadChanged(){
		System.out.println("thread changed");

		setChanged();
		notifyObservers(new Update());
	    clearChanged();
	}
}