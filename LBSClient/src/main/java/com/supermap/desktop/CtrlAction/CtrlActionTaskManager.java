package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dialog.JDialogTaskManager;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.utilities.CommonUtilities;
import com.supermap.desktop.utilities.ManagerXMLParser;

/**
 * Created by xie on 2017/1/18.
 */
public class CtrlActionTaskManager extends CtrlAction {
    public CtrlActionTaskManager(IBaseItem caller, IForm formClass) {
        super(caller, formClass);
    }

    @Override
    public void run() {
        if (ManagerXMLParser.getTotalTaskCount() > 0 && null == JDialogTaskManager.getTaskManager()
                && null != CommonUtilities.getManagerContainer() && CommonUtilities.getManagerContainer().getItems().size() == 0) {
            CommonUtilities.recoverTask();
        } else {
            CommonUtilities.getFileManagerContainer();
        }
    }

    @Override
    public boolean enable() {
        return true;
    }
}
