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
