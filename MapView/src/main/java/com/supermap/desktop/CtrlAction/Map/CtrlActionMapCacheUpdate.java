package com.supermap.desktop.CtrlAction.Map;

import com.supermap.data.processing.MapCacheBuilder;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.dialog.cacheClip.DialogMapCacheClipBuilder;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.SmFileChoose;

import javax.swing.*;
import java.io.File;

/**
 * Created by xie on 2017/6/12.
 */
public class CtrlActionMapCacheUpdate extends CtrlAction {
	public CtrlActionMapCacheUpdate(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		String moduleName = "GetCacheUpdateConfigFile";
		if (!SmFileChoose.isModuleExist(moduleName)) {
			String fileFilters = SmFileChoose.createFileFilter(MapViewProperties.getString("MapCache_CacheConfigFile"), "sci");
			SmFileChoose.addNewNode(fileFilters, System.getProperty("user.dir"),
					MapViewProperties.getString("String_OpenColorTable"), moduleName, "OpenOne");
		}
		SmFileChoose smFileChoose = new SmFileChoose(moduleName);
		int state = smFileChoose.showDefaultDialog();
		if (state == JFileChooser.APPROVE_OPTION) {
			File sciFile = smFileChoose.getSelectedFile();
			if (sciFile.exists()) {
				MapCacheBuilder mapCacheBuilder = new MapCacheBuilder();
				mapCacheBuilder.fromConfigFile(sciFile.getPath());
				DialogMapCacheClipBuilder builder = new DialogMapCacheClipBuilder(DialogMapCacheClipBuilder.SingleUpdateProcessClip, mapCacheBuilder);
				builder.firstStepPane.textFieldCacheName.setText(mapCacheBuilder.getCacheName());
				builder.firstStepPane.labelConfigValue.setText(mapCacheBuilder.getCacheName());
				builder.firstStepPane.setSciPath(sciFile.getPath());
				builder.firstStepPane.fileChooserControlFileCache.setPath(sciFile.getParentFile().getParent());
				builder.firstStepPane.resetComponentsInfo();
				builder.showDialog();
			}
		}
	}

	@Override
	public boolean enable() {
		return true;
	}
}
