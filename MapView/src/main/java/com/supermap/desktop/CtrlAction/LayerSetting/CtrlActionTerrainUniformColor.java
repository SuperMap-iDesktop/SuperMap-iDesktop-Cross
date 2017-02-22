package com.supermap.desktop.CtrlAction.LayerSetting;

import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.dialog.DialogTerrainUniformColor;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.mapping.Layers;

import javax.swing.*;

/**
 * Created by lixiaoyao on 2017/2/17.
 */
public class CtrlActionTerrainUniformColor extends CtrlAction {

    public CtrlActionTerrainUniformColor(IBaseItem caller, IForm formClass) {
        super(caller, formClass);
    }

    @Override
    public void run() {
        DialogTerrainUniformColor dialogTerrainUniformColor = new DialogTerrainUniformColor((JFrame) Application.getActiveApplication().getMainFrame(), true);
        DialogResult result = dialogTerrainUniformColor.showDialog();
    }

    @Override
    public boolean enable() {
        boolean result=false;
        IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
        Layers currentFormMapLayer = formMap.getMapControl().getMap().getLayers();
        for (int i = 0; i < currentFormMapLayer.getCount(); i++) {
            if (currentFormMapLayer.get(i).getDataset().getType() == DatasetType.GRID) {
                result=true;
                break;
            }
        }
        return result;
    }
}
