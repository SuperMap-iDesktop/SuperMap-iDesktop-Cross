package com.supermap.desktop.process.parameters.ParameterPanels.StatisticsField;

import com.supermap.analyst.spatialanalyst.StatisticsType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.FieldInfo;
import com.supermap.data.FieldInfos;
import com.supermap.data.FieldType;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedEvent;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.parameter.ipls.ParameterSimpleStatisticsFieldGroup;
import com.supermap.desktop.process.parameters.ParameterPanels.SwingPanel;
import com.supermap.desktop.ui.controls.CellRenders.ListStatisticsTypeCellRender;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SortTable.SmSortTable;
import com.supermap.desktop.utilities.ArrayUtilities;
import com.supermap.desktop.utilities.FieldTypeUtilities;
import com.supermap.desktop.utilities.StatisticsTypeUtilities;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * Created by lixiaoyao on 2017/8/8.
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.SIMPLE_STATISTICS_FIELD)
public class ParameterSimpleStatisticsFieldGroupPanel extends SwingPanel {
	private ParameterSimpleStatisticsFieldGroup parameterSimpleStatisticsFieldGroup;
	private JLabel label = new JLabel();
	private SmSortTable table = new SmSortTable();
	private SimpleStatisticsFieldGroupTableModel tableModel = new SimpleStatisticsFieldGroupTableModel();

	public ParameterSimpleStatisticsFieldGroupPanel(IParameter parameter) {
		super(parameter);
		parameterSimpleStatisticsFieldGroup = (ParameterSimpleStatisticsFieldGroup) parameter;
		initComponent();
		initLayout();
		initListener();
	}

	private void initComponent() {
		label.setText(parameterSimpleStatisticsFieldGroup.getDescribe());
		label.setToolTipText(parameterSimpleStatisticsFieldGroup.getDescribe());
		table.setModel(tableModel);
		table.getColumnModel().getColumn(0).setMaxWidth(30);
		table.setDefaultRenderer(FieldType.class, new DefaultTableCellRenderer() {
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

		// 统计类型
		TableColumn column_statisticsType = table.getColumnModel().getColumn(3);
		DefaultCellEditor cellEditorStatisticsType = new StatisticsTypeCellEditor();
		cellEditorStatisticsType.setClickCountToStart(2);
		column_statisticsType.setCellEditor(cellEditorStatisticsType);
		column_statisticsType.setCellRenderer(statisticsTypeCellRenderer);

		DatasetVector dataset = parameterSimpleStatisticsFieldGroup.getDataset();
		if (dataset != null) {
			tableModel.setDataset(dataset);
		}
	}

	private void initLayout() {
		panel.setPreferredSize(new Dimension(200, 200));
		panel.setLayout(new GridBagLayout());
		panel.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(200, 200));
		panel.add(scrollPane, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(5, 0, 0, 0));
	}

	private void initListener() {
		parameterSimpleStatisticsFieldGroup.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(parameterSimpleStatisticsFieldGroup.FIELD_DATASET)) {
					tableModel.setDataset(parameterSimpleStatisticsFieldGroup.getDataset());
				}
			}
		});
		tableModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE) {
					parameterSimpleStatisticsFieldGroup.setSelectedFields(tableModel.getSelectedFields());
					parameterSimpleStatisticsFieldGroup.setSelectedStatisticsType(tableModel.getSelectedStatisticsType());
				}
			}
		});
	}

	DefaultTableCellRenderer statisticsTypeCellRenderer = new DefaultTableCellRenderer() {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (component instanceof JLabel && value instanceof StatisticsType) {
				((JLabel) component).setText(StatisticsTypeUtilities.getStatisticsTypeNameForOtherType((StatisticsType) value));
			}
			return component;
		}
	};

	@Override
	public void fieldConstraintChanged(FieldConstraintChangedEvent event) {
		if (event.getFieldName().equals(parameterSimpleStatisticsFieldGroup.FIELD_DATASET)) {
			tableModel.setDataset(parameterSimpleStatisticsFieldGroup.getDataset());
		}
	}

	class SimpleStatisticsFieldGroupTableModel extends DefaultTableModel {
		String[] columnHeaders = new String[]{
				"",
				ControlsProperties.getString("String_FieldInfoName"),
				ControlsProperties.getString("String_FieldInfoType"),
				ControlsProperties.getString("String_StatisticsType"),
		};
		private ArrayList<TableData> tableDatas = new ArrayList<>();

		public static final int COLUMN_FIELD_IS_SELECTED = 0;
		public static final int COLUMN_FIELD_NAME = 1;
		public static final int COLUMN_FIELD_TYPE = 2;
		public static final int COLUMN_FIELD_STATISTICS_TYPE = 3;
		private Dataset dataset;

		SimpleStatisticsFieldGroupTableModel() {
			// do nothing
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return (column == COLUMN_FIELD_IS_SELECTED || column==COLUMN_FIELD_STATISTICS_TYPE);
		}

		@Override
		public int getRowCount() {
			return tableDatas == null ? 0 : tableDatas.size();
		}

		@Override
		public int getColumnCount() {
			return columnHeaders == null ? 0 : columnHeaders.length;
		}

		@Override
		public String getColumnName(int column) {
			return columnHeaders[column];
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			switch (columnIndex) {
				case COLUMN_FIELD_IS_SELECTED:
					return Boolean.class;
				case COLUMN_FIELD_NAME:
					return String.class;
				case COLUMN_FIELD_TYPE:
					return FieldType.class;
				case COLUMN_FIELD_STATISTICS_TYPE:
					return StatisticsType.class;
			}
			return super.getColumnClass(columnIndex);
		}

		@Override
		public Object getValueAt(int row, int column) {
			switch (column) {
				case 0:
					return tableDatas.get(row).isSelected;
				case 1:
					return tableDatas.get(row).fieldInfo.getName();
				case 2:
					return tableDatas.get(row).fieldInfo.getType();
				case 3:
					return tableDatas.get(row).statisticsType;
			}
			return super.getValueAt(row, column);
		}

		@Override
		public void setValueAt(Object aValue, int row, int column) {
			if (column == COLUMN_FIELD_IS_SELECTED) {
				tableDatas.get(row).isSelected = (boolean) aValue;
				fireTableCellUpdated(row, column);
			} else if (column == COLUMN_FIELD_STATISTICS_TYPE) {
				tableDatas.get(row).statisticsType = (StatisticsType) aValue;
				fireTableCellUpdated(row, column);
			}
		}

		public Dataset getDataset() {
			return dataset;
		}

		public void setDataset(DatasetVector dataset) {
			this.dataset = dataset;
			tableDatas.clear();
			if (dataset != null) {
				FieldInfos fieldInfos = dataset.getFieldInfos();
				int count = fieldInfos.getCount();
				FieldType[] fieldType = parameterSimpleStatisticsFieldGroup.getFieldType();
				//StatisticsType[] statisticsTypes=parameterSimpleStatisticsFieldGroup.getSelectedStatisticsType();
				for (int i = 0; i < count; i++) {
					if ((fieldType == null || ArrayUtilities.isArrayContains(fieldType, fieldInfos.get(i).getType()))) {
						if (!fieldInfos.get(i).isSystemField() && fieldInfos.get(i).getType() != FieldType.DATETIME && fieldInfos.get(i).getType() != FieldType.LONGBINARY) {
							ArrayList<StatisticsType> supportedStatisticsType = getSupportedStatisticsType(fieldInfos.get(i).getType());
							tableDatas.add(new TableData(fieldInfos.get(i), supportedStatisticsType, supportedStatisticsType.get(0)));
						}
					}
				}
			}
			fireTableDataChanged();
		}

		public FieldInfo[] getSelectedFields() {
			ArrayList<FieldInfo> fieldInfoArrayList = new ArrayList<>();
			for (TableData tableData : tableDatas) {
				if (tableData.isSelected) {
					fieldInfoArrayList.add(tableData.fieldInfo);
				}
			}
			return fieldInfoArrayList.toArray(new FieldInfo[fieldInfoArrayList.size()]);
		}

		public StatisticsType[] getSelectedStatisticsType() {
			ArrayList<StatisticsType> statisticsTypes = new ArrayList<>();
			for (TableData tableData : tableDatas) {
				if (tableData.isSelected) {
					statisticsTypes.add(tableData.statisticsType);
				}
			}
			return statisticsTypes.toArray(new StatisticsType[statisticsTypes.size()]);
		}

		public ArrayList<StatisticsType> getCurrentSelectedStatisticsType(int row) {
			ArrayList<StatisticsType> statisticsTypes = new ArrayList<>();
			statisticsTypes = tableDatas.get(row).supportedStatisticsType;
			return statisticsTypes;
		}

		private ArrayList<StatisticsType> getSupportedStatisticsType(FieldType type) {
			ArrayList<StatisticsType> statisticsTypes = new ArrayList<>();
			if (type == FieldType.CHAR || type == FieldType.TEXT || type == FieldType.WTEXT || type == FieldType.BOOLEAN) {
				statisticsTypes.add(StatisticsType.FIRST);
				statisticsTypes.add(StatisticsType.LAST);
			} else {
				statisticsTypes.add(StatisticsType.MAX);
				statisticsTypes.add(StatisticsType.MIN);
				statisticsTypes.add(StatisticsType.SUM);
				statisticsTypes.add(StatisticsType.MEAN);
				statisticsTypes.add(StatisticsType.FIRST);
				statisticsTypes.add(StatisticsType.LAST);
			}
			return statisticsTypes;
		}
	}

	class TableData {
		boolean isSelected;
		FieldInfo fieldInfo;
		StatisticsType statisticsType;
		ArrayList<StatisticsType> supportedStatisticsType;

		TableData(FieldInfo fieldInfo, ArrayList<StatisticsType> supportedStatisticsType, StatisticsType statisticsType) {
			this.fieldInfo = fieldInfo;
			this.supportedStatisticsType = supportedStatisticsType;
			this.statisticsType = statisticsType;
		}
	}

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
			//int realRow = table.convertRowIndexToModel(row);
			//StatisticsFieldInfo statisticsFieldInfo = ((StatisticsFieldTableModel) table.getModel()).getRow(realRow);
			ArrayList<StatisticsType> statisticsTypes = ((SimpleStatisticsFieldGroupTableModel) table.getModel()).getCurrentSelectedStatisticsType(row);
			comboBox = (JComboBox) super.getTableCellEditorComponent(table, value, isSelected, row, column);
			Object item = comboBox.getSelectedItem();
			comboBox.setRenderer(new ListStatisticsTypeCellRender());
			comboBox.removeAllItems();
			for (StatisticsType statisticsType : statisticsTypes) {
				comboBox.addItem(statisticsType);
			}
			if (item != null) {
				comboBox.setSelectedItem(item);
			}
			return comboBox;
		}
	}
}
