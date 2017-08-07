package com.supermap.desktop.CtrlAction.Map;

import com.supermap.data.processing.MapCacheBuilder;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.dialog.cacheClip.DialogMapCacheClipBuilder;
import com.supermap.desktop.dialog.cacheClip.cache.CacheUtilities;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.mapping.Map;

/**
 * Created by xie on 2017/7/7.
 */
public class CtrlActionMultiProcessClipNew extends CtrlAction {
	public CtrlActionMultiProcessClipNew(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	protected void run() {
		Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_StartBuildCacheNew"));
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Map map = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().getMap();
				if (!CacheUtilities.dynamicEffectClosed(map)) {
					return;
				}
				if (CacheUtilities.voladateDatasource()) {
					MapCacheBuilder mapCacheBuilder = new MapCacheBuilder();
					Map newMap = new Map(Application.getActiveApplication().getWorkspace());
					newMap.fromXML(map.toXML());
					mapCacheBuilder.setMap(newMap);
					new DialogMapCacheClipBuilder(DialogMapCacheClipBuilder.MultiProcessClip, mapCacheBuilder).showDialog();
				}
			}
		}, "thread");
		thread.start();
	}

	@Override
	public boolean enable() {
		return CacheUtilities.isEnabled();
	}
}
