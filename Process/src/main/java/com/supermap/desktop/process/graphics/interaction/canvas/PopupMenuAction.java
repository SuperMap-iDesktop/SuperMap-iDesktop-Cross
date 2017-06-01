package com.supermap.desktop.process.graphics.interaction.canvas;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IContextMenuManager;
import com.supermap.desktop.process.graphics.GraphCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * @author XiaJT
 */
public class PopupMenuAction extends CanvasActionAdapter {
	private JPopupMenu processPopupMenu;

	public PopupMenuAction(GraphCanvas graphCanvas) {
		IContextMenuManager manager = Application.getActiveApplication().getMainFrame().getContextMenuManager();
		processPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop._FormProcess.FormContextMenu");
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1) {
			showPopupMenu(e);
		}
	}

	private void showPopupMenu(MouseEvent e) {
		processPopupMenu.show((Component) e.getSource(), e.getX(), e.getY());
	}

	@Override
	public void clean() {

	}
}
