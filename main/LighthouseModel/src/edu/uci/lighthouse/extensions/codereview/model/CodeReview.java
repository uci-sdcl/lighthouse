package edu.uci.lighthouse.extensions.codereview.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import edu.uci.lighthouse.model.LighthouseAuthor;

@Entity(name = "LighthouseCodeReview")
public class CodeReview {

	@Id @GeneratedValue
	private Integer id;	
	
	@OneToOne(cascade = CascadeType.ALL)
	private LighthouseAuthor reviewee;
	
	@OneToOne(cascade = CascadeType.ALL)
	private LighthouseAuthor reviewer;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="review_id", referencedColumnName="id")	
	private Set<FileSnapshot> fileSnapshot;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LighthouseAuthor getReviewee() {
		return reviewee;
	}

	public void setReviewee(LighthouseAuthor reviewee) {
		this.reviewee = reviewee;
	}

	public LighthouseAuthor getReviewer() {
		return reviewer;
	}

	public void setReviewer(LighthouseAuthor reviewer) {
		this.reviewer = reviewer;
	}

	public Set<FileSnapshot> getFileSnapshot() {
		return fileSnapshot;
	}

	public void setFileSnapshot(Set<FileSnapshot> fileSnapshot) {
		this.fileSnapshot = fileSnapshot;
	}
}
