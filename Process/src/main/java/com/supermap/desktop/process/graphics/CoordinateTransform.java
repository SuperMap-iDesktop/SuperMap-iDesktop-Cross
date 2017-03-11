package com.supermap.desktop.process.graphics;

import com.supermap.desktop.Application;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * 用来管理 GraphCanvas 的平移、缩放等
 * Created by highsad on 2017/3/2.
 */
public class CoordinateTransform {
	private GraphCanvas canvas;
	private Point originLocation;
	private double translateX; // 100% 画布尺寸下的 X轴平移距离，缩放后的结果会受 scale 的影响
	private double translateY; // 100% 画布尺寸下的 Y轴平移距离，缩放后的结果会受 scale 的影响
	private double scale; // percentage

	public CoordinateTransform(GraphCanvas canvas) {
		this(canvas, new Point(0, 0));
	}

	public CoordinateTransform(GraphCanvas canvas, Point originLocation) {
		this.originLocation = originLocation;
		this.canvas = canvas;
		this.translateX = 0;
		this.translateY = 0;
		this.scale = 0;
	}

	public void translate(double translateX, double translateY) {
		this.translateX += translateX;
		this.translateY += translateY;
	}

	public void scale(int scale) {
		this.scale += scale;
	}

	/**
	 * Inverse transforms the specified <code>src<code/>.
	 *
	 * @param src
	 * @return
	 */
	public Point inverse(Point src) {
		Point ret = new Point();

		try {
			AffineTransform transform = new AffineTransform();
			transform.translate(this.translateX, this.translateY);
			transform.scale(getScalePercentage(), getScalePercentage());
			transform.inverseTransform(src, ret);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return ret;
	}

	public Rectangle inverse(Rectangle rect) {
		Point leftTop = inverse(rect.getLocation());
		Point rightBottom = inverse(new Point(rect.x + rect.width, rect.y + rect.height));
		return new Rectangle(leftTop.x, leftTop.y, rightBottom.x - leftTop.x, rightBottom.y - leftTop.y);
	}

	public void reset() {
		this.translateX = 0;
		this.translateY = 0;
		this.scale = 0;
	}

	/**
	 * @param origin
	 * @return
	 */
	public AffineTransform getAffineTransform(AffineTransform origin) {
		AffineTransform transform = new AffineTransform(origin);
		double scalePercentage = getScalePercentage();
		transform.scale(scalePercentage, scalePercentage);
		transform.translate(this.translateX, this.translateY);
		return transform;

	}

	public double getScalePercentage() {
		return (100 + this.scale) / 100d;
	}
}
