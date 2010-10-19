package edu.uci.ligthouse.services.internal.persistence;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import edu.uci.lighthouse.services.persistence.IPersistable;
import edu.uci.lighthouse.services.persistence.IPersistenceService;
import edu.uci.lighthouse.services.persistence.PersistenceException;

public class GenericPersistenceService implements IPersistenceService {
	
	private static Logger logger = Logger.getLogger(GenericPersistenceService.class);
	
	@Override
	public String getServiceName() {
		return GenericPersistenceService.class.getName();
	}

	@Override
	public void save(IPersistable obj) throws PersistenceException {
		logger.debug("Saving " + obj.getFileName());
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(obj.getFileName()));
			oos.writeObject(obj);
			oos.close();
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	@Override
	public IPersistable load(IPersistable obj) throws PersistenceException {
		logger.debug("Loading " + obj.getFileName());
		IPersistable instance = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(
					new FileInputStream(obj.getFileName()));
			instance = (IPersistable) ois.readObject();
			ois.close();
		} catch (Exception e) {
			logger.error(e);
		}
		return instance;
	}

}
