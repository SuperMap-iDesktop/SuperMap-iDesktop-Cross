package com.supermap.desktop.implement;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;

/**
 * @author XiaJT
 */
public class PopupMenuMousePressEventListener implements AWTEventListener {
	private static PopupMenuMousePressEventListener popupMenuMousePressEventListener;

	@Override
	public void eventDispatched(final AWTEvent event) {
		if (!(event instanceof MouseEvent)) {
			// We are interested in MouseEvents only
			return;
		}
		MouseEvent me = (MouseEvent) event;
		final Component src = me.getComponent();
		if (me.getID() == MouseEvent.MOUSE_PRESSED) {//|| me.getID() == MouseEvent.MOUSE_RELEASED
			if (isInPopup(src) ||
					(src instanceof JMenu && ((JMenu) src).isSelected())) {
				return;
			} else {
				try {
					Class<AWTEvent> clazz = AWTEvent.class;
					Field consumed = clazz.getDeclaredField("consumed");
					consumed.setAccessible(true);
					consumed.setBoolean(event, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
//				src.dispatchEvent(event);
			}
		}
	}

	private boolean isInPopup(Component src) {
		return getParentPopupMenu(src) != null;
	}

	private JPopupMenu getParentPopupMenu(Component src) {
		for (Component c = src; c != null; c = c.getParent()) {
			if (c instanceof Applet || c instanceof Window) {
				break;
			} else if (c instanceof JPopupMenu) {
				return ((JPopupMenu) c);
			}
		}
		return null;
	}

	public static PopupMenuMousePressEventListener getInstance() {
		if (popupMenuMousePressEventListener == null) {
			popupMenuMousePressEventListener = new PopupMenuMousePressEventListener();
		}
		return popupMenuMousePressEventListener;
	}
}
