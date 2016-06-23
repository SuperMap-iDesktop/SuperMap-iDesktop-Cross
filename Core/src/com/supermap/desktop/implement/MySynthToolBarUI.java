package com.supermap.desktop.implement;

import com.supermap.desktop.Application;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.synth.SynthToolBarUI;
import java.awt.*;

/**
 * @author XiaJT
 */
public class MySynthToolBarUI extends SynthToolBarUI {

	private static final int UP = 1;
	private static final int LEFT = 2;
	private static final int DOWN = 3;
	private static final int RIGHT = 4;

	@Override
	public void setFloating(boolean b, Point p) {
		// 放置时判断位置
		if (p == null) {
			super.setFloating(b, p);
			return;
		}
		Component deepestComponent = SwingUtilities.getDeepestComponentAt(Application.getActiveApplication().getMainFrame().getToolbarManager().getToolbarsContainer(), p.x, p.y);
		if (!b && deepestComponent != null) {
			if (deepestComponent instanceof ToolBarContainer) {
				deepestComponent = ((ToolBarContainer) deepestComponent).getComponent(((ToolBarContainer) deepestComponent).getComponentCount() - 1);
			}
			if (isSmToolBarSon(deepestComponent)) {
				// 插入
				int index = ((SmToolbar) toolBar).getIndex();
				SmToolbar resultToolBar = getParentToolBar(deepestComponent);
				Rectangle resultToolBarBounds = resultToolBar.getBounds();
				int putPlace = getPutPlace(resultToolBarBounds, p);
				if (resultToolBar.getHeight() + resultToolBar.getY() - p.y < 5) {
					putPlace = DOWN;
				}
				if (putPlace == DOWN) {
					super.setFloating(b, p);
					int toolBarContainerIndex = ((ToolBarContainer) resultToolBar.getParent()).getIndex() + 1;
					ToolBarContainer toolBarContainer = ToolBarContainer.getToolBarContainer(toolBarContainerIndex);
					((SmToolbar) toolBar).setRowIndex(toolBarContainerIndex);
					Container parent = toolBar.getParent();
					parent.remove(toolBar);
					parent.validate();
					toolBarContainer.add(toolBar);
					toolBarContainer.validate();
					Application.getActiveApplication().getMainFrame().getToolbarManager().update();
				} else if (resultToolBar != toolBar) {

					Container parent = resultToolBar.getParent();
					int placeIndex = resultToolBar.getIndex();
					if (putPlace == RIGHT) {
						placeIndex++;
					}
					if (index < placeIndex) {
						// 原来在前面
						for (int i = 0; i < parent.getComponentCount(); i++) {
							if (parent.getComponent(i) != null && parent.getComponent(i) instanceof SmToolbar) {
								SmToolbar component = (SmToolbar) parent.getComponent(i);
								if (component.getIndex() >= placeIndex) {
									break;
								}
								if (component.getIndex() > index) {
									int temp = component.getIndex();
									component.setIndex(index);
									index = temp;
								}
							}
						}
						((SmToolbar) toolBar).setIndex(index);
					} else {
						// 本来在后面

						for (int i = parent.getComponentCount() - 1; i >= 0; i--) {
							if (parent.getComponent(i) != null && parent.getComponent(i) instanceof SmToolbar) {
								SmToolbar component = (SmToolbar) parent.getComponent(i);
								if (component.getIndex() <= index) {
									break;
								}
								if (component.getIndex() < placeIndex) {
									int temp = component.getIndex();
									component.setIndex(placeIndex);
									placeIndex = temp;
								}
							}
						}
						((SmToolbar) toolBar).setIndex(placeIndex);
					}
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

	private int getPutPlace(Rectangle resultToolBarBounds, Point p) {
		if (resultToolBarBounds.contains(p)) {
			if (p.getX() - resultToolBarBounds.getX() < 50) {
				return LEFT;
			}
		}
		return RIGHT;
	}

	private boolean isSmToolBarSon(Component deepestComponent) {
		return getParentToolBar(deepestComponent) != null;
	}

	private SmToolbar getParentToolBar(Component component) {
		if (component == null) {
			return null;
		}
		if (component instanceof SmToolbar) {
			return ((SmToolbar) component);
		}
		return getParentToolBar(component.getParent());
	}

	protected Border createRolloverBorder() {
		return new EmptyBorder(3, 3, 3, 3);
	}

	protected Border createNonRolloverBorder() {
		return new EmptyBorder(3, 3, 3, 3);
	}

	@Override
	public boolean canDock(Component c, Point p) {
		// 是否可以放置
		return Application.getActiveApplication().getMainFrame().getToolbarManager().getToolbarsContainer().contains(p);
	}
}
