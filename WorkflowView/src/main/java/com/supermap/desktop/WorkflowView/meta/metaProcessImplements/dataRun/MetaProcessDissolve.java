package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.dataRun;

import com.supermap.analyst.spatialanalyst.DissolveParameter;
import com.supermap.analyst.spatialanalyst.DissolveType;
import com.supermap.analyst.spatialanalyst.Generalization;
import com.supermap.analyst.spatialanalyst.StatisticsType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.FieldInfo;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.DatasourceConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasetConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

/**
 * Created by lixiaoyao on 2017/8/8.
 */
public class MetaProcessDissolve extends MetaProcess {
	private static final String INPUT_DATA = CommonProperties.getString("String_GroupBox_SourceData");
	private final static String OUTPUT_DATA = "DissolveResult";
	private static final double STANDARD_NUMBER = 1000000.0;
	private DecimalFormat decimalFormat = new DecimalFormat("###############0.00000000#");

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset sourceDataset;
	private ParameterComboBox comboBoxDissolveMode;
	private ParameterNumber numberDissolveTolerance;
	private ParameterTextField textAreaSQLExpression;
	private ParameterSQLExpression textSQLExpression;
	private ParameterCheckBox checkBoxIsNullValue;
	private ParameterFieldGroup fieldsDissolve;
	private ParameterSimpleStatisticsFieldGroup statisticsFieldGroup;
	private ParameterSaveDataset resultDataset;


	public MetaProcessDissolve() {
		initParameters();
		initParameterConstraint();
		initParametersState();
		registerListener();
	}

	private void initParameters() {
		this.sourceDatasource = new ParameterDatasourceConstrained();
		this.sourceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		this.sourceDataset = new ParameterSingleDataset(DatasetType.LINE, DatasetType.REGION);
		this.sourceDataset.setDescribe(CommonProperties.getString("String_Label_Dataset"));
		this.comboBoxDissolveMode = new ParameterComboBox();
		this.comboBoxDissolveMode.setDescribe(ProcessProperties.getString("String_DissolveMode"));
		this.numberDissolveTolerance = new ParameterNumber();
		this.numberDissolveTolerance.setDescribe(ProcessProperties.getString("String_DissolveTolerance"));
		this.textAreaSQLExpression = new ParameterTextField();
		this.textAreaSQLExpression.setDescribe(ControlsProperties.getString("String_LabelFilter"));
		this.textSQLExpression = new ParameterSQLExpression();
		this.textSQLExpression.setDescribe(ControlsProperties.getString("String_SuspensionPoints"));
		this.checkBoxIsNullValue = new ParameterCheckBox();
		this.checkBoxIsNullValue.setDescribe(ProcessProperties.getString("String_IsNullValue"));
		this.fieldsDissolve = new ParameterFieldGroup(ProcessProperties.getString("String_DissolveFields"));
		this.statisticsFieldGroup = new ParameterSimpleStatisticsFieldGroup(ProcessProperties.getString("String_StatisticsField"));
		this.resultDataset = new ParameterSaveDataset();
		this.resultDataset.setDatasourceDescribe(CommonProperties.getString("String_TargetDatasource"));
		this.resultDataset.setDatasetDescribe(CommonProperties.getString("String_TargetDataset"));

		ParameterCombine sourceData = new ParameterCombine();
		sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		sourceData.addParameters(this.sourceDatasource, this.sourceDataset);

		ParameterCombine parameterCombineParent = new ParameterCombine(ParameterCombine.HORIZONTAL);
		parameterCombineParent.addParameters(this.textAreaSQLExpression, this.textSQLExpression);
		parameterCombineParent.setWeightIndex(0);
		this.textSQLExpression.setAnchor(GridBagConstraints.EAST);
		ParameterCombine parameterSetting = new ParameterCombine();
		parameterSetting.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
		parameterSetting.addParameters(this.comboBoxDissolveMode, this.numberDissolveTolerance, parameterCombineParent, this.checkBoxIsNullValue, this.fieldsDissolve, this.statisticsFieldGroup);

		ParameterCombine targetData = new ParameterCombine();
		targetData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		targetData.addParameters(this.resultDataset);

		this.parameters.setParameters(sourceData, parameterSetting, targetData);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.LINE_POLYGON_VECTOR, sourceData);
		this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.LINE_POLYGON_VECTOR, targetData);

	}

	private void initParametersState() {
		Dataset dataset = DatasetUtilities.getDefaultDataset(DatasetType.LINE, DatasetType.REGION);
		this.numberDissolveTolerance.setSelectedItem(0.00001);
		if (dataset != null) {
			this.sourceDatasource.setSelectedItem(dataset.getDatasource());
			this.sourceDataset.setSelectedItem(dataset);
			this.resultDataset.setResultDatasource(dataset.getDatasource());
			this.resultDataset.setSelectedItem(dataset.getDatasource().getDatasets().getAvailableDatasetName("result_Dissolve"));
			this.numberDissolveTolerance.setUnit(dataset.getPrjCoordSys().getCoordUnit().toString());
			//BigDecimal temp = new BigDecimal(Double.valueOf(DatasetUtilities.getDefaultTolerance((DatasetVector)dataset).getNodeSnap()));
			this.numberDissolveTolerance.setSelectedItem(DatasetUtilities.getDefaultTolerance((DatasetVector) dataset).getNodeSnap());
			this.fieldsDissolve.setDataset((DatasetVector) dataset);
			this.statisticsFieldGroup.setDataset((DatasetVector) dataset);
			this.textSQLExpression.setSelectDataset(dataset);
		}

		ParameterDataNode parameterDataNodeOnlyMultipart = new ParameterDataNode(ProcessProperties.getString("String_Dissolve_Mode_OnlyMultiPart"), DissolveType.ONLYMULTIPART);
		ParameterDataNode parameterDataNodeOnlySingle = new ParameterDataNode(ProcessProperties.getString("String_Dissolve_Mode_Single"), DissolveType.SINGLE);
		ParameterDataNode parameterDataNodeMultipart = new ParameterDataNode(ProcessProperties.getString("String_Dissolve_Mode_MultiPart"), DissolveType.MULTIPART);
		this.comboBoxDissolveMode.setItems(parameterDataNodeOnlyMultipart, parameterDataNodeOnlySingle, parameterDataNodeMultipart);
		this.comboBoxDissolveMode.setSelectedItem(parameterDataNodeOnlySingle);
		this.comboBoxDissolveMode.setRequisite(true);
		this.numberDissolveTolerance.setMinValue(0);
		this.numberDissolveTolerance.setIsIncludeMin(true);
		this.numberDissolveTolerance.setRequisite(true);
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint constraintSource = new EqualDatasourceConstraint();
		constraintSource.constrained(sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		constraintSource.constrained(sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasetConstraint equalDatasetConstraint = new EqualDatasetConstraint();
		equalDatasetConstraint.constrained(sourceDataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		equalDatasetConstraint.constrained(fieldsDissolve, ParameterFieldGroup.FIELD_DATASET);
		equalDatasetConstraint.constrained(statisticsFieldGroup, ParameterSimpleStatisticsFieldGroup.FIELD_DATASET);
		equalDatasetConstraint.constrained(textSQLExpression, ParameterSQLExpression.DATASET_FIELD_NAME);

		DatasourceConstraint.getInstance().constrained(resultDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
	}

	private void registerListener() {
		this.sourceDataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (sourceDataset.getSelectedItem() != null && evt.getNewValue() instanceof Dataset) {
					numberDissolveTolerance.setSelectedItem(DatasetUtilities.getDefaultTolerance((DatasetVector) evt.getNewValue()).getNodeSnap());
				}
			}
		});

		this.textSQLExpression.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (null != evt.getNewValue()) {
					textAreaSQLExpression.setSelectedItem(textSQLExpression.getSelectedItem());
				}
			}
		});
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		try {
			fireRunning(new RunningEvent(MetaProcessDissolve.this, 0, "start"));
			DissolveParameter dissolveParameter = new DissolveParameter();

			String datasetName = resultDataset.getDatasetName();
			datasetName = resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(datasetName);
			DatasetVector src = null;
			if (this.getParameters().getInputs().getData(INPUT_DATA).getValue() != null) {
				src = (DatasetVector) this.getParameters().getInputs().getData(INPUT_DATA).getValue();
			} else {
				src = (DatasetVector) sourceDataset.getSelectedItem();
			}

			dissolveParameter.setDissolveType((DissolveType) this.comboBoxDissolveMode.getSelectedData());
			dissolveParameter.setTolerance(Double.valueOf(this.numberDissolveTolerance.getSelectedItem().toString()));
			dissolveParameter.setFilterString(this.textAreaSQLExpression.getSelectedItem().toString());
			if (this.checkBoxIsNullValue.getSelectedItem().equals("false")) {
				dissolveParameter.setNullValue(false);
			} else if (this.checkBoxIsNullValue.getSelectedItem().equals("true")) {
				dissolveParameter.setNullValue(true);
			}
			StatisticsType[] statisticsTypes = this.statisticsFieldGroup.getSelectedStatisticsType();
			String[] statisticsFieldNames = getFieldName(this.statisticsFieldGroup.getSelectedFields());
			String[] fieldNames = getFieldName(this.fieldsDissolve.getSelectedFields());

			dissolveParameter.setFieldNames(fieldNames);
			dissolveParameter.setStatisticsFieldNames(statisticsFieldNames);
			dissolveParameter.setStatisticsTypes(statisticsTypes);

			Generalization.addSteppedListener(steppedListener);
			DatasetVector result = Generalization.dissolve(src, this.resultDataset.getResultDatasource(), datasetName, dissolveParameter);
			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(result);
			isSuccessful = result != null;
			fireRunning(new RunningEvent(MetaProcessDissolve.this, 100, "finished"));

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			Generalization.removeSteppedListener(steppedListener);
		}

		return isSuccessful;
	}

	private String[] getFieldName(FieldInfo fieldInfo[]) {
		try {
			String[] fieldNames = new String[fieldInfo.length];
			for (int i = 0; i < fieldInfo.length; i++) {
				fieldNames[i] = fieldInfo[i].getName();
			}
			return fieldNames;
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
			return null;
		}
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public String getKey() {
		return MetaKeys.DISSOLVE;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Form_Dissolve");
	}
}
