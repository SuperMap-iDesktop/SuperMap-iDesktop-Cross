package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.typeConversion;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.ParameterCombine;
import com.supermap.desktop.process.parameter.ipls.ParameterDatasourceConstrained;
import com.supermap.desktop.process.parameter.ipls.ParameterSaveDataset;
import com.supermap.desktop.process.parameter.ipls.ParameterSingleDataset;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.RecordsetUtilities;

import java.util.Map;

/**
 * Created By Chens on 2017/7/26 0026
 */
public class MetaProcessEPSToSimple extends MetaProcessTypeConversion {
	public MetaProcessEPSToSimple() {
		initParameters();
		initParameterConstraint();
		initParameterState();
	}

	private void initParameters() {
		OUTPUT_DATA = "EPSToSimpleResult";
		inputDatasource = new ParameterDatasourceConstrained();
		inputDataset = new ParameterSingleDataset(DatasetType.POINTEPS, DatasetType.LINEEPS, DatasetType.REGIONEPS);
		outputData = new ParameterSaveDataset();

		ParameterCombine inputCombine = new ParameterCombine();
		inputCombine.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		inputCombine.addParameters(inputDatasource, inputDataset);
		ParameterCombine outputCombine = new ParameterCombine();
		outputCombine.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		outputCombine.addParameters(outputData);

		parameters.setParameters(inputCombine, outputCombine);
		parameters.addInputParameters(INPUT_DATA, new DatasetTypes("",
				DatasetTypes.POINTEPS.getValue() | DatasetTypes.LINEEPS.getValue() | DatasetTypes.REGIONEPS.getValue()), inputCombine);
		parameters.addOutputParameters(OUTPUT_DATA, ProcessOutputResultProperties.getString("String_Result_Simple_Dataset"), DatasetTypes.SIMPLE_VECTOR, outputCombine);
	}

	private void initParameterState() {
		Dataset dataset = DatasetUtilities.getDefaultDataset(DatasetType.POINTEPS, DatasetType.LINEEPS, DatasetType.REGIONEPS);
		if (dataset != null) {
			inputDatasource.setSelectedItem(dataset.getDatasource());
			inputDataset.setSelectedItem(dataset);
			if (dataset.getType().equals(DatasetType.POINTEPS)) {
				outputData.setSelectedItem("result_pointEPSToSimple");
			} else if (dataset.getType().equals(DatasetType.LINEEPS)) {
				outputData.setSelectedItem("result_lineEPSToSimple");
			} else if (dataset.getType().equals(DatasetType.REGIONEPS)) {
				outputData.setSelectedItem("result_regionEPSToSimple");
			}
		}
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(inputDatasource, ParameterDatasourceConstrained.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(inputDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public String getKey() {
		return MetaKeys.CONVERSION_EPS_TO_SIMPLE;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_EPSToSimple");
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
			if (src.getType().equals(DatasetType.POINTEPS)) {
				datasetVectorInfo.setType(DatasetType.POINT);
			} else if (src.getType().equals(DatasetType.LINEEPS)) {
				datasetVectorInfo.setType(DatasetType.LINE);
			} else if (src.getType().equals(DatasetType.REGIONEPS)) {
				datasetVectorInfo.setType(DatasetType.REGION);
			}
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

			Recordset recordsetInput = src.getRecordset(false, CursorType.DYNAMIC);
			while (!recordsetInput.isEOF()) {
				Geometry geometry = null;
				try {
					geometry = recordsetInput.getGeometry();
					Map<String, Object> value = mergePropertyData(resultDataset, recordsetInput.getFieldInfos(), RecordsetUtilities.getFieldValuesIgnoreCase(recordsetInput));
					convert(recordsetResult, geometry, value);
				} finally {
					if (geometry != null) {
						geometry.dispose();
					}
				}
				recordsetInput.moveNext();
			}
			recordsetResult.getBatch().update();
			recordsetInput.close();
			recordsetInput.dispose();
			isSuccessful = recordsetResult != null;
			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(resultDataset);
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

	private boolean convert(Recordset recordsetResult, Geometry geometry, Map<String, Object> value) {
		boolean isConvert = true;
		try {
			if (geometry instanceof GeoPointEPS) {
				GeoPointEPS geoPointEPS = (GeoPointEPS) geometry;
				for (int i = 0; i < geoPointEPS.getCount(); i++) {
					GeoPoint geoPoint = new GeoPoint(geoPointEPS.get(i).getX(), geoPointEPS.get(i).getY());
					recordsetResult.addNew(geoPoint, value);
					geoPoint.dispose();
				}
				geoPointEPS.dispose();
			} else if (geometry instanceof GeoLineEPS) {
				Point2Ds point2Ds = new Point2Ds();
				GeoLineEPS geoLineEPS = (GeoLineEPS) geometry;
				for (int i = 0; i < geoLineEPS.getCount(); i++) {
					PointEPS pointEPS = geoLineEPS.get(i);
					point2Ds.add(new Point2D(pointEPS.getX(), pointEPS.getY()));
				}
				GeoLine geoLine = new GeoLine(point2Ds);
				recordsetResult.addNew(geoLine, value);
				geoLine.dispose();
				geoLineEPS.dispose();
			} else if (geometry instanceof GeoRegionEPS) {
				Point2Ds point2Ds = new Point2Ds();
				GeoRegionEPS geoRegionEPS = (GeoRegionEPS) geometry;
				for (int i = 0; i < geoRegionEPS.getCount(); i++) {
					PointEPS pointEPS = geoRegionEPS.get(i);
					point2Ds.add(new Point2D(pointEPS.getX(), pointEPS.getY()));
				}
				GeoRegion geoRegion = new GeoRegion(point2Ds);
				recordsetResult.addNew(geoRegion, value);
				geoRegion.dispose();
				geoRegionEPS.dispose();
			} else {
				isConvert = false;
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
			isConvert = false;
		}

		return isConvert;
	}
}
