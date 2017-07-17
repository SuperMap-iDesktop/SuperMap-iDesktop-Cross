package com.supermap.desktop.process.meta.metaProcessImplements.DatasetFieldWithDatasource;

import com.supermap.data.Dataset;
import com.supermap.data.FieldInfo;

/**
 * Created by Chen on 2017/6/26 0026.
 * 用于DEM构建的，由数据集、数据源及其字段构成的集合
 */
public class DatasetFieldWithDatasourceInfo {
    private Dataset dataset;
    private FieldInfo fieldInfo;

    public DatasetFieldWithDatasourceInfo(Dataset dataset, FieldInfo fieldInfo) {
        this.dataset = dataset;
        this.fieldInfo = fieldInfo;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public FieldInfo getFieldInfo() {
        return fieldInfo;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    public void setFieldInfo(FieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DatasetFieldWithDatasourceInfo)) return false;

        DatasetFieldWithDatasourceInfo that = (DatasetFieldWithDatasourceInfo) o;

        if (dataset != null ? !dataset.equals(that.dataset) : that.dataset != null)
            return false;
        return fieldInfo != null ? fieldInfo.equals(that.fieldInfo) : that.fieldInfo == null;
    }

    @Override
    public int hashCode() {
        int result = dataset != null ? dataset.hashCode() : 0;
        result = 31 * result + (fieldInfo != null ? fieldInfo.hashCode() : 0);
        return result;
    }
}