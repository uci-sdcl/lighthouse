package edu.uci.lighthouse.extensions.codereview.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import org.apache.log4j.Logger;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;

@Entity(name="LighthouseCodeSelection")
public class CodeSelection {
	
	@Id @GeneratedValue
	private Integer id;
	
	@Lob
	private byte[] selection;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="selection_id", referencedColumnName="id")
	private Set<Comment> comments = new LinkedHashSet<Comment>();
	
	private static Logger logger = Logger.getLogger(CodeSelection.class);
	
	public CodeSelection(ITextSelection selection){
		setSelection(selection);
	}
	
	protected CodeSelection(){
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
			logger.error(e,e);
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
			logger.error(e,e);
		}
		this.selection = bos.toByteArray();
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

	public void addComment(Comment comment){
		comments.add(comment);
	}

	protected void setComments(Set<Comment> comments) {
		this.comments = comments;
	}
}
