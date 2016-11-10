package com.supermap.desktop.ui.controls;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IDockbarManager;
import com.supermap.desktop.WorkEnvironment;
import com.supermap.desktop.enums.DockState;
import com.supermap.desktop.ui.XMLDockbar;
import com.supermap.desktop.ui.XMLDockbarBase;

import java.awt.*;
import java.util.ArrayList;

/**
 * SplitWindow 某一部分设置为不可见，只会是内容不可见，SplitWindow 的布局会保持原样，可见的部分不会填充整个 SplitWindow。其他 Dock 风格的 Window 也一样。因此第一个版本不实现动态设置 visible 的功能。后续版本可能会根据需求另外选择合适的 Docking
 * 库，或者自行修改。
 *
 */
public class DockbarManager implements IDockbarManager {
	private static final String WORKSPACE_COMPONENT_MANAGER_NAME = "com.supermap.desktop.ui.WorkspaceComponentManager";
	private static final String LAYERS_COMPONENT_MANAGER_NAME = "com.supermap.desktop.ui.LayersComponentManager";
	private static final String OUTPUT_FRAME_NAME = "com.supermap.desktop.ui.OutputFrame";

	private static final String SUPERMAP_PLUGIN_CONTROLS = "SuperMap.Desktop.Controls";

	private static final Point DEFAULT_FLOATINGLOCATION = new Point(600, 200);
	private static final Dimension DEFAULT_FLOATINGSIZE = new Dimension(400, 300);
	private ArrayList<IDockbar> dockbars = null;

	/**
	 * 放置所有 Floating 状态的 Dock 面板。
	 */
	private Dockbar workspaceComponentManager = null;
	private Dockbar layersComponentManager = null;
	private Dockbar outputFrame = null;

	public DockbarManager() {
		this.dockbars = new ArrayList<IDockbar>();
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
				if (controlClass == dockbar.getComponent().getClass()) {
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
		return this.dockbars.contains(dockbar);
	}

	public boolean add(IDockbar dockbar) {
		return this.dockbars.add(dockbar);
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
		for (int i = 0; i < workEnvironment.getPluginInfos().getDockbars().getDockbars().size(); i++) {
			XMLDockbarBase xmlDockbarBase = workEnvironment.getPluginInfos().getDockbars().get(i);

			if (xmlDockbarBase instanceof XMLDockbar) {
				Dockbar dockbar = new Dockbar((XMLDockbar) xmlDockbarBase);
				if (dockbar.getPluginInfo().getBundleName().equalsIgnoreCase(SUPERMAP_PLUGIN_CONTROLS)
						&& dockbar.getComponentName().equalsIgnoreCase(WORKSPACE_COMPONENT_MANAGER_NAME)) {
					this.workspaceComponentManager = dockbar;
				} else if (dockbar.getPluginInfo().getBundleName().equalsIgnoreCase(SUPERMAP_PLUGIN_CONTROLS)
						&& dockbar.getComponentName().equalsIgnoreCase(LAYERS_COMPONENT_MANAGER_NAME)) {
					this.layersComponentManager = dockbar;
				} else if (dockbar.getPluginInfo().getBundleName().equalsIgnoreCase(SUPERMAP_PLUGIN_CONTROLS)
						&& dockbar.getComponentName().equalsIgnoreCase(OUTPUT_FRAME_NAME)) {
					this.outputFrame = dockbar;
				} else {
					if (dockbar.getDockState() == DockState.DOCK) {
//						this.customDockbarsWindow.addTab(dockbar, dockbar.getIndex());
					} else if (dockbar.getDockState() == DockState.FLOAT) {
//						this.floatingWindowChildrenContainer.addTab(dockbar);
					}
				}

				this.dockbars.add(dockbar);
			}
		}

		return true;
	}

	private boolean isComponentVisible(Dockbar dockbar) {
		return dockbar != null && dockbar.isVisible();
	}
}
