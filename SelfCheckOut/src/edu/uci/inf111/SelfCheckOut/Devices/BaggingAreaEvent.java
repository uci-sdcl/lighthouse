/*
 * 
 * Creator: Susan Elliott Sim
 * Course: Inf111, Winter 2008
 * 
 * Created on May 10, 2006
 * Updated on January 17, 2008
 * 
 * Copyright, 2006, 2008 University of California. 
 */

 package edu.uci.inf111.SelfCheckOut.Devices;

/**
 * The BaggingAreaEvent class serves to encapsulate the information associated
 * with a change in the state of the BaggingArea, so that it can easily be passed 
 * to the BaggingAreaListeners.
 *
 */
public class BaggingAreaEvent {
	/**
	 * The total weight in the BaggingArea at the time of the event.
	 */
	private double totalWeight;

	/**
	 * The weight change in the BaggingArea since the last event.
	 * This is useful since we might compare it to a known product weight.
	 */
	private double weightChange;

	/**
	 * Create a BaggingAreaEvent.  The object does not change after construction.
	 * @param totalWeight	The total weight of the bagging area
	 * @param weightChange	The weight change since the last event was sent.
	 */
	public BaggingAreaEvent(double totalWeight, double weightChange) {
		this.totalWeight = totalWeight;
		this.weightChange = weightChange;
	}
	
	/**
	 * Returns the total weight in the bagging area when the event occured. 
	 */
	public double getWeight() {
		return totalWeight;		
	}
	
	/**
	 * Returns the weight change in the bagging area since the last event occured. 
	 */
	public double getChange() {
		return weightChange;
	}

}
