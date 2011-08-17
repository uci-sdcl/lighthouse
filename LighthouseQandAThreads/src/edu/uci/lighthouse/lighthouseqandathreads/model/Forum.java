package edu.uci.lighthouse.lighthouseqandathreads.model;

import java.util.ArrayList;
import java.util.List;

public class Forum {
	ArrayList<Thread> threads = new ArrayList<Thread>();
	
	public void addThread(Thread thread){
		threads.add(thread);
	}
	
	public List<Thread> getThreads(){
		return threads;
	}
}
