package com.supermap.desktop.mapview.map.propertycontrols;

public class MapNullException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public MapNullException() {
		super("Map cannot be null.");
	}

	/**
	 * Constructor that allows a specific error message to be specified.
	 *
	 * @param message
	 *            the detail message.
	 */
	public MapNullException(String message) {
		super(message);
	}
}
