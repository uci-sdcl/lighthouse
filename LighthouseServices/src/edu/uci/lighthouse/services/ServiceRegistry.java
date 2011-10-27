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
package edu.uci.lighthouse.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ServiceRegistry {
	private Map<String, ILighthouseService> svcMap = new HashMap<String, ILighthouseService>();
	private static ServiceRegistry instance;
	
	public synchronized static ServiceRegistry getInstance() {
		if (instance == null) {
			instance = new ServiceRegistry();
		}
		return instance;
	}
	
	public synchronized void register(ILighthouseService svc){
		svcMap.put(svc.getServiceName(), svc);
	}
	
	public synchronized void unregister(ILighthouseService svc) {
		svcMap.remove(svc.getServiceName());
	}
	
	public Collection<ILighthouseService> getRegisteredServices() {
		return svcMap.values();
	}
	
	public ILighthouseService getService(String serviceName){
		return svcMap.get(serviceName);
	}
}
