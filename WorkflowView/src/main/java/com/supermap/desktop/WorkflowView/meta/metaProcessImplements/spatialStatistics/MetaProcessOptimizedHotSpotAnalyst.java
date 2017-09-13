package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.spatialStatistics;

import com.supermap.analyst.spatialstatistics.AggregationMethod;
import com.supermap.analyst.spatialstatistics.ClusteringDistributions;
import com.supermap.analyst.spatialstatistics.OptimizedParameter;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.FieldType;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.DatasourceConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasetConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author XiaJT
 * 控制优化
 */
public class MetaProcessOptimizedHotSpotAnalyst extends MetaProcess {
	private static final String INPUT_SOURCE_DATASET = CommonProperties.getString("String_GroupBox_SourceData");
	private final static String OUTPUT_DATASET = "OptimizedHotSpotResult";
	private ParameterDatasource parameterDatasource = new ParameterDatasource();
	private ParameterSingleDataset parameterSingleDataset = new ParameterSingleDataset(DatasetType.REGION, DatasetType.LINE, DatasetType.POINT);

	private ParameterFieldComboBox parameterFieldComboBox = new ParameterFieldComboBox().setShowNullValue(true);

	private ParameterComboBox parameterComboBox = new ParameterComboBox();

	private ParameterDatasource parameterDatasourceBounding = new ParameterDatasource();
	// 事件点发生区域的边界面数据集，必须为面数据集
	private ParameterSingleDataset parameterSingleDatasetBounding = new ParameterSingleDataset(DatasetType.REGION);

	private ParameterDatasource parameterDatasourceAggregating = new ParameterDatasource();
	// 聚合事件点以获得事件计数的面数据集，必须为面数据集。
	private ParameterSingleDataset parameterSingleDatasetAggregating = new ParameterSingleDataset(DatasetType.REGION);

	private ParameterSaveDataset parameterSaveDataset = new ParameterSaveDataset();

	private PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			reload();
		}
	};

	public MetaProcessOptimizedHotSpotAnalyst() {
		initParameters();
		initParameterConstraint();
		initParameterState();
		initListener();
		reload();
	}

	private void initParameters() {
		parameterComboBox.addItem(new ParameterDataNode("AggregationPolygons", AggregationMethod.AGGREGATIONPOLYGONS));
		parameterComboBox.addItem(new ParameterDataNode("NetworkPolygons", AggregationMethod.NETWORKPOLYGONS));
		parameterComboBox.addItem(new ParameterDataNode("SnapNearByPoints", AggregationMethod.SNAPNEARBYPOINTS));
		parameterFieldComboBox.setDescribe(ProcessProperties.getString("String_AssessmentField"));

		parameterComboBox.setDescribe(ProcessProperties.getString("String_AggregationMethod"));


		ParameterCombine parameterCombineSource = new ParameterCombine();
		parameterCombineSource.addParameters(parameterDatasource);
		parameterCombineSource.addParameters(parameterSingleDataset);
		parameterCombineSource.setDescribe(CommonProperties.getString("String_ColumnHeader_SourceData"));

		ParameterCombine parameterCombine = new ParameterCombine();
		parameterCombine.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
		parameterCombine.addParameters(parameterFieldComboBox);
		parameterCombine.addParameters(parameterComboBox);
		parameterCombine.addParameters(parameterDatasourceBounding);
		parameterCombine.addParameters(parameterSingleDatasetBounding);
		parameterCombine.addParameters(parameterDatasourceAggregating);
		parameterCombine.addParameters(parameterSingleDatasetAggregating);

		// 设置数据源和数据集的所属名称
		parameterDatasourceBounding.setDescribe(ProcessProperties.getString("String_BoundingPolygons_Datasource"));
		parameterSingleDatasetBounding.setDescribe(ProcessProperties.getString("String_BoundingPolygons_Dataset"));
		parameterDatasourceAggregating.setDescribe(ProcessProperties.getString("String_AggregatingPolygons_Datasource"));
		parameterSingleDatasetAggregating.setDescribe(ProcessProperties.getString("String_AggregatingPolygons_Dataset"));

		ParameterCombine parameterCombineResult = new ParameterCombine();
		parameterCombineResult.addParameters(parameterSaveDataset);
		parameterCombineResult.setDescribe(CommonProperties.getString("String_ResultSet"));

		parameters.setParameters(parameterCombineSource, parameterCombine, parameterCombineResult);
		parameters.addInputParameters(INPUT_SOURCE_DATASET, DatasetTypes.VECTOR, parameterCombineSource);
		parameters.addOutputParameters(OUTPUT_DATASET, ProcessOutputResultProperties.getString("String_OptimizedHotSpotAnalystResult"), DatasetTypes.VECTOR, parameterCombineResult);
	}

	private void initParameterState() {
		parameterSaveDataset.setDefaultDatasetName("result_optimizedHotSpot");
		DatasetVector defaultDatasetVector = DatasetUtilities.getDefaultDatasetVector();
		if (defaultDatasetVector != null) {
			parameterDatasource.setSelectedItem(defaultDatasetVector.getDatasource());
			parameterDatasourceBounding.setSelectedItem(defaultDatasetVector.getDatasource());
			parameterDatasourceAggregating.setSelectedItem(defaultDatasetVector.getDatasource());

			parameterSingleDataset.setSelectedItem(defaultDatasetVector);
			parameterSingleDatasetBounding.setSelectedItem(defaultDatasetVector);
			parameterSingleDatasetAggregating.setSelectedItem(defaultDatasetVector);
			parameterFieldComboBox.setDataset(defaultDatasetVector);
			parameterFieldComboBox.setFieldName(defaultDatasetVector);
			// 评估字段名称，仅数值有效
			parameterFieldComboBox.setFieldType(new FieldType[]{FieldType.INT16, FieldType.INT32, FieldType.INT64, FieldType.SINGLE, FieldType.DOUBLE});
		}
		parameterComboBox.setSelectedItem(AggregationMethod.AGGREGATIONPOLYGONS);
	}

	private void initListener() {
		parameterComboBox.addPropertyListener(propertyChangeListener);
		parameterFieldComboBox.addPropertyListener(propertyChangeListener);
	}

	/**
	 * 根据评估字段和评估类型设置可用与否
	 * yuanR2017.9.12
	 */
	private void reload() {
		parameterDatasourceBounding.setEnabled(parameterComboBox.getSelectedData().equals(AggregationMethod.NETWORKPOLYGONS) && StringUtilities.isNullOrEmptyString(parameterFieldComboBox.getSelectedItem()));
		parameterSingleDatasetBounding.setEnabled(parameterComboBox.getSelectedData().equals(AggregationMethod.NETWORKPOLYGONS) && StringUtilities.isNullOrEmptyString(parameterFieldComboBox.getSelectedItem()));
		parameterDatasourceAggregating.setEnabled(parameterComboBox.getSelectedData().equals(AggregationMethod.AGGREGATIONPOLYGONS) && StringUtilities.isNullOrEmptyString(parameterFieldComboBox.getSelectedItem()));
		parameterSingleDatasetAggregating.setEnabled(parameterComboBox.getSelectedData().equals(AggregationMethod.AGGREGATIONPOLYGONS) && StringUtilities.isNullOrEmptyString(parameterFieldComboBox.getSelectedItem()));
	}

	private void initParameterConstraint() {
		DatasourceConstraint.getInstance().constrained(parameterDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(parameterSaveDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(parameterDatasourceBounding, ParameterDatasource.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(parameterDatasourceAggregating, ParameterDatasource.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(parameterDatasourceBounding, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(parameterSingleDatasetBounding, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint equalDatasourceConstraint1 = new EqualDatasourceConstraint();
		equalDatasourceConstraint1.constrained(parameterDatasourceAggregating, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint1.constrained(parameterSingleDatasetAggregating, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint equalDatasourceConstraint2 = new EqualDatasourceConstraint();
		equalDatasourceConstraint2.constrained(parameterDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint2.constrained(parameterSingleDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasetConstraint equalDatasetConstraint = new EqualDatasetConstraint();
		equalDatasetConstraint.constrained(parameterSingleDataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		equalDatasetConstraint.constrained(parameterFieldComboBox, ParameterFieldComboBox.DATASET_FIELD_NAME);


	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_optimizedHotSpotAnalyst");
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		DatasetVector datasetVector;
		if (parameters.getInputs().getData(INPUT_SOURCE_DATASET) != null &&
				parameters.getInputs().getData(INPUT_SOURCE_DATASET).getValue() instanceof DatasetVector) {
			datasetVector = (DatasetVector) parameters.getInputs().getData(INPUT_SOURCE_DATASET).getValue();
		} else {
			datasetVector = (DatasetVector) parameterSingleDataset.getSelectedItem();
		}
		OptimizedParameter optimizedParameter = new OptimizedParameter();
		optimizedParameter.setAggregationMethod(AggregationMethod.SNAPNEARBYPOINTS);
		if (parameterSingleDatasetAggregating.isEnabled()) {
			optimizedParameter.setAggregatingPolygons((DatasetVector) parameterSingleDatasetAggregating.getSelectedItem());
			optimizedParameter.setAggregationMethod(AggregationMethod.AGGREGATIONPOLYGONS);
		}
		if (parameterSingleDatasetBounding.isEnabled()) {
			optimizedParameter.setBoundingPolygons((DatasetVector) parameterSingleDatasetBounding.getSelectedItem());
			optimizedParameter.setAggregationMethod(AggregationMethod.NETWORKPOLYGONS);
		}
		optimizedParameter.setAssessmentFieldName(parameterFieldComboBox.getFieldName());

		try {

			ClusteringDistributions.addSteppedListener(steppedListener);
			DatasetVector result = ClusteringDistributions.optimizedHotSpotAnalyst(datasetVector, parameterSaveDataset.getResultDatasource(),
					parameterSaveDataset.getResultDatasource().getDatasets().getAvailableDatasetName(parameterSaveDataset.getDatasetName()), optimizedParameter);
			this.getParameters().getOutputs().getData(OUTPUT_DATASET).setValue(result);
			isSuccessful = result != null;
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
		} finally {
			ClusteringDistributions.removeSteppedListener(steppedListener);
		}
		return isSuccessful;
	}

	@Override
	public String getKey() {
		return MetaKeys.OPTIMIZED_HOT_SPOT_ANALYST;
	}
}
