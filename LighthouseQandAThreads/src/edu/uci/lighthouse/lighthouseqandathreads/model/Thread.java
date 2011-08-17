package edu.uci.lighthouse.lighthouseqandathreads.model;

import java.util.ArrayList;
import java.util.List;

public class Thread {
	private String subject;
	private Post rootQuestion;
	private Solution solution;
	public ThreadCreator threadCreator;

	public Thread(String subject, Post question) {
		this.subject = subject;
		rootQuestion = question;
	}
	
	
	public String getSubject(){
		return subject;
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