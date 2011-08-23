package edu.uci.lighthouse.model.QAforums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ForumThread implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4548381815095491038L;

	@Id
    @GeneratedValue
    int id;
    
	private Post rootQuestion;
	private Solution solution;
	public LHthreadCreator threadCreator;

	public ForumThread(Post question) {
		rootQuestion = question;
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
}