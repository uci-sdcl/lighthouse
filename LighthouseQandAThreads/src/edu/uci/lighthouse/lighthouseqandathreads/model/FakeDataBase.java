package edu.uci.lighthouse.lighthouseqandathreads.model;

public class FakeDataBase {
	Forum forum = new Forum();

	private FakeDataBase() {

	}
	
	public void populate(TeamMember tm){
		Post rootPost = new Post(true,"Need Interface","Hey Bill, would you make a getFoo()?", tm);
		
		Post response = new Post(false,"RE: Need Interface","Anyone out there?",tm);
		rootPost.addResponse(response);
		
		Thread thread = new Thread(rootPost);
		
		Forum forum = new Forum();
		forum.addThread(thread);
	}
	
	public static FakeDataBase getInstance(){
		return new FakeDataBase();
	}
	
	public void addNewThread(Post root){
		Thread thread = new Thread(root);
		forum.addThread(thread);
	}
	
	public Forum getForum(){
		return forum;
	}
}
