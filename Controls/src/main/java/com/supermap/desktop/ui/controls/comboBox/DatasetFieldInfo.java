package com.supermap.desktop.ui.controls.comboBox;

import com.supermap.data.DatasetVector;
import com.supermap.data.FieldInfo;

/**
 * Created by hanyz on 2017/2/20.
 */
public class DatasetFieldInfo {
    DatasetVector dataset;
    FieldInfo fieldInfo;
    boolean isPrimaryTable = false;

    public DatasetFieldInfo(DatasetVector dataset, FieldInfo fieldInfo) {
        this.dataset = dataset;
        this.fieldInfo = fieldInfo;
    }

    public DatasetFieldInfo(DatasetVector dataset, FieldInfo fieldInfo, boolean isPrimaryTable) {
        this.dataset = dataset;
        this.fieldInfo = fieldInfo;
        this.isPrimaryTable = isPrimaryTable;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof DatasetFieldInfo) {
            return super.equals(obj);
        }
        if (obj instanceof FieldInfo && this.getFieldInfo() != null) {
            return this.getFieldInfo().equals(obj);
        }
        return obj instanceof String && obj.equals(this.toString());
    }

    @Override
    public String toString() {
        String datasetName = this.dataset == null ? "" : this.dataset.getName();
        String FieldCaption = this.fieldInfo == null ? "" : this.fieldInfo.getCaption();
        if (isPrimaryTable) {
            return FieldCaption;
        } else {
            return datasetName + "." + FieldCaption;
        }
    }

    public DatasetVector getDataset() {
        return dataset;
    }

    public void setDataset(DatasetVector dataset) {
        this.dataset = dataset;
    }

    public void setFieldInfo(FieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    public FieldInfo getFieldInfo() {
        return fieldInfo;
    }


}
