package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.data.DatasetType;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.DatasourceConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;

/**
 * Created by lixiaoyao on 2017/7/26.
 */
public class MetaProcessFieldIndex extends MetaProcess {
	private final static String INPUT_DATA = CommonProperties.getString("String_GroupBox_SourceData");

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset sourceDataset;
	private ParameterCombine sourceData;

	private ParameterComboBox parameterComboBox;
	private ParameterRadioButton parameterRadioButton;
	private ParameterFieldSetDialog parameterFieldSetDialog;

	public MetaProcessFieldIndex() {
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
		sourceData = new ParameterCombine();
		sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		sourceData.addParameters(sourceDatasource, sourceDataset);

		parameterComboBox=new ParameterComboBox(ProcessProperties.getString("String_FieldIndex_Form"));
		parameterRadioButton=new ParameterRadioButton();
		ParameterDataNode parameterDataNodeCreate = new ParameterDataNode(ProcessProperties.getString("String_FieldIndex_Create"),null);
		ParameterDataNode parameterDataNodeEdit = new ParameterDataNode(ProcessProperties.getString("String_FieldIndex_Edit"), null);
		ParameterDataNode parameterDataNodeDelete = new ParameterDataNode(ProcessProperties.getString("String_FieldIndex_Delete"),null);
		parameterRadioButton.setItems(new ParameterDataNode[]{parameterDataNodeCreate, parameterDataNodeEdit,parameterDataNodeDelete});
		parameterRadioButton.setSelectedItem(parameterDataNodeCreate);
		parameterFieldSetDialog=new ParameterFieldSetDialog();
		parameterFieldSetDialog.setDescribe(CommonProperties.getString("String_FieldsSetting"));

		ParameterCombine paramSet = new ParameterCombine();
		paramSet.setDescribe(CommonProperties.getString("String_FormEdgeCount_Text"));
		paramSet.addParameters(parameterComboBox,parameterRadioButton,parameterFieldSetDialog);
		this.parameters.setParameters(sourceData, paramSet);

	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
	}

	private void initParametersState() {

	}
	private void registerListener() {

	}
	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		return isSuccessful;
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public String getKey() {
		return MetaKeys.FIELD_INDEX;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("string_checkbox_chckbxFieldIndex");
	}
}
