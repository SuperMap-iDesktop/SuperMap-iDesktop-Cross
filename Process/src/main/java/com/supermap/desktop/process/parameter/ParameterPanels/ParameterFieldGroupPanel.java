package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.FieldInfo;
import com.supermap.data.FieldInfos;
import com.supermap.data.FieldType;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedEvent;
import com.supermap.desktop.process.parameter.implement.ParameterFieldGroup;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SortTable.SmSortTable;
import com.supermap.desktop.utilities.ArrayUtilities;
import com.supermap.desktop.utilities.FieldTypeUtilities;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * @author XiaJT
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.FIELD_GROUP)
public class ParameterFieldGroupPanel extends SwingPanel {
	private ParameterFieldGroup parameterFieldGroup;
	private JLabel label = new JLabel();
	private SmSortTable table = new SmSortTable();
	private FieldGroupTableModel tableModel = new FieldGroupTableModel();

	public ParameterFieldGroupPanel(IParameter parameter) {
		super(parameter);
		parameterFieldGroup = (ParameterFieldGroup) parameter;
		initComponent();
		initLayout();
		initListener();
	}

	@Override
	public void fieldConstraintChanged(FieldConstraintChangedEvent event) {
		if (event.getFieldName().equals(ParameterFieldGroup.FIELD_DATASET)) {
			tableModel.setDataset(parameterFieldGroup.getDataset());
		}
	}

	private void initComponent() {
		label.setText(parameterFieldGroup.getDescribe());
		label.setToolTipText(parameterFieldGroup.getDescribe());
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
		DatasetVector dataset = parameterFieldGroup.getDataset();
		if (dataset != null) {
			tableModel.setDataset(dataset);
		}
	}

	private void initListener() {
		parameterFieldGroup.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterFieldGroup.FIELD_DATASET)) {
					tableModel.setDataset(parameterFieldGroup.getDataset());
				}
			}
		});
		tableModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE) {
					parameterFieldGroup.setSelectedFields(tableModel.getSelectedFields());
				}
			}
		});
	}

	private void initLayout() {
		panel.setPreferredSize(new Dimension(200, 200));
		panel.setLayout(new GridBagLayout());
		panel.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(200, 200));
		panel.add(scrollPane, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(5, 0, 0, 0));
	}

	class FieldGroupTableModel extends DefaultTableModel {
		String[] columnHeaders = new String[]{
				"",
				ControlsProperties.getString("String_ColumnRampName"),
				CommonProperties.getString("String_Field_Caption"),
				ControlsProperties.getString("String_GeometryPropertyTabularControl_DatGridViewColumnFieldType")
		};
		private ArrayList<TableData> tableDatas = new ArrayList<>();

		public static final int COLUMN_INDEX_IS_SELECTED = 0;
		public static final int COLUMN_INDEX_NAME = 1;
		public static final int COLUMN_INDEX_CAPTION = 2;
		public static final int COLUMN_INDEX_TYPE = 3;
		private Dataset dataset;

		FieldGroupTableModel() {

		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return column == 0;
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
				case COLUMN_INDEX_IS_SELECTED:
					return Boolean.class;
				case COLUMN_INDEX_NAME:
				case COLUMN_INDEX_CAPTION:
					return String.class;
				case COLUMN_INDEX_TYPE:
					return FieldType.class;
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
					return tableDatas.get(row).fieldInfo.getCaption();
				case 3:
					return tableDatas.get(row).fieldInfo.getType();
			}
			return super.getValueAt(row, column);
		}

		@Override
		public void setValueAt(Object aValue, int row, int column) {
			if (column == 0) {
				tableDatas.get(row).isSelected = (boolean) aValue;
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
				FieldType[] fieldType = parameterFieldGroup.getFieldType();
				for (int i = 0; i < count; i++) {
					if (fieldType == null || ArrayUtilities.isArrayContains(fieldType, fieldInfos.get(i).getType())) {
						if (!fieldInfos.get(i).isSystemField() || parameterFieldGroup.isShowSystemField()) {
							tableDatas.add(new TableData(fieldInfos.get(i)));
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
	}

	class TableData {
		boolean isSelected;
		FieldInfo fieldInfo;

		TableData(FieldInfo fieldInfo) {
			this.fieldInfo = fieldInfo;
		}
	}
}
