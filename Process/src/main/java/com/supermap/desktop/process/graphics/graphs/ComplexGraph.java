package com.supermap.desktop.process.graphics.graphs;

import com.alibaba.fastjson.JSONObject;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.events.GraphBoundsChangedListener;

import java.awt.*;

/**
 * It's not used temporarily.Delaying completion of this class whenever there is a need.
 * Created by highsad on 2017/3/22.
 */
public class ComplexGraph implements IGraph {
	private java.util.List<IGraph> graphs;

	@Override
	public void setCanvas(GraphCanvas canvas) {

	}

	@Override
	public GraphCanvas getCanvas() {
		return null;
	}

	@Override
	public Rectangle getBounds() {
		return null;
	}

	@Override
	public Point getLocation() {
		return null;
	}

	@Override
	public Point getCenter() {
		return null;
	}

	@Override
	public int getWidth() {
		return 0;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public void setLocation(Point point) {

	}

	@Override
	public void setSize(int width, int height) {

	}

	@Override
	public boolean contains(Point point) {
		return false;
	}

	@Override
	public void addGraphBoundsChangedListener(GraphBoundsChangedListener listener) {

	}

	@Override
	public void removeGraghBoundsChangedListener(GraphBoundsChangedListener listener) {

	}

	@Override
	public String toXml() {
		return null;
	}

	@Override
	public IGraph formXml(JSONObject xml) {
		return null;
	}
}
