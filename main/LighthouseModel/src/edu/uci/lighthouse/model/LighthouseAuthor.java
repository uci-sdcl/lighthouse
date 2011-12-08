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
package edu.uci.lighthouse.model;

import java.io.Serializable;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Entity;
import javax.persistence.Id;

import edu.uci.lighthouse.model.util.LHStringUtil;

/**
 * This is the class that represents each developer
 */
@Entity
public class LighthouseAuthor implements Serializable{

	private static final long serialVersionUID = 4952522633654542472L;
	
	@Id
	private String name;
	
	public LighthouseAuthor(String name) {
		this.name = name;
	}

	protected LighthouseAuthor() {
		this("");
	}

	public String getName() {
		int pos = name.indexOf("@");
		if (pos != -1) {
			return name.substring(0, pos);
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getFullName(){
		return name;
	}
	
	public URL getAvatar() {
		URL result = null;
		try {
			result = new URL(
					"https://ssl.gstatic.com/ui/v1/icons/mail/profile_mask2.png");
			int pos = name.indexOf("@");
			if (pos != -1) {

				result = new URL("http://www.gravatar.com/avatar/"
						+ LHStringUtil.getMD5Hash(name).toLowerCase() + "?s=32");

			}
		} catch (Exception e) {

		}
		return result;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LighthouseAuthor other = (LighthouseAuthor) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
