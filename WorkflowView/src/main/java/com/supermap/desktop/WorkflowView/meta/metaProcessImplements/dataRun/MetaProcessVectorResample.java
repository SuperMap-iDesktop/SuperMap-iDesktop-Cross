package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.dataRun;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.MapUtilities;

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
	private ParameterCheckBox parameterIsSaveSmallGeometry;
	// 进行拓扑预处理
	private ParameterCheckBox parameterIsTopologyPreprocess;
	// 节点捕捉容限
	private ParameterNumber parameterVertexTolerance;

	public MetaProcessVectorResample() {
		initParameters();
		initComponentState();
		initParameterConstraint();
	}


	private void initParameters() {
		// 源数据集
		this.datasource = new ParameterDatasourceConstrained();
		this.dataset = new ParameterSingleDataset(DatasetType.LINE, DatasetType.REGION);

		ParameterCombine parameterCombineSourceData = new ParameterCombine();
		parameterCombineSourceData.setDescribe(ControlsProperties.getString("String_GroupBox_SourceDataset"));
		parameterCombineSourceData.addParameters(this.datasource, this.dataset);

		// 参数设置
		this.parameterResampleType = new ParameterComboBox(ProcessProperties.getString("String_Resample_Type"));
		this.parameterResampleTolerance = new ParameterNumber(ProcessProperties.getString("String_Resample_Tolerance"));
		this.parameterIsSaveSmallGeometry = new ParameterCheckBox(ProcessProperties.getString("String_Resample_SaveSmallGeometry"));
		this.parameterIsTopologyPreprocess = new ParameterCheckBox(ProcessProperties.getString("String_Resample_TopologyPreprocess"));
		this.parameterVertexTolerance = new ParameterNumber(ProcessProperties.getString("String_Resample_VertexInterval"));

		this.parameterResampleType.addItem(new ParameterDataNode(ProcessProperties.getString("String_ResampleType_RTBEND"), ResampleType.RTBEND));
		this.parameterResampleType.addItem(new ParameterDataNode(ProcessProperties.getString("String_ResampleType_RTGENERAL"), ResampleType.RTGENERAL));
		this.parameterResampleTolerance.setMaxBit(17);
		this.parameterResampleTolerance.setMinValue(0);
		this.parameterResampleTolerance.setIsIncludeMin(false);
		this.parameterVertexTolerance.setMaxBit(17);
		this.parameterVertexTolerance.setMinValue(0);

		this.parameterResampleType.setRequisite(true);
		this.parameterResampleTolerance.setRequisite(true);

		ParameterCombine parameterCombineParameter = new ParameterCombine();
		parameterCombineParameter.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
		parameterCombineParameter.addParameters(this.parameterResampleType, this.parameterResampleTolerance, this.parameterIsSaveSmallGeometry,
				this.parameterIsTopologyPreprocess, this.parameterVertexTolerance);


		parameters.setParameters(parameterCombineSourceData, parameterCombineParameter);
		this.parameters.addInputParameters(INPUT_SOURCE_DATASET, DatasetTypes.LINE_POLYGON_VECTOR, parameterCombineSourceData);
		this.parameters.addOutputParameters(OUTPUT_DATA, ProcessOutputResultProperties.getString("String_Result_Resample"), DatasetTypes.LINE_POLYGON_VECTOR, parameterCombineSourceData);
	}

	private void initComponentState() {
		// 获得线和面类型的数据集
		Dataset datasetVector = DatasetUtilities.getDefaultDataset(DatasetType.LINE, DatasetType.REGION);
		if (datasetVector != null) {
			this.datasource.setSelectedItem(datasetVector.getDatasource());
			this.dataset.setSelectedItem(datasetVector);
//			this.resultDataset.setResultDatasource(datasetVector.getDatasource());
//			this.resultDataset.setSelectedItem(datasetVector.getDatasource().getDatasets().getAvailableDatasetName("result_VectorResample"));
		}
		this.parameterResampleType.setSelectedItem(ResampleType.RTBEND);
		this.parameterVertexTolerance.setEnabled(false);
		reloadValue();
	}

	private void initParameterConstraint() {

		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(this.datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(this.dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		this.parameterIsTopologyPreprocess.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterCheckBox.PARAMETER_CHECK_BOX_VALUE)) {
					parameterVertexTolerance.setEnabled((boolean) evt.getNewValue());
				}
			}
		});

		// 当数据集改变时，根据数据集类型给予不同的单位值，并且设置拓扑预处理和保留小对象是否可用
		this.dataset.addPropertyListener(new PropertyChangeListener() {
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
	 * 优化代码结构-yuanR2017.9.7
	 */
	private void reloadValue() {
		Dataset datasetSelectedDataset = (Dataset) this.dataset.getSelectedItem();
		if (datasetSelectedDataset != null) {
			DatasetType datasetType = datasetSelectedDataset.getType();
			String unit = ProcessProperties.getString(datasetType.equals(DatasetType.LINE) ? "String_Label_degree" : "String_Label_meter");
			this.parameterResampleTolerance.setUnit(unit);
			this.parameterVertexTolerance.setUnit(unit);
			String value = datasetType.equals(DatasetType.LINE) ? "0.0001" : "10";
			this.parameterResampleTolerance.setSelectedItem(value);
			this.parameterVertexTolerance.setSelectedItem(value);

			this.parameterIsSaveSmallGeometry.setSelectedItem((datasetType.equals(DatasetType.REGION) && !this.dataset.getSelectedItem().isReadOnly()));
			this.parameterIsTopologyPreprocess.setSelectedItem((datasetType.equals(DatasetType.REGION) && !this.dataset.getSelectedItem().isReadOnly()));
			this.parameterIsSaveSmallGeometry.setEnabled(datasetType.equals(DatasetType.REGION) && !this.dataset.getSelectedItem().isReadOnly());
			this.parameterIsTopologyPreprocess.setEnabled(datasetType.equals(DatasetType.REGION) && !this.dataset.getSelectedItem().isReadOnly());
		}
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		DatasetVector sourceDatasetVector = null;
		try {
			if (this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET) != null
					&& this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET).getValue() instanceof DatasetVector) {
				sourceDatasetVector = (DatasetVector) this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET).getValue();
			} else {
				sourceDatasetVector = (DatasetVector) this.dataset.getSelectedItem();
			}

			ResampleType resampleType = (ResampleType) this.parameterResampleType.getSelectedData();
			double tolerance = Double.valueOf((String) this.parameterResampleTolerance.getSelectedItem());
			Boolean isSaveSmallGeometry = "true".equalsIgnoreCase((String) this.parameterIsSaveSmallGeometry.getSelectedItem());
			Boolean isTopologyPreprocess = "true".equalsIgnoreCase((String) this.parameterIsTopologyPreprocess.getSelectedItem());
			double vertexTolerance = Double.valueOf((String) this.parameterVertexTolerance.getSelectedItem());

			ResampleInformation resampleInformation = new ResampleInformation();
			resampleInformation.setResampleType(resampleType);
			resampleInformation.setTolerance(tolerance);
			resampleInformation.setTopologyPreprocess(isTopologyPreprocess);

			if (isTopologyPreprocess) {
				resampleInformation.setVertexInterval(vertexTolerance);
			}

			sourceDatasetVector.addSteppedListener(this.steppedListener);
			isSuccessful = sourceDatasetVector.resample(resampleInformation, true, isSaveSmallGeometry);

			if (isSuccessful) {
				MapUtilities.refreshIfDatasetOpened(sourceDatasetVector);
			}
			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(sourceDatasetVector);
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
