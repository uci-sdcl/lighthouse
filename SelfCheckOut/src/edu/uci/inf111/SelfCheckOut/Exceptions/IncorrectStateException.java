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
 * The IncorrectStateException is not normally used directly, but acts as a parent
 * class for those exceptions which represent violations of the business rules of
 * the states a SelfCheckOut object can be in. 
 *
 */
public class IncorrectStateException extends Exception {
	private static final long serialVersionUID = 1L;

	public IncorrectStateException() {
		super();
	}

	public IncorrectStateException(String message) {
		super(message);
	}

	public IncorrectStateException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncorrectStateException(Throwable cause) {
		super(cause);
	}

}
