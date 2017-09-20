package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.gridAnalyst;

import com.supermap.analyst.spatialanalyst.SolarRadiation;
import com.supermap.analyst.spatialanalyst.SolarRadiationParameter;
import com.supermap.analyst.spatialanalyst.SolarRadiationResult;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.DatasourceConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.process.parameters.ParameterPanels.SolarRadiation.ParameterCheckboBoxAndTextField;
import com.supermap.desktop.process.parameters.ParameterPanels.SolarRadiation.ParameterSolarRadiationAnalysisType;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by lixiaoyao on 2017/9/7.
 */
public class MetaProcessSolarRadiation extends MetaProcess {
	private final static String INPUT_DATA = ProcessProperties.getString("String_TerrainData");
	private final static String OUTPUT_DATA_TOTAL = "TotalResult";
	private final static String OUTPUT_DATA_DIFFUSE = "DiffuseResult";
	private final static String OUTPUT_DATA_DIRECTION = "DirectionResult";
	private final static String OUTPUT_DATA_DURATION = "DurationResult";

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset sourceDataset;
	private ParameterSolarRadiationAnalysisType solarRadiationAnalysisType;
	private ParameterNumber parameterNumberLatitude;
	private ParameterNumber parameterNumberTransmittance;
	private ParameterNumber parameterNumberZFactor;
	private ParameterDatasource resultDatasource;
	private ParameterTextField textFieldTotal;
	private ParameterCheckboBoxAndTextField checkboBoxAndTextFieldDirection;
	private ParameterCheckboBoxAndTextField checkboBoxAndTextFieldDiffuse;
	private ParameterCheckboBoxAndTextField checkboBoxAndTextFieldDuration;

	public MetaProcessSolarRadiation() {
		initParameters();
		initParametersState();
		initParameterConstraint();
		registerListener();
	}

	private void initParameters() {
		this.sourceDatasource = new ParameterDatasourceConstrained();
		this.sourceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		this.sourceDataset = new ParameterSingleDataset(DatasetType.GRID);
		this.sourceDataset.setDescribe(CommonProperties.getString("String_Label_Dataset"));
		this.solarRadiationAnalysisType = new ParameterSolarRadiationAnalysisType(ProcessProperties.getString("String_AnalysisType"));
		this.parameterNumberLatitude = new ParameterNumber(ProcessProperties.getString("String_AreaLatitude"));
		this.parameterNumberTransmittance = new ParameterNumber(ProcessProperties.getString("String_Transmittance"));
		this.parameterNumberZFactor = new ParameterNumber(ProcessProperties.getString("String_ZFactor"));
		this.resultDatasource = new ParameterDatasource();
		this.resultDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		this.textFieldTotal = new ParameterTextField(ProcessProperties.getString("String_SolarRadiationTotal"));
		this.checkboBoxAndTextFieldDirection = new ParameterCheckboBoxAndTextField(ProcessProperties.getString("String_SolarRadiationDirection"));
		this.checkboBoxAndTextFieldDiffuse = new ParameterCheckboBoxAndTextField(ProcessProperties.getString("String_SolarRadiationDiffuse"));
		this.checkboBoxAndTextFieldDuration = new ParameterCheckboBoxAndTextField(ProcessProperties.getString("String_SolarRadiationDuration"));

		ParameterCombine sourceData = new ParameterCombine();
		sourceData.setDescribe(ProcessProperties.getString("String_TerrainData"));
		sourceData.addParameters(this.sourceDatasource, this.sourceDataset);
		ParameterCombine radiationSetting = new ParameterCombine();
		radiationSetting.setDescribe(ProcessProperties.getString("String_RadiationSetting"));
		radiationSetting.addParameters(this.parameterNumberLatitude, this.parameterNumberTransmittance, this.parameterNumberZFactor);
		ParameterCombine resultData = new ParameterCombine();
		resultData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		resultData.addParameters(this.resultDatasource, this.textFieldTotal, this.checkboBoxAndTextFieldDirection, this.checkboBoxAndTextFieldDiffuse, this.checkboBoxAndTextFieldDuration);

		this.parameters.setParameters(sourceData, this.solarRadiationAnalysisType, radiationSetting, resultData);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.GRID, sourceData);
		this.parameters.addOutputParameters(OUTPUT_DATA_TOTAL, ProcessOutputResultProperties.getString("String_SolarRadiationTotal"), DatasetTypes.GRID, this.textFieldTotal);
		this.parameters.addOutputParameters(OUTPUT_DATA_DIFFUSE, ProcessOutputResultProperties.getString("String_SolarRadiationDiffuse"), DatasetTypes.GRID, this.checkboBoxAndTextFieldDiffuse);
		this.parameters.addOutputParameters(OUTPUT_DATA_DIRECTION, ProcessOutputResultProperties.getString("String_SolarRadiationDirection"), DatasetTypes.GRID, this.checkboBoxAndTextFieldDirection);
		this.parameters.addOutputParameters(OUTPUT_DATA_DURATION, ProcessOutputResultProperties.getString("String_SolarRadiationDuration"), DatasetTypes.GRID, this.checkboBoxAndTextFieldDuration);
	}

	private void initParametersState() {
		Dataset dataset = DatasetUtilities.getDefaultDataset(DatasetType.GRID);
		if (dataset != null) {
			this.sourceDatasource.setSelectedItem(dataset.getDatasource());
			this.sourceDataset.setSelectedItem(dataset);
			this.resultDatasource.setSelectedItem(dataset.getDatasource());
			this.textFieldTotal.setSelectedItem(dataset.getDatasource().getDatasets().getAvailableDatasetName("result_Total"));
			this.checkboBoxAndTextFieldDirection.setSelectedItem(dataset.getDatasource().getDatasets().getAvailableDatasetName("result_Direction"));
			this.checkboBoxAndTextFieldDiffuse.setSelectedItem(dataset.getDatasource().getDatasets().getAvailableDatasetName("result_Diffuse"));
			this.checkboBoxAndTextFieldDuration.setSelectedItem(dataset.getDatasource().getDatasets().getAvailableDatasetName("result_Duration"));
			changeLatitude((DatasetGrid)dataset);
		}
		this.resultDatasource.setReadOnlyNeeded(false);
		this.parameterNumberLatitude.setMinValue(-90);
		this.parameterNumberLatitude.setMaxValue(90);
		this.parameterNumberLatitude.setIsIncludeMin(true);
		this.parameterNumberLatitude.setIncludeMax(true);
		this.parameterNumberTransmittance.setSelectedItem(0.5);
		this.parameterNumberZFactor.setSelectedItem(1);
		this.parameterNumberTransmittance.setMinValue(0);
		this.parameterNumberTransmittance.setMaxValue(1);
		this.parameterNumberTransmittance.setIsIncludeMin(true);
		this.parameterNumberTransmittance.setIncludeMax(true);
		this.parameterNumberZFactor.setMinValue(0);
		this.parameterNumberZFactor.setIsIncludeMin(false);
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(this.sourceDatasource, ParameterDatasourceConstrained.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(this.sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		DatasourceConstraint.getInstance().constrained(this.resultDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
	}

	private void registerListener() {
		this.sourceDataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (sourceDataset.getSelectedItem() != null && evt.getNewValue() instanceof DatasetGrid) {
					changeLatitude((DatasetGrid) evt.getNewValue());
				}
			}
		});

	}

	private void changeLatitude(DatasetGrid datasetGrid){
		if (datasetGrid.getPrjCoordSys().getGeoCoordSys() != null) {
			if (datasetGrid.getPrjCoordSys().getType() != PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) {
				Point2Ds point2Ds = new Point2Ds();
				point2Ds.add(datasetGrid.getBounds().getCenter());
				CoordSysTranslator.inverse(point2Ds, datasetGrid.getPrjCoordSys());
				parameterNumberLatitude.setSelectedItem(point2Ds.getItem(0).getY());
			} else {
				parameterNumberLatitude.setSelectedItem(datasetGrid.getBounds().getCenter().getY());
			}
			parameterNumberLatitude.setEnabled(false);
			//用户的数据集范围错误的时候允许自定义设置区域纬度
			if (Double.valueOf(parameterNumberLatitude.getSelectedItem()) < -90 ||
					Double.valueOf(parameterNumberLatitude.getSelectedItem()) > 90) {
				parameterNumberLatitude.setEnabled(true);
				parameterNumberLatitude.setSelectedItem(45);
			}
		} else {
			parameterNumberLatitude.setEnabled(true);
			parameterNumberLatitude.setSelectedItem(45);
		}
	}

	@Override
	public final boolean execute() {
		boolean isSuccessful = false;
		try {
			String totalDatasetName = this.resultDatasource.getSelectedItem().getDatasets().getAvailableDatasetName(this.textFieldTotal.getSelectedItem().toString());
			String directionDatasetName = null;
			if (this.checkboBoxAndTextFieldDirection.isSelected()) {
				directionDatasetName = this.resultDatasource.getSelectedItem().getDatasets().getAvailableDatasetName(this.checkboBoxAndTextFieldDirection.getSelectedItem().toString());
			}
			String diffuseDatasetName = null;
			if (this.checkboBoxAndTextFieldDiffuse.isSelected()) {
				diffuseDatasetName = this.resultDatasource.getSelectedItem().getDatasets().getAvailableDatasetName(this.checkboBoxAndTextFieldDiffuse.getSelectedItem().toString());
			}
			String durationDatasetName = null;
			if (this.checkboBoxAndTextFieldDuration.isSelected()) {
				durationDatasetName = this.resultDatasource.getSelectedItem().getDatasets().getAvailableDatasetName(this.checkboBoxAndTextFieldDuration.getSelectedItem().toString());
			}
			DatasetGrid src = null;
			if (this.getParameters().getInputs().getData(INPUT_DATA).getValue() != null) {
				src = (DatasetGrid) this.getParameters().getInputs().getData(INPUT_DATA).getValue();
			} else {
				src = (DatasetGrid) this.sourceDataset.getSelectedItem();
			}

			SolarRadiationParameter solarRadiationParameter = this.solarRadiationAnalysisType.getSolarRadiationParameter();
			solarRadiationParameter.setLatitude(Double.valueOf(this.parameterNumberLatitude.getSelectedItem().toString()));
			solarRadiationParameter.setTransmittance(Double.valueOf(this.parameterNumberTransmittance.getSelectedItem().toString()));
			solarRadiationParameter.setZFactor(Double.valueOf(this.parameterNumberZFactor.getSelectedItem().toString()));

			SolarRadiation.addSteppedListener(steppedListener);
			SolarRadiationResult solarRadiationResult = SolarRadiation.areaSolarRadiation(src, solarRadiationParameter,
					this.resultDatasource.getSelectedItem(), totalDatasetName, directionDatasetName, diffuseDatasetName, durationDatasetName);
			this.getParameters().getOutputs().getData(OUTPUT_DATA_TOTAL).setValue(solarRadiationResult.getTotalDatasetGrid());
			this.getParameters().getOutputs().getData(OUTPUT_DATA_DIRECTION).setValue(solarRadiationResult.getDirectDatasetGrid());
			this.getParameters().getOutputs().getData(OUTPUT_DATA_DIFFUSE).setValue(solarRadiationResult.getDiffuseDatasetGrid());
			this.getParameters().getOutputs().getData(OUTPUT_DATA_DURATION).setValue(solarRadiationResult.getDurationDatasetGrid());
			isSuccessful = solarRadiationResult != null;
		} catch (Exception e) {
			if (e.getMessage().toString().indexOf("StartHour") != -1 || e.getMessage().toString().indexOf("StartDay") != -1) {
				Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_StartTimeNotEqualEndTime"));
			} else {
				Application.getActiveApplication().getOutput().output(e);
			}
		} finally {
			SolarRadiation.removeSteppedListener(steppedListener);
		}
		return isSuccessful;
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public String getKey() {
		return MetaKeys.SOLAR_RADIATION;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_SolarRadiation");
	}
}
