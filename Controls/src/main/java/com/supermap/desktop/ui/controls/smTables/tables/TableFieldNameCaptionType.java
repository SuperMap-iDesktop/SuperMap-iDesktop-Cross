package com.supermap.desktop.ui.controls.smTables.tables;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.FieldInfo;
import com.supermap.data.FieldType;
import com.supermap.desktop.process.parameter.ipls.ParameterFieldGroup;
import com.supermap.desktop.ui.controls.CheckHeaderCellRenderer;
import com.supermap.desktop.ui.controls.SortTable.SmSortTable;
import com.supermap.desktop.ui.controls.smTables.*;
import com.supermap.desktop.ui.controls.smTables.models.ModelFieldNameCaptionType;
import com.supermap.desktop.utilities.FieldTypeUtilities;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by lixiaoyao on 2017/8/9.
 */
public class TableFieldNameCaptionType extends SmSortTable implements ITable {

	private ModelFieldNameCaptionType modelFieldNameCaptionType;
	private Dataset dataset;
	private FieldType[] fieldTypes=null;
	private FieldInfo[] selectedFields=null;
	private boolean isShowSystemField=false;
	private static final int TABLE_COLUMN_CHECKABLE = 0;

	private ITableController tableController=new TableControllerAdapter() {
		@Override
		public void selectedAll(ITable iTable) {
			super.selectedAll(iTable);
		}

		@Override
		public void selectedIInverse(ITable iTable) {
			super.selectedIInverse(iTable);
		}

		@Override
		public void delete(ITable iTable) {
			int[] row = iTable.getTable().getSelectedRows();
			for(int i=row.length-1;i>=0;i--){
				((IModel)iTable.getTablesModel()).getModelController().delete(row[i]);
			}
		}

		@Override
		public void selectedSystemField(ITable iTable) {
			((IModel)iTable.getTablesModel()).getModelController().selectedSystemField();
		}

		@Override
		public void selectedNonSystemField(ITable iTable) {
			((IModel)iTable.getTablesModel()).getModelController().selectedNonSystemField();
		}

	};

	public Dataset getDataset() {
		return this.dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
		init();
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
		return selectedFields;
	}

	@Override
	public ITableController getTableController(){
		return this.tableController;
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
		return this.modelFieldNameCaptionType;
	}

	private void init(){
		this.modelFieldNameCaptionType=new ModelFieldNameCaptionType(this.fieldTypes,this.isShowSystemField);
		this.modelFieldNameCaptionType.setDataset((DatasetVector)this.dataset);
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
		removeListener();
		registerListener();
	}

	private TableModelListener tableModelListener=new TableModelListener() {
		@Override
		public void tableChanged(TableModelEvent e) {
			if (e.getType() == TableModelEvent.UPDATE) {
				selectedFields=modelFieldNameCaptionType.getSelectedFields();
			}
		}
	};

	private void registerListener(){
		this.modelFieldNameCaptionType.addTableModelListener(this.tableModelListener);
	}

	private void removeListener(){
		this.modelFieldNameCaptionType.removeTableModelListener(this.tableModelListener);
	}

}
