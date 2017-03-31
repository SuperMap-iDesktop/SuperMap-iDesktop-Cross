package com.supermap.desktop.CtrlAction.Map;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.dialog.DialogMapCacheBuilder;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by lixiaoyao on 2017/3/15.
 */
public class CtrlActionMapCacheBuilder extends CtrlAction {
    public CtrlActionMapCacheBuilder(IBaseItem caller, IForm formClass) {
        super(caller, formClass);
    }

    @Override
    public void run() {
        IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
        DialogMapCacheBuilder dialogMapCacheBuilder = new DialogMapCacheBuilder((JFrame) Application.getActiveApplication().getMainFrame(), true,formMap.getMapControl().getMap());
        DialogResult result = dialogMapCacheBuilder.showDialog();
    }

    @Override
    public boolean enable() {
        boolean result=false;
        IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
        if (formMap!=null) {
            ArrayList<Layer> arrayList = MapUtilities.getLayers(formMap.getMapControl().getMap(), true);
            if (arrayList.size() > 0) {
                result = true;
            }
        }
        return result;
    }
}
