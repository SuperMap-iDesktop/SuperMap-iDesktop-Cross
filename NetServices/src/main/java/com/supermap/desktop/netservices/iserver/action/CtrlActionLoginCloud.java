package com.supermap.desktop.netservices.iserver.action;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.CloudLicenseDialog;

import javax.swing.*;

/**
 * Created by xie on 2016/12/22.
 */
public class CtrlActionLoginCloud extends CtrlAction {

    public CtrlActionLoginCloud(IBaseItem caller, IForm formClass) {
        super(caller, formClass);
    }

    @Override
    public void run() {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CloudLicenseDialog cloudLicenseDialog = new CloudLicenseDialog();
                cloudLicenseDialog.setVisible(true);
            }
        });
    }
}
