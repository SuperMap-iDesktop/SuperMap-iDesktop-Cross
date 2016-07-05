package com.supermap.desktop.controls.colorScheme;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dialog.ColorSchemeDialogs.ColorSchemeTreeNode;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.utilities.PathUtilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author XiaJT
 */
public class ColorSchemeManager {
	private static ColorSchemeManager colorSchemeManager;
	private List<ColorSchemeManagerChangedListener> colorSchemeManagerChangedListeners;


	private int version;
	private ColorSchemeTreeNode rootTreeNode;


	public ColorSchemeTreeNode getRootTreeNode() {
		return rootTreeNode;
	}

	public void setRootTreeNode(ColorSchemeTreeNode rootTreeNode) {
		this.rootTreeNode = rootTreeNode;
		save();
		fireColorSchemeManagerChanged();
	}

	public static ColorSchemeManager getColorSchemeManager() {
		if (colorSchemeManager == null) {
			colorSchemeManager = new ColorSchemeManager();
		}
		return colorSchemeManager;
	}

	private ColorSchemeManager() {
		init();
	}

	private void init() {
		colorSchemeManagerChangedListeners = new ArrayList<>();
		version = 60000;
		initData();
	}

	private void initData() {
		//region 改版
		//		colorSchemeList.clear();
//		Document document = XmlUtilities.getDocument(filePath);
//		NodeList childNodes = document.getChildNodes().item(0).getChildNodes();
//		for (int i = 0; i < childNodes.getLength(); i++) {
//			if (childNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
//				if (TAG_FILE_HEADER.equals(childNodes.item(i).getNodeName())) {
//					// 文件头
//					String versionValue = childNodes.item(i).getChildNodes().item(1).getChildNodes().item(0).getNodeValue();
//					version = Integer.valueOf(versionValue);
//				} else if (TAG_DATA_BLOCK.equals(childNodes.item(i).getNodeName())) {
//					// colors
//					NodeList childNodesColors = childNodes.item(i).getChildNodes();
//					for (int j = 0; j < childNodesColors.getLength(); j++) {
//						if (childNodesColors.item(j).getNodeType() == Node.ELEMENT_NODE) {
//							String colorFilePath = childNodesColors.item(j).getChildNodes().item(0).getNodeValue();
//							if (new File(colorFilePath).exists()) {
//								ColorScheme colorScheme = new ColorScheme();
//								if (colorScheme.fromXML(new File(colorFilePath), false)) {
//									colorScheme.setColorSchemePath(colorFilePath);
//									colorSchemeList.add(colorScheme);
//								}
//							}
//						}
//					}
//				}
//			}
//		}
		//endregion
		rootTreeNode = new ColorSchemeTreeNode(null);
		rootTreeNode.setFilePath(PathUtilities.getFullPathName(ControlsProperties.getString("String_ColorSchemeRootFilePath"), true));
		// 默认
		initTreeNode(PathUtilities.getFullPathName(ControlsProperties.getString("String_ColorSchemeDefaultFilePath"), true), CoreProperties.getString("String_Default"));
		// 自定义
		initTreeNode(PathUtilities.getFullPathName(ControlsProperties.getString("String_ColorSchemeUserDefineFilePath"), true), CoreProperties.getString("String_UserDefine"));
		// 我的收藏
		initTreeNode(PathUtilities.getFullPathName(ControlsProperties.getString("String_ColorSchemeFavoritesFilePath"), true), CoreProperties.getString("String_MyFavorites"));
	}

	private void initTreeNode(String directoryPath, String nodeName) {
		ColorSchemeTreeNode defaultTreeNode = new ColorSchemeTreeNode(rootTreeNode);
		defaultTreeNode.setName(nodeName);
		rootTreeNode.addChild(defaultTreeNode);
		File file = new File(directoryPath);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files != null && files.length > 0) {
				for (File subFile : files) {
					if (subFile.isDirectory()) {
						ColorSchemeTreeNode colorSchemeTreeNode = new ColorSchemeTreeNode(defaultTreeNode);
						defaultTreeNode.addChild(colorSchemeTreeNode);
						String name = subFile.getName();
						colorSchemeTreeNode.setName(name);

						File[] colorSchemeFiles = subFile.listFiles();
						if (colorSchemeFiles != null) {
							for (File colorSchemeFile : colorSchemeFiles) {
								ColorScheme colorScheme = new ColorScheme();
								if (colorScheme.fromXML(colorSchemeFile, false)) {
									colorScheme.setParentNode(colorSchemeTreeNode);
									colorScheme.setColorSchemePath(colorSchemeFile.getAbsolutePath());
									colorSchemeTreeNode.addColorScheme(colorScheme);
								}
							}
						}
					}
				}
			}
		}
	}


	public void save() {
		rootTreeNode.save();
	}


	public void fireColorSchemeManagerChanged() {
		ColorSchemeManagerChangedEvent colorSchemeManagerChangedEvent = new ColorSchemeManagerChangedEvent(rootTreeNode);
		for (int i = colorSchemeManagerChangedListeners.size() - 1; i >= 0; i--) {
			colorSchemeManagerChangedListeners.get(i).colorSchemeManagerChanged(colorSchemeManagerChangedEvent);
		}
	}

	public void addColorSchemeManagerChangedListener(ColorSchemeManagerChangedListener colorSchemeManagerChangedListener) {
		if (!colorSchemeManagerChangedListeners.contains(colorSchemeManagerChangedListener)) {
			colorSchemeManagerChangedListeners.add(colorSchemeManagerChangedListener);
		}
	}

	public void removeColorSchemeManagerChangedListener(ColorSchemeManagerChangedListener colorSchemeManagerChangedListener) {
		colorSchemeManagerChangedListeners.remove(colorSchemeManagerChangedListener);
	}


}
