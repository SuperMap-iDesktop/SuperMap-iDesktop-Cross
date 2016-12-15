package com.supermap.desktop.ui;

import com.supermap.desktop.Application;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop._XMLTag;
import org.w3c.dom.Element;

public class XMLCommandFactory {
	private XMLCommandFactory() {
		// 工具类不提供构造函数
	}

	public static XMLCommand create(Element element, PluginInfo pluginInfo, XMLCommand parent) {
		XMLCommand command = null;

		try {
			if (element != null) {
				if (element.getNodeName().equalsIgnoreCase(_XMLTag.g_ControlButton)) {
					command = new XMLButton(pluginInfo, parent);
				} else if (element.getNodeName().equalsIgnoreCase(_XMLTag.g_ControlLabel)) {
					command = new XMLLabel(pluginInfo, parent);
				} else if (element.getNodeName().equalsIgnoreCase(_XMLTag.g_ControlEditBox)) {
					command = new XMLTextbox(pluginInfo, parent);
				} else if (element.getNodeName().equalsIgnoreCase(_XMLTag.g_ControlComboBox)) {
					command = new XMLComboBox(pluginInfo, parent);
				} else if (element.getNodeName().equalsIgnoreCase(_XMLTag.g_ControlSeparator)) {
					command = new XMLSeparator(pluginInfo, parent);
				} else if (element.getNodeName().equalsIgnoreCase(_XMLTag.g_ControlCheckbox)) {
					command = new XMLCheckBox(pluginInfo, parent);
				}

				if (command != null) {
					command.initialize(element);
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return command;
	}
}
