package com.supermap.desktop.ui.controls.ChooseTable;

import com.supermap.data.DatasetVector;
import com.supermap.data.FieldInfo;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.CheckHeaderCellRenderer;
import com.supermap.desktop.utilities.FieldTypeUtilities;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;

/**
 * Created by xie on 2017/8/7.
 */
public class SmMultiFieldsChooseTable extends JTable {
	MultipleCheckboxTableModel checkTableModle = null;
	private final Object[] TABLETITLE = {"", CommonProperties.getString("String_SourceFieldName")
			, CommonProperties.getString("String_TargetFieldName"), CommonProperties.getString("String_Field_Type")};
	private final boolean[] ENABLECOLUMNS = {true, false, true, false};
	private final int checkColumnIndexMaxSize = 40;
	private final int rowHeight = 23;
	private final int TABLE_COLUMN_CHECKABLE = 0;
	private final int TABLE_COLUMN_SOURCEFIELDNAME = 1;
	private final int TABLE_COLUMN_TARGETFIELDNAME = 2;
	private final int TABLE_COLUMN_FIELDTYPE = 3;
	private boolean isSelectionChanged = false;
	ArrayList<Info> notSystemFieldInfos = new ArrayList<>();
	private ListSelectionListener listListener = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (!isSelectionChanged) {
				isSelectionChanged = true;
				int[] selectRows = SmMultiFieldsChooseTable.this.getSelectedRows();
				for (int i = 0, length = selectRows.length; i < length; i++) {
					notSystemFieldInfos.get(selectRows[i]).setTargetFieldName(String.valueOf(getValueAt(selectRows[i], TABLE_COLUMN_TARGETFIELDNAME)));
				}
				isSelectionChanged = false;
			}
		}
	};

	public SmMultiFieldsChooseTable(DatasetVector datasetVector) {
		init(datasetVector);
	}

	public void init(DatasetVector datasetVector) {
		this.checkTableModle = new MultipleCheckboxTableModel(getData(datasetVector), TABLETITLE, ENABLECOLUMNS);
		this.setModel(this.checkTableModle);
		this.getColumn(this.getModel().getColumnName(TABLE_COLUMN_CHECKABLE)).setMaxWidth(checkColumnIndexMaxSize);
		this.setRowHeight(rowHeight);
		this.getColumnModel().getColumn(TABLE_COLUMN_CHECKABLE).setCellRenderer(new MultipleCheckboxTableRenderer());
		this.getTableHeader().getColumnModel().getColumn(TABLE_COLUMN_CHECKABLE).setHeaderRenderer(new CheckHeaderCellRenderer(this, "", false));
		this.getSelectionModel().removeListSelectionListener(this.listListener);
		this.getSelectionModel().addListSelectionListener(this.listListener);
	}

	private Object[][] getData(DatasetVector datasetVector) {
		if (datasetVector == null) {
			return new Object[0][0];
		}
		ArrayList<FieldInfo> fieldInfos = new ArrayList<>();
		for (int i = 0; i < datasetVector.getFieldInfos().getCount(); i++) {
			if (!datasetVector.getFieldInfos().get(i).isSystemField()) {
				fieldInfos.add(datasetVector.getFieldInfos().get(i));
			}
		}
		int size = fieldInfos.size();
		Object data[][] = new Object[size][4];
		for (int i = 0; i < size; i++) {
			data[i][TABLE_COLUMN_CHECKABLE] = false;
			data[i][TABLE_COLUMN_SOURCEFIELDNAME] = fieldInfos.get(i).getName();
			data[i][TABLE_COLUMN_TARGETFIELDNAME] = fieldInfos.get(i).getName();
			data[i][TABLE_COLUMN_FIELDTYPE] = FieldTypeUtilities.getFieldTypeName(fieldInfos.get(i).getType());
			Info info = new Info();
			info.setSourceFieldName(fieldInfos.get(i).getName());
			info.setTargetFieldName(fieldInfos.get(i).getName());
			notSystemFieldInfos.add(info);
		}
		return data;
	}

	public ArrayList<Info> getSelectedFieldsName() {
		ArrayList<Info> selectedFieldsName = new ArrayList();

		for (int i = 0; i < this.getRowCount(); i++) {
			if (((boolean) this.getValueAt(i, TABLE_COLUMN_CHECKABLE))) {
				selectedFieldsName.add(notSystemFieldInfos.get(i));
			}
		}
		return selectedFieldsName;
	}

	public class Info {
		private String sourceFieldName;
		private String targetFieldName;

		public String getSourceFieldName() {
			return sourceFieldName;
		}

		public void setSourceFieldName(String sourceFieldName) {
			this.sourceFieldName = sourceFieldName;
		}

		public String getTargetFieldName() {
			return targetFieldName;
		}

		public void setTargetFieldName(String targetFieldName) {
			this.targetFieldName = targetFieldName;
		}
	}
}
