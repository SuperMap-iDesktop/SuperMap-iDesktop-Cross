package com.supermap.desktop.process.parameter.ipls;

import com.supermap.analyst.spatialanalyst.StatisticsField;
import com.supermap.analyst.spatialanalyst.StatisticsFieldType;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.IMultiSelectionParameter;

import java.beans.PropertyChangeEvent;

/**
 * Created by lixiaoyao on 2017/8/15.
 */
public class ParameterStatisticsFieldGroupForRarefyPoints extends AbstractParameter implements IMultiSelectionParameter {
	public static final String FIELD_DATASET="dataset";
	public static final String STATISTICS_FIELD_TYPE="StatisticsFieldType";

	@ParameterField(name = FIELD_DATASET)
	private DatasetVector dataset;
	private String describe;
	private StatisticsField[] selectedStatisticsFields;
	private StatisticsFieldType statisticsFieldType=StatisticsFieldType.AVERAGE;

	public ParameterStatisticsFieldGroupForRarefyPoints(){

	}
	public ParameterStatisticsFieldGroupForRarefyPoints(String describe){
		this.describe=describe;
	}

	@Override
	public void setSelectedItem(Object item) {

	}

	@Override
	public Object getSelectedItem() {
		throw new UnsupportedOperationException("See GetSelectedFieldInfos");
	}

	@Override
	public String getType() {
		return ParameterType.STATISTICS_FIELD_FOR_RAREFY_POINTS;
	}

	@Override
	public String getDescribe() {
		return describe;
	}

	public DatasetVector getDataset() {
		return dataset;
	}

	public void setDataset(DatasetVector dataset) {
		DatasetVector oldValue = this.dataset;
		this.dataset = dataset;
		firePropertyChangeListener(new PropertyChangeEvent(this, FIELD_DATASET, oldValue, dataset));
	}

	public StatisticsField[] getSelectedStatisticsFields() {
		return selectedStatisticsFields;
	}

	public void setSelectedStatisticsFields(StatisticsField[] statisticsFields) {
		 this.selectedStatisticsFields=statisticsFields;
	}


	public StatisticsFieldType getStatisticsFieldType() {
		return this.statisticsFieldType;
	}

	public void setStatisticsFieldType(StatisticsFieldType statisticsFieldType) {
		StatisticsFieldType oldValue=this.statisticsFieldType;
		this.statisticsFieldType = statisticsFieldType;
		firePropertyChangeListener(new PropertyChangeEvent(this, STATISTICS_FIELD_TYPE, oldValue, statisticsFieldType));
	}


}
