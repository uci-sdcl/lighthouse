/*
 * Creator: Susan Elliott Sim
 * Course: Inf111, Winter 2008
 * 
 * Created on May 10, 2006
 * Updated on January 17, 2008
 * 
 * Copyright, 2006, 2008 University of California. 
 * 
 * The ProductDB class maintains the items in the database. The items are stored in a hash table.
 */

package edu.uci.inf111.SelfCheckOut.App;

import java.util.Hashtable;

/**
 * The ProductDB class encapsulates the list of all products sold in the store. 
 * In a real system, this would likely be a wrapper around a database of products
 * which would be managed elsewhere.  In our sample system, we have a method which 
 * can provide a sample DB, and the capability to add items to the DB using an
 * addItem() method.
 *
 */
public class ProductDB {
	/**
	 * This Hashtable is the core of our sample DB.  In a real implementation, the
	 * actual data would likely be in a separate database, which we would access
	 * using database queries.
	 */
	private Hashtable<String, ProductInfo> productsHT;

	/**
	 * Constructs an empty database.
	 */
	public ProductDB() {
		productsHT = new Hashtable<String, ProductInfo>();
	}

	/**
	 * This test method constructs a sample database which is useful for
	 * testing purposes.  Products may also be added individually using the
	 * addItem() method.
	 * @throws Exception
	 */
	public void initializeTestDB() throws Exception {
		/* 
		 * This try block is used to capture the various code- and item-creation exceptions
		 * which are thrown by UPC(), BIC(), etc.
		 */ 
		try {
			// Hardcoding the items in the database.
			
			// For details on how to create legal UPCs, see the UPC class.
			UPC upc1 = new UPC("786936224306");
			UPC upc2 = new UPC("717951000842");
			UPC upc3 = new UPC("024543213710");
			UPC upc4 = new UPC("085392132225");
			UPC upc5 = new UPC("013803086706");
			
			
			// Any 5-digit String is a legal BIC.
			BIC bic1 = new BIC("11111");
			BIC bic2 = new BIC("22222");
			BIC bic3 = new BIC("33333");
			BIC bic4 = new BIC("44444");
			BIC bic5 = new BIC("55555");
			

			// Packaged Products consist of a description, UPC, price and weight.
			PackagedProduct pp1 = new PackagedProduct("Kellogg Cereal", upc1,
					3.52, 1.35);
			PackagedProduct pp2 = new PackagedProduct("Coca Cola (12 pack)",
					upc2, 3.20, 4);
			PackagedProduct pp3 = new PackagedProduct("Ice Cream", upc3, 4.00,
					2.2);
			PackagedProduct pp4 = new PackagedProduct("Oreo Cookies", upc4,
					3.50, 0.8);
			PackagedProduct pp5 = new PackagedProduct("Artichoke Hrts", upc5,
					2.79, 0.75);

			
			// Bulk Products consist of a description, BIC, and unit price.
			BulkProduct bp1 = new BulkProduct("Banana", bic1, 0.69);
			BulkProduct bp2 = new BulkProduct("Oranges", bic2, 1.50);
			BulkProduct bp3 = new BulkProduct("Spinach", bic3, 0.99);
			BulkProduct bp4 = new BulkProduct("Fuji Apples", bic4, 1.69);
			BulkProduct bp5 = new BulkProduct("Kiwi", bic5, 1.29);


			// adding items to hash table
			addItem(pp1);
			addItem(pp2);
			addItem(pp3);
			addItem(pp4);
			addItem(pp5);

			
			addItem(bp1);
			addItem(bp2);
			addItem(bp3);
			addItem(bp4);
			addItem(bp5);

		} catch (Exception e) {
			throw (e);
		}
	}

	/**
	 * This method returns a copy of the ProductDB Hashtable.  If we
	 * provided the original, external code could modify the DB directly.
	 */
	public Hashtable<String, ProductInfo> listAll() {
		// make a copy of productsHT before returning
		Hashtable<String, ProductInfo> copyHT = new Hashtable<String, ProductInfo>(
				productsHT);
		return copyHT;
	}

	/**
	 * This method looks up a product in the database.  
	 * @param code	The UIC or BIC of the product.
	 * @return	The ProductInfo of the corresponding product, or null if no such product.
	 */
	public ProductInfo lookUpItem(Code code) {
		return productsHT.get(code.getCode());
	}

	/**
	 * This method is called to add items directly to the database in our example.  In a 
	 * real implementation, this would likely be done directly to the product database
	 * using a separate piece of software.
	 * @param item	The product to be added.
	 */
	public void addItem(ProductInfo item) {
		productsHT.put(item.getCode().getCode(), item);
	}
}
