package com.supermap.desktop.CtrlAction.LayerSetting;

import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.dialog.DialogTerrainUniformColor;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;

import javax.swing.*;
import java.util.ArrayList;

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
        boolean result = false;
        IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
        ArrayList<Layer> arrayList = MapUtilities.getLayers(formMap.getMapControl().getMap(), true); //针对formap获取layers，如果当有图层分组时会拿不到所有图层
        int indexCount = 0; // 用来判断当前地图窗口打开的删格数据集有几个，如果只有一个功能不可用，如果有两个或两个以上，才可用
        if (arrayList != null) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).getDataset()!=null &&arrayList.get(i).getDataset().getType() == DatasetType.GRID) {
                    indexCount = indexCount + 1;
                }
            }
        }
        if (indexCount >= 2) {
            result = true;
        }
        return result;
    }
}
