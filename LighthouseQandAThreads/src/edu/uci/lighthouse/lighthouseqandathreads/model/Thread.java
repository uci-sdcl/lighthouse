package edu.uci.lighthouse.lighthouseqandathreads.model;

import java.util.List;

public class Thread {
	private String subject;
	private Post rootQuestion;
	private Solution solution;
	private List<Post> responses;
	public ThreadCreator threadCreator;

	public Thread(Post question) {
		throw new UnsupportedOperationException();
	}
}