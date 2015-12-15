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

// $Id: HoverManager.java,v 1.16 2008/11/28 10:00:35 jesper Exp $

package net.infonode.gui.hover.hoverable;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import net.infonode.gui.ComponentUtil;
import net.infonode.util.ArrayUtil;

/**
 * @author johan
 */
public class HoverManager {
	private static HoverManager INSTANCE = new HoverManager();

	private final HierarchyListener hierarchyListener = new HierarchyListener() {
		@Override
		public void hierarchyChanged(final HierarchyEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
						if (((Component) e.getSource()).isShowing()) {
							addHoverListeners((Hoverable) e.getSource());
						} else {
							removeHoverListeners((Hoverable) e.getSource());
						}
					}
				}
			});
		}
	};

	private final MouseInputAdapter mouseAdapter = new MouseInputAdapter() {
	};

	private final HashSet hoverableComponents = new HashSet();

	private final ArrayList enteredComponents = new ArrayList();

	private boolean enabled = true;

	private boolean hasPermission = true;

	private boolean active = true;

	private boolean gotEnterAfterExit = false;

	private boolean isDrag = false;

	private final AWTEventListener eventListener = new AWTEventListener() {
		@Override
		public void eventDispatched(final AWTEvent e) {
			if (active) {
				HoverManager.this.eventDispatched(e);
			}
		}
	};

	private void eventDispatched(final AWTEvent e) {
		if (e.getSource() instanceof Component /* Fix for TrayIcon in 1.6. Only handle real components */&& e instanceof MouseEvent) {
			MouseEvent event = (MouseEvent) e;

			if (event.getID() == MouseEvent.MOUSE_PRESSED || event.getID() == MouseEvent.MOUSE_RELEASED) {
				handleButtonEvent(event);
			} else if (event.getID() == MouseEvent.MOUSE_ENTERED || event.getID() == MouseEvent.MOUSE_MOVED) {
				handleEnterEvent(event);
			} else if (event.getID() == MouseEvent.MOUSE_EXITED) {
				handleExitEvent();
			} else if (event.getID() == MouseEvent.MOUSE_DRAGGED) {
				isDrag = true;
			}
		}
	}

	private void handleButtonEvent(MouseEvent event) {
		if (event.getID() == MouseEvent.MOUSE_PRESSED && event.getButton() == MouseEvent.BUTTON1) {
			enabled = false;
			isDrag = false;
		} else if (!enabled && event.getID() == MouseEvent.MOUSE_RELEASED) {
			enabled = true;

			if (isDrag) {
				final Component topComponent = ComponentUtil.getTopLevelAncestor((Component) event.getSource());
				if (topComponent == null)
					exitAll();
				else if (!((Component) event.getSource()).contains(event.getPoint())) {
					final Point point = SwingUtilities.convertPoint((Component) event.getSource(), event.getPoint(), topComponent);
					if (!topComponent.contains(point.x, point.y)) {
						exitAll();
					} else if (topComponent instanceof Container) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {
										Component component = ComponentUtil.findComponentUnderGlassPaneAt(point, topComponent);

										if (component != null) {
											Point point2 = SwingUtilities.convertPoint(topComponent, point, component);
											eventDispatched(new MouseEvent(component, MouseEvent.MOUSE_ENTERED, 0, 0, point2.x, point2.y, 0, false));
										}
									}
								});
							}
						});
					}
				}
			}
		}
	}

	private void handleEnterEvent(MouseEvent event) {
		gotEnterAfterExit = true;

		ArrayList exitables = new ArrayList(enteredComponents);
		ArrayList enterables = new ArrayList();

		Component component = (Component) event.getSource();
		while (component != null) {
			if (hoverableComponents.contains(component)) {
				exitables.remove(component);
				enterables.add(component);
			}

			component = component.getParent();
		}

		if (!enterables.isEmpty()) {
			Object obj[] = enterables.toArray();
			for (int i = obj.length - 1; i >= 0; i--) {
				if (!((Hoverable) obj[i]).acceptHover(enterables)) {
					enterables.remove(obj[i]);
					exitables.add(obj[i]);
				}
			}
		}

		for (int i = exitables.size() - 1; i >= 0; i--) {
			dispatchExit((Hoverable) exitables.get(i));
		}

		for (int i = enterables.size() - 1; i >= 0; i--) {
			dispatchEnter((Hoverable) enterables.get(i));
		}
	}

	private void handleExitEvent() {
		gotEnterAfterExit = false;

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (!gotEnterAfterExit)
					exitAll();
			}
		});
	}

	public static HoverManager getInstance() {
		return INSTANCE;
	}

	private HoverManager() {
		try {
			SecurityManager securityManager = System.getSecurityManager();
			if (securityManager != null)
				securityManager.checkPermission(new AWTPermission("listenToAllAWTEvents"));
		} catch (SecurityException e) {
			hasPermission = false;
		}
	}

	private void exitAll() {
		gotEnterAfterExit = false;
		Object[] obj = enteredComponents.toArray();
		for (int i = obj.length - 1; i >= 0; i--) {
			dispatchExit((Hoverable) obj[i]);
		}
	}

	public void init() {
		gotEnterAfterExit = false;
		isDrag = false;
		enabled = true;
	}

	public void setEventListeningActive(boolean active) {
		this.active = active;
	}

	public void dispatchEvent(MouseEvent event) {
		eventDispatched(event);
	}

	private void addHoverListeners(Hoverable hoverable) {
		if (hoverableComponents.add(hoverable)) {
			Component component = (Component) hoverable;
			component.addMouseListener(mouseAdapter);
			component.addMouseMotionListener(mouseAdapter);

			if (active && hoverableComponents.size() == 1) {
				try {
					Toolkit.getDefaultToolkit().addAWTEventListener(eventListener, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
					hasPermission = true;
				} catch (SecurityException e) {
					hasPermission = false;
				}
			}
		}
	}

	private void removeHoverListeners(Hoverable hoverable) {
		if (hoverableComponents.remove(hoverable)) {
			((Component) hoverable).removeMouseListener(mouseAdapter);
			((Component) hoverable).removeMouseMotionListener(mouseAdapter);
			dispatchExit(hoverable);

			if (hasPermission && hoverableComponents.isEmpty()) {
				Toolkit.getDefaultToolkit().removeAWTEventListener(eventListener);
			}
		}
	}

	public void addHoverable(Hoverable hoverable) {
		if (hoverable instanceof Component) {
			Component component = (Component) hoverable;

			if (ArrayUtil.contains(component.getHierarchyListeners(), hierarchyListener))
				return;

			component.addHierarchyListener(hierarchyListener);

			if (component.isShowing())
				addHoverListeners(hoverable);
		}
	}

	public void removeHoverable(Hoverable hoverable) {
		Component component = (Component) hoverable;
		component.removeHierarchyListener(hierarchyListener);
		removeHoverListeners(hoverable);
	}

	public boolean isHovered(Hoverable hoverable) {
		return enteredComponents.contains(hoverable);
	}

	public boolean isEventListeningActive() {
		return active && hasPermission;
	}

	private void dispatchEnter(Hoverable hoverable) {
		if (enabled && !enteredComponents.contains(hoverable)) {
			enteredComponents.add(hoverable);
			hoverable.hoverEnter();
		}
	}

	private void dispatchExit(Hoverable hoverable) {
		if (enabled && enteredComponents.remove(hoverable))
			hoverable.hoverExit();
	}
}