package com.supermap.desktop.process.meta.metaProcessImplements.DataRun;

import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

/**
 * Created by yuanR on 2017/7/18.
 * 线面光滑
 */
public class MetaProcessLinePolygonSmooth extends MetaProcess {
	private final static String INPUT_SOURCE_DATASET = "SourceDataset";

	private ParameterDatasourceConstrained datasource;
	private ParameterSingleDataset dataset;
	private ParameterNumber parameterTextFieldSmoothness;


	public MetaProcessLinePolygonSmooth() {
		initParameters();
		initComponentState();
		initParameterConstraint();
	}


	private void initParameters() {
		datasource = new ParameterDatasourceConstrained();
		dataset = new ParameterSingleDataset(DatasetType.LINE, DatasetType.REGION);
		parameterTextFieldSmoothness = new ParameterNumber(ProcessProperties.getString("String_Label_Smoothness"));
		parameterTextFieldSmoothness.setMaxBit(0);
		parameterTextFieldSmoothness.setMinValue(2);
		parameterTextFieldSmoothness.setMaxValue(10);
		parameterTextFieldSmoothness.setRequisite(true);

		ParameterCombine parameterCombineSourceData = new ParameterCombine();
		parameterCombineSourceData.addParameters(datasource, dataset);
		parameterCombineSourceData.setDescribe(ControlsProperties.getString("String_GroupBox_SourceDataset"));

		ParameterCombine parameterCombineParameter = new ParameterCombine();
		parameterCombineParameter.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
		parameterCombineParameter.addParameters(parameterTextFieldSmoothness);

		parameters.setParameters(parameterCombineSourceData, parameterCombineParameter);
		this.parameters.addInputParameters(INPUT_SOURCE_DATASET, DatasetTypes.LINE_POLYGON_VECTOR, parameterCombineSourceData);

	}

	private void initComponentState() {
		parameterTextFieldSmoothness.setSelectedItem("2");
		DatasetVector datasetVector = DatasetUtilities.getDefaultDatasetVector();
		if (datasetVector != null) {
			datasource.setSelectedItem(datasetVector.getDatasource());
			dataset.setSelectedItem(datasetVector);
		}
	}

	private void initParameterConstraint() {

		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
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

			int smoothness = Integer.valueOf(((String) parameterTextFieldSmoothness.getSelectedItem()));

			datasetVector.addSteppedListener(this.steppedListener);
			isSuccessful = datasetVector.smooth(smoothness, true);
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
		return ProcessProperties.getString("String_LinePolygonSmooth");
	}

	@Override
	public String getKey() {
		return MetaKeys.LinePolygonSmooth;
	}

}
