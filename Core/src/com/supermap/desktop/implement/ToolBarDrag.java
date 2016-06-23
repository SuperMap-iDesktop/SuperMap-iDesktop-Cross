package com.supermap.desktop.implement;

import com.supermap.desktop.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author XiaJT
 */
public class ToolBarDrag {

	private static ToolBarDrag toolBarDrag;

	private SmToolbar toolbar;
	private Rectangle rectangle;
	private int lastPlace;

	private static final int UP = 1;
	private static final int LEFT = 2;
	private static final int DOWN = 3;
	private static final int RIGHT = 4;

	private MouseAdapter mouseListener = new MouseAdapter() {
		@Override
		public void mouseDragged(MouseEvent e) {
			SmToolbar currentToolbar = getCurrentForcedToolbar(e.getPoint());
			if (currentToolbar == null) {
				removedDrewLine();
				return;
			}
			if (currentToolbar != ToolBarDrag.this.toolbar) {
				removedDrewLine();
			}

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			removedDrewLine();
		}
	};

	private void removedDrewLine() {

	}

	private ToolBarDrag() {
		// 单例
	}

	public static ToolBarDrag getInstance() {
		if (toolBarDrag == null) {
			toolBarDrag = new ToolBarDrag();
		}
		return new ToolBarDrag();
	}

	public static void addListeners(SmToolbar smToolbar) {
		getInstance().install(smToolbar);
	}

	public static void removeListeners(SmToolbar smToolbar) {
		getInstance().unInstall(smToolbar);
	}

	public void install(SmToolbar smToolbar) {
		this.unInstall(smToolbar);
		smToolbar.addMouseMotionListener(mouseListener);
		smToolbar.addMouseListener(mouseListener);
	}

	public void unInstall(SmToolbar smToolbar) {
		smToolbar.removeMouseMotionListener(mouseListener);
		smToolbar.removeMouseListener(mouseListener);
	}

	public SmToolbar getCurrentForcedToolbar(Point point) {
		Component deepestComponent = SwingUtilities.getDeepestComponentAt(Application.getActiveApplication().getMainFrame().getToolbarManager().getToolbarsContainer(), point.x, point.y);
		if (deepestComponent != null) {
			return getParentToolBar(deepestComponent);
		}
		return null;
	}

	public static SmToolbar getParentToolBar(Component component) {
		if (component == null) {
			return null;
		}
		if (component instanceof SmToolbar) {
			return ((SmToolbar) component);
		}
		return getParentToolBar(component.getParent());
	}
}
