/*
 * Creator: Susan Elliott Sim
 * Course: Inf111, Winter 2008
 * 
 * Created on May 10, 2006
 * Updated on January 17, 2008
 * 
 * Copyright, 2006, 2008 University of California. 
 * 
 * The BulkProduct class is for products with a BIC code. It implements the ProductInfo interface.
 */

package edu.uci.inf111.SelfCheckOut.App;

/**
 * The BulkProduct class represents a grocery product which is sold by
 * weight, such as produce, deli, meat, etc.  The details of the product
 * are stored at the time of construction, and can be retrieved using
 * accessor functions.
 *
 */
public class BulkProduct implements ProductInfo {
	/**
	 * The BIC representing the product's 5-digit bulk item code.
	 */
	private BIC myBIC;

	/**
	 * The price per unit weight of the product
	 */
	private double myUnitPrice;

	/**
	 * The String description of the product.
	 */
	private String myDescription;
	
	/**
	 * @param description 	The text description of the product.
	 * @param BICcode 		A BIC representing the product's 5-digit bulk item code.
	 * @param price 		The unit price of the product.
	 */
	public BulkProduct(String description, BIC BICcode, double price) {
		myDescription = description;
		myBIC = BICcode;
		myUnitPrice = price;
	}

	/**
	 * Accessor function returning the product's BIC
	 */
	public BIC getBIC() {
		return myBIC;
	}
	
	/* (non-Javadoc)
	 * @see edu.uci.ics121.SelfCheckOut.App.ProductInfo#getCode()
	 */
	public Code getCode() {
		return getBIC();
	}

	/* (non-Javadoc)
	 * @see edu.uci.ics121.SelfCheckOut.App.ProductInfo#getPrice()
	 */
	public double getPrice() {
		return myUnitPrice;
	}

	/* (non-Javadoc)
	 * @see edu.uci.ics121.SelfCheckOut.App.ProductInfo#getDescription()
	 */
	public String getDescription() {
		return myDescription;
	}

}
