package com.supermap.desktop.process.parameters.ParameterPanels;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.JComboBoxUIUtilities;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedEvent;
import com.supermap.desktop.process.parameter.events.ParameterValueLegalListener;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.parameter.ipls.ParameterSaveDatasetTable;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SortTable.SmSortTable;
import com.supermap.desktop.utilities.DatasetTypeUtilities;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * Created By Chens on 2017/8/24 0024
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.SAVE_DATASET_TABLE)
public class ParameterSaveDatasetTablePanel extends SwingPanel implements IParameterPanel {
	private ParameterSaveDatasetTable parameterSaveDatasetTable;
	private JLabel labelDatasource;
	private DatasourceComboBox datasourceComboBox;
	private SmSortTable table;
	private SaveDatasetTableModel saveDatasetTableModel;
	private boolean isSelectingItem = false;

	public ParameterSaveDatasetTablePanel(IParameter parameterSaveDatasetTable) {
		super(parameterSaveDatasetTable);
		this.parameterSaveDatasetTable = (ParameterSaveDatasetTable) parameterSaveDatasetTable;
		initComponent();
		initLayout();
		registerListener();
	}

	private void initComponent() {
		labelDatasource =new JLabel(ControlsProperties.getString("String_Label_ResultDatasource"));
		datasourceComboBox = new DatasourceComboBox();
		initTable();
		initParameterSaveDatasetTable();
	}

	private void initTable() {
		table = new SmSortTable();
		saveDatasetTableModel = new SaveDatasetTableModel(parameterSaveDatasetTable.getDatasetVector());
		table.setModel(saveDatasetTableModel);
		table.getColumnModel().getColumn(0).setMaxWidth(30);
		table.setDefaultRenderer(DatasetType.class, new DefaultTableCellRenderer(){
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JLabel jLabel = new JLabel();
				jLabel.setText(DatasetTypeUtilities.toString((DatasetType) value));
				jLabel.setOpaque(true);
				jLabel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
				jLabel.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
				return jLabel;
			}
		});
	}

	private void initParameterSaveDatasetTable() {
		if (datasourceComboBox.getSelectedItem() != this.parameterSaveDatasetTable.getResultDatasource()) {
			this.parameterSaveDatasetTable.setResultDatasource((Datasource) datasourceComboBox.getSelectedItem());
		}
		if (saveDatasetTableModel.getDatasetNames() != this.parameterSaveDatasetTable.getDatasetNames()) {
			this.parameterSaveDatasetTable.setDatasetNames(saveDatasetTableModel.getDatasetNames());
		}
		if (saveDatasetTableModel.getDatasetTypes() != this.parameterSaveDatasetTable.getDatasetTypes()) {
			this.parameterSaveDatasetTable.setDatasetTypes(saveDatasetTableModel.getDatasetTypes());
		}
	}

	private void initLayout() {
		panel.setLayout(new GridBagLayout());
		panel.add(labelDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 20));
		panel.add(datasourceComboBox, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST).setInsets(0, 25, 5, 5));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(table);
		scrollPane.setPreferredSize(new Dimension(200, 200));
		panel.add(scrollPane, new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 5));
	}

	private void registerListener() {
		datasourceComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!isSelectingItem && e.getStateChange() == ItemEvent.SELECTED) {
					isSelectingItem = true;
					parameterSaveDatasetTable.setResultDatasource((Datasource) datasourceComboBox.getSelectedItem());
					isSelectingItem = false;
					checkDatasetName();
				}
			}
		});
		saveDatasetTableModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE) {
					parameterSaveDatasetTable.setDatasetTypes(saveDatasetTableModel.getDatasetTypes());
					parameterSaveDatasetTable.setDatasetNames(saveDatasetTableModel.getDatasetNames());
				}
			}
		});
		parameterSaveDatasetTable.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterSaveDatasetTable.FIELD_DATASET)) {
					saveDatasetTableModel.setDatasetVector(parameterSaveDatasetTable.getDatasetVector());
				}
			}
		});
	}

	private void checkDatasetName() {
		Datasource selectedDatasource = datasourceComboBox.getSelectedDatasource();
		if (selectedDatasource != null) {
			for (int i = 0; i < table.getRowCount(); i++) {
				String text = table.getValueAt(i, 2).toString();
				String availableDatasetName = selectedDatasource.getDatasets().getAvailableDatasetName(text);
				if (!availableDatasetName.equals(text)) {
					isSelectingItem = true;
					table.setValueAt(availableDatasetName,i,2);
					parameterSaveDatasetTable.setDatasetName(availableDatasetName, i);
					isSelectingItem = false;
				}
			}
		}
	}

	private class SaveDatasetTableModel extends DefaultTableModel {
		String[] columnHeaders = new String[]{
				"",
				ProcessProperties.getString("string_label_lblDatasetType"),
				ControlsProperties.getString("String_Label_DatasetName")
		};
		private ArrayList<TableData> tableDatas = new ArrayList<>();
		private DatasetVector datasetVector;

		public SaveDatasetTableModel(DatasetVector datasetVector) {
			this.datasetVector = datasetVector;
			setDatasetVector(datasetVector);
		}

		public DatasetVector getDatasetVector() {
			return datasetVector;
		}

		public void setDatasetVector(DatasetVector datasetVector) {
			if (datasetVector != null && datasetVector.getType().equals(DatasetType.CAD)) {
				this.datasetVector = datasetVector;
				tableDatas.clear();
				initDatasetTypesByQuery();
				fireTableDataChanged();
			}
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return column == 0 || column == 2;
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
				case 0:
					return Boolean.class;
				case 1:
					return DatasetType.class;
				case 2:
					return String.class;
			}
			return null;
		}

		@Override
		public Object getValueAt(int row, int column) {
			switch (column) {
				case 0:
					return tableDatas.get(row).isSelected;
				case 1:
					return tableDatas.get(row).datasetType;
				case 2:
					return tableDatas.get(row).datasetName;
			}
			return null;
		}

		@Override
		public void setValueAt(Object aValue, int row, int column) {
			if (column == 0) {
				tableDatas.get(row).isSelected = (boolean) aValue;
			} else if (column == 1) {
				tableDatas.get(row).datasetType = (DatasetType) aValue;
			} else {
				tableDatas.get(row).datasetName = aValue.toString();
			}
			fireTableCellUpdated(row,column);
		}

		public DatasetType[] getDatasetTypes() {
			ArrayList<DatasetType> datasetTypes = new ArrayList<>();
			for (TableData tableData : tableDatas) {
				if (tableData.isSelected) {
					datasetTypes.add(tableData.datasetType);
				}
			}
			return datasetTypes.toArray(new DatasetType[datasetTypes.size()]);
		}

		public String[] getDatasetNames() {
			ArrayList<String> strings = new ArrayList<>();
			for (TableData tableData : tableDatas) {
				if (tableData.isSelected) {
					strings.add(tableData.datasetName);
				}
			}
			return strings.toArray(new String[strings.size()]);
		}

		private void initDatasetTypesByQuery() {
			ArrayList<GeometryType> geometryTypes = new ArrayList<>();
			try {
					QueryParameter queryParameter = new QueryParameter();
					queryParameter.setHasGeometry(false);
					queryParameter.setCursorType(CursorType.STATIC);
					queryParameter.setGroupBy(new String[]{"SmGeoType"});
					Recordset recordsetGroup = datasetVector.query(queryParameter);
					if (recordsetGroup != null && recordsetGroup.getRecordCount() > 0) {
						recordsetGroup.moveFirst();
						while (!recordsetGroup.isEOF()) {
							if (geometryTypes.size() <= 7) {
								short value = recordsetGroup.getInt16("SmGeoType");
								GeometryType geometryType = GeometryType.newInstance(value);
								if (geometryType != GeometryType.GEOCOMPOUND && !geometryTypes.contains(geometryType)) {
									geometryTypes.add(geometryType);
								}
								recordsetGroup.moveNext();
							} else {
								break;
							}
						}
						recordsetGroup.close();
						recordsetGroup.dispose();
						//查询复合对象进行遍历
						if (geometryTypes.size() <= 7) {
							queryParameter.setHasGeometry(false);
							queryParameter.setCursorType(CursorType.STATIC);
							queryParameter.setAttributeFilter("SmGeoType='1000'");
							queryParameter.setGroupBy(null);
							queryParameter.setHasGeometry(true);
							Recordset recordset = datasetVector.query(queryParameter);
							recordset.moveFirst();
							while (!recordset.isEOF()) {
								Geometry geoItem = recordset.getGeometry();
								if (geoItem != null) {
									Geometry[] geometrys = null;
									geometrys = ((GeoCompound) geoItem).divide(false);
									if (geometryTypes.size() <= 7) {
										for (int i = 0; i < geometrys.length; i++) {
											if (!geometryTypes.contains(geometrys[i].getType())) {
												geometryTypes.add(geometrys[i].getType());
											}
											geometrys[i].dispose();
										}
										geoItem.dispose();
										recordset.moveNext();
									} else {
										break;
									}
								}
							}
							recordset.close();
							recordset.dispose();
						}
						addDatasetType(geometryTypes);
					}
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e.getMessage());
			}
		}

		private void addDatasetType(ArrayList<GeometryType> geometryTypes) {
			DatasetType datasetType = DatasetType.CAD;
			ArrayList<DatasetType> datasetTypes = new ArrayList<>();
			for (TableData tableData : tableDatas) {
				datasetTypes.add(tableData.datasetType);
			}
			for (GeometryType geometryType : geometryTypes) {
				if (geometryType.equals(GeometryType.GEOLINEM) || geometryType.equals(GeometryType.GEOLINE)
						|| geometryType.equals(GeometryType.GEOARC) || geometryType.equals(GeometryType.GEOBSPLINE)
						|| geometryType.equals(GeometryType.GEOCARDINAL) || geometryType.equals(GeometryType.GEOCURVE)
						|| geometryType.equals(GeometryType.GEOELLIPTICARC) || geometryType.equals(GeometryType.GEOLINE)) {
					datasetType = DatasetType.LINE;
				} else if (geometryType.equals(GeometryType.GEOCIRCLE) || geometryType.equals(GeometryType.GEORECTANGLE)
						|| geometryType.equals(GeometryType.GEOROUNDRECTANGLE) || geometryType.equals(GeometryType.GEOELLIPSE)
						|| geometryType.equals(GeometryType.GEOPIE) || geometryType.equals(GeometryType.GEOCHORD)
						|| geometryType.equals(GeometryType.GEOREGION)) {
					datasetType = DatasetType.REGION;
				} else if (geometryType.equals(GeometryType.GEOPOINT)) {
					datasetType = DatasetType.POINT;
				} else if (geometryType.equals(GeometryType.GEOTEXT)) {
					datasetType = DatasetType.TEXT;
				} else if (geometryType.equals(GeometryType.GEOPOINT3D)) {
					datasetType = DatasetType.POINT3D;
				} else if (geometryType.equals(GeometryType.GEOLINE3D)) {
					datasetType = DatasetType.LINE3D;
				} else if (geometryType.equals(GeometryType.GEOREGION3D)) {
					datasetType = DatasetType.REGION3D;
				} else if (geometryType.equals(GeometryType.GEOMODEL)) {
					datasetType = DatasetType.MODEL;
				}

				if (!datasetTypes.contains(datasetType)) {
					tableDatas.add(new TableData(true, datasetType, "result_CADTo" + datasetType.name()));
					datasetTypes.add(datasetType);
				}
			}
		}
	}

	private class TableData {
		boolean isSelected;
		DatasetType datasetType;
		String datasetName;

		public TableData(boolean isSelected, DatasetType datasetType, String datasetName) {
			this.isSelected = isSelected;
			this.datasetType = datasetType;
			this.datasetName = datasetName;
		}
	}

	@Override
	public void fieldConstraintChanged(FieldConstraintChangedEvent event) {
		if (event.getFieldName().equals(ParameterSaveDatasetTable.FIELD_DATASET)) {
			saveDatasetTableModel.setDatasetVector(parameterSaveDatasetTable.getDatasetVector());

		}
		if (event.getFieldName().equals(ParameterSaveDatasetTable.DATASOURCE_FIELD_NAME)) {
			isSelectingItem = true;
			datasourceComboBox.removeAllItems();
			Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
			for (int i = 0; i < datasources.getCount(); i++) {
				Datasource datasource = datasources.get(i);
				if (!datasource.isReadOnly() && parameterSaveDatasetTable.isValueLegal(ParameterSaveDatasetTable.DATASOURCE_FIELD_NAME, datasource)) {
					datasourceComboBox.addItem(datasource);
				}
			}
			datasourceComboBox.setSelectedItem(null);
			for (int i = 0; i < datasourceComboBox.getItemCount(); i++) {
				Datasource datasource = datasourceComboBox.getItemAt(0);
				Object valueSelected = parameterSaveDatasetTable.isValueSelected(ParameterSaveDatasetTable.DATASOURCE_FIELD_NAME, datasource);
				if (valueSelected != ParameterValueLegalListener.NO) {
					if (valueSelected == ParameterValueLegalListener.DO_NOT_CARE) {
						break;
					} else if (valueSelected instanceof Datasource) {
						datasourceComboBox.setSelectedItem(valueSelected);
						break;
					}
				}
			}

			if (datasourceComboBox.getSelectedItem() == null && datasourceComboBox.getItemCount() > 0) {
				if (parameterSaveDatasetTable.getResultDatasource() != null && JComboBoxUIUtilities.getItemIndex(datasourceComboBox, parameterSaveDatasetTable.getResultDatasource()) != -1) {
					datasourceComboBox.setSelectedItem(parameterSaveDatasetTable.getResultDatasource());
				} else {
					isSelectingItem = false;
					datasourceComboBox.setSelectedIndex(0);
					isSelectingItem = true;
				}
			}
			isSelectingItem = false;
		}
	}
}
