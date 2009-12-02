package edu.uci.lighthouse.parser;

import java.util.Collection;

import org.eclipse.core.resources.IFile;

public interface IParser {

	public void setFiles(Collection<IFile> files);
	
	public Collection<IFile> getFiles();
	
	public void parse() throws ParseException;
	
	public Collection<ParserEntity> getEntities();
	
	public Collection<ParserRelationship> getRelationships();
	
}
