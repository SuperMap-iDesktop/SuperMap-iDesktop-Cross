package com.supermap.desktop.CtrlAction.Map;

import com.supermap.data.processing.MapCacheBuilder;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dialog.cacheClip.DialogMapCacheClipBuilder;
import com.supermap.desktop.dialog.cacheClip.cache.CacheUtilities;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.mapping.Map;

/**
 * Created by xie on 2017/7/7.
 */
public class CtrlActionWorkSpaceMultiProcessClipNew extends CtrlAction {
	public CtrlActionWorkSpaceMultiProcessClipNew(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	protected void run() {
		Map map = CacheUtilities.getWorkspaceSelectedMap();
		if (CacheUtilities.volatileDatasource()) {
			MapCacheBuilder mapCacheBuilder = new MapCacheBuilder();
			Map newMap = new Map(Application.getActiveApplication().getWorkspace());
			newMap.fromXML(map.toXML());
			mapCacheBuilder.setMap(newMap);
			new DialogMapCacheClipBuilder(DialogMapCacheClipBuilder.MultiProcessClip, mapCacheBuilder).showDialog();
		}
	}

	@Override
	public boolean enable() {
		return CacheUtilities.selectedMapIsEnabled();
	}
}

