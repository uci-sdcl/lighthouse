package edu.uci.lighthouse.services;

import edu.uci.ligthouse.services.internal.persistence.GenericPersistenceService;

public class LighthouseServiceFactory {

	public static ILighthouseService getService(String serviceName) {
		return new GenericPersistenceService();
	}
	
}
