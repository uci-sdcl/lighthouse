/*
 * Creator: Susan Elliott Sim
 * Course: Inf111, Winter 2008
 * 
 * Created on May 10, 2006
 * Updated on January 17, 2008
 * 
 * Copyright, 2006, 2008 University of California. 
 * 
 * The BaggingArea class can simulate the action of adding an item to the bagging area, and detect
 * a change in weight.
 */

package edu.uci.inf111.SelfCheckOut.Devices;

import java.util.Vector;

/**
 * The BaggingArea class represents a wrapper for a hardware driver 
 * of the sensors in a bagging area.  Because we're not really using 
 * such a device, our class provides methods to change the weight 
 * reported by the bagging area, and to zero that weight if we're 
 * resetting the system.<br>
 * <br>
 * The BaggingArea uses an <i>Observer</i> (or <i>Listener</i>) design
 * pattern.  Objects which implement the <code>BaggingAreaListener</code>
 * interface may use the <code>attach()</code> method to register with 
 * the BaggingArea.  When a weight change event occurs, the BaggingArea
 * reports the event to its observers using their notify methods.
 * In this example, we do not support detaching from the BaggingArea.<br>
 * <br> 
 * In the business logic of our system (implemented in SelfCheckOut) we 
 * use known product weights to ascertain if the customer has placed an 
 * item in the bagging area after scanning it.  Until we see a (correct)
 * weight change, we would keep telling them to bag the item, and we 
 * would refuse to accept scans of additional items.  
 *
 */
public class BaggingArea {
	/**
	 * totalWeight records the current weight being sensed in the BaggingArea.
	 */
	private double totalWeight;

	/**
	 * observers is a Vector of BaggingAreaListeners which will be notified 
	 * of weight change events.
	 */
	private Vector<BaggingAreaListener> observers;

	/**
	 * This simple constructor initializes a zero weight and an empty set of observers.
	 */
	public BaggingArea() {
		observers = new Vector<BaggingAreaListener>();
		totalWeight = 0;
	}

	/**
	 * Returns the current weight being registered by the bagging area 'sensor'.
	 */
	public double getTotalWeight() {
		return totalWeight;
	}

	/**
	 * changeWeight() is the method we use to simulate the bagging area scales detecting
	 * a change in weight.  This is a gross simplification, but will suffice for our system.
	 * A small time delay is included to represent the time taken by the action.
	 * Note that a negative weight corresponds to the customer removing an item from the
	 * bagging area. 
	 * @param weight The amount of weight change in this event.  
	 */
	public void changeWeight(double weight) {
		totalWeight = totalWeight + weight;
		BaggingAreaEvent baEvent = new BaggingAreaEvent(totalWeight, weight);
		
		notifyObserver(baEvent); // notify the observer that the weight has changed
	}
	
	/**
	 * The zeroWeight() method zeroes the scales of the BaggingArea, simulating the 
	 * customer leaving with their purchases.
	 */
	public void zeroWeight() {
		changeWeight(-totalWeight);
	}

	/**
	 * notifyObserver() passes the information of the weight change event along to the
	 * observer objects as a BaggingAreaEvent, which includes the current weight in the
	 * the bagging area as well as the most recent change.  The information is passed
	 * by calling the notifyBaggingAreaEvent() method on the Listener.
	 * @param event An event containing the current weight in the area and the most recent change.
	 */
	private void notifyObserver(BaggingAreaEvent event) {
		for (int index = 0; index < observers.size(); index++) {
			observers.get(index).notifyBaggingAreaEvent(this, event);
		}
	}
	
	/**
	 * The attach() method registers a BaggingreaListener to receive all future BaggingAreaEvents.
	 * @param bal The Listener (often the calling object) to be attached.
	 */
	public void attach(BaggingAreaListener bal) {
		if (bal != null) {
			observers.add(bal);
		}
	}
}
