package com.supermap.desktop.process.core;

/**
 * Created by highsad on 2017/6/20.
 */
public interface IConnection {
	IProcess formProcess();

	IProcess toProcess();

	void disconnect();

	String getMessage();
}
