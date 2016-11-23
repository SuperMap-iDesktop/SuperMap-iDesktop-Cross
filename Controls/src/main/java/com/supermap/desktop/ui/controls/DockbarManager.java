package com.supermap.desktop.ui.controls;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IDockbarManager;
import com.supermap.desktop.WorkEnvironment;
import com.supermap.desktop.enums.DockState;
import com.supermap.desktop.ui.DockConstraint;
import com.supermap.desktop.ui.XMLDockbar;
import com.supermap.desktop.ui.XMLDockbarBase;
import com.supermap.desktop.ui.XMLDockbars;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.view.View;
import org.flexdock.view.Viewport;

import javax.swing.*;
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

	private ArrayList<Dockbar> dockbars = null;
	private Viewport dockPort;
	private View mainView;

	private Dockbar workspaceComponentManager = null;
	private Dockbar layersComponentManager = null;
	private Dockbar outputFrame = null;

	public DockbarManager(JComponent mainContent) {
		this.dockbars = new ArrayList<Dockbar>();
		this.dockPort = new Viewport();
		this.mainView = new View("mainView", null, null);
		this.mainView.setTerritoryBlocked(DockingConstants.CENTER_REGION, true);
		this.mainView.setTitlebar(null);
		this.mainView.getContentPane().setLayout(new BorderLayout());
		this.mainView.getContentPane().add(mainContent);
		this.dockPort.dock(this.mainView);
	}

	public IDockbar getWorkspaceComponentManager() {
		return (IDockbar) DockingManager.getDockable(WORKSPACE_COMPONENT_MANAGER_ID);
	}

	public IDockbar getLayersComponentManager() {
		return (IDockbar) DockingManager.getDockable(LAYERS_COMPONENT_MANAGER_ID);
	}

	public IDockbar getOutputFrame() {
		return (IDockbar) DockingManager.getDockable(OUTPUT_FRAME_ID);
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
		XMLDockbars dockbars = workEnvironment.getPluginInfos().getDockbars();
		for (int i = 0; i < dockbars.size(); i++) {
			XMLDockbar dockbar = dockbars.get(i);

			if (dockbar.getID().equalsIgnoreCase(WORKSPACE_COMPONENT_MANAGER_ID)) {
				this.workspaceComponentManager = new Dockbar(dockbar);
			} else if (dockbar.getID().equalsIgnoreCase(LAYERS_COMPONENT_MANAGER_ID)) {
				this.layersComponentManager = new Dockbar(dockbar);
			} else if (dockbar.getID().equalsIgnoreCase(OUTPUT_FRAME_ID)) {
				this.outputFrame = new Dockbar(dockbar);
			} else {
				this.dockbars.add(new Dockbar(dockbar));
			}
		}

		this.mainView.dock(this.workspaceComponentManager, DockingConstants.EAST_REGION, 0.3f);
		this.workspaceComponentManager.dock(this.layersComponentManager, DockingConstants.SOUTH_REGION, 0.5f);
		for (int i = 0; i < this.dockbars.size(); i++) {
			if (i == 0) {
				this.mainView.dock(this.dockbars.get(i), DockingConstants.WEST_REGION, 0.3f);
			} else {
				this.dockbars.get(0).dock(this.dockbars.get(i));
			}
		}
		this.mainView.dock(this.outputFrame, DockingConstants.SOUTH_REGION, 0.3f);
		return true;
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
}
