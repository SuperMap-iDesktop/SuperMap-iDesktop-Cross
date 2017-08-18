package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.dataRun;

import com.supermap.analyst.spatialanalyst.EdgeMatchMode;
import com.supermap.analyst.spatialanalyst.EdgeMatchParameter;
import com.supermap.analyst.spatialanalyst.Generalization;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by yuanR on 2017/7/22  .
 * 图幅接边
 */
public class MetaProcessEdgeMatch extends MetaProcess {

	private final static String INPUT_SOURCE_DATASET = CommonProperties.getString("String_GroupBox_SourceData");
	private final static String OUTPUT_DATA = "EdgeMatchResult";

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset sourceDataset;

	private ParameterDatasourceConstrained targetDatasource;
	private ParameterSingleDataset targetDataset;

	private ParameterComboBox edgeMatchMode;
	private ParameterNumber edgeTolerance;
	private ParameterCheckBox union;

	private ParameterDatasourceConstrained linkDatasource;
	private ParameterTextField linkDatasetName;
	private ParameterCheckBox isLinkDataset;


	public MetaProcessEdgeMatch() {

		initParameters();
		initComponentState();
		initParameterConstraint();
	}


	private void initParameters() {
		sourceDatasource = new ParameterDatasourceConstrained();
		sourceDataset = new ParameterSingleDataset(DatasetType.LINE);

		targetDatasource = new ParameterDatasourceConstrained();
		targetDataset = new ParameterSingleDataset(DatasetType.LINE);
		targetDatasource.setRequisite(true);
		targetDataset.setRequisite(true);

		edgeMatchMode = new ParameterComboBox(ProcessProperties.getString("String_EdgeMatchMode"));
		edgeMatchMode.addItem(new ParameterDataNode(ProcessProperties.getString("String_EdgeMatchMode_THEOTHEREDGE"), EdgeMatchMode.THEOTHEREDGE));
		edgeMatchMode.addItem(new ParameterDataNode(ProcessProperties.getString("String_EdgeMatchMode_THEMIDPOINT"), EdgeMatchMode.THEMIDPOINT));
		// 暂时不支持交点位置接边
//		edgeMatchMode.addItem(new ParameterDataNode(ProcessProperties.getString("String_EdgeMatchMode_THEINTERSECTION"), EdgeMatchMode.THEINTERSECTION));
		edgeMatchMode.setRequisite(true);

		edgeTolerance = new ParameterNumber(ProcessProperties.getString("String_EdgeMatchTolerance"));
		edgeTolerance.setMaxBit(22);
		edgeTolerance.setMinValue(0);
		edgeTolerance.setIsIncludeMin(false);
		edgeTolerance.setRequisite(true);

		union = new ParameterCheckBox(ProcessProperties.getString("String_EdgeMatchUnion"));

		linkDatasource = new ParameterDatasourceConstrained();
		linkDatasetName = new ParameterTextField(ControlsProperties.getString("String_Label_ResultDataset"));
		isLinkDataset = new ParameterCheckBox(ProcessProperties.getString("String_EdgeMatch_OutputDatasetLink"));

		// 源数据
		ParameterCombine parameterCombineSourceData = new ParameterCombine();
		parameterCombineSourceData.addParameters(sourceDatasource, sourceDataset);
		parameterCombineSourceData.setDescribe(ControlsProperties.getString("String_GroupBox_SourceDataset"));
		// 目标数据
		ParameterCombine parameterCombineTargetData = new ParameterCombine();
		parameterCombineTargetData.addParameters(targetDatasource, targetDataset);
		parameterCombineTargetData.setDescribe(ControlsProperties.getString("String_GroupBox_TargetDataset"));
		//参数设置
		ParameterCombine parameterCombine = new ParameterCombine(ParameterCombine.HORIZONTAL);
		parameterCombine.addParameters(union, isLinkDataset);

		ParameterCombine parameterCombineParameter = new ParameterCombine();
		parameterCombineParameter.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
		parameterCombineParameter.addParameters(edgeMatchMode, edgeTolerance, parameterCombine);
		//接边关联数据
		ParameterCombine parameterCombineLinkData = new ParameterCombine();
		parameterCombineLinkData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		parameterCombineLinkData.addParameters(linkDatasource, linkDatasetName);

		parameters.setParameters(parameterCombineSourceData, parameterCombineTargetData, parameterCombineParameter, parameterCombineLinkData);
		this.parameters.addInputParameters(INPUT_SOURCE_DATASET, DatasetTypes.LINE, parameterCombineSourceData);
		this.parameters.addOutputParameters(OUTPUT_DATA, ProcessOutputResultProperties.getString("String_Result_EdgeMatch"), DatasetTypes.LINE, parameterCombineSourceData);
	}

	private void initComponentState() {

		Dataset datasetVector = DatasetUtilities.getDefaultDataset(DatasetType.LINE);
		if (datasetVector != null) {
			sourceDatasource.setSelectedItem(datasetVector.getDatasource());
			sourceDataset.setSelectedItem(datasetVector);
			linkDatasource.setSelectedItem(datasetVector.getDatasource());
			targetDataset.setSelectedItem(datasetVector);
		}
		edgeMatchMode.setSelectedItem(EdgeMatchMode.THEOTHEREDGE);
		edgeTolerance.setSelectedItem(0.00001);
		linkDatasetName.setSelectedItem("LinkDataset");

		linkDatasource.setEnabled(false);
		linkDatasetName.setEnabled(false);
	}

	private void initParameterConstraint() {

		EqualDatasourceConstraint equalSourceDatasourceConstraint = new EqualDatasourceConstraint();
		equalSourceDatasourceConstraint.constrained(sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalSourceDatasourceConstraint.constrained(sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint equalTargetDatasourceConstraint = new EqualDatasourceConstraint();
		equalTargetDatasourceConstraint.constrained(targetDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalTargetDatasourceConstraint.constrained(targetDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		isLinkDataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterCheckBox.PARAMETER_CHECK_BOX_VALUE)) {
					linkDatasource.setEnabled((boolean) evt.getNewValue());
					linkDatasetName.setEnabled((boolean) evt.getNewValue());
				}
			}
		});
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		try {
			DatasetVector sourceDataset = null;
			if (this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET) != null
					&& this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET).getValue() instanceof DatasetVector) {
				sourceDataset = (DatasetVector) this.getParameters().getInputs().getData(INPUT_SOURCE_DATASET).getValue();
			} else {
				sourceDataset = (DatasetVector) this.sourceDataset.getSelectedItem();
			}

			DatasetVector targetDataset = null;
			targetDataset = (DatasetVector) this.targetDataset.getSelectedItem();

			EdgeMatchParameter edgeMatchParameter = new EdgeMatchParameter();
			edgeMatchParameter.setEdgeMatchMode((EdgeMatchMode) edgeMatchMode.getSelectedData());
			edgeMatchParameter.setTolerance(Double.valueOf((String) edgeTolerance.getSelectedItem()));
			edgeMatchParameter.setUnion("true".equalsIgnoreCase((String) union.getSelectedItem()));

			Boolean isLinkData = "true".equalsIgnoreCase((String) isLinkDataset.getSelectedItem());
			if (isLinkData) {
				edgeMatchParameter.setOutputDatasource((Datasource) linkDatasource.getSelectedItem());
				edgeMatchParameter.setOutputDatasetLinkName((String) linkDatasetName.getSelectedItem());
			}

			Generalization.addSteppedListener(this.steppedListener);
			isSuccessful = Generalization.edgeMatch(sourceDataset, targetDataset, edgeMatchParameter);
			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(targetDataset);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			Generalization.removeSteppedListener(this.steppedListener);
		}
		return isSuccessful;
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_EdgeMatch");
	}

	@Override
	public String getKey() {
		return MetaKeys.EDGE_MATCH;
	}
}
