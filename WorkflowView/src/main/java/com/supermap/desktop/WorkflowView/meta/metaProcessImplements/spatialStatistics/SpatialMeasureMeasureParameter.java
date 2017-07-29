package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.spatialStatistics;

import com.supermap.analyst.spatialstatistics.DistanceMethod;
import com.supermap.analyst.spatialstatistics.EllipseSize;
import com.supermap.analyst.spatialstatistics.MeasureParameter;
import com.supermap.data.DatasetVector;
import com.supermap.data.FieldType;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.metaProcessImplements.spatialStatistics.StatisticsField.ParameterStatisticsField;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedEvent;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedListener;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.StringUtilities;

import static com.supermap.desktop.WorkflowView.meta.metaProcessImplements.spatialStatistics.ParameterPatternsParameter.DATASET_FIELD_NAME;

/**
 * Created by hanyz on 2017/5/2.
 */
public class SpatialMeasureMeasureParameter extends ParameterCombine {
	private String metaKeys;
	@ParameterField(name = DATASET_FIELD_NAME)
	private DatasetVector currentDataset;

	private ParameterComboBox parameterDistanceMethodComboBox = new ParameterComboBox();//距离计算方法类型。仅对中心要素有效。暂只支持欧式距离。
	private ParameterComboBox parameterEllipseSizeComboBox = new ParameterComboBox();//椭圆大小类型,仅对方向分布有效。
	private ParameterCheckBox parameterIgnoreDirectionCheckBox = new ParameterCheckBox();//是否忽略起点和终点的方向,仅对线性方向平均值有效。
	private ParameterFieldComboBox parameterGroupFieldComboBox = new ParameterFieldComboBox().setShowNullValue(true);//分组字段,可以为数值型、时间型、文本型。
	private ParameterFieldComboBox parameterSelfWeightFieldComboBox = new ParameterFieldComboBox().setShowNullValue(true);//设置自身权重字段的名称。仅数值字段有效。暂仅对中心要素有效。
	private ParameterFieldComboBox parameterWeightFieldComboBox = new ParameterFieldComboBox().setShowNullValue(true);//权重字段的名称
	private ParameterLabel parameterStatisticsTypesLabel = new ParameterLabel();//统计类型的集合
	private ParameterStatisticsField parameterStatisticsTypesUserDefine = new ParameterStatisticsField();//统计类型的集合

	public SpatialMeasureMeasureParameter(String metaKeys) {
		this.metaKeys = metaKeys;
		initParameters();
		initLayout();
		initParameterConstraint();
	}


	protected void initParameters() {
		FieldType[] fieldType = {FieldType.INT16, FieldType.INT32, FieldType.INT64, FieldType.SINGLE, FieldType.DOUBLE};
		parameterGroupFieldComboBox.setFieldType(new FieldType[]{FieldType.INT16, FieldType.INT32, FieldType.INT64, FieldType.SINGLE, FieldType.DOUBLE, FieldType.TEXT, FieldType.DATETIME});
		parameterSelfWeightFieldComboBox.setFieldType(fieldType);
		parameterWeightFieldComboBox.setFieldType(fieldType);

		this.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
		parameterDistanceMethodComboBox.setDescribe(ProcessProperties.getString("String_DistanceMethod"));
		parameterEllipseSizeComboBox.setDescribe(ProcessProperties.getString("String_EllipseSize"));
		parameterIgnoreDirectionCheckBox.setDescribe(ProcessProperties.getString("String_IsIgnoreDirection"));
		//// FIXME: 2017/5/2 字段类型限制；可以为空
		parameterGroupFieldComboBox.setDescribe(ProcessProperties.getString("String_GroupField"));
		parameterSelfWeightFieldComboBox.setDescribe(ProcessProperties.getString("String_SelfWeightField"));
		parameterWeightFieldComboBox.setDescribe(ProcessProperties.getString("String_WeightField"));
		parameterStatisticsTypesLabel.setDescribe(ProcessProperties.getString("String_StatisticsTypes"));

		parameterDistanceMethodComboBox.setItems(new ParameterDataNode(ProcessProperties.getString("String_EUCLIDEAN"), DistanceMethod.EUCLIDEAN));
		parameterEllipseSizeComboBox.setItems(
				new ParameterDataNode(ProcessProperties.getString("String_EllipseSize_SINGLE"), EllipseSize.SINGLE),
				new ParameterDataNode(ProcessProperties.getString("String_EllipseSize_TWICE"), EllipseSize.TWICE),
				new ParameterDataNode(ProcessProperties.getString("String_EllipseSize_TRIPLE"), EllipseSize.TRIPLE));
		parameterStatisticsTypesUserDefine.setEnabled(currentDataset != null);
	}

	private void initLayout() {
		if (metaKeys.equals(MetaKeys.CentralElement)) {
			this.addParameters(parameterDistanceMethodComboBox);
		}
		if (metaKeys.equals(MetaKeys.Directional)) {
			this.addParameters(parameterEllipseSizeComboBox);
		}
		if (metaKeys.equals(MetaKeys.LinearDirectionalMean)) {
			this.addParameters(parameterIgnoreDirectionCheckBox);
		}
		if (metaKeys.equals(MetaKeys.Directional) || metaKeys.equals(MetaKeys.StandardDistance)) {
			this.addParameters(parameterEllipseSizeComboBox);
		}
		if (metaKeys.equals(MetaKeys.CentralElement)) {
			this.addParameters(parameterSelfWeightFieldComboBox);
		}
		this.addParameters(parameterGroupFieldComboBox);
		this.addParameters(parameterWeightFieldComboBox);
		this.addParameters(parameterStatisticsTypesLabel);
		this.addParameters(parameterStatisticsTypesUserDefine);
	}

	private void initParameterConstraint() {
		this.addFieldConstraintChangedListener(new FieldConstraintChangedListener() {
			@Override
			public void fieldConstraintChanged(FieldConstraintChangedEvent event) {
				if (event.getFieldName().equals(DATASET_FIELD_NAME)) {
					datasetChanged();
				}
			}
		});
	}

	public void setCurrentDataset(DatasetVector currentDataset) {
		this.currentDataset = currentDataset;
		datasetChanged();
	}

	private void datasetChanged() {
		parameterGroupFieldComboBox.setDataset(currentDataset);
		parameterSelfWeightFieldComboBox.setDataset(currentDataset);
		parameterWeightFieldComboBox.setDataset(currentDataset);
		parameterStatisticsTypesUserDefine.setDataset(currentDataset);
		parameterStatisticsTypesUserDefine.setEnabled(currentDataset != null);
	}

	public MeasureParameter getMeasureParameter() {
		MeasureParameter measureParameter = new MeasureParameter();
		if (metaKeys.equals(MetaKeys.CentralElement)) {
			measureParameter.setDistanceMethod((DistanceMethod) parameterDistanceMethodComboBox.getSelectedData());
		}
		if (metaKeys.equals(MetaKeys.Directional) || metaKeys.equals(MetaKeys.StandardDistance)) {
			measureParameter.setEllipseSize((EllipseSize) parameterEllipseSizeComboBox.getSelectedData());
		}
		if (metaKeys.equals(MetaKeys.LinearDirectionalMean)) {
			boolean isIgnoreDirection = "true".equalsIgnoreCase((String) parameterIgnoreDirectionCheckBox.getSelectedItem());
			measureParameter.setOrientation(isIgnoreDirection);
		}
		String groupFieldName = parameterGroupFieldComboBox.getFieldName();
		if (!StringUtilities.isNullOrEmpty(groupFieldName)) {
			measureParameter.setGroupFieldName(groupFieldName);
		}
		String selfWeightFieldName = parameterSelfWeightFieldComboBox.getFieldName();
		if (!StringUtilities.isNullOrEmpty(selfWeightFieldName)) {
			measureParameter.setSelfWeightFieldName(selfWeightFieldName);
		}
		String weightFieldName = parameterWeightFieldComboBox.getFieldName();
		if (!StringUtilities.isNullOrEmpty(weightFieldName)) {
			measureParameter.setWeightFieldName(weightFieldName);
		}
		measureParameter.setStatisticsFieldNames(parameterStatisticsTypesUserDefine.getStatisticsFieldNames());
		measureParameter.setStatisticsTypes(parameterStatisticsTypesUserDefine.getStatisticsType());
		return measureParameter;
	}
}
