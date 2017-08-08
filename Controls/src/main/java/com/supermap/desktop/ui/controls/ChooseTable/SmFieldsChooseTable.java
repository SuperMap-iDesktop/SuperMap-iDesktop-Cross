package com.supermap.desktop.ui.controls.ChooseTable;

import com.supermap.data.DatasetVector;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.CheckTableModel;
import com.supermap.desktop.ui.controls.CheckHeaderCellRenderer;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by lixiaoyao on 2017/2/10.
 */
public class SmFieldsChooseTable extends JTable {
    CheckTableModel checkTableModel = null;
    private final Object[] tableTitle = {"", CommonProperties.getString("String_Field_Caption")};
    private final static int checkColumnIndex=0;
    private final static int checkColumnIndexMaxSize=40;
    private final static int rowHeight=23;
    private final static int enableColumn=0;
    private static final int TABLE_COLUMN_CHECKABLE = 0;
    private static final int TABLE_COLUMN_CAPTION = 1;
    ArrayList<String> isNotSystemFields = new ArrayList<String>();

    private void init(){
        this.setRowHeight(rowHeight);
        this.getTableHeader().getColumnModel().getColumn(TABLE_COLUMN_CHECKABLE).setHeaderRenderer(new CheckHeaderCellRenderer(this,"",false));
    }

    public SmFieldsChooseTable(DatasetVector datasetVector) {
        this.checkTableModel = new CheckTableModel(getData(datasetVector), tableTitle, enableColumn);
        this.setModel(this.checkTableModel);
        this.getColumn(this.getModel().getColumnName(checkColumnIndex)).setMaxWidth(checkColumnIndexMaxSize);
        init();
    }

    private Object[][] getData(DatasetVector datasetVector){
	    if (datasetVector == null) {
		    return new Object[0][0];
	    }
	    int count = 0;
	    for (int i = 0; i < datasetVector.getFieldInfos().getCount(); i++) {
            if (!datasetVector.getFieldInfos().get(i).isSystemField()) {
                count++;
            }
        }
        Object data[][]=new Object[count][2];
        int length = 0;
        for (int i = 0; i < datasetVector.getFieldInfos().getCount(); i++) {
            if (!datasetVector.getFieldInfos().get(i).isSystemField()) {
                data[length][TABLE_COLUMN_CHECKABLE]=false;
                data[length][TABLE_COLUMN_CAPTION]= datasetVector.getFieldInfos().get(i).getCaption();
                isNotSystemFields.add(datasetVector.getFieldInfos().get(i).getName());
                length++;
            }
        }
        return data;
    }

    public ArrayList getSelectedFieldsName(){
        ArrayList<String> selectedFieldsName = new ArrayList<String>();

        for (int i=0;i<this.getRowCount();i++){
            if ((Boolean)this.getValueAt(i,TABLE_COLUMN_CHECKABLE)){
                selectedFieldsName.add(isNotSystemFields.get(i));
            }
        }
        return selectedFieldsName;
    }
}
