package com.supermap.desktop.ui;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.supermap.desktop.Application;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop._XMLTag;
import com.supermap.desktop.enums.XMLCommandType;

public class XMLButtonDropdown extends XMLCommand {

	private XMLCommand parentTemp;
	public XMLCommand getParentTemp() {
		return parentTemp;
	}

	public void setParentTemp(XMLCommand parentTemp) {
		this.parentTemp = parentTemp;
	}

	private XMLMenu dropdownMenu;

	public XMLButtonDropdown(PluginInfo pluginInfo, XMLCommand parent) {
		super(pluginInfo);
		this.parentTemp = parent;
		this.commandType = XMLCommandType.BUTTONDROPDOWN;
		dropdownMenu = new XMLMenu(pluginInfo, this);
	}

	public XMLMenu getDropdownMenu() {
		return this.dropdownMenu;
	}

	public void setDropdownMenu(XMLMenu dropdownMenu) {
		this.dropdownMenu = dropdownMenu;
	}

	@Override
	public boolean getIsContainer() {
		return false;
	}

	@Override
	public boolean initialize(Element element) {
		boolean result = false;
		try {
			result = super.initialize(element);

			if (element.getNodeType() == Node.ELEMENT_NODE && element.getNodeName().equalsIgnoreCase(_XMLTag.g_ControlButtonDropdown)) {

				dropdownMenu = new XMLMenu(this.getPluginInfo(), this);
				dropdownMenu.initialize(element);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public void merge(XMLCommand otherCommand) {
		try {
			this.getDropdownMenu().merge(((XMLButtonDropdown) otherCommand).getDropdownMenu());
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public XMLCommandBase clone(XMLCommandBase parent) {
		XMLButtonDropdown result = null;
		try {
			result = new XMLButtonDropdown(this.getPluginInfo(), (XMLToolbar) parent);

			result.setID(this.getID());
			result.setVisible(this.getVisible());
			result.setIndex(this.getIndex());
			result.setLabel(this.getLabel());
			result.setImageFile(this.getImageFile());
			result.setCtrlActionClass(this.getCtrlActionClass());
			result.getPluginInfo().setBundleName(this.getPluginInfo().getBundleName());
			result.setCustomProperty(this.getCustomProperty());
			this.getDropdownMenu().copyTo(result);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public void addSubItem(XMLCommandBase subItem) {
		try {
			XMLMenu xmlMenu = (XMLMenu) subItem;
			this.setDropdownMenu(xmlMenu);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public XMLCommandBase createElement(XMLCommandType commandType) {
		XMLMenuGroup result = null;
		return result;
	}

	@Override
	protected boolean doRemove() {
		Boolean result = false;
		return result;
	}

	@Override
	public XMLCommandBase saveToPluginInfo(PluginInfo pluginInfo, XMLCommandBase parent) {
		XMLButtonDropdown result = null;
		try {
			result = (XMLButtonDropdown) super.saveToPluginInfo(pluginInfo, parent);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}
}
