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

// $Id: ComponentUtil.java,v 1.25 2005/12/04 13:46:04 jesper Exp $

package net.infonode.gui;

import net.infonode.gui.componentpainter.ComponentPainter;
import net.infonode.util.Direction;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.util.ArrayList;

public class ComponentUtil {
	private ComponentUtil() {
	}

	public static final Component getChildAt(Container container, Point point) {
		Component component = container.getComponentAt(point);
		return component == null || component.getParent() != container ? null : component;
	}

	public static final Component getVisibleChildAt(Container container, Point point) {
		for (int i = 0; i < container.getComponentCount(); i++) {
			Component component = container.getComponent(i);
			if (component.isVisible() && component.contains(point.x - component.getX(), point.y - component.getY()))
				return component;
		}

		return null;
	}

	public static final Component getChildAtLine(Container container, Point point, boolean horizontal) {
		if (horizontal) {
			for (int i = 0; i < container.getComponentCount(); i++) {
				Component component = container.getComponent(i);
				if (point.x >= component.getX() && point.x < component.getX() + component.getWidth())
					return component;
			}
		} else {
			for (int i = 0; i < container.getComponentCount(); i++) {
				Component component = container.getComponent(i);
				if (point.y >= component.getY() && point.y < component.getY() + component.getHeight())
					return component;
			}
		}

		return null;
	}

	public static void getComponentTreePosition(Component component, ArrayList pos) {
		if (component.getParent() == null) {
			return;
		}

		getComponentTreePosition(component.getParent(), pos);

		pos.add(new Integer(component.getParent().getComponentCount() - ComponentUtil.getComponentIndex(component)));
	}

	public static Component findComponentUnderGlassPaneAt(Point point, Component top) {
		Component component = null;

		if (top.isShowing()) {
			if (top instanceof RootPaneContainer)
				component = ((RootPaneContainer) top).getLayeredPane().findComponentAt(
						SwingUtilities.convertPoint(top, point, ((RootPaneContainer) top).getLayeredPane()));
			else
				component = ((Container) top).findComponentAt(point);
		}

		return component;
	}

	public static final int getComponentIndex(Component component) {
		if (component != null && component.getParent() != null) {
			Container container = component.getParent();
			for (int i = 0; i < container.getComponentCount(); i++) {
				if (container.getComponent(i) == component)
					return i;
			}
		}

		return -1;
	}

	public static final String getBorderLayoutOrientation(Direction direction) {
		return direction == Direction.UP ? BorderLayout.NORTH : direction == Direction.LEFT ? BorderLayout.WEST
				: direction == Direction.DOWN ? BorderLayout.SOUTH : BorderLayout.EAST;
	}

	public static Color getBackgroundColor(Component component) {
		if (component == null)
			return null;

		if (component instanceof BackgroundPainter) {
			ComponentPainter painter = ((BackgroundPainter) component).getComponentPainter();

			if (painter != null) {
				Color color = painter.getColor(component);

				if (color != null)
					return color;
			}
		}

		return component.isOpaque() ? component.getBackground() : getBackgroundColor(component.getParent());
	}

	public static int countComponents(Container container) {
		int num = 1;
		for (int i = 0; i < container.getComponentCount(); i++) {
			Component comp = container.getComponent(i);
			if (comp instanceof Container)
				num += countComponents((Container) comp);
			else
				num++;
		}

		return num;
	}

	public static int getVisibleChildrenCount(Component component) {
		if (component == null || !(component instanceof Container))
			return 0;

		int count = 0;
		Container container = (Container) component;

		for (int i = 0; i < container.getComponentCount(); i++)
			if (container.getComponent(i).isVisible())
				count++;

		return count;
	}

	public static Component getTopLevelAncestor(Component component) {
		Component componentTemp = component;
		while (componentTemp != null) {
			if (componentTemp instanceof Window || componentTemp instanceof Applet)
				break;
			componentTemp = componentTemp.getParent();
		}
		return componentTemp;
	}

	public static boolean hasVisibleChildren(Component component) {
		return getVisibleChildrenCount(component) > 0;
	}

	public static boolean isOnlyVisibleComponent(Component component) {
		return component != null && component.isVisible() && getVisibleChildrenCount(component.getParent()) == 1;
	}

	public static boolean isOnlyVisibleComponents(Component[] components) {
		if (components != null && components.length > 0) {
			boolean visible = getVisibleChildrenCount(components[0].getParent()) == components.length;
			if (visible)
				for (int i = 0; i < components.length; i++)
					visible = visible && components[i].isVisible();
			return visible;
		}
		return false;
	}

	public static Component findFirstComponentOfType(Component component, Class nowClass) {
		if (nowClass.isInstance(component))
			return component;

		if (component instanceof Container) {
			Container container = (Container) component;
			for (int i = 0; i < container.getComponentCount(); i++) {
				Component firstComponent = findFirstComponentOfType(container.getComponent(i), nowClass);
				if (firstComponent != null)
					return firstComponent;
			}
		}
		return null;
	}

	public static boolean isFocusable(Component component) {
		return component.isFocusable() && component.isDisplayable() && component.isVisible() && component.isEnabled();
	}

	/**
	 * Requests focus unless the component already has focus. For some weird reason calling {@link Component#requestFocusInWindow()}when the component is focus
	 * owner changes focus owner to another component!
	 *
	 * @param component the component to request focus for
	 * @return true if the component has focus or probably will get focus, otherwise false
	 */
	public static boolean requestFocus(Component component) {
		return KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == component || component.requestFocusInWindow();
	}

	/**
	 * Requests focus for a component. If that's not possible it's {@link FocusTraversalPolicy}is checked. If that doesn't work all it's children is recursively
	 * checked with this method.
	 *
	 * @param component the component to request focus for
	 * @return the component which has focus or probably will obtain focus, null if no component will receive focus
	 */
	public static Component smartRequestFocus(Component component) {
		Component componentTemp = component;
		if (requestFocus(componentTemp))
			return componentTemp;

		if (componentTemp instanceof JComponent) {
			FocusTraversalPolicy policy = ((JComponent) componentTemp).getFocusTraversalPolicy();

			if (policy != null) {
				Component focusComponent = policy.getDefaultComponent((Container) componentTemp);

				if (focusComponent != null && requestFocus(focusComponent)) {
					return focusComponent;
				}
			}
		}

		if (componentTemp instanceof Container) {
			Component[] children = ((Container) componentTemp).getComponents();

			for (int i = 0; i < children.length; i++) {
				componentTemp = smartRequestFocus(children[i]);

				if (componentTemp != null)
					return componentTemp;
			}
		}

		return null;
	}

	/**
	 * Calculates preferred max height for the given components without checking isVisible.
	 *
	 * @param components Components to check
	 * @return max height
	 */
	public static int getPreferredMaxHeight(Component[] components) {
		int maxHeight = 0;
		for (int i = 0; i < components.length; i++) {
			int componentHeight = (int) components[i].getPreferredSize().getHeight();
			if (componentHeight > maxHeight)
				maxHeight = componentHeight;
		}
		return maxHeight;
	}

	/**
	 * Calculates preferred max width for the given components without checking isVisible.
	 *
	 * @param components Components to check
	 * @return max width
	 */
	public static int getPreferredMaxWidth(Component[] components) {
		int width = 0;
		for (int i = 0; i < components.length; i++) {
			int k = (int) components[i].getPreferredSize().getWidth();
			if (k > width)
				width = k;
		}
		return width;
	}

	public static void setAllOpaque(Container container, boolean opaque) {
		if (container instanceof JComponent) {
			((JComponent) container).setOpaque(opaque);
			for (int i = 0; i < container.getComponentCount(); i++) {
				Component component = container.getComponent(i);
				if (component instanceof Container)
					setAllOpaque((Container) component, opaque);
			}
		}
	}

	public static void validate(JComponent component) {
		component.revalidate();
	}

	public static void validate(Component component) {
		if (component instanceof JComponent)
			((JComponent) component).revalidate();
		else
			component.validate();
	}
}