package com.supermap.desktop.implement;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.enums.XMLCommandType;
import com.supermap.desktop.ui.XMLCommand;

import javax.swing.*;

public class SmComponentFactory {

	private SmComponentFactory() {
		// 工具类不提供构造函数
	}

	public static IBaseItem create(XMLCommand xmlCommand, JComponent parent) {
		IBaseItem result = null;

		try {
			if (xmlCommand != null) {
				if (xmlCommand.getCommandType() == XMLCommandType.BUTTON) {
					result = new SmCtrlActionButton(null, xmlCommand, parent);
				} else if (xmlCommand.getCommandType() == XMLCommandType.LABEL) {
					result = new SmLabel(null, xmlCommand, parent);
				} else if (xmlCommand.getCommandType() == XMLCommandType.TEXTBOX) {
					result = new SmTextField(null, xmlCommand, parent);
				} else if (xmlCommand.getCommandType() == XMLCommandType.COMBOBOX) {
					result = new SmComboBox(null, xmlCommand, parent);
				} else if (xmlCommand.getCommandType() == XMLCommandType.SEPARATOR) {
					result = new SmSeparator(null, xmlCommand, parent);
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}
}
