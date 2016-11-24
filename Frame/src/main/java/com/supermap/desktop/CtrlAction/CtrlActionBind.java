package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.*;
import com.supermap.desktop.controls.GeometryPropertyBindWindow.BindUtilties;
import com.supermap.desktop.implement.CtrlAction;

import java.util.List;

/**
 * Created by xie on 2016/11/10.
 */
public class CtrlActionBind extends CtrlAction {
    public static List selectList;

    public CtrlActionBind(IBaseItem caller, IForm formClass) {
        super(caller, formClass);
    }

    @Override
    public void run() {
        try {
            BindUtilties.showPopumenu(getCaller());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean enable() {
        IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
        int size = formManager.getCount();
        int bindSize = 0;
        for (int i = 0; i < size; i++) {
            IForm form = formManager.get(i);
            if (form instanceof IFormMap || form instanceof IFormTabular) {
                bindSize++;
            }
        }
        return bindSize > 1;
    }
}
