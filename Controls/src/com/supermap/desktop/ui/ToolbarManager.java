package com.supermap.desktop.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import com.supermap.desktop.Application;
import com.supermap.desktop.WorkEnvironment;
import com.supermap.desktop.Interface.IToolbar;
import com.supermap.desktop.Interface.IToolbar;
import com.supermap.desktop.Interface.IToolbarManager;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.SmMenu;
import com.supermap.desktop.implement.SmToolbar;
import com.supermap.desktop.ui.XMLButton;
import com.supermap.desktop.ui.XMLComboBox;
import com.supermap.desktop.ui.XMLCommand;
import com.supermap.desktop.ui.XMLLabel;
import com.supermap.desktop.ui.XMLMenu;
import com.supermap.desktop.ui.XMLTextbox;
import com.supermap.desktop.ui.XMLToolbar;

public class ToolbarManager implements IToolbarManager {

	private JPanel toolbarsContainer = null;
	private ArrayList<IToolbar> listToolbars = null;
	private HashMap<WindowType, ArrayList<IToolbar>> childToolbars = null;
	private Container toolbarContainer;

	public ToolbarManager() {
		this.listToolbars = new ArrayList<IToolbar>();
		this.childToolbars = new HashMap<WindowType, ArrayList<IToolbar>>();
		this.toolbarsContainer = new JPanel();
	}

	public JPanel getToolbarsContainer() {
		return this.toolbarsContainer;
	}

	@Override
	public IToolbar get(int index) {
		return this.listToolbars.get(index);
	}

	@Override
	public IToolbar get(String id) {
		IToolbar item = null;
		for (int i = 0; i < this.listToolbars.size(); i++) {
			if (this.listToolbars.get(i).getID().equalsIgnoreCase(id)) {
				item = this.listToolbars.get(i);
				break;
			}
		}
		return item;
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
		return childToolbarsList.size();
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

	public void setToolbarContainer(Container container) {
		this.toolbarContainer = container;
	}

	public boolean load(WorkEnvironment workEnvironment) {
		boolean result = false;
		try {
			// 查找有哪些 Toolbar
			ArrayList<XMLToolbar> xmlToolbars = workEnvironment.getPluginInfos().getToolbars().getToolbars();
			for (int i = 0; i < xmlToolbars.size(); i++) {
				XMLToolbar xmlToolbar = xmlToolbars.get(i);
				SmToolbar toolbar = new SmToolbar(xmlToolbar);
				// 判断一下如果关联的ControlClass为空，就添加到主菜单列表中，否则关联到子窗口菜单列表
				if ("".equals(xmlToolbar.getFormClassName())) {
					this.listToolbars.add(toolbar);
					this.toolbarContainer.add(toolbar, BorderLayout.NORTH);
				} else {
					WindowType windowType = getWindowType(xmlToolbar.getFormClassName());
					ArrayList<IToolbar> childToolbarsList = null;
					if (this.childToolbars.containsKey(windowType)) {
						childToolbarsList = this.childToolbars.get(windowType);
					} else {
						childToolbarsList = new ArrayList<IToolbar>();
						this.childToolbars.put(windowType, childToolbarsList);
					}
					childToolbarsList.add(toolbar);
				}
			}
			result = true;
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	public void saveChange() {
		if (this.toolbarsContainer != null && this.toolbarsContainer.getComponentCount() > 0) {
			for (int i = 0; i < this.toolbarsContainer.getComponentCount(); i++) {
				SmToolbar toolbar = (SmToolbar) this.toolbarsContainer.getComponent(i);
				if (toolbar != null) {
					// 更新 workEnvironment 中 XMLToolbar 的 index
					XMLToolbar xmlToolbar = toolbar.getXMLToolbar();
					xmlToolbar.setIndex(i);
				}
			}

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
							break;
						}
					}
				}
			}
		}
	}

	public boolean loadChildToolbar(WindowType windowType) {
		boolean result = false;
		try {
			ArrayList<IToolbar> childToolbarsList = this.childToolbars.get(windowType);
			if (childToolbarsList != null) {
				for (int i = 0; i < childToolbarsList.size(); i++) {
					SmToolbar toolbar = (SmToolbar) childToolbarsList.get(i);
					this.toolbarContainer.add(toolbar, BorderLayout.NORTH);
					int count = this.toolbarContainer.getComponentCount();
					count = count + 1;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	public boolean removeChildToolbar(WindowType windowType) {
		boolean result = false;
		try {
			int count = this.toolbarContainer.getComponentCount();
			for (int index = count - 1; index >= this.listToolbars.size(); index--) {
				this.toolbarContainer.remove(index);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
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
