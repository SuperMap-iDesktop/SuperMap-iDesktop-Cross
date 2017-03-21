package com.supermap.desktop.process.graphics.events;

import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;
import java.util.EventObject;

/**
 * Created by highsad on 2017/3/21.
 */
public class GraphBoundsChangedEvent extends EventObject {
	private final static int LOCATION = 1;
	private final static int SIZE = 2;
	private final static int LOCATION_AND_SIZE = 3;

	private int type = 3;
	private Point oldLocation = null;
	private Point newLocation = null;
	private int oldWidth = 0;
	private int newWidth = 0;
	private int oldHeight = 0;
	private int newHeight = 0;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @throws IllegalArgumentException if source is null.
	 */
	public GraphBoundsChangedEvent(IGraph graph, Point oldLocation, Point newLocation) {
		super(graph);
		this.type = LOCATION;
		this.oldLocation = oldLocation;
		this.newLocation = newLocation;
	}

	public GraphBoundsChangedEvent(IGraph graph, int oldWidth, int newWidth, int oldHeight, int newHeight) {
		super(graph);
		this.type = SIZE;
		this.oldWidth = oldWidth;
		this.newWidth = newWidth;
		this.oldHeight = oldHeight;
		this.newHeight = newHeight;
	}

	public GraphBoundsChangedEvent(IGraph graph, Point oldLocation, Point newLocation, int oldWidth, int newWidth, int oldHeight, int newHeight) {
		super(graph);
		this.type = LOCATION_AND_SIZE;
		this.oldLocation = oldLocation;
		this.newLocation = newLocation;
		this.oldWidth = oldWidth;
		this.newWidth = newWidth;
		this.oldHeight = oldHeight;
		this.newHeight = newHeight;
	}

	public int getType() {
		return this.type;
	}

	public Point getOldLocation() {
		return oldLocation;
	}

	public Point getNewLocation() {
		return newLocation;
	}

	public int getOldWidth() {
		return oldWidth;
	}

	public int getNewWidth() {
		return newWidth;
	}

	public int getOldHeight() {
		return oldHeight;
	}

	public int getNewHeight() {
		return newHeight;
	}
}
