package com.supermap.desktop.CtrlAction.Map;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.dialog.cacheClip.DialogCacheOperationView;
import com.supermap.desktop.dialog.cacheClip.DialogMapCacheClipBuilder;
import com.supermap.desktop.dialog.cacheClip.cache.CacheUtilities;
import com.supermap.desktop.implement.CtrlAction;

/**
 * Created by xie on 2017/8/23.
 */
public class CtrlActionSingleCache extends CtrlAction {
	public CtrlActionSingleCache(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	protected void run() {
		DialogCacheOperationView singleCache = new DialogCacheOperationView(DialogMapCacheClipBuilder.SingleProcessClip);
		singleCache.setMap(((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().getMap());
		singleCache.showDialog();
	}

	@Override
	public boolean enable() {
		return CacheUtilities.isEnabled();
	}
}

