package com.supermap.desktop.implement;

import com.supermap.desktop.Application;

import java.awt.*;

/**
 * @author XiaJT
 */
public class ToolbarUIUtilties {

	public static final int PUT_UP = 1;
	public static final int PUT_LEFT = 2;
	public static final int PUT_DOWN = 3;
	public static final int PUT_RIGHT = 4;

	private ToolbarUIUtilties() {
		// 工具类
	}

	public static boolean isSmToolBarSon(Component deepestComponent) {
		return getParentToolBar(deepestComponent) != null;
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

	public static int getPutPlace(Rectangle resultToolBarBounds, Point p, int resultToolBarX) {
		if (resultToolBarBounds.contains(p)) {
			if (resultToolBarBounds.getHeight() + resultToolBarBounds.getY() - p.y < 12) {
				return PUT_DOWN;
			}
			if (p.getX() - resultToolBarX < 50) {
				return PUT_LEFT;
			}
		}
		return PUT_RIGHT;
	}

	public static void addToDown(int toolBarContainerIndex, SmToolbar smToolbar) {
		ToolBarContainer.prepareInsertRow(toolBarContainerIndex);
		ToolBarContainer toolBarContainer = ToolBarContainer.getToolBarContainer(toolBarContainerIndex);
		smToolbar.setRowIndex(toolBarContainerIndex);
		Container parent = smToolbar.getParent();
		parent.remove(smToolbar);
		parent.validate();
		toolBarContainer.add(smToolbar);
		toolBarContainer.validate();
		Application.getActiveApplication().getMainFrame().getToolbarManager().update();
	}
}
