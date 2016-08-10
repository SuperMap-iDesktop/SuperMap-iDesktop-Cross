package com.supermap.desktop.ui;

import com.supermap.desktop.Interface.IXMLCreator;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop.enums.DockGroupStyle;
import com.supermap.desktop.enums.DockSite;
import com.supermap.desktop.enums.DockState;
import com.supermap.desktop.enums.XMLCommandType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class XMLDockbarGroup extends XMLDockbarBase {
	private ArrayList<XMLDockbarBase> dockBars = null;
	private DockGroupStyle dockGroupStyle = DockGroupStyle.TABWiINDOW;
	private IXMLCreator xmlCreator = null;

	public XMLDockbarGroup(PluginInfo pluginInfo, XMLCommandBase group) {
		super(pluginInfo, group);
		this.commandType = XMLCommandType.DOCKBARGROUP;
		this.dockBars = new ArrayList<XMLDockbarBase>();
	}

	@Override
	public boolean initialize(Element element) {
		boolean result = false;

		result = super.initialize(element);
		try {
			this.dockGroupStyle = DockGroupStyle.getDockGroupStyle(element.getAttribute(g_AttributionGroupStyle));
		} catch (Exception e) {
			// TODO: handle exception
		}

		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			XMLDockbarBase XMLDockbarBase = null;
			Node childNode = childNodes.item(i);

			if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				if (childNode.getNodeName().equalsIgnoreCase(g_NodeDockbar)) {
					XMLDockbarBase = new XMLDockbar(this.getPluginInfo(), this);
				} else if (childNode.getNodeName().equalsIgnoreCase(g_NodeDockbarGroup)) {
					XMLDockbarBase = new XMLDockbarGroup(this.getPluginInfo(), this);
				}
			}

			if (XMLDockbarBase != null) {
				XMLDockbarBase.initialize((Element) childNode);
				this.dockBars.add(XMLDockbarBase);
			}
		}
		return result;
	}

	public boolean merge(XMLDockbarGroup otherGroup) {
		ArrayList<XMLDockbarBase> otherDockBars = otherGroup.getDockBars();

		for (int i = 0; i < otherDockBars.size(); i++) {
			XMLDockbarBase otherBar = otherDockBars.get(i);

			if (otherBar instanceof XMLDockbar) {
				if (!otherBar.getID().isEmpty() && otherBar.getID().length() != 0) {
					XMLDockbarBase preBar = this.getItem(otherBar.getID());
					if (preBar == null) {
						otherBar.copyTo(this);
					}
				} else {
					otherBar.copyTo(this);
				}
			} else if (otherBar instanceof XMLDockbarGroup && (!otherBar.getID().isEmpty() && otherBar.getID().length() != 0)) {
				XMLDockbarBase preBarGroup = this.getItem(otherBar.getID());
				if (preBarGroup == null || preBarGroup instanceof XMLDockbar) {
					otherBar.copyTo(this);
				} else if (preBarGroup instanceof XMLDockbarGroup) {
					((XMLDockbarGroup) preBarGroup).merge((XMLDockbarGroup) otherBar);
				}
			}
		}
		return true;
	}

	@Override
	public XMLCommandBase clone(XMLCommandBase parent) {
		XMLDockbarGroup result = null;

		try {
			result = (XMLDockbarGroup) super.clone(parent);
			result.setDockGroupStyle(this.getDockGroupStyle());
			for (int i = 0; i < this.dockBars.size(); i++) {
				this.dockBars.get(i).copyTo(result);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	@Override
	public XMLCommandBase createElement(XMLCommandType commandType) {
		XMLDockbarBase result = null;

		try {
			result = (XMLDockbarBase) this.xmlCreator.createElement(commandType);
			this.dockBars.add(result);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	@Override
	public IXMLCreator getXMLCreator() {
		if (this.xmlCreator == null) {
			this.xmlCreator = new XMLDockbarBaseCreator(this);
			this.xmlCreator.setDefaultValueCreator(new DockBarDefaultValueCreator(this));
		}
		return xmlCreator;
	}

	@Override
	public XMLCommandBase saveToPluginInfo(PluginInfo pluginInfo, XMLCommandBase parent) {
		XMLDockbarGroup result = null;

		try {
			result = (XMLDockbarGroup) super.saveToPluginInfo(pluginInfo, parent);
			if (result != null) {
				result.setDockGroupStyle(this.getDockGroupStyle());
				for (int i = 0; i < this.dockBars.size(); i++) {
					XMLDockbarBase resultDockBar = (XMLDockbarBase) this.dockBars.get(i).saveToPluginInfo(pluginInfo, result);
					if (resultDockBar != null) {
						result.dockBars.add(resultDockBar);
					}
				}
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
				// 如果不是插入到最后，那么需要比较后面子项的 CommandIndex 进行递增
				if (insertIndex + 1 < this.dockBars.size() && insertDockBarBase.getIndex() >= this.dockBars.get(insertIndex + 1).getIndex()) {
					for (int i = insertIndex + 1; i < this.dockBars.size(); i++) {
						this.dockBars.get(i).setIndex((i - insertIndex) + insertDockBarBase.getIndex());
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public Element toXML(Document document) {
		Element dockBarGroupNode = null;
		try {
			if (document != null) {
				dockBarGroupNode = document.createElement(g_NodeDockbarGroup);
				dockBarGroupNode.setAttribute(g_AttributionID, this.getID());
				dockBarGroupNode.setAttribute(g_AttributionGroupStyle, DockGroupStyle.toString(this.getDockGroupStyle()));
				dockBarGroupNode.setAttribute(g_AttributionDockstate, DockState.toString(this.getDockState()));
				dockBarGroupNode.setAttribute(g_AttributionDocksite, DockSite.toString(this.getDockSite()));
				dockBarGroupNode.setAttribute(g_AttributionVisible, Boolean.toString(this.getVisible()).toLowerCase());
				dockBarGroupNode.setAttribute(g_AttributionAutoHide, Boolean.toString(this.isAutoHide()).toLowerCase());
				dockBarGroupNode.setAttribute(g_AttributionFloatingLocation, convertToString(this.getFloatingLoation()));
				dockBarGroupNode.setAttribute(g_AttributionSize, convertToString(this.getPaneSize()));
				dockBarGroupNode.setAttribute(g_AttributionCustomProperty, this.getCustomProperty());

				for (int i = 0; i < this.dockBars.size(); i++) {
					Element dockBarNode = this.dockBars.get(i).toXML(document);
					dockBarGroupNode.appendChild(dockBarNode);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return dockBarGroupNode;
	}

	public DockGroupStyle getDockGroupStyle() {
		return this.dockGroupStyle;
	}

	public void setDockGroupStyle(DockGroupStyle dockGroupStyle) {
		this.dockGroupStyle = dockGroupStyle;
	}

	public ArrayList<XMLDockbarBase> getDockBars() {
		return this.dockBars;
	}

	public XMLDockbarBase getItem(String id) {
		XMLDockbarBase result = null;

		try {
			for (int i = 0; i < this.dockBars.size(); i++) {
				XMLDockbarBase dockBar = this.dockBars.get(i);
				if (!id.isEmpty() && id.equalsIgnoreCase(dockBar.getID())) {
					result = dockBar;
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
}
