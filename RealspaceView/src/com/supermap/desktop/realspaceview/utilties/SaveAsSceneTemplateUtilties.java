package com.supermap.desktop.realspaceview.utilties;

import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.realspaceview.RealspaceViewProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveAsSceneTemplateUtilties {
	
	private SaveAsSceneTemplateUtilties(){
		
	}
	/**
	 * 将地图输出为场景模板
	 * @param SceneXml 需要输出的场景xml字符串
	 * @return 输出的场景模板路径
	 */
	public static String saveAsSceneTemplate(String SceneXml){
		String moduleName = "SavaAsSceneTemplate";
		String sceneTemplatePath = null;
		// 创建SmFileChoose类
		if(!SmFileChoose.isModuleExist(moduleName)){
			String fileFilters = SmFileChoose.createFileFilter(RealspaceViewProperties.getString("String_SceneTemplateFilter"), "xml");
			SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"), RealspaceViewProperties.getString("String_Title_SavaAsTemplate"), moduleName, "SaveOne");
		}
		SmFileChoose smFileChoose= new SmFileChoose(moduleName);
		smFileChoose.setSelectedFile(new File("SceneTemplate.xml"));
		int state = smFileChoose.showDefaultDialog();
		if (state == JFileChooser.APPROVE_OPTION) {
			try {
				//保存场景信息
				FileWriter fileWriter = new FileWriter(smFileChoose.getFilePath());
				fileWriter.write(SceneXml);
				fileWriter.flush();
				fileWriter.close();
				sceneTemplatePath = smFileChoose.getFilePath();
			} catch (IOException e) {
				Application.getActiveApplication().getOutput().output(e);
			}
		}
		return sceneTemplatePath;
	}
	
	/**
	 * 获取工作空间管理器中当前选中的场景
	 * @return
	 */
	public static String getActiveSceneXml(){
		String sceneXml = null;
		// 获取场景名称
		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) workspaceTree.getSelectionPath().getLastPathComponent();
		TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
		String sceneName = (String)selectedNodeData.getData();
		// 根据场景名称找到场景的xml字符串
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		sceneXml = workspace.getScenes().getSceneXML(sceneName);
		
		return sceneXml;
	}
}
