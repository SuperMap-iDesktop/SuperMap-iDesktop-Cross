package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.ConversionAnalyst;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetImage;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.DatasourceConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by lixiaoyao on 2017/7/11.
 */
public class MetaProcessThinRaster extends MetaProcess{
	private final static String INPUT_DATA = "InputData";
	private final static String OUTPUT_DATA = "ExtractResult";

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset sourceDataset;
	private ParameterCombine sourceData;

	private ParameterNumber textFieldNoValue;
	private ParameterNumber textFieldNoValueTolerance;
	private ParameterCombine parameterSetting;

	private ParameterSaveDataset resultDataset;
	private ParameterCombine resultData;

	public MetaProcessThinRaster() {
		initParameters();
		initParameterConstraint();
		initParametersState();
		registerListener();
	}

	private void initParameters() {
		sourceDatasource = new ParameterDatasourceConstrained();
		sourceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		sourceDataset = new ParameterSingleDataset(DatasetType.GRID, DatasetType.IMAGE);
		sourceDataset.setDescribe(CommonProperties.getString("String_Label_Dataset"));

		resultDataset = new ParameterSaveDataset();
		this.resultDataset.setDatasourceDescribe(CommonProperties.getString("String_TargetDatasource"));
		this.resultDataset.setDatasetDescribe(CommonProperties.getString("String_TargetDataset"));

		textFieldNoValue = new ParameterNumber(CommonProperties.getString("String_Label_NoData"));
		textFieldNoValueTolerance = new ParameterNumber(CommonProperties.getString("String_Label_NoValueTolerance"));

		sourceData = new ParameterCombine();
		sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		sourceData.addParameters(sourceDatasource, sourceDataset);

		parameterSetting = new ParameterCombine();
		parameterSetting.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
		parameterSetting.addParameters(textFieldNoValue,textFieldNoValueTolerance);

		resultData = new ParameterCombine();
		resultData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		resultData.addParameters(resultDataset);


		this.parameters.setParameters(sourceData, parameterSetting, resultData);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.GRID, sourceData);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.IMAGE, sourceData);
		this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.GRID, resultData);
		this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.IMAGE, resultData);
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(sourceDatasource, ParameterDatasourceConstrained.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		DatasourceConstraint.getInstance().constrained(resultDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
	}

	private void initParametersState() {
		DatasetGrid datasetGrid = DatasetUtilities.getDefaultDatasetGrid();
		if (datasetGrid != null) {
			this.sourceDatasource.setSelectedItem(datasetGrid.getDatasource());
			this.sourceDataset.setDatasource(datasetGrid.getDatasource());
		}

		resultDataset.setSelectedItem("ThinRaster");
		textFieldNoValue.setSelectedItem("-9999");
		textFieldNoValueTolerance.setSelectedItem("0");
		textFieldNoValueTolerance.setMinValue(0);
		textFieldNoValueTolerance.setIsIncludeMin(true);
	}

	private void registerListener() {
		sourceDataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (sourceDataset.getSelectedItem() instanceof DatasetGrid){
					textFieldNoValue.setSelectedItem(((DatasetGrid) sourceDataset.getSelectedItem()).getNoValue());
				}else if (sourceDataset.getSelectedItem() instanceof DatasetImage){
					textFieldNoValue.setSelectedItem("16777215");
				}
			}
		});
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		try {
			fireRunning(new RunningEvent(MetaProcessThinRaster.this, 0, "start"));

			String datasetName = resultDataset.getDatasetName();
			datasetName = resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(datasetName);

			Dataset src=null;
			if (this.getParameters().getInputs().getData(INPUT_DATA).getValue() != null) {
				src = (Dataset)this.getParameters().getInputs().getData(INPUT_DATA).getValue();
			} else {
				src = sourceDataset.getSelectedDataset();
			}
			ConversionAnalyst.addSteppedListener(steppedListener);

			Dataset result = ConversionAnalyst.thinRaster(src, Math.round( Double.valueOf(this.textFieldNoValue.getSelectedItem().toString())), Double.valueOf(this.textFieldNoValueTolerance.getSelectedItem().toString()), this.resultDataset.getResultDatasource(), datasetName);

			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(result);
			isSuccessful = result != null;

			fireRunning(new RunningEvent(MetaProcessThinRaster.this, 100, "finished"));
		}catch (Exception e){
			Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_Params_error"));
		}finally {
			ConversionAnalyst.removeSteppedListener(steppedListener);
		}
		return isSuccessful;
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public String getKey() {
		return MetaKeys.THINRASTER;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Form_GridThinRaster");
	}
}
