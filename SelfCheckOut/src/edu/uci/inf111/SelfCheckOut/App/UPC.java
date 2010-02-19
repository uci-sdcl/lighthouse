/*
 * Creator: Susan Elliott Sim
 * Course: Inf111, Winter 2008
 * 
 * Created on May 10, 2006
 * Updated on January 17, 2008
 * 
 * Copyright, 2006, 2008 University of California. 
 * 
 * The UPC class maintains the UPC code. It verifies the UPC code upon creation of an object.
 */
package edu.uci.inf111.SelfCheckOut.App;

import edu.uci.inf111.SelfCheckOut.Exceptions.InvalidUPCException;

/**
 * The UPC class represents a Universal Product Code, a unique 12-digit code found on most packaged products.  
 * UPCs are typically machine-read, and so contain an internal checksum to reduce errors in correctly reading them.
 * @see Code
 * @see BIC
 *
 */
public class UPC implements Code {

	/**
	 * The String representation of the 12-digit code which this object represents. 
	 */
	private String myUniversalProductCode;

	/**
	 * Creates a UPC object unless the supplied digit string is illegal.
	 * @param universalProductCode A String of digits corresponding to the UPC code. 
	 * @throws InvalidUPCException Thrown if the provided String is null, too short, or fails the checksum.
	 */
	public UPC(String universalProductCode) throws InvalidUPCException {
		try {
			checkSum(universalProductCode);
			myUniversalProductCode = universalProductCode;
		} catch (InvalidUPCException iUPCe) {
			throw (iUPCe);
		}
	}

	/* (non-Javadoc)
	 * @see edu.uci.ics121.SelfCheckOut.App.Code#getCode()
	 */
	public String getCode() {
		return myUniversalProductCode;
	}

	/* (non-Javadoc)
	 * @see edu.uci.ics121.SelfCheckOut.App.Code#equals(edu.uci.ics121.SelfCheckOut.App.Code)
	 */
	public boolean equals(Code comparedCode) {
		return myUniversalProductCode == comparedCode.getCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		// return the corresponding integer value of the last character of the
		// UPC
		return (myUniversalProductCode
				.charAt(myUniversalProductCode.length() - 1)) - 48;
	}

	/**
	 * This function checks whether the scanned UPC is a valid one 
	 * (not in the sense whether it exists in the database, but 
	 * whether the UPC that is passed in is of correct formatCalculating the Checksum.<br>
	 * The checksum is a Modulo 10 calculation.<br>
	 * 1. Add the values of the digits in positions 1, 3, 5, 7, 9, and 11.<br>
	 * 2. Multiply this result by 3.<br>
	 * 3. Add the values of the digits in positions 2, 4, 6, 8, and 10.<br>
	 * 4. Sum the results of steps 2 and 3.<br>
	 * 5. The check character is the smallest number which, when added to the
	 * result in step 4, produces a multiple of 10.<br>
	 * Example: Assume the barcode data = 01234567890<br>
	 * 1. 0 + 2 + 4 + 6 + 8 + 0 = 20<br>
	 * 2. 20 x 3 = 60<br>
	 * 3. 1 + 3 + 5 + 7 + 9 = 25<br>
	 * 4. 60 + 25 = 85<br>
	 * 5. 85 + X = 90 (next highest multiple of 10), therefore X = 5 (checksum)<br>
	 * @param code The String to be checked
	 * @throws InvalidUPCException Thrown if the supplied code is of incorrect length or has an incorrect checksum.
	 */
	private void checkSum(String code) throws InvalidUPCException {
		char[] charsOfUPC;
		int[] digitsOfUPC = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		int oddSum = 0; // sum of numbers at odd positions of the UPC
		int evenSum = 0; // sum of numbers at even positions of the UPC

		if (code.length() != 12) {
			throw (new InvalidUPCException("UPC length must be 12 digits"));
		} else {
			charsOfUPC = code.toCharArray(); // convert UPC String to chars

			// change to integers
			for (int i = 0; i < 12; i++) {
				digitsOfUPC[i] = charsOfUPC[i] - 48;
			}

			// apply the checksum algorithm
			oddSum = digitsOfUPC[0] + digitsOfUPC[2] + digitsOfUPC[4]
					+ digitsOfUPC[6] + digitsOfUPC[8] + digitsOfUPC[10];
			evenSum = digitsOfUPC[1] + digitsOfUPC[3] + digitsOfUPC[5]
					+ digitsOfUPC[7] + digitsOfUPC[9];
			if ((oddSum * 3 + evenSum + digitsOfUPC[11]) % 10 != 0) {
				throw (new InvalidUPCException("UPC checksum error"));
			}
		}
	}
}
