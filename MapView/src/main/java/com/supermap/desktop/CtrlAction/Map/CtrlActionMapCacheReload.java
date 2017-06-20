package com.supermap.desktop.CtrlAction.Map;

import com.supermap.data.processing.MapCacheBuilder;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.dialog.SmOptionPane;
import com.supermap.desktop.dialog.cacheClip.DialogMapCacheClipBuilder;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.SmFileChoose;

import javax.swing.*;
import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by xie on 2017/5/9.
 */
public class CtrlActionMapCacheReload extends CtrlAction {
	public CtrlActionMapCacheReload(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		String moduleName = "GetCacheConfigFile";
		if (!SmFileChoose.isModuleExist(moduleName)) {
			String fileFilters = SmFileChoose.createFileFilter(MapViewProperties.getString("MapCache_CacheConfigFile"), "sci");
			SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
					MapViewProperties.getString("String_SaveAsFile"), moduleName, "OpenOne");
		}
		SmFileChoose smFileChoose = new SmFileChoose(moduleName);
		smFileChoose.setSelectedFile(new File(MapViewProperties.getString("MapCache_CacheConfigFileIsNotbrackets")));
		int state = smFileChoose.showDefaultDialog();
		if (state == JFileChooser.APPROVE_OPTION) {
			File file = smFileChoose.getSelectedFile();
			File fileParent = file.getParentFile();
			boolean hasLogFile = false;
			if (fileParent.isDirectory()) {
				String[] resumFile = fileParent.list(new FilenameFilter() {

					public boolean accept(File dir, String name) {
						return name.equalsIgnoreCase("resumable.log");
					}
				});
				if (null != resumFile && resumFile.length > 0) {
					hasLogFile = true;
				}
			}
			if (!hasLogFile) {
				SmOptionPane pane = new SmOptionPane();
				pane.showConfirmDialog(MapViewProperties.getString("String_Error_LogNotExist"));
				return;
			}
			if (file.exists()) {
//				Workspace wk = Application.getActiveApplication().getWorkspace();
//				Map map = new Map(wk);
				MapCacheBuilder mapCacheBuilder = new MapCacheBuilder();
				mapCacheBuilder.fromConfigFile(file.getPath());
//				map.open(mapCacheBuilder.getCacheName());
//				Map newMap = new Map(wk);
//				newMap.fromXML(map.toXML());
//				mapCacheBuilder.setMap(newMap);

				DialogMapCacheClipBuilder mapCacheClipBuilder = new DialogMapCacheClipBuilder(DialogMapCacheClipBuilder.ReloadProcessClip, mapCacheBuilder);
				mapCacheClipBuilder.setComponentsEnabled(false);
				mapCacheClipBuilder.buttonOk.setEnabled(true);
				mapCacheClipBuilder.firstStepPane.labelConfigValue.setText(mapCacheBuilder.getCacheName());
				mapCacheClipBuilder.setResumeAble(true);
				mapCacheClipBuilder.firstStepPane.fileChooserControlFileCache.setPath(file.getParent());
				mapCacheClipBuilder.firstStepPane.resetComponentsInfo();
				mapCacheClipBuilder.showDialog();
			}
		}
	}

	@Override
	public boolean enable() {
		return true;
	}
}
