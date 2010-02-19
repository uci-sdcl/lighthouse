/*
 * 
 * Creator: Susan Elliott Sim
 * Course: Inf111, Winter 2008
 * 
 * Created on May 10, 2006
 * Updated on January 17, 2008
 * 
 * Copyright, 2006, 2008 University of California. 
 */

package edu.uci.inf111.SelfCheckOut.Exceptions;


/**
 * A simple exception which is thrown when a customer tries to 
 * scan a new item before bagging the previous one.
 *
 */
public class AddWhileBaggingException extends IncorrectStateException {
	private static final long serialVersionUID = 1L;

	public AddWhileBaggingException() {
		super();
	}

	public AddWhileBaggingException(String message) {
		super(message);
	}

	public AddWhileBaggingException(String message, Throwable cause) {
		super(message, cause);
	}

	public AddWhileBaggingException(Throwable cause) {
		super(cause);
	}

}
