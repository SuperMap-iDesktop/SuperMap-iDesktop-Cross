package com.supermap.desktop.ui;

import org.w3c.dom.Element;

import com.supermap.desktop.Application;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop.enums.XMLCommandType;

public class XMLComboBox extends XMLCommand {

	private XMLCommand parentTemp;

	@Override
	public XMLCommand getParent() {
		return parentTemp;
	}

	public void setParent(XMLCommand parent) {
		this.parentTemp = parent;
	}

	public XMLComboBox(PluginInfo pluginInfo, XMLCommand parent) {
		super(pluginInfo);
		this.parentTemp = parent;
		this.commandType = XMLCommandType.COMBOBOX;
	}

	@Override
	public boolean getIsContainer() {
		return false;
	}

	@Override
	public boolean initialize(Element xmlNodeCommand) {
		boolean result = false;
		try {
			result = super.initialize(xmlNodeCommand);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public XMLCommandBase clone(XMLCommandBase parent) {
		XMLComboBox result = null;
		try {
			result = (XMLComboBox) super.clone(parent);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public XMLCommandBase saveToPluginInfo(PluginInfo pluginInfo, XMLCommandBase parent) {
		XMLComboBox result = null;
		try {
			result = (XMLComboBox) super.saveToPluginInfo(pluginInfo, parent);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}
}
