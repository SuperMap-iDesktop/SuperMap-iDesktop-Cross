package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.dataRun;

import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.MapUtilities;

/**
 * Created by yuanR on 2017/7/18.
 * 线面光滑
 */
public class MetaProcessLinePolygonSmooth extends MetaProcess {
	private final static String INPUT_SOURCE_DATASET = CommonProperties.getString("String_GroupBox_SourceData");
	private final static String OUTPUT_DATA = "LinePolygonSmoothResult";

	private ParameterDatasourceConstrained datasource;
	private ParameterSingleDataset dataset;
	private ParameterNumber parameterTextFieldSmoothness;


	public MetaProcessLinePolygonSmooth() {
		initParameters();
		initComponentState();
		initParameterConstraint();
	}


	private void initParameters() {
		this.datasource = new ParameterDatasourceConstrained();
		this.datasource.setReadOnlyNeeded(false);
		this.dataset = new ParameterSingleDataset(DatasetType.LINE, DatasetType.REGION);
		this.parameterTextFieldSmoothness = new ParameterNumber(ProcessProperties.getString("String_Label_Smoothness"));
		this.parameterTextFieldSmoothness.setMaxBit(0);
		this.parameterTextFieldSmoothness.setMinValue(2);
		this.parameterTextFieldSmoothness.setMaxValue(10);
		this.parameterTextFieldSmoothness.setRequisite(true);

		ParameterCombine parameterCombineSourceData = new ParameterCombine();
		parameterCombineSourceData.addParameters(this.datasource, this.dataset);
		parameterCombineSourceData.setDescribe(ControlsProperties.getString("String_GroupBox_SourceDataset"));

		ParameterCombine parameterCombineParameter = new ParameterCombine();
		parameterCombineParameter.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
		parameterCombineParameter.addParameters(this.parameterTextFieldSmoothness);

		this.parameters.setParameters(parameterCombineSourceData, parameterCombineParameter);
		this.parameters.addInputParameters(INPUT_SOURCE_DATASET, DatasetTypes.LINE_POLYGON_VECTOR, parameterCombineSourceData);
		this.parameters.addOutputParameters(OUTPUT_DATA, ProcessOutputResultProperties.getString("String_Result_LinePolygonSmooth"), DatasetTypes.LINE_POLYGON_VECTOR, parameterCombineSourceData);
	}

	private void initComponentState() {
		this.parameterTextFieldSmoothness.setSelectedItem("2");
		DatasetVector datasetVector = DatasetUtilities.getDefaultDatasetVector();
		if (datasetVector != null) {
			this.datasource.setSelectedItem(datasetVector.getDatasource());
			this.dataset.setSelectedItem(datasetVector);
		}
	}

	private void initParameterConstraint() {

		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(this.datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(this.dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		DatasetVector datasetVector = null;
		try {

			if (this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET) != null
					&& this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET).getValue() instanceof DatasetVector) {
				datasetVector = (DatasetVector) this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET).getValue();
			} else {
				datasetVector = (DatasetVector) this.dataset.getSelectedItem();
			}

			int smoothness = Integer.valueOf(((String) this.parameterTextFieldSmoothness.getSelectedItem()));

			datasetVector.addSteppedListener(this.steppedListener);
			isSuccessful = datasetVector.smooth(smoothness, true);

			if (isSuccessful) {
				MapUtilities.refreshIfDatasetOpened(datasetVector);
			}
			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(datasetVector);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			datasetVector.removeSteppedListener(this.steppedListener);
		}
		return isSuccessful;
	}

	@Override
	public IParameters getParameters() {
		return this.parameters;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_LinePolygonSmooth");
	}

	@Override
	public String getKey() {
		return MetaKeys.LINE_POLYGON_SMOOTH;
	}

}
