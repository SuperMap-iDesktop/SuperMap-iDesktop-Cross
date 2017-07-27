package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.DataRun;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameters.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by yuanR on 2017/7/18.
 * 矢量重采样
 */
public class MetaProcessVectorResample extends MetaProcess {
	private final static String INPUT_SOURCE_DATASET = "SourceDataset";
//	private final static String OUTPUT_DATASET = "VectorResampleResult";

	private ParameterDatasourceConstrained datasource;
	private ParameterSingleDataset dataset;

	// 重采样类型
	private ParameterComboBox parameterResampleType;
	// 重采样距离
	private ParameterNumber parameterResampleTolerance;
	// 重采样是否保留小对象
	private ParameterCheckBox parameterisSaveSmallGeometry;
	// 进行拓扑预处理
	private ParameterCheckBox parameterisTopologyPreprocess;
	// 节点捕捉容限
	private ParameterNumber parameterVertexTolerance;
	// 字符：度
	private ParameterLabel label;

//	private ParameterSaveDataset parameterSaveDataset;


	public MetaProcessVectorResample() {
		initParameters();
		initComponentState();
		initParameterConstraint();
	}


	private void initParameters() {
		// 源数据集
		datasource = new ParameterDatasourceConstrained();
		dataset = new ParameterSingleDataset(DatasetType.LINE, DatasetType.REGION);

		ParameterCombine parameterCombineSourceData = new ParameterCombine();
		parameterCombineSourceData.setDescribe(ControlsProperties.getString("String_GroupBox_SourceDataset"));
		parameterCombineSourceData.addParameters(datasource, dataset);

		// 参数设置
		parameterResampleType = new ParameterComboBox(ProcessProperties.getString("String_Resample_Type"));
		parameterResampleTolerance = new ParameterNumber(ProcessProperties.getString("String_Resample_Tolerance"));
		parameterisSaveSmallGeometry = new ParameterCheckBox(ProcessProperties.getString("String_Resample_SaveSmallGeometry"));
		parameterisTopologyPreprocess = new ParameterCheckBox(ProcessProperties.getString("String_Resample_TopologyPreprocess"));
		parameterVertexTolerance = new ParameterNumber(ProcessProperties.getString("String_Resample_VertexInterval"));

		parameterResampleType.addItem(new ParameterDataNode(ProcessProperties.getString("String_ResampleType_RTBEND"), ResampleType.RTBEND));
		parameterResampleType.addItem(new ParameterDataNode(ProcessProperties.getString("String_ResampleType_RTGENERAL"), ResampleType.RTGENERAL));
		parameterResampleTolerance.setMaxBit(17);
		parameterResampleTolerance.setMinValue(0);
		parameterResampleTolerance.setIsIncludeMin(false);
		parameterVertexTolerance.setMaxBit(17);
		parameterVertexTolerance.setMinValue(0);

//		parameterResampleTolerance.addItem(new ParameterDataNode("1", "0.7036"));
//		parameterResampleTolerance.addItem(new ParameterDataNode("2", "0.3518"));
//		parameterResampleTolerance.addItem(new ParameterDataNode("3", "0.1759"));
//		parameterResampleTolerance.addItem(new ParameterDataNode("4", "0.088"));
//		parameterResampleTolerance.addItem(new ParameterDataNode("5", "0.044"));
//		parameterResampleTolerance.addItem(new ParameterDataNode("6", "0.022"));
//		parameterResampleTolerance.addItem(new ParameterDataNode("7", "0.011"));
//		parameterResampleTolerance.addItem(new ParameterDataNode("8", "0.0055"));
//		parameterResampleTolerance.addItem(new ParameterDataNode("9", "0.0028"));
//		parameterResampleTolerance.addItem(new ParameterDataNode("10", "0.0014"));
//		parameterResampleTolerance.addItem(new ParameterDataNode("11", "0.0007"));
//		parameterResampleTolerance.addItem(new ParameterDataNode("12", "0.0004"));
//		parameterResampleTolerance.addItem(new ParameterDataNode("13", "0.0002"));
//		parameterResampleTolerance.addItem(new ParameterDataNode("14", "0.0001"));
//		parameterResampleTolerance.addItem(new ParameterDataNode("15", "0.00005"));
//		parameterResampleTolerance.addItem(new ParameterDataNode("16", "0.000025"));
//		parameterResampleTolerance.addItem(new ParameterDataNode("17", "0.000013"));
//		parameterResampleTolerance.addItem(new ParameterDataNode("18", "0.000065"));
//		parameterResampleTolerance.addItem(new ParameterDataNode("19", "0.000033"));
//		parameterResampleTolerance.addItem(new ParameterDataNode("20", "0.000017"));
		parameterResampleType.setRequisite(true);
		parameterResampleTolerance.setRequisite(true);

		ParameterCombine parameterCombineParameter = new ParameterCombine();
		parameterCombineParameter.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
		parameterCombineParameter.addParameters(parameterResampleType, parameterResampleTolerance, parameterisSaveSmallGeometry,
				parameterisTopologyPreprocess, parameterVertexTolerance);

		// 结果数据
//		parameterSaveDataset = new ParameterSaveDataset();
//		ParameterCombine parameterCombineResult = new ParameterCombine();
//		parameterCombineResult.addParameters(parameterSaveDataset);
//		parameterCombineResult.setDescribe(CommonProperties.getString("String_ResultSet"));

		parameters.setParameters(
				parameterCombineSourceData,
				parameterCombineParameter
//				parameterCombineResult
		);

		this.parameters.addInputParameters(INPUT_SOURCE_DATASET, DatasetTypes.LINE_POLYGON_VECTOR, parameterCombineSourceData);
//		this.parameters.addOutputParameters(OUTPUT_DATASET, DatasetTypes.SIMPLE_VECTOR, parameterCombineResult);
	}

	private void initComponentState() {
		// 获得线和面类型的数据集
		Dataset datasetVector = DatasetUtilities.getDefaultDataset(DatasetType.LINE, DatasetType.REGION);
		if (datasetVector != null) {
			datasource.setSelectedItem(datasetVector.getDatasource());
			dataset.setSelectedItem(datasetVector);
		}
		parameterResampleType.setSelectedItem(ResampleType.RTBEND);
		reloadValue();
		parameterVertexTolerance.setEnabled(false);

	}

	private void initParameterConstraint() {

		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		parameterisTopologyPreprocess.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterCheckBox.PARAMETER_CHECK_BOX_VALUE)) {
					parameterVertexTolerance.setEnabled((boolean) evt.getNewValue());
				}
			}
		});

		datasource.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterDatasource.DATASOURCE_FIELD_NAME)) {
					Dataset datasetVector = DatasetUtilities.getDefaultDataset(DatasetType.LINE, DatasetType.REGION);
					if (datasetVector != null) {
						dataset.setSelectedItem(datasetVector);
					}
				}
			}
		});

//		// 当数据集改变时，根据数据集类型给予不同的单位值
		dataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterSingleDataset.DATASET_FIELD_NAME)) {
					reloadValue();
				}
			}
		});
	}

	/**
	 * 当数据集改变时，根据数据集类型设置不同的距离单位
	 * yuanR
	 */
	public void reloadValue() {
		Dataset datasetSelectedDataset = (Dataset) dataset.getSelectedItem();
		if (datasetSelectedDataset != null) {
			DatasetType datasetType = datasetSelectedDataset.getType();
			if (datasetType.equals(DatasetType.LINE)) {
				parameterResampleTolerance.setUnit(ProcessProperties.getString("String_Label_degree"));
				parameterVertexTolerance.setUnit(ProcessProperties.getString("String_Label_degree"));
				parameterResampleTolerance.setSelectedItem(0.0001);
				parameterVertexTolerance.setSelectedItem(0.0001);
			} else if (datasetType.equals(DatasetType.REGION)) {
				parameterResampleTolerance.setUnit(ProcessProperties.getString("String_Label_meter"));
				parameterVertexTolerance.setUnit(ProcessProperties.getString("String_Label_meter"));
				parameterResampleTolerance.setSelectedItem(10);
				parameterVertexTolerance.setSelectedItem(10);
			}
		}
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		try {
			DatasetVector datasetVector = null;
			if (this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET) != null
					&& this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET).getValue() instanceof DatasetVector) {
				datasetVector = (DatasetVector) this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET).getValue();
			} else {
				datasetVector = (DatasetVector) dataset.getSelectedItem();
			}

			ResampleType resampleType = (ResampleType) parameterResampleType.getSelectedData();
			double tolerance = Double.valueOf((String) parameterResampleTolerance.getSelectedItem());
			Boolean isSaveSmallGeometry = "true".equalsIgnoreCase((String) parameterisSaveSmallGeometry.getSelectedItem());
			Boolean isTopologyPreprocess = "true".equalsIgnoreCase((String) parameterisTopologyPreprocess.getSelectedItem());
			double vertexTolerance = Double.valueOf((String) parameterVertexTolerance.getSelectedItem());

			ResampleInformation resampleInformation = new ResampleInformation();
			resampleInformation.setResampleType(resampleType);
			resampleInformation.setTolerance(tolerance);
			resampleInformation.setTopologyPreprocess(isTopologyPreprocess);
			if (isTopologyPreprocess) {
				resampleInformation.setVertexInterval(vertexTolerance);
			}

			datasetVector.addSteppedListener(this.steppedListener);
			isSuccessful = datasetVector.resample(resampleInformation, true, isSaveSmallGeometry);
			datasetVector.removeSteppedListener(this.steppedListener);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_Params_error"));
		} finally {

		}
		return isSuccessful;
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_VectorResample");
	}

	@Override
	public String getKey() {
		return MetaKeys.VectorResample;
	}
}
