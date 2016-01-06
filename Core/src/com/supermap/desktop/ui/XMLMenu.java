package com.supermap.desktop.ui;

import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.supermap.desktop.Application;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop.Interface.IXMLCreator;
import com.supermap.desktop.enums.XMLCommandType;

public class XMLMenu extends XMLCommand {

	ArrayList<XMLMenuGroup> groups = null;
	private IXMLCreator xmlCreator;
	protected String formClassName = "";
	private XMLCommandBase xmlGroup;

	public XMLMenu(PluginInfo pluginInfo, XMLMenus parent) {
		super(pluginInfo, parent);

		XMLCommandType commandType = XMLCommandType.FRAMEMENU;
		if (parent.getCommandType().equals(XMLCommandType.CONTEXTMENUS)) {
			commandType = XMLCommandType.CONTEXTMENU;
		}
		this.commandType = commandType;
		this.groups = new ArrayList<XMLMenuGroup>();
	}

	public XMLMenu(PluginInfo pluginInfo, XMLMenuGroup parent) {
		super(pluginInfo, parent);

		this.commandType = parent.getCommandType();
		this.groups = new ArrayList<XMLMenuGroup>();
	}

	public XMLMenu(PluginInfo pluginInfo, XMLButtonDropdown parent) {
		super(pluginInfo, parent);

		this.commandType = parent.getCommandType();
		this.groups = new ArrayList<XMLMenuGroup>();
	}

	@Override
	public boolean initialize(Element element) {

		super.initialize(element);

		try {
				this.formClassName = element.getAttribute(g_AttributionFormClass);
			// 菜单项
			for (int i = 0; i < element.getChildNodes().getLength(); i++) {
				XMLMenuGroup group = null;
				Node node = element.getChildNodes().item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equalsIgnoreCase(g_NodeGroup)) {
					group = XMLMenuGroup.buildMenuGroup(this.getPluginInfo(), this, (Element) node);
				}

				if (group != null) {
					this.groups.add(group);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return true;
	}

	@Override
	public IXMLCreator getXMLCreator() {
		if (this.xmlCreator == null) {
			this.xmlCreator = new XMLMenuGroupCreator(this);
			this.xmlCreator.setDefaultValueCreator(new MenuGroupDefaultValueCreator(this));
		}

		return this.xmlCreator;
	}

	public String getFormClassName() {
		return this.formClassName;
	}

	public void setFormClassName(String controlClass) {
		this.formClassName = controlClass;
	}

	public ArrayList<XMLMenuGroup> groups() {
		return this.groups;
	}

	@Override
	public void merge(XMLCommand otherCommand) {
		try {
			XMLMenu otherMenu = (XMLMenu) otherCommand;

			for (int i = 0; i < otherMenu.groups().size(); i++) {
				XMLMenuGroup otherGroup = otherMenu.groups().get(i);

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
					setXmlGroup(otherGroup.copyTo((XMLCommandBase) this));
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public XMLCommandBase clone(XMLCommandBase parent) {
		XMLMenu result = null;
		try {
			if (parent instanceof XMLMenus) {
				result = new XMLMenu(this.getPluginInfo(), (XMLMenus) parent);
			} else if (parent instanceof XMLMenuGroup) {
				result = new XMLMenuButtonDropdown(this.getPluginInfo(), (XMLMenuGroup) parent);
			} else if (parent instanceof XMLButtonDropdown) {
				result = new XMLMenu(this.getPluginInfo(), (XMLButtonDropdown) parent);
			}

			result.setID(this.getID());
			result.setVisible(this.getVisible());
			result.setIndex(this.getIndex());
			result.setLabel(this.getLabel());
			result.setImageFile(this.getImageFile());
			result.setFormClassName(this.getFormClassName());
			result.getPluginInfo().setBundleName(this.getPluginInfo().getBundleName());
			result.setCustomProperty(this.getCustomProperty());

			for (int i = 0; i < this.groups().size(); i++) {
				this.groups().get(i).copyTo(result);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public void addSubItem(XMLCommandBase subItem) {
		XMLMenuGroup xmlGroupTemp = (XMLMenuGroup) subItem;
		try {
			int insertPos = this.groups().size();
			for (int i = this.groups().size() - 1; i >= 0; i--) {
				if (this.groups().get(i).getIndex() > xmlGroupTemp.getIndex()) {
					insertPos--;
				} else {
					break;
				}
			}

			if (insertPos < 0 || (insertPos == this.groups().size())) {
				this.groups().add(xmlGroupTemp);
			} else {
				this.groups().add(insertPos, xmlGroupTemp);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
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
	protected boolean doRemove() {
		Boolean result = false;
		try {
			result = ((XMLMenus) this.getParent()).getMenus().remove(this);
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
				result.setImageFile(this.getImageFile());
				result.setFormClassName(this.getFormClassName());
				result.getPluginInfo().setBundleName(this.getPluginInfo().getBundleName());
				result.setCustomProperty(this.getCustomProperty());

				for (int i = 0; i < this.groups().size(); i++) {
					XMLCommandBase resultGroup = this.groups().get(i).saveToPluginInfo(pluginInfo, result);
					if (resultGroup != null) {
						result.groups().add((XMLMenuGroup) resultGroup);
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	public XMLCommandBase getXmlGroup() {
		return xmlGroup;
	}

	public void setXmlGroup(XMLCommandBase xmlGroup) {
		this.xmlGroup = xmlGroup;
	}

}