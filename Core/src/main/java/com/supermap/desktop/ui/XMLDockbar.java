package com.supermap.desktop.ui;

import com.supermap.desktop.Application;
import com.supermap.desktop.Plugin;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop.enums.DockSite;
import com.supermap.desktop.enums.DockState;
import com.supermap.desktop.enums.XMLCommandType;
import org.osgi.framework.Bundle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;

public class XMLDockbar extends XMLCommand {
	private static final String SPLIT_COMMA = ",";
	private String controlClass = null;

	public XMLDockbar(PluginInfo pluginInfo, XMLCommandBase group) {
		super(pluginInfo, group);
		this.commandType = XMLCommandType.DOCKBAR;
	}

	@Override
	public boolean initialize(Element element) {
		boolean result = false;

		result = super.initialize(element);

		try {
			this.controlClass = element.getAttribute(g_AttributionControl);
			result = true;
		} catch (Exception e) {
			result = false;
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
				result.setID(this.getID());
				result.setVisible(this.getVisible());
				result.setCustomProperty(this.getCustomProperty());
				result.setLabel(this.getLabel());
				result.setTooltip(this.getTooltip());
				result.setIndex(this.getIndex());
				result.setControlClass(this.controlClass);
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
				result.setID(this.getID());
				result.setVisible(this.getVisible());
				result.setCustomProperty(this.getCustomProperty());
				result.setIndex(this.getIndex());
				result.setLabel(this.getLabel());
				result.setControlClass(this.getControlClass());
				result.getPluginInfo().setBundleName(this.getPluginInfo().getBundleName());
				result.setHelpURL(this.getHelpURL());
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
				dockbarNode = document.createElement(g_NodeDockbar);
				dockbarNode.setAttribute(g_AttributionIndex, Integer.toString(this.getIndex()));
				dockbarNode.setAttribute(g_AttributionLabel, this.getLabel());
				dockbarNode.setAttribute(g_AttributionVisible, Boolean.toString(this.getVisible()));
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

	public static String convertToString(Dimension size) {
		String result = null;

		try {
			result = Double.toString(size.getWidth()) + SPLIT_COMMA + Double.toString(size.getHeight());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public static String convertToString(Point point) {
		String result = null;

		try {
			result = Double.toString(point.getX()) + SPLIT_COMMA + Double.toString(point.getY());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
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
	protected XMLCommandBase createNew(XMLCommandBase parent) {
		XMLCommandBase result = null;

		if (this instanceof XMLDockbar) {
			result = new XMLDockbar(this.getPluginInfo(), parent);
		}
		return result;
	}

	protected XMLCommandBase createNew(PluginInfo pluginInfo, XMLCommandBase parent) {
		XMLCommandBase result = null;

		if (this instanceof XMLDockbar) {
			result = new XMLDockbar(pluginInfo, parent);
		}
		return result;
	}

	private Point getPoint(String pointString) {
		Point point = new Point();

		try {
			if (!pointString.isEmpty()) {
				String[] strArray = pointString.split(SPLIT_COMMA);
				if (strArray.length == 2) {
					int pointX = Integer.parseInt(strArray[0]);
					int pointY = Integer.parseInt(strArray[1]);
					point = new Point(pointX, pointY);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return point;
	}

	private Dimension getDimension(String dimensionString) {
		Dimension dimension = new Dimension();

		try {
			if (!dimensionString.isEmpty()) {
				String[] strArray = dimensionString.split(SPLIT_COMMA);
				if (strArray.length == 2) {
					int width = Integer.parseInt(strArray[0]);
					int height = Integer.parseInt(strArray[1]);
					dimension = new Dimension(width, height);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return dimension;
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
