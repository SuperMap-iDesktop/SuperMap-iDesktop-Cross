package com.supermap.desktop.ui.controls;

import java.io.File;

import javax.swing.JPanel;

import com.supermap.data.GeoStyle;
import com.supermap.data.Resources;
import com.supermap.data.SymbolType;

/**
 * 符号库基类面板，该面板用于双击WorkspaceTree的符号库节点时弹出的对话框
 * 
 * @author xuzw
 *
 */
class SymbolLibraryPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private String currentSystemPath;

	private transient SymbolPanel symbolPanel;

	// 一个内部的GeoStyle，符号库基类面板虽然继承于符号选择器，但是不能获取风格
	private transient GeoStyle internalGeoStyle;

	public SymbolLibraryPanel() {
		super();
		symbolPanel = new SymbolPanel();
		this.add(symbolPanel);
	}

	public SymbolLibraryPanel(Resources resources, SymbolType symbolType) {
		symbolPanel.setSymbolLibraryPanel(true);
		symbolPanel.setType(symbolType);
		internalGeoStyle = new GeoStyle();
		symbolPanel.setStyle(internalGeoStyle);
		symbolPanel.setResources(resources);
	}

	/**
	 * 获取指定的符号库文件路径
	 * 
	 * @return
	 */
	public String getCurrentSymPath() {
		return currentSystemPath;
	}

	/**
	 * 设置符号库文件路径
	 * 
	 * @param filePath
	 */
	public void setCurrentSymPath(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			currentSystemPath = filePath;
			symbolPanel.getFileChooserImportSymFile().setCurrentDirectory(file);
		}
	}

	/**
	 * 获取资源
	 * 
	 * @return
	 */
	public Resources getResources() {
		return symbolPanel.getResources();
	}

	/**
	 * 设置资源
	 * 
	 * @param resources
	 */
	public void setResources(Resources resources) {
		symbolPanel.setResources(resources);
		if (internalGeoStyle == null) {
			internalGeoStyle = new GeoStyle();
		}
		symbolPanel.setStyle(internalGeoStyle);
	}

	/**
	 * 获取符号面板类型
	 * 
	 * @return
	 */
	public SymbolType getType() {
		return symbolPanel.getType();
	}

	/**
	 * 设置符号面板类型
	 * 
	 * @param symbolType
	 */
	public void setType(SymbolType symbolType) {
		symbolPanel.setType(symbolType);
		if (internalGeoStyle == null) {
			internalGeoStyle = new GeoStyle();
		}
		symbolPanel.setStyle(internalGeoStyle);
	}

}
