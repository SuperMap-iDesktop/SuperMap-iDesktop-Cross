package com.supermap.desktop.implement;

import com.sun.java.swing.plaf.windows.WindowsToolBarUI;
import com.supermap.desktop.Application;

import javax.swing.*;
import java.awt.*;

/**
 * @author XiaJT
 */
public class MyWindowsToolBarUI extends WindowsToolBarUI {

	private Component subDockingSource;

	@Override
	public void setFloating(boolean b, Point p) {
		// 放置时判断位置
		if (p == null) {
			super.setFloating(b, p);
			return;
		}
		JPanel toolbarsContainer = Application.getActiveApplication().getMainFrame().getToolbarManager().getToolbarsContainer();
		if (subDockingSource != null) {
			p = SwingUtilities.convertPoint(subDockingSource, p, toolbarsContainer);
		}
		Component deepestComponent = SwingUtilities.getDeepestComponentAt(toolbarsContainer, p.x, p.y);
		if (!b && deepestComponent != null) {
			if (deepestComponent instanceof ToolBarContainer) {
				deepestComponent = ((ToolBarContainer) deepestComponent).getComponent(((ToolBarContainer) deepestComponent).getComponentCount() - 1);
			}
			if (ToolbarUIUtilties.isSmToolBarSon(deepestComponent)) {
				// 插入
				int index = ((SmToolbar) toolBar).getIndex();
				SmToolbar resultToolBar = ToolbarUIUtilties.getParentToolBar(deepestComponent);
				Rectangle contaninerBounds = resultToolBar.getParent().getBounds();
				int putPlace = ToolbarUIUtilties.getPutPlace(contaninerBounds, p, resultToolBar.getX());
				if (putPlace == ToolbarUIUtilties.PUT_DOWN) {
					int toolBarContainerIndex = ((ToolBarContainer) resultToolBar.getParent()).getIndex() + 1;
					super.setFloating(b, p);
					ToolbarUIUtilties.addToDown(toolBarContainerIndex, (SmToolbar) toolBar);
				} else if (resultToolBar != toolBar) {

					Container parent = resultToolBar.getParent();
					int placeIndex = resultToolBar.getIndex();
					if (((SmToolbar) toolBar).getRowIndex() != resultToolBar.getRowIndex()) {
						// 换行放最后，原来索引不重要了
						index = ((SmToolbar) parent.getComponent(parent.getComponentCount() - 1)).getIndex() + 1;
					}
					if (index < placeIndex) {
						// 原来在前面
						for (int i = 0; i < parent.getComponentCount(); i++) {
							if (parent.getComponent(i) != null && parent.getComponent(i) instanceof SmToolbar) {
								SmToolbar component = (SmToolbar) parent.getComponent(i);
								if (component.getIndex() > placeIndex) {
									break;
								}
								if (component.getIndex() == placeIndex) {
									if (putPlace == ToolbarUIUtilties.PUT_RIGHT) {
										int temp = component.getIndex();
										component.setIndex(index);
										index = temp;
									}
									break;
								}
								if (component.getIndex() > index) {
									int temp = component.getIndex();
									component.setIndex(index);
									index = temp;
								}
							}
						}
					} else {
						// 本来在后面
						for (int i = parent.getComponentCount() - 1; i >= 0; i--) {
							if (parent.getComponent(i) != null && parent.getComponent(i) instanceof SmToolbar) {
								SmToolbar component = (SmToolbar) parent.getComponent(i);
								if (component.getIndex() < placeIndex) {
									break;
								}
								if (component.getIndex() == placeIndex) {
									if (putPlace == ToolbarUIUtilties.PUT_LEFT) {
										int temp = component.getIndex();
										component.setIndex(index);
										index = temp;
									}
									break;
								}
								if (component.getIndex() < index) {
									int temp = component.getIndex();
									component.setIndex(index);
									index = temp;
								}
							}
						}
					}
					((SmToolbar) toolBar).setIndex(index);

					super.setFloating(false, p);
					Container parent1 = toolBar.getParent();
					parent1.remove(toolBar);
					parent1.validate();
					parent.add(toolBar);
					parent.validate();
					parent.repaint();
				} else {
					super.setFloating(false, p);
				}
			}
		} else {
			super.setFloating(true, p);
		}
	}


	@Override
	protected DragWindow createDragWindow(JToolBar toolbar) {
		DragWindow dragWindow = super.createDragWindow(toolbar);
		dragWindow.setOpacity(0.8f);
		return dragWindow;
	}

	@Override
	public boolean canDock(Component c, Point p) {
		// 是否可以放置
		subDockingSource = c;
		Point globalPoint = getGlobalPoint(p);
		JPanel toolbarsContainer = Application.getActiveApplication().getMainFrame().getToolbarManager().getToolbarsContainer();
		Rectangle bounds = new Rectangle(toolbarsContainer.getLocationOnScreen().x, toolbarsContainer.getLocationOnScreen().y, toolbarsContainer.getWidth(), toolbarsContainer.getHeight());
		return bounds.contains(globalPoint);
	}

	private Point getGlobalPoint(Point p) {
		Point dockingPosition = subDockingSource.getLocationOnScreen();
		return new Point(p.x + dockingPosition.x, p.y + dockingPosition.y);
	}


}
