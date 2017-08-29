package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.typeConversion;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.ipls.ParameterCombine;
import com.supermap.desktop.process.parameter.ipls.ParameterDatasourceConstrained;
import com.supermap.desktop.process.parameter.ipls.ParameterSaveDataset;
import com.supermap.desktop.process.parameter.ipls.ParameterSingleDataset;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.RecordsetUtilities;

import java.util.HashMap;
import java.util.Map;

/**
 * Created By Chens on 2017/8/28 0028
 */
public abstract class MetaProcess3DTo2D extends MetaProcessTypeConversion {
	private DatasetType inputType;
	private DatasetType outputType;

	public MetaProcess3DTo2D(DatasetType inputType, DatasetType outputType) {
		this.inputType = inputType;
		this.outputType = outputType;

		initHook();
		initParameters();
		initParameterConstraint();
	}

	protected void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(inputDatasource, ParameterDatasourceConstrained.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(inputDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
	}

	protected void initParameters() {
		inputDatasource = new ParameterDatasourceConstrained();
		inputDataset = new ParameterSingleDataset(inputType);
		outputData = new ParameterSaveDataset();
		outputData.setSelectedItem(getOutputName());

		Dataset dataset = DatasetUtilities.getDefaultDataset(inputType);
		if (dataset != null) {
			inputDatasource.setSelectedItem(dataset.getDatasource());
			inputDataset.setSelectedItem(dataset);
		}

		ParameterCombine inputCombine = new ParameterCombine();
		inputCombine.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		inputCombine.addParameters(inputDatasource, inputDataset);
		ParameterCombine outputCombine = new ParameterCombine();
		outputCombine.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		outputCombine.addParameters(outputData);

		parameters.setParameters(inputCombine, outputCombine);
		parameters.addInputParameters(INPUT_DATA, datasetTypeToTypes(inputType), inputCombine);
		if (outputType.equals(DatasetType.REGION)) {
			parameters.addOutputParameters(OUTPUT_DATA, ProcessOutputResultProperties.getString("String_Result_Region_Dataset"), datasetTypeToTypes(outputType), outputCombine);
		} else if (outputType.equals(DatasetType.LINE)) {
			parameters.addOutputParameters(OUTPUT_DATA,ProcessOutputResultProperties.getString("String_Result_Line_Dataset"), datasetTypeToTypes(outputType), outputCombine);
		} else if (outputType.equals(DatasetType.POINT)) {
			parameters.addOutputParameters(OUTPUT_DATA, ProcessOutputResultProperties.getString("String_Result_Point_Dataset"), datasetTypeToTypes(outputType), outputCombine);
		}
	}

	protected abstract void initHook();

	protected abstract String getOutputName();

	protected abstract String getOutputResultName();

	protected HashMap<String, Object> mergePropertyData(DatasetVector des, FieldInfos srcFieldInfos, Map<String, Object> properties,double zValue) {
		HashMap<String, Object> results = new HashMap<>();
		FieldInfos desFieldInfos = des.getFieldInfos();

		for (int i = 0; i < desFieldInfos.getCount(); i++) {
			FieldInfo desFieldInfo = desFieldInfos.get(i);

			if (!desFieldInfo.isSystemField() && properties.containsKey(desFieldInfo.getName().toLowerCase())) {
				FieldInfo srcFieldInfo = srcFieldInfos.get(desFieldInfo.getName());

				if (desFieldInfo.getType() == srcFieldInfo.getType()) {
					// 如果要源字段和目标字段类型一致，直接保存
					results.put(desFieldInfo.getName(), properties.get(desFieldInfo.getName().toLowerCase()));
				} else if (desFieldInfo.getType() == FieldType.WTEXT || desFieldInfo.getType() == FieldType.TEXT) {

					// 如果目标字段与源字段类型不一致，则只有目标字段是文本型字段时，将源字段值做 toString 处理
					results.put(desFieldInfo.getName(), properties.get(desFieldInfo.getName().toLowerCase()).toString());
				}
			}
		}
		results.put("zValue", zValue);
		return results;
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		Recordset recordsetResult = null;
		try {
			fireRunning(new RunningEvent(this, 0, "start"));

			DatasetVector src = null;
			if (parameters.getInputs().getData(INPUT_DATA).getValue() != null) {
				src = (DatasetVector) parameters.getInputs().getData(INPUT_DATA).getValue();
			} else {
				src = (DatasetVector) inputDataset.getSelectedDataset();
			}

			DatasetVectorInfo datasetVectorInfo = new DatasetVectorInfo();
			datasetVectorInfo.setName(outputData.getResultDatasource().getDatasets().getAvailableDatasetName(outputData.getDatasetName()));
			datasetVectorInfo.setType(outputType);
			DatasetVector resultDataset = outputData.getResultDatasource().getDatasets().create(datasetVectorInfo);

			resultDataset.setPrjCoordSys(src.getPrjCoordSys());
			for (int i = 0; i < src.getFieldInfos().getCount(); i++) {
				FieldInfo fieldInfo = src.getFieldInfos().get(i);
				if (!fieldInfo.isSystemField() && !fieldInfo.getName().toLowerCase().equals("smuserid")) {
					resultDataset.getFieldInfos().add(fieldInfo);
				}
			}
			FieldInfo fieldInfoZValue = new FieldInfo();
			fieldInfoZValue.setName("zValue");
			fieldInfoZValue.setCaption("zValue");
			fieldInfoZValue.setDefaultValue("0");
			fieldInfoZValue.setType(FieldType.DOUBLE);
			fieldInfoZValue.setRequired(true);
			resultDataset.getFieldInfos().add(fieldInfoZValue);
			fieldInfoZValue.dispose();

			recordsetResult = resultDataset.getRecordset(false, CursorType.DYNAMIC);
			recordsetResult.addSteppedListener(steppedListener);

			recordsetResult.getBatch().setMaxRecordCount(2000);
			recordsetResult.getBatch().begin();

			Recordset recordsetInput = src.getRecordset(false, CursorType.DYNAMIC);
			while (!recordsetInput.isEOF()) {
				IGeometry geometry = null;
				try {
					geometry = DGeometryFactory.create(recordsetInput.getGeometry());
					convert(resultDataset, recordsetInput.getFieldInfos(), RecordsetUtilities.getFieldValuesIgnoreCase(recordsetInput),recordsetResult, geometry);
				} finally {
					if (geometry != null) {
						geometry.dispose();
					}
				}
				recordsetInput.moveNext();
			}
			recordsetResult.getBatch().update();
			isSuccessful = recordsetResult != null;
			recordsetInput.close();
			recordsetInput.dispose();
			if (isSuccessful) {
				this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(resultDataset);
			} else {
				outputData.getResultDatasource().getDatasets().delete(resultDataset.getName());
			}
			fireRunning(new RunningEvent(this, 100, "finish"));
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (recordsetResult != null) {
				recordsetResult.removeSteppedListener(steppedListener);
				recordsetResult.close();
				recordsetResult.dispose();
			}
		}

		return isSuccessful;
	}

	protected abstract void convert(DatasetVector resultDataset, FieldInfos fieldInfos, Map<String, Object> fieldValuesIgnoreCase, Recordset recordsetResult, IGeometry geometry);
}
