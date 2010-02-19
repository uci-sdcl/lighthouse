/*
 * 
 * Creator: Susan Elliott Sim
 * Course: Inf111, Winter 2008
 * 
 * Created on May 10, 2006
 * Updated on January 17, 2008
 * 
 * Copyright, 2006, 2008 University of California. 
 * 
 * The GroceryItem class.
 * Each item in the user's CheckOutCart is a GroceryItem object which contains product information,
 * the price (total for this item), and its weight.
 */

package edu.uci.inf111.SelfCheckOut.App;

/**
 * A GroceryItem object represents a specific item which has been scanned by
 * the customer.  A separate weight and cost are recorded when the object is
 * created, since BulkProducts only include the <i>unit</i> price, and we
 * require the item's actual weight and cost in order to compute the bill.
 *
 */
public class GroceryItem {
	/**
	 * The actual price of the item.
	 */
	private double price;

	/**
	 * The actual weight of the item.
	 */
	private double weight;

	/**
	 * The ProductInfo for the item, either a PackagedProduct or BulkProduct.
	 */
	private ProductInfo info;

	/**
	 * This constructor records the information about this item, which can be retrieved using accessor methods.
	 * @param productInfo	The information (description, code) of the item being scanned.
	 * @param productCost	The cost of the item.  For PackagedProducts, this is simply the cost, but for BulkProducts this is computed based on the item's actual weight.
	 * @param productWeight	The weight of the item.  For PackagedProducts, this is simply the listed weight, but for BulkProducts this represents the amount of product purchased.
	 */
	public GroceryItem(ProductInfo productInfo, double productCost,
			double productWeight) {
		info = productInfo;
		price = productCost;
		weight = productWeight;
	}

	/**
	 * An accessor method returning the actual price of the item.
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * An accessor method returning the actual weight of the item.	 
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * An accessor method returning the ProductInfo of the item.
	 */
	public ProductInfo getInfo() {
		return info;
	}
}
