package edu.uci.lighthouse.lighthouseqandathreads.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Forum {
    @Id
    @GeneratedValue
    int id;
    
	ArrayList<Thread> threads = new ArrayList<Thread>();
	
	public void addThread(Thread thread){
		threads.add(thread);
	}
	
	public List<Thread> getThreads(){
		return threads;
	}
}
