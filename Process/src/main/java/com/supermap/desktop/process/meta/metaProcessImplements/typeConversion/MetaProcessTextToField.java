package com.supermap.desktop.process.meta.metaProcessImplements.typeConversion;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.EqualDatasetConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.parameter.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

/**
 * Created by yuanR on 2017/7/26 0026.
 * 文本转字段
 */
public class MetaProcessTextToField extends MetaProcessTypeConversion {

	private final static String INPUT_DATA = CommonProperties.getString("String_GroupBox_SourceData");
	private final static String OUTPUT_DATA = "result_TextToField";

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset dataset;

	private ParameterFieldComboBox parameterFieldComboBox;

//	private ParameterSaveDataset saveDataset;

	public MetaProcessTextToField() {
		initParameters();
		initParameterConstraint();
		initParametersState();

	}

	private void initParameters() {
		sourceDatasource = new ParameterDatasourceConstrained();
		dataset = new ParameterSingleDataset(DatasetType.TEXT);

		parameterFieldComboBox = new ParameterFieldComboBox(ProcessProperties.getString("String_ComboBox_PendingTextField"));
		parameterFieldComboBox.setFieldType(new FieldType[]{FieldType.TEXT, FieldType.WTEXT});
		parameterFieldComboBox.setFieldName((DatasetVector) dataset.getSelectedItem());
		parameterFieldComboBox.setShowNullValue(false);
		parameterFieldComboBox.setRequisite(true);

//		this.saveDataset = new ParameterSaveDataset();

		// 源数据
		ParameterCombine parameterCombineSourceData = new ParameterCombine();
		parameterCombineSourceData.addParameters(sourceDatasource, dataset);
		parameterCombineSourceData.setDescribe(ControlsProperties.getString("String_GroupBox_SourceDataset"));

		//参数设置
		ParameterCombine parameterCombineSet = new ParameterCombine();
		parameterCombineSet.addParameters(parameterFieldComboBox);
		parameterCombineSet.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));

//		// 结果数据
//		ParameterCombine parameterCombineResultData = new ParameterCombine();
//		parameterCombineResultData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
//		parameterCombineResultData.addParameters(saveDataset);

		parameters.setParameters(parameterCombineSourceData, parameterCombineSet);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.TEXT, parameterCombineSourceData);
//		this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.TEXT, parameterCombineResultData);
	}

	private void initParameterConstraint() {

		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasetConstraint equalDatasetConstraint = new EqualDatasetConstraint();
		equalDatasetConstraint.constrained(dataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		equalDatasetConstraint.constrained(parameterFieldComboBox, ParameterFieldComboBox.DATASET_FIELD_NAME);


	}

	private void initParametersState() {
		Dataset defaultDataset = DatasetUtilities.getDefaultDataset(DatasetType.TEXT);
		if (defaultDataset != null) {
			sourceDatasource.setSelectedItem(defaultDataset.getDatasource());
			dataset.setSelectedItem(defaultDataset);
		}
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		Recordset recordsetInput = null;
		try {
			fireRunning(new RunningEvent(MetaProcessTextToField.this, 0, "start"));
			DatasetVector src = null;
			if (parameters.getInputs().getData(INPUT_DATA).getValue() != null) {
				src = (DatasetVector) parameters.getInputs().getData(INPUT_DATA).getValue();
			} else {
				src = (DatasetVector) dataset.getSelectedDataset();
			}

			// 首先判断操作的字段是原先有的还是需要新创建
			Boolean isAdd = true;
			String filedName = (String) parameterFieldComboBox.getSelectedItem();
			FieldInfos fieldInfos = src.getFieldInfos();
			for (int i = 0; i < fieldInfos.getCount(); i++) {
				if (fieldInfos.get(i).getName().equals(filedName)) {
					isAdd = false;
					break;
				}
			}
			if (isAdd) {
				FieldInfo newFieldInfo = new FieldInfo();
				newFieldInfo.setName(filedName);
				fieldInfos.add(newFieldInfo);
			}

			// 通过记录集获得其数据集的文本内容,并赋值
			recordsetInput = src.getRecordset(false, CursorType.DYNAMIC);
			recordsetInput.addSteppedListener(steppedListener);
			recordsetInput.getBatch().setMaxRecordCount(2000);
			recordsetInput.getBatch().begin();
			recordsetInput.moveFirst();
			while (!recordsetInput.isEOF()) {
				GeoText geoText = (GeoText) recordsetInput.getGeometry();
				String value = geoText.getText();
				isSuccessful = recordsetInput.setFieldValue(filedName, value);
				if (!isSuccessful) {
					break;
				}
				geoText.clone();
				recordsetInput.moveNext();
			}
			recordsetInput.getBatch().update();
			fireRunning(new RunningEvent(MetaProcessTextToField.this, 100, "finished"));

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (recordsetInput != null) {
				recordsetInput.removeSteppedListener(steppedListener);
				recordsetInput.close();
				recordsetInput.dispose();
			}
		}
		return isSuccessful;
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public String getKey() {
		return MetaKeys.CONVERSION_TEXT_TO_FILED;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_TextToField");
	}
}
