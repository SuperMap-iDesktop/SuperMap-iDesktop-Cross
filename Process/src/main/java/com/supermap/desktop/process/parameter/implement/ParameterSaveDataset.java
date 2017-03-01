package com.supermap.desktop.process.parameter.implement;

import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.ParameterPanels.ParameterSaveDatasetPanel;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class ParameterSaveDataset extends AbstractParameter implements ISelectionParameter {
	private JPanel panel;
	private Datasource resultDatasource;
	private String datasetName;

	@Override
	public String getType() {
		return ParameterType.SAVE_DATASET;
	}

	@Override
	public JPanel getPanel() {
		if (panel == null) {
			panel = new ParameterSaveDatasetPanel(this);
		}
		return panel;
	}

	@Override
	public void setSelectedItem(Object value) {

	}

	@Override
	public Object getSelectedItem() {
		return datasetName;
	}

	@Override
	public void dispose() {

	}

	public Datasource getResultDatasource() {
		return resultDatasource;
	}

	public void setResultDatasource(Datasource resultDatasource) {
		this.resultDatasource = resultDatasource;
	}

	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}
}
