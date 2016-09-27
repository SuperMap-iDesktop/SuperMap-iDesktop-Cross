package com.supermap.desktop.CtrlAction.transformationForm.Dialogs;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.data.TransformationMode;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.JComboBoxUIUtilities;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.CellRenders.TableDatasourceCellRender;
import com.supermap.desktop.ui.controls.ComponentBorderPanel.CompTitledPane;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.SortTable.SmSortTable;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.datasetChoose.DatasetChooseMode;
import com.supermap.desktop.ui.controls.datasetChoose.DatasetChooser;
import com.supermap.desktop.utilities.TransformationModeUtilities;
import com.supermap.mapping.Map;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 新建配准
 *
 * @author XiaJT
 */
public class JDialogNewTransformationForm extends SmDialog {

	private JPanel panelTransformationLayer = new JPanel();
	private JLabel labelTransformationDatasource = new JLabel();
	private DatasourceComboBox comboBoxTransformationDatasource;
	private JLabel labelTransformationDataset = new JLabel();
	private DatasetComboBox comboBoxTransformationDataset = new DatasetComboBox(new Dataset[0]);

	private JPanel panelReferenceLayer = new JPanel();
	private SmSortTable tableReferenceLayers = new SmSortTable();
//	private JLabel labelReferenceDatasource = new JLabel();
//	private DatasourceComboBox comboBoxReferenceDatasource;
//	private JLabel labelReferenceDataset = new JLabel();
//	private DatasetComboBox comboBoxReferenceDataset = new DatasetComboBox();

	private CompTitledPane panelResultDatasetMain;
	private JPanel panelResultDataset = new JPanel();
	private JCheckBox checkBoxSaveAsDataset = new JCheckBox();
	private JLabel labelResultDatasource = new JLabel();
	private DatasourceComboBox comboBoxResultDatasource;
	private JLabel labelResultDataset = new JLabel();
	private SmTextFieldLegit textFieldResultDatasetName = new SmTextFieldLegit();

	private JLabel labelTransformationMode = new JLabel();

	private JComboBox<TransformationMode> comboBoxTransformationMode = new JComboBox<>(new TransformationMode[]{
			TransformationMode.OFFSET,
			TransformationMode.RECT,
			TransformationMode.LINEAR,
			TransformationMode.SQUARE
	});
	private JPanel panelButton = new JPanel();
	private SmButton smButtonOK = new SmButton();
	private SmButton smButtonCancel = new SmButton();

	private static final DatasetType[] datasetTypes = new DatasetType[]{
			DatasetType.POINT, DatasetType.LINE, DatasetType.REGION, DatasetType.TEXT, DatasetType.CAD, DatasetType.NETWORK,
			DatasetType.LINEM, DatasetType.GRID, DatasetType.IMAGE, DatasetType.POINT3D, DatasetType.LINE3D, DatasetType.REGION3D,
			DatasetType.GRIDCOLLECTION, DatasetType.IMAGECOLLECTION, DatasetType.PARAMETRICLINE, DatasetType.PARAMETRICREGION,
			DatasetType.NETWORK3D
	};
	private ReferenceLayersTableModel referenceLayersTableModel;
	private JScrollPane scrollPane;


	public JDialogNewTransformationForm() {
		init();
	}

	private void init() {
		initComponents();
		initLayout();
		initResources();
		initListeners();
		initComponentState();
	}

	private void initComponents() {
		referenceLayersTableModel = new ReferenceLayersTableModel();
		tableReferenceLayers.setModel(referenceLayersTableModel);
		tableReferenceLayers.setIsSortLastRow(false);
		tableReferenceLayers.getColumnModel().getColumn(0).setCellRenderer(new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				DataCell dataCell = new DataCell(value);
				if (isSelected) {
					dataCell.setBackground(table.getSelectionBackground());
				} else {
					dataCell.setBackground(table.getBackground());
				}
				return dataCell;
			}
		});
		tableReferenceLayers.getColumnModel().getColumn(2).setMaxWidth(30);
		tableReferenceLayers.getColumnModel().getColumn(1).setCellRenderer(new TableDatasourceCellRender());
		tableReferenceLayers.getColumnModel().getColumn(2).setCellRenderer(new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JLabel x = new JLabel();
				if (row != tableReferenceLayers.getRowCount() - 1) {
					x.setText("X");
				}
				x.setHorizontalAlignment(SwingConstants.CENTER);
				x.setOpaque(true);
				x.setForeground(Color.red);
				if (isSelected) {
					x.setBackground(table.getSelectionBackground());
				} else {
					x.setBackground(table.getBackground());
				}
				return x;
			}
		});
//		((DefaultRowSorter) tableReferenceLayers.getRowSorter()).setRowFilter(new RowFilter() {
//			@Override
//			public boolean include(Entry entry) {
//				return entry.getValue(0) != tableReferenceLayers.getValueAt(tableReferenceLayers.getRowCount() - 1, 0);
//			}
//		});
		textFieldResultDatasetName.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				return comboBoxResultDatasource.getSelectedDatasource().getDatasets().getAvailableDatasetName(textFieldValue).equals(textFieldValue);
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		});
		ArrayList<Datasource> datasourcesList = new ArrayList<>();
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		for (int i = 0; i < datasources.getCount(); i++) {
			if (datasources.get(i).isOpened()) {
				datasourcesList.add(datasources.get(i));
			}
		}
		Datasource[] datasourcesArray = datasourcesList.toArray(new Datasource[datasourcesList.size()]);
		comboBoxTransformationDatasource = new DatasourceComboBox(datasourcesArray);

		datasourcesList.clear();
		for (int i = 0; i < datasources.getCount(); i++) {
			if (datasources.get(i).isOpened() && !datasources.get(i).isReadOnly()) {
				datasourcesList.add(datasources.get(i));
			}
		}
		datasourcesArray = datasourcesList.toArray(new Datasource[datasourcesList.size()]);
		comboBoxResultDatasource = new DatasourceComboBox(datasourcesArray);

		panelResultDatasetMain = new CompTitledPane(checkBoxSaveAsDataset, panelResultDataset);
		panelTransformationLayer.setBorder(new TitledBorder(DataEditorProperties.getString("String_Transfernation_TargetLayer")));
		panelReferenceLayer.setBorder(new TitledBorder(DataEditorProperties.getString("String_Transfernation_ReferLayer")));
		comboBoxTransformationMode.setRenderer(new ListCellRenderer<TransformationMode>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends TransformationMode> list, TransformationMode value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel result = new JLabel();
				result.setText(TransformationModeUtilities.toString(value));
				result.setOpaque(true);
				result.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
				return result;
			}
		});
		comboBoxTransformationMode.setSelectedItem(TransformationMode.LINEAR);
		scrollPane = new JScrollPane(tableReferenceLayers);
		this.setSize(new Dimension(400, 600));
		this.setLocationRelativeTo(null);
	}

	private void initLayout() {
		panelTransformationLayer.setLayout(new GridBagLayout());
		panelTransformationLayer.add(labelTransformationDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(10, 10, 0, 0));
		panelTransformationLayer.add(comboBoxTransformationDatasource, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10));
		panelTransformationLayer.add(labelTransformationDataset, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(5, 10, 10, 0));
		panelTransformationLayer.add(comboBoxTransformationDataset, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 10, 10));

		panelReferenceLayer.setLayout(new GridBagLayout());
//		panelReferenceLayer.add(buttonAddReferenceLayers, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setWeight(1, 0).setInsets(10, 10, 0, 10));
		panelReferenceLayer.add(scrollPane, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setInsets(5, 10, 10, 10));
//		panelReferenceLayer.add(labelReferenceDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(10, 10, 0, 0));
//		panelReferenceLayer.add(comboBoxReferenceDatasource, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10));
//		panelReferenceLayer.add(labelReferenceDataset, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(5, 10, 10, 0));
//		panelReferenceLayer.add(comboBoxReferenceDataset, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 10, 10));

		panelResultDataset.setLayout(new GridBagLayout());
		panelResultDataset.add(labelResultDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(10, 10, 0, 0));
		panelResultDataset.add(comboBoxResultDatasource, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10));
		panelResultDataset.add(labelResultDataset, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(5, 10, 10, 0));
		panelResultDataset.add(textFieldResultDatasetName, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 10, 10));

		panelButton.setLayout(new GridBagLayout());
		panelButton.add(smButtonOK, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setWeight(1, 0).setInsets(0, 0, 0, 0));
		panelButton.add(smButtonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.NONE).setWeight(0, 0).setInsets(0, 5, 0, 0));

		this.setLayout(new GridBagLayout());
		this.add(panelTransformationLayer, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 0).setInsets(10, 10, 0, 10));


		this.add(panelReferenceLayer, new GridBagConstraintsHelper(0, 2, 2, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setInsets(5, 10, 0, 10));

		this.add(labelTransformationMode, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.NONE).setInsets(5, 20, 0, 0));
		this.add(comboBoxTransformationMode, new GridBagConstraintsHelper(1, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0).setFill(GridBagConstraints.NONE).setInsets(5, 5, 0, 20));

		this.add(panelResultDatasetMain, new GridBagConstraintsHelper(0, 4, 2, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 0).setInsets(5, 10, 0, 10));
		this.add(panelButton, new GridBagConstraintsHelper(0, 5, 2, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 0).setInsets(5, 10, 10, 10));
	}

	private void initResources() {
		this.setTitle(DataEditorProperties.getString("String_TransfernationPreSetTitle"));
		labelTransformationDatasource.setText(ControlsProperties.getString("String_Label_ResultDatasource"));
		labelResultDatasource.setText(ControlsProperties.getString("String_Label_ResultDatasource"));
		labelTransformationDataset.setText(ControlsProperties.getString("String_Label_ResultDataset"));
//		labelReferenceDataset.setText(ControlsProperties.getString("String_Label_ResultDataset"));
		labelTransformationMode.setText(DataEditorProperties.getString("String_TransformationMode"));
		labelResultDataset.setText(ControlsProperties.getString("String_Label_ResultDataset"));
		checkBoxSaveAsDataset.setText(DataEditorProperties.getString("String_Transfernation_Resave"));
		smButtonOK.setText(CommonProperties.getString(CommonProperties.OK));
		smButtonCancel.setText(CommonProperties.getString(CommonProperties.Cancel));
	}

	private void initListeners() {
		comboBoxTransformationDatasource.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED && comboBoxTransformationDatasource.getSelectedItem() != null) {
					comboBoxTransformationDataset.setDatasets(comboBoxTransformationDatasource.getSelectedDatasource().getDatasets());
				}
			}
		});

		comboBoxResultDatasource.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED && comboBoxResultDatasource.getSelectedItem() != null) {
					textFieldResultDatasetName.setText(getUniqueDatasetName(textFieldResultDatasetName.getText()));
				}
			}
		});

		smButtonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogResult = DialogResult.OK;
				dispose();
			}
		});

		smButtonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogResult = DialogResult.CANCEL;
				dispose();
			}
		});

		checkBoxSaveAsDataset.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean selected = checkBoxSaveAsDataset.isSelected();
				comboBoxResultDatasource.setEnabled(selected);
				textFieldResultDatasetName.setEditable(selected);
			}
		});
		tableReferenceLayers.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					Point point = e.getPoint();
					int row = tableReferenceLayers.rowAtPoint(point);
					int column = tableReferenceLayers.columnAtPoint(point);

					if (row != tableReferenceLayers.getRowCount() - 1 && column == 2) {
						referenceLayersTableModel.removeDataAt(tableReferenceLayers.convertRowIndexToModel(row));
						return;
					}
					if (e.getClickCount() == 2) {
						addButtonClicked();
					}
				}
			}
		});
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
					addButtonClicked();
				}
			}
		});
	}

	private void addButtonClicked() {
		DatasetChooser datasetChooser = new DatasetChooser(DatasetChooseMode.DATASET, DatasetChooseMode.MAP);
		datasetChooser.setSupportDatasetTypes(datasetTypes);
		if (datasetChooser.showDialog() == DialogResult.OK) {
			List<Object> selectedObjects = datasetChooser.getSelectedObjects();
			for (Object selectedObject : selectedObjects) {
				referenceLayersTableModel.addData(selectedObject);
			}
		}
	}

	private void initComponentState() {
		Dataset currentDataset = null;
		Datasource currentDatasource = null;
		Dataset[] activeDatasets = Application.getActiveApplication().getActiveDatasets();
		if (activeDatasets.length > 0) {
			currentDataset = activeDatasets[0];
			currentDatasource = currentDataset.getDatasource();
		} else {
			Datasource[] activeDatasources = Application.getActiveApplication().getActiveDatasources();
			if (activeDatasources.length > 0) {
				for (Datasource activeDatasource : activeDatasources) {
					if (activeDatasource.isOpened() && activeDatasource.getDatasets().getCount() > 0) {
						currentDatasource = activeDatasource;
						break;
					}
				}
			}
			Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
			for (int i = 0; i < datasources.getCount(); i++) {
				Datasource activeDatasource = datasources.get(i);
				if (activeDatasource.isOpened() && activeDatasource.getDatasets().getCount() > 0) {
					currentDatasource = activeDatasource;
					break;
				}
			}
		}
		comboBoxTransformationDatasource.setSelectedIndex(-1);
		if (JComboBoxUIUtilities.getItemIndex(comboBoxTransformationDatasource, currentDatasource) != -1) {
			comboBoxTransformationDatasource.setSelectedDatasource(currentDatasource);
			comboBoxResultDatasource.setSelectedDatasource(currentDatasource);
			if (currentDataset != null) {
				comboBoxTransformationDataset.setSelectedDataset(currentDataset);
			}
		} else {
			if (comboBoxTransformationDatasource.getItemCount() > 0) {
				comboBoxTransformationDatasource.setSelectedIndex(0);
			}
		}
		checkBoxSaveAsDataset.setSelected(true);
		textFieldResultDatasetName.setText(getUniqueDatasetName("Result_adjust"));
	}

	private String getUniqueDatasetName(String datasetName) {
		Datasource selectedDatasource = comboBoxResultDatasource.getSelectedDatasource();
		return selectedDatasource.getDatasets().getAvailableDatasetName(datasetName);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	/**
	 * @return 配准数据集
	 */
	public Dataset getTransformationDataset() {
		return comboBoxTransformationDataset.getSelectedDataset();
	}


	/**
	 * 结果数据集名称
	 *
	 * @return
	 */
	public String getResultDatasetName() {
		if (checkBoxSaveAsDataset.isSelected()) {
			return textFieldResultDatasetName.getText();
		}
		return null;
	}

	public Datasource getResultDatasource() {
		if (checkBoxSaveAsDataset.isSelected()) {
			return comboBoxResultDatasource.getSelectedDatasource();
		}
		return null;
	}

	public List<Object> getReferenceObjects() {
		return referenceLayersTableModel.getDataList();
	}

	public TransformationMode getTransformationMode() {
		return (TransformationMode) comboBoxTransformationMode.getSelectedItem();
	}

	private class ReferenceLayersTableModel extends DefaultTableModel {
		private ArrayList<Object> dataList = new ArrayList<>();
		private String[] columnNames = new String[]{
				CommonProperties.getString(CommonProperties.stringDataset),
				CommonProperties.getString(CommonProperties.stringDatasource),
				""
		};

		@Override
		public int getRowCount() {
			if (dataList == null) {
				return 0;
			}
			return dataList.size();
		}

		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

		@Override
		public Object getValueAt(int row, int column) {
			if (row >= dataList.size()) {
				if (column == 0) {
					return ControlsProperties.getString("String_AddMore");
				}
				return "";
			}
			if (column == 0) {
				return dataList.get(row);
			} else if (column == 1) {
				Object data = dataList.get(row);
				if (data instanceof Dataset) {
					return ((Dataset) data).getDatasource();
				}
			}
			return "";
		}


		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == 0) {
				return Object.class;
			}
			return String.class;
		}

		public java.util.List<Object> getDataList() {
			return dataList;
		}

		public void addData(Object selectedObject) {
			dataList.add(selectedObject);
			fireTableRowsInserted(dataList.size() - 1, dataList.size() - 1);
		}

		public void removeDataAt(int row) {
			if (row >= getRowCount()) {
				throw new UnsupportedOperationException(String.valueOf(row + "_" + getRowCount()));
			}
			Object o = dataList.get(row);
			if (o instanceof com.supermap.mapping.Map) {
				((Map) o).close();
			}
			dataList.remove(o);
			fireTableRowsDeleted(row, row);
		}
	}
}
