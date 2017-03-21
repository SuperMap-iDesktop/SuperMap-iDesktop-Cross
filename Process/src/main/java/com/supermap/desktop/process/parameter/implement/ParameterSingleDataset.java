package com.supermap.desktop.process.parameter.implement;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.ParameterPanels.ParameterSingleDatasetPanel;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.beans.PropertyChangeEvent;

/**
 * Created by xie on 2017/2/16.
 */
public class ParameterSingleDataset extends AbstractParameter implements ISelectionParameter {
    private Dataset selectedItem;
    private DatasetType[] datasetTypes;
    public ParameterSingleDataset(DatasetType ...datasetTypes){
        this.datasetTypes = datasetTypes;
    }
    @Override
    public void setSelectedItem(Object item) {
        if (item instanceof Dataset) {
            Dataset oldValue = this.selectedItem;
            this.selectedItem = (Dataset) item;
            firePropertyChangeListener(new PropertyChangeEvent(this, AbstractParameter.PROPERTY_VALE, oldValue, selectedItem));
        }
    }

    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }

    @Override
    public String getType() {
	    return ParameterType.SINGLE_DATASET;
    }

	@Override
	public IParameterPanel getParameterPanel() {
		IParameterPanel parameterPanel = super.getParameterPanel();
		((ParameterSingleDatasetPanel) parameterPanel).setDatasetTypes(datasetTypes);
		return parameterPanel;
	}

	@Override
	public void dispose() {

    }

    @Override
    public String getDescribe() {
        return null;
    }
}
