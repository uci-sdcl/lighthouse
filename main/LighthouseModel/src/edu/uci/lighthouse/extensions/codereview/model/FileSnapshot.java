package edu.uci.lighthouse.extensions.codereview.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import edu.uci.lighthouse.model.util.CollectionsUtil;

@Entity(name = "LighthouseFileSnapShot")
public class FileSnapshot implements Cloneable, IDatabaseEntry{

	@Id
	@GeneratedValue
	private Integer id;
	
	@NotNull
	private String filename;

	@Lob
	@NotNull
	private String content;

	@OneToMany(cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinColumn(name = "snapshot_id", referencedColumnName = "id")
	@MapKey(name="id")
	private Map<Integer, CodeSelection> codeSelections = new HashMap<Integer, CodeSelection>();

	public Integer getId() {
		return id;
	}

	void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public Collection<CodeSelection> getCodeSelection() {
		return codeSelections.values();
	}

	public void addCodeSelection(CodeSelection codeSelection) {
		codeSelections.put(codeSelection.getId(), codeSelection);
	}

	public CodeSelection getCodeSelectionById(int id){
		return codeSelections.get(id);
	}
	
	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
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
			if (id.equals(other.id)
					&& content.equals(other.content)
					&& CollectionsUtil.equals(codeSelections.values(),
							other.codeSelections.values())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return getFilename();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		FileSnapshot clone = (FileSnapshot) super.clone();
		clone.codeSelections = new HashMap<Integer, CodeSelection>(this.codeSelections); 
		return clone;
	}
}
