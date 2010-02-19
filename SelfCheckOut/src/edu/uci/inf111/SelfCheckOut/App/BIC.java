/*
 * Creator: Susan Elliott Sim
 * Course: Inf111, Winter 2008
 * 
 * Created on May 10, 2006
 * Updated on January 17, 2008
 * 
 * Copyright, 2006, 2008 University of California. 
 * 
 * The BIC class maintains the BIC code. It verifies the BIC code upon creation of an object.
 */

package edu.uci.inf111.SelfCheckOut.App;

import edu.uci.inf111.SelfCheckOut.Exceptions.InvalidBICException;

/**
 * The BIC class represents a Bulk Item Code.  This is an identifying code
 * (implementing the Code interface) which would be affixed to a Bulk 
 * grocery item, such as produce or meat.  These items are sold by 
 * weight, and generally do not carry a UPC.  The BIC is a wrapper for
 * a 5-digit String which holds the actual numeric code.
 *
 */
public class BIC implements Code {

	/**
	 * Contains the 5-digit numeric code which this object represents.
	 */
	private String myBulkItemCode;

	/**
	 * This constructor checks that the code String is a valid code, and
	 * then stores it.  In the case that the code string is invalid, an
	 * InvalidBICException is thrown.
	 * @param bulkItemCode	A String containing a 5-digit bulk item code.
	 * @throws InvalidBICException
	 */
	public BIC(String bulkItemCode) throws InvalidBICException {
		if(bulkItemCode == null)
		{
			/* 
			 * If we don't catch a null here, the following checkLength()
			 * call might throw a NullPointerException.  Run-time
			 * exceptions are generally quite serious bugs.
			 * By throwing a custom exception, we know that the caller
			 * can handle any problems here.   
			 */ 
			throw new InvalidBICException("BIC must not be null");
		}
		if (checkLength(bulkItemCode) == true) {
			myBulkItemCode = bulkItemCode;
		} else {
			/* 
			 * We also throw an exception for illegal string lengths.
			 * The message is largely for debugging purposes.
			 */
			throw (new InvalidBICException("BIC length must be 5"));
		}
	}

	/* (non-Javadoc)
	 * @see edu.uci.ics121.SelfCheckOut.App.Code#getCode()
	 */
	public String getCode() {
		return myBulkItemCode;
	}

	/* (non-Javadoc)
	 * @see edu.uci.ics121.SelfCheckOut.App.Code#equals(edu.uci.ics121.SelfCheckOut.App.Code)
	 */
	public boolean equals(Code comparedCode) {
		return (myBulkItemCode == comparedCode.getCode());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		/* 
		 * Return the corresponding integer value of the last character of the
		 * bulk item code.  This is not actually being used as a hash in this case.  
		 */ 
		return (myBulkItemCode.charAt(myBulkItemCode.length() - 1)) - 48;
	}

	/**
	 * Checks that the length of the provided string is exactly 5.
	 * @param code
	 * @return <code>true</code> if the string length is 5; <code>false</code> otherwise.
	 */
	private boolean checkLength(String code) {
		if (code.length() == 5) {
			return true;
		} else {
			return false;
		}
	}
}
