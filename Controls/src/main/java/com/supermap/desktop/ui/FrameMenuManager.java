package com.supermap.desktop.ui;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFrameMenuManager;
import com.supermap.desktop.Interface.IMenu;
import com.supermap.desktop.WorkEnvironment;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.SmMenu;
import com.supermap.desktop.utilities.SystemPropertyUtilities;

import javax.swing.*;
import java.util.ArrayList;
import java.util.EnumMap;

public class FrameMenuManager implements IFrameMenuManager {

	private ArrayList<IMenu> listMenus = null;
	private EnumMap<WindowType, ArrayList<IMenu>> childFrameMenus = null;
	private JMenuBar frameMenuBar;

	public FrameMenuManager() {
		this.listMenus = new ArrayList<IMenu>();
		this.childFrameMenus = new EnumMap<WindowType, ArrayList<IMenu>>(WindowType.class);
	}

	@Override
	public IMenu get(int index) {
		return this.listMenus.get(index);
	}

	@Override
	public IMenu get(String id) {
		IMenu item = null;
		for (int i = 0; i < this.listMenus.size(); i++) {
			if (this.listMenus.get(i).getID().equalsIgnoreCase(id)) {
				item = this.listMenus.get(i);
				break;
			}
		}
		return item;
	}

	@Override
	public int getCount() {
		return this.listMenus.size();
	}

	@Override
	public boolean contains(IMenu item) {
		return this.listMenus.contains(item);
	}

	@Override
	public boolean contains(WindowType windowType, IMenu item) {
		ArrayList<IMenu> childMenus = this.childFrameMenus.get(windowType);
		return childMenus.contains(item);
	}

	@Override
	public IMenu getChildMenu(WindowType windowType, int index) {
		ArrayList<IMenu> childMenus = this.childFrameMenus.get(windowType);
		return childMenus.get(index);
	}

	@Override
	public IMenu getChildMenu(WindowType windowType, String key) {
		ArrayList<IMenu> childMenus = this.childFrameMenus.get(windowType);
		IMenu item = null;
		for (int i = 0; i < childMenus.size(); i++) {
			if (childMenus.get(i).getID().equalsIgnoreCase(key)) {
				item = childMenus.get(i);
				break;
			}
		}
		return item;
	}

	@Override
	public int getChildMenuCount(WindowType windowType) {
		ArrayList<IMenu> childMenus = this.childFrameMenus.get(windowType);
		return childMenus.size();
	}

	public JMenuBar getMenuBar() {
		return this.frameMenuBar;
	}

	public void setMenuBar(JMenuBar menuBar) {
		this.frameMenuBar = menuBar;
	}

	public boolean loadMainMenu(WorkEnvironment workEnvironment) {
		boolean result = false;
		try {
			// 查找有哪些 FrameMenu
			ArrayList<XMLMenu> xmlMenus = workEnvironment.getPluginInfos().getFrameMenus().getMenus();
			for (int i = 0; i < xmlMenus.size(); i++) {
				XMLMenu xmlMenu = xmlMenus.get(i);
				if (SystemPropertyUtilities.isSupportPlatform(xmlMenu.getPlatform())) {
					SmMenu menu = new SmMenu(xmlMenu);
					// 判断一下如果关联的ControlClass为空，就添加到主菜单列表中，否则关联到子窗口菜单列表
					if ("".equals(xmlMenu.getFormClassName())) {
						this.listMenus.add(menu);
						this.frameMenuBar.add(menu);
					} else {
						WindowType windowType = getWindowType(xmlMenu.getFormClassName());
						ArrayList<IMenu> childMenus = null;
						if (this.childFrameMenus.containsKey(windowType)) {
							childMenus = this.childFrameMenus.get(windowType);
						} else {
							childMenus = new ArrayList<IMenu>();
							this.childFrameMenus.put(windowType, childMenus);
						}
						childMenus.add(menu);
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	public boolean loadChildMenu(WindowType windowType) {
		boolean result = false;
		try {
			ArrayList<IMenu> childMenus = this.childFrameMenus.get(windowType);
			if (childMenus != null) {
				for (int i = 0; i < childMenus.size(); i++) {
					SmMenu menu = (SmMenu) childMenus.get(i);
					// modify by huchenpu 20151215
					// 添加的子菜单放到帮助的前面
					if (this.frameMenuBar.getComponentIndex(menu) == -1) {
						this.frameMenuBar.add(menu, this.frameMenuBar.getMenuCount() - 1);
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	public boolean removeChildMenu(WindowType windowType) {
		boolean result = false;

		try {
			ArrayList<IMenu> childMenus = this.childFrameMenus.get(windowType);
			if (childMenus == null) {
				return false;
			}

			for (int i = 0; i < childMenus.size(); i++) {
				IMenu menu = childMenus.get(i);

				if (menu instanceof JMenuItem) {
					int index = this.frameMenuBar.getComponentIndex(((JMenuItem) menu));

					if (index >= 0) {
						this.frameMenuBar.remove(index);
					}
				}
			}
			result = true;
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
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
		} else if ("SuperMap.Desktop.FormTransformation".equalsIgnoreCase(controlCalss)) {
			windowType = WindowType.TRANSFORMATION;
		}

		return windowType;
	}
}
