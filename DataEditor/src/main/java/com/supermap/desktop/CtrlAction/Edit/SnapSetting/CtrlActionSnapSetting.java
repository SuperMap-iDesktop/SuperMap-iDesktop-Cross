package com.supermap.desktop.CtrlAction.Edit.SnapSetting;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;

/**
 * Created by xie on 2016/12/7.
 */
public class CtrlActionSnapSetting extends CtrlAction {
    public CtrlActionSnapSetting(IBaseItem caller, IForm formClass) {
        super(caller, formClass);
    }

    @Override
    public void run() {
        try {
            IForm form = Application.getActiveApplication().getActiveForm();
            if (form != null) {
                if (form instanceof IFormMap && null != ((IFormMap) form).getMapControl()) {
	                SnapSettingDialog dialog = new SnapSettingDialog();
	                dialog.showDialog();
                }
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
    }

    @Override
    public boolean enable() {
        boolean enable = false;
        try {
            IForm form = Application.getActiveApplication().getActiveForm();
            if (form != null) {
                if (form instanceof IFormMap && null != ((IFormMap) form).getMapControl()) {
                    enable = true;
                }
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
        return enable;
    }

}
