package com.supermap.desktop.CtrlAction;


import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dialog.JDialogHeatMap;
import com.supermap.desktop.dialog.JDialogLogin;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.params.IServerLoginInfo;
import com.supermap.desktop.ui.controls.DialogResult;

import javax.swing.*;

public class CtrlActionHeatMap extends CtrlAction {

    String topicNameRespond = "KernelDensity_Respond";

    public CtrlActionHeatMap(IBaseItem caller, IForm formClass) {
        super(caller, formClass);
    }

    @Override
    public void run() {
        try {
            if (!IServerLoginInfo.login) {
                JDialogLogin loginDialog = new JDialogLogin();
                DialogResult result = loginDialog.showDialog();
                if (result == DialogResult.OK) {
                    JFrame parent = (JFrame) Application.getActiveApplication().getMainFrame();
                    new JDialogHeatMap(parent, true).showDialog();
                }
            } else {
                JFrame parent = (JFrame) Application.getActiveApplication().getMainFrame();
                new JDialogHeatMap(parent, true).showDialog();
            }
//			DialogResult result = dialog.showDialog();
//			if (result == DialogResult.OK || result == DialogResult.APPLY) {
//				WorkThead thread = new WorkThead();
//				thread.start();
//			}
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
    }

    @Override
    public boolean enable() {
        return true;
    }

    class WorkThead extends Thread {

        @Override
        public void run() {
            try {
//				lbsResultConsumer consumer = new lbsResultConsumer();
//				consumer.doWork(topicNameRespond);
            } finally {
            }
        }
    }
}