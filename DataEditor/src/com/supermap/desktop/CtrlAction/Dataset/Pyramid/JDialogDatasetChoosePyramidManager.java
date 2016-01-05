package com.supermap.desktop.CtrlAction.Dataset.Pyramid;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.DatasetChooser;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by SillyB on 2016/1/2.
 */
public class JDialogDatasetChoosePyramidManager extends DatasetChooser {

	private List<Dataset> selectedDatasets;

	public JDialogDatasetChoosePyramidManager(JDialog owner, boolean flag, Datasource datasource, String[] datasetTypes) {
		super(owner, flag, datasource, datasetTypes);
		this.setLocationRelativeTo(null);
		this.setTitle(CoreProperties.getString("String_FormDatasetBrowse_FormText"));
		this.buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setSelectedDatasets();
				dialogResult = DialogResult.OK;
				dispose();
			}
		});
	}

	private void setSelectedDatasets() {
		selectedDatasets = new ArrayList<>();
		MutiTableModel model = (MutiTableModel) table.getModel();
		int[] selectCount = table.getSelectedRows();
		for (int i = 0; i < selectCount.length; i++) {
			Vector<Object> tempVector = model.getTagValue(selectCount[i]);
			String datasetName = tempVector.get(COLUMN_INDEX_DATASET).toString();
			String datasourceName = tempVector.get(COLUMN_INDEX_CURRENT_DATASOURCE).toString();
			Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceName);
			Dataset dataset = CommonToolkit.DatasetWrap.getDatasetFromDatasource(datasetName, datasource);
			selectedDatasets.add(dataset);
		}
	}

	public List<Dataset> getSelectedDatasets() {
		return selectedDatasets;
	}

	@Override
	public DialogResult showDialog() {
		this.selectedDatasets = null;
		this.setDialogResult(DialogResult.CLOSED);
		return super.showDialog();
	}

	@Override
	public void dispose() {
		this.workspaceTree.dispose();
		super.dispose();
	}
}
