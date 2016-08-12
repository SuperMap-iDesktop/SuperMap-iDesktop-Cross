package com.supermap.desktop.CtrlAction.CADStyle;

import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.editor.IEditor;

/**
 * Created by xie on 2016/8/10.
 */
public class CADStyleEditor implements IEditor{

    @Override
    public void activate(EditEnvironment environment) {
        CADStyleDialog dialog = new CADStyleDialog();
        dialog.showDialog();
    }

    @Override
    public void deactivate(EditEnvironment environment) {

    }

    @Override
    public boolean enble(EditEnvironment environment) {
        return false;
    }

    @Override
    public boolean check(EditEnvironment environment) {
        return false;
    }
}
