package com.supermap.desktop.ui.controls.smTables;

import com.supermap.desktop.Application;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

/**
 * Created by lixiaoyao on 2017/8/9.
 */
public abstract class TableControllerAdapter implements ITableController {

	@Override
	public void selectAll(ITable iTable) {
		try {
			if (iTable.isCheckBoxColumn()) {
				((IModel) iTable.getTablesModel()).getModelController().selectedAll();
			} else {
				iTable.getTable().setRowSelectionInterval(0, iTable.getTable().getRowCount() - 1);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public void selectInverse(ITable iTable) {
		try {
			if (iTable.isCheckBoxColumn()) {
				((IModel) iTable.getTablesModel()).getModelController().selectedIInverse();
			} else {
				int[] temp;
				ListSelectionModel selectionModel;
				int allRowCount;

				selectionModel = iTable.getTable().getSelectionModel();
				temp = iTable.getTable().getSelectedRows();
				allRowCount = iTable.getTable().getRowCount();

				ArrayList<Integer> selectedRows = new ArrayList<Integer>();
				for (int index = 0; index < temp.length; index++) {
					selectedRows.add(temp[index]);
				}

				selectionModel.clearSelection();
				for (int index = 0; index < allRowCount; index++) {
					if (!selectedRows.contains(index)) {
						selectionModel.addSelectionInterval(index, index);
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/*
	选择系统字段
	 */
	@Override
	public void selectSystemField(ITable iTable) {

	}

	/*
	选择非系统字段
	 */
	@Override
	public void selectUnSystemField(ITable iTable) {

	}

	@Override        //此方法默认实现只是针对数据存储在tablemodel中的table，类似于TableFieldNameCaptionType类中的model，请自行实现
	public void delete(ITable iTable) {
		try {
			int[] row = iTable.getTable().getSelectedRows();
			for (int i = row.length - 1; i >= 0; i--) {
				//iTable.getTablesModel().delete(row[i]);
				((DefaultTableModel) iTable.getTable().getModel()).removeRow(row[i]);
			}
			//int selectedRowCount=iTable.getTable().getSelectedRowCount();
			//for (int i=0;i<selectedRowCount;i++){
			//int deleteRowIndex=iTable.getTable().convertRowIndexToModel(iTable.getTable().getSelectedRow()-i);
			//((DefaultTableModel)iTable.getTable().getModel()).removeRow(deleteRowIndex);
			//}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}


}
