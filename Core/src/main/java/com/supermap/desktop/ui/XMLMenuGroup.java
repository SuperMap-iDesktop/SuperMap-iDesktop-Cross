package com.supermap.desktop.ui;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IXMLCreator;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop.enums.XMLCommandType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;

public class XMLMenuGroup extends XMLCommand {

	public XMLMenuGroup(XMLCommandBase parent) {
		super(parent);
		this.commandType = XMLCommandType.MENUGROUP;
		this.commands = new ArrayList<XMLCommand>();
	}

	public XMLMenuGroup(PluginInfo pluginInfo, XMLCommandBase parent) {
		super(pluginInfo, parent);
		this.commandType = XMLCommandType.MENUGROUP;
		this.commands = new ArrayList<XMLCommand>();
	}

	@Override
	public boolean initialize(Element xmlNode) {

		super.initialize(xmlNode);
		try {
			// 菜单项
			for (int i = 0; i < xmlNode.getChildNodes().getLength(); i++) {
				Node node = xmlNode.getChildNodes().item(i);

				XMLMenuGroup group = null;
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					if (node.getNodeName().equalsIgnoreCase(g_NodeGroup)) {
						group = new XMLMenuGroup(this.getPluginInfo(), this);
						group.initialize((Element) node);
					} else {
						XMLCommand xmlCommand = buildCommand((Element) node);
						if (xmlCommand != null) {
							// 添加
							int insertPos = this.items().size();
							for (int index = this.items().size() - 1; index >= 0; index--) {
								if (this.items().get(index).getIndex() > xmlCommand.getIndex()) {
									insertPos--;
								} else {
									break;
								}
							}

							if (insertPos < 0 || (insertPos == this.items().size())) {
								this.items().add(xmlCommand);
							} else {
								this.items().add(insertPos, xmlCommand);
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return true;
	}

	private IXMLCreator xmlCreator;

	@Override
	public IXMLCreator getXMLCreator() {
		if (this.xmlCreator == null) {
			this.xmlCreator = new XMLMenuGroupItemCreator(this);
			this.xmlCreator.setDefaultValueCreator(new MenuGroupItemDefaultValueCreator(this));
		}

		return this.xmlCreator;
	}

	String text = "";

	@Override
	public String getLabel() {
		return this.text;
	}

	@Override
	public void setLabel(String text) {
		this.text = text;
	}

	protected boolean visible = true;

	@Override
	public boolean getVisible() {
		return this.visible;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	private String layoutStyle = "";

	public String getLayoutStyle() {
		return this.layoutStyle;
	}

	public void setLayoutStyle(String layoutStyle) {
		this.layoutStyle = layoutStyle;
	}

	protected String customProperty = "";

	@Override
	public String getCustomProperty() {
		return this.customProperty;
	}

	@Override
	public void setCustomProperty(String customProperty) {
		this.customProperty = customProperty;
	}

	private ArrayList<XMLCommand> commands = null;

	public ArrayList<XMLCommand> items() {
		return this.commands;
	}

	public XMLCommand getMenuItem(String key) {
		XMLCommand xmlCommand = null;
		try {
			for (int i = 0; i < this.commands.size(); i++) {
				if (this.commands.get(i).getID().equalsIgnoreCase(key)) {
					xmlCommand = this.commands.get(i);
					break;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return xmlCommand;
	}

	@Override
	public Boolean loadImage() {
		return false;
	}

	static public XMLMenuGroup buildMenuGroup(PluginInfo pluginInfo, XMLCommandBase parent, Element xmlNodeCommand) {
		XMLMenuGroup group = null;
		if (xmlNodeCommand != null && (xmlNodeCommand.getNodeName().equalsIgnoreCase(g_NodeGroup))) {
			group = new XMLMenuGroup(pluginInfo, parent);
			group.initialize(xmlNodeCommand);
		}

		return group;
	}

	public XMLCommand buildCommand(Element xmlNodeCommand) {
		XMLCommand xmlCommand = null;
		if (xmlNodeCommand.getNodeName().equalsIgnoreCase(g_ControlButton)) {
			xmlCommand = new XMLMenuButton(this.getPluginInfo(), this); // menuItem
		} else if (xmlNodeCommand.getNodeName().equalsIgnoreCase(g_ControlButtonDropdown)) {
			xmlCommand = new XMLMenuButtonDropdown(this.getPluginInfo(), this); // dropdownmenu
		} else if (xmlNodeCommand.getNodeName().equalsIgnoreCase(g_ControlLabel)) {
			xmlCommand = new XMLLabel(this.getPluginInfo(), this);
		} else if (xmlNodeCommand.getNodeName().equalsIgnoreCase(g_ControlEditBox)) {
			xmlCommand = new XMLTextbox(this.getPluginInfo(), this);
		} else if (xmlNodeCommand.getNodeName().equalsIgnoreCase(g_ControlComboBox)) {
			xmlCommand = new XMLComboBox(this.getPluginInfo(), this);
		} else if (xmlNodeCommand.getNodeName().equalsIgnoreCase(g_ControlSeparator)) {
			xmlCommand = new XMLSeparator(this.getPluginInfo(), this);
		}

		if (xmlCommand != null) {
			xmlCommand.initialize(xmlNodeCommand);
		}
		return xmlCommand;
	}

	public boolean merge(XMLMenuGroup otherGroup) {
		boolean result = false;
		try {
			for (int i = 0; i < otherGroup.items().size(); i++) {
				XMLCommand otherCommand = otherGroup.items().get(i);

				Boolean isContains = false;
				for (int j = 0; j < this.items().size(); j++) {
					XMLCommand thisCommand = this.items().get(j);
					if (thisCommand.canMerge() && thisCommand.getID() != "" && thisCommand.getID().equalsIgnoreCase(otherCommand.getID())) {
						thisCommand.merge(otherCommand);
						isContains = true;
						break;
					}
				}

				if (!isContains) {
					otherCommand.copyTo(this);
				}
			}
			result = true;
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public XMLCommandBase clone(XMLCommandBase parent) {
		XMLMenuGroup result = null;
		try {
			result = new XMLMenuGroup(parent);
			result.setID(this.getID());
			result.setIndex(this.getIndex());
			result.setLabel(this.getLabel());
			result.setVisible(this.getVisible());
			result.setCustomProperty(this.customProperty);
			for (int i = 0; i < this.commands.size(); i++) {
				this.commands.get(i).copyTo(result);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public void addSubItem(XMLCommandBase subItem) {
		XMLCommand command = (XMLCommand) subItem;
		try {
			int insertPos = this.items().size();
			for (int i = this.items().size() - 1; i >= 0; i--) {
				if (this.items().get(i).getIndex() > command.getIndex()) {
					insertPos--;
				} else {
					break;
				}
			}

			if (insertPos < 0 || (insertPos == this.items().size())) {
				this.items().add(command);
			} else {
				this.items().add(insertPos, command);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public XMLCommandBase createElement(XMLCommandType commandType) {
		XMLCommand result = null;
		try {
			result = (XMLCommand) this.getXMLCreator().createElement(commandType);
			addSubItem(result);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	protected boolean doRemove() {
		Boolean result = false;
		try {
			if (this.getParent() instanceof XMLMenu) {
				XMLMenu xmltMenu = (XMLMenu) this.getParent();
				xmltMenu.groups().remove(this);
			}
			result = true;
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public XMLCommandBase saveToPluginInfo(PluginInfo pluginInfo, XMLCommandBase parent) {
		XMLMenuGroup result = null;

		return result;
	}

}
