package com.supermap.desktop.CtrlAction.Map;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dialog.cacheClip.DialogCacheUpdate;
import com.supermap.desktop.implement.CtrlAction;

/**
 * Created by xie on 2017/6/12.
 */
public class CtrlActionMapCacheUpdate extends CtrlAction {
	public CtrlActionMapCacheUpdate(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		new DialogCacheUpdate().showDialog();
	}

	@Override
	public boolean enable() {
		return true;
	}
}
