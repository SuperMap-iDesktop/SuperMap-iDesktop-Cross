package com.supermap.desktop.ui;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IToolbar;
import com.supermap.desktop.Interface.IToolbarManager;
import com.supermap.desktop.WorkEnvironment;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.SmToolbar;
import com.supermap.desktop.implement.ToolBarContainer;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ToolbarManager implements IToolbarManager {

	private JPanel toolbarsContainer = null;
	private ArrayList<IToolbar> listToolbars = null;
	private HashMap<WindowType, ArrayList<IToolbar>> childToolbars = null;

	public ToolbarManager() {
		this.listToolbars = new ArrayList<IToolbar>();
		this.childToolbars = new HashMap<WindowType, ArrayList<IToolbar>>();
		this.toolbarsContainer = new JPanel();
	}

	@Override
	public JPanel getToolbarsContainer() {
		return this.toolbarsContainer;
	}

	@Override
	public IToolbar get(int index) {
		return this.listToolbars.get(index);
	}

	@Override
	public IToolbar get(String id) {
		for (IToolbar listToolbar : this.listToolbars) {
			if (listToolbar.getID().equalsIgnoreCase(id)) {
				return listToolbar;
			}
		}
		for (ArrayList<IToolbar> iToolbars : childToolbars.values()) {
			for (IToolbar iToolbar : iToolbars) {
				if (iToolbar.getID().equalsIgnoreCase(id)) {
					return iToolbar;
				}
			}
		}
		return null;
	}

	@Override
	public int getCount() {
		return this.listToolbars.size();
	}

	@Override
	public boolean contains(IToolbar item) {
		return this.listToolbars.contains(item);
	}

	@Override
	public boolean contains(WindowType windowType, IToolbar item) {
		ArrayList<IToolbar> childToolbarsList = this.childToolbars.get(windowType);
		return childToolbarsList.contains(item);
	}

	@Override
	public IToolbar getChildToolbar(WindowType windowType, int index) {
		ArrayList<IToolbar> childToolbarsList = this.childToolbars.get(windowType);
		return childToolbarsList.get(index);
	}

	@Override
	public IToolbar getChildToolbar(WindowType windowType, String key) {
		ArrayList<IToolbar> childToolbarsList = this.childToolbars.get(windowType);
		IToolbar item = null;
		for (int i = 0; i < childToolbarsList.size(); i++) {
			if (childToolbarsList.get(i).getID().equalsIgnoreCase(key)) {
				item = childToolbarsList.get(i);
				break;
			}
		}
		return item;
	}

	@Override
	public int getChildToolbarCount(WindowType windowType) {
		ArrayList<IToolbar> childToolbarsList = this.childToolbars.get(windowType);
		return childToolbarsList == null ? 0 : childToolbarsList.size();
	}

	@Override
	public void update() {
		reLayoutToolBarContainer();
	}

	public void add(IToolbar item) {
		this.listToolbars.add(item);
	}

	public void removeAt(int index) {
		this.listToolbars.remove(index);
	}

	public void removeAll() {
		this.listToolbars.clear();
	}

//	public void setToolbarContainer(Container container) {
//		this.toolbarContainer = container;
//		toolbarContainer
//	}

	public boolean load(WorkEnvironment workEnvironment) {
		// 查找有哪些 Toolbar
		ArrayList<XMLToolbar> xmlToolbars = workEnvironment.getPluginInfos().getToolbars().getToolbars();
		for (int i = 0; i < xmlToolbars.size(); i++) {
			XMLToolbar xmlToolbar = xmlToolbars.get(i);
			SmToolbar toolbar = new SmToolbar(xmlToolbar);
			// 判断一下如果关联的ControlClass为空，就添加到主菜单列表中，否则关联到子窗口菜单列表
			if ("".equals(xmlToolbar.getFormClassName())) {
				this.listToolbars.add(toolbar);
				addToolBar(toolbar);
			} else {
				WindowType windowType = getWindowType(xmlToolbar.getFormClassName());
				ArrayList<IToolbar> childToolbarsList;
				if (this.childToolbars.containsKey(windowType)) {
					childToolbarsList = this.childToolbars.get(windowType);
				} else {
					childToolbarsList = new ArrayList<>();
					this.childToolbars.put(windowType, childToolbarsList);
				}
				childToolbarsList.add(toolbar);
			}
		}
		reLayoutToolBarContainer();
		return true;
	}

	public void saveChange() {
		// FIXME: 2016/6/21
		if (this.toolbarsContainer != null && this.toolbarsContainer.getComponentCount() > 0) {
			List<ToolBarContainer> containerS = ToolBarContainer.getContainerS();
			for (ToolBarContainer toolBarContainer : containerS) {
				if (toolBarContainer != null) {
					for (int i = 0; i < toolBarContainer.getComponentCount(); i++) {
						Component component = toolBarContainer.getComponent(i);
						if (component != null && component instanceof SmToolbar) {
							SmToolbar toolbar = (SmToolbar) component;
							// 更新 workEnvironment 中 XMLToolbar 的 index
							XMLToolbar xmlToolbar = toolbar.getXMLToolbar();
							xmlToolbar.setIndex(i);
							xmlToolbar.setRowIndex(toolBarContainer.getIndex());
						}
					}
				}
			}
//			for (int i = 0; i < this.toolbarsContainer.getComponentCount(); i++) {
//				SmToolbar toolbar = (SmToolbar) this.toolbarsContainer.getComponent(i);
//
//			}

			// 同步更新每一个 pluginInfo 所关联的 XMLToolbar 的 index，然后对每一个 pluginInfo 进行保存
			WorkEnvironment currentWorkEnvironment = Application.getActiveApplication().getWorkEnvironmentManager().getActiveWorkEnvironment();
			XMLToolbars totalXMLToolbars = currentWorkEnvironment.getPluginInfos().getToolbars();

			for (int i = 0; i < totalXMLToolbars.getToolbars().size(); i++) {
				XMLToolbar toolbar = totalXMLToolbars.getToolbars().get(i);

				String toolbarID = toolbar.getID();
				if (toolbarID != null && !toolbarID.isEmpty()) {
					for (int j = 0; j < currentWorkEnvironment.getPluginInfos().size(); j++) {

						// 每一个 PluginInfo 中，同一个 ID 的 Toolbar 节点只有一个
						XMLToolbar updateTo = currentWorkEnvironment.getPluginInfos().get(j).getToolbars().getToolbar(toolbarID);
						if (updateTo != null) {
							updateTo.setIndex(toolbar.getIndex());
							updateTo.setRowIndex(toolbar.getRowIndex());
							break;
						}
					}
				}
			}
		}
	}

	public void loadChildToolbar(WindowType windowType) {
		try {
			ArrayList<IToolbar> childToolbarsList = this.childToolbars.get(windowType);
			if (childToolbarsList != null) {
				for (IToolbar aChildToolbarsList : childToolbarsList) {
					SmToolbar toolbar = (SmToolbar) aChildToolbarsList;
					addToolBar(toolbar);
				}
			}
			reLayoutToolBarContainer();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void reLayoutToolBarContainer() {
		int beforeCount = this.toolbarsContainer.getComponentCount();
		this.toolbarsContainer.removeAll();
		this.toolbarsContainer.setLayout(new GridBagLayout());
		List<ToolBarContainer> containerS = ToolBarContainer.getContainerS();
		for (ToolBarContainer toolBarContainer : containerS) {
			this.toolbarsContainer.add(toolBarContainer, new GridBagConstraintsHelper(0, toolBarContainer.getIndex(), 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH));
		}

		if (beforeCount != this.toolbarsContainer.getComponentCount() && toolbarsContainer.getParent() != null) {
			toolbarsContainer.getParent().validate();
			toolbarsContainer.getParent().repaint();
		} else {
			this.toolbarsContainer.validate();
			this.toolbarsContainer.repaint();
		}
	}

	private void addToolBar(SmToolbar toolbar) {
		ToolBarContainer.getToolBarContainer(toolbar.getRowIndex()).add(toolbar);
	}

	public boolean removeChildToolbar(WindowType windowType) {
		boolean result = false;
		ArrayList<IToolbar> childToolbarsList = this.childToolbars.get(windowType);
		if (childToolbarsList != null) {
			for (IToolbar aChildToolbarsList : childToolbarsList) {
				SmToolbar toolbar = (SmToolbar) aChildToolbarsList;
//				toolbar.setVisible(false);
				Window parentWindow = ComponentUIUtilities.getParentWindow(toolbar);
				if (parentWindow != Application.getActiveApplication().getMainFrame()) {
					parentWindow.dispose();
				}
				ToolBarContainer.getToolBarContainer(toolbar.getRowIndex()).remove(toolbar);
			}
		}
		return result;
	}

	private WindowType getWindowType(String controlCalss) {
		WindowType windowType = WindowType.UNKNOWN;
		if ("SuperMap.Desktop.FormMap".equalsIgnoreCase(controlCalss)) {
			windowType = WindowType.MAP;
		} else if ("SuperMap.Desktop.FormScene".equalsIgnoreCase(controlCalss)) {
			windowType = WindowType.SCENE;
		} else if ("SuperMap.Desktop.FormLayout".equalsIgnoreCase(controlCalss)) {
			windowType = WindowType.LAYOUT;
		} else if ("SuperMap.Desktop.FormTabular".equalsIgnoreCase(controlCalss)) {
			windowType = WindowType.TABULAR;
		}

		return windowType;
	}


}
