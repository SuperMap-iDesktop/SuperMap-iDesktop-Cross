package com.supermap.desktop.process.parameters.ParameterPanels.StatisticsField;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedEvent;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedListener;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.parameter.ipls.ParameterDatasetChooseTable;
import com.supermap.desktop.process.parameters.ParameterPanels.SwingPanel;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.CollectionDataset.JPanelDatasetChoose;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by xie on 2017/8/9.
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.DATASET_CHOOSE_TABLE)
public class ParameterDatasetChooseTablePanel extends SwingPanel {
	private ParameterDatasetChooseTable datasetChooseTable;
	private JPanelDatasetChooseForParameter datasetChoosePanel;
	private final String[] columnNames = {"", CommonProperties.getString("String_ColumnHeader_Dataset"), CommonProperties.getString("String_ColumnHeader_Datasource")};
	private final boolean[] enables = {false, false, false};

	public ParameterDatasetChooseTablePanel(IParameter parameter) {
		super(parameter);
		this.datasetChooseTable = (ParameterDatasetChooseTable) parameter;
		init();
	}

	private void init() {
		initComponents();
		initLayout();
		registEvents();
	}

	private void registEvents() {
		datasetChooseTable.addFieldConstraintChangedListener(new FieldConstraintChangedListener() {
			@Override
			public void fieldConstraintChanged(FieldConstraintChangedEvent event) {
				Dataset dataset = ((ParameterDatasetChooseTable) event.getParameter()).getDataset();
				if (null != dataset) {
					datasetChoosePanel.setIllegalDataset(dataset);
					datasetChoosePanel.setSupportDatasetTypes(new DatasetType[]{dataset.getType()});
				}
			}
		});
		this.datasetChoosePanel.getTableModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				datasetChooseTable.setSelectedItem(datasetChoosePanel.getDatasets());
			}
		});
	}

	private void initLayout() {
		panel.setLayout(new GridBagLayout());
		panel.add(this.datasetChoosePanel, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		panel.setBorder(new TitledBorder(CommonProperties.getString("String_ColumnHeader_SourceData")));
	}

	private void initComponents() {
		this.datasetChoosePanel = new JPanelDatasetChooseForParameter((ArrayList<Dataset>) datasetChooseTable.getSelectedItem(), columnNames, enables);
		this.datasetChoosePanel.setIllegalDataset(this.datasetChooseTable.getDataset());
		if (null != this.datasetChooseTable.getDataset()) {
			this.datasetChoosePanel.setSupportDatasetTypes(new DatasetType[]{this.datasetChooseTable.getDataset().getType()});
		}
	}


	//内部类,不作为外部使用
	class JPanelDatasetChooseForParameter extends JPanelDatasetChoose {
		private final int COLUMN_INDEX = 0;
		private final int COLUMN_DATASET = 1;
		private final int COLUMN_DATASOURCE = 2;
		private final int MAX_SIZE = 40;

		public JPanelDatasetChooseForParameter(ArrayList<Dataset> datasets, String[] columnName, boolean[] enableColumn) {
			super(datasets, columnName, enableColumn);
			this.tableDatasetDisplay.getColumnModel().getColumn(COLUMN_INDEX).setMaxWidth(MAX_SIZE);
		}

		@Override
		protected void exchangeItem(int sourceRow, int targetRow) {
			Object targetDatasetCell = tableModel.getValueAt(targetRow, COLUMN_DATASET);
			Object sourceDatasetCell = tableModel.getValueAt(sourceRow, COLUMN_DATASET);
			Object targetDatasourceCell = tableModel.getValueAt(targetRow, COLUMN_DATASOURCE);
			Object sourceDatasourceCell = tableModel.getValueAt(sourceRow, COLUMN_DATASOURCE);

			tableModel.setValueAt(targetDatasetCell, sourceRow, COLUMN_DATASET);
			tableModel.setValueAt(sourceDatasetCell, targetRow, COLUMN_DATASET);
			tableModel.setValueAt(targetDatasourceCell, sourceRow, COLUMN_DATASOURCE);
			tableModel.setValueAt(sourceDatasourceCell, targetRow, COLUMN_DATASOURCE);
		}

		@Override
		protected Object[] transFormData(Dataset dataset) {
			Object[] datasetInfo = null;
			if (dataset.equals(getIllegalDataset())) {
				Application.getActiveApplication().getOutput().output(MessageFormat.format(ProcessProperties.getString("String_AppendRowError"), dataset.getName()));
			} else {
				datasetInfo = new Object[3];
				datasetInfo[COLUMN_INDEX] = tableModel.getRowCount() + 1;
				datasetInfo[COLUMN_DATASET] = new DataCell(dataset);
				DataCell cell = new DataCell();
				cell.initDatasourceType(dataset.getDatasource());
				datasetInfo[COLUMN_DATASOURCE] = cell;
			}
			return datasetInfo;
		}

		@Override
		public void setSupportDatasetTypes(DatasetType[] supportDatasetTypes) {
			this.supportDatasetTypes = supportDatasetTypes;
		}

		@Override
		protected Object[][] getData(ArrayList<Dataset> datasets) {
			if (null == datasets || (null != datasets && datasets.size() == 0)) {
				return new Object[0][0];
			}
			int size = datasets.size();
			Object[][] result = new Object[size][];
			DataCell datasetCell;
			DataCell datasourceCell;
			for (int i = 0; i < size; i++) {
				result[i][COLUMN_INDEX] = i + 1;
				datasetCell = new DataCell();
				datasetCell.initDatasetType(datasets.get(i));
				result[i][COLUMN_DATASET] = datasetCell;
				datasourceCell = new DataCell();
				datasourceCell.initDatasourceType(datasets.get(i).getDatasource());
				result[i][COLUMN_DATASOURCE] = datasourceCell;
			}
			return result;
		}
	}
}
