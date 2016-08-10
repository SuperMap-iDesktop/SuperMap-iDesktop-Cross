package com.supermap.desktop.netservices.iserver;

import java.util.EventListener;

/**
 * 功能进度 Listener
 * 
 * @author highsad
 *
 */
public interface FunctionProgressListener extends EventListener {
	public void functionProgress(FunctionProgressEvent event);
}
