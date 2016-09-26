package com.supermap.desktop.CtrlAction.CADStyle;

import com.supermap.data.DatasetType;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Recordset;
import com.supermap.mapping.Layers;
import com.supermap.mapping.Map;

import java.util.ArrayList;

/**
 * Created by xie on 2016/9/20.
 */
public class CADStyleUtilities {
    //工具类不提供构造方法

    /**
     * 获取可编辑的文本，或者CAD记录集
     *
     * @param map
     * @return
     */
    public static ArrayList<Recordset> getActiveRecordset(Map map) {
        ArrayList<Recordset> recordset = null;
        if (!selectionHasDisposed(map) && map.findSelection(true).length > 0) {
            recordset = new ArrayList<Recordset>();
            Layers laysers = map.getLayers();
            int layersCount = laysers.getCount();
            for (int i = 0; i < layersCount; i++) {
                if (laysers.get(i).isEditable() && (laysers.get(i).getDataset().getType().equals(DatasetType.CAD) || laysers.get(i).getDataset().getType().equals(DatasetType.TEXT))) {
                    recordset.add(laysers.get(i).getSelection().toRecordset());
                }
            }
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

    public static boolean is3DGeometry(Geometry tempGeometry) {
        if (!tempGeometry.getType().equals(GeometryType.GEOREGION3D)
                && !tempGeometry.getType().equals(GeometryType.GEOLINE3D) && !tempGeometry.getType().equals(GeometryType.GEOPOINT3D)) {
            return false;
        }
        return true;
    }
}
