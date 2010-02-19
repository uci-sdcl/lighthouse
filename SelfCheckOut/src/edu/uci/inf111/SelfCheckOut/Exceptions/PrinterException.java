/*
 * 
 * Creator: Rosalva Gallardo-Valencia
 * Course: Inf111, Winter 2008
 * 
 * Created on Feb 14, 2008
 * 
 * Copyright, 2008 University of California. 
 */
package edu.uci.inf111.SelfCheckOut.Exceptions;
/**
 * An exception which is thrown when the Printer device fails
 * because the file path is not valid, the data to print is null or contain nulls, or
 * there was an error writing on the output file.
 */
public class PrinterException extends Exception {

	private static final long serialVersionUID = 1L;
	public PrinterException() {
		super();
	}

	public PrinterException(String message) {
		super(message);
	}

	public PrinterException(String message, Throwable cause) {
		super(message, cause);
	}

	public PrinterException(Throwable cause) {
		super(cause);
	}
}
