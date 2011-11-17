package edu.uci.lighthouse.extensions.codereview.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import org.eclipse.jface.text.ITextSelection;

@Entity(name="LighthouseCodeSelection")
public class CodeSelection {
	
	@Id @GeneratedValue
	private Integer id;
	
	@Lob
	private ITextSelection selection;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="selection_id", referencedColumnName="id")
	private Set<Comment> comments;
	
	
	public ITextSelection getSelection() {
		return selection;
	}


	public void setSelection(ITextSelection selection) {
		this.selection = selection;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Set<Comment> getComments() {
		return comments;
	}


	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}
}
