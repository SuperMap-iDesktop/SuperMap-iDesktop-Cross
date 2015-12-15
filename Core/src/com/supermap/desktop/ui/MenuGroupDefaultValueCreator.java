package com.supermap.desktop.ui;

import java.util.ArrayList;

import com.supermap.desktop.Application;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop.Interface.IDefaultValueCreator;

public class MenuGroupDefaultValueCreator implements IDefaultValueCreator {

	private XMLCommandBase rootParent;

	public MenuGroupDefaultValueCreator(XMLCommandBase rootParent) {
		this.rootParent = rootParent;
	}

	private PluginInfo getDefaultPluginInfo(ArrayList<PluginInfo> pluginInfos) {
		PluginInfo pluginInfo = null;
		return pluginInfo;
	}

	private int GetDefaultIndexMethod(ArrayList<XMLMenuGroup> groups, int index) {
		int result = index;
		try {
			while (!isIndexEnabled(groups, result)) {
				result++;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	private Boolean isIndexEnabled(ArrayList<XMLMenuGroup> groups, int index) {
		Boolean result = true;
		try {
			for (int i = 0; i < groups.size(); i++) {
				XMLMenuGroup contextMenuGroup = groups.get(i);
				if (contextMenuGroup.getIndex() == index) {
					result = false;
					break;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	private String GetDefaultKey(ArrayList<XMLMenuGroup> groups, String key) {
		String result = key;
		try {
			int count = 0;
			while (!IsKeyEnabled(groups, result)) {
				count++;
				result = key + String.valueOf(count);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	private Boolean IsKeyEnabled(ArrayList<XMLMenuGroup> groups, String key) {
		Boolean result = true;
		try {
			for (int i = 0; i < groups.size(); i++) {
				XMLMenuGroup group = groups.get(i);
				if (group.getIsContainer() && group.getID().equalsIgnoreCase(key)) {
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
	public String getDefaultLabel(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDefaultID(String key) {

		String result = key;
		return result;
	}

	@Override
	public Boolean isIDEnabled(String key) {
		Boolean result = true;
		try {
			if (this.rootParent instanceof XMLMenu) {
				XMLMenu contextMenu = (XMLMenu) this.rootParent;
				result = IsKeyEnabled(contextMenu.groups(), key);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public int getDefaultIndex() {
		int result = 0;
		try {
			if (this.rootParent instanceof XMLMenu) {
				XMLMenu contextMenu = (XMLMenu) this.rootParent;
				result = GetDefaultIndexMethod(contextMenu.groups(), 0);
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

}
