package com.supermap.desktop.ui.controls;

import com.supermap.desktop.ui.Direction;
import com.supermap.desktop.ui.DockPath;

import java.util.List;

/**
 * Created by highsad on 2017/2/21.
 * 用来做浮动窗口自动构建布局和导入导出用的
 * 由于当前浮动窗口的使用方式导致实现过于复杂，难点很多，暂时使用
 * 保留临时解决方案进行浮动窗口的配置文件读取构建，以后有灵感再做
 */
public abstract class DockStrategy {
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

	public void addDock(Dockbar dockbar, DockPath dockPath) {
		Direction addToDirection = dockPath.getDirection();

		if (addToDirection == Direction.TOP) {
			addTopDock(dockbar, dockPath.getNext());
		} else if (addToDirection == Direction.LEFT) {
			addLeftDock(dockbar, dockPath.getNext());
		} else if (addToDirection == Direction.BOTTOM) {
			addBottomDock(dockbar, dockPath.getNext());
		} else if (addToDirection == Direction.RIGHT) {
			addRightDock(dockbar, dockPath.getNext());
		}
	}

	public void addTopDock(Dockbar dockbar, DockPath dockPath) {
		if (this.top == null) {
			DockNode parent = this instanceof DockNode ? (DockNode) this : null;
			this.top = new DockNode(parent, Direction.TOP);
		}

		this.top.addDock(dockbar, dockPath);
	}

	public void addLeftDock(Dockbar dockbar, DockPath dockPath) {
		if (this.left == null) {
			DockNode parent = this instanceof DockNode ? (DockNode) this : null;
			this.left = new DockNode(parent, Direction.LEFT);
		}

		this.left.addDock(dockbar, dockPath);
	}

	public void addBottomDock(Dockbar dockbar, DockPath dockPath) {
		if (this.bottom == null) {
			DockNode parent = this instanceof DockNode ? (DockNode) this : null;
			this.bottom = new DockNode(parent, Direction.BOTTOM);
		}

		this.bottom.addDock(dockbar, dockPath);
	}

	public void addRightDock(Dockbar dockbar, DockPath dockPath) {
		if (this.right == null) {
			DockNode parent = this instanceof DockNode ? (DockNode) this : null;
			this.right = new DockNode(parent, Direction.RIGHT);
		}

		this.right.addDock(dockbar, dockPath);
	}
}
