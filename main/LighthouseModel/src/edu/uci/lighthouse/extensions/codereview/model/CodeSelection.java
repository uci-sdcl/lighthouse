package edu.uci.lighthouse.extensions.codereview.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
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

import org.apache.log4j.Logger;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import edu.uci.lighthouse.model.util.CollectionsUtil;

@Entity(name = "LighthouseCodeSelection")
public class CodeSelection {

	@Id
	@GeneratedValue
	private Integer id;

	@Lob
	private byte[] selection;

	@OneToMany(cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinColumn(name = "selection_id", referencedColumnName = "id")
	private Collection<Comment> comments = new LinkedHashSet<Comment>();

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
			result = new TextSelection(dis.readInt(), dis.readInt());
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

	protected void setId(Integer id) {
		this.id = id;
	}

	public Collection<Comment> getComments() {
		return comments;
	}

	public void addComment(Comment comment) {
		comments.add(comment);
	}

	protected void setComments(Set<Comment> comments) {
		logger.debug("Set comments: " + comments.size());
		this.comments = comments;
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
			if (id.intValue() == other.id.intValue()
					&& Arrays.equals(selection, other.selection)
					&& CollectionsUtil.equals(comments, other.comments)) {
				return true;
			}
		}
		return false;
	}
}
