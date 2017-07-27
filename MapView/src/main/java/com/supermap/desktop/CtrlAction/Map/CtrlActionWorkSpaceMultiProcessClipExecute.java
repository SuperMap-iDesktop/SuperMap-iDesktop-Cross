package com.supermap.desktop.CtrlAction.Map;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dialog.cacheClip.DialogCacheBuilder;
import com.supermap.desktop.dialog.cacheClip.DialogMapCacheClipBuilder;
import com.supermap.desktop.dialog.cacheClip.cache.CacheUtilities;
import com.supermap.desktop.dialog.cacheClip.cache.LogWriter;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.mapping.Map;

/**
 * Created by xie on 2017/7/7.
 */
public class CtrlActionWorkSpaceMultiProcessClipExecute extends CtrlAction {
	public CtrlActionWorkSpaceMultiProcessClipExecute(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	protected void run() {
		Map map = CacheUtilities.getWorkspaceSelectedMap();
		if (CacheUtilities.voladateDatasource()) {
//			Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_StartBuildCacheExecute"));
//			String[] params = {"Multi", "null", map.getName(), "null"};
//			CacheUtilities.startProcess(params, DialogCacheBuilder.class.getName(), LogWriter.BUILD_CACHE);
			new DialogCacheBuilder(DialogMapCacheClipBuilder.MultiProcessClip).setVisible(true);
		}
	}

	@Override
	public boolean enable() {
		return CacheUtilities.selectedMapIsEnabled();
	}
}