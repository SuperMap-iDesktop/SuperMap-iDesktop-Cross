package com.supermap.desktop.exception;

import com.supermap.desktop.Application;
import com.supermap.desktop.utilties.LogUtilities;

/**
 * Created by Administrator on 2016/3/22.
 */
public class SmUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		LogUtilities.debug("", e);
		Application.getActiveApplication().getOutput().output(e);
	}
}
