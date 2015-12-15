package com.supermap.desktop.ui;

import com.supermap.desktop.Application;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop.enums.XMLCommandType;

public class XMLMenuCommand extends XMLCommand {

	public XMLMenuCommand(PluginInfo pluginInfo, XMLMenuGroup group) {

		super(pluginInfo, group);
		this.commandType = XMLCommandType.COMMAND;

		this.group = group;
	}

	protected XMLMenuGroup group;

	public XMLMenuGroup getGroup() {
		return this.group;
	}

	@Override
	protected boolean doRemove() {
		Boolean result = false;
		try {
			result = ((XMLMenuGroup) this.getParent()).items().remove(this);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}
}
