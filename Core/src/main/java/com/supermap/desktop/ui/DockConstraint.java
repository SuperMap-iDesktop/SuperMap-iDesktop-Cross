package com.supermap.desktop.ui;

import java.util.ArrayList;

/**
 * Created by highsad on 2016/11/17.
 */
public class DockConstraint {
	private DockConstraint left;
	private DockConstraint right;
	private DockConstraint top;
	private DockConstraint bottom;

	private ArrayList<XMLDockbar> dockbars = new ArrayList<>();

	public DockConstraint() {

	}

	public DockConstraint getLeft() {
		return left;
	}

	public DockConstraint getRight() {
		return right;
	}

	public DockConstraint getTop() {
		return top;
	}

	public DockConstraint getBottom() {
		return bottom;
	}

	public XMLDockbar[] getDockbars() {
		return this.dockbars.toArray(new XMLDockbar[this.dockbars.size()]);
	}

	public void addDockbar(XMLDockbar dockbar) {
		this.dockbars.add(dockbar);
	}

	public void install(XMLDockbar dockbar) {
		if (dockbar != null) {
			Direction[] directions = dockbar.getDockPath().getDirections();
			DockConstraint dc = this;
			for (int i = 0; i < directions.length; i++) {
				Direction direction = directions[i];

				if (direction == Direction.TOP) {
					this.top = this.top == null ? new DockConstraint() : this.top;
					dc = this.top;
				} else if (direction == Direction.LEFT) {
					this.left = this.left == null ? new DockConstraint() : this.left;
					dc = this.left;
				} else if (direction == Direction.BOTTOM) {
					this.bottom = this.bottom == null ? new DockConstraint() : this.bottom;
					dc = this.bottom;
				} else if (direction == Direction.RIGHT) {
					this.right = this.right == null ? new DockConstraint() : this.right;
					dc = this.right;
				}
			}
			dc.addDockbar(dockbar);
		}
	}

	public void reset() {
		this.left.reset();
		this.right.reset();
		this.top.reset();
		this.bottom.reset();
		this.dockbars.clear();
	}
}
