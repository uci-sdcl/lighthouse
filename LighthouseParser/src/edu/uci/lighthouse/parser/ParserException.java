package edu.uci.lighthouse.parser;

public class ParserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2882305442959433879L;

	public ParserException(String message){
		super(message);
	}

	public ParserException(Throwable cause){
		super(cause);
	}

}
