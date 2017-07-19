package com.supermap.desktop.CtrlAction.Map;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
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
public class CtrlActionMultiProcessClipExecute extends CtrlAction {
	public CtrlActionMultiProcessClipExecute(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	protected void run() {
		Map map = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().getMap();
		if (!CacheUtilities.dynamicEffectClosed(map)) {
			return;
		}
		if (CacheUtilities.volatileDatasource(map)) {
			Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_StartBuildCache"));
			String[] params = {"Multi", "null", map.getName(), "null"};
			CacheUtilities.startProcess(params, DialogCacheBuilder.class.getName(), LogWriter.BUILD_CACHE);
		}
	}

	@Override
	public boolean enable() {
		return CacheUtilities.isEnabled();
	}
}
