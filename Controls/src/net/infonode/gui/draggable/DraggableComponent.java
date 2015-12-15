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

// $Id: DraggableComponent.java,v 1.32 2009/02/05 15:57:56 jesper Exp $

package net.infonode.gui.draggable;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import net.infonode.gui.ComponentUtil;
import net.infonode.gui.EventUtil;

public class DraggableComponent {
	private final JComponent component;
	private JComponent[] eventComponents;

	private boolean reorderEnabled = true;
	private boolean enabled = true;
	private boolean reorderRestoreOnDrag;
	private boolean detectOuterAreaAsLine = true;
	private boolean enableInsideDrag;
	private boolean selectOnMousePress;

	private boolean mousePressed;
	private boolean dragEventFired;
	private boolean dragStarted;

	private boolean ignoreAddNotify = false;

	private int dragIndex;
	private int dragFromIndex;
	private int abortDragKeyCode = KeyEvent.VK_ESCAPE;

	private ArrayList layoutOrderList;

	private ArrayList listeners;
	private JComponent outerParentArea;

	private final KeyEventDispatcher abortDragKeyDispatcher = new KeyEventDispatcher() {
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if (mousePressed && e.getKeyCode() == abortDragKeyCode) {
				if (e.getID() == KeyEvent.KEY_PRESSED)
					dragCompleted(null);
				return true;
			}
			return false;
		}
	};

	private final MouseInputListener mouseInputListener = new MouseInputAdapter() {
		@Override
		public void mousePressed(MouseEvent event) {
			pressed(event);
		}

		@Override
		public void mouseReleased(MouseEvent event) {
			released(event);
		}

		@Override
		public void mouseDragged(MouseEvent event) {
			dragged(event);
		}
	};

	public DraggableComponent(JComponent component) {
		this(component, component);
	}

	public DraggableComponent(JComponent component, JComponent eventComponent) {
		this(component, new JComponent[] { eventComponent });
	}

	public DraggableComponent(JComponent component, JComponent[] eventComponents) {
		this.component = component;
		component.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent event) {
				fireChangedEvent(DraggableComponentEvent.TYPE_UNDEFINED);
			}
		});
		setEventComponents(eventComponents);
	}

	public void addListener(DraggableComponentListener l) {
		if (listeners == null)
			listeners = new ArrayList(2);

		listeners.add(l);
	}

	public void removeListener(DraggableComponentListener listener) {
		if (listeners != null) {
			listeners.remove(listener);

			if (listeners.isEmpty())
				listeners = null;
		}
	}

	public JComponent getComponent() {
		return component;
	}

	public JComponent[] getEventComponents() {
		return eventComponents;
	}

	public void setEventComponents(JComponent[] eventComponents) {
		if (this.eventComponents != null) {
			for (int i = 0; i < this.eventComponents.length; i++) {
				this.eventComponents[i].removeMouseListener(mouseInputListener);
				this.eventComponents[i].removeMouseMotionListener(mouseInputListener);
			}
		}

		this.eventComponents = eventComponents;

		if (this.eventComponents != null) {
			for (int i = 0; i < this.eventComponents.length; i++) {
				this.eventComponents[i].addMouseListener(mouseInputListener);
				this.eventComponents[i].addMouseMotionListener(mouseInputListener);
			}
		}
	}

	public int getAbortDragKeyCode() {
		return abortDragKeyCode;
	}

	public void setAbortDragKeyCode(int abortDragKeyCode) {
		this.abortDragKeyCode = abortDragKeyCode;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		if (this.enabled != enabled) {
			this.enabled = enabled;
			fireChangedEvent(enabled ? DraggableComponentEvent.TYPE_ENABLED : DraggableComponentEvent.TYPE_DISABLED);
		}
	}

	public boolean isReorderEnabled() {
		return reorderEnabled;
	}

	public void setReorderEnabled(boolean reorderEnabled) {
		this.reorderEnabled = reorderEnabled;
	}

	public boolean isReorderRestoreOnDrag() {
		return reorderRestoreOnDrag;
	}

	public void setReorderRestoreOnDrag(boolean reorderRestoreOnDrag) {
		this.reorderRestoreOnDrag = reorderRestoreOnDrag;
	}

	public boolean isDetectOuterAreaAsLine() {
		return detectOuterAreaAsLine;
	}

	public void setDetectOuterAreaAsLine(boolean detectOuterAreaAsLine) {
		this.detectOuterAreaAsLine = detectOuterAreaAsLine;
	}

	public boolean isEnableInsideDrag() {
		return enableInsideDrag;
	}

	public void setEnableInsideDrag(boolean enableInsideDrag) {
		this.enableInsideDrag = enableInsideDrag;
	}

	public boolean isSelectOnMousePress() {
		return selectOnMousePress;
	}

	public void setSelectOnMousePress(boolean selectOnMousePress) {
		this.selectOnMousePress = selectOnMousePress;
	}

	public void drag(Point point) {
		if (enabled) {
			dragIndex = getComponentIndex(component);
			dragFromIndex = dragIndex;
			doDrag(point);
		}
	}

	public void abortDrag() {
		if (dragStarted)
			dragCompleted(null);
	}

	public void setLayoutOrderList(ArrayList layoutOrderList) {
		this.layoutOrderList = layoutOrderList;
	}

	public void select() {
		if (enabled)
			fireSelectedEvent();
	}

	public void setOuterParentArea(JComponent outerParentArea) {
		this.outerParentArea = outerParentArea;
	}

	public boolean isIgnoreAddNotify() {
		return ignoreAddNotify;
	}

	public void setIgnoreAddNotify(boolean ignoreAddNotify) {
		this.ignoreAddNotify = ignoreAddNotify;
	}

	private void pressed(MouseEvent event) {
		if (enabled && event.getButton() == MouseEvent.BUTTON1) {
			if (selectOnMousePress && !event.isShiftDown())
				select();
			dragStarted = false;
			KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(abortDragKeyDispatcher);
			mousePressed = true;
			dragIndex = getComponentIndex(component);
			dragFromIndex = dragIndex;

			fireChangedEvent(DraggableComponentEvent.TYPE_PRESSED);
		}
	}

	private void released(MouseEvent event) {
		if (mousePressed) {
			if (event.getButton() == MouseEvent.BUTTON1)
				dragCompleted(event);
			else {
				dragCompleted(null);
				event.consume();
			}
		}
	}

	private void dragged(MouseEvent event) {
		if (enabled && mousePressed) {
			Point point = SwingUtilities.convertPoint((JComponent) event.getSource(), event.getPoint(), component);
			if (dragStarted || enableInsideDrag || !component.contains(point)) {
				if (reorderEnabled)
					doDrag(point);
				else
					dragStarted = true;

				fireDraggedEvent(EventUtil.convert(event, component, point));
			}
		}
	}

	private void dragCompleted(MouseEvent event) {
		mousePressed = false;
		dragStarted = false;

		KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(abortDragKeyDispatcher);

		if (event == null) {
			restoreComponentOrder();
			fireNotDroppedEvent();
		} else if (!checkParentContains(SwingUtilities.convertPoint((JComponent) event.getSource(), event.getPoint(), component.getParent()))) {
			restoreComponentOrder();
			fireDroppedEvent(EventUtil.convert(event, component));
		} else {
			fireDroppedEvent(EventUtil.convert(event, component));
			if (!selectOnMousePress && !event.isShiftDown())
				fireSelectedEvent();
		}

		fireChangedEvent(DraggableComponentEvent.TYPE_RELEASED);
	}

	private void updateParent() {
		if (component.getParent() != null) {
			ComponentUtil.validate(component.getParent());
		}
	}

	private void doDrag(Point point) {
		dragStarted = true;
		JComponent parent = (JComponent) component.getParent();

		if (parent.getComponentCount() == 1)
			return;

		Point point2 = SwingUtilities.convertPoint(component, point, parent);
		int toIndex = getMoveComponentIndex(point2);
		if (toIndex != -1) {
			toIndex = Math.min(toIndex, parent.getComponentCount() - 1);
			Component fromComponent = getComponent(parent, dragIndex);
			int fromDimension;
			int toPos;
			int toDimension;

			if (isVerticalDrag()) {
				fromDimension = fromComponent.getHeight();
				toPos = (int) SwingUtilities.convertPoint(parent, point2, getComponent(parent, toIndex)).getY();
				toDimension = getComponent(parent, toIndex).getHeight();
			} else {
				fromDimension = fromComponent.getWidth();
				toPos = (int) SwingUtilities.convertPoint(parent, point2, getComponent(parent, toIndex)).getX();
				toDimension = getComponent(parent, toIndex).getWidth();
			}

			if ((toIndex > dragIndex && toDimension - toPos > fromDimension) || ((dragIndex == -1 || toIndex < dragIndex) && toPos > fromDimension))
				return;

			if (dragIndex != -1 && dragIndex != toIndex) {
				removeComponent(parent, fromComponent, dragIndex);
				addComponent(parent, fromComponent, toIndex);
				fireChangedEvent(DraggableComponentEvent.TYPE_MOVED);
			}
		}

		if (toIndex < 0) {
			restoreComponentOrder();
		} else
			dragIndex = toIndex;
	}

	private boolean isVerticalDrag() {
		JComponent parent = (JComponent) component.getParent();
		if (parent.getComponentCount() > 1)
			return getComponent(parent, 0).getY() < getComponent(parent, 1).getY();

		return false;
	}

	private boolean checkParentContains(Point point) {
		if (outerParentArea == null)
			return component.getParent().contains(point);

		Point point2 = SwingUtilities.convertPoint(component.getParent(), point, outerParentArea);
		if (detectOuterAreaAsLine) {
			Insets insets = new Insets(0, 0, 0, 0);
			return component.getParent().contains(point)
					|| (outerParentArea.contains(point2) && (isVerticalDrag() ? (point2.getX() >= insets.left && point2.getX() < (outerParentArea.getWidth() - insets.right))
							: (point2.getY() >= insets.top && point2.getY() < (outerParentArea.getHeight() - insets.bottom))));
		}

		return component.getParent().contains(point) || outerParentArea.contains(point2);
	}

	private int getMoveComponentIndex(Point point) {
		JComponent parent = (JComponent) component.getParent();
		if (checkParentContains(point)) {
			boolean vertical = isVerticalDrag();
			for (int i = 0; i < parent.getComponentCount() - 1; i++) {
				Point point2 = getComponent(parent, i + 1).getLocation();

				if (vertical) {
					if (point.getY() >= 0 && point.getY() < point2.getY())
						return i;
				} else {
					if (point.getX() >= 0 && point.getX() < point2.getX())
						return i;
				}
			}

			if (dragIndex == -1)
				return parent.getComponentCount();
			else if (vertical)
				return point.getY() < 0 ? 0 : parent.getComponentCount() - 1;
			else
				return point.getX() < 0 ? 0 : parent.getComponentCount() - 1;
		}

		return -1;
	}

	private JComponent getComponent(Container parent, int index) {
		if (layoutOrderList != null)
			return (JComponent) layoutOrderList.get(index);

		return (JComponent) parent.getComponent(index);
	}

	private int getComponentIndex(Component component) {
		if (layoutOrderList != null)
			return layoutOrderList.indexOf(component);

		return ComponentUtil.getComponentIndex(component);
	}

	private void addComponent(Container parent, Component component, int index) {
		if (layoutOrderList != null) {
			layoutOrderList.add(index, component);
			parent.add(component, index);
		} else
			parent.add(component, index);

		revalidateComponentTree((JComponent) component);
	}

	private void removeComponent(Container parent, Component component, int index) {
		revalidateComponentTree((JComponent) component);

		if (layoutOrderList != null)
			if (index < 0) {
				layoutOrderList.remove(component);
				parent.remove(component);
			} else {
				Component tmp = (Component) layoutOrderList.get(index);
				layoutOrderList.remove(index);
				parent.remove(tmp);
			}
		else if (index < 0)
			parent.remove(component);
		else
			parent.remove(index);
	}

	private void revalidateComponentTree(JComponent jComponent) {
		Container parent = jComponent.getParent();
		int index = ComponentUtil.getComponentIndex(jComponent);
		if (index > 0)
			doRevalidateComponentTree((JComponent) parent.getComponent(index - 1));
		doRevalidateComponentTree(jComponent);
		if (index < parent.getComponentCount() - 1)
			doRevalidateComponentTree((JComponent) parent.getComponent(index + 1));
	}

	private void doRevalidateComponentTree(JComponent jComponent) {
		jComponent.revalidate();
		int count = jComponent.getComponentCount();
		for (int i = 0; i < count; i++)
			doRevalidateComponentTree((JComponent) jComponent.getComponent(i));
	}

	private void restoreComponentOrder() {
		if (reorderEnabled && dragIndex != -1 && dragFromIndex != -1 && dragIndex != dragFromIndex) {
			Container parent = component.getParent();
			Component componentTemp = getComponent(parent, dragIndex);
			removeComponent(parent, componentTemp, -1);
			dragIndex = dragFromIndex;
			addComponent(parent, componentTemp, dragIndex);
			fireChangedEvent(DraggableComponentEvent.TYPE_MOVED);
		}
	}

	private void fireChangedEvent(int type) {
		updateParent();

		if (listeners != null) {
			DraggableComponentEvent event = new DraggableComponentEvent(this, type);
			Object[] listenersList = listeners.toArray();
			for (int i = 0; i < listenersList.length; i++)
				((DraggableComponentListener) listenersList[i]).changed(event);
		}
	}

	private void fireSelectedEvent() {
		updateParent();

		if (listeners != null) {
			DraggableComponentEvent event = new DraggableComponentEvent(this);
			Object[] listenersList = listeners.toArray();
			for (int i = 0; i < listenersList.length; i++)
				((DraggableComponentListener) listenersList[i]).selected(event);
		}
	}

	private void fireDraggedEvent(MouseEvent mouseEvent) {
		dragEventFired = true;
		if (listeners != null) {
			DraggableComponentEvent event = new DraggableComponentEvent(this, mouseEvent);
			Object[] listenersList = listeners.toArray();
			for (int i = 0; i < listenersList.length; i++)
				((DraggableComponentListener) listenersList[i]).dragged(event);
		}
	}

	private void fireDroppedEvent(MouseEvent mouseEvent) {
		updateParent();

		if (dragEventFired) {
			dragEventFired = false;
			if (listeners != null) {
				DraggableComponentEvent event = new DraggableComponentEvent(this, mouseEvent);
				Object[] listenersList = listeners.toArray();
				for (int i = 0; i < listenersList.length; i++)
					((DraggableComponentListener) listenersList[i]).dropped(event);
			}
		}
	}

	private void fireNotDroppedEvent() {
		updateParent();

		if (dragEventFired) {
			dragEventFired = false;
			if (listeners != null) {
				DraggableComponentEvent event = new DraggableComponentEvent(this);
				Object[] listenersList = listeners.toArray();
				for (int i = 0; i < listenersList.length; i++)
					((DraggableComponentListener) listenersList[i]).dragAborted(event);
			}
		}
	}
}