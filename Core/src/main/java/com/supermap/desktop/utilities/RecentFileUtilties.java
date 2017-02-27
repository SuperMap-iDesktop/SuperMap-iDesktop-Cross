package com.supermap.desktop.utilities;

import com.supermap.data.Datasource;
import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop._XMLTag;
import com.supermap.desktop.implement.SmMenu;
import com.supermap.desktop.implement.SmMenuItem;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.XMLCommand;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;

import static com.supermap.desktop._XMLTag.LINUX_RECENT_FILE_XML;
import static com.supermap.desktop._XMLTag.RECENT_FILE_DATASOURCE;
import static com.supermap.desktop._XMLTag.RECENT_FILE_GROUP;
import static com.supermap.desktop._XMLTag.RECENT_FILE_WORKSPACE;

/**
 * @author XiaJT
 */
public class RecentFileUtilties {

	private static PluginInfo datasourcePluginInfo;
	private static PluginInfo workspacePluginInfo;
	private static SmMenu recentDatasourceMenu = null;
	private static SmMenu recentWorkspaceMenu = null;

	public static final String FILE_TYPE_DATASOURCE = "datasource";
	public static final String FILE_TYPE_WORKSPACE = "workspace";
	private static final int maxRecentFileCount = 8;

	private RecentFileUtilties() {

	}


	private static boolean isRecentFileExist(String recentFilePath) {
		if (!StringUtilities.isNullOrEmpty(recentFilePath)) {
			File file = new File(recentFilePath);
			if (!file.exists()) {
				try {
					if (file.getParentFile().exists() || file.getParentFile().mkdirs()) {
						if (file.createNewFile()) {
							FileUtilities.writeToFile(file, CoreProperties.getString("String_RecentFileString"));
							return true;
						}
					}
				} catch (Exception e) {
					return false;
				}
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * 将指定数据源添加到最近文件列表中
	 */
	public static void addDatasourceToRecentFile(Datasource datasource) {
		if (datasource != null) {
			String filePath = datasource.getConnectionInfo().getServer().replace("\\", "/");
			addFileToRecentFile(datasourcePluginInfo, recentDatasourceMenu, FILE_TYPE_DATASOURCE, filePath);
		}

	}

	/**
	 * 将指定数据源添加到最近文件列表中
	 */
	public static void addWorkspaceToRecentFile(Workspace workspace) {
		if (workspace != null) {
			String filePath = workspace.getConnectionInfo().getServer().replace("\\", "/");
			addFileToRecentFile(workspacePluginInfo, recentWorkspaceMenu, FILE_TYPE_WORKSPACE, filePath);
		}
	}

	private static void addFileToRecentFile(PluginInfo pluginInfo, SmMenu menu, String fileType, String filePath) {
		if (menu != null) {
			removeRecentFile(fileType, filePath);

			XMLCommand xmlCommand = new XMLCommand(pluginInfo);
			xmlCommand.setCtrlActionClass("CtrlActionRecentFiles");
			xmlCommand.setLabel(filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length()));
			xmlCommand.setTooltip(filePath);
			SmMenuItem menuItem = new SmMenuItem(null, xmlCommand, menu);
			menu.insert((IBaseItem) menuItem, 0);
			if (menu.getItemCount() > maxRecentFileCount) {
				removeFormMenu(fileType, menu.getItem(maxRecentFileCount).getToolTipText());
			}
			saveRecentFile(fileType, filePath);
		}
	}


	public static void saveRecentFile(String fileType, String filePath) {
		try {
			Document recentFileDocument = getRecentFileDocument();
			if (recentFileDocument != null) {
				Element element = getElement(recentFileDocument, fileType);
				if (element != null) {
					NodeList childNodes = element.getChildNodes();
					ArrayList<Element> recentFiles = new ArrayList<>();
					for (int j = childNodes.getLength() - 1; j >= 0; j--) {
						if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
							Element childItem = (Element) (childNodes.item(j));
							recentFiles.add(0, childItem);
						}
						element.removeChild(childNodes.item(j));
					}
					Element button = createDefaultButtonElement(recentFileDocument);
					button.setAttribute("label", filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length()));
					button.setAttribute("screenTip", filePath);
					element.appendChild(button);
					for (int i = 0; i < recentFiles.size(); i++) {
						if (i >= 49) {
							// 天衍四九 (.net限制最多50条，这边保持一致)
							break;
						}
						Element recentFile = recentFiles.get(i);
						element.appendChild(recentFile);
					}
					XmlUtilities.saveXml(getRecentFilePath(), recentFileDocument, recentFileDocument.getXmlEncoding());
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private static Element createDefaultButtonElement(Document recentFileDocument) {
		if (recentFileDocument != null) {
			Element button = recentFileDocument.createElement("button");
			button.setAttribute("visible", "true");
			button.setAttribute("checkState", "false");
			button.setAttribute("onAction", "SuperMap.Desktop._CtrlActionRecentFiles");
			button.setAttribute("assemblyName", "SuperMap.Desktop.UI.Controls.dll");
			button.setAttribute("style", "text");
			button.setAttribute("image", "");
			button.setAttribute("size", "normal");
			button.setAttribute("lock", "false");
			return button;
		}
		return null;
	}

	public static void removeRecentFile(String fileType, String filePath) {
		try {
			Document document = getRecentFileDocument();
			removeFormMenu(fileType, filePath);

			String recentFilePath = getRecentFilePath();
			if (document != null) {
				Element fileElement = getElement(document, fileType);
				Element[] buttons = XmlUtilities.getChildElementNodesByName(fileElement, "button");
				if (buttons != null) {
					for (int i = buttons.length - 1; i >= 0; i--) {
						Element button = buttons[i];
						if (button.getAttribute("screenTip").equals(filePath)) {
							fileElement.removeChild(button);
						}
					}
				}
				XmlUtilities.saveXml(recentFilePath, document, document.getXmlEncoding());
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private static void removeFormMenu(String fileType, String filePath) {
		SmMenu menu = fileType.equals(FILE_TYPE_WORKSPACE) ? recentWorkspaceMenu : recentDatasourceMenu;
		for (IBaseItem item : menu.items()) {
			if (((SmMenuItem) item).getToolTipText().equals(filePath)) {
				menu.remove(item);
			}
		}
	}

	public static void initRecentFileMenu(String fileType) {
		try {
			Element fileElement;
			PluginInfo pluginInfo;
			if (fileType.equals(FILE_TYPE_WORKSPACE)) {
				pluginInfo = workspacePluginInfo;
			} else {
				pluginInfo = datasourcePluginInfo;
			}
			SmMenu menu = fileType.equals(FILE_TYPE_WORKSPACE) ? recentWorkspaceMenu : recentDatasourceMenu;

			Document document = getRecentFileDocument();
			fileElement = getElement(document, fileType);
			if (fileElement == null) {
				return;
			}
			Element[] datasources = XmlUtilities.getChildElementNodesByName(fileElement, "button");
			for (int i = 0; i < datasources.length && i < maxRecentFileCount; i++) {
				Element element = datasources[i];
				String fileName = element.getAttribute("label");
				String filePath = element.getAttribute("screenTip");
				XMLCommand xmlCommand = new XMLCommand(pluginInfo);
				xmlCommand.setCtrlActionClass("CtrlActionRecentFiles");
				xmlCommand.setLabel(fileName);
				xmlCommand.setTooltip(filePath);
				SmMenuItem menuItem = new SmMenuItem(null, xmlCommand, menu);
				menu.add((IBaseItem) menuItem);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private static Element getElement(Document document, String fileType) {
		String findId = fileType.equals(FILE_TYPE_WORKSPACE) ? RECENT_FILE_WORKSPACE : RECENT_FILE_DATASOURCE;
		String findLabel = fileType.equals(FILE_TYPE_WORKSPACE) ? CoreProperties.getString("String_RecentFileWorkspace") : CoreProperties.getString("String_RecentFileDatasource");
		Element rootNode = document.getDocumentElement();
		if (rootNode != null) {
			Element[] elements = XmlUtilities.getChildElementNodesByName(rootNode, RECENT_FILE_GROUP);
			for (Element element : elements) {
				String id = element.getAttribute("id");
				if (!StringUtilities.isNullOrEmpty(id) && id.equals(findId)) {
					return element;
				}
				String label = element.getAttribute("label");
				if (!StringUtilities.isNullOrEmptyString(label) && label.equals(findLabel)) {
					return element;
				}
			}
		}
		return null;
	}

	private static String getRecentFilePath() {
		return SystemPropertyUtilities.isWindows() ? _XMLTag.WINDOWS_RECENT_FILE_XML : FileUtilities.getAppDataPath() + LINUX_RECENT_FILE_XML;
	}

	private static Document getRecentFileDocument() {
		String recentFilePath = getRecentFilePath();
		if (!StringUtilities.isNullOrEmpty(recentFilePath) && isRecentFileExist(recentFilePath)) {
			return XmlUtilities.getDocument(recentFilePath);
		}
		return null;
	}

	//region getter and setter
	public static PluginInfo getDatasourcePluginInfo() {
		return datasourcePluginInfo;
	}

	public static void setDatasourcePluginInfo(PluginInfo datasourcePluginInfo) {
		RecentFileUtilties.datasourcePluginInfo = datasourcePluginInfo;
	}

	public static PluginInfo getWorkspacePluginInfo() {
		return workspacePluginInfo;
	}

	public static void setWorkspacePluginInfo(PluginInfo workspacePluginInfo) {
		RecentFileUtilties.workspacePluginInfo = workspacePluginInfo;
	}

	public static SmMenu getRecentDatasourceMenu() {
		return recentDatasourceMenu;
	}

	public static void setRecentDatasourceMenu(SmMenu recentDatasourceMenu) {
		RecentFileUtilties.recentDatasourceMenu = recentDatasourceMenu;
	}


	public static SmMenu getRecentWorkspaceMenu() {
		return recentWorkspaceMenu;
	}

	public static void setRecentWorkspaceMenu(SmMenu recentWorkspaceMenu) {
		RecentFileUtilties.recentWorkspaceMenu = recentWorkspaceMenu;
	}

	public static void fillMenu(String fileType) {
		SmMenu smMenu;
		if (fileType.equals(FILE_TYPE_WORKSPACE)) {
			smMenu = recentWorkspaceMenu;
		} else {
			smMenu = recentDatasourceMenu;
		}
		Document document = XmlUtilities.getDocument(getRecentFilePath());
		Element element = getElement(document, fileType);
		Element[] buttons = XmlUtilities.getChildElementNodesByName(element, "button");
		if (smMenu.getCount() < maxRecentFileCount && buttons != null && buttons.length >= maxRecentFileCount) {
			PluginInfo pluginInfo;
			if (fileType.equals(FILE_TYPE_WORKSPACE)) {
				pluginInfo = workspacePluginInfo;
			} else {
				pluginInfo = datasourcePluginInfo;
			}
			for (int i = smMenu.getCount(); i < maxRecentFileCount; i++) {
				Element button = buttons[i];
				String fileName = button.getAttribute("label");
				String filePath = button.getAttribute("screenTip");
				XMLCommand xmlCommand = new XMLCommand(pluginInfo);
				xmlCommand.setCtrlActionClass("CtrlActionRecentFiles");
				xmlCommand.setLabel(fileName);
				xmlCommand.setTooltip(filePath);
				SmMenuItem menuItem = new SmMenuItem(null, xmlCommand, smMenu);
				smMenu.add((IBaseItem) menuItem);
			}
		}
	}
	//endregion
}
