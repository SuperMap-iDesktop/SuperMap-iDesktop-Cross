package com.supermap.desktop.ui;

import com.supermap.desktop.Application;
import com.supermap.desktop.Plugin;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop.enums.XMLCommandType;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.XmlUtilities;
import org.osgi.framework.Bundle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.awt.*;
import java.util.ArrayList;

public class XMLDockbar extends XMLCommand {
	private final static String ATTRIBUTION_DOCK_DIRECTION = "dockDirection";
	private final static String ATTRIBUTION_DOCK_STATE = "dockState";
	private String title = "";
	private String controlClass = null;
	//	private DockPath dockPath;
	private String dockDirection = "rightTop";
	private String dockState = "normal"; // normal minimized maximized float

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

		if (element.hasAttribute(ATTRIBUTION_DOCK_DIRECTION)) {
			this.dockDirection = element.getAttribute(ATTRIBUTION_DOCK_DIRECTION);
		}

		if (element.hasAttribute(ATTRIBUTION_DOCK_STATE)) {
			this.dockState = element.getAttribute(ATTRIBUTION_DOCK_STATE);
		}

		if (StringUtilities.isNullOrEmpty(this.dockState)) {
			this.dockState = "normal";
		}

		if (StringUtilities.isNullOrEmpty(this.controlClass)) {
			return false;
		}
		this.title = StringUtilities.isNullOrEmpty(this.title) ? this.controlClass : this.title;
		return true;
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
				result.setDockDirection(getDockDirection());
				result.setDockState(getDockState());
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
				result.setDockDirection(getDockDirection());
				result.setDockState(getDockState());
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

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDockDirection() {
		return dockDirection;
	}

	public void setDockDirection(String dockDirection) {
		this.dockDirection = dockDirection;
	}

	public String getDockState() {
		return dockState;
	}

	public void setDockState(String dockState) {
		this.dockState = dockState;
	}
}
