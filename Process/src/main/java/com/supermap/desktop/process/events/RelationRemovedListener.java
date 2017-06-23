package com.supermap.desktop.process.events;

import java.util.EventListener;

/**
 * Created by highsad on 2017/6/21.
 */
public interface RelationRemovedListener<T> extends EventListener {
	void relationRemoved(RelationRemovedEvent<T> e);
}
