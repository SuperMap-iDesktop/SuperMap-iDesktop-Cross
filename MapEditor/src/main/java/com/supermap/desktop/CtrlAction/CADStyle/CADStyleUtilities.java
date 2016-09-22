package com.supermap.desktop.CtrlAction.CADStyle;

import com.supermap.data.Recordset;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Map;

/**
 * Created by xie on 2016/9/20.
 */
public class CADStyleUtilities {
    //工具类不提供构造方法
    public static Recordset getActiveRecordset(Map map) {
        Recordset recordset = null;
        if (map.findSelection(true).length > 0) {
            recordset = MapUtilities.getActiveMap().findSelection(true)[0].toRecordset();
        }
        return recordset;
    }
}
