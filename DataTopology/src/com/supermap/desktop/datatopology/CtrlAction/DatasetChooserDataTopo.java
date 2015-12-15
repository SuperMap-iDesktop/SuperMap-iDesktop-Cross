package com.supermap.desktop.datatopology.CtrlAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;

import javax.swing.JDialog;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.datatopology.DataTopologyProperties;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DatasetChooser;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTable;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTableModel;

public class DatasetChooserDataTopo extends DatasetChooser {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MutiTable preprocessTable;
	private JDialogTopoPreProgress topoPreProgress;

	public DatasetChooserDataTopo(JDialog owner, boolean flag, Datasource datasource, MutiTable preprocessTable, String[] datasetType) {
		super(owner, flag, datasource, datasetType);
		setTitle(DataTopologyProperties.getString("String_Text_Preprogress"));
		getDataset();
		this.preprocessTable = preprocessTable;
		this.topoPreProgress = (JDialogTopoPreProgress) owner;
	}

	private void getDataset() {
		buttonOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addInfoToMainTable();
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (2 == e.getClickCount()) {
					addInfoToMainTable();
				}
			}
		});
		table.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					addInfoToMainTable();
				}
			}
		});
	}

	private void addInfoToMainTable() {
		try {
			int[] selectIndex = table.getSelectedRows();
			MutiTableModel model = (MutiTableModel) table.getModel();
			int rowCount = preprocessTable.getRowCount();
			HashSet<DatasetType> datasetTypes = new HashSet<DatasetType>();
			for (int i = 0; i < selectIndex.length; i++) {
				Object[] temp = new Object[3];
				temp[COLUMN_INDEX_DATASET] = preprocessTable.getRowCount() + 1;
				DataCell datasetCell = (DataCell) model.getTagValue(selectIndex[i]).get(0);
				topoPreProgress.getComboBoxConsultDataset().addItem(datasetCell);
				temp[COLUMN_INDEX_CURRENT_DATASOURCE] = datasetCell;
				String datasourceName = (String) model.getTagValue(selectIndex[i]).get(1);
				Datasource dataSource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceName);
				String path = CommonToolkit.DatasourceImageWrap.getImageIconPath(dataSource.getEngineType());
				Dataset dataset = dataSource.getDatasets().get(datasetCell.toString());
				datasetTypes.add(dataset.getType());
				DataCell cell = new DataCell(path, datasourceName);
				temp[COLUMN_INDEX_DATASET_TYPE] = cell;
				// 设置容限值为最后一个矢量数据集的结点容限
				topoPreProgress.getTextFieldTolerance().setText(((DatasetVector) dataset).getTolerance().getNodeSnap() + "");
				preprocessTable.addRow(temp);
			}
			if (rowCount < preprocessTable.getRowCount()) {
				if (datasetTypes.contains(DatasetType.REGION)) {
					topoPreProgress.getCheckBoxArcsInserted().setEnabled(true);
					topoPreProgress.getCheckBoxPolygonsChecked().setEnabled(true);
					topoPreProgress.getCheckBoxVertexArcInserted().setEnabled(true);
					topoPreProgress.getCheckBoxVertexesSnapped().setEnabled(true);
				} else if (datasetTypes.contains(DatasetType.LINE)) {
					topoPreProgress.getCheckBoxArcsInserted().setEnabled(true);
					topoPreProgress.getCheckBoxVertexArcInserted().setEnabled(true);
					topoPreProgress.getCheckBoxVertexesSnapped().setEnabled(true);
				} else if (datasetTypes.contains(DatasetType.POINT)) {
					topoPreProgress.getCheckBoxVertexesSnapped().setEnabled(true);
				}
				preprocessTable.setRowSelectionInterval(rowCount, preprocessTable.getRowCount() - 1);
			}
			dispose();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}
}
