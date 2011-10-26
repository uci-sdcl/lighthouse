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
package edu.uci.lighthouse.core.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Assert;

import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.LighthouseModelManager;
import edu.uci.lighthouse.model.io.IPersistable;
import edu.uci.lighthouse.services.LighthouseServiceFactory;
import edu.uci.lighthouse.services.persistence.IPersistenceService;
import edu.uci.lighthouse.services.persistence.PersistenceException;

/**
 * Creates and loads <code>IPersistable</code> instances lazily.
 * @author tproenca
 *
 */
public class PersistableRegistry {
	
	private static Map<Class<? extends IPersistable>, IPersistable> registry = new HashMap<Class<? extends IPersistable>, IPersistable>();
	
	private static Logger logger = Logger.getLogger(PersistableRegistry.class);
	
	/**
	 * Returns the class instance. If the instance does not exists, one will be created and it will try to load the previous state from the file system.
	 * @param clazz
	 */
	public static IPersistable getInstance(Class<? extends IPersistable> clazz){
		IPersistable instance = registry.get(clazz);
		if (instance == null) {
			IPersistenceService svc = (IPersistenceService) LighthouseServiceFactory.getService("GenericPersistenceService");
			try {
				// FIXME: Make this approach more generic.
				instance = LighthouseModel.getInstance();
				if (clazz.isInstance(instance)) {
					LighthouseModelManager manager = new LighthouseModelManager((LighthouseModel)instance);
					LighthouseModel inModel = (LighthouseModel)svc.load(instance);
					manager.modelCopy(inModel);
				} else {
					instance = clazz.newInstance();
					instance = svc.load(instance);
				}
			} catch (Exception e) {
				logger.error(e,e);
			} 
		}
		Assert.isNotNull(instance);
		registry.put(clazz, instance);
		return instance;
	}
	
	/**
	 * Saves instances from the registry to the file system.
	 */
	public static void saveInstances() {
		IPersistenceService svc = (IPersistenceService) LighthouseServiceFactory.getService("GenericPersistenceService");
		Collection<IPersistable> values = registry.values();
		for (IPersistable persistable : values) {
			try {
				svc.save(persistable);
			} catch (PersistenceException e) {
				logger.error(e,e);
			}
		}
	}
}
