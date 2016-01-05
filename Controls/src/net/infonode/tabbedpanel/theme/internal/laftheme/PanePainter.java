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

// $Id: PanePainter.java,v 1.11 2005/12/04 13:46:05 jesper Exp $
package net.infonode.tabbedpanel.theme.internal.laftheme;

import net.infonode.util.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

class PanePainter extends JTabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean mouseEntered = false;

	private boolean focusActive = false;

	private boolean useMouseEnterExit = false;

	private transient Direction direction;

	PanePainter(Direction d) {
		setTabPlacement(d);
		setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);

		useMouseEnterExit = UIManager.getLookAndFeel().getClass().getName().indexOf(".LiquidLookAndFeel") > -1;

		if (!useMouseEnterExit)
			super.processMouseEvent(new MouseEvent(this, MouseEvent.MOUSE_ENTERED, System.currentTimeMillis(), 0, 0, 0, 0, false));
	}

	void setTabAreaEntered(boolean entered) {
		if (entered)
			super.processMouseEvent(new MouseEvent(this, MouseEvent.MOUSE_ENTERED, System.currentTimeMillis(), 0, 0, 0, 0, false));
		else
			super.processMouseEvent(new MouseEvent(this, MouseEvent.MOUSE_EXITED, System.currentTimeMillis(), 0, -1, -1, 0, false));
	}

	private void setTabPlacement(Direction d) {
		this.direction = d;

		if (d == Direction.UP)
			setTabPlacement(JTabbedPane.TOP);
		else if (d == Direction.LEFT)
			setTabPlacement(JTabbedPane.LEFT);
		else if (d == Direction.RIGHT)
			setTabPlacement(JTabbedPane.RIGHT);
		else
			setTabPlacement(JTabbedPane.BOTTOM);
	}

	void setMouseEntered(boolean entered) {
		if (useMouseEnterExit) {
			if (entered && !mouseEntered) {
				super.processMouseEvent(new MouseEvent(this, MouseEvent.MOUSE_ENTERED, System.currentTimeMillis(), 0, 0, 0, 0, false));
			} else if (!entered && mouseEntered) {
				super.processMouseEvent(new MouseEvent(this, MouseEvent.MOUSE_EXITED, System.currentTimeMillis(), 0, -1, -1, 0, false));
			}
		} else {
			if (!entered && mouseEntered) {
				super.processMouseMotionEvent(new MouseEvent(this, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, -1, -1, 0, false));
			}
		}

		mouseEntered = entered;
	}

	void setHoveredTab(int index) {
		if (index > -1 && index < getTabCount()) {
			Rectangle hoverBounds = getBoundsAt(index);
			int xPos = hoverBounds.x + hoverBounds.width / 2;
			int yPos = hoverBounds.y + hoverBounds.height / 2;

			super.processMouseMotionEvent(new MouseEvent(this, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, xPos, yPos, 0, false));
		}
	}

	void setFocusActive(boolean active) {
		if (active && !focusActive)
			super.processFocusEvent(new FocusEvent(this, FocusEvent.FOCUS_GAINED));
		else if (!active && focusActive)
			super.processFocusEvent(new FocusEvent(this, FocusEvent.FOCUS_LOST));

		focusActive = active;
	}

	Direction getDirection() {
		return direction;
	}

	void doValidation() {
		Component c = this;
		while (c != null) {
			c.invalidate();
			c = c.getParent();
		}
		validate();
	}

	void removeAllTabs() {
		removeAll();
		doValidation();
	}

	@Override
	public Font getFont() {
		Font font = UIManager.getFont("TabbedPane.font");
		return font == null ? super.getFont() : font;
	}

	@Override
	public void updateUI() {
		setBorder(null);
		setBackground(null);
		setForeground(null);
		setOpaque(false);

		super.updateUI();

		setTabLayoutPolicy(WRAP_TAB_LAYOUT);

		useMouseEnterExit = UIManager.getLookAndFeel().getClass().getName().indexOf(".LiquidLookAndFeel") > -1;

		if (!useMouseEnterExit)
			super.processMouseEvent(new MouseEvent(this, MouseEvent.MOUSE_ENTERED, System.currentTimeMillis(), 0, 0, 0, 0, false));
	}

	@Override
	public boolean hasFocus() {
		return focusActive;
	}

	@Override
	public void repaint() {
		// do nothing
	}

	@Override
	public void repaint(long tm, int x, int y, int width, int height) {
		// do nothing
	}

	void paint(Graphics g, int tx, int ty) {
		Rectangle clip = g.getClipBounds();

		if (clip != null && clip.x == 0 && clip.y == 0 && clip.width == 0 && clip.height == 0) {
			return;
		}

		g.translate(tx, ty);
		update(g);
		g.translate(-tx, -ty);
	}

	@Override
	protected void processMouseEvent(MouseEvent e) {
		// do nothing
	}

	@Override
	protected void processMouseMotionEvent(MouseEvent e) {
		// do nothing
	}

	@Override
	protected void processFocusEvent(FocusEvent e) {
		// do nothing
	}

	@Override
	protected void processMouseWheelEvent(MouseWheelEvent e) {
		// do nothing
	}
}
