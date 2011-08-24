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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import edu.uci.lighthouse.LHmodelExtensions.LHclassPluginExtension;

@Entity
public class Post extends Observable implements Serializable, Observer{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8635902581163644158L;

	@Id
    @GeneratedValue
    int id;
    
	private String subject;
	private boolean question;
	@OneToMany (cascade = CascadeType.ALL)
	private Collection<Post> responses = new ArrayList<Post>();
	private String message = "";
	@ManyToOne (cascade = CascadeType.ALL)
	private TeamMember author;
	
	public Post(){
	}
	
	public Post(boolean isQuestion, String subject, String message, TeamMember author){
		this.subject = subject;
		this.question = isQuestion;
		this.message = message;
		this.author = author;
	}
	
	private void observeResponses(){
		for(Post post : responses){
			post.addObserver(this);
		}
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setAuthor(TeamMember author) {
		this.author = author;
	}
	public TeamMember getTeamMemberAuthor() {
		return author;
	}
	public void setQuestion(boolean question) {
		this.question = question;
	}
	public boolean isQuestion() {
		return question;
	}
	
	public void setResponses(Collection<Post> responses){
		this.responses = responses;
		observeResponses();
	}
	
	public Collection<Post> getResponses(){
		return responses;
	}
	
	public void addResponse(Post post){
		responses.add(post);
		post.addObserver(this);
		PostChanged();
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubject() {
		return subject;
	}
	
	
	private void initResponseObserving(){
		for(Post post : responses){
			post.initObserving();
		}
	}
	
	public void initObserving(){
		observeResponses();
		initResponseObserving();
	}
	
	private void PostChanged(){
		setChanged();
		notifyObservers(new Update());
	    clearChanged();
	}

	
	public void update(Observable o, Object arg) {
		PostChanged();		
	}
}