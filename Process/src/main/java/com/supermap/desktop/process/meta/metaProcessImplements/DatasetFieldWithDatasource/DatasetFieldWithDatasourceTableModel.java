package com.supermap.desktop.process.meta.metaProcessImplements.DatasetFieldWithDatasource;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.FieldInfo;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

/**
 * Created by Chen on 2017/6/26 0026.
 */
public class DatasetFieldWithDatasourceTableModel extends DefaultTableModel{
    private ArrayList<DatasetFieldWithDatasourceInfo> datasetFieldWithDatasourceInfos = new ArrayList<>();
    private final String[] columnNames = new String[]{
            CommonProperties.getString("String_ColumnHeader_Dataset"),
            CommonProperties.getString("String_ColumnHeader_Datasource"),
            ProcessProperties.getString("String_Label_HeightField")
    };
    public static final int COLUMN_DATASET = 0;
    public static final int COLUMN_DATASOURCE = 1;
    public static final int COLUMN_FIELD = 2;

    public ArrayList<DatasetFieldWithDatasourceInfo> getDatasetFieldWithDatasourceInfos() {
        return datasetFieldWithDatasourceInfos;
    }

    public void setDatasetFieldWithDatasourceInfos(ArrayList<DatasetFieldWithDatasourceInfo> datasetFieldWithDatasourceInfos) {
        if (datasetFieldWithDatasourceInfos != null) {
            this.datasetFieldWithDatasourceInfos = datasetFieldWithDatasourceInfos;
        } else {
            this.datasetFieldWithDatasourceInfos.clear();
        }
        fireTableDataChanged();
    }

    public DatasetFieldWithDatasourceTableModel(ArrayList<DatasetFieldWithDatasourceInfo> datasetFieldWithDatasourceInfos) {
        super();
        if (datasetFieldWithDatasourceInfos != null) {
            this.datasetFieldWithDatasourceInfos = datasetFieldWithDatasourceInfos;
        }
    }

    public void addRow(DatasetFieldWithDatasourceInfo rowData) {
        datasetFieldWithDatasourceInfos.add(rowData);
        fireTableRowsInserted(datasetFieldWithDatasourceInfos.size() - 1, datasetFieldWithDatasourceInfos.size() - 1);
    }

    public DatasetFieldWithDatasourceInfo getRow(int row) {
        if (row < datasetFieldWithDatasourceInfos.size()) {
            return datasetFieldWithDatasourceInfos.get(row);
        }
        return null;
    }

    @Override
    public void removeRow(int row) {
        datasetFieldWithDatasourceInfos.remove(row);
        fireTableRowsDeleted(row,row);
    }

    @Override
    public int getRowCount() {
        if (datasetFieldWithDatasourceInfos == null) {
            return 0;
        }
        return datasetFieldWithDatasourceInfos.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == COLUMN_FIELD) {
            return true;
        }
        return false;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (column == COLUMN_DATASET) {
            return datasetFieldWithDatasourceInfos.get(row).getDataset();
        } else if (column == COLUMN_DATASOURCE) {
            return datasetFieldWithDatasourceInfos.get(row).getDataset().getDatasource();
        } else if (column == COLUMN_FIELD) {
            return datasetFieldWithDatasourceInfos.get(row).getFieldInfo();
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == COLUMN_DATASET) {
            return Dataset.class;
        } else if (columnIndex == COLUMN_DATASOURCE) {
            return Datasource.class;
        } else if (columnIndex == COLUMN_FIELD) {
            return FieldInfo.class;
        }
        return super.getColumnClass(columnIndex);
    }
}
