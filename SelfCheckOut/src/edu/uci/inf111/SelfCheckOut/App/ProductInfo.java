/*
 * Creator: Susan Elliott Sim
 * Course: Inf111, Winter 2008
 * 
 * Created on May 10, 2006
 * Updated on January 17, 2008
 * 
 * Copyright, 2006, 2008 University of California. 
 * 
 * The ProductInfo interface is implemented by BulkProduct and PackagedProduct.
 */

package edu.uci.inf111.SelfCheckOut.App;

/**
 * The ProductInfo interface is implemented by any class which represents a saleable product
 * in our store.  In out example, these will be the BulkProduct and PackagedProduct.  These 
 * products differ in the manner in which their prices are calculated, but have in common a
 * descriptor, price, and identifying code.  The interface provides common accessor methods
 * for these fields.
 *
 */
public interface ProductInfo {
	/**
	 * Accessor method for product description
	 */
	public String getDescription();

	/**
	 * Accessor method for unit price
	 */
	public double getPrice();
	
	/**
	 * Accessor method for identifying code, either BIC or UPC.  (Both are of type Code)
	 */
	public Code getCode();
	
}
