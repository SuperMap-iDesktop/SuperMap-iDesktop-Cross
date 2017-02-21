package com.supermap.desktop.ui.controls;

import com.supermap.desktop.ui.Direction;
import com.supermap.desktop.ui.DockPath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by highsad on 2017/2/21.
 */
public class DockNode extends AbstractDockNode {
	private List<Dockbar> centerDocks;

	public DockNode() {
		this.centerDocks = new ArrayList<>();

	}

	public Dockbar[] getCenterDocks() {
		return this.centerDocks.toArray(new Dockbar[this.centerDocks.size()]);
	}

	public void addDock(Dockbar dockbar, DockPath dockPath) {
		if (dockPath.isLeaf()) {
			addCenterDock(dockbar);
		} else {
			Direction direction = dockPath.getDirection();
			if (direction == Direction.TOP) {
				addTopDock(dockbar, dockPath.getNext());
			} else if (direction == Direction.LEFT) {
				addLeftDock(dockbar, dockPath.getNext());
			} else if (direction == Direction.BOTTOM) {
				addBottomDock(dockbar, dockPath.getNext());
			} else if (direction == Direction.RIGHT) {
				addRightDock(dockbar, dockPath.getNext());
			}
		}
	}

	public void addCenterDock(Dockbar dockbar) {
		this.centerDocks.add(dockbar);
	}
}
