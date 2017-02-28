package com.supermap.desktop.process.graphics.graphs.decorator;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.graphs.AbstractGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.LineGraph;
import com.supermap.desktop.utilities.DoubleUtilities;

import java.awt.*;

/**
 * Created by highsad on 2017/2/25.
 */
public class ArrowDecorator extends AbstractDecorator {

	private LineGraph line;

	public ArrowDecorator(GraphCanvas canvas) {
		super(canvas, null);
	}

	public Point[] calArrow(Point start, Point end) {
		return calArrow(start.x, start.y, end.x, end.y);
	}

	public Point[] calArrow(int startX, int startY, int endX, int endY) {
		double awrad = 30 * Math.PI / 180;// 30表示角度，但是在计算中要用弧度进行计算，所以要把角度转换为弧度
		double arraow_len = 20;// 箭头长度
		double[] arr1 = rotateVec(endX - startX, endY - startY, awrad, arraow_len);
		double[] arr2 = rotateVec(endX - startX, endY - startY, -awrad, arraow_len);
		double x1 = endX - arr1[0]; // (x3,y3)是第一端点
		double y1 = endY - arr1[1];
		double x2 = endX - arr2[0]; // (x4,y4)是第二端点
		double y2 = endY - arr2[1];
		Point point1 = new Point(DoubleUtilities.intValue(x1), DoubleUtilities.intValue(y1));
		Point point2 = new Point(DoubleUtilities.intValue(x2), DoubleUtilities.intValue(y2));
		return new Point[]{point1, point2};
	}

	// 计算
	public double[] rotateVec(int px, int py, double ang, double newLen) {

		double mathstr[] = new double[2];
		// 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、新长度
		double vx = px * Math.cos(ang) - py * Math.sin(ang);
		double vy = px * Math.sin(ang) + py * Math.cos(ang);
		double d = Math.sqrt(vx * vx + vy * vy);
		vx = vx / d * newLen;
		vy = vy / d * newLen;
		mathstr[0] = vx;
		mathstr[1] = vy;
		return mathstr;
	}

	@Override
	public IGraph clone() {
		return null;
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
}
