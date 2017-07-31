package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.spatialStatistics.StatisticsField;

import com.supermap.analyst.spatialstatistics.StatisticsType;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

/**
 * Created by hanyz on 2017/5/5.
 */
public class ParameterStatisticsField extends AbstractParameter {
	public static final String DATASET_FIELD_NAME = "Dataset";
	public static final String STATISTICS_FIELDINFO = "StatisticsFieldInfo";
	@ParameterField(name = DATASET_FIELD_NAME)
	private DatasetVector dataset;
	@ParameterField(name = STATISTICS_FIELDINFO)
	private ArrayList<StatisticsFieldInfo> statisticsFieldInfos;

	public ArrayList<StatisticsFieldInfo> getStatisticsFieldInfos() {
		fireUpdateValue(STATISTICS_FIELDINFO);
		return statisticsFieldInfos;
	}

	public void setStatisticsFieldInfos(ArrayList<StatisticsFieldInfo> statisticsFieldInfos) {
		this.statisticsFieldInfos = statisticsFieldInfos;
	}


	public ParameterStatisticsField() {
		super();
	}

	public StatisticsType[] getStatisticsType() {
		statisticsFieldInfos = getStatisticsFieldInfos();
		ArrayList<StatisticsType> statisticsTypes = new ArrayList<>();
		if (this.statisticsFieldInfos != null) {
			for (StatisticsFieldInfo statisticsFieldInfo : this.statisticsFieldInfos) {
				statisticsTypes.add(statisticsFieldInfo.getStatisticsType());
			}
		}
		return statisticsTypes.toArray(new StatisticsType[statisticsTypes.size()]);
	}

	public String[] getStatisticsFieldNames() {
		statisticsFieldInfos = getStatisticsFieldInfos();
		ArrayList<String> fieldNames = new ArrayList<>();
		if (statisticsFieldInfos != null) {
			for (StatisticsFieldInfo statisticsFieldInfo : statisticsFieldInfos) {
				fieldNames.add(statisticsFieldInfo.getFieldName());
			}
		}
		return fieldNames.toArray(new String[fieldNames.size()]);
	}

	public DatasetVector getDataset() {
		return dataset;
	}

	public void setDataset(DatasetVector dataset) {
		this.dataset = dataset;
		DatasetVector oldValue = this.dataset;
		firePropertyChangeListener(new PropertyChangeEvent(this, DATASET_FIELD_NAME, oldValue, this.dataset));
	}

	@Override
	public String getType() {
		return ParameterType.STATISTICS_FIELD;
	}

	@Override
	public String getDescribe() {
		return null;
	}
}
