package com.supermap.desktop.process.graphics;

import java.awt.*;

/**
 * 用来管理 GraphCanvas 的平移、缩放等
 * Created by highsad on 2017/3/2.
 */
public class CoordinateTransform {
	private Point originLocation;
	private double translateX;
	private double translateY;
	private double scaleX;
	private double scaleY;

	public CoordinateTransform() {
		this(new Point(0, 0));
	}

	public CoordinateTransform(Point originLocation) {
		this.originLocation = originLocation;
	}

	public void translate(double translateX, double translateY) {
		this.translateX += translateX;
		this.translateY += translateY;
	}

	public void scale(double scaleX, double scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}
}
