package edu.uci.lighthouse.model.io;

import javax.persistence.PersistenceException;

public interface IPersistence {

	public static int XML = 0;
	
	public static int BINARY = 1;
	
	public void save() throws PersistenceException;
	
	public void save(String fileName) throws PersistenceException;
	
	public void load() throws PersistenceException;
	
	public void load(String fileName) throws PersistenceException;
	
}
