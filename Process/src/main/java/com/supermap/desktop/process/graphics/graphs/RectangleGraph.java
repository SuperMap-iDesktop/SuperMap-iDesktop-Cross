package com.supermap.desktop.process.graphics.graphs;

import com.alibaba.fastjson.JSONObject;
import com.supermap.desktop.process.graphics.GraphCanvas;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Created by highsad on 2017/1/19.
 */
public class RectangleGraph extends AbstractGraph {
	private double arcWidth = 0d;
	private double arcHeight = 0d;

	public RectangleGraph(GraphCanvas canvas) {
		super(canvas, new RoundRectangle2D.Double(0, 0, 150, 50, 15, 15));
	}

	@Override
	public RoundRectangle2D getShape() {
		return (RoundRectangle2D) super.shape;
	}

	public double getArcWidth() {
		return this.arcWidth;
	}

	public double getArcHeight() {
		return this.arcHeight;
	}

	public void setArcWidth(double arcWidth) {
		getShape().setRoundRect(getShape().getX(), getShape().getY(), getShape().getWidth(), getShape().getHeight(), arcWidth, getShape().getArcHeight());
	}

	public void setArcHeight(double arcHeight) {
		this.arcHeight = arcHeight;
		getShape().setRoundRect(getShape().getX(), getShape().getY(), getShape().getWidth(), getShape().getHeight(), getShape().getArcWidth(), arcHeight);
	}

	@Override
	public void applyLocation(Point point) {
		getShape().setFrame(point.getX(), point.getY(), getShape().getWidth(), getShape().getHeight());
	}

	@Override
	public void applySize(int width, int height) {
		getShape().setFrame(getShape().getX(), getShape().getY(), width, height);
	}

	@Override
	public boolean contains(Point p) {
		return this.shape.contains(p);
	}

	@Override
	protected void toXmlHook(JSONObject jsonObject) {
		super.toXmlHook(jsonObject);
		jsonObject.put("shape", getShape().getX() + "," + getShape().getY() + "," + getShape().getWidth() + "," + getShape().getHeight() + "," + getShape().getArcWidth() + "," + getShape().getArcHeight());
	}

	@Override
	protected void onPaint(Graphics g) {
		g.setColor(new Color(202, 221, 254));
		((Graphics2D) g).fill(this.shape);
	}

	@Override
	protected void formXmlHook(JSONObject xml) {
		super.formXmlHook(xml);
		String[] shapes = ((String) xml.get("shape")).split(",");
		shape = new RoundRectangle2D.Double(Double.valueOf(shapes[0]), Double.valueOf(shapes[1]), Double.valueOf(shapes[2]), Double.valueOf(shapes[3]), Double.valueOf(shapes[4]), Double.valueOf(shapes[5]));
	}
}
