package com.supermap.desktop.exception;

public class InvalidScaleException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5827729003171668412L;

	/**
	 * Default constructor.
	 */
	public InvalidScaleException() {
		super();
	}

	/**
	 * Constructor that allows a specific error message to be specified.
	 *
	 * @param message
	 *            the detail message.
	 */
	public InvalidScaleException(String message) {
		super(message);
	}
}
