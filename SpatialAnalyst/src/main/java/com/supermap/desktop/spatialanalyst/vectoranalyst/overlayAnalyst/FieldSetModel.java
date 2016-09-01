package com.supermap.desktop.spatialanalyst.vectoranalyst.overlayAnalyst;

import com.supermap.data.DatasetVector;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.table.AbstractTableModel;

/**
 * Created by xie on 2016/8/31.
 */
public class FieldSetModel extends AbstractTableModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final String[] tableTitle = {ControlsProperties.getString("String_ColumnHeader_FieldIndexes"), CommonProperties.getString("String_FieldName")};
    private DatasetVector dataaset;

    public FieldSetModel(DatasetVector dataaset) {
        super();
        this.dataaset = dataaset;
    }

    public FieldSetModel() {
        super();
    }

    @Override
    public int getRowCount() {
        int count = 0;
        for (int i = 0; i < dataaset.getFieldInfos().getCount(); i++) {
            if (!dataaset.getFieldInfos().get(i).isSystemField()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int getColumnCount() {
        return tableTitle.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return tableTitle[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Boolean.class;
        } else {
            return String.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
//        ExportFileInfo tempExportFileInfo = exports.get(rowIndex);
//        if (0 == columnIndex) {
//            return tempExportFileInfo.getDatasetCell();
//        }
//        if (1 == columnIndex) {
//            return tempExportFileInfo.getDatasource().getAlias();
//        }
//        if (2 == columnIndex) {
//            return tempExportFileInfo.getDataType();
//        }
        return "";
    }
}
