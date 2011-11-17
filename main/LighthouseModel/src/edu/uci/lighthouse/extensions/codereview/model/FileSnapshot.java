package edu.uci.lighthouse.extensions.codereview.model;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

@Entity(name = "LighthouseFileSnapShot")
public class FileSnapshot {
	
	@Id @GeneratedValue
	private Integer id;	
	
	@Lob
	private String content;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="snapshot_id", referencedColumnName="id")
	private Set<CodeSelection> codeSelections = new LinkedHashSet<CodeSelection>();

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

	public Set<CodeSelection> getCodeSelection() {
		return codeSelections;
	}
	
	public void addCodeSelection(CodeSelection codeSelection){
		codeSelections.add(codeSelection);
	}

	protected void setCodeSelection(Set<CodeSelection> codeSelection) {
		this.codeSelections = codeSelection;
	}
}
