/*
 * Copyright (C) 2004 NNL Technology AB
 * Visit www.infonode.net for information about InfoNode(R) 
 * products and how to contact NNL Technology AB.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, 
 * MA 02111-1307, USA.
 */

// $Id: CursorManager.java,v 1.17 2005/12/04 13:46:04 jesper Exp $
package net.infonode.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.WeakHashMap;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.17 $
 */
public class CursorManager {
	private static class RootCursorInfo {
		private Cursor savedCursor;
		private Cursor cursor;
		private JComponent jComponentPanel;

		private boolean cursorSet = false;

		RootCursorInfo(JComponent panel) {
			this.jComponentPanel = panel;
		}

		public JComponent getComponent() {
			return jComponentPanel;
		}

		public void pushCursor(Cursor cursor) {
			if (savedCursor == null)
				savedCursor = cursor;

			cursorSet = true;
		}

		public Cursor popCursor() {
			Cursor cursorTemp = savedCursor;
			savedCursor = null;

			cursorSet = false;
			return cursorTemp;
		}

		public boolean isCursorSet() {
			return cursorSet;
		}

		public Cursor getCursor() {
			return this.cursor;
		}

		public void setCursor(Cursor cursor) {
			this.cursor = cursor;
		}

	}

	private static boolean enabled = true;
	private static WeakHashMap windowPanels = new WeakHashMap();

	private CursorManager() {
	}

	public static void setGlobalCursor(final JRootPane root, Cursor cursor) {
		if (root == null)
			return;

		RootCursorInfo rootCursorInfo = (RootCursorInfo) windowPanels.get(root);

		if (rootCursorInfo == null) {
			rootCursorInfo = new RootCursorInfo(new JComponent() {
			});
			windowPanels.put(root, rootCursorInfo);
			root.getLayeredPane().add(rootCursorInfo.getComponent());
			root.getLayeredPane().setLayer(rootCursorInfo.getComponent(), JLayeredPane.DRAG_LAYER.intValue() + 10);
			rootCursorInfo.getComponent().setBounds(0, 0, root.getWidth(), root.getHeight());
			root.getLayeredPane().addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					((RootCursorInfo) windowPanels.get(root)).getComponent().setSize(root.getSize());
				}
			});
		}

		if (!rootCursorInfo.isCursorSet()) {
			rootCursorInfo.setCursor(cursor);
			rootCursorInfo.pushCursor(root.isCursorSet() ? root.getCursor() : null);
		}

		if (enabled) {
			root.setCursor(cursor);
			rootCursorInfo.getComponent().setVisible(true);
		}
	}

	public static Cursor getCurrentGlobalCursor(JRootPane root) {
		if (root == null)
			return Cursor.getDefaultCursor();

		RootCursorInfo rootCursorInfo = (RootCursorInfo) windowPanels.get(root);
		return rootCursorInfo == null || !rootCursorInfo.isCursorSet() ? Cursor.getDefaultCursor() : rootCursorInfo.getCursor();
	}

	public static void resetGlobalCursor(JRootPane root) {
		if (root == null)
			return;

		RootCursorInfo rootCursorInfo = (RootCursorInfo) windowPanels.get(root);

		if (rootCursorInfo != null && rootCursorInfo.isCursorSet()) {
			root.setCursor(rootCursorInfo.popCursor());
			rootCursorInfo.getComponent().setVisible(false);
		}
	}

	public static void setEnabled(boolean enabled) {
		CursorManager.enabled = enabled;
	}

	public static boolean isEnabled() {
		return enabled;
	}

	public static JComponent getCursorLayerComponent(JRootPane root) {
		if (root == null)
			return null;

		RootCursorInfo rootCursorInfo = (RootCursorInfo) windowPanels.get(root);
		return rootCursorInfo == null ? null : rootCursorInfo.getComponent();
	}

	public static boolean isGlobalCursorSet(JRootPane root) {
		if (root == null)
			return false;

		RootCursorInfo rootCursorInfo = (RootCursorInfo) windowPanels.get(root);
		return rootCursorInfo != null && rootCursorInfo.isCursorSet();
	}
}
