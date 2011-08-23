package edu.uci.lighthouse.lighthouseqandathreads.model;

import java.util.Observable;


public class FakeDataBase extends Observable{
	private Forum forum = new Forum();
	private static FakeDataBase db;
	
	private FakeDataBase() {

	}
	
	public void populate(TeamMember tm){
		Post rootPost = new Post(true,"Need Interface","Hey Bill, would you make a getFoo()?", tm);
		
		Post response = new Post(false,"RE: Need Interface","Anyone out there?",tm);
		rootPost.addResponse(response);
		
		Thread thread = new Thread(rootPost);
		
		forum.addThread(thread);
		DBChanged();
	}
	
	public static FakeDataBase getInstance(){
		if(db == null){
			db = new FakeDataBase();
		}
		return db;
	}
	
	public void addNewThread(Post root){
		Thread thread = new Thread(root);
		forum.addThread(thread);
		DBChanged();
	}
	
	public void reply(Post replyee, Post reply){
		replyee.addResponse(reply);
		DBChanged();
	}
	

	private void DBChanged(){
        setChanged();
        notifyObservers();
        clearChanged();
	}
	
	public Forum getForum(){
		return forum;
	}
}
