package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.spatialStatistics;

import com.supermap.analyst.spatialstatistics.AggregationMethod;
import com.supermap.analyst.spatialstatistics.ClusteringDistributions;
import com.supermap.analyst.spatialstatistics.OptimizedParameter;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author XiaJT
 * 重构界面yuanR2017.9.15
 * 1、根据需选择的数据集类型，对界面进行选择：当是线和面数据集时，所需控件只要字段选择界面
 * 2、当聚合方式虚选择网络时，范围数据集可以为空
 * 3、根据数据集类型重写执行
 */
public class MetaProcessOptimizedHotSpotAnalyst extends MetaProcess {
	private static final String INPUT_SOURCE_DATASET = CommonProperties.getString("String_GroupBox_SourceData");
	private final static String OUTPUT_DATASET = "OptimizedHotSpotResult";
	private ParameterDatasource parameterDatasource;
	private ParameterSingleDataset parameterSingleDataset;
	private ParameterSaveDataset parameterSaveDataset;

	// 线面数据集所需要的面板:
	private ParameterFieldComboBox parameterFieldComboBoxNotPoint;
	// 点数据需要的面板:
	private ParameterComboBox parameterComboBox;
	private ParameterDatasource parameterDatasourceAggregating;
	private ParameterSingleDataset parameterSingleDatasetAggregating;
	private
	ParameterDatasource parameterDatasourceBounding;
	// 网络聚合方式范围数据集可以为空
	private ParameterSingleDataset parameterSingleDatasetBounding;

	private ParameterSwitch parameterSwitchDatasetType = new ParameterSwitch();
	private ParameterSwitch parameterSwitchAggregationMethod = new ParameterSwitch();

	/**
	 * 根据数据类型显示不同的面板
	 * 根据不同的聚合方式选择不同的数据集类型面板
	 */
	private PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (null != parameterSingleDataset.getSelectedDataset()) {
				if (parameterSingleDataset.getSelectedDataset().getType().equals(DatasetType.POINT)) {
					parameterSwitchDatasetType.switchParameter("PointType");
				} else {
					parameterSwitchDatasetType.switchParameter("NotPointType");
				}
			}
			if (parameterComboBox.getSelectedData().equals(AggregationMethod.AGGREGATIONPOLYGONS)) {
				parameterSwitchAggregationMethod.switchParameter("Aggregating");
			} else {
				parameterSwitchAggregationMethod.switchParameter("Bounding");
			}
		}
	};

	public MetaProcessOptimizedHotSpotAnalyst() {
		initParameters();
		initComponentLayout();
		initParameterState();
		initListener();
		initParameterConstraint();
	}

	private void initParameters() {
		parameterDatasource = new ParameterDatasource();
		parameterSingleDataset = new ParameterSingleDataset(DatasetType.REGION, DatasetType.LINE, DatasetType.POINT);
		parameterSaveDataset = new ParameterSaveDataset();

		parameterFieldComboBoxNotPoint = new ParameterFieldComboBox(ProcessProperties.getString("String_AssessmentField"));
		parameterFieldComboBoxNotPoint.setFieldType(fieldType);

		parameterComboBox = new ParameterComboBox(ProcessProperties.getString("String_AggregationMethod"));
		parameterComboBox.addItem(new ParameterDataNode(ProcessProperties.getString("String_AGGREGATION"), AggregationMethod.AGGREGATIONPOLYGONS));
		parameterComboBox.addItem(new ParameterDataNode(ProcessProperties.getString("String_NETWORK"), AggregationMethod.NETWORKPOLYGONS));

		parameterDatasourceAggregating = new ParameterDatasource();
		parameterSingleDatasetAggregating = new ParameterSingleDataset(DatasetType.REGION);
		parameterDatasourceBounding = new ParameterDatasource();
		parameterSingleDatasetBounding = new ParameterSingleDataset(DatasetType.REGION).setShowNullValue(true);
		parameterDatasourceBounding.setDescribe(ProcessProperties.getString("String_BoundingPolygons_Datasource"));
		parameterSingleDatasetBounding.setDescribe(ProcessProperties.getString("String_BoundingPolygons_Dataset"));
		parameterDatasourceAggregating.setDescribe(ProcessProperties.getString("String_AggregatingPolygons_Datasource"));
		parameterSingleDatasetAggregating.setDescribe(ProcessProperties.getString("String_AggregatingPolygons_Dataset"));


	}

	private void initComponentLayout() {

		// 聚合
		ParameterCombine parameterCombineAggregating = new ParameterCombine();
		parameterCombineAggregating.addParameters(parameterDatasourceAggregating);
		parameterCombineAggregating.addParameters(parameterSingleDatasetAggregating);
		// 网格
		ParameterCombine parameterCombineBounding = new ParameterCombine();
		parameterCombineBounding.addParameters(parameterDatasourceBounding);
		parameterCombineBounding.addParameters(parameterSingleDatasetBounding);
		// 聚合方式选择面板
		parameterSwitchAggregationMethod.add("Aggregating", parameterCombineAggregating);
		parameterSwitchAggregationMethod.add("Bounding", parameterCombineBounding);

		// 点数据集参数面板
		ParameterCombine parameterCombinePoint = new ParameterCombine();
		parameterCombinePoint.addParameters(parameterComboBox);
		parameterCombinePoint.addParameters(parameterSwitchAggregationMethod);

		parameterSwitchDatasetType.add("NotPointType", parameterFieldComboBoxNotPoint);
		parameterSwitchDatasetType.add("PointType", parameterCombinePoint);

		// 源数据集
		ParameterCombine parameterCombineSource = new ParameterCombine();
		parameterCombineSource.addParameters(parameterDatasource);
		parameterCombineSource.addParameters(parameterSingleDataset);
		parameterCombineSource.setDescribe(CommonProperties.getString("String_ColumnHeader_SourceData"));
		// 参数面板
		ParameterCombine parameterCombineSet = new ParameterCombine();
		parameterCombineSet.addParameters(parameterSwitchDatasetType);
		parameterCombineSet.setDescribe(ProcessProperties.getString("String_setParameter"));
		// 结果
		ParameterCombine parameterCombineResult = new ParameterCombine();
		parameterCombineResult.addParameters(parameterSaveDataset);
		parameterCombineResult.setDescribe(CommonProperties.getString("String_ResultSet"));

		parameters.setParameters(parameterCombineSource, parameterCombineSet, parameterCombineResult);
		parameters.addInputParameters(INPUT_SOURCE_DATASET, DatasetTypes.VECTOR, parameterCombineSource);
		parameters.addOutputParameters(OUTPUT_DATASET, ProcessOutputResultProperties.getString("String_OptimizedHotSpotAnalystResult"), DatasetTypes.VECTOR, parameterCombineResult);
	}

	private void initParameterState() {
		parameterSaveDataset.setDefaultDatasetName("result_optimizedHotSpot");
		DatasetVector defaultDatasetVector = DatasetUtilities.getDefaultDatasetVector();
		Dataset defaultDatasetRegion = DatasetUtilities.getDefaultDataset(DatasetType.REGION);
		if (defaultDatasetRegion != null) {
			parameterDatasourceAggregating.setSelectedItem(defaultDatasetRegion.getDatasource());
			parameterSingleDatasetAggregating.setSelectedItem(defaultDatasetRegion);
		}

		if (defaultDatasetVector != null) {
			parameterDatasource.setSelectedItem(defaultDatasetVector.getDatasource());
			parameterSingleDataset.setSelectedItem(defaultDatasetVector);
			parameterFieldComboBoxNotPoint.setFieldName(defaultDatasetVector);
			parameterDatasourceBounding.setSelectedItem(defaultDatasetVector.getDatasource());
			parameterSingleDatasetBounding.setSelectedItem(defaultDatasetVector);

			if (defaultDatasetVector.getType().equals(DatasetType.POINT)) {
				parameterSwitchDatasetType.switchParameter("PointType");
			} else {
				parameterSwitchDatasetType.switchParameter("NotPointType");
			}
		}
		parameterComboBox.setSelectedItem(AggregationMethod.AGGREGATIONPOLYGONS);
		parameterSwitchAggregationMethod.switchParameter("Aggregating");

	}

	private void initListener() {
		parameterSingleDataset.addPropertyListener(propertyChangeListener);
		parameterComboBox.addPropertyListener(propertyChangeListener);
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
		equalDatasetConstraint.constrained(parameterFieldComboBoxNotPoint, ParameterFieldComboBox.DATASET_FIELD_NAME);
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
		// 根据数据集类型进行不同参数设置
		OptimizedParameter optimizedParameter = new OptimizedParameter();
		if (datasetVector.getType().equals(DatasetType.POINT)) {
			// 点类型
			optimizedParameter.setAggregationMethod((AggregationMethod) parameterComboBox.getSelectedData());
			if (optimizedParameter.getAggregationMethod().equals(AggregationMethod.AGGREGATIONPOLYGONS)) {
				optimizedParameter.setAggregatingPolygons((DatasetVector) parameterSingleDatasetAggregating.getSelectedItem());
			} else {
				optimizedParameter.setBoundingPolygons((DatasetVector) parameterSingleDatasetBounding.getSelectedItem());
			}
		} else {
			// 线面类型
			optimizedParameter.setAggregationMethod(AggregationMethod.NETWORKPOLYGONS);
			optimizedParameter.setAssessmentFieldName(parameterFieldComboBoxNotPoint.getFieldName());
		}
		try {
			ClusteringDistributions.addSteppedListener(steppedListener);
			DatasetVector result = ClusteringDistributions.optimizedHotSpotAnalyst(datasetVector, parameterSaveDataset.getResultDatasource(),
					parameterSaveDataset.getResultDatasource().getDatasets().getAvailableDatasetName(parameterSaveDataset.getDatasetName()), optimizedParameter);
			this.getParameters().getOutputs().getData(OUTPUT_DATASET).setValue(result);
			isSuccessful = result != null;
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
			e.printStackTrace();
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
