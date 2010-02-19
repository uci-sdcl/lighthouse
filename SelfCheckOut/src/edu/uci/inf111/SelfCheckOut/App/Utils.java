package edu.uci.inf111.SelfCheckOut.App;

import java.util.Enumeration;
import java.util.Hashtable;

public class Utils {

	/**
	 * A utility method which prints the contents of the DB to System.out
	 * @param itemsInDB  The DB Hashtable, which should be accessed using db.listAll()
	 */
	public static void printDB(Hashtable<String, ProductInfo> itemsInDB) {
		Enumeration<ProductInfo> items = itemsInDB.elements();
		ProductInfo retrievedItem;
		PackagedProduct tempPP;
		BulkProduct tempBP;
		System.out.println("There are " + itemsInDB.size()
				+ " items in the database.");
		System.out
				.println("*** Below is a list of products currently in the database.***");
		System.out
				.println("-------------------------------------------------------");

		while (items.hasMoreElements()) {
			retrievedItem = items.nextElement();

			if (retrievedItem instanceof PackagedProduct) // if it is a
															// PackagedProduct
			{
				tempPP = (PackagedProduct) retrievedItem;
				System.out.println("Product description: "
						+ tempPP.getDescription());
				System.out.println("UPC: " + tempPP.getUPC().getCode());
				System.out.println("Cost: " + tempPP.getPrice());
				System.out.println("Weight: " + tempPP.getWeight());
			} else if (retrievedItem instanceof BulkProduct) // if it is a
																// BulkProduct
			{
				tempBP = (BulkProduct) retrievedItem;
				System.out.println("Product description: "
						+ tempBP.getDescription());
				System.out.println("BIC: " + tempBP.getBIC().getCode());
				System.out.println("Unit Cost: " + tempBP.getPrice());
			} else {
				// This should never execute
				System.out.println("ERROR: Product type unknown.");
			}
		}

		System.out
				.println("-------------------------------------------------------");
	}

	/**
	 * A utility method used to print out the cart contents.
	 * @param cc The CheckOutCart to print.
	 */
	public static void printCart(CheckOutCart cc) {
		Enumeration<GroceryItem> itemsInCart = cc.listItems();
		Object retrievedItem;
		GroceryItem tempGI;
		PackagedProduct tempPP;
		BulkProduct tempBP;

		System.out
				.println("*** Below is a list of products you have added: ***");
		System.out
				.println("-------------------------------------------------------");

		while (itemsInCart.hasMoreElements()) {
			tempGI = (GroceryItem) itemsInCart.nextElement();
			retrievedItem = tempGI.getInfo(); // returns the ProductInfo
												// object in GroceryItem

			if (retrievedItem instanceof PackagedProduct) // if it is a
															// PackagedProduct
			{
				tempPP = (PackagedProduct) retrievedItem;
				System.out.println("Product description: "
						+ tempPP.getDescription());
				System.out.println("UPC: " + tempPP.getUPC().getCode());
				System.out.printf("Cost: $%.2f\n", tempPP.getPrice());
				System.out.println("Weight: " + tempPP.getWeight());
			} else if (retrievedItem instanceof BulkProduct) // if it is a
																// BulkProduct
			{
				tempBP = (BulkProduct) retrievedItem;
				System.out.println("Product description: "
						+ tempBP.getDescription());
				System.out.println("BIC: " + tempBP.getBIC().getCode());
				System.out.printf("Unit Cost: %.2f\n", tempBP.getPrice());
				System.out.println("Weight: " + tempGI.getWeight());
				System.out.printf("Total Cost: $%.2f\n", tempGI.getPrice());
			} else {
				// This should never execute
				System.out.println("ERROR: Product type unknown.");
			}
			System.out.println();
		}

		System.out.printf("---- TOTAL: $%.2f\n", cc.getTotalCost());
		System.out
				.println("-------------------------------------------------------");
	}

}
