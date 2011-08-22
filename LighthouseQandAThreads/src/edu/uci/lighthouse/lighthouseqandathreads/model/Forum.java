package edu.uci.lighthouse.lighthouseqandathreads.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import edu.uci.lighthouse.LHmodelExtensions.ILHclassPluginExtension;

@Entity
public class Forum implements ILHclassPluginExtension{
    @Id
    @GeneratedValue
    int id;
    
    @OneToMany
	ArrayList<Thread> threads = new ArrayList<Thread>();
	
	public void addThread(Thread thread){
		threads.add(thread);
	}
	
	public List<Thread> getThreads(){
		return threads;
	}
}
