package com.supermap.desktop.ui.controls;

import com.supermap.desktop.ui.Direction;
import com.supermap.desktop.ui.DockPath;
import org.flexdock.view.View;

/**
 * Created by highsad on 2017/2/21.
 */
public class DockLayout extends AbstractDockNode {
	private View mainView;

	public DockLayout(View mainView) {
		this.mainView = mainView;
	}

	public View getMainView() {
		return mainView;
	}

	public void addDock(Dockbar dockbar, DockPath dockPath) {
		Direction direction = dockPath.getDirection();
		DockPath addPath = dockPath.isLeaf() ? dockPath : dockPath.getNext();

		if (direction == Direction.TOP) {
			addTopDock(dockbar, addPath);
		} else if (direction == Direction.LEFT) {
			addLeftDock(dockbar, addPath);
		} else if (direction == Direction.BOTTOM) {
			addBottomDock(dockbar, addPath);
		} else if (direction == Direction.RIGHT) {
			addRightDock(dockbar, addPath);
		}
	}
}
