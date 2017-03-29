package com.supermap.desktop.CtrlAction.Map;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.dialog.DialogMapCacheBuilder;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.mapping.Layers;

import javax.swing.*;

/**
 * Created by lixiaoyao on 2017/3/15.
 */
public class CtrlActionMapCacheBuilder extends CtrlAction {
    public CtrlActionMapCacheBuilder(IBaseItem caller, IForm formClass) {
        super(caller, formClass);
    }

    @Override
    public void run() {
        DialogMapCacheBuilder dialogMapCacheBuilder = new DialogMapCacheBuilder((JFrame) Application.getActiveApplication().getMainFrame(), true);
        DialogResult result = dialogMapCacheBuilder.showDialog();
    }

    @Override
    public boolean enable() {
        boolean result=false;
        IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
        Layers currentFormMapLayer = formMap.getMapControl().getMap().getLayers();
        if (currentFormMapLayer.getCount()>0){
            result=true;
        }
        return result;
    }
}
