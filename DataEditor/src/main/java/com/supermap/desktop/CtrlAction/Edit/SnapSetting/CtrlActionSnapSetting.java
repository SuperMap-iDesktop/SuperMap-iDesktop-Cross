package com.supermap.desktop.CtrlAction.Edit.SnapSetting;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.mapping.SnapSetting;

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
                    SnapSetting snapSetting = ((IFormMap) form).getMapControl().getSnapSetting();
                    SnapSettingDialog dialog = new SnapSettingDialog(snapSetting);
                    dialog.showDialog();
                }
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
    }
}
