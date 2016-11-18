package com.supermap.desktop.ui.mdi.exception;

/**
 * Created by highsad on 2016/9/26.
 */
public class UnknownPageException extends RuntimeException {
	public UnknownPageException(String page) {
		super("unknown page " + page + " .");
	}
}
