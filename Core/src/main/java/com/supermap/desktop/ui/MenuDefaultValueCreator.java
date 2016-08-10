package com.supermap.desktop.ui;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IDefaultValueCreator;
import com.supermap.desktop.PluginInfo;

import java.util.ArrayList;

public class MenuDefaultValueCreator implements IDefaultValueCreator {

	private XMLMenus rootParent;

	public MenuDefaultValueCreator(XMLMenus rootParent) {
		this.rootParent = rootParent;
	}

	@Override
	public String getDefaultLabel(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDefaultID(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 新建菜单子项时，需要 id 在对应的控件上不重复
	 * 
	 * @param newMenu
	 * @param contextMenus
	 * @param id
	 * @return
	 */
	public String getDefaultID(XMLMenu newMenu, String id) {
		String result = id;
		try {
			int count = 0;
			ArrayList<XMLMenu> menus = this.rootParent.getMenus();
			while (!isIDEnabled(newMenu, menus, result)) {
				count++;
				result = id + String.valueOf(count);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public Boolean isIDEnabled(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	public Boolean isIDEnabled(XMLMenu newMenu, ArrayList<XMLMenu> menus, String id) {
		Boolean result = true;
		try {
			for (int i = 0; i < menus.size(); i++) {
				XMLMenu menu = menus.get(i);
				if (menu.getIsContainer() && menu.getFormClassName().equalsIgnoreCase(newMenu.getFormClassName()) && menu.getID().equalsIgnoreCase(id)) {
					result = false;
					break;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public int getDefaultIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getDefaultIndex(XMLMenu newMenu) {
		ArrayList<XMLMenu> menus = this.rootParent.getMenus();
		return getDefaultIndex(newMenu, menus, 0);
	}

	private int getDefaultIndex(XMLMenu newMenu, ArrayList<XMLMenu> menus, int index) {
		int result = index;
		try {
			while (!isIndexEnabled(newMenu, menus, result)) {
				result++;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public PluginInfo getDefaultPluginInfo() {
		PluginInfo pluginInfo = null;
		return pluginInfo;
	}

	private PluginInfo getDefaultPluginInfo(ArrayList<PluginInfo> pluginInfos) {
		PluginInfo pluginInfo = null;
		return pluginInfo;
	}

	private Boolean isIndexEnabled(XMLMenu newMenu, ArrayList<XMLMenu> menus, int index) {
		Boolean result = true;
		try {
			for (int i = 0; i < menus.size(); i++) {
				XMLMenu menu = menus.get(i);
				if (menu.getFormClassName().equalsIgnoreCase(newMenu.getFormClassName()) && menu.getIndex() == index) {
					result = false;
					break;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

}
