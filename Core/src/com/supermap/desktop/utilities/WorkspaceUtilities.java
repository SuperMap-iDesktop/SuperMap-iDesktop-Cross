package com.supermap.desktop.utilities;

import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.data.EngineType;
import com.supermap.data.ErrorInfo;
import com.supermap.data.Toolkit;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.desktop.Application;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormLayout;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.PluginInfo;
import com.supermap.desktop._XMLTag;
import com.supermap.desktop.enums.OpenWorkspaceResult;
import com.supermap.desktop.event.SaveWorkspaceEvent;
import com.supermap.desktop.event.SaveWorkspaceListener;
import com.supermap.desktop.implement.SmMenu;
import com.supermap.desktop.implement.SmMenuItem;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.XMLCommand;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 工作空间工具类
 *
 * @author highsad
 */
public class WorkspaceUtilities {

	private WorkspaceUtilities() {
		// 工具类，不提供构造方法
	}

	private static transient CopyOnWriteArrayList<SaveWorkspaceListener> saveWorkspaceListeners = new CopyOnWriteArrayList<SaveWorkspaceListener>();
	private static PluginInfo pluginInfo;

	public static PluginInfo getPluginInfo() {
		return WorkspaceUtilities.pluginInfo;
	}

	public static void setPluginInfo(PluginInfo pluginInfo) {
		WorkspaceUtilities.pluginInfo = pluginInfo;
	}

	private static SmMenu recentWorkspaceMenu = null;

	public static SmMenu getRecentWorkspaceMenu() {
		return recentWorkspaceMenu;
	}

	public static void setRecentWorkspaceMenu(SmMenu recentWorkspaceMenu) {
		WorkspaceUtilities.recentWorkspaceMenu = recentWorkspaceMenu;
	}

	public static synchronized void addSaveWorkspaceListener(SaveWorkspaceListener listener) {
		if (saveWorkspaceListeners == null) {
			saveWorkspaceListeners = new CopyOnWriteArrayList<>();
		}

		if (!saveWorkspaceListeners.contains(listener)) {

			saveWorkspaceListeners.add(listener);
		}
	}

	public static void removeSaveWorkspaceListener(SaveWorkspaceListener listener) {
		if (saveWorkspaceListeners != null && saveWorkspaceListeners.contains(listener)) {
			saveWorkspaceListeners.remove(listener);
		}
	}

	public static boolean fireSaveWorkspaceEvent(boolean isSaveCurrentWorkspace, boolean isCloseAllOpenedWindows, boolean isCloseWorkspace,
	                                             WorkspaceConnectionInfo info) {
		boolean result = true;
		try {
			SaveWorkspaceEvent event = new SaveWorkspaceEvent(Application.getActiveApplication().getMainFrame(), isSaveCurrentWorkspace,
					isCloseAllOpenedWindows, isCloseWorkspace, info);
			fireSaveWorkspaceEvent(event);
			result = event.getHandled();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	private static void fireSaveWorkspaceEvent(SaveWorkspaceEvent event) {
		if (saveWorkspaceListeners != null) {
			Iterator<SaveWorkspaceListener> iter = saveWorkspaceListeners.iterator();
			while (iter.hasNext()) {
				SaveWorkspaceListener listener = iter.next();
				listener.saveWorkspace(event);
			}
		}
	}

	/**
	 * 打开所有类型的工作空间，建议其他地方在调用的时候直接构造WorkspaceConnectionInfo 调用该方法。
	 *
	 * @param info  工作空间连接信息
	 * @param first 是否是第一次打开
	 * @return 打开工作空间的结果
	 */
	public static OpenWorkspaceResult openWorkspace(WorkspaceConnectionInfo info, boolean first) {
		OpenWorkspaceResult result = OpenWorkspaceResult.FAILED_PASSWORD_WRONG;
		try {
			// 打开新的工作空间之前需要先关闭当前工作空间
			if (WorkspaceUtilities.closeWorkspace()) {
				((JFrame) Application.getActiveApplication().getMainFrame()).setCursor(Cursor.WAIT_CURSOR);
				Toolkit.clearErrors();
				boolean isOpened = false;
				HashMap<String, String> readOnlyDatasourceDictionary = new HashMap<String, String>();
				try {
					// 如果数据源中含有只读文件，则给出提示是否已只读打开。
					// 先构造一个工作空间，判断一下是否需要进行提示（这个主要是处理UI问题，因为数据源只有在打开工作空间的情况下才能够判断
					// 如果直接在application下的工作空间下判断，界面上就会出现 打开失败-
					// 提示是否只读打开-重新打开的现象，体验不好）
					Workspace workspace = new Workspace();
					isOpened = workspace.open(info);

					if (isOpened
							&& (workspace.getType() == WorkspaceType.SMW || workspace.getType() == WorkspaceType.SMWU
							|| workspace.getType() == WorkspaceType.SXW || workspace.getType() == WorkspaceType.SXWU)) {
						boolean isAllExcuteThisOperation = false;
						for (int i = 0; i < workspace.getDatasources().getCount(); i++) {
							Datasource datasource = workspace.getDatasources().get(i);
							// 如果上一次是以只读打开形式打开的文件，则保存工作空间后可以成功打开，故不需要给出提示。
							// 所以如果数据源没有成功打开，且文件类型是只读类型，则给出提示。
							if (!datasource.isOpened()) {
								String datasourceFilePath = datasource.getConnectionInfo().getServer();
								if (!datasourceFilePath.contains(":memory:")) {
									File file = new File(datasourceFilePath);
									// 仅限于文件型工作空间与文件型数据源，sdb已淘汰，不进行判断
									if (file.getName().toLowerCase().endsWith(".udb")) {
										String datasourceUddPath = datasourceFilePath.substring(0, datasourceFilePath.lastIndexOf(".")) + ".udd";// datasource.getConnectionInfo().getServer().toLowerCase().replace("udb",
										// "udd");
										File uddFile = new File(datasourceUddPath);
										if ((file.exists() && uddFile.exists()) && (!file.canWrite() || !uddFile.canWrite())) {
											String message = MessageFormat.format(CoreProperties.getString("String_OpenReadOnlyDatasourceWarning"),
													datasourceFilePath);
											if (!isAllExcuteThisOperation) {
												int dialogResult = JOptionPaneUtilities.showConfirmDialog(message);
												if (dialogResult == JOptionPane.YES_OPTION) {
													readOnlyDatasourceDictionary.put(datasourceFilePath, datasource.getAlias());
													isAllExcuteThisOperation = true;
												}
											} else if (isAllExcuteThisOperation) {
												readOnlyDatasourceDictionary.put(datasourceFilePath, datasource.getAlias());
											}
										}
									}
								}
							}
						}
					}
					workspace.close();
					workspace.dispose();

					// 真正的打开Application下面的工作空间
					isOpened = Application.getActiveApplication().getWorkspace().open(info);
					if (isOpened) {
						result = OpenWorkspaceResult.SUCCESSED;
						for (Entry<String, String> entry : readOnlyDatasourceDictionary.entrySet()) {
							Application.getActiveApplication().getWorkspace().getDatasources().close(entry.getValue());
							DatasourceUtilities.openFileDatasource(entry.getKey(), "", true, true);
						}

						for (int index = Application.getActiveApplication().getWorkspace().getDatasources().getCount() - 1; index >= 0; index--) {
							Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(index);
							if (!datasource.isOpened()) {
								if (!datasource.getConnectionInfo().getServer().contains(":memory:")) {
									String failedInfo = String.format(CommonProperties.getString("String_Message_DataSource_Openfail"), datasource.getAlias());
									Application.getActiveApplication().getOutput().output(failedInfo);
								} else {
									Application.getActiveApplication().getWorkspace().getDatasources().close(datasource.getAlias());
								}
							}
						}
					} else {
						ErrorInfo[] errorInfos = Toolkit.getLastErrors(2);
						for (int i = 0; i < errorInfos.length; i++) {
							if (errorInfos[i].getMarker().equals(CoreProperties.getString("String_UGS_PASSWORD"))) {
								if (!first) {
									// Application.getActiveApplication().getOutput().output(errorInfos[i].getMessage());
								}
								result = OpenWorkspaceResult.FAILED_PASSWORD_WRONG;
								break;
							} else {
								result = OpenWorkspaceResult.FAILED_UNKNOW;
							}
						}
					}
				} catch (Exception ex) {
					if ("supermap_license_error_wronglicensedata".equalsIgnoreCase(ex.getMessage())) {
						Application.getActiveApplication().getOutput().output(CoreProperties.getString("String_Wronglicensedata"));
					} else if (ex.getMessage().equalsIgnoreCase(CoreProperties.getString("String_UGS_PASSWORD_Message"))) {
						result = OpenWorkspaceResult.FAILED_PASSWORD_WRONG;
					} else {
						ErrorInfo[] errorInfos = Toolkit.getLastErrors(2);
						for (int i = 0; i < errorInfos.length; i++) {
							if (errorInfos[i].getMarker().equals(CoreProperties.getString("String_UGS_PASSWORD"))) {
								if (!first) {
									Application.getActiveApplication().getOutput().output(errorInfos[i].getMessage());
								}
								result = OpenWorkspaceResult.FAILED_PASSWORD_WRONG;
								break;
							} else {
								Application.getActiveApplication().getOutput().output(errorInfos[i].getMessage());
								result = OpenWorkspaceResult.FAILED_UNKNOW;
							}
						}
					}
					if (result != OpenWorkspaceResult.FAILED_PASSWORD_WRONG) {
						Application.getActiveApplication().getOutput().output(ex);
					}
				}

				if ((info.getType() == WorkspaceType.SMW || info.getType() == WorkspaceType.SMWU || info.getType() == WorkspaceType.SXWU || info.getType() == WorkspaceType.SXW)
						&& result == OpenWorkspaceResult.SUCCESSED) {
					// 如果是文件型工作空间并且打开成功了，则需要添加到最近文件列表中
					addWorkspaceFileToRecentFile(info.getServer());
				}
			} else {
				result = OpenWorkspaceResult.FAILED_CANCEL;
			}
		} catch (Exception ex) {
			// Application.getActiveApplication().getOutput().output(ex);
		} finally {
			((JFrame) Application.getActiveApplication().getMainFrame()).setCursor(Cursor.DEFAULT_CURSOR);
		}

		return result;
	}

	/**
	 * 将指定路径的工作空间添加到最近文件列表
	 *
	 * @param filePath 要加入列表的文件路径
	 * @return 添加成功返回true，失败返回false
	 */
	public static void addWorkspaceFileToRecentFile(String filePath) {
		String filePathTemp = filePath;
		try {
			filePathTemp = filePathTemp.replace("\\", "/");
			if (recentWorkspaceMenu != null) {
				removeRecentFile(filePathTemp);

				XMLCommand xmlCommand = new XMLCommand(pluginInfo);
				xmlCommand.setCtrlActionClass("CtrlActionRecentFiles");
				xmlCommand.setLabel(filePathTemp);
				xmlCommand.setTooltip(filePathTemp);
				SmMenuItem menuItem = new SmMenuItem(null, xmlCommand, recentWorkspaceMenu);
				if (menuItem != null) {
					recentWorkspaceMenu.insert((IBaseItem) menuItem, 0);
					if (recentWorkspaceMenu.getItemCount() > 7) {
						removeRecentFile(recentWorkspaceMenu.getItem(7).getText());
					}
					saveRecentFile(filePathTemp);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public static void initRecentFileMenu() {
		try {
			String recentFilePath = PathUtilities.getFullPathName(_XMLTag.RECENT_FILE_XML, false);
			File file = new File(recentFilePath);
			if (file.exists()) {
				Element element = XmlUtilities.getRootNode(recentFilePath);
				if (element != null) {
					NodeList nodes = element.getChildNodes();
					for (int i = 0; i < nodes.getLength(); i++) {
						if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
							Element item = (Element) (nodes.item(i));
							if (item.getNodeName().equalsIgnoreCase(_XMLTag.g_NodeGroup)) {
								String type = item.getAttribute(_XMLTag.g_ControlLabel);
								if (type.equalsIgnoreCase(CoreProperties.getString("String_RecentWorkspace"))) {
									NodeList childnNodes = item.getChildNodes();
									for (int j = 0; j < childnNodes.getLength(); j++) {
										if (childnNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
											Element childItem = (Element) (childnNodes.item(j));
											String filePath = childItem.getAttribute(_XMLTag.g_NodeContent);
											XMLCommand xmlCommand = new XMLCommand(pluginInfo);
											xmlCommand.setCtrlActionClass("CtrlActionRecentFiles");
											xmlCommand.setLabel(filePath);
											xmlCommand.setTooltip(filePath);
											SmMenuItem menuItem = new SmMenuItem(null, xmlCommand, recentWorkspaceMenu);
											if (menuItem != null) {
												recentWorkspaceMenu.add((IBaseItem) menuItem);
											}
										}
									}
								}
							}
						}
					}
				}
			}

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public static void saveRecentFile(String filePath) {
		try {
			String recentFilePath = PathUtilities.getFullPathName(_XMLTag.RECENT_FILE_XML, false);
			File file = new File(recentFilePath);
			if (file.exists()) {
				Document document = XmlUtilities.getDocument(recentFilePath);
				Element element = document.getDocumentElement();
				if (element != null) {
					NodeList nodes = element.getChildNodes();
					for (int i = 0; i < nodes.getLength(); i++) {
						if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
							Element item = (Element) (nodes.item(i));
							if (item.getNodeName().equalsIgnoreCase(_XMLTag.g_NodeGroup)) {
								String type = item.getAttribute(_XMLTag.g_ControlLabel);
								if (type.equalsIgnoreCase(CoreProperties.getString("String_RecentWorkspace"))) {
									// 把原来的记录全部取出来保存
									NodeList childnNodes = item.getChildNodes();
									ArrayList<String> recentFiles = new ArrayList<String>();
									for (int j = childnNodes.getLength() - 1; j >= 0; j--) {
										if (childnNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
											Element childItem = (Element) (childnNodes.item(j));
											recentFiles.add(0, childItem.getAttribute(_XMLTag.g_NodeContent));
										}
										item.removeChild(childnNodes.item(j));
									}

									// 添加新纪录
									Element newItem = document.createElement("item");
									newItem.setAttribute(_XMLTag.g_NodeContent, filePath);
									item.appendChild(newItem);

									// 把之前的记录再写入
									for (int j = 0; j < recentFiles.size(); j++) {
										newItem = document.createElement("item");
										newItem.setAttribute(_XMLTag.g_NodeContent, recentFiles.get(j));
										item.appendChild(newItem);
									}

									// 保存文件
									XmlUtilities.saveXml(recentFilePath, document, document.getXmlEncoding());
									break;
								}
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public static void removeRecentFile(String filePath) {
		try {
			for (IBaseItem item : recentWorkspaceMenu.items()) {
				if (((SmMenuItem) item).getToolTipText().equals(filePath)) {
					recentWorkspaceMenu.remove(item);
					break;
				}
			}

			String recentFilePath = PathUtilities.getFullPathName(_XMLTag.RECENT_FILE_XML, false);
			File file = new File(recentFilePath);
			if (file.exists()) {
				Document document = XmlUtilities.getDocument(recentFilePath);
				Element element = document.getDocumentElement();
				if (element != null) {
					NodeList nodes = element.getChildNodes();
					for (int i = 0; i < nodes.getLength(); i++) {
						if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
							Element item = (Element) (nodes.item(i));
							if (item.getNodeName().equalsIgnoreCase(_XMLTag.g_NodeGroup)) {
								String type = item.getAttribute(_XMLTag.g_ControlLabel);
								if (type.equalsIgnoreCase(CoreProperties.getString("String_RecentWorkspace"))) {
									NodeList childnNodes = item.getChildNodes();
									for (int j = 0; j < childnNodes.getLength(); j++) {
										if (childnNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
											Element childItem = (Element) (childnNodes.item(j));
											String itemPath = childItem.getAttribute(_XMLTag.g_NodeContent);
											if (itemPath.equalsIgnoreCase(filePath)) {
												item.removeChild(childnNodes.item(j));
											}
										}
									}

									// 保存文件

									XmlUtilities.saveXml(recentFilePath, document, document.getXmlEncoding());
									break;
								}
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 判断当前工作空间文件是否只读
	 *
	 * @return
	 */
	public static boolean isWorkspaceReadonly() {
		String path = Application.getActiveApplication().getWorkspace().getConnectionInfo().getServer();
		boolean isReadOnly = false;

		File file = new File(path);
		if (file.exists()) {
			isReadOnly = !file.canWrite();
		}

		if (isReadOnly) {
			JOptionPaneUtilities.showMessageDialog(CoreProperties.getString("String_workspaceReadonly"));
		}

		return isReadOnly;
	}

	/**
	 * 判断工作空间是否被修改过
	 *
	 * @return
	 */
	public static boolean isWorkspaceModified() {
		boolean result = false;
		try {
			result = Application.getActiveApplication().getWorkspace().isModified();
			if (!result) {
				for (int index = 0; index < Application.getActiveApplication().getMainFrame().getFormManager().getCount(); index++) {
					IForm form = Application.getActiveApplication().getMainFrame().getFormManager().get(index);
					if (form == null) {
						continue;
					} else if (form instanceof IFormMap && ((IFormMap) form).getMapControl() != null && ((IFormMap) form).getMapControl().getMap() != null
							&& ((IFormMap) form).getMapControl().getMap().isModified()) {
						result = true;
						break;
					} else if (form instanceof IFormLayout && ((IFormLayout) form).getMapLayoutControl() != null
							&& ((IFormLayout) form).getMapLayoutControl().getMapLayout() != null
							&& ((IFormLayout) form).getMapLayoutControl().getMapLayout().isModified()) {
						result = true;
						break;
					} else if (form instanceof IFormScene) {
						result = true;
						break;
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	/**
	 * 关闭工作空间
	 */
	public static boolean closeWorkspace() {
		boolean isClose = true;
		int result = -1;
		try {
			boolean needSave = false;
			boolean isContinue = true;
			Component parent = (Component) Application.getActiveApplication().getMainFrame();
			// 提示保存内存型数据源
			if (DatasourceUtilities.isContainMemoryDatasource(Application.getActiveApplication().getWorkspace())) {
				String[] datasources = DatasourceUtilities.getMemoryDatasources(Application.getActiveApplication().getWorkspace());
				String datasourcesName = "";
				for (int i = 0; i < datasources.length - 1; i++) {
					datasourcesName += datasources[i] + "、";
				}
				datasourcesName += datasources[datasources.length - 1];//
				String message = MessageFormat.format(CommonProperties.getString("String_Message_CloseMemoryDatasource"), datasourcesName);
				result = JOptionPaneUtilities.showConfirmDialog(message);
				if (result == JOptionPane.NO_OPTION || result == JOptionPane.CLOSED_OPTION) {
					isContinue = false;
				} else {
					DatasourceUtilities.closeMemoryDatasource();
				}
			}

			if (isContinue) {
				if (WorkspaceUtilities.isWorkspaceModified()) {
					if (GlobalParameters.isShowFormClosingInfo()) {
						result = JOptionPaneUtilities.showConfirmDialogWithCancel(CoreProperties.getString("String_SaveWorkspacePrompt"));
						if (result == JOptionPane.NO_OPTION) {
							if (Application.getActiveApplication().getMainFrame().getFormManager().closeAll(false)) {
								Application.getActiveApplication().getWorkspace().close();
							}
						} else if (result == JOptionPane.YES_OPTION) {
							needSave = true;
						} else if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
							isClose = false;
						}
					} else {
						needSave = true;
					}

					if (needSave) {
						if (Application.getActiveApplication().getMainFrame().getFormManager().closeAll(true)) {
							if (isWorkspaceReadonly()) {
								isClose = fireSaveWorkspaceEvent(true, true, true, null);
							} else {
								if (Application.getActiveApplication().getWorkspace().getType() == WorkspaceType.DEFAULT) {
									isClose = fireSaveWorkspaceEvent(true, true, true, null);// 对于默认工作空间来说，这时候需要另存
								} else {
									if (!Application.getActiveApplication().getWorkspace().save()) {
										isClose = fireSaveWorkspaceEvent(true, true, true, null);
									}
								}
							}

							if (!WorkspaceUtilities.isWorkspaceModified()) {
								Application.getActiveApplication().getWorkspace().close();
								isClose = true;
							}
						} else {
							isClose = false;
						}
					}
				} else {
					if (Application.getActiveApplication().getMainFrame().getFormManager().closeAll(true)) {
						Application.getActiveApplication().getWorkspace().close();
					} else {
						isClose = false;
					}
				}
			} else {
				isClose = false;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return isClose;
	}

	public static Workspace copyWorkspace(Workspace workspace, Workspace copyWorkspace) {
		copyWorkspace.setCaption(workspace.getCaption());
		copyWorkspace.setDescription(workspace.getDescription());

		RepeatDatasourceDeal repeatDatasourceDeal = new RepeatDatasourceDeal(workspace.getDatasources(), copyWorkspace.getDatasources());
		List<Datasource> datasourceBOnly = repeatDatasourceDeal.getDatasourceBOnly();
		for (int i = datasourceBOnly.size() - 1; i >= 0; i--) {
			copyWorkspace.getDatasources().close(datasourceBOnly.get(i).getAlias());
		}
		List<Datasource> datasourceAOnly = repeatDatasourceDeal.getDatasourceAOnly();
		for (Datasource datasource : datasourceAOnly) {
			copyWorkspace.getDatasources().open(datasource.getConnectionInfo());
		}

//		copyWorkspace.getDatasources().closeAll();
//		for (int i = 0; i < workspace.getDatasources().getCount(); i++) {
//			Datasource datasource = workspace.getDatasources().get(i);
//			if (!":memory:".equalsIgnoreCase(datasource.getConnectionInfo().getServer()) && (datasource.isReadOnly() || datasource.getEngineType() != EngineType.UDB)) {
//				try {
//					copyWorkspace.getDatasources().open(datasource.getConnectionInfo());
//				} catch (Exception e) {
//					// ignore
//				}
//			}
//		}

		copyWorkspace.getMaps().clear();
		copyWorkspace.getScenes().clear();
		copyWorkspace.getLayouts().clear();

		IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
		for (int i = 0; i < formManager.getCount(); i++) {
			IForm iForm = formManager.get(i);
			if (iForm instanceof IFormMap) {
				copyWorkspace.getMaps().add(iForm.getText(), ((IFormMap) iForm).getMapControl().getMap().toXML());
			} else if (iForm instanceof IFormScene) {
				copyWorkspace.getScenes().add(iForm.getText(), ((IFormScene) iForm).getSceneControl().getScene().toXML());
			} else if (iForm instanceof IFormLayout) {
				copyWorkspace.getLayouts().add(iForm.getText(), ((IFormLayout) iForm).getMapLayoutControl().getMapLayout().toXML());
			}
		}

		for (int i = 0; i < workspace.getMaps().getCount(); i++) {
			String mapName = workspace.getMaps().get(i);
			if (copyWorkspace.getMaps().indexOf(mapName) == -1) {
				copyWorkspace.getMaps().add(mapName, workspace.getMaps().getMapXML(i));
			}
		}

		for (int i = 0; i < workspace.getScenes().getCount(); i++) {

			String sceneName = workspace.getScenes().get(i);
			if (copyWorkspace.getScenes().indexOf(sceneName) == -1) {
				copyWorkspace.getScenes().add(sceneName, workspace.getScenes().getSceneXML(i));
			}
		}

		for (int i = 0; i < workspace.getLayouts().getCount(); i++) {

			String layoutName = workspace.getLayouts().get(i);
			if (copyWorkspace.getLayouts().indexOf(layoutName) == -1) {
				copyWorkspace.getLayouts().add(layoutName, workspace.getLayouts().getLayoutXML(i));
			}
		}

		String tempFolder = FileUtilities.getTempFolder() + "CrossSymbolCopyFile";
		String markerSymbolFilePath = tempFolder + ".sym";
		workspace.getResources().getMarkerLibrary().toFile(markerSymbolFilePath);
		copyWorkspace.getResources().getMarkerLibrary().fromFile(markerSymbolFilePath);
		if (new File(markerSymbolFilePath).exists()) {
			new File(markerSymbolFilePath).delete();
		}

		String lineSymbolFilePath = tempFolder + ".lsl";
		workspace.getResources().getLineLibrary().toFile(lineSymbolFilePath);
		copyWorkspace.getResources().getLineLibrary().fromFile(lineSymbolFilePath);
		if (new File(lineSymbolFilePath).exists()) {
			new File(lineSymbolFilePath).delete();
		}

		String fillSymbolFilePath = tempFolder + ".bru";
		workspace.getResources().getFillLibrary().toFile(fillSymbolFilePath);
		copyWorkspace.getResources().getFillLibrary().fromFile(fillSymbolFilePath);
		if (new File(fillSymbolFilePath).exists()) {
			new File(fillSymbolFilePath).delete();
		}

		return copyWorkspace;
	}

	public static boolean deleteFileWorkspace(String workSpaceFilePath) {
		if (StringUtilities.isNullOrEmpty(workSpaceFilePath)) {
			return true;
		}
		String end = workSpaceFilePath.substring(workSpaceFilePath.length() - 5, workSpaceFilePath.length());
		if (!FileUtilities.delete(workSpaceFilePath)) {
			return false;
		}
		if (end.equalsIgnoreCase(".sxwu")) {
			String prefix = workSpaceFilePath.substring(0, workSpaceFilePath.length() - 4);
			boolean deleteBru = FileUtilities.delete(prefix + "bru");
			boolean deleteLsl = FileUtilities.delete(prefix + "lsl");
			boolean deleteSym = FileUtilities.delete(prefix + "sym");
			// 不加变量直接写在条件里会导致前面的删除失败时，后面的就不进行操作了
			if (!deleteBru || !deleteLsl || !deleteSym) {
				return false;
			}
		}
		return true;
	}

}

class RepeatDatasourceDeal {
	private List<Datasource> datasourceAOnly = new ArrayList<>();
	private List<Datasource> datasourceBOnly = new ArrayList<>();

	RepeatDatasourceDeal(Datasources datasourcesA, Datasources datasourcesB) {
		for (int i = 0; i < datasourcesA.getCount(); i++) {
			Datasource datasource = datasourcesA.get(i);
			// 只考虑不为内存和非只读数据源的情况
			if (!":memory:".equalsIgnoreCase(datasource.getConnectionInfo().getServer()) && datasource.isOpened() && (datasource.isReadOnly() || datasource.getEngineType() != EngineType.UDB)) {
				datasourceAOnly.add(datasource);
			}
		}
		for (int i = 0; i < datasourcesB.getCount(); i++) {
			Datasource datasource = datasourcesB.get(i);
			// 只考虑不为内存和非只读数据源的情况
			if (!":memory:".equalsIgnoreCase(datasource.getConnectionInfo().getServer()) && datasource.isOpened() && (datasource.isReadOnly() || datasource.getEngineType() != EngineType.UDB)) {
				datasourceBOnly.add(datasource);
			}
		}

		for (int i = datasourceAOnly.size() - 1; i >= 0; i--) {
			Datasource datasourceA = datasourceAOnly.get(i);
			for (int j = datasourceBOnly.size() - 1; j >= 0; j--) {
				Datasource datasourceB = datasourceAOnly.get(i);
				if (datasourceA.toString().equalsIgnoreCase(datasourceB.toString())) {
					datasourceAOnly.remove(datasourceA);
					datasourceBOnly.remove(datasourceB);
				}
			}
		}
	}

	List<Datasource> getDatasourceAOnly() {
		return datasourceAOnly;
	}

	List<Datasource> getDatasourceBOnly() {
		return datasourceBOnly;
	}
}
