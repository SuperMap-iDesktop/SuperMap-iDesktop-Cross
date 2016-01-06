package com.supermap.desktop.ui;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.supermap.desktop.Application;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop._XMLTag;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IXMLCreator;
import com.supermap.desktop.enums.XMLCommandType;

public class XMLToolbar extends XMLCommand {

	private ArrayList<XMLCommand> commands = null;
	private IXMLCreator xmlCreator;
	private String formClassName = "";
	private IForm associatedForm = null;

	public XMLToolbar(PluginInfo pluginInfo, XMLToolbars parent) {
		super(pluginInfo, parent);
		this.commandType = XMLCommandType.TOOLBAR;
		this.commands = new ArrayList<XMLCommand>();
	}

	@Override
	public boolean initialize(Element element) {
		super.initialize(element);
		try {
			if (element.hasAttribute(g_AttributionFormClass)) {
				this.formClassName = element.getAttribute(g_AttributionFormClass);
			}

			// 工具条子项
			for (int i = 0; i < element.getChildNodes().getLength(); i++) {
				Node node = element.getChildNodes().item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					XMLCommand command = this.buildCommand((Element) node);
					if (command != null) {
						this.commands.add(command);
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return true;
	}

	public XMLCommand buildCommand(Element xmlNodeCommand) {
		XMLCommand xmlCommand = null;

		if (xmlNodeCommand.getNodeName().equalsIgnoreCase(g_ControlButton)) {
			xmlCommand = new XMLButton(this.getPluginInfo(), this);
		} else if (xmlNodeCommand.getNodeName().equalsIgnoreCase(g_ControlButtonDropdown)) {
			xmlCommand = new XMLButtonDropdown(this.getPluginInfo(), this);
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

	@Override
	public IXMLCreator getXMLCreator() {
		return this.xmlCreator;
	}

	public String getFormClassName() {
		return this.formClassName;
	}

	public void setFormClassName(String formClassName) {
		this.formClassName = formClassName;
	}

	public IForm getAssociatedForm() {
		return associatedForm;
	}

	public void setAssociatedForm(IForm associatedForm) {
		this.associatedForm = associatedForm;
	}

	public ArrayList<XMLCommand> items() {
		return this.commands;
	}

	public XMLCommand getToolbarItem(String id) {
		XMLCommand xmlCommand = null;
		try {
			for (int i = 0; i < this.commands.size(); i++) {
				if (this.commands.get(i).getID().equalsIgnoreCase(id)) {
					xmlCommand = this.commands.get(i);
					break;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return xmlCommand;
	}

	public boolean merge(XMLToolbar otherToolbar) {
		boolean result = false;
		try {
			for (int i = 0; i < otherToolbar.items().size(); i++) {
				XMLCommand otherCommand = otherToolbar.items().get(i);

				boolean isContains = false;
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
		XMLToolbar result = null;
		try {
			if (parent instanceof XMLToolbars) {
				result = new XMLToolbar(this.getPluginInfo(), (XMLToolbars) parent);
			}

			result.setID(this.getID());
			result.setVisible(this.getVisible());
			result.setIndex(this.getIndex());
			result.setLabel(this.getLabel());
			result.setFormClassName(this.getFormClassName());
			result.getPluginInfo().setBundleName(this.getPluginInfo().getBundleName());
			result.setHelpURL(this.getHelpURL());
			result.setCustomProperty(this.getCustomProperty());
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
		boolean result = false;
		try {
			if (this.getParent() instanceof XMLToolbars) {
				XMLToolbars xmlToolbars = (XMLToolbars) this.getParent();
				xmlToolbars.getToolbars().remove(this);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public XMLCommandBase saveToPluginInfo(PluginInfo pluginInfo, XMLCommandBase parent) {
		XMLMenu result = null;
		try {
			if (this.getPluginInfo() == pluginInfo) {
				result = new XMLMenu(pluginInfo, (XMLMenus) parent);
				result.setID(this.getID());
				result.setVisible(this.getVisible());
				result.setIndex(this.getIndex());
				result.setLabel(this.getLabel());
				result.setFormClassName(this.getFormClassName());
				result.getPluginInfo().setBundleName(this.getPluginInfo().getBundleName());
				result.setHelpURL(this.getHelpURL());
				result.setCustomProperty(this.getCustomProperty());
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}
}
