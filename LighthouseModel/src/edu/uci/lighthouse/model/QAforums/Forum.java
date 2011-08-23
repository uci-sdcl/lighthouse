package edu.uci.lighthouse.model.QAforums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import edu.uci.lighthouse.LHmodelExtensions.LHclassPluginExtension;

@Entity
public class Forum extends LHclassPluginExtension implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8791302461228282266L;

	@Id
    @GeneratedValue
    int id;
    
    
	ArrayList<Thread> threads = new ArrayList<Thread>();
	
	public void addThread(Post rootPost){
		Thread thread = new Thread(rootPost);
		addThread(thread);
	}
	public void addThread(Thread thread){
		threads.add(thread);
		forumChanged();
	}
	
	public List<Thread> getThreads(){
		return threads;
	}
	
	private void forumChanged(){
        setChanged();
        notifyObservers();
        clearChanged();
	}
}
