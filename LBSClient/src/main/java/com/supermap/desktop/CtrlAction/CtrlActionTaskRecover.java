package com.supermap.desktop.CtrlAction;

import com.supermap.Interface.TaskEnum;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dialog.JDialogTaskManager;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.utilities.CommonUtilities;
import com.supermap.desktop.utilities.ManagerXMLParser;

import java.util.List;

public class CtrlActionTaskRecover extends CtrlAction {

    public CtrlActionTaskRecover(IBaseItem caller, IForm formClass) {
        super(caller, formClass);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.supermap.desktop.implement.CtrlAction#run()
     */
    @Override
    public void run() {
        if (null == JDialogTaskManager.getTaskManager()) {
            CommonUtilities.recoverTask();
        }
    }

    @Override
    public boolean enable() {
        List<String> downloadTaskPropertyLists = ManagerXMLParser.getTaskPropertyList(TaskEnum.DOWNLOADTASK);
        List<String> uploadTaskPropertyLists = ManagerXMLParser.getTaskPropertyList(TaskEnum.UPLOADTASK);
        int recoverTaskCount = downloadTaskPropertyLists.size() + uploadTaskPropertyLists.size();
        return recoverTaskCount > 0;
    }

}
