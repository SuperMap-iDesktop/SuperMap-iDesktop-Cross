package com.supermap.desktop.ui;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.supermap.desktop.Application;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IXMLCreator;
import com.supermap.desktop.enums.XMLCommandType;

public class XMLStatusbar extends XMLCommand {

	private ArrayList<XMLCommand> items = null;
	private IXMLCreator xmlCreator = null;
	private String formClassName = "";
	private IForm associatedForm = null;

	public XMLStatusbar(PluginInfo pluginInfo) {
		super(pluginInfo);
		this.commandType = XMLCommandType.STATUSBAR;
		this.items = new ArrayList<XMLCommand>();
	}

	public XMLStatusbar(PluginInfo pluginInfo, XMLCommandBase parent) {
		super(pluginInfo, parent);
		this.commandType = XMLCommandType.STATUSBAR;
		this.items = new ArrayList<XMLCommand>();
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
		return this.items;
	}

	@Override
	public boolean initialize(Element element) {
		boolean result = false;

		try {
			if (element != null) {
				super.initialize(element);
				if (element.hasAttribute(g_AttributionFormClass)) {
					this.formClassName = element.getAttribute(g_AttributionFormClass);
				}

				NodeList childNodes = element.getChildNodes();
				if (childNodes != null) {
					for (int i = 0; i < childNodes.getLength(); i++) {
						Node childNode = childNodes.item(i);
						if (childNode != null && childNode.getNodeType() == Node.ELEMENT_NODE) {
							XMLCommand command = XMLCommandFactory.create((Element) childNode, this.getPluginInfo(), this);
							insertCommand(command, this.items);
						}
					}
				}
				result = true;
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	public boolean merge(XMLStatusbar otherStatusbar) {
		boolean result = false;

		try {
			for (int i = 0; i < otherStatusbar.items().size(); i++) {
				XMLCommand otherCommand = otherStatusbar.items().get(i);

				boolean isContains = false;
				for (int j = 0; j < this.items.size(); j++) {
					XMLCommand thisCommand = this.items.get(j);
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
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	@Override
	public XMLCommandBase clone(XMLCommandBase parent) {
		XMLStatusbar result = null;

		try {
			result = new XMLStatusbar(parent.getPluginInfo(), (XMLStatusbars) parent);
			result.setFormClassName(this.formClassName);
			result.setVisible(this.getVisible());
			result.setCustomProperty(this.getCustomProperty());
			for (int i = 0; i < this.items.size(); i++) {
				this.items.get(i).copyTo(result);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	@Override
	public void addSubItem(XMLCommandBase subItem) {
		XMLCommand command = (XMLCommand) subItem;

		try {
			insertCommand(command, this.items);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	@Override
	public void insertSubItem(XMLCommandBase preItem, XMLCommandBase insertItem) {
		try {
			XMLCommand preCommand = (XMLCommand) preItem;
			XMLCommand insertCommand = (XMLCommand) insertItem;

			if (insertCommand != null) {
				int insertIndex = 0;
				insertCommand.setIndex(0);
				// 如果前一个元素为空，就默认插入到最前
				if (preCommand != null) {
					if (!this.items.contains(preCommand)) {
						throw new Exception("error.");
					}

					insertIndex = this.items.indexOf(preCommand) + 1;
					insertCommand.setIndex(preCommand.getIndex() + 1);
				}

				this.items.add(insertIndex, insertCommand);
				// 如果不是插入到最后，那么需要比较后面子项的 CommandIndex 进行递增
				if (insertIndex + 1 < this.items.size() && (insertCommand.getIndex() >= this.items.get(insertIndex + 1).getIndex())) {
					for (int i = insertIndex + 1; i < this.items.size(); i++) {
						this.items.get(i).setIndex((i - insertIndex) + insertCommand.getIndex());
					}
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}
}
