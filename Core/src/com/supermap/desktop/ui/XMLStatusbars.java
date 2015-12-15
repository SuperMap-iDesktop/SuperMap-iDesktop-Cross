package com.supermap.desktop.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.xml.soap.Node;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.supermap.desktop.Application;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop._XMLTag;
import com.supermap.desktop.enums.XMLCommandType;

public class XMLStatusbars extends XMLCommandBase {

	private ArrayList<XMLStatusbar> statusbars = null;

	public XMLStatusbars() {
		this.commandType = XMLCommandType.STATUSBARS;
		this.statusbars = new ArrayList<XMLStatusbar>();
	}

	public XMLStatusbars(PluginInfo pluginInfo) {
		super(pluginInfo);
		this.commandType = XMLCommandType.STATUSBARS;
		this.statusbars = new ArrayList<XMLStatusbar>();
	}

	public ArrayList<XMLStatusbar> getStatusbars() {
		return statusbars;
	}

	@Override
	public boolean getIsContainer() {
		return false;
	}

	public boolean load(Element element) {
		boolean result = false;

		try {
			if (element != null) {
				NodeList nodes = element.getChildNodes();
				for (int i = 0; i < nodes.getLength(); i++) {
					XMLStatusbar xmlStatusbar = null;
					if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE && nodes.item(i).getNodeName().equalsIgnoreCase(_XMLTag.g_NodeStatusbar)) {
						Element childElement = (Element) (nodes.item(i));
						xmlStatusbar = new XMLStatusbar(this.getPluginInfo(), this);
						xmlStatusbar.initialize(childElement);
					}

					if (xmlStatusbar != null) {
						insertCommand(xmlStatusbar, this.statusbars);
					}
				}
			}
			result = true;
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	public boolean merge(XMLStatusbars otherStatusbars) {
		boolean result = false;

		try {
			for (int i = 0; i < otherStatusbars.getStatusbars().size(); i++) {
				XMLStatusbar otherStatusbar = otherStatusbars.getStatusbars().get(i);
				if (otherStatusbar.getID() != null) {
					XMLStatusbar preStatusbar = this.getStatusbar(otherStatusbar.getID());
					if (preStatusbar == null) {
						otherStatusbar.copyTo(this);
					} else {
						preStatusbar.merge(otherStatusbar);
					}
				} else {
					otherStatusbar.copyTo(this);
				}
			}
			result = true;
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	public void sort() {
		Collections.sort(this.statusbars);
	}

	@Override
	public XMLCommandBase clone(XMLCommandBase parent) {
		XMLStatusbars result = null;

		try {
			result = new XMLStatusbars(this.getPluginInfo());
			for (int i = 0; i < this.statusbars.size(); i++) {
				this.statusbars.get(i).copyTo(result);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	@Override
	public void addSubItem(XMLCommandBase subItem) {
		this.statusbars.add((XMLStatusbar) subItem);
	}

	@Override
	public void insertSubItem(XMLCommandBase preItem, XMLCommandBase insertItem) {
		try {
			XMLStatusbar preStatusbar = (XMLStatusbar) preItem;
			XMLStatusbar insertStatusbar = (XMLStatusbar) insertItem;
			if (insertStatusbar != null) {
				int insertIndex = 0;
				insertStatusbar.setIndex(0);
				// 如果前一个元素为空，就默认插入到最前
				if (preStatusbar != null) {
					if (!this.statusbars.contains(preStatusbar)) {
						throw new Exception("error.");
					}

					insertIndex = this.statusbars.indexOf(preStatusbar) + 1;
					insertStatusbar.setIndex(preStatusbar.getIndex() + 1);
				}

				this.statusbars.add(insertIndex, insertStatusbar);
				// 如果不是插入到最后，那么需要比较后面子项的CommandIndex进行递增
				if (insertIndex + 1 < this.statusbars.size() && (insertStatusbar.getIndex() >= this.statusbars.get(insertIndex + 1).getIndex())) {
					for (int i = insertIndex + 1; i < this.statusbars.size(); i++) {
						this.statusbars.get(i).setIndex((i - insertIndex) + insertStatusbar.getIndex());
					}
				}
			}

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	public XMLStatusbar getStatusbar(String id) {
		XMLStatusbar statusbar = null;

		try {
			for (int i = 0; i < this.statusbars.size(); i++) {
				if (this.statusbars.get(i).getID().equalsIgnoreCase(id)) {
					statusbar = this.statusbars.get(i);
					break;
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return statusbar;
	}

	public void toElement(Element element) {
		// do nothing

	}
}
