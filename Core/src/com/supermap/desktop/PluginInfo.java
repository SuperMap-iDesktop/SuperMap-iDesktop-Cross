package com.supermap.desktop;

import com.supermap.desktop.enums.XMLCommandType;
import com.supermap.desktop.ui.XMLDockbars;
import com.supermap.desktop.ui.XMLMenus;
import com.supermap.desktop.ui.XMLStatusbars;
import com.supermap.desktop.ui.XMLToolbar;
import com.supermap.desktop.ui.XMLToolbars;
import com.supermap.desktop.utilties.XmlUtilties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.FileNotFoundException;

public class PluginInfo {

	private Element frameMenusElement = null;
	private Element toolbarsElement = null;
	private Element statusbarsElement = null;
	private Element dockbarsElement = null;
	private Element contextMenusElement = null;

	public PluginInfo(Element element) {
		this.name = "";
		this.author = "";
		this.url = "";
		this.configLocation = "";
		this.bundleName = "";
		this.description = "";
		this.uiDefinition = "";
		this.hasRecentFile = false;
		this.namespaceURL = "";
		this.enable = true;
		this.FromConfig(element);
	}

	public PluginInfo(PluginInfo pluginInfo) {
		this.setName(pluginInfo.getName());
	}

	String namespaceURL = "";

	public String getNamespaceURL() {
		return this.namespaceURL;
	}

	public void setNamespaceURL(String namespaceURL) {
		this.namespaceURL = namespaceURL;
	}

	private String name = "";

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String author = "";

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	private String url = "";

	public String getURL() {
		return this.url;
	}

	public void setURL(String url) {
		this.url = url;
	}

	private String configLocation = "";

	public String getConfigLocation() {
		return this.configLocation;
	}

	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}

	private String bundleName = "";

	public String getBundleName() {
		return this.bundleName;
	}

	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}

	private String description = "";

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private String uiDefinition = "";

	public String getUIDefinition() {
		return this.uiDefinition;
	}

	public void setUIDefinition(String uiDefinition) {
		this.uiDefinition = uiDefinition;
	}

	private Boolean enable = false;

	public Boolean getEnable() {
		return this.enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	private Boolean hasRecentFile = false;

	public Boolean getHasRecentFile() {
		return this.hasRecentFile;
	}

	public void setHasRecentFile(Boolean hasRecentFile) {
		this.hasRecentFile = hasRecentFile;
	}

	private String recentFile = "";

	public String getRecentFile() {
		return this.recentFile;
	}

	public void setRecentFile(String recentFile) {
		this.recentFile = recentFile;
	}

	private Boolean isCurrent = false;

	public Boolean getIsCurrent() {
		return this.isCurrent;
	}

	public void setHasIsCurrent(Boolean isCurrent) {
		this.isCurrent = isCurrent;
	}

	private String helpLocalRoot = "";

	public String getHelpLocalRoot() {
		return this.helpLocalRoot;
	}

	public void setHelpLocalRoot(String helpLocalRoot) {
		this.helpLocalRoot = helpLocalRoot;
	}

	private String helpOnlineRoot = "";

	public String getHelpOnlineRoot() {
		return this.helpOnlineRoot;
	}

	public void setHelpOnlineRoot(String helpOnlineRoot) {
		this.helpOnlineRoot = helpOnlineRoot;
	}

	private XMLMenus xmlFrameMenus = null;

	public XMLMenus getFrameMenus() {
		return this.xmlFrameMenus;
	}

	private XMLToolbars xmlToolbars = null;

	public XMLToolbars getToolbars() {
		return this.xmlToolbars;
	}

	private XMLStatusbars xmlStatusbars = null;

	public XMLStatusbars getStatusbars() {
		return this.xmlStatusbars;
	}

	private XMLDockbars xmlDockbars = null;

	public XMLDockbars getDockbars() {
		return this.xmlDockbars;
	}

	private XMLMenus xmlContextMenus = null;

	public XMLMenus getContextMenus() {
		return this.xmlContextMenus;
	}

	public Boolean IsValid() {
		Boolean valid = false;
		valid = true;
		return valid;
	}

	public boolean FromConfig(Element element) {
		boolean result = false;

		try {
			if (element.hasAttribute(_XMLTag.g_AttributionName)) {
				this.setName(element.getAttribute(_XMLTag.g_AttributionName));
			}
			if (element.hasAttribute(_XMLTag.g_AttributionAuthor)) {

				this.setAuthor(element.getAttribute(_XMLTag.g_AttributionAuthor));
			}
			if (element.hasAttribute(_XMLTag.g_AttributionDescription)) {

				this.setAuthor(element.getAttribute(_XMLTag.g_AttributionDescription));
			}
			if (element.hasAttribute(_XMLTag.g_AttributionURL)) {
				this.setURL(element.getAttribute(_XMLTag.g_AttributionURL));
			}
			if (element.getNamespaceURI() != null) {
				this.setNamespaceURL(element.getNamespaceURI());
			}
			if (element.hasAttribute(_XMLTag.g_AttributionHelpLocalRoot)) {
				this.setHelpLocalRoot(element.getAttribute(_XMLTag.g_AttributionHelpLocalRoot));
			}
			if (element.hasAttribute(_XMLTag.g_AttributionHelpOnlineRoot)) {
				this.setHelpOnlineRoot(element.getAttribute(_XMLTag.g_AttributionHelpOnlineRoot));
			}

			NodeList nodes = element.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {

				if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element item = (Element) (nodes.item(i));

					if (item.getNodeName().equalsIgnoreCase(_XMLTag.g_Runtime)) {
						this.setBundleName(item.getAttribute(_XMLTag.g_AttributionBundleName));
						if (item.hasAttribute(_XMLTag.g_AttributionEnabled) && "false".equalsIgnoreCase(item.getAttribute(_XMLTag.g_AttributionEnabled))) {
							this.setEnable(false);
						}
					} else if (item.getNodeName().equalsIgnoreCase(_XMLTag.g_NodeFrameMenus)) {
						this.frameMenusElement = item;
					} else if (item.getNodeName().equalsIgnoreCase(_XMLTag.g_NodeToolbars)) {
						this.toolbarsElement = item;
					} else if (item.getNodeName().equalsIgnoreCase(_XMLTag.g_NodeStatusbars)) {
						this.statusbarsElement = item;
					} else if (item.getNodeName().equalsIgnoreCase(_XMLTag.g_NodeDockbars)) {
						this.dockbarsElement = item;
					} else if (item.getNodeName().equalsIgnoreCase(_XMLTag.g_NodeContextMenus)) {
						this.contextMenusElement = item;
					}
				}
			}

			if (this.IsValid()) {
				result = true;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	public Boolean parseUI() {

		this.xmlFrameMenus = new XMLMenus(this, XMLCommandType.FRAMEMENUS);
		this.xmlFrameMenus.load(this.frameMenusElement);

		this.xmlToolbars = new XMLToolbars(this);
		this.xmlToolbars.load(this.toolbarsElement);

		this.xmlContextMenus = new XMLMenus(this, XMLCommandType.CONTEXTMENUS);
		this.xmlContextMenus.load(this.contextMenusElement);

		this.xmlDockbars = new XMLDockbars(this);
		this.xmlDockbars.load(this.dockbarsElement);

		this.xmlStatusbars = new XMLStatusbars(this);
		this.xmlStatusbars.load(this.statusbarsElement);

		return true;
	}

	public void toXML() {
		Element toolbarsElementTemp = null;
		Document document = XmlUtilties.getDocument(this.configLocation);
		if (document != null) {
			Element documentElement = document.getDocumentElement();
			NodeList nodeToolbars = documentElement.getElementsByTagName(_XMLTag.g_NodeToolbars);

			if (nodeToolbars != null && nodeToolbars.getLength() > 0) {
				toolbarsElementTemp = (Element) nodeToolbars.item(0);
			}

			if (this.xmlToolbars != null && toolbarsElementTemp != null) {
				NodeList childNodes = toolbarsElementTemp.getChildNodes();

				for (int i = 0; i < childNodes.getLength(); i++) {
					Node childNode = childNodes.item(i);

					if (childNode != null && childNode.getNodeType() == Node.ELEMENT_NODE) {
						Element childElement = (Element) childNode;

						if (childElement.getNodeName().equalsIgnoreCase(_XMLTag.g_NodeToolbar)) {
							String id = childElement.getAttribute(_XMLTag.g_AttributionID);
							XMLToolbar xmlToolbar = this.xmlToolbars.getToolbar(id);

							if (xmlToolbar != null) {
								childElement.setAttribute(_XMLTag.g_AttributionIndex, Integer.toString(xmlToolbar.getIndex()));

								childElement.setAttribute(_XMLTag.g_AttributionVisible, Boolean.toString(xmlToolbar.getVisible()));
							}
						}
					}
				}

				try {
					XmlUtilties.saveXml(this.configLocation, document, document.getXmlEncoding());
				} catch (FileNotFoundException e) {
					Application.getActiveApplication().getOutput().output(e);
				}
			}
		}
	}

	@Override
	public String toString() {
		return this.getName();
	}
}
