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

// $Id: DraggableComponentBox.java,v 1.53 2009/02/05 15:57:56 jesper Exp $

package net.infonode.gui.draggable;

import net.infonode.gui.*;
import net.infonode.gui.layout.DirectionLayout;
import net.infonode.gui.panel.SimplePanel;
import net.infonode.util.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class DraggableComponentBox extends SimplePanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final boolean componentBoxEnabled = true;

	private final JComponent componentBox;
	private JComponent componentContainer;

	private JComponent outerParentArea = this;
	private Direction componentDirection = Direction.UP;
	private boolean scrollEnabled = false;
	private boolean ensureSelectedVisible;
	private boolean autoSelect = true;
	private boolean descendingSortOrder = true;

	private boolean doReverseSort = false;
	private boolean mustSort = false;

	private int scrollOffset;
	private final int iconSize;
	private transient DraggableComponent selectedComponent;
	private transient DraggableComponent topComponent;
	private ArrayList listeners;
	private final ArrayList draggableComponentList = new ArrayList(10);
	private final ArrayList layoutOrderList = new ArrayList(10);

	private ScrollButtonBox scrollButtonBox;

	private boolean useDefaultScrollButtons = true;

	private transient final DraggableComponentListener draggableComponentListener = new DraggableComponentListener() {
		@Override
		public void changed(DraggableComponentEvent event) {
			if (event.getType() == DraggableComponentEvent.TYPE_MOVED) {
				sortComponentList(!descendingSortOrder);
			}
			fireChangedEvent(event);
		}

		@Override
		public void selected(DraggableComponentEvent event) {
			doSelectComponent(event.getSource());
		}

		@Override
		public void dragged(DraggableComponentEvent event) {
			fireDraggedEvent(event);
		}

		@Override
		public void dropped(DraggableComponentEvent event) {
			ensureSelectedVisible();
			fireDroppedEvent(event);
		}

		@Override
		public void dragAborted(DraggableComponentEvent event) {
			ensureSelectedVisible();
			fireNotDroppedEvent(event);
		}
	};

	private int switc;

	public DraggableComponentBox(int iconSize) {
		this(iconSize, true);
	}

	public DraggableComponentBox(int iconSize, boolean useDefaultScrollButtons) {
		this.iconSize = iconSize;
		this.useDefaultScrollButtons = useDefaultScrollButtons;
		// Fix minimum size when flipping direction
		final DirectionLayout layout = new DirectionLayout(componentDirection == Direction.UP ? Direction.RIGHT
				: componentDirection == Direction.LEFT ? Direction.DOWN : componentDirection == Direction.DOWN ? Direction.RIGHT : Direction.DOWN) {
			@Override
			public Dimension minimumLayoutSize(Container parent) {
				Dimension minDimension = super.minimumLayoutSize(parent);
				Dimension prefDimension = super.preferredLayoutSize(parent);
				return componentDirection.isHorizontal() ? new Dimension(prefDimension.width, minDimension.height) : new Dimension(minDimension.width,
						prefDimension.height);
			}

			@Override
			public void layoutContainer(Container parent) {
				if (DraggableComponentBox.this != null && componentBoxEnabled) {
					doSort();
					super.layoutContainer(parent);
				}
			}

			@Override
			public Dimension preferredLayoutSize(Container parent) {
				doSort();
				return super.preferredLayoutSize(parent);
			}
		};

		layout.setLayoutOrderList(layoutOrderList);

		componentBox = new SimplePanel(layout) {
			@Override
			public boolean isOptimizedDrawingEnabled() {
				return DraggableComponentBox.this != null && getComponentSpacing() >= 0;
			}
		};

		componentBox.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				// do nothing
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				fireChangedEvent();
			}
		});

		initialize();
	}

	public void addListener(DraggableComponentBoxListener listener) {
		if (listeners == null)
			listeners = new ArrayList(2);

		listeners.add(listener);
	}

	public void removeListener(DraggableComponentBoxListener listener) {
		if (listeners != null) {
			listeners.remove(listener);

			if (listeners.isEmpty())
				listeners = null;
		}
	}

	public void addDraggableComponent(DraggableComponent component) {
		insertDraggableComponent(component, -1);
	}

	public void insertDraggableComponent(DraggableComponent component, int index) {
		component.setLayoutOrderList(layoutOrderList);

		component.addListener(draggableComponentListener);
		if (index < 0) {
			layoutOrderList.add(component.getComponent());
			componentBox.add(component.getComponent());
		} else {
			layoutOrderList.add(index, component.getComponent());
			componentBox.add(component.getComponent(), index);
		}

		sortComponentList(!descendingSortOrder);

		draggableComponentList.add(component);
		component.setOuterParentArea(outerParentArea);
		componentBox.revalidate();

		fireAddedEvent(component);
		if (autoSelect && layoutOrderList.size() == 1 && selectedComponent == null && component.isEnabled())
			doSelectComponent(component);

		updateScrollButtons();
	}

	private void updateScrollButtons() {
		if (scrollButtonBox != null) {
			ScrollableBox scrollableBox = (ScrollableBox) componentContainer;
			scrollButtonBox.setButton1Enabled(!scrollableBox.isLeftEnd());
			scrollButtonBox.setButton2Enabled(!scrollableBox.isRightEnd());
		}
	}

	public void insertDraggableComponent(DraggableComponent component, Point point) {
		int componentIndex = getComponentIndexAtPoint(point);
		if (componentIndex != -1 && !layoutOrderList.isEmpty())
			insertDraggableComponent(component, componentIndex);
		else
			insertDraggableComponent(component, -1);
	}

	public void selectDraggableComponent(DraggableComponent component) {
		if (component == null) {
			if (selectedComponent != null) {
				DraggableComponent oldSelected = selectedComponent;
				selectedComponent = null;
				fireSelectedEvent(selectedComponent, oldSelected);
			}
		} else
			component.select();
	}

	public void removeDraggableComponent(DraggableComponent component) {
		if (component != null && draggableComponentList.contains(component)) {
			int index = layoutOrderList.indexOf(component.getComponent());
			component.removeListener(draggableComponentListener);
			if (component == topComponent)
				topComponent = null;
			if (layoutOrderList.size() > 1 && selectedComponent != null) {
				if (selectedComponent == component) {
					if (autoSelect) {
						int selectIndex = findSelectableComponentIndex(index);
						if (selectIndex > -1)
							selectDraggableComponent(findDraggableComponent((Component) layoutOrderList.get(selectIndex)));
						else
							selectedComponent = null;
					} else {
						selectDraggableComponent(null);
					}
				}
			} else {
				if (selectedComponent != null) {
					DraggableComponent oldSelected = selectedComponent;
					selectedComponent = null;
					fireSelectedEvent(selectedComponent, oldSelected);
				}
			}
			draggableComponentList.remove(component);
			layoutOrderList.remove(component.getComponent());
			componentBox.remove(component.getComponent());
			componentBox.revalidate();
			component.setLayoutOrderList(null);

			sortComponentList(!descendingSortOrder);

			updateScrollButtons();

			fireRemovedEvent(component);
		}
	}

	public boolean containsDraggableComponent(DraggableComponent component) {
		return draggableComponentList.contains(component);
	}

	public DraggableComponent getSelectedDraggableComponent() {
		return selectedComponent;
	}

	public int getDraggableComponentCount() {
		return layoutOrderList.size();
	}

	public DraggableComponent getDraggableComponentAt(int index) {
		return index < layoutOrderList.size() ? findDraggableComponent((Component) layoutOrderList.get(index)) : null;
	}

	public int getDraggableComponentIndex(DraggableComponent component) {
		return layoutOrderList.indexOf(component.getComponent());
	}

	public Object[] getDraggableComponents() {
		return draggableComponentList.toArray();
	}

	public Component[] getBoxComponents() {
		return componentBox.getComponents();
	}

	public boolean getDepthSortOrder() {
		return descendingSortOrder;
	}

	public void setDepthSortOrder(boolean descending) {
		if (descending != this.descendingSortOrder) {
			this.descendingSortOrder = descending;
			sortComponentList(!descending);
			doSort();
		}
	}

	public boolean isScrollEnabled() {
		return scrollEnabled;
	}

	public void setScrollEnabled(boolean scrollEnabled) {
		if (scrollEnabled != this.scrollEnabled) {
			this.scrollEnabled = scrollEnabled;
			initialize();
		}
	}

	public int getScrollOffset() {
		return scrollOffset;
	}

	public void setScrollOffset(int scrollOffset) {
		if (scrollOffset != this.scrollOffset) {
			this.scrollOffset = scrollOffset;
			if (scrollEnabled)
				((ScrollableBox) componentContainer).setScrollOffset(scrollOffset);
		}
	}

	public int getComponentSpacing() {
		return getDirectionLayout().getComponentSpacing();
	}

	public void setComponentSpacing(int componentSpacing) {
		if (componentSpacing != getDirectionLayout().getComponentSpacing()) {
			if (getComponentSpacing() < 0 && componentSpacing >= 0) {
				DraggableComponent tmp = topComponent;
				sortComponentList(false);
				topComponent = tmp;
			}
			getDirectionLayout().setComponentSpacing(componentSpacing);
			sortComponentList(!descendingSortOrder);
			componentBox.revalidate();
		}
	}

	public boolean isEnsureSelectedVisible() {
		return ensureSelectedVisible;
	}

	public void setEnsureSelectedVisible(boolean ensureSelectedVisible) {
		this.ensureSelectedVisible = ensureSelectedVisible;
	}

	public boolean isAutoSelect() {
		return autoSelect;
	}

	public void setAutoSelect(boolean autoSelect) {
		this.autoSelect = autoSelect;
	}

	public Direction getComponentDirection() {
		return componentDirection;
	}

	public void setComponentDirection(Direction componentDirection) {
		if (componentDirection != this.componentDirection) {
			this.componentDirection = componentDirection;
			getDirectionLayout().setDirection(
					componentDirection == Direction.UP ? Direction.RIGHT : componentDirection == Direction.LEFT ? Direction.DOWN
							: componentDirection == Direction.DOWN ? Direction.RIGHT : Direction.DOWN);
			if (scrollEnabled) {
				scrollButtonBox.setVertical(componentDirection.isHorizontal());
				((ScrollableBox) componentContainer).setVertical(componentDirection.isHorizontal());
			}
		}
	}

	public void setTopComponent(DraggableComponent topComponent) {
		if (topComponent != this.topComponent) {
			this.topComponent = topComponent;

			sortComponentList(!descendingSortOrder);
		}
	}

	public ScrollButtonBox getScrollButtonBox() {
		return scrollButtonBox;
	}

	public JComponent getOuterParentArea() {
		return outerParentArea;
	}

	public void setOuterParentArea(JComponent outerParentArea) {
		this.outerParentArea = outerParentArea;
	}

	public void dragDraggableComponent(DraggableComponent component, Point p) {
		if (draggableComponentList.contains(component)) {
			component.drag(SwingUtilities.convertPoint(this, p, component.getComponent()));
		}

	}

	@Override
	public Dimension getMaximumSize() {
		if (scrollEnabled)
			return getPreferredSize();

		if (componentDirection == Direction.LEFT || componentDirection == Direction.RIGHT)
			return new Dimension((int) getPreferredSize().getWidth(), (int) super.getMaximumSize().getHeight());

		return new Dimension((int) super.getMaximumSize().getWidth(), (int) getPreferredSize().getHeight());

	}

	public Dimension getInnerSize() {
		boolean mustSortTemp = this.mustSort;
		this.mustSort = false;
		Dimension dimension = scrollEnabled ? componentBox.getPreferredSize() : componentBox.getSize();
		this.mustSort = mustSortTemp;
		return dimension;
	}

	public void scrollToVisible(final DraggableComponent draggableComponent) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (scrollEnabled) {
					((ScrollableBox) componentContainer).ensureVisible(layoutOrderList.indexOf(draggableComponent.getComponent()));
				}
			}
		});
	}

	// Prevents focus problems when adding/removing focused component while sorting when spacing < 0
	private void setIgnoreAddRemoveNotify(boolean ignore) {
		for (int i = 0; i < draggableComponentList.size(); i++)
			((DraggableComponent) draggableComponentList.get(i)).setIgnoreAddNotify(ignore);
	}

	private void doSort() {
		if (mustSort && getComponentSpacing() < 0 && componentBox.getComponentCount() > 0) {
			setIgnoreAddRemoveNotify(true);

			mustSort = false;
			Component component;
			Component topComonentTemp = topComponent != null ? topComponent.getComponent() : null;

			int index = 0;
			if (topComonentTemp != null) {
				if (componentBox.getComponent(0) != topComonentTemp) {
					componentBox.remove(topComonentTemp);
					componentBox.add(topComonentTemp, index);
				}
				index++;
			}

			setSwitc(0);
			int size = layoutOrderList.size();
			for (int i = 0; i < size; i++) {
				component = (Component) layoutOrderList.get(doReverseSort ? size - i - 1 : i);
				if (component != topComonentTemp) {
					if (componentBox.getComponent(index) != component) {
						setSwitc(getSwitc() + 1);
						componentBox.remove(component);
						componentBox.add(component, index);
					}
					index++;
				}
			}

			setIgnoreAddRemoveNotify(false);
		}
	}

	private void sortComponentList(boolean reverseSort) {
		this.doReverseSort = reverseSort;
		mustSort = true;
	}

	private int getComponentIndexAtPoint(Point point) {
		JComponent jComponent = null;
		Point point2 = SwingUtilities.convertPoint(this, point, componentBox);
		Point point3 = SwingUtilities.convertPoint(componentBox, point, outerParentArea);
		if (outerParentArea.contains(point3))
			jComponent = (JComponent) ComponentUtil.getChildAtLine(componentBox, point2, getDirectionLayout().getDirection().isHorizontal());

		return layoutOrderList.indexOf(jComponent);
	}

	private void doSelectComponent(DraggableComponent component) {
		if (selectedComponent != null) {
			DraggableComponent oldSelected = selectedComponent;
			selectedComponent = component;
			ensureSelectedVisible();
			fireSelectedEvent(selectedComponent, oldSelected);
		} else {
			selectedComponent = component;
			ensureSelectedVisible();
			fireSelectedEvent(selectedComponent, null);
		}
	}

	private int findSelectableComponentIndex(int index) {
		int selectIndex = -1;
		int k;
		for (int i = 0; i < layoutOrderList.size(); i++) {
			if ((findDraggableComponent((Component) layoutOrderList.get(i))).isEnabled() && i != index) {
				k = selectIndex;
				selectIndex = i;
				if (k < index && selectIndex > index)
					return selectIndex;
				else if (k > index && selectIndex > index)
					return k;
			}
		}

		return selectIndex;
	}

	private DraggableComponent findDraggableComponent(Component component) {
		for (int i = 0; i < draggableComponentList.size(); i++)
			if (((DraggableComponent) draggableComponentList.get(i)).getComponent() == component)
				return (DraggableComponent) draggableComponentList.get(i);

		return null;
	}

	private DirectionLayout getDirectionLayout() {
		return (DirectionLayout) componentBox.getLayout();
	}

	private void initialize() {
		if (componentContainer != null)
			remove(componentContainer);

		DirectionLayout layout = getDirectionLayout();
		layout.setCompressing(!scrollEnabled);

		if (scrollEnabled) {
			if (useDefaultScrollButtons)
				scrollButtonBox = new ScrollButtonBox(componentDirection.isHorizontal(), iconSize);
			else
				scrollButtonBox = new ScrollButtonBox(componentDirection.isHorizontal(), null, null, null, null);

			final ScrollableBox scrollableBox = new ScrollableBox(componentBox, componentDirection.isHorizontal(), scrollOffset);
			scrollableBox.setLayoutOrderList(layoutOrderList);
			scrollButtonBox.addListener(new ScrollButtonBoxListener() {
				@Override
				public void scrollButton1() {
					scrollableBox.scrollLeft(1);
				}

				@Override
				public void scrollButton2() {
					scrollableBox.scrollRight(1);
				}
			});

			scrollableBox.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					scrollButtonBox.setButton1Enabled(!scrollableBox.isLeftEnd());
					scrollButtonBox.setButton2Enabled(!scrollableBox.isRightEnd());
				}
			});

			scrollButtonBox.setButton1Enabled(!scrollableBox.isLeftEnd());
			scrollButtonBox.setButton2Enabled(!scrollableBox.isRightEnd());

			scrollableBox.addScrollableBoxListener(new ScrollableBoxListener() {
				@Override
				public void scrolledLeft(ScrollableBox box) {
					scrollButtonBox.setButton1Enabled(!box.isLeftEnd());
					scrollButtonBox.setButton2Enabled(true);
				}

				@Override
				public void scrolledRight(ScrollableBox box) {
					scrollButtonBox.setButton1Enabled(true);
					scrollButtonBox.setButton2Enabled(!box.isRightEnd());
				}

				@Override
				public void changed(ScrollableBox box) {
					fireChangedEvent();
				}
			});
			componentContainer = scrollableBox;
		} else {
			scrollButtonBox = null;
			componentContainer = componentBox;
		}

		componentContainer.setAlignmentY(0);
		add(componentContainer, BorderLayout.CENTER);

		revalidate();
	}

	private void ensureSelectedVisible() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (scrollEnabled && ensureSelectedVisible && selectedComponent != null) {
					((ScrollableBox) componentContainer).ensureVisible(layoutOrderList.indexOf(selectedComponent.getComponent()));
				}
			}
		});
	}

	private void fireDraggedEvent(DraggableComponentEvent e) {
		if (listeners != null) {
			DraggableComponentBoxEvent event = new DraggableComponentBoxEvent(this, e.getSource(), e, SwingUtilities.convertPoint(e.getSource().getComponent(),
					e.getMouseEvent().getPoint(), this));
			Object[] listenersList = listeners.toArray();
			for (int i = 0; i < listenersList.length; i++)
				((DraggableComponentBoxListener) listenersList[i]).componentDragged(event);
		}
	}

	private void fireDroppedEvent(DraggableComponentEvent e) {
		if (listeners != null) {
			DraggableComponentBoxEvent event = new DraggableComponentBoxEvent(this, e.getSource(), e, SwingUtilities.convertPoint(e.getSource().getComponent(),
					e.getMouseEvent().getPoint(), this));
			Object[] listenersList = listeners.toArray();
			for (int i = 0; i < listenersList.length; i++)
				((DraggableComponentBoxListener) listenersList[i]).componentDropped(event);
		}
	}

	private void fireNotDroppedEvent(DraggableComponentEvent e) {
		if (listeners != null) {
			DraggableComponentBoxEvent event = new DraggableComponentBoxEvent(this, e.getSource(), e);
			Object[] listenersList = listeners.toArray();
			for (int i = 0; i < listenersList.length; i++)
				((DraggableComponentBoxListener) listenersList[i]).componentDragAborted(event);
		}
	}

	private void fireSelectedEvent(DraggableComponent component, DraggableComponent oldDraggableComponent) {
		if (listeners != null) {
			DraggableComponentBoxEvent event = new DraggableComponentBoxEvent(this, component, oldDraggableComponent);
			Object[] listenersList = listeners.toArray();
			for (int i = 0; i < listenersList.length; i++)
				((DraggableComponentBoxListener) listenersList[i]).componentSelected(event);
		}
	}

	private void fireAddedEvent(DraggableComponent component) {
		if (listeners != null) {
			DraggableComponentBoxEvent event = new DraggableComponentBoxEvent(this, component);
			Object[] listenersList = listeners.toArray();
			for (int i = 0; i < listenersList.length; i++)
				((DraggableComponentBoxListener) listenersList[i]).componentAdded(event);
		}
	}

	private void fireRemovedEvent(DraggableComponent component) {
		if (listeners != null) {
			DraggableComponentBoxEvent event = new DraggableComponentBoxEvent(this, component);
			Object[] listenersList = listeners.toArray();
			for (int i = 0; i < listenersList.length; i++)
				((DraggableComponentBoxListener) listenersList[i]).componentRemoved(event);
		}
	}

	private void fireChangedEvent(DraggableComponentEvent e) {
		if (listeners != null) {
			DraggableComponentBoxEvent event = new DraggableComponentBoxEvent(this, e.getSource(), e);
			Object[] listenersList = listeners.toArray();
			for (int i = 0; i < listenersList.length; i++)
				((DraggableComponentBoxListener) listenersList[i]).changed(event);
		}
	}

	private void fireChangedEvent() {
		if (listeners != null) {
			DraggableComponentBoxEvent event = new DraggableComponentBoxEvent(this);
			Object[] listenersList = listeners.toArray();
			for (int i = 0; i < listenersList.length; i++)
				((DraggableComponentBoxListener) listenersList[i]).changed(event);
		}
	}

	public int getSwitc() {
		return switc;
	}

	public void setSwitc(int switc) {
		this.switc = switc;
	}
}