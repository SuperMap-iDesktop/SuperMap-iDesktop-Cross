package com.supermap.desktop.mapview.utilties;

import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.tree.DefaultMutableTreeNode;

import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;

public class SaveAsMapTemplateUtilties {

	/**
	 * 将地图输出为地图模板
	 * 
	 * @param mapXml 需要输出的地图的xml字符串
	 * @return 输出的地图模板路径
	 */

	private SaveAsMapTemplateUtilties() {
		// 工具类不提供构造函数
	}

	public static String saveAsMapTemplate(String mapXml) {
		String moduleName = "SavaAsMapTemplate";
		String mapTemplatePath = null;
		// 创建SmFileChoose类
		if (!SmFileChoose.isModuleExist(moduleName)) {
			String fileFilters = SmFileChoose.createFileFilter(MapViewProperties.getString("String_LoadMapTemplateFileFilter"), "xml");
			SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
					MapViewProperties.getString("String_Title_SavaAsTemplate"), moduleName, "SaveOne");
		}
		SmFileChoose smFileChoose = new SmFileChoose(moduleName);
		int state = smFileChoose.showDefaultDialog();
		if (state == JFileChooser.APPROVE_OPTION) {
			try {
				// 保存地图信息
				FileWriter fileWriter = new FileWriter(smFileChoose.getFilePath());
				fileWriter.write(mapXml);
				fileWriter.flush();
				fileWriter.close();
				mapTemplatePath = smFileChoose.getFilePath();
			} catch (IOException e) {
				Application.getActiveApplication().getOutput().output(e);
			}
		}
		return mapTemplatePath;
	}

	/**
	 * 获取工作空间管理器中当前选中的地图
	 * 
	 * @return
	 */
	public static String getActiveMapXml() {
		String mapXml = null;
		// 获取地图名称
		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) workspaceTree.getSelectionPath().getLastPathComponent();
		TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
		String mapName = (String) selectedNodeData.getData();
		// 根据地图名称找到地图的xml字符串
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		mapXml = workspace.getMaps().getMapXML(mapName);

		return mapXml;
	}
}
