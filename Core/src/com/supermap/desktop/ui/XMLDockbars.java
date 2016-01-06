package com.supermap.desktop.ui;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.supermap.desktop.Application;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop.enums.XMLCommandType;

public class XMLDockbars extends XMLCommandBase {
	private ArrayList<XMLDockbarBase> dockBars = null;

	public XMLDockbars() {
		this.commandType = XMLCommandType.DOCKBARS;
		this.dockBars = new ArrayList<XMLDockbarBase>();
	}

	public XMLDockbars(PluginInfo pluginInfo) {
		super(pluginInfo);
		this.commandType = XMLCommandType.DOCKBARS;
		this.dockBars = new ArrayList<XMLDockbarBase>();
	}

	public boolean load(Element element) {
		boolean result = false;

		try {
			// 查找包含DockBar 的节点
			NodeList nodes = element.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node barNode = nodes.item(i);
				if (barNode instanceof Element) {
					XMLDockbarBase XMLDockbarBase = null;
					if (barNode.getNodeName().equalsIgnoreCase(g_NodeDockbar)) {
						XMLDockbarBase = new XMLDockbar(this.getPluginInfo(), this);
					} else if (barNode.getNodeName().equals(g_NodeDockbarGroup)) {
						XMLDockbarBase = new XMLDockbarGroup(this.getPluginInfo(), this);
					}

					if (XMLDockbarBase != null) {
						XMLDockbarBase.initialize((Element) barNode);
						this.dockBars.add(XMLDockbarBase);
					}
				}
			}
			result = true;
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	public boolean merge(XMLDockbars otherBars) {
		for (int i = 0; i < otherBars.getDockbars().size(); i++) {
			XMLDockbarBase bar = otherBars.dockBars.get(i);
			if (bar instanceof XMLDockbar) {
				if (bar.getID() != null && !bar.getID().isEmpty() && bar.getID().length() != 0) {
					XMLDockbarBase preBar = this.getDockBar(bar.getID());
					if (preBar == null) {
						bar.copyTo(this);
					}
				} else {
					bar.copyTo(this);
				}
			} else if (bar instanceof XMLDockbarGroup && (bar.getID() != null && !bar.getID().isEmpty() && bar.getID().length() != 0)) {
				XMLDockbarBase preBarGroup = this.getDockBar(bar.getID());
				if (preBarGroup == null || preBarGroup instanceof XMLDockbar) {
					bar.copyTo(this);
				} else if (preBarGroup instanceof XMLDockbarGroup) {
					((XMLDockbarGroup) preBarGroup).merge((XMLDockbarGroup) bar);
				}
			}
		}
		return true;
	}

	public void sort() {
		java.util.Collections.sort(this.dockBars);
	}

	@Override
	public XMLCommandBase clone(XMLCommandBase parent) {
		XMLDockbars result = null;

		try {
			result = new XMLDockbars();
			for (int i = 0; i < this.dockBars.size(); i++) {
				this.dockBars.get(i).copyTo(result);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	@Override
	public void addSubItem(XMLCommandBase subItem) {
		try {
			this.dockBars.add((XMLDockbarBase) subItem);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void insertSubItem(XMLCommandBase preItem, XMLCommandBase insertItem) {
		try {
			XMLDockbarBase preDockBarBase = (XMLDockbarBase) preItem;
			XMLDockbarBase insertDockBarBase = (XMLDockbarBase) insertItem;

			if (insertDockBarBase != null) {
				int insertIndex = 0;
				insertDockBarBase.setIndex(0);

				// 如果前一个元素为空，就默认插入到最前
				if (preDockBarBase != null) {
					if (!this.dockBars.contains(preDockBarBase)) {
						throw new Exception("Error.");
					}

					insertIndex = this.dockBars.indexOf(preDockBarBase) + 1;
					insertDockBarBase.setIndex(preDockBarBase.getIndex() + 1);
				}

				this.dockBars.add(insertIndex, insertDockBarBase);

				// 如果不是插入到最后，那么需要比较后面子项的 commandIndex 进行递增
				if (insertIndex + 1 < this.dockBars.size() && (insertDockBarBase.getIndex() >= this.dockBars.get(insertIndex + 1).getIndex())) {
					for (int i = insertIndex + 1; i < this.dockBars.size(); i++) {
						this.dockBars.get(i).setIndex((i - insertIndex) + insertDockBarBase.getIndex());
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public boolean save() {
		boolean result = true;
		return result;
	}

	@Override
	public XMLCommandBase createElement(XMLCommandType commandType) {
		XMLDockbarBase result = null;

		try {
			result = (XMLDockbarBase) this.getXMLCreator().createElement(commandType);
			this.dockBars.add(result);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	@Override
	public XMLCommandBase saveToPluginInfo(PluginInfo pluginInfo, XMLCommandBase parent) {
		XMLDockbars result = null;

		try {
			if (this.getPluginInfo() == pluginInfo) {
				result = new XMLDockbars(pluginInfo);
				for (int i = 0; i < this.dockBars.size(); i++) {
					XMLDockbarBase resultDockBarBase = (XMLDockbarBase) this.dockBars.get(i).saveToPluginInfo(pluginInfo, result);
					if (resultDockBarBase != null) {
						result.getDockbars().add(resultDockBarBase);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	@Override
	public Element toXML(Document document) {
		Element dockBarsNode = null;

		try {
			if (document != null) {
				dockBarsNode = document.createElement(g_NodeDockbars);
				for (int i = 0; i < this.dockBars.size(); i++) {
					Element dockBarNode = this.dockBars.get(i).toXML(document);
					dockBarsNode.appendChild(dockBarNode);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return dockBarsNode;
	}

	public int size() {
		return this.dockBars.size();
	}

	public ArrayList<XMLDockbarBase> getDockbars() {
		return dockBars;
	}

	public XMLDockbarBase get(int index) {
		return this.dockBars.get(index);
	}

	private XMLDockbarBase getDockBar(String id) {
		XMLDockbarBase result = null;

		if (!this.dockBars.isEmpty() && id != null && !id.isEmpty()) {
			for (int i = 0; i < this.dockBars.size(); i++) {
				XMLDockbarBase dockBar = this.dockBars.get(i);
				if (id.equalsIgnoreCase(dockBar.getID())) {
					result = dockBar;
					break;
				}
			}
		}
		return result;
	}
}
