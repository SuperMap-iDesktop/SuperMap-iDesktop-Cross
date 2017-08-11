package com.supermap.desktop.ui.controls.smTables.tables;

import com.supermap.data.DatasetVector;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.CheckHeaderCellRenderer;
import com.supermap.desktop.ui.controls.smTables.CheckHeaderCellRender;
import com.supermap.desktop.ui.controls.smTables.ITable;
import com.supermap.desktop.ui.controls.smTables.ITableController;
import com.supermap.desktop.ui.controls.smTables.TableControllerAdapter;
import com.supermap.desktop.ui.controls.smTables.models.ModelFieldName;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

/**
 * Created by lixiaoyao on 2017/8/9.
 */
public class TableFieldName extends JTable implements ITable {

	private ModelFieldName modelFieldName = null;
	private DatasetVector datasetVector=null;
	private final Object[] tableTitle = {"", ControlsProperties.getString("String_ColumnRampName")};
	private final static int CHECK_COLUMN_INDEX_MAX_SIZE =40;
	private final static int ROW_HEIGHT =23;
	private final static int ENABLE_COLUMN =0;
	private static final int TABLE_COLUMN_CHECKABLE = 0;
	private static final int TABLE_COLUMN_CAPTION = 1;
	ArrayList<String> isNotSystemFields = new ArrayList<String>();

//	private ITableController tableController=new TableControllerAdapter() {
//		@Override
//		public void selectedAll(ITable iTable) {
//			super.selectedAll(iTable);
//		}
//
//		@Override
//		public void selectedIInverse(ITable iTable) {
//			super.selectedIInverse(iTable);
//		}
//
//		@Override
//		public void delete(ITable iTable) {
//			super.delete(iTable);
//		}
//	};

	@Override
	public ITableController getTableController(){
		return null;
	}

	@Override
	public JTable getTable(){
		return this;
	}

	@Override
	public Boolean isCheckBoxColumn(){
		return true;
	}

	@Override
	public DefaultTableModel getTablesModel(){
		return null;
	}

	public DatasetVector getDatasetVector() {
		return this.datasetVector;
	}

	public void setDatasetVector(DatasetVector datasetVector) {
		this.datasetVector = datasetVector;
		this.modelFieldName = new ModelFieldName(getData(this.datasetVector), tableTitle, ENABLE_COLUMN);
		this.setModel(this.modelFieldName);
		this.getColumn(this.getModel().getColumnName(TABLE_COLUMN_CHECKABLE)).setMaxWidth(CHECK_COLUMN_INDEX_MAX_SIZE);
		init();
	}

	public TableFieldName(){

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

	private void init(){
		this.setRowHeight(ROW_HEIGHT);
		this.getTableHeader().getColumnModel().getColumn(TABLE_COLUMN_CHECKABLE).setHeaderRenderer(new CheckHeaderCellRender(this,"",false));
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
