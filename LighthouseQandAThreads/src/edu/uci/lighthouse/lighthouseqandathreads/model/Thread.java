package edu.uci.lighthouse.lighthouseqandathreads.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Thread {
    @Id
    @GeneratedValue
    int id;
    
	private Post rootQuestion;
	private Solution solution;
	//public ThreadCreator threadCreator;

	public Thread(Post question) {
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