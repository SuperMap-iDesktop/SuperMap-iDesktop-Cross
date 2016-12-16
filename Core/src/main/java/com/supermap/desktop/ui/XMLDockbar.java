package com.supermap.desktop.ui;

import com.sun.javaws.jnl.XMLUtils;
import com.supermap.desktop.Application;
import com.supermap.desktop.Plugin;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop.enums.DockSite;
import com.supermap.desktop.enums.DockState;
import com.supermap.desktop.enums.XMLCommandType;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.XmlUtilities;
import org.osgi.framework.Bundle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;

public class XMLDockbar extends XMLCommand {
	private String title = "";
	private String controlClass = null;
	private DockPath dockPath = new DockPath();

	public XMLDockbar(PluginInfo pluginInfo, XMLCommandBase group) {
		super(pluginInfo, group);
		this.commandType = XMLCommandType.DOCKBAR;
	}

	@Override
	public boolean initialize(Element element) {
		boolean result = super.initialize(element);

		if (!result) {
			return false;
		}

		if (element.hasAttribute(g_AttributionTitle)) {
			this.title = element.getAttribute(g_AttributionTitle);
		}

		if (element.hasAttribute(g_AttributionComponent)) {
			this.controlClass = element.getAttribute(g_AttributionComponent);
		}

		if (StringUtilities.isNullOrEmpty(this.controlClass)) {
			return false;
		}
		this.title = StringUtilities.isNullOrEmpty(this.title) ? this.controlClass : this.title;
		return loadPath((Element) XmlUtilities.getChildElementNodeByName(element, g_NodeDockPath));
	}

	private boolean loadPath(Element element) {
		boolean result = false;

		if (element.hasAttribute(g_AttributionRatio)) {
			String strRatio = element.getAttribute(g_AttributionRatio);
			double ratio = 0.5;
			try {
				ratio = Double.parseDouble(strRatio);
			} catch (NumberFormatException e) {
				ratio = 0.5;
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
			this.dockPath.setRatio(ratio);
		}

		// 获取路径
		Element[] directionNodes = XmlUtilities.getChildElementNodesByName(element, g_NodeDirection);
		for (int i = 0; i < directionNodes.length; i++) {
			Element directionNode = directionNodes[i];
			this.dockPath.addDirection(Direction.valueOf(directionNode.getNodeValue()));
		}

		// 配置文件没有配置，默认 left
		if (this.dockPath.getDepth() < 1) {
			this.dockPath.addDirection(Direction.LEFT);
		}
		return result;
	}

	public Component CreateComponent() {
		Component result = null;

		try {
			if ((this.getPluginInfo().getBundleName() == null || this.getPluginInfo().getBundleName().isEmpty()) && this.getPluginInfo() != null) {
				this.getPluginInfo().setBundleName(this.getPluginInfo().getBundleName());
			}

			if (this.getPluginInfo().getBundleName() == null || this.getPluginInfo().getBundleName().isEmpty()) {
				return null;
			}

			Bundle bundle = null;
			for (int i = 0; i < Application.getActiveApplication().getPluginManager().getCount(); i++) {
				Plugin plugin = Application.getActiveApplication().getPluginManager().get(i);
				if (plugin.getName().equalsIgnoreCase(this.getPluginInfo().getName())) {
					bundle = plugin.getBundle();
				}
			}

			if (bundle != null) {
				Class<?> controlBundleClass = bundle.loadClass(this.getControlClass());
				if (controlBundleClass != null) {
					result = (Component) controlBundleClass.newInstance();
				}
			}

			if (result == null) {
				result = (Component) Class.forName(this.controlClass).newInstance();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	@Override
	public XMLCommandBase clone(XMLCommandBase parent) {
		XMLDockbar result = null;

		try {
			result = (XMLDockbar) createNew(parent);
			if (result != null) {
				result.setID(getID());
				result.setTitle(getTitle());
				result.setVisible(getVisible());
				result.setControlClass(getControlClass());
				result.getDockPath().init(getDockPath());
			}
		} catch (Exception e) {
			result = null;
		}
		return result;
	}

	@Override
	public XMLCommandBase saveToPluginInfo(PluginInfo pluginInfo, XMLCommandBase parent) {
		XMLDockbar result = null;

		try {
			if (this.getPluginInfo() == pluginInfo) {
				result = (XMLDockbar) createNew(pluginInfo, parent);
			}

			if (result != null) {
				result.setID(getID());
				result.setTitle(getTitle());
				result.setVisible(getVisible());
				result.setControlClass(getControlClass());
				result.getDockPath().init(getDockPath());
				result.getPluginInfo().setBundleName(getPluginInfo().getBundleName());
			}
		} catch (Exception e) {
			result = null;
		}
		return result;
	}

	@Override
	public Element toXML(Document document) {
		Element dockbarNode = null;

		try {
			if (document != null) {
//				dockbarNode = document.createElement(g_NodeDockbar);
//				dockbarNode.setAttribute(g_AttributionIndex, Integer.toString(this.getIndex()));
//				dockbarNode.setAttribute(g_AttributionLabel, this.getLabel());
//				dockbarNode.setAttribute(g_AttributionVisible, Boolean.toString(this.getVisible()));
//				dockbarNode.setAttribute(g_AttributionControl, this.getControlClass());
//				dockbarNode.setAttribute(g_AttributionBundleName, this.getPluginInfo().getBundleName());
//				dockbarNode.setAttribute(g_AttributionHelpURL, this.getHelpURL());
//				dockbarNode.setAttribute(g_AttributionCustomProperty, this.getCustomProperty());
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return dockbarNode;
	}

	@Override
	protected boolean doRemove() {
		boolean result = false;

		try {
			result = ((XMLDockbars) this.getParent()).getDockbars().remove(this);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	/**
	 * 创建一个新的对象
	 *
	 * @param parent 指定的父容器
	 * @return
	 */
	private XMLCommandBase createNew(XMLCommandBase parent) {
		return new XMLDockbar(getPluginInfo(), parent);
	}

	private XMLCommandBase createNew(PluginInfo pluginInfo, XMLCommandBase parent) {
		return new XMLDockbar(pluginInfo, parent);
	}

	@Override
	public boolean getIsContainer() {
		return false;
	}

	public String getControlClass() {
		return this.controlClass;
	}

	public void setControlClass(String controlClass) {
		this.controlClass = controlClass;
	}

	public String getTitle() {
		return title;
	}

	public DockPath getDockPath() {
		return dockPath;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
