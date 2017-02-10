package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dialog.JDialogTaskManager;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.utilities.CommonUtilities;
import com.supermap.desktop.utilities.ManagerXMLParser;

public class CtrlActionTaskRecover extends CtrlAction {

    public CtrlActionTaskRecover(IBaseItem caller, IForm formClass) {
        super(caller, formClass);
    }

    @Override
    public void run() {
        if (null == JDialogTaskManager.getTaskManager()) {
            CommonUtilities.recoverTask();
        }
    }

    @Override
    public boolean enable() {
        boolean enable = ManagerXMLParser.getTotalTaskCount() > 0 && null == JDialogTaskManager.getTaskManager()
                && null != CommonUtilities.getManagerContainer() && CommonUtilities.getManagerContainer().getItems().size() == 0;
        return enable;
    }

}
