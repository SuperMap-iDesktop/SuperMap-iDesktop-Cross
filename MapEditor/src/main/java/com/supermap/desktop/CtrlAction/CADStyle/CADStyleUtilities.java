package com.supermap.desktop.CtrlAction.CADStyle;

import com.supermap.data.Recordset;
import com.supermap.mapping.Map;

/**
 * Created by xie on 2016/9/20.
 */
public class CADStyleUtilities {
    //工具类不提供构造方法

    /**
     * 获取活动的记录集
     *
     * @param map
     * @return
     */
    public static Recordset getActiveRecordset(Map map) {
        Recordset recordset = null;
        if (!selectionHasDisposed(map) && map.findSelection(true).length > 0) {
            recordset = map.findSelection(true)[0].toRecordset();
        }
        return recordset;
    }

    /**
     * 由于selection没有判断dispose方法，故自己处理
     *
     * @param map
     * @return
     */
    private static boolean selectionHasDisposed(Map map) {
        boolean result = false;
        try {
            map.findSelection(true);
        } catch (Exception ex) {
            result = true;
        }
        return result;
    }
}
