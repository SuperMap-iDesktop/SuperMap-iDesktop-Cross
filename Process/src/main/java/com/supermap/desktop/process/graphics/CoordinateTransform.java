package com.supermap.desktop.process.graphics;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.graphics.events.CanvasTransformEvent;
import com.supermap.desktop.process.graphics.events.CanvasTransformListener;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * 用来管理 GraphCanvas 的平移、缩放等
 * Created by highsad on 2017/3/2.
 */
public class CoordinateTransform {
	private GraphCanvas canvas;
	private double maxScale = 900d;
	private double minScale = -99d;
	private double maxTranslateX = Integer.MAX_VALUE;
	private double minTranslateX = Integer.MIN_VALUE;
	private double maxTranslateY = Integer.MAX_VALUE;
	private double minTranslateY = Integer.MIN_VALUE;
	private double translateX; // 100% 画布尺寸下的 X轴平移距离，缩放后的结果会受 scale 的影响
	private double translateY; // 100% 画布尺寸下的 Y轴平移距离，缩放后的结果会受 scale 的影响
	private double scale; // percentage

	private EventListenerList listenerList = new EventListenerList();

	public CoordinateTransform(GraphCanvas canvas) {
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
		fireCanvasTransform(new CanvasTransformEvent(this.canvas, CanvasTransformEvent.TYPE_TRANSLATE));
	}

	public void translateX(double translateX) {
		this.translateX += translateX;
		this.translateX = this.translateX >= this.maxTranslateX ? this.maxTranslateX : this.translateX;
		this.translateX = this.translateX <= this.minTranslateX ? this.minTranslateX : this.translateX;
		fireCanvasTransform(new CanvasTransformEvent(this.canvas, CanvasTransformEvent.TYPE_TRANSLATE));
	}

	public void translateXTo(double translateTo) {
		if (this.translateX != translateTo) {
			this.translateX = translateTo;
			this.translateX = this.translateX >= this.maxTranslateX ? this.maxTranslateX : this.translateX;
			this.translateX = this.translateX <= this.minTranslateX ? this.minTranslateX : this.translateX;
			fireCanvasTransform(new CanvasTransformEvent(this.canvas, CanvasTransformEvent.TYPE_TRANSLATE));
		}
	}

	public void translateY(double translateY) {
		this.translateY += translateY;
		this.translateY = this.translateY >= this.maxTranslateY ? this.maxTranslateY : this.translateY;
		this.translateY = this.translateY <= this.minTranslateY ? this.minTranslateY : this.translateY;
		fireCanvasTransform(new CanvasTransformEvent(this.canvas, CanvasTransformEvent.TYPE_TRANSLATE));
	}

	public void translateYTo(double translateTo) {
		if (this.translateY != translateTo) {
			this.translateY = translateTo;
			this.translateY = this.translateY >= this.maxTranslateY ? this.maxTranslateY : this.translateY;
			this.translateY = this.translateY <= this.minTranslateY ? this.minTranslateY : this.translateY;
			fireCanvasTransform(new CanvasTransformEvent(this.canvas, CanvasTransformEvent.TYPE_TRANSLATE));
		}
	}

	public void scale(double scale) {
		this.scale += scale;

		if (this.scale >= this.maxScale) {
			this.scale = this.maxScale;
		} else if (this.scale <= this.minScale) {
			this.scale = this.minScale;
		}
		fireCanvasTransform(new CanvasTransformEvent(this.canvas, CanvasTransformEvent.TYPE_SCALE));
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
	 * 指定 distance 从屏幕坐标系到画布坐标系的转换。
	 *
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

	/**
	 * 指定 rect 从屏幕坐标系到画布坐标系的转换。
	 *
	 * @param rect
	 * @return
	 */
	public Rectangle inverse(Rectangle rect) {
		return inverse(rect.x, rect.y, rect.width, rect.height);
	}

	public Rectangle inverse(int x, int y, int width, int height) {
		Point leftTop = inverse(new Point(x, y));
		Point rightBottom = inverse(new Point(x + width, y + height));
		return new Rectangle(leftTop.x, leftTop.y, rightBottom.x - leftTop.x, rightBottom.y - leftTop.y);
	}

	/**
	 * graph 的空间位置信息从屏幕坐标系到画布坐标系的转换。
	 *
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

	/**
	 * @param src
	 * @return
	 */
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
		return transform(rect.x, rect.y, rect.width, rect.height);
	}

	public Rectangle transform(int x, int y, int width, int height) {
		Point leftTop = transform(new Point(x, y));
		Point rightBottom = transform(new Point(x + width, y + height));
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

	public void addCanvasTransformListener(CanvasTransformListener listener) {
		this.listenerList.add(CanvasTransformListener.class, listener);
	}

	public void removeCanvasTransformListener(CanvasTransformListener listener) {
		this.listenerList.remove(CanvasTransformListener.class, listener);
	}

	private void fireCanvasTransform(CanvasTransformEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == CanvasTransformListener.class) {
				((CanvasTransformListener) listeners[i + 1]).canvasTransform(e);
			}
		}
	}
}
