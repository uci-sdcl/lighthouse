/*******************************************************************************
* Copyright (c) {2009,2011} {Software Design and Collaboration Laboratory (SDCL)
*				, University of California, Irvine}.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    {Software Design and Collaboration Laboratory (SDCL)
*	, University of California, Irvine}
*			- initial API and implementation and/or initial documentation
*******************************************************************************/
package edu.uci.lighthouse.model.QAforums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import edu.uci.lighthouse.model.LighthouseAuthor;

@Entity
public class TeamMember implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1320293063793751427L;
	@OneToOne (cascade = CascadeType.ALL)
	private LighthouseAuthor author;
	
	@Id
    @GeneratedValue
    int id;
	
	 @OneToMany (fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	Collection<Post> posts = new ArrayList<Post>();
	
	 public TeamMember(){}
	 
	public TeamMember(LighthouseAuthor author){
		this.setAuthor(author);
	}
	
	public void respondToThread(ForumThread aThread) {
		throw new UnsupportedOperationException();
	}

	public void setAuthor(LighthouseAuthor author) {
		this.author = author;
	}

	public LighthouseAuthor getAuthor() {
		return author;
	}
}
