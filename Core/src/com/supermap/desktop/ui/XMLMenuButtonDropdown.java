package com.supermap.desktop.ui;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.supermap.desktop.Application;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop.Interface.IXMLCreator;
import com.supermap.desktop.enums.XMLCommandType;

public class XMLMenuButtonDropdown extends XMLMenu {

	public XMLMenuButtonDropdown(PluginInfo pluginInfo, XMLMenuGroup group) {
		super(pluginInfo, group);
		this.groups = new ArrayList<XMLMenuGroup>();
		this.commandType = XMLCommandType.MENUBUTTONDROPDOWN;
	}

	@Override
	public boolean getIsContainer() {
		return true;
	}

	private IXMLCreator xmlCreator;

	@Override
	public IXMLCreator getXMLCreator() {
		if (this.xmlCreator == null) {
			this.xmlCreator = new XMLMenuGroupCreator(this);
			this.xmlCreator.setDefaultValueCreator(new MenuGroupDefaultValueCreator(this));
		}

		return this.xmlCreator;
	}

	@Override
	public boolean initialize(Element xmlNodeCommand) {
		try {
			super.initialize(xmlNodeCommand);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return true;
	}

	@Override
	public void merge(XMLCommand otherCommand) {
		try {
			XMLMenuButtonDropdown otherXMLMenuButtonDropdown = (XMLMenuButtonDropdown) otherCommand;

			for (int i = 0; i < otherXMLMenuButtonDropdown.groups().size(); i++) {
				XMLMenuGroup otherGroup = otherXMLMenuButtonDropdown.groups().get(i);
				Boolean isContains = false;
				for (int j = 0; j < this.groups().size(); j++) {
					XMLMenuGroup thisGroup = this.groups().get(j);
					if (thisGroup.getID().equalsIgnoreCase(otherGroup.getID())) {
						thisGroup.merge(otherGroup);
						isContains = true;
						break;
					}
				}

				if (!isContains) {
					// 添加
					otherGroup.copyTo(this);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

	}

	@Override
	public XMLCommandBase clone(XMLCommandBase parent) {
		XMLMenuButtonDropdown result = null;
		try {
			result = (XMLMenuButtonDropdown) super.clone(parent);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public XMLCommandBase createElement(XMLCommandType commandType) {
		XMLMenuGroup result = null;
		try {
			result = (XMLMenuGroup) this.getXMLCreator().createElement(commandType);
			this.groups.add(result);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public XMLCommandBase saveToPluginInfo(PluginInfo pluginInfo, XMLCommandBase parent) {
		XMLMenuButtonDropdown result = null;
		try {
			result = (XMLMenuButtonDropdown) super.saveToPluginInfo(pluginInfo, parent);
			if (result != null) {
				result.setID(this.getID());
				for (int i = 0; i < this.groups.size(); i++) {
					XMLMenuGroup resultGroup = (XMLMenuGroup) this.groups.get(i).saveToPluginInfo(pluginInfo, result);
					if (resultGroup != null) {
						// do nothing
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public void addSubItem(XMLCommandBase subItem) {
		XMLMenuGroup menuGroup = (XMLMenuGroup) subItem;
		try {
			int insertPos = this.groups().size();
			for (int i = this.groups().size() - 1; i >= 0; i--) {
				if (this.groups().get(i).getIndex() > menuGroup.getIndex()) {
					insertPos--;
				} else {
					break;
				}
			}

			if (insertPos < 0 || (insertPos == this.groups().size())) {
				this.groups().add(menuGroup);
			} else {
				this.groups().add(insertPos, menuGroup);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

}
