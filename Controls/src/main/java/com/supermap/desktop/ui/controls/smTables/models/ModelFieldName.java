package com.supermap.desktop.ui.controls.smTables.models;

import com.supermap.desktop.ui.controls.smTables.IModel;
import com.supermap.desktop.ui.controls.smTables.IModelController;
import com.supermap.desktop.ui.controls.smTables.ModelControllerAdapter;

import javax.swing.table.DefaultTableModel;

/**
 * Created by lixiaoyao on 2017/8/10.
 */
public class ModelFieldName extends DefaultTableModel implements IModel {
	private int enableColumn;

	public ModelFieldName(Object[][] data, Object[] columnNames, int enableColumn) {
		super(data, columnNames);
		this.enableColumn = enableColumn;
	}


	// /**
	// * 根据类型返回显示控件
	// * 布尔类型返回显示checkbox
	// */
	@Override
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (enableColumn == columnIndex) {
			return true;
		}
		return false;
	}

	private IModelController modelController=new ModelControllerAdapter() {
		@Override
		public void selectAllOrNull(boolean value) {
			for (int i = 0; i < getRowCount(); i++) {
				setValueAt(value, i, 0);
			}
		}
	};

	@Override
	public IModelController getModelController(){
		return this.modelController;
	}
}
