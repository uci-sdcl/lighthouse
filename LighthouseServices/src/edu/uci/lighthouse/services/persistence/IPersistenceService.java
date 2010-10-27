package edu.uci.lighthouse.services.persistence;

import edu.uci.lighthouse.model.io.IPersistable;
import edu.uci.lighthouse.services.ILighthouseService;

public interface IPersistenceService extends ILighthouseService {
	
	public void save(IPersistable obj) throws PersistenceException;
	
	public IPersistable load(IPersistable obj) throws PersistenceException;
	
}
