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
	private int lastPlace;
	private final int lineWidth = 2;


	private ToolBarDrag() {
		// 单例
	}

	private MouseAdapter mouseListener = new MouseAdapter() {
		@Override
		public void mouseDragged(MouseEvent e) {
			Point point = e.getPoint();
			JPanel toolbarsContainer = Application.getActiveApplication().getMainFrame().getToolbarManager().getToolbarsContainer();
			point = SwingUtilities.convertPoint((Component) e.getSource(), point, toolbarsContainer);
			SmToolbar currentToolbar = getCurrentForcedToolbar(point);

			if (currentToolbar != null) {

				int putPlace = ToolbarUIUtilties.getPutPlace(currentToolbar.getParent().getBounds(), point, (int) currentToolbar.getX());
				if (currentToolbar != toolbar || lastPlace != putPlace) {
					removedDrewLine();
					toolbar = currentToolbar;
					lastPlace = putPlace;
					addDrawLine();
				}
			} else {
				removedDrewLine();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			removedDrewLine();
		}
	};

	private void addDrawLine() {
		drawLine(Color.BLACK);
	}

	private void removedDrewLine() {
		if (toolbar != null) {
			Color background = toolbar.getBackground();
			drawLine(background);
			toolbar = null;
		}
	}

	private void drawLine(Color color) {
		if (toolbar != null) {

			Graphics2D graphics = (Graphics2D) toolbar.getGraphics();
			graphics.setColor(color);
			Rectangle rect = toolbar.getVisibleRect();
			switch (lastPlace) {
				case ToolbarUIUtilties.PUT_LEFT:
					graphics.fillRect(rect.x + 12, rect.y, lineWidth, rect.height);
					break;
				case ToolbarUIUtilties.PUT_DOWN:

					graphics.fillRect(rect.x + 12, rect.y + rect.height - 2, rect.width, lineWidth);
					break;
				case ToolbarUIUtilties.PUT_RIGHT:
					graphics.fillRect(rect.x + rect.width - 2, rect.y, lineWidth, rect.height);
			}
		}
	}

	public static ToolBarDrag getInstance() {
		if (toolBarDrag == null) {
			toolBarDrag = new ToolBarDrag();
		}
		return toolBarDrag;
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
		if (deepestComponent instanceof ToolBarContainer && ((ToolBarContainer) deepestComponent).getComponentCount() > 0) {

			deepestComponent = ((ToolBarContainer) deepestComponent).getComponent(((ToolBarContainer) deepestComponent).getComponentCount() - 1);
		} else {
			deepestComponent = null;
		}
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
