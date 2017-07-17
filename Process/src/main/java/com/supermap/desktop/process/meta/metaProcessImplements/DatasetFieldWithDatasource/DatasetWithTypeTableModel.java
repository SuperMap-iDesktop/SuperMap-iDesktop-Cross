package com.supermap.desktop.process.meta.metaProcessImplements.DatasetFieldWithDatasource;


import com.supermap.data.*;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

/**
 * Created by Chen on 2017/6/27 0027.
 * DEM构建表添加按钮的弹窗对应的tablemodel，包含数据集及其对应类型
 */
public class DatasetWithTypeTableModel extends DefaultTableModel {
    private ArrayList<Dataset> datasets;

    private final String[] columnNames = new String[]{
            CommonProperties.getString("String_ColumnHeader_Dataset"),
            CommonProperties.getString("String_ColumnHeader_DatasetType"),
    };
    public static final int COLUMN_DATASET = 0;
    public static final int COLUMN_TYPE = 1;

    public DatasetWithTypeTableModel(Datasource datasource) {
        super();
        Datasets datasets = datasource.getDatasets();
        for (int i = 0; i < datasets.getCount(); i++) {
            Dataset dataset = datasets.get(i);
            if (dataset.getType() == DatasetType.LINE || dataset.getType() == DatasetType.POINT) {
                this.datasets.add(dataset);
            }
        }
    }

    public ArrayList<Dataset> getDatasets() {
        return datasets;
    }

    public void setDatasets(ArrayList<Dataset> datasets) {
        this.datasets = datasets;
    }

    public void setDatasets(Datasource datasource) {
        Datasets datasets = datasource.getDatasets();
        for (int i = 0; i < datasets.getCount(); i++) {
            Dataset dataset = datasets.get(i);
            if (dataset.getType() == DatasetType.LINE || dataset.getType() == DatasetType.POINT) {
                this.datasets.add(dataset);
            }
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public Dataset getValueAt(int row, int column) {
        return datasets.get(row);
    }

    public Dataset getRow(int row) {
        if (row < datasets.size()) {
            return datasets.get(row);
        }
        return null;
    }
}
