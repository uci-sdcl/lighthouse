/*
 * Creator: Susan Elliott Sim
 * Course: Inf111, Winter 2008
 * 
 * Created on May 10, 2006
 * Updated on January 17, 2008
 * 
 * Copyright, 2006, 2008 University of California. 
 * 
 * The SelfCheckOut class contains functions that can be called by the real user interface
 * of the self checkout systems.
 */

package edu.uci.inf111.SelfCheckOut.App;

import java.util.*;

import edu.uci.inf111.SelfCheckOut.Devices.BaggingArea;
import edu.uci.inf111.SelfCheckOut.Devices.BaggingAreaEvent;
import edu.uci.inf111.SelfCheckOut.Devices.BaggingAreaListener;
import edu.uci.inf111.SelfCheckOut.Devices.PaymentCollector;
import edu.uci.inf111.SelfCheckOut.Exceptions.AddWhileBaggingException;
import edu.uci.inf111.SelfCheckOut.Exceptions.AddWhilePayingException;
import edu.uci.inf111.SelfCheckOut.Exceptions.IncorrectStateException;
import edu.uci.inf111.SelfCheckOut.Exceptions.InvalidProductException;

/**
 * The SelfCheckOut class contains the business logic of the sales point, and keeps
 * track of the state of the current customer's checkout.  The class contains 
 * methods to handle adding items to the customer's cart, accepting payment,
 * and recieving events from the BaggingArea.
 *
 */
public class SelfCheckOut implements BaggingAreaListener {
	/**
	 * This enumeration represents the four states of the
	 * SelfCheckOut system:<br>
	 * <code>READY</code> means the system is awaiting a new customer<br>
	 * <code>ADDING</code> means the system is prepared for another item to be added<br>
	 * <code>BAGGING</code> means the system is awaiting notification that the item 
	 * has been placed in the bagging area<br>
	 * <code>DONEADDING</code> means the system is waiting for the customer to pay for their items.<br>
	 * Attempts to add items while <code>BAGGING</code> or <code>DONEADDING</code> result in errors.
	 * @author Susan Elliott Sim
	 *
	 */
	public enum checkOutState {
		READY, ADDING, BAGGING, DONEADDING
	}; // different states of the system

	/**
	 * The cart containing items the customer has scanned.
	 */
	private CheckOutCart checkOutCart;

	/**
	 * The associated BaggingArea, which will notify SelfCheckOut when it detects a weight change.
	 */
	public BaggingArea baggingArea;

	/**
	 * An object representing the credit card or cash accepting device.
	 */
	private PaymentCollector paymentCollector;

	/**
	 * The database of products in the store.
	 */
	private ProductDB DB;

	/**
	 * The current state of the system.
	 */
	private checkOutState transactionState;

	/**
	 * The argument-less constructor makes the necessary utility classes and passes them to the 
	 * argumented constructor.
	 * @throws Exception
	 */
	public SelfCheckOut() throws Exception {
		this(new BaggingArea(), new PaymentCollector(), new ProductDB());		
	}
	
	/**
	 * This is the chief constructor.  It records the provided BaggingArea, PaymentCollector and ProductDB,
	 * and attaches itself to the BsggingArea so that it receives notifications of BaggingAreaEvents. 
	 * @param bagging
	 * @param payment
	 * @param db
	 * @throws Exception
	 */
	public SelfCheckOut(BaggingArea bagging, PaymentCollector payment,
			ProductDB db) throws Exception {
		checkOutCart = new CheckOutCart();
		baggingArea = bagging;
		baggingArea.attach(this);
		paymentCollector = payment;
		DB = db;
		transactionState = checkOutState.READY;
	}

	/**
	 * This version of addItem() accepts a UPC code and adds the corresponding 
	 * PackagedProduct to the customer's cart.  It looks the code up in the DB.
	 * 
	 * @param upcCode	The UPC of the scanned item.
	 * @return			The GroceryItem which is also added to the CheckOutCart.
	 * @throws IncorrectStateException	Thrown when addItem() is called during Bagging or once payment is initiated.
	 * @throws InvalidProductException	Thrown when a product corresponding to the UPC is not found.
	 */
	public GroceryItem addItem(UPC upcCode) throws IncorrectStateException,
			InvalidProductException {
		/*
		 *  if weight change is not ok, or transactionState is BAGGING or
		 *  DONEADDING, don't allow customer to add!  
		 */
		if (transactionState == checkOutState.BAGGING) {
			// user should place the previous item in the bagging area first.
			throw new AddWhileBaggingException();			
		} else if (transactionState == checkOutState.DONEADDING) 
		{
			// user has chosen to pay, and cannot add more items
			throw new AddWhilePayingException();
		} else {
			// returns a ProductInfo object			
			ProductInfo info = DB.lookUpItem(upcCode); 
			if (info == null) {
				throw new InvalidProductException();
			} else {
				// create a new GroceryItem object
				GroceryItem newItem = new GroceryItem(info,
						((PackagedProduct) info).getPrice(),
						((PackagedProduct) info).getWeight());
				// add the new GroceryItem object to vector				
				checkOutCart.addItemToCart(newItem); 
				transactionState = checkOutState.BAGGING;
				return newItem;
			}
		}
	}

	// This function will be called to add a BulkProduct object to checkOutCart.
	// It accepts an BIC code
	// object since this is what the user interface will pass. This function
	// will use it to find the product
	// information, create a GroceryItem object to add to the cart.
	/**
	 * This version of addItem() accepts a BIC and weight, and adds the corresponding 
	 * BulkProduct to the customer's cart.  It looks the code up in the DB.
	 * 
	 * @param bicCode	The BIC of the scanned item.
	 * @param weight	The amount of the BulkProduct being purchased.
	 * @return			The GroceryItem which is also added to the CheckOutCart.
	 * @throws IncorrectStateException	Thrown when addItem() is called during Bagging or once payment is initiated.
	 * @throws InvalidProductException	Thrown when a product corresponding to the BIC is not found.
	 */
	public GroceryItem addItem(BIC bicCode, double weight)
			throws IncorrectStateException, InvalidProductException {

		/* if weight change is not ok, or transactionState is BAGGING or
		 * DONEADDING, don't allow customer to add!
		 */
		if (transactionState == checkOutState.BAGGING) {
			// user should place the previous item in the bagging area first.
			throw new AddWhileBaggingException();
		} else if (transactionState == checkOutState.DONEADDING) 
		{
			// user has chosen to pay, and cannot add more items
			throw new AddWhilePayingException();
		} else {
			// returns a ProductInfo object
			ProductInfo info = DB.lookUpItem(bicCode); 
			if (info == null) {
				throw new InvalidProductException();
			} else {
				// create a new GroceryItem object
				GroceryItem newItem = new GroceryItem(info,
						((BulkProduct) info).getPrice() * weight, weight);
				// add the new GroceryItem object to the cart
				checkOutCart.addItemToCart(newItem); 
				transactionState = checkOutState.BAGGING;
				return newItem;
			}
		}
	}

	// Calls relevant function in CheckOutCart to return the Enumeration
	// containing items in the CheckOutcart
	/**
	 * This method retrieves an enumeration of all the items currently in the cart
	 * and returns it.
	 */
	public Enumeration<GroceryItem> listItemsInCart() {
		return checkOutCart.listItems();
	}

	/**
	 * This method returns the current cost total of all items in the cart.
	 */
	public double getTotalCost() {
		return checkOutCart.getTotalCost();
	}

	/**
	 * When the bagging area detects a change in total weight, this function
	 * is called to change the state of the system (transactionState).
	 * Normally we would do a weight check here to ascertain if the predicted and actual
	 * bagging area weights match. Since that functionality is not implemented
	 * in this example, we simply change state to ADDING
	 * @param be	The attached BaggingArea which is sending the event.
	 * @param event	The BaggingAreaEvent, which includes the total weight and most recent change in the bagging area. 
	 */
	public void notifyBaggingAreaEvent(BaggingArea be, BaggingAreaEvent event) {
		transactionState = checkOutState.ADDING;
	}

	/**
	 * Handling payment is not part of this assignment. This function just
	 * returns the cart indicating the payment was received.  It also clears the
	 * shopping cart and resets the system state.  If we implemented this part of the system,
	 * we would throw an exception to indicate a failed transaction rather than returning null.
	 * @return the cart corresponding to the just-completed transaction.
	 */
	public CheckOutCart payForGroceries() {
		transactionState = checkOutState.DONEADDING;
		// check if payment is successful
		if(paymentCollector.collect(checkOutCart.getTotalCost())) {
			// here is where we would record the transaction into our store's inventory
			// and financial records, if we were simulating that part of the system.
			
			// make a copy of the old cart
			CheckOutCart cc = checkOutCart;
			
			// replace the full cart with a new one
			checkOutCart = new CheckOutCart();
			
			// reset our state to waiting for a customer.
			transactionState = checkOutState.READY;
			
			// return the old cart for examination
			return cc;
		}
		return null;
	}
	
	/**
	 * An accessor method which returns the BaggingArea associated with this SelfCheckout.
	 * Useful if the application wants to also receive bagging events, for example.
	 */
	public BaggingArea getBaggingArea() {
		return baggingArea;
	}
	
	/**
	 * An accessor method which returns the ProductDB associated with this SelfCheckOut.
	 * Useful if the application wants to add items to the database or to look up items.
	 */
	public ProductDB getProductDB() {
		return DB;		
	}
	
	/**
	 * An accessor method which returns the PaymentCollector associated with this SelfCheckOut.
	 * Useful if... useful if....  All right, not particularly useful.
	 */
	public PaymentCollector getPaymentCollector() {
		return paymentCollector;
	}

}
