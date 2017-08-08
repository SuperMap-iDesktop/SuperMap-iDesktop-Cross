package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.typeConversion;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.EqualDatasetConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.ipls.*;
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
		if (inputType.equals(DatasetType.POINT)) {
			outputType = DatasetType.POINT3D;
			OUTPUT_DATA = "Point2Dto3DResult";
		} else if (inputType.equals(DatasetType.LINE)) {
			outputType = DatasetType.LINE3D;
			OUTPUT_DATA = "Line2Dto3DResult";
		} else if (inputType.equals(DatasetType.REGION)) {
			outputType = DatasetType.REGION3D;
			OUTPUT_DATA = "Region2Dto3DResult";
		}
		initParameters();
		initParameterConstraint();
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
			comboBoxZ.setFieldName((DatasetVector) dataset);
			comboBoxFrom.setFieldName((DatasetVector) dataset);
			comboBoxTo.setFieldName((DatasetVector) dataset);
		}
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
			GeoPoint3D geoPoint3D = new GeoPoint3D(geoPoint.getX(), geoPoint.getY(), Double.valueOf(zCoordinate.toString()));
			recordsetResult.addNew(geoPoint3D, value);
			geoPoint3D.dispose();
			geoPoint.dispose();
		} else if (geometry instanceof GeoRegion) {
			GeoRegion geoRegion = (GeoRegion) geometry;
			for (int i = 0; i < geoRegion.getPartCount(); i++) {
				Point2Ds point2Ds = geoRegion.getPart(i);
				Point3Ds point3Ds = new Point3Ds();
				for (int j = 0; j < point2Ds.getCount(); j++) {
					point3Ds.add(new Point3D(point2Ds.getItem(j).getX(), point2Ds.getItem(j).getY(), Double.valueOf(zCoordinate.toString())));
				}
				GeoRegion3D geoRegion3D = new GeoRegion3D(point3Ds);
				recordsetResult.addNew(geoRegion3D, value);
				geoRegion3D.dispose();
			}
			geoRegion.dispose();
		}
		return isConvert;
	}

	private boolean convert(Recordset recordsetResult, Geometry geometry, Map<String, Object> value,Object fromCoordinate,Object toCoordinate) {
		boolean isConvert = true;
		if (geometry instanceof GeoLine) {
			GeoLine geoLine = (GeoLine) geometry;
			for (int i = 0; i < geoLine.getPartCount(); i++) {
				Point2Ds point2Ds = geoLine.getPart(i);
				double increment = (Double.valueOf(fromCoordinate.toString()) - Double.valueOf(toCoordinate.toString())) / point2Ds.getCount();
				Point3Ds point3Ds = new Point3Ds();
				for (int j = 0; j < point2Ds.getCount(); j++) {
					point3Ds.add(new Point3D(point2Ds.getItem(j).getX(), point2Ds.getItem(j).getY(), (Double.valueOf(fromCoordinate.toString()) + increment * j)));
				}
				GeoLine3D geoLine3D = new GeoLine3D(point3Ds);
				recordsetResult.addNew(geoLine3D, value);
				geoLine3D.dispose();
			}
			geoLine.dispose();
		} else {
			isConvert = false;
		}
		return isConvert;
	}
}
