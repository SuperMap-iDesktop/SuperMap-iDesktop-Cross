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

import java.awt.*;
import java.util.ArrayList;

public class XMLDockbar extends XMLCommand {
	private String title = "";
	private String controlClass = null;
	//	private DockPath dockPath;
	private ArrayList<DockPath> dockPaths = new ArrayList<>();

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

//	private boolean loadPath(Element element) {
//		boolean result = false;
//
//		// 获取路径
//		DockPath dockPath = DockPath.ROOT;
//		Element[] directionNodes = XmlUtilities.getChildElementNodesByName(element, g_NodeDirection);
//		if (directionNodes != null && directionNodes.length > 0) {
//			for (int i = 0; i < directionNodes.length; i++) {
//				dockPath = loadDirection(directionNodes[i], dockPath);
//			}
//		} else {
//			// 配置文件没有配置 Direction，则默认为 ROOT.LEFT
//			dockPath = new DockPath();
//			dockPath.setRelateTo(DockPath.ROOT);
//			dockPath.setDirection(Direction.LEFT);
//		}
//		this.dockPath = dockPath;
//
//		return result;
//	}

	private boolean loadPath(Element element) {
		boolean result = false;

		// 获取路径
		Element[] directionNodes = XmlUtilities.getChildElementNodesByName(element, g_NodeDirection);
		if (directionNodes != null && directionNodes.length > 0) {
			for (int i = 0; i < directionNodes.length; i++) {
				this.dockPaths.add(loadDirection(directionNodes[i], null));
			}
		} else {
			// 配置文件没有配置 Direction，则默认为 ROOT.LEFT
			DockPath dockPath = new DockPath();
			dockPath.setDirection(Direction.LEFT);
			this.dockPaths.add(dockPath);
		}

		return result;
	}

	private DockPath loadDirection(Element directionNode, DockPath relateTo) {
		DockPath path = new DockPath();
		path.setDirection(Direction.valueOf(directionNode.getNodeValue()));

		if (directionNode.hasAttribute(g_AttributionRatio)) {
			String strRatio = directionNode.getAttribute(g_AttributionRatio);
			double ratio = 0.5;
			try {
				ratio = Double.parseDouble(strRatio);
			} catch (NumberFormatException e) {
				ratio = 0.5;
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
			path.setRatio(ratio);
		}

		return path;
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

	public DockPath[] getDockPaths() {
		return this.dockPaths.toArray(new DockPath[this.dockPaths.size()]);
	}

//	public DockPath getDockPath() {
//		return dockPath;
//	}

	public void setDockPath(DockPath dockPath) {
//		this.dockPath = dockPath;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
