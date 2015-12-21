package com.supermap.desktop.CtrlAction;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.realspaceview.RealspaceViewProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.realspace.Scene;

/**
 * 加载地图模板
 * 
 * @author XiaJT
 *
 */
public class CtrlActionLoadSceneTemplate extends CtrlAction {

	public CtrlActionLoadSceneTemplate(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			boolean continueFlag = false;
			String fileName = "";
			String filePath = "";
			if (!SmFileChoose.isModuleExist("LoadSceneTemplate")) {
				String fileFilters = SmFileChoose.createFileFilter(RealspaceViewProperties.getString("String_SceneTemplateFilter"), "xml");
				SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
						RealspaceViewProperties.getString("String_LoadSceneTemplateFile"), "LoadSceneTemplate", "OpenOne");
			}
			SmFileChoose fileChooser = new SmFileChoose("LoadSceneTemplate");
			fileChooser.showDefaultDialog();
			filePath = fileChooser.getFilePath();
			fileName = fileChooser.getFileName();
			if (filePath != null && filePath.length() > 0 && fileName != null && fileName.length() > 0) {
				continueFlag = true;
			}

			// 添加地图模板
			if (continueFlag) {
				StringBuilder mapTemplateXml = new StringBuilder();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
				String s = "";
				while ((s = bufferedReader.readLine()) != null) {
					mapTemplateXml.append(s);
				}
				IFormScene formScene = (IFormScene) CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.SCENE);
				if (formScene != null) {
					Scene scene = formScene.getSceneControl().getScene();
					scene.setWorkspace(Application.getActiveApplication().getWorkspace());
					scene.fromXML(mapTemplateXml.toString());
					scene.refresh();
					UICommonToolkit.getLayersManager().setScene(scene);
				}
				bufferedReader.close();
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	@Override
	public boolean enable() {
		return true;
	}
}