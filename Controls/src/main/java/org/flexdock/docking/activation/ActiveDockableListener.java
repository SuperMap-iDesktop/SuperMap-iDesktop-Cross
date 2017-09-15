/*
 * Created on Mar 18, 2005
 */
package org.flexdock.docking.activation;

import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.util.DockingUtility;
import org.flexdock.util.SwingUtility;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;

/**
 * @author Christopher Butler
 */
public class ActiveDockableListener implements DockingConstants, PropertyChangeListener, ChangeListener, AWTEventListener {
	private static final ActiveDockableListener SINGLETON = new ActiveDockableListener();
	private static HashSet PROP_EVENTS = new HashSet();

	static {
		primeImpl();
	}

	public static void prime() {
	}

	private static void primeImpl() {
		PROP_EVENTS.add(PERMANENT_FOCUS_OWNER);
		PROP_EVENTS.add(ACTIVE_WINDOW);

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
				focusManager.addPropertyChangeListener(SINGLETON);
			}
		});

		Toolkit.getDefaultToolkit().addAWTEventListener(SINGLETON, AWTEvent.MOUSE_EVENT_MASK);
	}

	public static ActiveDockableListener getInstance() {
		return SINGLETON;
	}

	private ActiveDockableListener() {
	}

	public void eventDispatched(AWTEvent event) {
		//catch all mousePressed events
		if (event.getID() != MouseEvent.MOUSE_PRESSED)
			return;

		MouseEvent evt = (MouseEvent) event;

		if (evt.getSource() instanceof Component) {
			Component c = (Component) evt.getSource();

			// check to see if the event was targeted at the deepest component at the current
			// mouse loaction
			Container container = c instanceof Container ? (Container) c : null;
			if (container != null && container.getComponentCount() > 1) {
				// if not, find the deepest component
				Point p = evt.getPoint();
				c = SwingUtilities.getDeepestComponentAt(c, p.x, p.y);
			}

			// request activation of the dockable that encloses this component
			ActiveDockableTracker.requestDockableActivation(c);
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String pName = evt.getPropertyName();
		if (!PROP_EVENTS.contains(pName))
			return;

		Component oldVal = SwingUtility.toComponent(evt.getOldValue());
		Component newVal = SwingUtility.toComponent(evt.getNewValue());
		boolean switchTo = newVal != null;

		if (ACTIVE_WINDOW.equals(pName))
			handleWindowChange(evt, oldVal, newVal, switchTo);
		else
			handleFocusChange(evt, oldVal, newVal, switchTo);
	}

	private void handleWindowChange(PropertyChangeEvent evt, Component oldVal, Component newVal, boolean activate) {
		// notify the ActiveDockableTracker of the window change
		ActiveDockableTracker.windowActivated(newVal);

		Component srcComponent = activate ? newVal : oldVal;
		ActiveDockableTracker tracker = ActiveDockableTracker.getTracker(srcComponent);
		if (tracker != null)
			tracker.setActive(activate);
	}

	private void handleFocusChange(PropertyChangeEvent evt, Component oldVal, Component newVal, boolean switchTo) {
		if (!switchTo)
			return;

		if (newVal instanceof JTabbedPane)
			newVal = ((JTabbedPane) newVal).getSelectedComponent();
		activateComponent(newVal);
	}

	private void activateComponent(Component c) {
		Dockable dockable = DockingUtility.getAncestorDockable(c);
		if (dockable == null)
			return;

		ActiveDockableTracker tracker = ActiveDockableTracker.getTracker(dockable.getComponent());
		if (tracker != null) {
			tracker.setActive(dockable);
		}
	}


	public void stateChanged(ChangeEvent e) {
		Object obj = e.getSource();
		if (obj instanceof JTabbedPane) {
			JTabbedPane pane = (JTabbedPane) obj;
			Component c = pane.getSelectedComponent();
			Dockable dockable = DockingManager.getDockable(c);
			if (dockable != null) {
				activateComponent(dockable.getComponent());

				// 在 DefaultDockingPort 里对 Tab 进行 add 操作也会进到这里
				// 如果是动态添加的，导致从一个 View 变成 一个 TabPane，就会因为
				// DefaultDockingPort 的代码实现导致无限循环，先注释掉这个方法，
				// 解决以下问题。
				// 1. 打开工作流面板，创建一个节点，双击节点理应弹出参数面板，但是会进入两个 tab 来回切换的死循环
				// 2. 焦点在一个 dock 上，另一个 dock 是 tabbedPane，直接点击 TabbedPane 的某个 Tab，此时不点击 dock 的标题就无法激活 dock
//				udpateTabChangeFocus(dockable);
			}
		}
	}

	private void udpateTabChangeFocus(final Dockable dockable) {
		KeyboardFocusManager mgr = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		Dockable focusParent = DockingUtility.getAncestorDockable(mgr.getFocusOwner());
		if (focusParent == null || focusParent == dockable)
			return;

		// the current focusParent-dockable is different than the currently active dockable.
		// we'll need to update the focus component
		final Component comp = dockable.getComponent();
		final Component deep = SwingUtilities.getDeepestComponentAt(comp, comp.getWidth() / 2, comp.getHeight() / 2);
		// invokeLater because the new tab may not yet be showing, meaning the enumeration of its
		// focus-cycle will return empty.  the parent dockable in the new tab must be showing.
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				ActiveDockableTracker.focusDockable(deep, dockable, true);
			}
		});

	}

}
