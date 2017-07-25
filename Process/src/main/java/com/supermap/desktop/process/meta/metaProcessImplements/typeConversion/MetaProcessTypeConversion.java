package com.supermap.desktop.process.meta.metaProcessImplements.typeConversion;

import com.supermap.data.*;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;

import java.util.HashMap;
import java.util.Map;

/**
 * Created By Chens on 2017/7/21 0021
 */
public abstract class MetaProcessTypeConversion extends MetaProcess {

    protected HashMap<String, Object> mergePropertyData(DatasetVector des, FieldInfos srcFieldInfos, Map<String, Object> properties) {
        HashMap<String, Object> results = new HashMap<>();
        FieldInfos desFieldInfos = des.getFieldInfos();

        for (int i = 0; i < desFieldInfos.getCount(); i++) {
            FieldInfo desFieldInfo = desFieldInfos.get(i);

            if (!desFieldInfo.isSystemField() && properties.containsKey(desFieldInfo.getName().toLowerCase())) {
                FieldInfo srcFieldInfo = srcFieldInfos.get(desFieldInfo.getName());

                if (desFieldInfo.getType() == srcFieldInfo.getType()) {
                    // 如果要源字段和目标字段类型一致，直接保存
                    results.put(desFieldInfo.getName(), properties.get(desFieldInfo.getName().toLowerCase()));
                } else if (desFieldInfo.getType() == FieldType.WTEXT || desFieldInfo.getType() == FieldType.TEXT) {

                    // 如果目标字段与源字段类型不一致，则只有目标字段是文本型字段时，将源字段值做 toString 处理
                    results.put(desFieldInfo.getName(), properties.get(desFieldInfo.getName().toLowerCase()).toString());
                }
            }
        }
        return results;
    }

    /**
     * 输入DatasetType，输出对应的DatasetTypes
     * @param type
     * @return
     */
    protected DatasetTypes datasetTypeToTypes(DatasetType type) {
        if (type.equals(DatasetType.POINT)) {
            return DatasetTypes.POINT;
        } else if (type.equals(DatasetType.LINE)) {
            return DatasetTypes.LINE;
        } else if (type.equals(DatasetType.REGION)) {
            return DatasetTypes.REGION;
        } else if (type.equals(DatasetType.TEXT)) {
            return DatasetTypes.TEXT;
        } else if (type.equals(DatasetType.CAD)) {
            return DatasetTypes.CAD;
        } else if (type.equals(DatasetType.LINKTABLE)) {
            return DatasetTypes.LINKTABLE;
        } else if (type.equals(DatasetType.NETWORK)) {
            return DatasetTypes.NETWORK;
        } else if (type.equals(DatasetType.NETWORK3D)) {
            return DatasetTypes.NETWORK3D;
        } else if (type.equals(DatasetType.LINEM)) {
            return DatasetTypes.LINEM;
        } else if (type.equals(DatasetType.PARAMETRICLINE)) {
            return DatasetTypes.PARAMETRICLINE;
        } else if (type.equals(DatasetType.PARAMETRICREGION)) {
            return DatasetTypes.PARAMETRICREGION;
        } else if (type.equals(DatasetType.GRIDCOLLECTION)) {
            return DatasetTypes.GRIDCOLLECTION;
        } else if (type.equals(DatasetType.IMAGECOLLECTION)) {
            return DatasetTypes.IMAGECOLLECTION;
        } else if (type.equals(DatasetType.MODEL)) {
            return DatasetTypes.MODEL;
        } else if (type.equals(DatasetType.TEXTURE)) {
            return DatasetTypes.TEXTURE;
        } else if (type.equals(DatasetType.IMAGE)) {
            return DatasetTypes.IMAGE;
        } else if (type.equals(DatasetType.WMS)) {
            return DatasetTypes.WMS;
        } else if (type.equals(DatasetType.WCS)) {
            return DatasetTypes.WCS;
        } else if (type.equals(DatasetType.GRID)) {
            return DatasetTypes.GRID;
        } else if (type.equals(DatasetType.VOLUME)) {
            return DatasetTypes.VOLUME;
        } else if (type.equals(DatasetType.TOPOLOGY)) {
            return DatasetTypes.TOPOLOGY;
        } else if (type.equals(DatasetType.POINT3D)) {
            return DatasetTypes.POINT3D;
        } else if (type.equals(DatasetType.LINE3D)) {
            return DatasetTypes.LINE3D;
        } else if (type.equals(DatasetType.REGION3D)) {
            return DatasetTypes.REGION3D;
        } else if (type.equals(DatasetType.POINTEPS)) {
            return DatasetTypes.POINTEPS;
        } else if (type.equals(DatasetType.LINEEPS)) {
            return DatasetTypes.LINEEPS;
        } else if (type.equals(DatasetType.REGIONEPS)) {
            return DatasetTypes.REGIONEPS;
        } else if (type.equals(DatasetType.TEXTEPS)) {
            return DatasetTypes.TEXTEPS;
        } else if (type.equals(DatasetType.TABULAR)) {
            return DatasetTypes.TABULAR;
        }
        return null;
    }
}
