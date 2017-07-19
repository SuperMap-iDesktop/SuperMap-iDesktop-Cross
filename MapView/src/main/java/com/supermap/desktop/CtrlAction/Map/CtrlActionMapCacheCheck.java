package com.supermap.desktop.CtrlAction.Map;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dialog.cacheClip.DialogCacheCheck;
import com.supermap.desktop.dialog.cacheClip.cache.CacheUtilities;
import com.supermap.desktop.dialog.cacheClip.cache.LogWriter;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.mapview.MapViewProperties;

/**
 * Created by xie on 2017/5/11.
 */
public class CtrlActionMapCacheCheck extends CtrlAction {
	public CtrlActionMapCacheCheck(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_StartCheckCache"));
		CacheUtilities.startProcess(new String[]{}, DialogCacheCheck.class.getName(), LogWriter.CHECK_CACEH);
//		new DialogCacheCheck().setVisible(true);
	}

	@Override
	public boolean enable() {
		return true;
	}
}
