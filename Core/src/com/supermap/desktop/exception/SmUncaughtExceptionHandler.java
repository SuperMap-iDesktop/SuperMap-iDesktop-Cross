package com.supermap.desktop.exception;

import com.supermap.desktop.Application;

/**
 * Created by Administrator on 2016/3/22.
 */
public class SmUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		Application.getActiveApplication().getOutput().output(new Exception(e));
	}
}
