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

// $Id: ResizablePanel.java,v 1.19 2005/12/04 13:46:04 jesper Exp $
package net.infonode.gui.panel;

import net.infonode.gui.CursorManager;
import net.infonode.util.Direction;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.19 $
 */
public class ResizablePanel extends BaseContainer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Direction direction;
	private int resizeWidth = 4;
	private boolean cursorChanged;
	private int offset = -1;
	private boolean mouseInside;
	private boolean heavyWeight = true;
	private boolean continuousLayout = false;
	private Component dragIndicator;
	private JComponent layeredPane;
	private JComponent innerArea;
	private Dimension lastSize;
	private int dragIndicatorThickness = 4;
	private Component component;

	public ResizablePanel(Direction _direction) {
		this(false, _direction, null);
	}

	public ResizablePanel(boolean useHeavyWeightDragIndicator, Direction _direction, Component mouseListenComponent) {
		super(new BorderLayout());
		this.heavyWeight = useHeavyWeightDragIndicator;
		this.direction = _direction;
		Component mouseListenComponentTemp = mouseListenComponent;
		if (heavyWeight) {
			dragIndicator = new Canvas();
		} else {
			dragIndicator = new BaseContainer();
		}
		setDragIndicatorColor(null);
		if (mouseListenComponentTemp == null)
			mouseListenComponentTemp = this;

		mouseListenComponentTemp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				if (offset == -1)
					resetCursor();

				mouseInside = false;
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				mouseInside = true;
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (!continuousLayout && layeredPane != null) {
					if (layeredPane instanceof JLayeredPane)
						layeredPane.add(dragIndicator, JLayeredPane.DRAG_LAYER);
					else
						layeredPane.add(dragIndicator, 0);

					layeredPane.repaint();
					updateDragIndicator(e);
				}
				if (cursorChanged) {
					offset = direction == Direction.LEFT ? e.getPoint().x : direction == Direction.RIGHT ? getWidth() - e.getPoint().x
							: direction == Direction.UP ? e.getPoint().y : getHeight() - e.getPoint().y;
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (!continuousLayout && layeredPane != null) {
					layeredPane.remove(dragIndicator);
					layeredPane.repaint();
				}
				offset = -1;
				checkCursor(e.getPoint());

				if (!continuousLayout && lastSize != null) {
					setPreferredSize(lastSize);
					revalidate();
				}

				lastSize = null;
			}
		});

		mouseListenComponentTemp.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				checkCursor(e.getPoint());
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				abstractMouseDragged(e);
			}
		});
	}

	private void abstractMouseDragged(MouseEvent e) {
		if (offset != -1) {
			int size = direction.isHorizontal() ? (direction == Direction.LEFT ? getWidth() - e.getPoint().x + offset : e.getPoint().x + offset)
					: (direction == Direction.UP ? getHeight() - e.getPoint().y + offset : e.getPoint().y + offset);
			lastSize = getBoundedSize(size);

			if (continuousLayout) {
				setPreferredSize(lastSize);
				revalidate();
			} else {
				updateDragIndicator(e);
			}
		}
	}

	public void setComponent(Component component) {
		if (this.component != null)
			remove(this.component);

		if (component != null) {
			add(component, BorderLayout.CENTER);
			revalidate();
		}

		this.component = component;
	}

	public void setDragIndicatorColor(Color color) {
		dragIndicator.setBackground(color == null ? Color.DARK_GRAY : color);
	}

	public void setLayeredPane(JComponent layeredPane) {
		this.layeredPane = layeredPane;
		if (innerArea == null)
			innerArea = layeredPane;
	}

	public void setInnerArea(JComponent innerArea) {
		JComponent innerAreaTemp = innerArea;
		if (innerAreaTemp == null)
			innerAreaTemp = layeredPane;
		else
			this.innerArea = innerAreaTemp;
	}

	public boolean isContinuousLayout() {
		return continuousLayout;
	}

	public void setContinuousLayout(boolean continuousLayout) {
		this.continuousLayout = continuousLayout;
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension dimension = super.getPreferredSize();
		return getBoundedSize(direction.isHorizontal() ? dimension.width : dimension.height);
	}

	private void updateDragIndicator(MouseEvent e) {
		if (layeredPane != null) {
			Point point = SwingUtilities.convertPoint((Component) e.getSource(), e.getPoint(), layeredPane);
			Point point2 = SwingUtilities.convertPoint(this.getParent(), getLocation(), layeredPane);
			Dimension size = innerArea.getSize();
			Dimension minimumSize = getMinimumSize();
			Point offsetTemp = SwingUtilities.convertPoint(innerArea, 0, 0, layeredPane);

			if (direction.isHorizontal()) {
				int x = 0;
				if (direction == Direction.LEFT)
					x = Math.min(Math.max(offsetTemp.x, point.x), offsetTemp.x + size.width - minimumSize.width);
				else
					x = Math.min(Math.max(offsetTemp.x + minimumSize.width, point.x), offsetTemp.x + size.width) - dragIndicatorThickness;

				dragIndicator.setBounds(x, point2.y, dragIndicatorThickness, getHeight());
			} else {
				int y = 0;
				if (direction == Direction.UP)
					y = Math.min(Math.max(offsetTemp.y, point.y), offsetTemp.y + size.height - minimumSize.height);
				else
					y = Math.min(Math.max(offsetTemp.y + minimumSize.height, point.y), offsetTemp.y + size.height) - dragIndicatorThickness;

				dragIndicator.setBounds(point2.x, y, getWidth(), dragIndicatorThickness);
			}
		}
	}

	private Dimension getBoundedSize(int size) {
		if (direction.isHorizontal()) {
			return new Dimension(Math.max(getMinimumSize().width, Math.min(size, getMaximumSize().width)), 0);
		} else {
			return new Dimension(0, Math.max(getMinimumSize().height, Math.min(size, getMaximumSize().height)));
		}
	}

	public void setResizeWidth(int width) {
		this.resizeWidth = width;
	}

	public int getResizeWidth() {
		return resizeWidth;
	}

	private void checkCursor(Point point) {
		if (offset != -1)
			return;

		int dist = direction == Direction.UP ? point.y : direction == Direction.DOWN ? getHeight() - point.y : direction == Direction.LEFT ? point.x
				: getWidth() - point.x;

		if (dist >= 0 && dist < resizeWidth && mouseInside) {
			if (!cursorChanged) {
				cursorChanged = true;
				CursorManager.setGlobalCursor(getRootPane(), new Cursor(direction == Direction.LEFT ? Cursor.W_RESIZE_CURSOR
						: direction == Direction.RIGHT ? Cursor.E_RESIZE_CURSOR : direction == Direction.UP ? Cursor.N_RESIZE_CURSOR : Cursor.S_RESIZE_CURSOR));
			}
		} else
			resetCursor();
	}

	private void resetCursor() {
		CursorManager.resetGlobalCursor(getRootPane());
		cursorChanged = false;
	}

	public Direction getDirection() {
		return direction;
	}

	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
	}
}
