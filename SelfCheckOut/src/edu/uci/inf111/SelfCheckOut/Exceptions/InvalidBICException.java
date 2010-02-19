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
 * An exception which is thrown when a BIC fails to construct
 * because the bulk item code provided is null or illegal. 
 */
public class InvalidBICException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidBICException() {
		super();
	}

	public InvalidBICException(String message) {
		super(message);
	}

	public InvalidBICException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidBICException(Throwable cause) {
		super(cause);
	}

}
