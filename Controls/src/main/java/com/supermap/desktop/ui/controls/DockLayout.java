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
		if (dockPath.isLeaf()) {
			return;
		}

		addDock(dockbar, dockPath.getDirection(), dockPath.getNext());
	}
}
