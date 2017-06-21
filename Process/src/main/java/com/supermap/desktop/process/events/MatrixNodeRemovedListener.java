package com.supermap.desktop.process.events;

import java.util.EventListener;

/**
 * Created by highsad on 2017/6/21.
 */
public interface MatrixNodeRemovedListener<T extends Object> extends EventListener {
	void matrixNodeRemoved(MatrixNodeRemovedEvent<T> e);
}
