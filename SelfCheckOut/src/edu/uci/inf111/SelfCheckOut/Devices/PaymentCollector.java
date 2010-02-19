/*
 * Creator: Susan Elliott Sim
 * Course: Inf111, Winter 2008
 * 
 * Created on May 10, 2006
 * Updated on January 17, 2008
 * 
 * Copyright, 2006, 2008 University of California. 
 * 
 * The PaymentCollector class handles the payment process. This is not a concern for this assignment.
 * It is created to make the system more "complete," but it lacks real functionalities.
 */

package edu.uci.inf111.SelfCheckOut.Devices;

/**
 * This class represents the payment-collecting portion of the system: the 
 * credit-card pad or cash-input part of the system.  We are not concerned with 
 * this part of the system, so a simple stub method is provided.
 *
 */
public class PaymentCollector {
	public PaymentCollector() {
	}

	/**
	 * This method is called by SelfCheckOut when the customer is finished scanning and
	 * wishes to pay.  It is a stub method which returns <code>true</code>, indicating successful payment.  
	 * @param amount	The amount of payment requested.
	 * @return <code>true</code> indicating payment accepted.
	 */
	public boolean collect(double amount) {
		// For this project, we are not concerned with this part of the system.
		// We will just assume
		// that whenever this function is called, this function will always
		// return true to the
		// calling program to indicate that the payment has been accepted.

		return true;
	}
}
