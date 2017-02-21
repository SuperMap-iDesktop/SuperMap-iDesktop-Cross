package com.supermap.desktop.ui.controls;

import com.supermap.desktop.ui.Direction;
import com.supermap.desktop.ui.DockPath;

import java.util.List;

/**
 * Created by highsad on 2017/2/21.
 */
public class AbstractDockNode {
	private DockNode top;
	private DockNode left;
	private DockNode bottom;
	private DockNode right;

	public DockNode getTop() {
		return top;
	}

	public DockNode getLeft() {
		return left;
	}

	public DockNode getBottom() {
		return bottom;
	}

	public DockNode getRight() {
		return right;
	}

	public void addTopDock(Dockbar dockbar, DockPath dockPath) {
		if (this.top == null) {
			this.top = new DockNode();
		}

		this.top.addDock(dockbar, dockPath);
	}

	public void addLeftDock(Dockbar dockbar, DockPath dockPath) {
		if (this.left == null) {
			this.left = new DockNode();
		}

		this.left.addDock(dockbar, dockPath);
	}

	public void addBottomDock(Dockbar dockbar, DockPath dockPath) {
		if (this.bottom == null) {
			this.bottom = new DockNode();
		}

		this.bottom.addDock(dockbar, dockPath);
	}

	public void addRightDock(Dockbar dockbar, DockPath dockPath) {
		if (this.right == null) {
			this.right = new DockNode();
		}

		this.right.addDock(dockbar, dockPath);
	}
}
