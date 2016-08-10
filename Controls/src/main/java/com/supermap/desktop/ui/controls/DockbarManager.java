package com.supermap.desktop.ui.controls;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IDockbarManager;
import com.supermap.desktop.WorkEnvironment;
import com.supermap.desktop.enums.DockState;
import com.supermap.desktop.ui.XMLDockbar;
import com.supermap.desktop.ui.XMLDockbarBase;
import com.supermap.desktop.ui.docking.*;
import com.supermap.desktop.ui.docking.properties.RootWindowProperties;
import com.supermap.desktop.ui.docking.theme.DockingWindowsTheme;
import com.supermap.desktop.ui.docking.theme.ShapedGradientDockingTheme;
import com.supermap.desktop.ui.docking.util.ViewMap;

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
	 * Dock 的根容器，在 RootWindow 里才能实现 Dock 特性。
	 */
	private RootWindow rootWindow = null;

	/**
	 * 将 RootWindow 划分为左右两部分区域，左边放置工作空间管理器、图层管理器、子窗口、输出窗口，这些使用 leftSplitWindow 再行划分；右边放置所有 Dock 状态的自定义 Dock 面板。
	 */
	private DockingWindow rootSplitWindow = null;

	/**
	 * 将 rootSplitWindow 划分为左右两部分， 右侧放置 customDockbarsWindow ，显示属性窗口等所有自定义窗口。 左侧放置其他停靠窗体
	 */
	private DockingWindow leftSplitWindow = null;
	private TabWindow customDockbarsWindow = null;

	/**
	 * 将 leftSplitWindow 的左边部分划分为上下两部分区域，上边放置工作空间管理器，下边放置图层管理器。
	 */
	private DockingWindow workspaceAndLayerSplitWindow = null;

	/**
	 * 将 leftSplitWindow 的右边部分划分为上下两部分区域，上边放置子窗口，下边放置输出窗口 。
	 */
	private DockingWindow childFormAndOutputSplitWindow = null;

	/**
	 * 放置所有 Floating 状态的 Dock 面板。
	 */
	private FloatingWindow floatingWindow = null;
	private TabWindow floatingWindowChildrenContainer = null;
	private Dockbar workspaceComponentManager = null;
	private Dockbar layersComponentManager = null;
	private TabWindow childFormsWindow = null;
	private Dockbar outputFrame = null;

	/**
	 * The currently applied docking windows theme
	 */
	private DockingWindowsTheme currentTheme = new ShapedGradientDockingTheme();

	/**
	 * In this properties object the modified property values for close buttons etc. are stored. This object is cleared when the theme is changed.
	 */
	private RootWindowProperties properties = new RootWindowProperties();

	public DockbarManager() {
		this.dockbars = new ArrayList<IDockbar>();
		this.customDockbarsWindow = new TabWindow();
		setTabWindowProperties(this.customDockbarsWindow);
		this.floatingWindowChildrenContainer = new TabWindow();
		setTabWindowProperties(this.floatingWindowChildrenContainer);
	}

	public RootWindow getRootWindow() {
		return this.rootWindow;
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
						this.customDockbarsWindow.addTab(dockbar, dockbar.getIndex());
					} else if (dockbar.getDockState() == DockState.FLOAT) {
						this.floatingWindowChildrenContainer.addTab(dockbar);
					}
				}

				this.dockbars.add(dockbar);
			}
		}

		createRootWindow();
		createFloatingWindow();
		return true;
	}

	public TabWindow getChildFormsWindow() {
		return this.childFormsWindow;
	}

	/**
	 * 设置 TabWindow 右上角各批量操作的按钮不可见
	 * 
	 * @param window
	 */
	public static void setTabWindowProperties(DockingWindow window) {
		if (window instanceof TabWindow) {
			((TabWindow) window).getTabWindowProperties().getUndockButtonProperties().setVisible(false);
			((TabWindow) window).getTabWindowProperties().getCloseButtonProperties().setVisible(false);
			((TabWindow) window).getTabWindowProperties().getDockButtonProperties().setVisible(false);
			((TabWindow) window).getTabWindowProperties().getMaximizeButtonProperties().setVisible(false);
			((TabWindow) window).getTabWindowProperties().getMinimizeButtonProperties().setVisible(false);
			((TabWindow) window).getTabWindowProperties().getRestoreButtonProperties().setVisible(false);
		}
	}

	private void createRootWindow() {
		try {
			createWorkspaceAndLayerSplitWindow();
			createChildFormAndOutputSplitWindow();
			createLeftSplitWindow();
			createRootSplitWindow();

			this.rootWindow = new RootWindow(new ViewMap());
			this.rootWindow.setWindow(this.rootSplitWindow);

			this.properties.addSuperObject(this.currentTheme.getRootWindowProperties());
			this.rootWindow.getRootWindowProperties().addSuperObject(this.properties);

			// 设置可见不可见
			if (!isComponentVisible(this.outputFrame)) {
				this.outputFrame.setVisible(false);
			}

			for (int i = this.customDockbarsWindow.getChildWindowCount() - 1; i >= 0; i--) {
				if (!this.customDockbarsWindow.getChildWindow(i).isVisible()) {
					this.customDockbarsWindow.getChildWindow(i).close();
				}
			}

			rootWindow.addListener(new DockingWindowAdapter() {
				// do nothing
			});
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void createFloatingWindow() {
		if (this.floatingWindow == null) {
			this.floatingWindow = this.rootWindow.createFloatingWindow(DEFAULT_FLOATINGLOCATION, DEFAULT_FLOATINGSIZE, null);
			this.floatingWindowChildrenContainer.setAutoCloseNoChild(false);
			this.floatingWindow.setWindow(this.floatingWindowChildrenContainer);
		}
	}

	private void createRootSplitWindow() {

		this.rootSplitWindow = new SplitWindow(true);
		((SplitWindow) this.rootSplitWindow).setWindows(this.leftSplitWindow, this.customDockbarsWindow);
		((SplitWindow) this.rootSplitWindow).setDividerLocation((float) 0.75);
	}

	private void createLeftSplitWindow() {
		if (this.workspaceAndLayerSplitWindow != null && this.layersComponentManager != null) {
			this.leftSplitWindow = new SplitWindow(true);
			((SplitWindow) this.leftSplitWindow).setWindows(this.workspaceAndLayerSplitWindow, this.childFormAndOutputSplitWindow);
			((SplitWindow) this.leftSplitWindow).setDividerLocation((float) 0.25);
		} else if (this.workspaceAndLayerSplitWindow == null && this.childFormAndOutputSplitWindow != null) {
			this.leftSplitWindow = this.childFormAndOutputSplitWindow;
		}
	}

	private void createWorkspaceAndLayerSplitWindow() {
		if (isComponentVisible(this.workspaceComponentManager) && isComponentVisible(this.layersComponentManager)) {
			this.workspaceAndLayerSplitWindow = new SplitWindow(false);
			((SplitWindow) this.workspaceAndLayerSplitWindow).setWindows(this.workspaceComponentManager, this.layersComponentManager);
			setTabWindowProperties(((SplitWindow) this.workspaceAndLayerSplitWindow).getLeftWindow());
			setTabWindowProperties(((SplitWindow) this.workspaceAndLayerSplitWindow).getRightWindow());
			((SplitWindow) this.workspaceAndLayerSplitWindow).setDividerLocation((float) 0.5);
		} else if (isComponentVisible(this.workspaceComponentManager) && !isComponentVisible(this.layersComponentManager)) {
			this.workspaceAndLayerSplitWindow = this.workspaceComponentManager;
		} else if (!isComponentVisible(this.workspaceComponentManager) && isComponentVisible(this.layersComponentManager)) {
			this.workspaceAndLayerSplitWindow = this.layersComponentManager;
		}
	}

	private void createChildFormAndOutputSplitWindow() {
		this.childFormsWindow = new TabWindow();
		this.childFormsWindow.setAutoCloseNoChild(false);
		this.childFormsWindow.getTabWindowProperties().getCloseButtonProperties().setVisible(false);
		this.childFormsWindow.getTabWindowProperties().getUndockButtonProperties().setVisible(false);
		this.childFormsWindow.getTabWindowProperties().getMaximizeButtonProperties().setVisible(false);

		childFormAndOutputSplitWindow = new SplitWindow(false);
		((SplitWindow) childFormAndOutputSplitWindow).setWindows(this.childFormsWindow, this.outputFrame);
		setTabWindowProperties(((SplitWindow) childFormAndOutputSplitWindow).getRightWindow());
		((SplitWindow) childFormAndOutputSplitWindow).setDividerLocation((float) 0.75);
	}

	private boolean isComponentVisible(Dockbar dockbar) {
		return dockbar != null && dockbar.isVisible();
	}
}
