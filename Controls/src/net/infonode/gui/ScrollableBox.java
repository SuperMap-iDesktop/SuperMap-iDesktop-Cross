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

//$Id: ScrollableBox.java,v 1.27 2005/12/04 13:46:04 jesper Exp $

package net.infonode.gui;

import net.infonode.gui.layout.LayoutUtil;
import net.infonode.gui.panel.SimplePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

public class ScrollableBox extends SimplePanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient LayoutManager layoutManager = new LayoutManager() {
		@Override
		public void addLayoutComponent(String name, Component comp) {
			// do nothing
		}

		@Override
		public void layoutContainer(Container parent) {
			if (parent.getComponentCount() > 0) {
				Component panel = parent.getComponent(0);

				panel.setBounds(0, 0, panel.getPreferredSize().width, panel.getPreferredSize().height);
				panel.validate();
				update();
			}
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			Dimension minDimension = (parent.getComponentCount() == 0) ? new Dimension(0, 0) : parent.getComponent(0).getMinimumSize();

			return LayoutUtil.add(vertical ? new Dimension(minDimension.width, 0) : new Dimension(0, minDimension.height), parent.getInsets());
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return (parent.getComponentCount() == 0) ? new Dimension(0, 0) : parent.getComponent(0).getPreferredSize();
		}

		@Override
		public void removeLayoutComponent(Component comp) {
			// do nothing
		}
	};

	private int leftIndex;
	private boolean vertical;
	private int scrollOffset;

	private boolean leftEnd = true;
	private boolean rightEnd = false;

	private ArrayList layoutOrderList;

	private transient MouseWheelListener mouseWheelListenerTemp = new MouseWheelListener() {
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			setLeftIndex(leftIndex + e.getWheelRotation());
		}
	};

	private ArrayList listeners = new ArrayList(1);

	public ScrollableBox(final JComponent scrollingContainer, boolean vertical, int scrollOffset) {
		setLayout(layoutManager);
		this.vertical = vertical;
		this.scrollOffset = scrollOffset;
		add(scrollingContainer);

		scrollingContainer.addMouseWheelListener(mouseWheelListenerTemp);
		scrollingContainer.addHierarchyListener(new HierarchyListener() {
			@Override
			public void hierarchyChanged(HierarchyEvent e) {
				if (scrollingContainer.getParent() != ScrollableBox.this) {
					scrollingContainer.removeHierarchyListener(this);
					scrollingContainer.removeMouseWheelListener(mouseWheelListenerTemp);
				}
			}
		});
	}

	public void addScrollableBoxListener(ScrollableBoxListener listener) {
		listeners.add(listener);
	}

	public void removeScrollableBoxListener(ScrollableBoxListener listener) {
		listeners.remove(listener);
	}

	public void setScrollingContainer(final JComponent component) {
		// do nothing
	}

	public JComponent getScrollingComponent() {
		return (getComponentCount() == 0) ? null : (JComponent) getComponent(0);
	}

	public void scrollLeft(int numIndex) {
		setLeftIndex(leftIndex - numIndex);
	}

	public void scrollRight(int numIndex) {
		setLeftIndex(leftIndex + numIndex);
	}

	public void ensureVisible(int index) {
		if (leftIndex > index) {
			setLeftIndex(index);
		} else if (leftIndex < index) {
			int newLeftIndex = findFitIndex(index);

			if (newLeftIndex > leftIndex) {
				setLeftIndex(newLeftIndex);
			}
		}
	}

	public boolean isLeftEnd() {
		return leftEnd;
	}

	public boolean isRightEnd() {
		return rightEnd;
	}

	public void setScrollOffset(int scrollOffset) {
		if (scrollOffset != this.scrollOffset) {
			this.scrollOffset = scrollOffset;
			update();
		}
	}

	public int getScrollOffset() {
		return scrollOffset;
	}

	public boolean isVertical() {
		return vertical;
	}

	public void setVertical(boolean vertical) {
		this.vertical = vertical;
		update();
		fireChanged();
	}

	public void setLayoutOrderList(ArrayList layoutOrderList) {
		this.layoutOrderList = layoutOrderList;
	}

	private int getDimensionSize(Dimension dimension) {
		return (int) (vertical ? dimension.getHeight() : dimension.getWidth());
	}

	private Point createPos(int p) {
		return vertical ? new Point(0, p) : new Point(p, 0);
	}

	private int getPos(Point point) {
		return vertical ? point.y : point.x;
	}

	private int getScrollOffset(int index) {
		if (index == 0)
			return 0;

		Component component = getScrollingComponents()[index - 1];
		return Math.min(scrollOffset, Math.max(getDimensionSize(component.getMinimumSize()), getDimensionSize(component.getPreferredSize()) / 2));
	}

	private Component[] getScrollingComponents() {
		JComponent jComponent = getScrollingComponent();

		if (jComponent == null)
			return new Component[0];

		if (layoutOrderList != null) {
			Component[] components = new Component[layoutOrderList.size()];
			for (int i = 0; i < layoutOrderList.size(); i++)
				components[i] = (Component) layoutOrderList.get(i);

			return components;
		}

		return jComponent.getComponents();
	}

	private int getScrollingComponentCount() {
		JComponent jComponentScrolling = getScrollingComponent();

		return (jComponentScrolling == null) ? 0 : jComponentScrolling.getComponentCount();
	}

	private int findFitIndex(int lastIndex) {
		int fitSize = getDimensionSize(getSize());

		if ((fitSize == 0) || (lastIndex < 0)) {
			return 0;
		}

		Component[] components = getScrollingComponents();
		int endPos = getPos(components[lastIndex].getLocation()) + getDimensionSize(components[lastIndex].getSize());

		for (int i = lastIndex; i >= 0; i--) {
			if ((endPos - getPos(components[i].getLocation()) + getScrollOffset(i)) > fitSize) {
				return Math.min(components.length - 1, i + 1);
			}
		}

		return 0;
	}

	private void update() {
		setLeftIndex(leftIndex);
	}

	private void setLeftIndex(int index) {
		JComponent scrollingComponent = getScrollingComponent();

		int oldLeftIndex = leftIndex;

		if (scrollingComponent != null) {
			int count = getScrollingComponentCount();
			int fitIndex = findFitIndex(count - 1);
			leftIndex = Math.min(fitIndex, Math.max(0, index));

			leftEnd = leftIndex == 0;
			rightEnd = !(leftIndex < fitIndex);

			scrollingComponent.setLocation(createPos(((count == 0) ? 0 : (-getPos(getScrollingComponents()[leftIndex].getLocation())))
					+ getScrollOffset(leftIndex)));
			Object[] listenersArray = listeners.toArray();
			for (int i = 0; i < listenersArray.length; i++)
				if (oldLeftIndex < index)
					fireScrolledRight();
				else if (oldLeftIndex > index)
					fireScrolledLeft();
		}
	}
@Override
	public void updateUI() {
		super.updateUI();
		if (listeners != null)
			fireChanged();
	}

	private void fireScrolledLeft() {
		Object[] listenersArray = listeners.toArray();
		for (int i = 0; i < listenersArray.length; i++)
			((ScrollableBoxListener) listenersArray[i]).scrolledLeft(this);
	}

	private void fireScrolledRight() {
		Object[] listenersArray = listeners.toArray();
		for (int i = 0; i < listenersArray.length; i++)
			((ScrollableBoxListener) listenersArray[i]).scrolledRight(this);
	}

	private void fireChanged() {
		Object[] listenersArray = listeners.toArray();
		for (int i = 0; i < listenersArray.length; i++)
			((ScrollableBoxListener) listenersArray[i]).changed(this);
	}
}