package com.supermap.desktop.ui.mdi.exception;

/**
 * Created by highsad on 2016/9/23.
 */
public class NullParameterException extends RuntimeException {
	public NullParameterException(String parameter) {
		super("Parameter " + " can not be null.");
	}
}
