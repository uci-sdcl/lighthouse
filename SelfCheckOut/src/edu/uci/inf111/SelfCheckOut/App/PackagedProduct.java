/*
 * Creator: Susan Elliott Sim
 * Course: Inf111, Winter 2008
 * 
 * Created on May 10, 2006
 * Updated on January 17, 2008
 * 
 * Copyright, 2006, 2008 University of California. 
 * 
 * The PackagedProduct class is for products with a UPC code. It implements the ProductInfo interface.
 */

package edu.uci.inf111.SelfCheckOut.App;

/**
 * A PackedProduct represents a single UPC-code-bearing product in the store.
 * Packaged products are sold as discrete single units, and never by weight.
 * Note the difference between 'items' and 'products':  A 'product' is a type
 * of good sold at the store, whereas an 'item' is a particular box of that
 * product.  
 *
 */
public class PackagedProduct implements ProductInfo {
	/**
	 * The UPC for this product. 
	 */
	private UPC myUPC;

	/**
	 * The price for a box of the product.
	 */
	private double myPrice;

	/**
	 * The estimated weight for a box of the product.
	 */
	private double myWeight;

	/**
	 * A text description of the product.
	 */
	private String myDescription;

	/**
	 * This constructor stores all relevant details of the product, which can
	 * be retrieved using accessor methods.
	 * @param descrip		A text description of the product.
	 * @param UPCcode		A unique 12-digit UPC code for the product.
	 * @param productCost	The cost of the product.
	 * @param productWeight	The estimated weight of the product.
	 */
	public PackagedProduct(String descrip, UPC UPCcode, double productCost,
			double productWeight) {
		myDescription = descrip;
		myUPC = UPCcode;
		myPrice = productCost;
		myWeight = productWeight;
	}

	/**
	 * An accessor method which returns the unique UPC of the product.
	 */
	public UPC getUPC() {
		return myUPC;
	}
	
	/**
	 * An accessor method which returns the unique Code (UPC) of the product.
	 */
	public Code getCode() {
		return getUPC();
	}

	/**
	 * An accessor method which returns the price of the product.
	 */
	public double getPrice() {
		return myPrice;
	}

	/**
	 * An accessor method which returns the weight of the product.
	 */
	public double getWeight() {
		return myWeight;
	}

	/**
	 * An accessor method which returns the text description of the product.
	 */
	public String getDescription() {
		return myDescription;
	}

}
