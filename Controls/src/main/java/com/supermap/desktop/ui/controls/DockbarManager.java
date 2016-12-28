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
import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.defaults.DefaultDockingStrategy;
import org.flexdock.dockbar.event.DockableEvent;
import org.flexdock.dockbar.event.DockableListener;
import org.flexdock.view.View;
import org.flexdock.view.Viewport;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.util.ArrayList;

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
	private ArrayList<Dockbar> dockbars = null;
	private Viewport dockPort;
	private View mainView;

	private Dockbar workspaceComponentManager = null;
	private Dockbar layersComponentManager = null;
	private Dockbar outputFrame = null;

	public DockbarManager(JComponent mainContent) {
		DefaultDockingStrategy.keepConstantPercentage(true);
		this.dockbars = new ArrayList<Dockbar>();
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
//		XMLDockbars dockbars = workEnvironment.getPluginInfos().getDockbars();
//		DockConstraint dc = dockbars.getDockConstraint();
//		dock(this.mainView, dc);

		this.dockPort.dock(this.mainView);
		XMLDockbars dockbars = workEnvironment.getPluginInfos().getDockbars();
		for (int i = 0; i < dockbars.size(); i++) {
			XMLDockbar xmlDockbar = dockbars.get(i);
			Dockbar dockbar = new Dockbar(xmlDockbar);

			if (dockbar.getID().equalsIgnoreCase(WORKSPACE_COMPONENT_MANAGER_ID)) {
				this.workspaceComponentManager = dockbar;
			} else if (dockbar.getID().equalsIgnoreCase(LAYERS_COMPONENT_MANAGER_ID)) {
				this.layersComponentManager = dockbar;
			} else if (dockbar.getID().equalsIgnoreCase(OUTPUT_FRAME_ID)) {
				this.outputFrame = dockbar;
			} else {
				this.dockbars.add(dockbar);
			}

			if (xmlDockbar.getVisible()) {
				display(dockbar, true);
			}
		}
//
//		this.mainView.dock(this.workspaceComponentManager.getView(), DockingConstants.WEST_REGION, 0.2f);
//		this.workspaceComponentManager.getView().dock(this.layersComponentManager.getView(), DockingConstants.SOUTH_REGION, 0.5f);
//		this.mainView.dock(this.outputFrame.getView(), DockingConstants.SOUTH_REGION, 0.7f);
//		DockingManager.setMinimized(this.outputFrame.getView(), true, DockingConstants.BOTTOM);
//		for (int i = 0; i < this.dockbars.size(); i++) {
//			Dockbar dockbar = this.dockbars.get(i);
//			if (i == 0) {
//				this.mainView.dock(dockbar.getView(), DockingConstants.EAST_REGION, 0.7f);
//			} else {
//				this.dockbars.get(0).getView().dock(dockbar.getView());
//			}
//			dockbar.setVisible(dockbar);
//		}
		return true;
	}

	public void display(Dockbar dockbar, boolean isDisplay) {
		if (isDisplay) {
			if (isDisplay(dockbar.getView())) {
				DockingManager.display(dockbar.getView());
				return;
			}

			if (dockbar == this.workspaceComponentManager) {
				Dockable layer = DockingManager.getDockable(LAYERS_COMPONENT_MANAGER_ID);
				if (layer != null && DockingManager.isDocked(layer)) {
					layer.dock(this.workspaceComponentManager.getView(), DockingConstants.NORTH_REGION, 0.5f);
				} else {
					this.mainView.dock(this.workspaceComponentManager.getView(), DockingConstants.WEST_REGION, 0.2f);
				}
			} else if (dockbar == this.layersComponentManager) {
				Dockable workspace = DockingManager.getDockable(WORKSPACE_COMPONENT_MANAGER_ID);
				if (workspace != null && DockingManager.isDocked(workspace)) {
					workspace.dock(this.layersComponentManager.getView(), DockingConstants.SOUTH_REGION, 0.5f);
				} else {
					this.mainView.dock(this.layersComponentManager.getView(), DockingConstants.WEST_REGION, 0.2f);
				}
			} else if (dockbar == this.outputFrame) {
				this.mainView.dock(this.outputFrame.getView(), DockingConstants.SOUTH_REGION, 0.7f);
				DockingManager.setMinimized(this.outputFrame.getView(), true, (MainFrame) Application.getActiveApplication().getMainFrame(), DockingConstants.BOTTOM);
			} else {
				Dockable docked = null;
				for (int i = 0; i < this.dockbars.size(); i++) {
					if (DockingManager.isDocked((Dockable) this.dockbars.get(i).getView())) {
						docked = this.dockbars.get(i).getView();
						break;
					}
				}

				if (docked != null) {
					docked.dock(dockbar.getView());
				} else {
					this.mainView.dock(dockbar.getView(), DockingConstants.EAST_REGION, 0.7f);
				}
			}
		} else {
			DockingManager.close(dockbar.getView());
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

	private void dock(View dockParent, DockConstraint dockConstraint) {
		if (dockConstraint.getLeft() != null) {
			DockConstraint left = dockConstraint.getLeft();

			if (left.getDepth() > 0) {
				XMLDockbar[] dockbars = left.getDockbars();

				for (int i = 0; i < dockbars.length; i++) {

				}
			} else {

			}
		}

		if (dockConstraint.getRight() != null) {

		}
//
//		dockConstraint.getTop() != null){
//
//		}
//
//		dockConstraint.getBottom() != null){
//
//		}
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
