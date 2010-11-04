package edu.uci.lighthouse.parser;

public class ParserRelationship {

	public static enum RelationshipType {
		INSIDE, EXTENDS, IMPLEMENTS, RETURN, RECEIVES, HOLDS, USES, CALL, ACCESS, THROW, MODIFIED_BY
	}
	
	private String from;
	private String to;
	private RelationshipType type;
	
	public ParserRelationship(String from, String to, RelationshipType type) {
		this.from = from;
		this.to = to;
		this.type = type;
	}
	
	public String getFrom() {
		return from;
	}
	public String getTo() {
		return to;
	}
	public RelationshipType getType() {
		return type;
	}
	
	
}
