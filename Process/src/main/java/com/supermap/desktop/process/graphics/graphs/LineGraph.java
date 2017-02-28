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

	private IGraph preProcess;
	private IGraph nextProcess;

	private GeneralPath path;
	private Point start;
	private Point end;
	private boolean isArrow = true;

	public LineGraph(GraphCanvas canvas) {
		super(canvas, new GeneralPath());
		this.path = new GeneralPath();
	}

	public IGraph getPreProcess() {
		return preProcess;
	}

	public IGraph getNextProcess() {
		return nextProcess;
	}

	public Point[] getPoints() {
		if (this.preProcess == null || (this.nextProcess == null && this.end == null)) {
			return null;
		}
		Point[] re = new Point[2];

		Point preTopCenter = new Point();
		preTopCenter.setLocation(this.preProcess.getLocation().getX() + this.preProcess.getWidth() / 2, this.preProcess.getLocation().getY());
		Point preLeftCenter = new Point();
		preLeftCenter.setLocation(this.preProcess.getLocation().getX(), this.preProcess.getLocation().getY() + this.preProcess.getHeight() / 2);
		Point preBottomCenter = new Point();
		preBottomCenter.setLocation(this.preProcess.getLocation().getX() + this.preProcess.getWidth() / 2, this.preProcess.getLocation().getY() + this.preProcess.getHeight());
		Point preRightCenter = new Point();
		preRightCenter.setLocation(this.preProcess.getLocation().getX() + this.preProcess.getWidth(), this.preProcess.getLocation().getY() + this.preProcess.getHeight() / 2);

		if (this.nextProcess != null && this.nextProcess.getLocation() != null) {
			Point nextTopCenter = new Point();
			Point nextLeftCenter = new Point();
			Point nextBottomCenter = new Point();
			Point nextRightCenter = new Point();
			nextTopCenter.setLocation(this.nextProcess.getLocation().getX() + this.nextProcess.getWidth() / 2, this.nextProcess.getLocation().getY());
			nextLeftCenter.setLocation(this.nextProcess.getLocation().getX(), this.nextProcess.getLocation().getY() + this.nextProcess.getHeight() / 2);
			nextBottomCenter.setLocation(this.nextProcess.getLocation().getX() + this.nextProcess.getWidth() / 2, this.nextProcess.getLocation().getY() + this.nextProcess.getHeight());
			nextRightCenter.setLocation(this.nextProcess.getLocation().getX() + this.nextProcess.getWidth(), this.nextProcess.getLocation().getY() + this.nextProcess.getHeight() / 2);

			if (this.preProcess.getLocation().getX() > this.nextProcess.getLocation().getX() + this.nextProcess.getWidth()) {
				re[0] = preLeftCenter;
				re[1] = nextRightCenter;
			} else if (this.preProcess.getLocation().getX() + this.preProcess.getWidth() < this.nextProcess.getLocation().getX()) {
				re[0] = preRightCenter;
				re[1] = nextLeftCenter;
			} else if (this.preProcess.getLocation().getY() > this.nextProcess.getLocation().getY() + this.nextProcess.getHeight()) {
				re[0] = preTopCenter;
				re[1] = nextBottomCenter;
			} else if (this.preProcess.getLocation().getY() + this.preProcess.getHeight() < this.nextProcess.getLocation().getY()) {
				re[0] = preBottomCenter;
				re[1] = nextTopCenter;
			} else {
				re[0] = preBottomCenter;
				re[1] = nextTopCenter;
			}
		} else {
			try {
				if (this.preProcess.getLocation().getX() > this.end.getX()) {
					re[0] = preLeftCenter;
					re[1] = this.end;
				} else if (this.preProcess.getLocation().getX() + this.preProcess.getWidth() < this.end.getX()) {
					re[0] = preRightCenter;
					re[1] = this.end;
				} else if (this.preProcess.getLocation().getY() > this.end.getY()) {
					re[0] = preTopCenter;
					re[1] = this.end;
				} else if (this.preProcess.getLocation().getY() + this.preProcess.getHeight() < this.end.getY()) {
					re[0] = preBottomCenter;
					re[1] = this.end;
				} else {
					re[0] = preBottomCenter;
					re[1] = this.end;
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		return re;
	}

	private int intValue(double d) {
		Double d1 = new Double(d);
		return d1.intValue();
	}

	public Point getStart() {
		return getPoints()[0];
	}

	public Point getEnd() {
		if (this.nextProcess != null) {
			return getPoints()[1];
		} else {
			return this.end;
		}
	}

	public boolean isArrow() {
		return isArrow;
	}

	public void setPreProcess(IGraph preProcess) {
		this.preProcess = preProcess;
	}

	public void setNextProcess(IGraph nextProcess) {
		this.nextProcess = nextProcess;
	}

	public void setPath(GeneralPath path) {
		this.path = path;
	}

	public void setStart(Point start) {
		this.start = start;
	}

	public void setEnd(Point end) {
		this.end = end;
	}

	public void setArrow(boolean arrow) {
		isArrow = arrow;
	}

	public GeneralPath getPath() {
		return path;
	}

	public Shape getShape() {
		return this.path;
	}

	public void reset() {
		this.path.reset();
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
