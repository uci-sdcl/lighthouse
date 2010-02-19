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
 * The BaggingAreaListener interface must be implemented by any class which 
 * wishes to attach() to the BaggingArea to receive BaggingAreaEvents.
 *
 */
public interface BaggingAreaListener {
	/**
	 * This method is called on the Listener whenever a bagging event (typically a weight change) occurs.
	 * @param be	The BaggingArea which is sending the event (in case the Listener is registered to more than one Area)
	 * @param event	The BaggingAreaEvent, which encapsulates information about the state change in the Area which prompted the event.
	 */
	public void notifyBaggingAreaEvent(BaggingArea be, BaggingAreaEvent event);
}
