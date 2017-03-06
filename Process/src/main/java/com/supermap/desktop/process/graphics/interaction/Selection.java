package com.supermap.desktop.process.graphics.interaction;

import com.supermap.desktop.process.events.GraphSelectChangedListener;
import com.supermap.desktop.process.events.GraphSelectedChangedEvent;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.handler.canvas.CanvasEventAdapter;

import javax.swing.event.EventListenerList;

/**
 * Created by highsad on 2017/3/2.
 */
public abstract class Selection extends CanvasEventAdapter {
	private GraphCanvas canvas;
	private EventListenerList list = new EventListenerList();

	public Selection(GraphCanvas canvas) {
		this.canvas = canvas;
	}

	public void addGraphSelectChangedListener(GraphSelectChangedListener listener) {
		this.list.add(GraphSelectChangedListener.class, listener);
	}

	public void removeGraphSelectChangedListener(GraphSelectChangedListener listener) {
		this.list.remove(GraphSelectChangedListener.class, listener);
	}

	public void fireGraphSelectChanged(GraphSelectedChangedEvent e) {

	}
}
