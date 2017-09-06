package com.supermap.desktop.process.parameters.ParameterPanels;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Interface.ISmTextFieldLegit;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedEvent;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedListener;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.parameter.ipls.ParameterCommonStatisticCombine;
import com.supermap.desktop.process.parameter.ipls.ParameterDatasetChooseTable;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.TextFields.NumTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created By Chens on 2017/8/21 0021
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.COMMON_STATISTIC)
public class ParameterCommonStatisticCombinePanel extends SwingPanel {
	private ParameterCommonStatisticCombine parameterCommonStatisticCombine;
	private ButtonGroup buttonGroup;
	private JRadioButton radioButtonValue;
	private JRadioButton radioButtonDatasets;
	private JLabel labelValue;
	private NumTextFieldLegit textFieldLegit;
	private JPanelDatasetChooseForParameter datasetChooseForParameter;
	private final String[] columnNames = {"", CommonProperties.getString("String_ColumnHeader_Dataset"), CommonProperties.getString("String_ColumnHeader_Datasource")};
	private final boolean[] enables = {false, false, false};
	private boolean isSelectingItem = false;

	public ParameterCommonStatisticCombinePanel(IParameter parameterCommonStatisticCombine) {
		super(parameterCommonStatisticCombine);
		this.parameterCommonStatisticCombine = (ParameterCommonStatisticCombine) parameterCommonStatisticCombine;
		initComponent();
		initLayout();
		registerListener();
	}

	private void initComponent() {
		buttonGroup = new ButtonGroup();
		radioButtonValue = new JRadioButton();
		radioButtonDatasets = new JRadioButton();
		labelValue = new JLabel();
		textFieldLegit = new NumTextFieldLegit();
		datasetChooseForParameter = new JPanelDatasetChooseForParameter(parameterCommonStatisticCombine.getDatasets(), columnNames, enables);

		buttonGroup.add(radioButtonValue);
		buttonGroup.add(radioButtonDatasets);
		radioButtonValue.setText(ProcessProperties.getString("String_RadioButton_CommonStatisticType_Single"));
		radioButtonDatasets.setText(ProcessProperties.getString("String_RadioButton_DatasetGrid"));
		radioButtonValue.setSelected(true);
		datasetChooseForParameter.setVisible(false);
		labelValue.setText(ProcessProperties.getString("String_Label_CommonStatisticType_Single"));
		textFieldLegit.setText("0");
		this.datasetChooseForParameter.setIllegalDataset(parameterCommonStatisticCombine.getDataset());
		if (null != this.parameterCommonStatisticCombine.getDataset()) {
			this.datasetChooseForParameter.setSupportDatasetTypes(new DatasetType[]{this.parameterCommonStatisticCombine.getDataset().getType()});
		}
	}

	private void initLayout() {
		panel.setLayout(new GridBagLayout());
		JPanel panelRadio = new JPanel();
		panelRadio.setLayout(new GridBagLayout());

		panelRadio.add(radioButtonValue, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 5, 0));
		panelRadio.add(radioButtonDatasets, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 5, 0));
		panel.add(panelRadio, new GridBagConstraintsHelper(0, 0, 2, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 0));
		panel.add(labelValue, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 5, 0, 20));
		panel.add(textFieldLegit, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 25, 0, 5));
		panel.add(datasetChooseForParameter, new GridBagConstraintsHelper(0, 2, 2, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 0, 0));
	}

	private void registerListener() {
		radioButtonValue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (radioButtonValue.isSelected()) {
					parameterCommonStatisticCombine.setValueChosen(true);
					textFieldLegit.setVisible(true);
					labelValue.setVisible(true);
					datasetChooseForParameter.setVisible(false);
				}
			}
		});
		radioButtonDatasets.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (radioButtonDatasets.isSelected()) {
					parameterCommonStatisticCombine.setValueChosen(false);
					textFieldLegit.setVisible(false);
					labelValue.setVisible(false);
					datasetChooseForParameter.setVisible(true);
				}
			}
		});
		parameterCommonStatisticCombine.addFieldConstraintChangedListener(new FieldConstraintChangedListener() {
			@Override
			public void fieldConstraintChanged(FieldConstraintChangedEvent event) {
				Dataset dataset = ((ParameterDatasetChooseTable) event.getParameter()).getDataset();
				if (null != dataset) {
					datasetChooseForParameter.setIllegalDataset(dataset);
					datasetChooseForParameter.setSupportDatasetTypes(new DatasetType[]{dataset.getType()});
				}
			}
		});
		datasetChooseForParameter.getTableModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				parameterCommonStatisticCombine.setSelectedItem(datasetChooseForParameter.getDatasets());
			}
		});
		textFieldLegit.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				change();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				change();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				change();
			}

			private void change() {
				if (!isSelectingItem && !StringUtilities.isNullOrEmpty(textFieldLegit.getText())) {
					isSelectingItem = true;
					parameterCommonStatisticCombine.setSelectedItem(Double.valueOf(textFieldLegit.getBackUpValue().toString()));
					isSelectingItem = false;
				}
			}
		});
	}

//	class JPanelDatasetChooseForParameter extends JPanelDatasetChoose {
//		private final int COLUMN_INDEX = 0;
//		private final int COLUMN_DATASET = 1;
//		private final int COLUMN_DATASOURCE = 2;
//		private final int MAX_SIZE = 40;
//
//		public JPanelDatasetChooseForParameter(ArrayList<Dataset> datasets, String[] columnName, boolean[] enableColumn) {
//			super(datasets, columnName, enableColumn);
//			this.tableDatasetDisplay.getColumnModel().getColumn(COLUMN_INDEX).setMaxWidth(MAX_SIZE);
//		}
//
//		@Override
//		protected void exchangeItem(int sourceRow, int targetRow) {
//			Object targetDatasetCell = tableModel.getValueAt(targetRow, COLUMN_DATASET);
//			Object sourceDatasetCell = tableModel.getValueAt(sourceRow, COLUMN_DATASET);
//			Object targetDatasourceCell = tableModel.getValueAt(targetRow, COLUMN_DATASOURCE);
//			Object sourceDatasourceCell = tableModel.getValueAt(sourceRow, COLUMN_DATASOURCE);
//
//			tableModel.setValueAt(targetDatasetCell, sourceRow, COLUMN_DATASET);
//			tableModel.setValueAt(sourceDatasetCell, targetRow, COLUMN_DATASET);
//			tableModel.setValueAt(targetDatasourceCell, sourceRow, COLUMN_DATASOURCE);
//			tableModel.setValueAt(sourceDatasourceCell, targetRow, COLUMN_DATASOURCE);
//		}
//
//		@Override
//		protected Object[] transFormData(Dataset dataset) {
//			Object[] datasetInfo = null;
//			if (dataset.equals(getIllegalDataset())) {
//				Application.getActiveApplication().getOutput().output(MessageFormat.format(ProcessProperties.getString("String_AppendRowError"), dataset.getName()));
//			} else {
//				datasetInfo = new Object[3];
//				datasetInfo[COLUMN_INDEX] = tableModel.getRowCount() + 1+" ";
//				datasetInfo[COLUMN_DATASET] = new DataCell(dataset);
//				DataCell cell = new DataCell();
//				cell.initDatasourceType(dataset.getDatasource());
//				datasetInfo[COLUMN_DATASOURCE] = cell;
//			}
//			return datasetInfo;
//		}
//
//		@Override
//		public void setSupportDatasetTypes(DatasetType[] supportDatasetTypes) {
//			this.supportDatasetTypes = supportDatasetTypes;
//		}
//
//		@Override
//		protected Object[][] getData(ArrayList<Dataset> datasets) {
//			if (null == datasets || (null != datasets && datasets.size() == 0)) {
//				return new Object[0][0];
//			}
//			int size = datasets.size();
//			Object[][] result = new Object[size][];
//			DataCell datasetCell;
//			DataCell datasourceCell;
//			for (int i = 0; i < size; i++) {
//				result[i][COLUMN_INDEX] = i + 1;
//				datasetCell = new DataCell();
//				datasetCell.initDatasetType(datasets.get(i));
//				result[i][COLUMN_DATASET] = datasetCell;
//				datasourceCell = new DataCell();
//				datasourceCell.initDatasourceType(datasets.get(i).getDatasource());
//				result[i][COLUMN_DATASOURCE] = datasourceCell;
//			}
//			return result;
//		}
//	}
}
