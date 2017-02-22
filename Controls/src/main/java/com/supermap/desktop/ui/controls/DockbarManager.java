package com.supermap.desktop.ui.controls;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IDockbarManager;
import com.supermap.desktop.WorkEnvironment;
import com.supermap.desktop.event.DockbarClosedEvent;
import com.supermap.desktop.event.DockbarClosedListener;
import com.supermap.desktop.event.DockbarClosingEvent;
import com.supermap.desktop.event.DockbarClosingListener;
import com.supermap.desktop.ui.*;
import com.supermap.desktop.utilities.StringUtilities;
import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.defaults.DefaultDockingStrategy;
import org.flexdock.dockbar.event.DockableEvent;
import org.flexdock.dockbar.event.DockableListener;
import org.flexdock.view.View;
import org.flexdock.view.Viewport;

import javax.print.Doc;
import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.util.*;

/**
 * SplitWindow 某一部分设置为不可见，只会是内容不可见，SplitWindow 的布局会保持原样，可见的部分不会填充整个 SplitWindow。其他 Dock 风格的 Window 也一样。因此第一个版本不实现动态设置 visible 的功能。后续版本可能会根据需求另外选择合适的 Docking
 * 库，或者自行修改。
 */
public class DockbarManager implements IDockbarManager {

	private static final String WORKSPACE_COMPONENT_MANAGER_ID = "workspaceComponentManager";
	private static final String LAYERS_COMPONENT_MANAGER_ID = "layersComponentManager";
	private static final String OUTPUT_FRAME_ID = "outputFrame";
	private static final String DOCKBAR_MANAGER_PORTID = "dockbarManagerPort";

	private EventListenerList listenerList = new EventListenerList();
	private Viewport dockPort;
	private View mainView;

	private ArrayList<Dockbar> dockbars = new ArrayList<>();
	private ArrayList<Dockbar> leftTopDockbars = new ArrayList<>();
	private ArrayList<Dockbar> leftBottomDockbars = new ArrayList<>();
	private ArrayList<Dockbar> bottomLeftDockbars = new ArrayList<>();
	private ArrayList<Dockbar> bottomRightDockbars = new ArrayList<>();
	private ArrayList<Dockbar> rightTopDockbars = new ArrayList<>();
	private ArrayList<Dockbar> rightBottomDockbars = new ArrayList<>();
	private Dockbar workspaceComponentManager = null;
	private Dockbar layersComponentManager = null;
	private Dockbar outputFrame = null;

	public DockbarManager(JComponent mainContent) {
		DefaultDockingStrategy.keepConstantPercentage(true);
		this.dockPort = new Viewport(DOCKBAR_MANAGER_PORTID);
		this.mainView = new View("mainView", null, null);
		this.mainView.setTerritoryBlocked(DockingConstants.CENTER_REGION, true);
		this.mainView.setTitlebar(null);
		this.mainView.getContentPane().setLayout(new BorderLayout());
		this.mainView.getContentPane().add(mainContent);
	}

	public Component getDockPort() {
		return this.dockPort;
	}

	public IDockbar getWorkspaceComponentManager() {
		return this.workspaceComponentManager;
	}

	public IDockbar getLayersComponentManager() {
		return this.layersComponentManager;
	}

	public IDockbar getOutputFrame() {
		return this.outputFrame;
	}

	@Override
	public IDockbar get(int index) {
		return this.dockbars.get(index);
	}

	@Override
	public IDockbar get(Class<?> controlClass) {
		IDockbar result = null;

		try {
			for (int i = 0; i < this.dockbars.size(); i++) {
				IDockbar dockbar = this.dockbars.get(i);
				if (controlClass == dockbar.getInnerComponent().getClass()) {
					result = dockbar;
					break;
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	@Override
	public int getCount() {
		return this.dockbars.size();
	}

	@Override
	public boolean contains(IDockbar dockbar) {
		return this.dockbars.contains((Dockbar) dockbar);
	}

	@Override
	public void addDockbarClosingListener(DockbarClosingListener listener) {
		this.listenerList.add(DockbarClosingListener.class, listener);
	}

	@Override
	public void removeDockbarClosingListener(DockbarClosingListener listener) {
		this.listenerList.remove(DockbarClosingListener.class, listener);
	}

	@Override
	public void addDockbarClosedListener(DockbarClosedListener listener) {
		this.listenerList.add(DockbarClosedListener.class, listener);
	}

	@Override
	public void removeDockbarClosedListener(DockbarClosedListener listener) {
		this.listenerList.remove(DockbarClosedListener.class, listener);
	}

	private void fireDockbarClosing(DockbarClosingEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == DockbarClosingListener.class) {
				((DockbarClosingListener) listeners[i + 1]).dockbarClosing(e);
			}
		}
	}

	private void fireDockbarClosed(DockbarClosedEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == DockbarClosedListener.class) {
				((DockbarClosedListener) listeners[i + 1]).dockbarClosed(e);
			}
		}
	}

	public boolean add(IDockbar dockbar) {
		return this.dockbars.add((Dockbar) dockbar);
	}

	public IDockbar remove(int index) {
		return this.dockbars.remove(index);
	}

	public boolean remove(IDockbar dockbar) {
		return this.dockbars.remove(dockbar);
	}

	public void removeAll() {
		this.dockbars.clear();
	}

	public boolean load(WorkEnvironment workEnvironment) {
		this.dockPort.dock(this.mainView);
		XMLDockbars dockbars = workEnvironment.getPluginInfos().getDockbars();
		for (int i = 0; i < dockbars.size(); i++) {
			XMLDockbar xmlDockbar = dockbars.get(i);
			Dockbar dockbar = new Dockbar(xmlDockbar);
			this.dockbars.add(dockbar);

			if (StringUtilities.stringEquals("left", xmlDockbar.getDockDirection(), true) || StringUtilities.stringEquals("leftTop", xmlDockbar.getDockDirection(), true)) {
				this.leftTopDockbars.add(dockbar);
			} else if (StringUtilities.stringEquals("leftBottom", xmlDockbar.getDockDirection(), true)) {
				this.leftBottomDockbars.add(dockbar);
			} else if (StringUtilities.stringEquals("bottom", xmlDockbar.getDockDirection(), true) || StringUtilities.stringEquals("bottomLeft", xmlDockbar.getDockDirection(), true)) {
				this.bottomLeftDockbars.add(dockbar);
			} else if (StringUtilities.stringEquals("bottomRight", xmlDockbar.getDockDirection(), true)) {
				this.bottomRightDockbars.add(dockbar);
			} else if (StringUtilities.stringEquals("right", xmlDockbar.getDockDirection(), true) || StringUtilities.stringEquals("rightTop", xmlDockbar.getDockDirection(), true)) {
				this.rightTopDockbars.add(dockbar);
			} else if (StringUtilities.stringEquals("rightBottom", xmlDockbar.getDockDirection(), true)) {
				this.rightBottomDockbars.add(dockbar);
			} else {
				this.rightTopDockbars.add(dockbar);
			}

			if (dockbar.getID().equalsIgnoreCase(WORKSPACE_COMPONENT_MANAGER_ID)) {
				this.workspaceComponentManager = dockbar;
			} else if (dockbar.getID().equalsIgnoreCase(LAYERS_COMPONENT_MANAGER_ID)) {
				this.layersComponentManager = dockbar;
			} else if (dockbar.getID().equalsIgnoreCase(OUTPUT_FRAME_ID)) {
				this.outputFrame = dockbar;
			} else {
				this.dockbars.add(dockbar);
			}
		}

		for (int i = 0; i < this.dockbars.size(); i++) {
			this.dockbars.get(i).initVisible();
		}
		return true;
	}

	public void setVisible(Dockbar dockbar, boolean isVisible) {
		if (isVisible) {
			if (isDisplay(dockbar.getView())) {
				DockingManager.display(dockbar.getView());
				return;
			}

			if (this.leftTopDockbars.contains(dockbar)) {
				displayLeftTop(dockbar);
			} else if (this.leftBottomDockbars.contains(dockbar)) {
				displayLeftBottom(dockbar);
			} else if (this.bottomLeftDockbars.contains(dockbar)) {
				displayBottomLeft(dockbar);
			} else if (this.bottomRightDockbars.contains(dockbar)) {
				displayBottomRight(dockbar);
			} else if (this.rightTopDockbars.contains(dockbar)) {
				displayRightTop(dockbar);
			} else if (this.rightBottomDockbars.contains(dockbar)) {
				displayRightBottom(dockbar);
			} else {
				System.out.println("not register.");
			}

			if (StringUtilities.stringEquals("minimized", dockbar.getDockState(), true)) {
				DockingManager.setMinimized(dockbar.getView(), true, (MainFrame) Application.getActiveApplication().getMainFrame(), getDockConstraint(dockbar.getDockDirection()));
			}
		} else {
			DockingManager.close(dockbar.getView());
		}
	}

	private void displayLeftTop(Dockbar dockbar) {
		Dockable topDocked = findDocked(this.leftTopDockbars);
		Dockable bottomDocked = this.leftBottomDockbars.size() == 0 ? null : findDocked(this.leftBottomDockbars);

		if (topDocked != null) {
			DockingManager.dock(dockbar.getView(), topDocked, DockingConstants.CENTER_REGION);
		} else if (bottomDocked != null) {
			DockingManager.dock(dockbar.getView(), bottomDocked, DockingConstants.NORTH_REGION, 0.5f);
		} else {
			this.mainView.dock(dockbar.getView(), DockingConstants.WEST_REGION, 0.2f);
		}
	}

	private void displayLeftBottom(Dockbar dockbar) {
		Dockable bottomDocked = findDocked(this.leftBottomDockbars);
		Dockable topDocked = this.leftTopDockbars.size() == 0 ? null : findDocked(this.leftTopDockbars);

		if (bottomDocked != null) {
			DockingManager.dock(dockbar.getView(), bottomDocked, DockingConstants.CENTER_REGION);
		} else if (topDocked != null) {
			DockingManager.dock(dockbar.getView(), topDocked, DockingConstants.SOUTH_REGION, 0.5f);
		} else {
			this.mainView.dock(dockbar.getView(), DockingConstants.WEST_REGION, 0.2f);
		}
	}

	private void displayBottomLeft(Dockbar dockbar) {
		Dockable leftDocked = findDocked(this.bottomLeftDockbars);
		Dockable rightDocked = this.bottomRightDockbars.size() == 0 ? null : findDocked(this.bottomRightDockbars);

		if (leftDocked != null) {
			DockingManager.dock(dockbar.getView(), leftDocked, DockingConstants.CENTER_REGION);
		} else if (rightDocked != null) {
			DockingManager.dock(dockbar.getView(), rightDocked, DockingConstants.WEST_REGION, 0.5f);
		} else {
			this.mainView.dock(dockbar.getView(), DockingConstants.SOUTH_REGION, 0.7f);
		}
	}

	private void displayBottomRight(Dockbar dockbar) {
		Dockable rightDocked = findDocked(this.bottomRightDockbars);
		Dockable leftDocked = this.bottomLeftDockbars.size() == 0 ? null : findDocked(this.bottomLeftDockbars);

		if (rightDocked != null) {
			DockingManager.dock(dockbar.getView(), rightDocked, DockingConstants.CENTER_REGION);
		} else if (leftDocked != null) {
			DockingManager.dock(dockbar.getView(), leftDocked, DockingConstants.EAST_REGION, 0.5f);
		} else {
			this.mainView.dock(dockbar.getView(), DockingConstants.SOUTH_REGION, 0.7f);
		}
	}

	private void displayRightTop(Dockbar dockbar) {
		Dockable topDocked = findDocked(this.rightTopDockbars);
		Dockable bottomDocked = this.rightBottomDockbars.size() == 0 ? null : findDocked(this.rightBottomDockbars);

		if (topDocked != null) {
			DockingManager.dock(dockbar.getView(), topDocked, DockingConstants.CENTER_REGION);
		} else if (bottomDocked != null) {
			DockingManager.dock(dockbar.getView(), bottomDocked, DockingConstants.NORTH_REGION, 0.5f);
		} else {
			this.mainView.dock(dockbar.getView(), DockingConstants.EAST_REGION, 0.7f);
		}
	}

	private void displayRightBottom(Dockbar dockbar) {
		Dockable bottomDocked = findDocked(this.rightBottomDockbars);
		Dockable topDocked = this.rightTopDockbars.size() == 0 ? null : findDocked(this.rightTopDockbars);

		if (bottomDocked != null) {
			DockingManager.dock(dockbar.getView(), bottomDocked, DockingConstants.CENTER_REGION);
		} else if (topDocked != null) {
			DockingManager.dock(dockbar.getView(), topDocked, DockingConstants.SOUTH_REGION, 0.5f);
		} else {
			this.mainView.dock(dockbar.getView(), DockingConstants.EAST_REGION, 0.7f);
		}
	}

	private Dockable findDocked(java.util.List<Dockbar> dockbars) {
		Dockable result = null;

		for (int i = 0; i < dockbars.size(); i++) {
			Dockbar dockbar = dockbars.get(i);
			Dockable dockable = DockingManager.getDockable(dockbar.getID());
			if (dockable != null && DockingManager.isDocked(dockable)) {
				result = dockable;
				break;
			}
		}
		return result;
	}

	private int getDockConstraint(String dockDirection) {
		if (StringUtilities.stringEquals("left", dockDirection, true)
				|| StringUtilities.stringEquals("leftTop", dockDirection, true)
				|| StringUtilities.stringEquals("leftBottom", dockDirection, true)) {
			return DockingConstants.LEFT;
		} else if (StringUtilities.stringEquals("bottom", dockDirection, true)
				|| StringUtilities.stringEquals("bottomLeft", dockDirection, true)
				|| StringUtilities.stringEquals("bottomRight", dockDirection, true)) {
			return DockingConstants.BOTTOM;
		} else {
			return DockingConstants.RIGHT;
		}
	}

	private boolean isDisplay(Dockable dockable) {
		return DockingManager.isDocked(dockable) || DockingManager.isMinimized(dockable);
	}

	public Dockbar findDockbar(Component component) {
		Dockbar dock = null;
		if (this.workspaceComponentManager.getInnerComponent() == component) {
			dock = this.workspaceComponentManager;
		} else if (this.layersComponentManager.getInnerComponent() == component) {
			dock = this.layersComponentManager;
		} else if (this.outputFrame.getInnerComponent() == component) {
			dock = this.outputFrame;
		} else {
			for (int i = 0; i < this.dockbars.size(); i++) {
				if (this.dockbars.get(i).getInnerComponent() == component) {
					dock = this.dockbars.get(i);
					break;
				}
			}
		}
		return dock;
	}

	public IDockbar findDockbar(String id) {
		IDockbar dock = null;
		if (this.workspaceComponentManager.getID().equals(id)) {
			dock = this.workspaceComponentManager;
		} else if (this.layersComponentManager.getID().equals(id)) {
			dock = this.layersComponentManager;
		} else if (this.outputFrame.getID().equals(id)) {
			dock = this.outputFrame;
		} else {
			for (int i = 0; i < this.dockbars.size(); i++) {
				if (this.dockbars.get(i).getID().equals(id)) {
					dock = this.dockbars.get(i);
					break;
				}
			}
		}
		return dock;
	}

	private boolean isComponentVisible(Dockbar dockbar) {
		return dockbar != null && dockbar.isVisible();
	}

	private class DockableHandler implements DockableListener {

		@Override
		public void dockable(DockableEvent e) {
			if (e.getEventType() == DockableEvent.CLOSING) {
				handleClosing(e);
			} else if (e.getEventType() == DockableEvent.CLOSED) {
				handleClosed(e);
			}
		}

		private void handleClosing(DockableEvent e) {
			IDockbar dockbar = findDockbar(e.getId());
			if (dockbar != null) {
				fireDockbarClosing(new DockbarClosingEvent(dockbar));
			}
		}

		private void handleClosed(DockableEvent e) {
			IDockbar dockbar = findDockbar(e.getId());
			if (dockbar != null) {
				fireDockbarClosed(new DockbarClosedEvent(dockbar));
			}
		}
	}
}
