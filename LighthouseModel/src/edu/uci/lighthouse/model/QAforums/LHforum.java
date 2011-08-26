package edu.uci.lighthouse.model.QAforums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.Hibernate;
import org.hibernate.annotations.CollectionOfElements;

import edu.uci.lighthouse.LHmodelExtensions.LHclassPluginExtension;

@Entity
public class LHforum extends LHclassPluginExtension implements Serializable, Observer{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8791302461228282266L;

	@Id
    @GeneratedValue
    int id;
    
	
	@OneToMany (fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	Collection<ForumThread> threads = new ArrayList<ForumThread>();
	
	public LHforum(){
		
	}
	
	/**
	 * 
	 * @param root
	 * @return Will return null if this method does not find thread
	 * with root question root.
	 */
	public ForumThread getThreadWithRoot(Post root){
		for(ForumThread thread: threads){
			if(thread.getRootQuestion() == root){
				return thread;
			}
		}
		return null;
	}
	
	private void observeThreads(){
		for(ForumThread thread : threads){
			thread.addObserver(this);
		}
	}
	
	private void initThreadObserving(){
		for(ForumThread thread : threads){
			thread.initObserving();
		}
	}
	
	public void addThread(Post rootPost){
		ForumThread thread = new ForumThread(rootPost);
		addThread(thread);
		
	}
	
	private void addThread(ForumThread thread){
		threads.add(thread);
		thread.addObserver(this);
		forumChanged(thread);
	}
	
	public void setThreads(Collection<ForumThread> threads){
		this.threads = threads;
		observeThreads();
	}
	
	public Collection<ForumThread> getThreads(){
		return threads;
	}
	
	public void initObserving(){
		this.observeThreads();
		initThreadObserving();
	}
	
	public int countThreads(){
		return threads.size();
	}
	
	public int countSolvedThreads(){
		int answered = 0;
		
		for(ForumThread thread: threads){
			if(thread.hasSolution()){
				answered++;
			}
		}
		return answered;
	}
	
	public int countUnSolvedThreads(){
		return countThreads() - countSolvedThreads();
	}
	
	private void forumChanged(ForumThread thread){
        setChanged();
        notifyObservers(new Update<ForumThread>(thread));
        clearChanged();
	}
	
	private void forumChanged(){
        setChanged();
        notifyObservers(new Update());
        clearChanged();
	}

	public void update(Observable arg0, Object arg1) {
		forumChanged();
	}
	

}
