package com.supermap.desktop.ui;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IXMLCreator;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop._XMLTag;
import com.supermap.desktop.enums.XMLCommandType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class XMLMenus extends XMLCommandBase {

	public XMLMenus(XMLCommandType commandType) {
		this.commandType = commandType;
		this.menus = new ArrayList<XMLMenu>();
	}

	public XMLMenus(PluginInfo pluginInfo, XMLCommandType commandType) {
		super(pluginInfo);
		this.commandType = commandType;
		this.menus = new ArrayList<XMLMenu>();
	}

	private IXMLCreator xmlCreator = null;

	@Override
	public IXMLCreator getXMLCreator() {
		if (this.xmlCreator == null) {
			this.xmlCreator = new XMLMenuCreator(this);
			this.xmlCreator.setDefaultValueCreator(new MenuDefaultValueCreator(this));
		}
		return this.xmlCreator;
	}

	@Override
	public boolean getIsContainer() {
		return false;
	}

	private ArrayList<XMLMenu> menus = null;

	public ArrayList<XMLMenu> getMenus() {
		return this.menus;
	}

	public XMLMenu getMenu(String id) {
		XMLMenu menu = null;
		try {
			for (int i = 0; i < this.menus.size(); i++) {
				if (this.menus.get(i).getID().equalsIgnoreCase(id)) {
					menu = this.menus.get(i);
					break;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return menu;
	}

	public boolean load(Element element) {
		boolean result = false;
		try {
			if (this.commandType == XMLCommandType.FRAMEMENUS) {
				result = loadFrameMenus(element);
			} else {
				result = loadContextMenus(element);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	private boolean loadFrameMenus(Element element) {
		boolean result = false;
		try {
			// 利用 FrameMenu 节点进行初始化
			if (element != null) {
				NodeList nodes = element.getChildNodes();
				for (int i = 0; i < nodes.getLength(); i++) {
					XMLMenu xmlMenu = null;
					if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE && nodes.item(i).getNodeName().equalsIgnoreCase(_XMLTag.g_NodeFrameMenu)) {
						Element childElement = (Element) (nodes.item(i));

						xmlMenu = new XMLMenu(this.getPluginInfo(), this);
						xmlMenu.initialize(childElement);
					}

					if (xmlMenu != null) {
						int insertPos = this.menus.size();
						for (int pos = this.menus.size() - 1; pos >= 0; pos--) {
							if (this.menus.get(pos).getIndex() > xmlMenu.getIndex()) {
								insertPos--;
							} else {
								break;
							}
						}

						if (insertPos < 0 || (insertPos == this.menus.size())) {
							this.menus.add(xmlMenu);
						} else {
							this.menus.add(insertPos, xmlMenu);
						}
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	private boolean loadContextMenus(Element element) {
		Element elementTemp = element;
		boolean result = false;
		try {
			// 首先获取 contextMenu 节点
			NodeList nodes = elementTemp.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {

				XMLMenu xmlMenu = null;
				if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE && nodes.item(i).getNodeName().equalsIgnoreCase(_XMLTag.g_NodeContextMenu)) {
					elementTemp = (Element) (nodes.item(i));

					xmlMenu = new XMLMenu(this.getPluginInfo(), this);
					xmlMenu.initialize(elementTemp);
				}

				if (xmlMenu != null) {
					this.menus.add(xmlMenu);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	public boolean merge(XMLMenus otherMenus) {
		boolean result = false;
		try {
			for (int i = 0; i < otherMenus.getMenus().size(); i++) {
				XMLMenu otherMenu = otherMenus.getMenus().get(i);
				if (otherMenu instanceof XMLMenu) {
					if (otherMenu.getID() != null) {
						XMLMenu preMenu = this.getMenu(otherMenu.getID());
						if (preMenu == null) {
							otherMenu.copyTo(this);
						} else { // 添加到当前菜单中来
							preMenu.merge(otherMenu);
						}
					} else {
						otherMenu.copyTo(this);
					}
				} else {
					// 记录日志，出问题了
				}
			}

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	@Override
	public void addSubItem(XMLCommandBase subItem) {
		try {
			XMLMenu xmlMenu = (XMLMenu) subItem;
			if (xmlMenu != null) {
				int insertPos = this.menus.size();
				for (int pos = this.menus.size() - 1; pos >= 0; pos--) {
					if (this.menus.get(pos).getIndex() > xmlMenu.getIndex()) {
						insertPos--;
					} else {
						break;
					}
				}

				if (insertPos < 0 || (insertPos == this.menus.size())) {
					this.menus.add(xmlMenu);
				} else {
					this.menus.add(insertPos, xmlMenu);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

}
