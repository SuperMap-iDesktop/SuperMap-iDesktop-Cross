package com.supermap.desktop.ui.controls.smTables.tables;

import com.supermap.analyst.spatialanalyst.StatisticsField;
import com.supermap.analyst.spatialanalyst.StatisticsFieldType;
import com.supermap.data.DatasetVector;
import com.supermap.data.FieldType;
import com.supermap.desktop.ui.controls.CellRenders.ListStatisticsTypeCellRender;
import com.supermap.desktop.ui.controls.SortTable.SmSortTable;
import com.supermap.desktop.ui.controls.smTables.CheckHeaderCellRender;
import com.supermap.desktop.ui.controls.smTables.ITable;
import com.supermap.desktop.ui.controls.smTables.ITableController;
import com.supermap.desktop.ui.controls.smTables.models.ModelRarefyPoints;
import com.supermap.desktop.utilities.FieldTypeUtilities;
import com.supermap.desktop.utilities.StatisticsTypeUtilities;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by lixiaoyao on 2017/8/15.
 */
public class TableRarefyPoints extends SmSortTable implements ITable{
	private ModelRarefyPoints modelRarefyPoints=null;
	private DatasetVector datasetVector=null;
	private final static int CHECK_COLUMN_INDEX_MAX_SIZE = 30;
	private static final int TABLE_COLUMN_CHECKABLE = 0;
	private static final int TABLE_COLUMN_STATISTICS_TYPE = 4;
	private StatisticsField statisticsField[];

	@Override
	public ITableController getTableController() {
		return null;
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
		return this.modelRarefyPoints;
	}

	public DatasetVector getDatasetVector() {
		return this.datasetVector;
	}

	public void setDatasetVector(DatasetVector datasetVector) {
		this.datasetVector = datasetVector;
		if (this.modelRarefyPoints==null){
			init();
		}else{
			this.modelRarefyPoints.setDataset(this.datasetVector);
		}
	}

	public void setAllStatisticsFieldType(StatisticsFieldType statisticsFieldType){
		for (int i=0;i<this.modelRarefyPoints.getRowCount();i++) {
			this.modelRarefyPoints.setValueAt(statisticsFieldType,i,TABLE_COLUMN_STATISTICS_TYPE);
		}
	}

	public TableRarefyPoints(){

	}

	private void init() {
		this.modelRarefyPoints = new ModelRarefyPoints();
		this.setModel(this.modelRarefyPoints);
		this.modelRarefyPoints.setDataset(this.datasetVector);
		this.getColumn(this.getModel().getColumnName(TABLE_COLUMN_CHECKABLE)).setMaxWidth(CHECK_COLUMN_INDEX_MAX_SIZE);
		this.getTableHeader().getColumnModel().getColumn(TABLE_COLUMN_CHECKABLE).setHeaderRenderer(new CheckHeaderCellRender(this, "", true));
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

		TableColumn column_statisticsType = this.getColumnModel().getColumn(TABLE_COLUMN_STATISTICS_TYPE);
		DefaultCellEditor cellEditorStatisticsType = new StatisticsTypeCellEditor();
		cellEditorStatisticsType.setClickCountToStart(2);
		column_statisticsType.setCellEditor(cellEditorStatisticsType);
		column_statisticsType.setCellRenderer(statisticsTypeCellRenderer);
	}


	public StatisticsField[] getStatisticsField() {
		this.statisticsField=this.modelRarefyPoints.getSelectedStatisticsField();
		return this.statisticsField;
	}

	DefaultTableCellRenderer statisticsTypeCellRenderer = new DefaultTableCellRenderer() {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (component instanceof JLabel && value instanceof StatisticsFieldType) {
				((JLabel) component).setText(StatisticsTypeUtilities.getStatisticsTypeNameForDatasetRarefyPointsType((StatisticsFieldType) value));
			}
			return component;
		}
	};

	// 添加到Table中的统计类型选择ComboBox
	private class StatisticsTypeCellEditor extends DefaultCellEditor {
		JComboBox comboBox;

		public StatisticsTypeCellEditor() {
			super(new JComboBox());
		}

		@Override
		public Object getCellEditorValue() {
			if (comboBox != null) {
				return comboBox.getSelectedItem();
			}
			return null;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			ArrayList<StatisticsFieldType> statisticsTypes = ((ModelRarefyPoints) table.getModel()).getCurrentSelectedStatisticsType(row);
			comboBox = (JComboBox) super.getTableCellEditorComponent(table, value, isSelected, row, column);
			Object item = comboBox.getSelectedItem();
			comboBox.setRenderer(new ListStatisticsTypeCellRender());
			comboBox.removeAllItems();
			for (StatisticsFieldType statisticsType : statisticsTypes) {
				comboBox.addItem(statisticsType);
			}
			if (item != null) {
				comboBox.setSelectedItem(item);
			}
			return comboBox;
		}
	}
}
