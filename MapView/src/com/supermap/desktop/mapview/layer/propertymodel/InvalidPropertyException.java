package com.supermap.desktop.mapview.layer.propertymodel;

public class InvalidPropertyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5827729003171668412L;

	/**
	 * Default constructor.
	 */
	public InvalidPropertyException() {
		super("Property is Error.");
	}

	/**
	 * Constructor that allows a specific error message to be specified.
	 *
	 * @param message
	 *            the detail message.
	 */
	public InvalidPropertyException(String message) {
		super(message);
	}
}
