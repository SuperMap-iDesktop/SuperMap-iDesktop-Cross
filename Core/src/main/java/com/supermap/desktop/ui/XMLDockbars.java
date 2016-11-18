package com.supermap.desktop.ui;

import com.supermap.desktop.Application;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop.enums.XMLCommandType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class XMLDockbars extends XMLCommandBase {
	private ArrayList<XMLDockbar> dockBars = null;

	/*为空则表示没有 Dockbar 配置，有实例对象存在则表示有 Dockbar*/
	private DockConstraint dockConstraint = null;

	public XMLDockbars() {
		this.commandType = XMLCommandType.DOCKBARS;
		this.dockBars = new ArrayList<XMLDockbar>();
	}

	public XMLDockbars(PluginInfo pluginInfo) {
		super(pluginInfo);
		this.commandType = XMLCommandType.DOCKBARS;
		this.dockBars = new ArrayList<XMLDockbar>();
	}

	public DockConstraint getDockConstraint() {
		return this.dockConstraint == null ? new DockConstraint() : this.dockConstraint;
	}

	public boolean load(Element element) {
		boolean result = false;

		try {
			// 查找包含DockBar 的节点
			NodeList nodes = element.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node barNode = nodes.item(i);
				if (barNode instanceof Element) {
					XMLDockbar xmlDockbar = null;
					if (barNode.getNodeName().equalsIgnoreCase(g_NodeDockbar)) {
						xmlDockbar = new XMLDockbar(this.getPluginInfo(), this);
					}

					if (xmlDockbar != null) {
						xmlDockbar.initialize((Element) barNode);
						this.dockBars.add(xmlDockbar);
						this.dockConstraint.install(xmlDockbar);
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
			XMLDockbar bar = otherBars.dockBars.get(i);
			if (bar instanceof XMLDockbar) {
				if (bar.getID() != null && !bar.getID().isEmpty() && bar.getID().length() != 0) {
					XMLDockbar preBar = this.getDockBar(bar.getID());
					if (preBar == null) {
						bar.copyTo(this);
					}
				} else {
					bar.copyTo(this);
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
			this.dockBars.add((XMLDockbar) subItem);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void insertSubItem(XMLCommandBase preItem, XMLCommandBase insertItem) {
		try {
			XMLDockbar preDockBar = (XMLDockbar) preItem;
			XMLDockbar insertDockBar = (XMLDockbar) insertItem;

			if (insertDockBar != null) {
				int insertIndex = 0;
				insertDockBar.setIndex(0);

				// 如果前一个元素为空，就默认插入到最前
				if (preDockBar != null) {
					if (!this.dockBars.contains(preDockBar)) {
						throw new Exception("Error.");
					}

					insertIndex = this.dockBars.indexOf(preDockBar) + 1;
					insertDockBar.setIndex(preDockBar.getIndex() + 1);
				}

				this.dockBars.add(insertIndex, insertDockBar);

				// 如果不是插入到最后，那么需要比较后面子项的 commandIndex 进行递增
				if (insertIndex + 1 < this.dockBars.size() && (insertDockBar.getIndex() >= this.dockBars.get(insertIndex + 1).getIndex())) {
					for (int i = insertIndex + 1; i < this.dockBars.size(); i++) {
						this.dockBars.get(i).setIndex((i - insertIndex) + insertDockBar.getIndex());
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
		XMLDockbar result = null;

		try {
			result = (XMLDockbar) this.getXMLCreator().createElement(commandType);
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
					XMLDockbar resultDockBar = (XMLDockbar) this.dockBars.get(i).saveToPluginInfo(pluginInfo, result);
					if (resultDockBar != null) {
						result.getDockbars().add(resultDockBar);
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

	public ArrayList<XMLDockbar> getDockbars() {
		return dockBars;
	}

	public XMLDockbar get(int index) {
		return this.dockBars.get(index);
	}

	private XMLDockbar getDockBar(String id) {
		XMLDockbar result = null;

		if (!this.dockBars.isEmpty() && id != null && !id.isEmpty()) {
			for (int i = 0; i < this.dockBars.size(); i++) {
				XMLDockbar dockBar = this.dockBars.get(i);
				if (id.equalsIgnoreCase(dockBar.getID())) {
					result = dockBar;
					break;
				}
			}
		}
		return result;
	}
}
