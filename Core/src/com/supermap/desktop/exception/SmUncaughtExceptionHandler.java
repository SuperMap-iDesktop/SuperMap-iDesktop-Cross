package com.supermap.desktop.exception;

import com.supermap.desktop.Application;
import com.supermap.desktop.utilties.LogUtilties;

/**
 * Created by Administrator on 2016/3/22.
 */
public class SmUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		LogUtilties.debug("", e);
		Application.getActiveApplication().getOutput().output(e);
	}
}
