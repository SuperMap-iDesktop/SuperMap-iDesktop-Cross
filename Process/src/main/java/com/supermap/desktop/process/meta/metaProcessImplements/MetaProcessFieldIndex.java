package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetTypeUtilities;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lixiaoyao on 2017/7/26.
 */
public class MetaProcessFieldIndex extends MetaProcess {
	private final static String INPUT_DATA = CommonProperties.getString("String_GroupBox_SourceData");

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset sourceDataset;
	private ParameterCombine sourceData;

//	private ParameterComboBox parameterComboBox;
	//private ParameterRadioButton parameterRadioButton;
	//private ParameterFieldSetDialog parameterFieldSetDialog;
	private ParameterTextField parameterTextField;
	private ParameterTextArea parameterTextAreaField;

	private Map<String,String> fieldIndexes=new HashMap<>();
	private ArrayList<String> indexs=new ArrayList<>();

	public MetaProcessFieldIndex() {
		initParameters();
		initParameterConstraint();
		initParametersState();
		registerListener();
	}

	private void initParameters() {
		sourceDatasource = new ParameterDatasourceConstrained();
		sourceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		sourceDataset = new ParameterSingleDataset(DatasetTypeUtilities.getDatasetTypeVector());
		sourceDataset.setDescribe(CommonProperties.getString("String_Label_Dataset"));
		sourceData = new ParameterCombine();
		sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		sourceData.addParameters(sourceDatasource, sourceDataset);

		//parameterComboBox = new ParameterComboBox(ProcessProperties.getString("String_FieldIndex_Form"));
		parameterTextField=new ParameterTextField(ProcessProperties.getString("String_FieldIndex_Form"));


//		parameterRadioButton = new ParameterRadioButton();
//		ParameterDataNode parameterDataNodeCreate = new ParameterDataNode(ProcessProperties.getString("String_FieldIndex_Create"), null);
//		ParameterDataNode parameterDataNodeEdit = new ParameterDataNode(ProcessProperties.getString("String_FieldIndex_Edit"), null);
//		ParameterDataNode parameterDataNodeDelete = new ParameterDataNode(ProcessProperties.getString("String_FieldIndex_Delete"), null);
//		parameterRadioButton.setItems(new ParameterDataNode[]{parameterDataNodeCreate, parameterDataNodeEdit, parameterDataNodeDelete});
//		parameterRadioButton.setSelectedItem(parameterDataNodeCreate);
//		parameterFieldSetDialog = new ParameterFieldSetDialog();
//		parameterFieldSetDialog.setDescribe(CommonProperties.getString("String_FieldsSetting"));
		parameterTextAreaField =new ParameterTextArea(ProcessProperties.getString("String_FieldIndex_FieldForIndex"));
//		ParameterCombine parameterCombineParent = new ParameterCombine(ParameterCombine.HORIZONTAL).addParameters(parameterComboBox,parameterFieldSetDialog);
//		parameterCombineParent.setWeightIndex(0);

		ParameterCombine paramSet = new ParameterCombine();
		paramSet.setDescribe(CommonProperties.getString("String_FormEdgeCount_Text"));
		paramSet.addParameters(parameterTextField,parameterTextAreaField);

		this.parameters.setParameters(sourceData, paramSet);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.VECTOR, sourceData);
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(sourceDatasource, ParameterDatasourceConstrained.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
	}

	private void initParametersState() {
		Dataset dataset = DatasetUtilities.getDefaultDatasetVector();
		if (dataset != null) {
			this.sourceDatasource.setSelectedItem(dataset.getDatasource());
			this.sourceDataset.setSelectedItem(dataset);
		}
		this.parameterTextAreaField.setLineWrap(true);
		this.parameterTextAreaField.setWrapStyleWord(true);
//		this.parameterComboBox.setEditable(true);
		reloadIndexField(dataset);

	}

	private void registerListener() {
		sourceDataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (sourceDataset.getSelectedItem() !=null) {
					reloadIndexField((Dataset)sourceDataset.getSelectedItem());
				}
			}
		});

//		parameterComboBox.addPropertyListener(new PropertyChangeListener() {
//			@Override
//			public void propertyChange(PropertyChangeEvent evt) {
//				showField();
//			}
//		});
	}

	private void reloadIndexField(Dataset dataset){

		this.parameterTextField.setSelectedItem(dataset.getName()+"_FieldIndex");
		FieldInfos fieldInfos= ((DatasetVector)dataset).getFieldInfos();
		String fields="";
		for (int i=0;i<fieldInfos.getCount();i++){
			if (!fieldInfos.get(i).isSystemField()) {
				fields = fields + fieldInfos.get(i).getName()+",";
			}
		}
		if (fields.substring(fields.length()-1,fields.length()).equals(",")){
			fields=fields.substring(0,fields.length()-1);
		}
		this.parameterTextAreaField.setSelectedItem(fields);
//		if (this.sourceDataset.getSelectedDataset()!=null){
//			this.fieldIndexes=((DatasetVector)this.sourceDataset.getSelectedDataset()).getFieldIndexes();
//		}
//		if (this.fieldIndexes!=null && this.fieldIndexes.size()>0){
//			String[] index= new String[this.fieldIndexes.size()];
//			this.fieldIndexes.keySet().toArray(index);
//			this.indexs.clear();
//			this.parameterComboBox.removeAllItems();
//			for (int i=0;i<index.length;i++){
//				this.parameterComboBox.addItem(new ParameterDataNode(index[i],index[i]));
//				this.indexs.add(index[i]);
//			}
//			this.parameterTextAreaField.setSelectedItem(this.fieldIndexes.get(index[0]));
//		}
	}
//
//	private void showField(){
//		String currentIndex=(String) this.parameterComboBox.getSelectedItem();
//		if (!StringUtilities.isNullOrEmpty(currentIndex)){
//			if (this.indexs.indexOf(currentIndex)!=-1){
//				this.parameterTextAreaField.setSelectedItem(this.fieldIndexes.get(currentIndex));
//			}
//		}
//	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		try {
			fireRunning(new RunningEvent(MetaProcessFieldIndex.this, 0, "start"));
			String fields=this.parameterTextAreaField.getSelectedItem().toString();
			String[] fieldIndex;
			if (fields.contains(",")) {
				fieldIndex = fields.split(",");
			}else{
				fieldIndex=new String[]{fields};
			}
			isSuccessful=((DatasetVector)sourceDataset.getSelectedDataset()).buildFieldIndex(fieldIndex,this.parameterTextField.getSelectedItem().toString());
			fireRunning(new RunningEvent(MetaProcessFieldIndex.this, 100, "finished"));
		}catch (Exception e){
			Application.getActiveApplication().getOutput().output(e);
		}
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
