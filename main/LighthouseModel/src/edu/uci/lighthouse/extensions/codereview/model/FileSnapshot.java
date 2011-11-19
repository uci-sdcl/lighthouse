package edu.uci.lighthouse.extensions.codereview.model;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import edu.uci.lighthouse.model.util.CollectionsUtil;

@Entity(name = "LighthouseFileSnapShot")
public class FileSnapshot {

	@Id
	@GeneratedValue
	private Integer id;

	@Lob
	private String content;

	@OneToMany(cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinColumn(name = "snapshot_id", referencedColumnName = "id")
	private Collection<CodeSelection> codeSelections = new LinkedHashSet<CodeSelection>();

	public String getContent() {
		return content;
	}

	public Integer getId() {
		return id;
	}

	protected void setId(Integer id) {
		this.id = id;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Collection<CodeSelection> getCodeSelection() {
		return codeSelections;
	}

	public void addCodeSelection(CodeSelection codeSelection) {
		codeSelections.add(codeSelection);
	}

	protected void setCodeSelection(Set<CodeSelection> codeSelection) {
		this.codeSelections = codeSelection;
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
		if (obj instanceof FileSnapshot) {
			FileSnapshot other = (FileSnapshot) obj;
			if (id.intValue() == other.id.intValue()
					&& content.equals(other.content)
					&& CollectionsUtil.equals(codeSelections,
							other.codeSelections)) {
				return true;
			}
		}
		return false;
	}
}
