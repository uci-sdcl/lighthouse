package edu.uci.lighthouse.model.QAforums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
    
	@OneToMany (cascade = CascadeType.ALL)
	Collection<ForumThread> threads = new ArrayList<ForumThread>();
	
	public LHforum(){}
	
	public void addThread(Post rootPost){
		ForumThread thread = new ForumThread(rootPost);
		addThread(thread);
	}
	public void addThread(ForumThread thread){
		threads.add(thread);
		forumChanged();
	}
	
	public Collection<ForumThread> getThreads(){
		return threads;
	}
	
	private void forumChanged(){
        setChanged();
        notifyObservers(new Update());
        clearChanged();
	}

	public void update(Observable arg0, Object arg1) {
		
	}
}
