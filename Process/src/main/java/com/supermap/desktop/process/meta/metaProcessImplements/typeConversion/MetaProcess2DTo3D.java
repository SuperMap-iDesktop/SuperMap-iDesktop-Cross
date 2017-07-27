package com.supermap.desktop.process.meta.metaProcessImplements.typeConversion;

import com.sun.xml.internal.bind.v2.TODO;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
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
import com.supermap.desktop.utilities.RecordsetUtilities;

import java.util.Map;

/**
 * Created By Chens on 2017/7/27 0027
 */
public class MetaProcess2DTo3D extends MetaProcessTypeConversion {
	private DatasetType inputType;
	private DatasetType outputType;

	private ParameterFieldComboBox comboBoxZ;
	private ParameterFieldComboBox comboBoxFrom;
	private ParameterFieldComboBox comboBoxTo;

	public MetaProcess2DTo3D(DatasetType inputType) {
		this.inputType = inputType;
		initParameters();
		initParameterConstraint();
		if (inputType.equals(DatasetType.POINT)) {
			outputType = DatasetType.POINT3D;
		} else if (inputType.equals(DatasetType.LINE)) {
			outputType = DatasetType.LINE3D;
		} else if (inputType.equals(DatasetType.REGION)) {
			outputType = DatasetType.REGION3D;
		}
	}

	private void initParameters() {
		inputDatasource = new ParameterDatasourceConstrained();
		inputDataset = new ParameterSingleDataset(inputType);
		outputData = new ParameterSaveDataset();
		comboBoxZ = new ParameterFieldComboBox(ProcessProperties.getString("String_Zcoordinate"));
		comboBoxFrom = new ParameterFieldComboBox(ProcessProperties.getString("String_FromZcoordinate"));
		comboBoxTo = new ParameterFieldComboBox(ProcessProperties.getString("String_ToZcoordinate"));

		Dataset dataset = DatasetUtilities.getDefaultDataset(DatasetType.POINT, DatasetType.LINE, DatasetType.REGION);
		if (dataset != null) {
			inputDatasource.setSelectedItem(dataset.getDatasource());
			inputDataset.setSelectedItem(dataset);
			comboBoxZ.setDataset((DatasetVector) dataset);
			comboBoxFrom.setDataset((DatasetVector) dataset);
			comboBoxTo.setDataset((DatasetVector) dataset);
		}
		FieldType[] fieldType = {FieldType.INT16, FieldType.INT32, FieldType.INT64, FieldType.SINGLE, FieldType.DOUBLE};
		comboBoxZ.setFieldType(fieldType);
		comboBoxFrom.setFieldType(fieldType);
		comboBoxTo.setFieldType(fieldType);

		ParameterCombine inputCombine = new ParameterCombine();
		inputCombine.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		inputCombine.addParameters(inputDatasource, inputDataset);
		ParameterCombine outputCombine = new ParameterCombine();
		outputCombine.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		outputCombine.addParameters(outputData);
		ParameterCombine settingCombine = new ParameterCombine();
		settingCombine.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));

		if (inputType.equals(DatasetType.POINT)) {
			outputData.setSelectedItem("result_point2DTo3D");
			settingCombine.addParameters(comboBoxZ);
		} else if (inputType.equals(DatasetType.LINE)) {
			outputData.setSelectedItem("result_line2DTo3D");
			settingCombine.addParameters(comboBoxFrom);
			settingCombine.addParameters(comboBoxTo);
		} else if (inputType.equals(DatasetType.REGION)) {
			outputData.setSelectedItem("result_region2DTo3D");
			settingCombine.addParameters(comboBoxZ);
		}
		parameters.setParameters(inputCombine,settingCombine,outputCombine);
		parameters.addInputParameters(INPUT_DATA, datasetTypeToTypes(inputType),inputCombine);
		parameters.addOutputParameters(OUTPUT_DATA, datasetTypeToTypes(outputType), outputCombine);
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(inputDatasource, ParameterDatasourceConstrained.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(inputDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasetConstraint equalDatasetConstraint = new EqualDatasetConstraint();
		equalDatasetConstraint.constrained(inputDataset,ParameterSingleDataset.DATASET_FIELD_NAME);
		equalDatasetConstraint.constrained(comboBoxZ,ParameterFieldComboBox.DATASET_FIELD_NAME);
		equalDatasetConstraint.constrained(comboBoxFrom,ParameterFieldComboBox.DATASET_FIELD_NAME);
		equalDatasetConstraint.constrained(comboBoxTo,ParameterFieldComboBox.DATASET_FIELD_NAME);
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public String getKey() {
		if (inputType.equals(DatasetType.POINT)) {
			return MetaKeys.CONVERSION_POINT2D_TO_3D;
		} else if (inputType.equals(DatasetType.LINE)) {
			return MetaKeys.CONVERSION_LINE2D_TO_3D;
		} else if (inputType.equals(DatasetType.REGION)) {
			return MetaKeys.CONVERSION_REGION2D_TO_3D;
		}
		return null;
	}

	@Override
	public String getTitle() {
		if (inputType.equals(DatasetType.POINT)) {
			return ProcessProperties.getString("String_Title_Point2DTo3D");
		} else if (inputType.equals(DatasetType.LINE)) {
			return ProcessProperties.getString("String_Title_Line2DTo3D");
		} else if (inputType.equals(DatasetType.REGION)) {
			return ProcessProperties.getString("String_Title_Region2DTo3D");
		}
		return null;
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		Recordset recordsetResult = null;
		try {
			fireRunning(new RunningEvent(this,0,"start"));

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

			recordsetResult = resultDataset.getRecordset(false, CursorType.DYNAMIC);
			recordsetResult.addSteppedListener(steppedListener);

			recordsetResult.getBatch().setMaxRecordCount(2000);
			recordsetResult.getBatch().begin();

			String zCoordinate = comboBoxZ.getFieldName();
			String fromCoordinate = comboBoxFrom.getFieldName();
			String toCoordinate = comboBoxTo.getFieldName();

			Recordset recordsetInput = src.getRecordset(false, CursorType.DYNAMIC);
			while (!recordsetInput.isEOF()) {
				Geometry geometry = null;
				try {
					geometry = recordsetInput.getGeometry();
					Map<String, Object> value = mergePropertyData(resultDataset, recordsetInput.getFieldInfos(), RecordsetUtilities.getFieldValuesIgnoreCase(recordsetInput));
					if (zCoordinate != null) {
						isSuccessful = convert(recordsetResult, geometry, value,recordsetInput.getFieldValue(zCoordinate));
					} else if (fromCoordinate != null && toCoordinate != null) {
						isSuccessful = convert(recordsetResult, geometry, value, recordsetInput.getFieldValue(fromCoordinate), recordsetInput.getFieldValue(toCoordinate));
					} else {
						Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_NullCoordinate_Error"));
						isSuccessful = false;
					}
				}finally {
					if (geometry != null) {
						geometry.dispose();
					}
				}
				recordsetInput.moveNext();
			}
			recordsetResult.getBatch().update();
			recordsetInput.close();
			recordsetInput.dispose();
			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(resultDataset);
			fireRunning(new RunningEvent(this,100,"finish"));
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}finally {
			if (recordsetResult != null) {
				recordsetResult.removeSteppedListener(steppedListener);
				recordsetResult.close();
				recordsetResult.dispose();
			}
		}

		return isSuccessful;
	}

	private boolean convert(Recordset recordsetResult, Geometry geometry, Map<String, Object> value,Object zCoordinate) {
		boolean isConvert = true;
		if (geometry instanceof GeoPoint) {
			GeoPoint geoPoint = (GeoPoint) geometry;
//			GeoPoint3D geoPoint3D=new GeoPoint3D()
		}
//TODO
		return isConvert;
	}

	private boolean convert(Recordset recordsetResult, Geometry geometry, Map<String, Object> value,Object fromCoordinate,Object toCoordinate) {
		//TODO
		return false;
	}
}
