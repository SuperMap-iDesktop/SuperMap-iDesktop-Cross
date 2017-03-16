package com.supermap.desktop.process.graphics;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * 用来管理 GraphCanvas 的平移、缩放等
 * Created by highsad on 2017/3/2.
 */
public class CoordinateTransform {
	private GraphCanvas canvas;
	private Point originLocation;
	private double maxScale = 900d;
	private double minScale = -99d;
	private double maxTranslateX = Integer.MAX_VALUE;
	private double minTranslateX = Integer.MIN_VALUE;
	private double maxTranslateY = Integer.MAX_VALUE;
	private double minTranslateY = Integer.MIN_VALUE;
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
		this.translateX = this.translateX >= this.maxTranslateX ? this.maxTranslateX : this.translateX;
		this.translateX = this.translateX <= this.minTranslateX ? this.minTranslateX : this.translateX;

		this.translateY += translateY;
		this.translateY = this.translateY >= this.maxTranslateY ? this.maxTranslateY : this.translateY;
		this.translateY = this.translateY <= this.minTranslateY ? this.minTranslateY : this.translateY;
	}

	public void scale(double scale) {
		this.scale += scale;

		if (this.scale >= this.maxScale) {
			this.scale = this.maxScale;
		} else if (this.scale <= this.minScale) {
			this.scale = this.minScale;
		}
	}

	/**
	 * @param scale
	 * @return
	 */
	public double validateScale(double scale) {
		double total = this.scale + scale;

		if (total >= this.maxScale) {
			return this.maxScale - this.scale;
		} else if (total <= this.minScale) {
			return this.scale - this.minScale;
		} else {
			return scale;
		}
	}

	public boolean isScaleValid(double scale) {
		return scale < this.maxScale - this.scale && scale > this.minScale - this.scale;
	}

	public boolean isTranslateXValid(double translateX) {
		return translateX < this.maxTranslateX - this.translateX && translateX > this.minTranslateX - this.translateX;
	}

	public boolean isTranslateYValid(double translateY) {
		return translateY < this.maxTranslateY - this.translateY && translateY > this.minTranslateY - this.translateY;
	}

	public double getMaxScale() {
		return maxScale;
	}

	public double getMinScale() {
		return minScale;
	}

	public double getMaxTranslateX() {
		return maxTranslateX;
	}

	public double getMinTranslateX() {
		return minTranslateX;
	}

	public double getMaxTranslateY() {
		return maxTranslateY;
	}

	public double getMinTranslateY() {
		return minTranslateY;
	}

	public double getTranslateX() {
		return translateX;
	}

	public double getTranslateY() {
		return translateY;
	}

	public double getScale() {
		return scale;
	}

	public void setMaxScale(double maxScale) {
		this.maxScale = maxScale;
	}

	public void setMinScale(double minScale) {
		this.minScale = minScale;
	}

	public void setMaxTranslateX(double maxTranslateX) {
		this.maxTranslateX = maxTranslateX;
	}

	public void setMinTranslateX(double minTranslateX) {
		this.minTranslateX = minTranslateX;
	}

	public void setMaxTranslateY(double maxTranslateY) {
		this.maxTranslateY = maxTranslateY;
	}

	public void setMinTranslateY(double minTranslateY) {
		this.minTranslateY = minTranslateY;
	}

	public void setTranslateX(double translateX) {
		this.translateX = translateX;
	}

	public void setTranslateY(double translateY) {
		this.translateY = translateY;
	}

	public void setScale(double scale) {
		this.scale = scale;
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
			transform.scale(getScaleRate(), getScaleRate());
			transform.translate(this.translateX, this.translateY);
			transform.inverseTransform(src, ret);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return ret;
	}

	/**
	 * @param distance
	 * @return
	 */
	public int inverse(int distance) {
		if (distance <= 0) {
			return -1;
		}

		Double invesed = new Double(distance / getScaleRate());
		return invesed.intValue();
	}

	public Rectangle inverse(Rectangle rect) {
		Point leftTop = inverse(rect.getLocation());
		Point rightBottom = inverse(new Point(rect.x + rect.width, rect.y + rect.height));
		return new Rectangle(leftTop.x, leftTop.y, rightBottom.x - leftTop.x, rightBottom.y - leftTop.y);
	}

	/**
	 * @param graph
	 */
	public void inverse(IGraph graph) {
		if (graph == null) {
			return;
		}

		Point inversedLocation = inverse(graph.getLocation());
		int inversedWidth = inverse(graph.getWidth());
		int inversedHeight = inverse(graph.getHeight());
		graph.setLocation(inversedLocation);
		graph.setSize(inversedWidth, inversedHeight);
	}

	public Point transform(Point src) {
		Point ret = new Point();

		try {
			AffineTransform affineTransform = new AffineTransform();
			affineTransform.scale(getScaleRate(), getScaleRate());
			affineTransform.translate(this.translateX, this.translateY);
			affineTransform.transform(src, ret);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return ret;
	}

	public Rectangle transform(Rectangle rect) {
		Point leftTop = transform(rect.getLocation());
		Point rightBottom = transform(new Point(rect.x + rect.width, rect.y + rect.height));
		return new Rectangle(leftTop.x, leftTop.y, rightBottom.x - leftTop.x, rightBottom.y - leftTop.y);
	}

	public void reset() {
		this.translateX = 0;
		this.translateY = 0;
		this.scale = 0;
	}

	/**
	 * @return
	 */
	public AffineTransform getAffineTransform(AffineTransform origin) {
		AffineTransform transform = new AffineTransform(origin);
		double scalePercentage = getScaleRate();
		transform.scale(scalePercentage, scalePercentage);
		transform.translate(this.translateX, this.translateY);
		return transform;
	}

	public double getScaleRate() {
		return getScaleRate(this.scale);
	}

	private double getScaleRate(double scale) {
		return (100 + scale) / 100d;
	}

	public double getScaleValue() {
		return this.scale;
	}
}
