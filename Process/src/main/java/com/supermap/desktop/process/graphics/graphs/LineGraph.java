package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.graphics.GraphCanvas;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.HashMap;

/**
 * Created by highsad on 2017/1/19.
 */
public class LineGraph extends AbstractGraph {

	private GeneralPath path;

	public LineGraph(GraphCanvas canvas) {
		super(canvas);
		this.path = new GeneralPath();
	}

	public Shape getShape() {
		return this.path;
	}

	public void reset() {
		this.path.reset();
	}

	// 计算
	public static double[] rotateVec(int px, int py, double ang, boolean isChLen, double newLen) {

		double mathstr[] = new double[2];
		// 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度
		double vx = px * Math.cos(ang) - py * Math.sin(ang);
		double vy = px * Math.sin(ang) + py * Math.cos(ang);
		if (isChLen) {
			double d = Math.sqrt(vx * vx + vy * vy);
			vx = vx / d * newLen;
			vy = vy / d * newLen;
			mathstr[0] = vx;
			mathstr[1] = vy;
		}
		return mathstr;
	}

	@Override
	public IGraph clone() {
		return null;
	}
}
