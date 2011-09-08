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
public class LHforum extends LHclassPluginExtension implements Serializable{
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
	

	/**
	 * Returns first thread that has a root post equal to the
	 * argument.  Equality for post is determined if their messages
	 * are equal. 
	 * 
	 * @param post
	 * @return
	 */
	public ForumThread getThreadWithRootQuestion(Post post){
		for(ForumThread thread: threads){
			Post root = thread.getRootQuestion();
			if(root.getMessage().equals(post.getMessage()))
				return thread;
		}
		return null;
	}

	
	public void addThread(Post rootPost){
		ForumThread thread = new ForumThread(rootPost);
		addThread(thread);
		
	}
	
	public List<ForumThread> orderThreads(){
		int i;
		int min;

		ArrayList<ForumThread> listOfThreads = new ArrayList<ForumThread>();
		listOfThreads.addAll(this.threads);

		for (i = 0; i < this.threads.size(); i++) {
			min = i;
			for (int j = i + 1; j < this.threads.size(); j++) {
				if (listOfThreads.get(j).getRootQuestion().getPostTime()
						.compareTo(listOfThreads.get(min).getRootQuestion().getPostTime()) < 0) {
					min = j;
				}
			}

			ForumThread temp = listOfThreads.get(i);
			listOfThreads.set(i, listOfThreads.get(min));
			listOfThreads.set(min, temp);
		}

		return listOfThreads;
	}
	
	public void addThread(ForumThread thread){
		threads.add(thread);
		forumChanged(new AddEvent<ForumThread,LHforum>(thread,this));
	}
	
	public void setThreads(Collection<ForumThread> threads){
		this.threads = threads;
	}
	
	public Collection<ForumThread> getThreads(){
		return threads;
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
	
	
	private void forumChanged(Object object){
        setChanged();
        notifyObservers(object);
        clearChanged();
	}

	public int countTotalResonses() {
		int count = 0;
		for(ForumThread thread: this.getThreads()){
			Post root = thread.getRootQuestion();
			count = count + root.getResponses().size();
		}
		return count;
	}

	public boolean hasSolvedThreads() {
		for(ForumThread thread: threads){
			if(thread.hasSolution()){
				return true;
			}
		}
		return false;
	}
	
	public boolean hasUnSolvedThreads() {
		for(ForumThread thread: threads){
			if(!thread.hasSolution()){
				return true;
			}
		}
		return false;
	}
	



}
