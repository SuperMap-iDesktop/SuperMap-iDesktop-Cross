package com.supermap.desktop.CtrlAction;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.mapping.Map;

/**
 * 加载地图模板
 * 
 * @author XiaJT
 *
 */
public class CtrlActionLoadMapTemplate extends CtrlAction {

	public CtrlActionLoadMapTemplate(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		BufferedReader br = null;
		try {
			boolean continueFlag = false;
			String fileName = "";
			String filePath = "";

			if (!SmFileChoose.isModuleExist("LoadMapTemplate")) {
				String fileFilters = SmFileChoose.createFileFilter(MapViewProperties.getString("String_LoadMapTemplateFileFilter"), "xml");
				SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
						MapViewProperties.getString("String_LoadMapTemplateTitle"), "LoadMapTemplate", "OpenOne");
			}
			SmFileChoose fileChooser = new SmFileChoose("LoadMapTemplate");
			fileChooser.showDefaultDialog();
			filePath = fileChooser.getFilePath();
			fileName = fileChooser.getFileName();
			if (filePath != null && filePath.length() > 0 && fileName != null && fileName.length() > 0) {
				continueFlag = true;
			}

			// 添加地图模板
			if (continueFlag) {
				StringBuilder mapTemplateXml = new StringBuilder();
				br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
				String s = "";
				while ((s = br.readLine()) != null) {
					mapTemplateXml.append(s);
				}
				IFormMap formMap = (IFormMap) CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.MAP);
				if (formMap != null) {
					Map map = formMap.getMapControl().getMap();
					map.fromXML(mapTemplateXml.toString());
					UICommonToolkit.getLayersManager().setMap(map);
					map.refresh();
					map.viewEntire();
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					Application.getActiveApplication().getOutput().output(e);
				}
			}
		}
	}

	@Override
	public boolean enable() {
		return true;
	}
}