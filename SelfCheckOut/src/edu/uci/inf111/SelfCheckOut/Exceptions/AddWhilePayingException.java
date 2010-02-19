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
 * scan a new item once they have begun to pay for their purchases.
 *
 */
public class AddWhilePayingException extends IncorrectStateException {
	private static final long serialVersionUID = 1L;

	public AddWhilePayingException() {
		super();
	}

	public AddWhilePayingException(String message) {
		super(message);
	}

	public AddWhilePayingException(String message, Throwable cause) {
		super(message, cause);
	}

	public AddWhilePayingException(Throwable cause) {
		super(cause);
	}

}
