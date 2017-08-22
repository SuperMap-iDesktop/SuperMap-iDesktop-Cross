package com.supermap.desktop.process.parameter.ipls;

import com.supermap.data.Dataset;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

/**
 * Created By Chens on 2017/8/21 0021
 */
public class ParameterCommonStatisticCombine extends AbstractParameter implements ISelectionParameter {
	@ParameterField(name = "SourceDatasets")
	private ArrayList<Dataset> datasets;
	@ParameterField(name = "Value")
	private double value;
	public static final String DATASET_FIELD_NAME = "Dataset";
	@ParameterField(name = DATASET_FIELD_NAME)
	private Dataset dataset;
	private boolean isValueChosen =true;

	@Override
	public Object getSelectedItem() {
		return isValueChosen ? value : datasets;
	}

	@Override
	public void setSelectedItem(Object item) {
		if (item != null) {
			if (item instanceof ArrayList) {
				firePropertyChangeListener(new PropertyChangeEvent(ParameterCommonStatisticCombine.this, "SourceDatasets", datasets, item));
				datasets = (ArrayList<Dataset>) item;
			} else {
				firePropertyChangeListener(new PropertyChangeEvent(ParameterCommonStatisticCombine.this, "Value", value, item));
				value = (double) item;
			}
		}
	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public void setDatasets(ArrayList<Dataset> datasets) {
		this.datasets = datasets;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public ArrayList<Dataset> getDatasets() {
		return datasets;
	}

	public double getValue() {
		return value;
	}

	@Override
	public String getType() {
		return ParameterType.COMMON_STATISTIC;
	}

	public boolean isValueChosen() {
		return isValueChosen;
	}

	public void setValueChosen(boolean value) {
		isValueChosen = value;
	}
}
