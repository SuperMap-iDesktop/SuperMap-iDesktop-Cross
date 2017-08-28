package com.supermap.desktop.ui.controls.smTables.tables;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.FieldInfo;
import com.supermap.data.FieldType;
import com.supermap.desktop.ui.controls.SortTable.SmSortTable;
import com.supermap.desktop.ui.controls.smTables.IModel;
import com.supermap.desktop.ui.controls.smTables.ITable;
import com.supermap.desktop.ui.controls.smTables.ITableController;
import com.supermap.desktop.ui.controls.smTables.TableControllerAdapter;
import com.supermap.desktop.ui.controls.smTables.models.ModelFieldNameCaptionType;
import com.supermap.desktop.utilities.FieldTypeUtilities;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Created by lixiaoyao on 2017/8/9.
 */
public class TableFieldNameCaptionType extends SmSortTable implements ITable {

	private ModelFieldNameCaptionType modelFieldNameCaptionType;
	private Dataset dataset;
	private FieldType[] fieldTypes = null;
	private FieldInfo[] selectedFields = null;
	private boolean isShowSystemField = false;
	private static final int TABLE_COLUMN_CHECKABLE = 0;

	private ITableController tableController = new TableControllerAdapter() {
		@Override
		public void selectAll(ITable iTable) {
			super.selectAll(iTable);
		}

		@Override
		public void selectInverse(ITable iTable) {
			super.selectInverse(iTable);
		}

		@Override
		public void delete(ITable iTable) {
			int[] row = iTable.getTable().getSelectedRows();
			for (int i = row.length - 1; i >= 0; i--) {
				((IModel) iTable.getTablesModel()).getModelController().delete(row[i]);
			}
		}

		@Override
		public void selectSystemField(ITable iTable) {
			((IModel) iTable.getTablesModel()).getModelController().selectedSystemField();
		}

		@Override
		public void selectUnSystemField(ITable iTable) {
			((IModel) iTable.getTablesModel()).getModelController().selectedNonSystemField();
		}

	};

	public Dataset getDataset() {
		return this.dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
		if (this.modelFieldNameCaptionType == null) {
			init();
		} else {
			this.modelFieldNameCaptionType.setDataset((DatasetVector) this.dataset,this.isShowSystemField);
		}
	}

	public FieldType[] getFieldTypes() {
		return this.fieldTypes;
	}

	public void setFieldTypes(FieldType[] fieldTypes) {
		this.fieldTypes = fieldTypes;
	}

	public boolean isShowSystemField() {
		return this.isShowSystemField;
	}

	public void setShowSystemField(boolean showSystemField) {
		this.isShowSystemField = showSystemField;
	}

	public FieldInfo[] getSelectedFields() {
		this.selectedFields = this.modelFieldNameCaptionType.getSelectedFields();
		return this.selectedFields;
	}

	@Override
	public ITableController getTableController() {
		return this.tableController;
	}

	@Override
	public JTable getTable() {
		return this;
	}

	@Override
	public Boolean isCheckBoxColumn() {
		return true;
	}

	@Override
	public DefaultTableModel getTablesModel() {
		return this.modelFieldNameCaptionType;
	}

	private void init() {
		this.modelFieldNameCaptionType = new ModelFieldNameCaptionType(this.fieldTypes, this.isShowSystemField);
		if (this.dataset == null) {
			this.modelFieldNameCaptionType.setDataset(null);
		} else {
			this.modelFieldNameCaptionType.setDataset((DatasetVector) this.dataset);
		}
		this.setModel(this.modelFieldNameCaptionType);
		this.getColumnModel().getColumn(TABLE_COLUMN_CHECKABLE).setMaxWidth(30);
		this.setDefaultRenderer(FieldType.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JLabel jLabel = new JLabel();
				jLabel.setText(FieldTypeUtilities.getFieldTypeName((FieldType) value));
				jLabel.setOpaque(true);
				jLabel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
				jLabel.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
				return jLabel;
			}
		});
	}
}

