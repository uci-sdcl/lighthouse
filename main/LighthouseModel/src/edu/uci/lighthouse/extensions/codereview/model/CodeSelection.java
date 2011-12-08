package edu.uci.lighthouse.extensions.codereview.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
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

import org.apache.log4j.Logger;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import edu.uci.lighthouse.model.util.CollectionsUtil;

@Entity(name = "LighthouseCodeSelection")
public class CodeSelection implements Cloneable, IDatabaseEntry {

	@Id
	@GeneratedValue
	private Integer id;

	@Lob
	@NotNull
	private byte[] selection;
	
	@OneToMany(cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinColumn(name = "selection_id", referencedColumnName = "id")
	@MapKey(name="id")
	private Map<Integer, Comment> comments = new HashMap<Integer, Comment>();

	private static Logger logger = Logger.getLogger(CodeSelection.class);

	public CodeSelection(ITextSelection selection) {
		setSelection(selection);
	}

	protected CodeSelection() {
		// Required by JPA
	}

	public ITextSelection getSelection() {
		ITextSelection result = null;
		ByteArrayInputStream bis = new ByteArrayInputStream(this.selection);
		DataInputStream dis = new DataInputStream(bis);
		try {
			final int offset = dis.readInt();
			final int length = dis.readInt();
			final int startLine = dis.readInt();
			result = new TextSelection(offset, length){
				@Override
				public int getStartLine() {
					return startLine;
				}
			};
			dis.close();
			bis.close();
		} catch (IOException e) {
			logger.error(e, e);
		}
		return result;
	}

	public void setSelection(ITextSelection selection) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeInt(selection.getOffset());
			dos.writeInt(selection.getLength());
			dos.writeInt(selection.getStartLine());
			dos.close();
			bos.close();
		} catch (IOException e) {
			logger.error(e, e);
		}
		this.selection = bos.toByteArray();
	}

	public Integer getId() {
		return id;
	}

	void setId(Integer id) {
		this.id = id;
	}

	public Collection<Comment> getComments() {
		//return comments;
		return comments.values();
	}

	public void addComment(Comment comment) {
		//comments.add(comment);
		comments.put(comment.getId(), comment);
	}
	
	public Comment getCommentById(int id){
		return comments.get(id);
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
		if (obj instanceof CodeSelection) {
			CodeSelection other = (CodeSelection) obj;
			if (id.equals(other.id)
					&& Arrays.equals(selection, other.selection)
					&& CollectionsUtil.equals(comments.values(), other.comments.values())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Line "+getSelection().getStartLine();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		CodeSelection clone = (CodeSelection)super.clone();
		clone.comments = new HashMap<Integer, Comment>(this.comments);
		return clone;
	}
}
