/*******************************************************************************
* Copyright (c) {2009,2011} {Software Design and Collaboration Laboratory (SDCL)
*				, University of California, Irvine}.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    {Software Design and Collaboration Laboratory (SDCL)
*	, University of California, Irvine}
*			- initial API and implementation and/or initial documentation
*******************************************************************************/
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
public class ForumThread extends Observable implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4548381815095491038L;

	@Id
    @GeneratedValue
    int id;
	
	 @OneToOne (cascade = CascadeType.ALL)
	private Post rootQuestion;
	
	 @OneToOne (cascade = CascadeType.ALL)
	private Solution solution;
	 
	 @OneToOne (cascade = CascadeType.ALL)
	public LHthreadCreator threadCreator;
	 
	private boolean closed = false;

	public ForumThread(){
	}

	public ForumThread(Post question) {
		rootQuestion = question;
		rootQuestion.setThread(this);
	}
	
	public void setRootQuestion(Post question){
		rootQuestion = question;
		rootQuestion.setRoot(true);
		rootQuestion.setThread(this);
	}
	

	public Post getRootQuestion(){
		return rootQuestion;
	}
	
	public boolean hasSolution(){
		return solution != null;
	}
	
	public void setSolution(Solution solution){
		this.solution = solution;
		try {
			ThreadChanged(new UpdateEvent<ForumThread>(this,this.getClass().getDeclaredField("solution")));
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Solution getSolution(){
		return solution;
	}
	

	/**
	 * 
	 * @return if anyone has replies to the root question of this thread
	 */
	public boolean hasReplies(){
		return this.getRootQuestion().getResponses().size() > 0;
	}

	

	
	private void ThreadChanged(Object arg){
		setChanged();
		notifyObservers(arg);
	    clearChanged();
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
		try {
			ThreadChanged(new UpdateEvent<ForumThread>(this,this.getClass().getDeclaredField("closed")));
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isClosed() {
		return closed;
	}
	

}
