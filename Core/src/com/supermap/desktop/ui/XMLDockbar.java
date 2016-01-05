package com.supermap.desktop.ui;

import java.awt.Component;

import org.osgi.framework.Bundle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.supermap.desktop.Application;
import com.supermap.desktop.Plugin;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop.PluginManager;
import com.supermap.desktop.enums.DockSite;
import com.supermap.desktop.enums.DockState;
import com.supermap.desktop.enums.XMLCommandType;

public class XMLDockbar extends XMLDockbarBase {
	private String controlClass = null;

	public XMLDockbar(PluginInfo pluginInfo, XMLCommandBase group) {
		super(pluginInfo, group);
		this.commandType = XMLCommandType.DOCKBAR;
	}

	@Override
	public boolean initialize(Element element) {
		super.initialize(element);

		try {
			this.controlClass = element.getAttribute(g_AttributionControl);
		} catch (Exception e) {
			// TODO: handle exception
		}
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
			result = (XMLDockbar) super.clone(parent);
			result.setControlClass(this.controlClass);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	@Override
	public XMLCommandBase saveToPluginInfo(PluginInfo pluginInfo, XMLCommandBase parent) {
		XMLDockbar result = null;

		try {
			result = (XMLDockbar) super.saveToPluginInfo(pluginInfo, parent);
			if (result != null) {
				result.setLabel(this.getLabel());
				result.setControlClass(this.getControlClass());
				result.getPluginInfo().setBundleName(this.getPluginInfo().getBundleName());
				result.setHelpURL(this.getHelpURL());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	@Override
	public Element toXML(Document document) {
		Element dockbarNode = null;

		try {
			if (document != null) {
				dockbarNode = document.createElement(g_NodeDockbar);
				dockbarNode.setAttribute(g_AttributionIndex, Integer.toString(this.getIndex()));
				dockbarNode.setAttribute(g_AttributionLabel, this.getLabel());
				dockbarNode.setAttribute(g_AttributionDockstate, DockState.toString(this.getDockState()));
				dockbarNode.setAttribute(g_AttributionDocksite, DockSite.toString(this.getDockSite()));
				dockbarNode.setAttribute(g_AttributionVisible, Boolean.toString(this.getVisible()));
				dockbarNode.setAttribute(g_AttributionAutoHide, Boolean.toString(this.isAutoHide()));
				dockbarNode.setAttribute(g_AttributionFloatingLocation, convertToString(this.getFloatingLoation()));
				dockbarNode.setAttribute(g_AttributionSize, convertToString(this.getPaneSize()));
				dockbarNode.setAttribute(g_AttributionControl, this.getControlClass());
				dockbarNode.setAttribute(g_AttributionBundleName, this.getPluginInfo().getBundleName());
				dockbarNode.setAttribute(g_AttributionHelpURL, this.getHelpURL());
				dockbarNode.setAttribute(g_AttributionCustomProperty, this.getCustomProperty());
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return dockbarNode;
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
}
