package edu.uci.lighthouse.lighthouseqandathreads.model;

import java.util.List;

public class Post {
	private boolean question;
	private List<Post> responses;
	private String message;
	private TeamMember author;
}