package com.supermap.desktop.CtrlAction.Map;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dialog.cacheClip.DialogCacheCheck;
import com.supermap.desktop.implement.CtrlAction;

/**
 * Created by xie on 2017/5/11.
 */
public class CtrlActionMapCacheCheck extends CtrlAction {
    public CtrlActionMapCacheCheck(IBaseItem caller, IForm formClass) {
        super(caller, formClass);
    }

    @Override
    public void run() {
        new DialogCacheCheck().showDialog();
    }

    @Override
    public boolean enable() {
        return true;
    }
}
