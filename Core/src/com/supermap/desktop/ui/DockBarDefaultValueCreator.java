package com.supermap.desktop.ui;

import java.util.ArrayList;

import com.supermap.desktop.Application;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop.Interface.IDefaultValueCreator;

public class DockBarDefaultValueCreator implements IDefaultValueCreator {
	private XMLCommandBase rootParent;

	public XMLCommandBase getRootParent() {
		return rootParent;
	}

	public void setRootParent(XMLCommandBase rootParent) {
		this.rootParent = rootParent;
	}

	public DockBarDefaultValueCreator(XMLCommandBase rootParent) {
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
	 * 新建菜单子项时，需要Key在对应的控件上不重复
	 * 
	 * @param newMenu
	 * @param contextMenus
	 * @param key
	 * @return
	 */
	public String getDefaultID(XMLMenu newMenu, String id) {
		String result = id;
		return result;
	}

	@Override
	public Boolean isIDEnabled(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	public Boolean isIDEnabled(XMLMenu newMenu, ArrayList<XMLMenu> menus, String key) {
		Boolean result = true;
		try {
			for (int i = 0; i < menus.size(); i++) {
				XMLMenu menu = menus.get(i);
				if (menu.getIsContainer() && menu.getFormClassName().equalsIgnoreCase(newMenu.getFormClassName()) && menu.getID().equalsIgnoreCase(key)) {
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

	@Override
	public PluginInfo getDefaultPluginInfo() {
		// TODO Auto-generated method stub
		return null;
	}

}
