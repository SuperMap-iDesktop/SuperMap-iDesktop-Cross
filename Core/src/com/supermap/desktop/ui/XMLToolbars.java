package com.supermap.desktop.ui;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.supermap.desktop.Application;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop._XMLTag;
import com.supermap.desktop.Interface.IXMLCreator;
import com.supermap.desktop.enums.XMLCommandType;

public class XMLToolbars extends XMLCommandBase {

	private ArrayList<XMLToolbar> toolbars = null;
	private IXMLCreator xmlCreator = null;

	public XMLToolbars() {
		this.commandType = XMLCommandType.TOOLBARS;
		this.toolbars = new ArrayList<XMLToolbar>();
	}

	public XMLToolbars(PluginInfo pluginInfo) {
		super(pluginInfo);
		this.commandType = XMLCommandType.TOOLBARS;
		this.toolbars = new ArrayList<XMLToolbar>();
	}

	@Override
	public IXMLCreator getXMLCreator() {
		if (this.xmlCreator == null) {
			this.xmlCreator = new XMLToolbarCreator(this);
			this.xmlCreator.setDefaultValueCreator(new ToolbarDefaultValueCreator(this));
		}
		return this.xmlCreator;
	}

	@Override
	public boolean getIsContainer() {
		return false;
	}

	public ArrayList<XMLToolbar> getToolbars() {
		return this.toolbars;
	}

	public XMLToolbar getToolbar(String id) {
		XMLToolbar menu = null;
		try {
			for (int i = 0; i < this.toolbars.size(); i++) {
				if (this.toolbars.get(i).getID().equalsIgnoreCase(id)) {
					menu = this.toolbars.get(i);
					break;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return menu;
	}

	public Boolean load(Element element) {
		boolean result = false;
		try {
			// 利用 FrameMenu 节点进行初始化
			if (element != null) {
				NodeList nodes = element.getChildNodes();
				for (int i = 0; i < nodes.getLength(); i++) {
					XMLToolbar XMLToolbar = null;
					if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE && nodes.item(i).getNodeName().equalsIgnoreCase(_XMLTag.g_NodeToolbar)) {
						Element childElement = (Element) (nodes.item(i));

						XMLToolbar = new XMLToolbar(this.getPluginInfo(), this);
						XMLToolbar.initialize(childElement);
					}

					if (XMLToolbar != null) {
						int insertPos = this.toolbars.size();
						for (int pos = this.toolbars.size() - 1; pos >= 0; pos--) {
							if (this.toolbars.get(pos).getIndex() > XMLToolbar.getIndex()) {
								insertPos--;
							} else {
								break;
							}
						}

						if (insertPos < 0 || (insertPos == this.toolbars.size())) {
							this.toolbars.add(XMLToolbar);
						} else {
							this.toolbars.add(insertPos, XMLToolbar);
						}
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	public boolean merge(XMLToolbars otherToolbars) {
		boolean result = false;
		try {
			for (int i = 0; i < otherToolbars.getToolbars().size(); i++) {
				XMLToolbar otherToolbar = otherToolbars.getToolbars().get(i);
				if (otherToolbar instanceof XMLToolbar) {
					if (otherToolbar.getID() != null) {
						XMLToolbar preToolbar = this.getToolbar(otherToolbar.getID());
						if (preToolbar == null) {
							otherToolbar.copyTo(this);
						} else { // 添加到当前菜单中来
							preToolbar.merge(otherToolbar);
						}
					} else {
						otherToolbar.copyTo(this);
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
			XMLToolbar xmlToolbar = (XMLToolbar) subItem;
			if (xmlToolbar != null) {
				int insertPos = this.toolbars.size();
				for (int pos = this.toolbars.size() - 1; pos >= 0; pos--) {
					if (this.toolbars.get(pos).getIndex() > xmlToolbar.getIndex()) {
						insertPos--;
					} else {
						break;
					}
				}

				if (insertPos < 0 || (insertPos == this.toolbars.size())) {
					this.toolbars.add(xmlToolbar);
				} else {
					this.toolbars.add(insertPos, xmlToolbar);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}
}
