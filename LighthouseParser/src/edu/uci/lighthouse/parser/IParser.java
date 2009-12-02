package edu.uci.lighthouse.parser;

import java.util.Collection;

import org.eclipse.core.resources.IFile;

public interface IParser {

	public void parse(Collection<IFile> files) throws ParserException;
	
	public Collection<ParserEntity> getEntities();
	
	public Collection<ParserRelationship> getRelationships();
	
}
