package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.dataRun;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by yuanR on 2017/7/18.
 * 矢量重采样
 * 重采样会修改原数据，当操作的面数据集为只读数据集时，设置为不能进行拓扑预处理
 */
public class MetaProcessVectorResample extends MetaProcess {
	private final static String INPUT_SOURCE_DATASET = CommonProperties.getString("String_GroupBox_SourceData");
	private final static String OUTPUT_DATA = "VectorResampleResult";

	private ParameterDatasourceConstrained datasource;
	private ParameterSingleDataset dataset;
//	private ParameterSaveDataset resultDataset;

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

		parameterResampleType.setRequisite(true);
		parameterResampleTolerance.setRequisite(true);

		ParameterCombine parameterCombineParameter = new ParameterCombine();
		parameterCombineParameter.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
		parameterCombineParameter.addParameters(parameterResampleType, parameterResampleTolerance, parameterisSaveSmallGeometry,
				parameterisTopologyPreprocess, parameterVertexTolerance);

		// 结果数据集
//		resultDataset = new ParameterSaveDataset();
//		ParameterCombine parameterCombineResultData = new ParameterCombine();
//		parameterCombineResultData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
//		parameterCombineResultData.addParameters(resultDataset);


		parameters.setParameters(
				parameterCombineSourceData,
				parameterCombineParameter
//				parameterCombineResultData
		);

		this.parameters.addInputParameters(INPUT_SOURCE_DATASET, DatasetTypes.LINE_POLYGON_VECTOR, parameterCombineSourceData);
		this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.LINE_POLYGON_VECTOR, parameterCombineSourceData);
	}

	private void initComponentState() {
		// 获得线和面类型的数据集
		Dataset datasetVector = DatasetUtilities.getDefaultDataset(DatasetType.LINE, DatasetType.REGION);
		if (datasetVector != null) {
			datasource.setSelectedItem(datasetVector.getDatasource());
			dataset.setSelectedItem(datasetVector);
//			this.resultDataset.setResultDatasource(datasetVector.getDatasource());
//			this.resultDataset.setSelectedItem(datasetVector.getDatasource().getDatasets().getAvailableDatasetName("result_VectorResample"));
		}
		parameterResampleType.setSelectedItem(ResampleType.RTBEND);
		reloadValue();
		parameterVertexTolerance.setEnabled(false);
	}

	private void initParameterConstraint() {

		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

//		DatasourceConstraint.getInstance().constrained(resultDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);

		parameterisTopologyPreprocess.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterCheckBox.PARAMETER_CHECK_BOX_VALUE)) {
					parameterVertexTolerance.setEnabled((boolean) evt.getNewValue());
				}
			}
		});

		// 当数据集改变时，根据数据集类型给予不同的单位值，并且设置拓扑预处理和保留小对象是否可用
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
				parameterResampleTolerance.setSelectedItem("0.0001");
				parameterVertexTolerance.setSelectedItem("0.0001");

				parameterisSaveSmallGeometry.setSelectedItem(false);
				parameterisSaveSmallGeometry.setEnabled(false);
				parameterisTopologyPreprocess.setSelectedItem(false);
				parameterisTopologyPreprocess.setEnabled(false);
			} else if (datasetType.equals(DatasetType.REGION)) {
				parameterResampleTolerance.setUnit(ProcessProperties.getString("String_Label_meter"));
				parameterVertexTolerance.setUnit(ProcessProperties.getString("String_Label_meter"));
				parameterResampleTolerance.setSelectedItem("10");
				parameterVertexTolerance.setSelectedItem("10");
				// 根据可读与否设置拓扑预处理是否可用--yuanR 2017.8.8
				if (dataset.getSelectedItem().isReadOnly()) {
					parameterisSaveSmallGeometry.setSelectedItem(false);
					parameterisSaveSmallGeometry.setEnabled(false);
					parameterisTopologyPreprocess.setSelectedItem(false);
					parameterisTopologyPreprocess.setEnabled(false);
				} else {
					parameterisSaveSmallGeometry.setSelectedItem(true);
					parameterisSaveSmallGeometry.setEnabled(true);
					parameterisTopologyPreprocess.setSelectedItem(true);
					parameterisTopologyPreprocess.setEnabled(true);
				}

			}
		}
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		DatasetVector sourceDatasetVector = null;
		try {
			fireRunning(new RunningEvent(MetaProcessVectorResample.this, 0, "start"));
			if (this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET) != null
					&& this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET).getValue() instanceof DatasetVector) {
				sourceDatasetVector = (DatasetVector) this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET).getValue();
			} else {
				sourceDatasetVector = (DatasetVector) dataset.getSelectedItem();
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

			sourceDatasetVector.addSteppedListener(this.steppedListener);
			isSuccessful = sourceDatasetVector.resample(resampleInformation, true, isSaveSmallGeometry);
			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(sourceDatasetVector);
			fireRunning(new RunningEvent(MetaProcessVectorResample.this, 100, "finished"));
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			sourceDatasetVector.removeSteppedListener(this.steppedListener);
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
		return MetaKeys.VECTOR_RESAMPLE;
	}
}
