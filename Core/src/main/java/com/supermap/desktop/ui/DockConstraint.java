package com.supermap.desktop.ui;

import java.util.ArrayList;

/**
 * Created by highsad on 2016/11/17.
 */
public class DockConstraint {
	private DockConstraint dockParent;

	private DockConstraint left;
	private DockConstraint right;
	private DockConstraint top;
	private DockConstraint bottom;

	private ArrayList<XMLDockbar> dockbars = new ArrayList<>();

	public DockConstraint() {

	}

	public DockConstraint getDockParent() {
		return this.dockParent;
	}

	public void setDockParent(DockConstraint dockParent) {
		this.dockParent = dockParent;
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

	/**
	 * 获取当前 Dock 的深度
	 *
	 * @return
	 */
	public int getDepth() {
		int depth = 0;

		while (this.getDockParent() != null) {
			depth++;
		}
		return depth;
	}

	public void install(XMLDockbar dockbar) {
		if (dockbar != null) {
			Direction[] directions = dockbar.getDockPath().getDirections();
			DockConstraint dc = this;
			for (int i = 0; i < directions.length; i++) {
				Direction direction = directions[i];

				if (direction == Direction.TOP) {
					this.top = this.top == null ? new DockConstraint() : this.top;
					this.top.setDockParent(dc);
					dc = this.top;
				} else if (direction == Direction.LEFT) {
					this.left = this.left == null ? new DockConstraint() : this.left;
					this.left.setDockParent(dc);
					dc = this.left;
				} else if (direction == Direction.BOTTOM) {
					this.bottom = this.bottom == null ? new DockConstraint() : this.bottom;
					this.bottom.setDockParent(dc);
					dc = this.bottom;
				} else if (direction == Direction.RIGHT) {
					this.right = this.right == null ? new DockConstraint() : this.right;
					this.right.setDockParent(dc);
					dc = this.right;
				}
			}
			dc.addDockbar(dockbar);
		}
	}

	/**
	 * 从自己开始往后进行优化，整合一些多余的 Dock 级别，比如配置文件里写的某一级可能并没有对应的控件，这种情况多发生在插件增删的时候
	 */
	public void optimize() {
//		if (this.dockbars.size() == 0) {
//			this.left
//		}
	}

	public void reset() {
		this.left.reset();
		this.right.reset();
		this.top.reset();
		this.bottom.reset();
		this.dockbars.clear();
	}
}
