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
 * An exception which is thrown when a UPC fails to construct
 * because the universal product code provided is null or illegal. 
 *
 */
public class InvalidUPCException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidUPCException() {
		super();
	}

	public InvalidUPCException(String message) {
		super(message);
	}

	public InvalidUPCException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidUPCException(Throwable cause) {
		super(cause);
	}

}
