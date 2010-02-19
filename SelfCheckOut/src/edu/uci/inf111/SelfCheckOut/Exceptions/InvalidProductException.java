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
 * An exception which is thrown when a scanned product does not
 * exist in the product database. 
 *
 */
public class InvalidProductException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidProductException() {
		super();
	}

	public InvalidProductException(String message) {
		super(message);
	}

	public InvalidProductException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidProductException(Throwable cause) {
		super(cause);
	}

}
