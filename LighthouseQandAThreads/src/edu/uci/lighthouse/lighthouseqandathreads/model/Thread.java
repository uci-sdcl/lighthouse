package edu.uci.lighthouse.lighthouseqandathreads.model;

import java.util.ArrayList;
import java.util.List;

public class Thread {
	private Post rootQuestion;
	private Solution solution;
	public ThreadCreator threadCreator;

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