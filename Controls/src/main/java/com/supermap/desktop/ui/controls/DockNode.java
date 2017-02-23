package com.supermap.desktop.ui.controls;

import com.supermap.desktop.ui.Direction;
import com.supermap.desktop.ui.DockPath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by highsad on 2017/2/21.
 * 用来做浮动窗口自动构建布局和导入导出用的
 * 由于当前浮动窗口的使用方式导致实现过于复杂，难点很多，暂时使用
 * 保留临时解决方案进行浮动窗口的配置文件读取构建，以后有灵感再做
 */
public class DockNode extends DockStrategy {
	private DockNode parent; // 上级
	private Direction nodeDirection; // 在上级中的方位
	private List<Dockbar> centerDocks;

	public DockNode(DockNode parent, Direction nodeDirection) {
		this.parent = parent;
		this.nodeDirection = nodeDirection;
		this.centerDocks = new ArrayList<>();

	}

	public DockNode getParent() {
		return parent;
	}

	public Direction getNodeDirection() {
		return nodeDirection;
	}

	public Dockbar[] getCenterDocks() {
		return this.centerDocks.toArray(new Dockbar[this.centerDocks.size()]);
	}

	public void addDock(Dockbar dockbar, DockPath dockPath) {
		if (dockPath == null) {
			addCenterDock(dockbar);
		} else {
			super.addDock(dockbar, dockPath);
		}
	}

	public void addCenterDock(Dockbar dockbar) {
		this.centerDocks.add(dockbar);
	}
}
