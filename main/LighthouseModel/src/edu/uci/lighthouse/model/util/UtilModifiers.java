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
package edu.uci.lighthouse.model.util;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModifier;
import edu.uci.lighthouse.model.LighthouseRelationship;

public enum UtilModifiers {
	PUBLIC, PRIVATE, PROTECTED, DEFAULT, STATIC, FINAL, SYNCHRONIZED, VOLATILE, TRANSIENT, NATIVE, ABSTRACT, STRICTFP;

	public String toString() {
		return name().toLowerCase();
	}

	public static boolean isPublic(String fqn) {
		return (PUBLIC.toString().equals(fqn));
	}

	public static boolean isPrivate(String fqn) {
		return (PRIVATE.toString().equals(fqn));
	}

	public static boolean isProtected(String fqn) {
		return (PROTECTED.toString().equals(fqn));
	}

	public static boolean isStatic(String fqn) {
		return (STATIC.toString().equals(fqn));
	}

	public static boolean isFinal(String fqn) {
		return (FINAL.toString().equals(fqn));
	}

	public static boolean isSynchronized(String fqn) {
		return (SYNCHRONIZED.toString().equals(fqn));
	}

	public static boolean isVolatile(String fqn) {
		return (VOLATILE.toString().equals(fqn));
	}

	public static boolean isTransient(String fqn) {
		return (TRANSIENT.toString().equals(fqn));
	}

	public static boolean isNative(String fqn) {
		return (NATIVE.toString().equals(fqn));
	}

	public static boolean isAbstract(String fqn) {
		return (ABSTRACT.toString().equals(fqn));
	}

	public static boolean isStrictfp(String fqn) {
		return (STRICTFP.toString().equals(fqn));
	}	

	/**
	 * Returns the visibility of the entity. The values can be PUBLIC,
	 * PROTECTED, PRIVATE or DEFAULT.
	 */
	public static UtilModifiers getVisibility(LighthouseEntity e) {
		UtilModifiers result = DEFAULT;
		Date lastTimestamp = new Date(0);
		Collection<LighthouseRelationship> list = LighthouseModel.getInstance()
		.getRelationshipsFrom(e);
		for (LighthouseRelationship r : list) {
			if (r.getToEntity() instanceof LighthouseModifier) {
				Collection<LighthouseEvent> events = LighthouseModel
				.getInstance().getEvents(r);
				if (events.size() > 0) {
					for (LighthouseEvent event : events) {
						if (lastTimestamp.before(event.getTimestamp())) {
							lastTimestamp = event.getTimestamp();
							UtilModifiers modifier = UtilModifiers.valueOf(r.getToEntity().getFullyQualifiedName().toUpperCase());
							if (modifier == PUBLIC || modifier == PROTECTED || modifier == PRIVATE){
								result = modifier;
							}
						}
					}
				} else {
					UtilModifiers modifier = UtilModifiers.valueOf(r.getToEntity().getFullyQualifiedName().toUpperCase());
					if (modifier == PUBLIC || modifier == PROTECTED || modifier == PRIVATE){
						result = modifier;
					}
				}
			}
		}
		return result;
	}

	public static Collection<UtilModifiers> getModifiers(LighthouseEntity e){
		Collection<UtilModifiers> result = new LinkedList<UtilModifiers>();
		Collection<LighthouseRelationship> list = LighthouseModel.getInstance()
		.getRelationshipsFrom(e);
		for (LighthouseRelationship r : list) {
			if (r.getToEntity() instanceof LighthouseModifier) {
				UtilModifiers modifier = UtilModifiers.valueOf(r.getToEntity().getFullyQualifiedName().toUpperCase());
				if (modifier != PUBLIC || modifier != PROTECTED || modifier != PRIVATE){
					result.add(modifier);
				}
			}
		}
		return result;
	}
}
