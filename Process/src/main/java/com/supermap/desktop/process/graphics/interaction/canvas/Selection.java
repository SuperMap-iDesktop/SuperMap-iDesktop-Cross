package com.supermap.desktop.process.graphics.interaction.canvas;

import com.supermap.desktop.process.events.GraphSelectChangedListener;
import com.supermap.desktop.process.events.GraphSelectedChangedEvent;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import javax.swing.event.EventListenerList;
import java.awt.*;
import java.util.Vector;

/**
 * Created by highsad on 2017/3/2.
 */
public abstract class Selection extends CanvasActionAdapter {
	public final static Point UNKOWN_POINT = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);

	private GraphCanvas canvas;
	private EventListenerList list = new EventListenerList();
	protected Vector<IGraph> selectedItems = new Vector<>();

	public Selection(GraphCanvas canvas) {
		this.canvas = canvas;
	}

	public GraphCanvas getCanvas() {
		return this.canvas;
	}

	public abstract boolean isSelecting();

	public IGraph[] getSelectedItems() {
		return this.selectedItems.toArray(new IGraph[this.selectedItems.size()]);
	}

	public IGraph getItem(int index) {
		if (this.selectedItems.size() == 0 || index < 0 || index > this.selectedItems.size() - 1) {
			return null;
		}

		return this.selectedItems.get(index);
	}

	public int size() {
		return this.selectedItems.size();
	}

	public void deselectItem(IGraph graph) {
		if (this.selectedItems.contains(graph)) {
			this.selectedItems.remove(graph);
			fireGraphSelectChanged(new GraphSelectedChangedEvent(this.getCanvas(), this));
		}
	}

	public void deselectItems(IGraph[] graphs) {
		if (graphs == null || graphs.length == 0) {
			return;
		}

		for (int i = graphs.length - 1; i >= 0; i--) {
			IGraph graph = graphs[i];
			if (this.selectedItems.contains(graph)) {
				this.selectedItems.remove(graph);
			}
		}
		fireGraphSelectChanged(new GraphSelectedChangedEvent(this.getCanvas(), this));
	}

	public boolean isSelected(IGraph item) {
		return this.selectedItems.contains(item);
	}

	public void addGraphSelectChangedListener(GraphSelectChangedListener listener) {
		this.list.add(GraphSelectChangedListener.class, listener);
	}

	public void removeGraphSelectChangedListener(GraphSelectChangedListener listener) {
		this.list.remove(GraphSelectChangedListener.class, listener);
	}

	public void fireGraphSelectChanged(GraphSelectedChangedEvent e) {
		Object[] listeners = this.list.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == GraphSelectChangedListener.class) {
				((GraphSelectChangedListener) listeners[i + 1]).graphSelectChanged(e);
			}
		}
	}

	public abstract void paint(Graphics graphics);

	@Override
	public void clean() {
		this.selectedItems.clear();
	}
}
