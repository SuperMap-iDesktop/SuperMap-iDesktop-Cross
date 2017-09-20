package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.typeConversion;

import com.supermap.data.CursorType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.DatasetVectorInfo;
import com.supermap.data.FieldInfo;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoPoint;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.EqualDatasetConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.ParameterCombine;
import com.supermap.desktop.process.parameter.ipls.ParameterDatasourceConstrained;
import com.supermap.desktop.process.parameter.ipls.ParameterFieldComboBox;
import com.supermap.desktop.process.parameter.ipls.ParameterSaveDataset;
import com.supermap.desktop.process.parameter.ipls.ParameterSingleDataset;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.RecordsetUtilities;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created By Chens on 2017/7/24 0024
 * 点数据集转换为线数据集
 */
public class MetaProcessPointToLine extends MetaProcessTypeConversion {
	private ParameterFieldComboBox comboBoxConnect;

	public MetaProcessPointToLine() {
		initParameters();
		initParameterConstraint();
	}

	private void initParameters() {
		OUTPUT_DATA = "PointToLineResult";
		inputDatasource = new ParameterDatasourceConstrained();
		inputDataset = new ParameterSingleDataset(DatasetType.POINT);
		outputData = new ParameterSaveDataset();
		comboBoxConnect = new ParameterFieldComboBox(ProcessProperties.getString("String_ConnectionField"));
		comboBoxConnect.setRequisite(true);

		Dataset dataset = DatasetUtilities.getDefaultDataset(DatasetType.POINT);
		if (dataset != null) {
			inputDatasource.setSelectedItem(dataset.getDatasource());
			inputDataset.setSelectedItem(dataset);
			comboBoxConnect.setFieldName((DatasetVector) dataset);
			outputData.setResultDatasource(dataset.getDatasource());
		}
		outputData.setDefaultDatasetName("result_PointToLine");

		ParameterCombine inputCombine = new ParameterCombine();
		inputCombine.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		inputCombine.addParameters(inputDatasource, inputDataset);
		ParameterCombine outputCombine = new ParameterCombine();
		outputCombine.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		outputCombine.addParameters(outputData);
		ParameterCombine settingCombine = new ParameterCombine();
		settingCombine.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
		settingCombine.addParameters(comboBoxConnect);

		parameters.setParameters(inputCombine, settingCombine, outputCombine);
		parameters.addInputParameters(INPUT_DATA, DatasetTypes.POINT, inputCombine);
		parameters.addOutputParameters(OUTPUT_DATA, ProcessOutputResultProperties.getString("String_Result_Line_Dataset"), DatasetTypes.LINE, outputCombine);
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(inputDatasource, ParameterDatasourceConstrained.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(inputDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasetConstraint equalDatasetConstraint = new EqualDatasetConstraint();
		equalDatasetConstraint.constrained(inputDataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		equalDatasetConstraint.constrained(comboBoxConnect, ParameterFieldComboBox.DATASET_FIELD_NAME);
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public String getKey() {
		return MetaKeys.CONVERSION_POINT_TO_LINE;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Title_PointToLine");
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;
		Recordset recordsetResult = null;

		try {
			DatasetVector src;
			if (parameters.getInputs().getData(INPUT_DATA).getValue() != null) {
				src = (DatasetVector) parameters.getInputs().getData(INPUT_DATA).getValue();
			} else {
				src = (DatasetVector) inputDataset.getSelectedDataset();
			}
			DatasetVectorInfo datasetVectorInfo = new DatasetVectorInfo();
			datasetVectorInfo.setName(outputData.getResultDatasource().getDatasets().getAvailableDatasetName(outputData.getDatasetName()));
			datasetVectorInfo.setType(DatasetType.LINE);
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

			String fieldName = comboBoxConnect.getFieldName();

			Recordset recordsetInput = src.getRecordset(false, CursorType.DYNAMIC);

			ArrayList<Point2Ds> point2DsArrayList = new ArrayList<>();
			ArrayList<Object> fieldValues = new ArrayList<>();
			ArrayList<Map<String, Object>> valueList = new ArrayList<>();
			while (!recordsetInput.isEOF()) {
				GeoPoint geoPoint = null;
				try {
					geoPoint = (GeoPoint) recordsetInput.getGeometry();
					Map<String, Object> value = mergePropertyData(resultDataset, recordsetInput.getFieldInfos(), RecordsetUtilities.getFieldValuesIgnoreCase(recordsetInput));
					Object currentFieldValue = recordsetInput.getFieldValue(fieldName);
					if (fieldValues.size() > 0) {
						for (int i = 0; i < fieldValues.size(); i++) {
							if (currentFieldValue == (fieldValues.get(i)) || currentFieldValue.equals(fieldValues.get(i))) {
								point2DsArrayList.get(i).add(new Point2D(geoPoint.getX(), geoPoint.getY()));
								break;
							} else {
								fieldValues.add(currentFieldValue);
								valueList.add(value);
								Point2Ds point2Ds = new Point2Ds();
								point2Ds.add(new Point2D(geoPoint.getX(), geoPoint.getY()));
								point2DsArrayList.add(point2Ds);
								break;
							}
						}
					} else {
						fieldValues.add(currentFieldValue);
						valueList.add(value);
						Point2Ds point2Ds = new Point2Ds();
						point2Ds.add(new Point2D(geoPoint.getX(), geoPoint.getY()));
						point2DsArrayList.add(point2Ds);
					}


				} finally {
					if (geoPoint != null) {
						geoPoint.dispose();
					}
				}
				recordsetInput.moveNext();
			}
			for (int i = 0; i < point2DsArrayList.size(); i++) {
				if (point2DsArrayList.get(i).getCount() > 1) {
					GeoLine geoLine = new GeoLine(point2DsArrayList.get(i));
					recordsetResult.addNew(geoLine, valueList.get(i));
					geoLine.dispose();
				}
			}
			recordsetResult.getBatch().update();
			recordsetInput.close();
			recordsetInput.dispose();
			isSuccessful = recordsetResult != null;
			if (isSuccessful) {
				this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(resultDataset);
			} else {
				outputData.getResultDatasource().getDatasets().delete(resultDataset.getName());
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
			e.printStackTrace();
		} finally {
			if (recordsetResult != null) {
				recordsetResult.removeSteppedListener(steppedListener);
				recordsetResult.close();
				recordsetResult.dispose();
			}
		}
		return isSuccessful;
	}
}
