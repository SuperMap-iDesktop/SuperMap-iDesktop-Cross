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
import com.supermap.desktop.ui.controls.SmFileChoose;

import javax.swing.*;
import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by xie on 2017/5/9.
 */
public class CtrlActionMapCacheResume extends CtrlAction {
	public CtrlActionMapCacheResume(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		String moduleName = "GetCacheReloadConfigFile";
		if (!SmFileChoose.isModuleExist(moduleName)) {
			String fileFilters = SmFileChoose.createFileFilter(MapViewProperties.getString("MapCache_CacheConfigFile"), "sci");
			SmFileChoose.addNewNode(fileFilters, System.getProperty("user.dir"),
					MapViewProperties.getString("String_OpenColorTable"), moduleName, "OpenOne");
		}
		SmFileChoose smFileChoose = new SmFileChoose(moduleName);
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
				MapCacheBuilder mapCacheBuilder = new MapCacheBuilder();
				mapCacheBuilder.fromConfigFile(file.getPath());
				mapCacheBuilder.setMap(((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().getMap());
				DialogMapCacheClipBuilder mapCacheClipBuilder = new DialogMapCacheClipBuilder(DialogMapCacheClipBuilder.ResumeProcessClip, mapCacheBuilder);
				mapCacheClipBuilder.setComponentsEnabled(false);
				mapCacheClipBuilder.buttonOk.setEnabled(true);
				mapCacheClipBuilder.firstStepPane.labelConfigValue.setText(mapCacheBuilder.getCacheName());
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
