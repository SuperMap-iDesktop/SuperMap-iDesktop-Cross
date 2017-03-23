package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.events.GraphBoundsChangedEvent;
import com.supermap.desktop.process.graphics.events.GraphBoundsChangedListener;

import javax.swing.event.EventListenerList;
import java.awt.*;

/**
 * Created by highsad on 2017/1/20.
 */
public abstract class AbstractGraph implements IGraph {

	private GraphCanvas canvas;
	protected Shape shape;
	private EventListenerList listenerList = new EventListenerList();

	public AbstractGraph(GraphCanvas canvas, Shape shape) {
		this.canvas = canvas;
		this.shape = shape;
	}

	public Shape getShape() {
		return this.shape;
	}

	@Override
	public Rectangle getBounds() {
		if (this.shape != null) {
			return this.shape.getBounds();
		} else {
			return null;
		}
	}

	@Override
	public Point getLocation() {
		if (this.shape != null) {
			return this.shape.getBounds().getLocation();
		} else {
			return null;
		}
	}

	@Override
	public Point getCenter() {
		if (this.shape != null) {
			double x = this.shape.getBounds().getX();
			double y = this.shape.getBounds().getY();
			double width = this.shape.getBounds().getWidth();
			double height = this.shape.getBounds().getHeight();
			Point center = new Point();
			center.setLocation(x + width / 2, y + height / 2);
			return center;
		} else {
			return null;
		}
	}

	@Override
	public int getWidth() {
		if (this.shape != null) {
			return this.shape.getBounds().width;
		} else {
			return -1;
		}
	}

	@Override
	public int getHeight() {
		if (this.shape != null) {
			return this.shape.getBounds().height;
		} else {
			return -1;
		}
	}

	@Override
	public void setLocation(Point point) {
		Point oldLocation = getShape().getBounds().getLocation();

		if (!oldLocation.equals(point)) {
			applyLocation(point);
			fireGraphBoundsChanged(new GraphBoundsChangedEvent(this, oldLocation, point));
		}
	}

	protected abstract void applyLocation(Point point);

	@Override
	public void setSize(int width, int height) {
		int oldWidth = getShape().getBounds().width;
		int oldHeight = getShape().getBounds().height;

		if (oldWidth != width && oldHeight != height) {
			applySize(width, height);
			fireGraphBoundsChanged(new GraphBoundsChangedEvent(this, oldWidth, width, oldHeight, height));
		}
	}

	protected abstract void applySize(int width, int height);

	@Override
	public boolean contains(Point point) {
		return this.shape.contains(point);
	}

	public GraphCanvas getCanvas() {
		return this.canvas;
	}

	@Override
	public void addGraphBoundsChangedListener(GraphBoundsChangedListener listener) {
		this.listenerList.add(GraphBoundsChangedListener.class, listener);
	}

	@Override
	public void removeGraghBoundsChangedListener(GraphBoundsChangedListener listener) {
		this.listenerList.remove(GraphBoundsChangedListener.class, listener);
	}

	protected void fireGraphBoundsChanged(GraphBoundsChangedEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == GraphBoundsChangedListener.class) {
				((GraphBoundsChangedListener) listeners[i + 1]).graghBoundsChanged(e);
			}
		}
	}
}
