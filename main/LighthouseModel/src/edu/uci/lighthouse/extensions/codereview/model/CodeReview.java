package edu.uci.lighthouse.extensions.codereview.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.util.CollectionsUtil;

@Entity(name = "LighthouseCodeReview")
@NamedQuery(name = "CodeReview.getPendingReviews", query = "SELECT review FROM LighthouseCodeReview review "
		+ "WHERE (review.reviewer = :reviewer "
		+ "OR review.reviewee = :reviewee) "
		+ "AND review.status <> '2' "
		+ "AND review.id > :lastId")
public class CodeReview {

	@Id
	@GeneratedValue
	private Integer id;

	@OneToOne
	@NotNull
	private LighthouseAuthor reviewee;

	@OneToOne
	@NotNull
	private LighthouseAuthor reviewer;

	@OneToMany(cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinColumn(name = "review_id", referencedColumnName = "id")
	@MapKey(name="id")
	private Map<Integer, FileSnapshot> filesSnapshot = new HashMap<Integer, FileSnapshot>();

	public static enum StatusType {
		OPEN, ACTIVE, CLOSE
	}

	@NotNull
	private StatusType status;

	public CodeReview(LighthouseAuthor reviewee) {
		this.reviewee = reviewee;
		this.status = StatusType.OPEN;
	}

	protected CodeReview() {
		// Required by JPA
	}

	public Integer getId() {
		return id;
	}

	protected void setId(Integer id) {
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

	public Collection<FileSnapshot> getFilesSnapshot() {
		return filesSnapshot.values();
	}

	public void addFileSnapshot(FileSnapshot snapshot) {
		filesSnapshot.put(snapshot.getId(), snapshot);
	}

	public FileSnapshot getFileSnapshotById(int id){
		return filesSnapshot.get(id);
	}

	public StatusType getStatus() {
		return status;
	}

	public void setStatus(StatusType status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CodeReview) {
			CodeReview other = (CodeReview) obj;
			if (id.equals(other.id)
					&& reviewee.equals(other.reviewee)
					&& reviewer.equals(other.reviewer)
					&& status.equals(other.status)
					&& CollectionsUtil.equals(filesSnapshot.values(),
							other.filesSnapshot.values())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Review "+id;
	}
}
