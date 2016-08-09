package com.supermap.desktop.ui;

import com.supermap.desktop.Interface.IDefaultValueCreator;
import com.supermap.desktop.PluginInfo;

import java.util.ArrayList;

public class MenuGroupItemDefaultValueCreator implements IDefaultValueCreator {

	private XMLMenuGroup rootParent;

	public MenuGroupItemDefaultValueCreator(XMLMenuGroup rootParent) {
		this.rootParent = rootParent;
	}

	private PluginInfo getDefaultPluginInfo(ArrayList<PluginInfo> pluginInfos) {

		PluginInfo pluginInfo = null;

		return pluginInfo;
	}

	private String getDefaultKey(XMLCommand[] commands, String id) {
		String result = id;
		return result;
	}

	private Boolean isKeyEnabled(XMLCommand[] commands, String id) {
		Boolean result = true;
		for (int i = 0; commands != null && i < commands.length; i++) {
			XMLCommand command = commands[i];
			if (id.equalsIgnoreCase(command.getID())) {
				result = false;
				break;
			}
		}

		return result;
	}

	private int getDefaultIndex(XMLCommand[] commands, int index) {
		int result = index;
		while (!isIndexEnabled(commands, result)) {
			result++;
		}

		return result;
	}

	private Boolean isIndexEnabled(XMLCommand[] commands, int index) {
		Boolean result = true;
		for (int i = 0; i < commands.length; i++) {
			XMLCommand command = commands[i];
			if (index == command.getIndex()) {
				result = false;
				break;
			}
		}

		return result;
	}

	private String getDefaultLabel(XMLCommand[] commands, String text) {
		String result = text;
		int count = 0;
		while (!isTextEnabled(commands, result)) {
			count++;
			result = text + String.valueOf(count);
		}

		return result;
	}

	private Boolean isTextEnabled(XMLCommand[] commands, String text) {
		Boolean result = true;
		for (int i = 0; commands != null && i < commands.length; i++) {
			XMLCommand command = commands[i];
			if (command.getIsContainer() && text.equalsIgnoreCase(command.getLabel())) {
				result = false;
				break;
			}
		}

		return result;
	}

	@Override
	public String getDefaultLabel(String text) {
		XMLCommand[] commands = this.rootParent.items().toArray(new XMLCommand[this.rootParent.items().size()]);
		return getDefaultLabel(commands, text);
	}

	@Override
	public String getDefaultID(String key) {
		XMLCommand[] commands = this.rootParent.items().toArray(new XMLCommand[this.rootParent.items().size()]);
		return getDefaultKey(commands, key);
	}

	@Override
	public Boolean isIDEnabled(String key) {
		XMLCommand[] commands = this.rootParent.items().toArray(new XMLCommand[this.rootParent.items().size()]);
		return isKeyEnabled(commands, key);
	}

	@Override
	public int getDefaultIndex() {
		XMLCommand[] commands = this.rootParent.items().toArray(new XMLCommand[this.rootParent.items().size()]);
		return getDefaultIndex(commands, 0);
	}

	@Override
	public PluginInfo getDefaultPluginInfo() {
		PluginInfo pluginInfo = null;
		return pluginInfo;
	}

}
