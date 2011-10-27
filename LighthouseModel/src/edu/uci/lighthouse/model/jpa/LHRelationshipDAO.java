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
package edu.uci.lighthouse.model.jpa;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import edu.uci.lighthouse.model.LHRelationshipPK;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseRelationship;

public class LHRelationshipDAO extends AbstractDAO<LighthouseRelationship, LHRelationshipPK> { 
	
	@SuppressWarnings("unchecked")
	public List<LighthouseEntity> executeNamedQueryGetFromEntityFqn(String nameQuery,
			Map<String, Object> parameters) throws JPAException {
		EntityManager entityManager = JPAUtility.createEntityManager();
		Query query = entityManager.createNamedQuery(nameQuery);
		if (parameters != null) {
			for (Map.Entry<String, Object> entry : parameters.entrySet()) {
				if (entry.getValue() instanceof Date) {
					query.setParameter(entry.getKey(), (Date) entry.getValue(),
							TemporalType.TIMESTAMP);
				} else {
					query.setParameter(entry.getKey(), entry.getValue());
				}
			}
		}
		List<LighthouseEntity> result = query.getResultList();
		JPAUtility.closeEntityManager(entityManager);
		return result;
	}
	
}
