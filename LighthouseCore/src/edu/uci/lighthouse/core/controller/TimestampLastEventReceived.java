package edu.uci.lighthouse.core.controller;

import java.util.Date;

public class TimestampLastEventReceived {

	private static TimestampLastEventReceived instance = null;
	private Date value = null;

	private TimestampLastEventReceived() {
		// Exists only to defeat instantiation.
	}

	public static TimestampLastEventReceived getInstance() {
		if (instance == null) {
			instance = new TimestampLastEventReceived();
		}
		return instance;
	}

	public void setValue(Date value) {
		this.value = value;
	}

	public Date getValue() {
		return value;
	}

}
