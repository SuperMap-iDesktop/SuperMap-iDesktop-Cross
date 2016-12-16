package com.supermap.desktop.ui.mdi.exception;

/**
 * Created by highsad on 2016/9/23.
 */
public class MdiActionModeException extends RuntimeException {
	public MdiActionModeException(String mode) {
		super("action.getMode() can not be " + mode);
	}
}
