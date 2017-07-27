package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.typeConversion;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.EqualDatasetConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.process.parameters.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.RecordsetUtilities;

import java.util.Map;

public class MetaProcessTabularToPoint extends MetaProcessTypeConversion {
    private static final String INPUT_DATA = "InputData";
    private static final String OUTPUT_DATA = "TabularToPointResult";

    private ParameterDatasourceConstrained inputDatasource;
    private ParameterSingleDataset inputDataset;
    private ParameterSaveDataset outputData;
    private ParameterFieldComboBox comboBoxX;
    private ParameterFieldComboBox comboBoxY;

    public MetaProcessTabularToPoint() {
        initParameters();
        initParameterConstraint();
    }

    private void initParameters() {
        inputDatasource = new ParameterDatasourceConstrained();
        inputDataset = new ParameterSingleDataset(DatasetType.POINT, DatasetType.LINE, DatasetType.REGION,
                DatasetType.TEXT,DatasetType.TABULAR,DatasetType.POINT3D,DatasetType.LINE3D,DatasetType.REGION3D,DatasetType.CAD);
        outputData = new ParameterSaveDataset();
        comboBoxX = new ParameterFieldComboBox(ProcessProperties.getString("String_Xcoordinate"));
        comboBoxY = new ParameterFieldComboBox(ProcessProperties.getString("String_Ycoordinate"));

        DatasetVector datasetVector = DatasetUtilities.getDefaultDatasetVector();
        if (datasetVector != null) {
            inputDatasource.setSelectedItem(datasetVector.getDatasource());
            inputDataset.setSelectedItem(datasetVector);
        }
        FieldType[] fieldType = {FieldType.INT16, FieldType.INT32, FieldType.INT64, FieldType.SINGLE, FieldType.DOUBLE};
        comboBoxX.setFieldType(fieldType);
        comboBoxY.setFieldType(fieldType);
        outputData.setSelectedItem("result_tabularToPoint");

        ParameterCombine inputCombine = new ParameterCombine();
        inputCombine.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
        inputCombine.addParameters(inputDatasource, inputDataset);
        ParameterCombine settingCombine = new ParameterCombine();
        settingCombine.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
        settingCombine.addParameters(comboBoxX,comboBoxY);
        ParameterCombine outputCombine = new ParameterCombine();
        outputCombine.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
        outputCombine.addParameters(outputData);

        DatasetTypes datasetTypes = new DatasetTypes("", DatasetTypes.POINT.getValue() |
                DatasetTypes.LINE.getValue() | DatasetTypes.REGION.getValue() |
                DatasetTypes.TEXT.getValue() | DatasetTypes.TABULAR.getValue() |
                DatasetTypes.POINT3D.getValue() | DatasetTypes.LINE3D.getValue() |
                DatasetTypes.REGION3D.getValue() | DatasetTypes.CAD.getValue());

        parameters.setParameters(inputCombine,settingCombine,outputCombine);
        parameters.addInputParameters(INPUT_DATA, datasetTypes, inputCombine);
        parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.POINT, outputCombine);
    }

    private void initParameterConstraint() {
        EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
        equalDatasourceConstraint.constrained(inputDatasource,ParameterDatasourceConstrained.DATASOURCE_FIELD_NAME);
        equalDatasourceConstraint.constrained(inputDataset,ParameterSingleDataset.DATASOURCE_FIELD_NAME);

        EqualDatasetConstraint equalDatasetConstraint = new EqualDatasetConstraint();
        equalDatasetConstraint.constrained(inputDataset, ParameterSingleDataset.DATASET_FIELD_NAME);
        equalDatasetConstraint.constrained(comboBoxX,ParameterFieldComboBox.DATASET_FIELD_NAME);
        equalDatasetConstraint.constrained(comboBoxY,ParameterFieldComboBox.DATASET_FIELD_NAME);
    }

    @Override
    public IParameters getParameters() {
        return parameters;
    }

    @Override
    public boolean execute() {
        boolean isSuccessful = false;
        Recordset recordsetResult = null;
        try {
            fireRunning(new RunningEvent(this,0,"start"));

            String filedX = null;
            String filedY = null;
            if (comboBoxX.getSelectedItem() != null && comboBoxY.getSelectedItem() != null) {
                filedX = comboBoxX.getFieldName();
                filedY = comboBoxY.getFieldName();
            } else {
                Application.getActiveApplication().getOutput().output("Coordinate is null");
                return false;
            }
            DatasetVector src = null;
            if (parameters.getInputs().getData(INPUT_DATA).getValue() != null) {
                src = (DatasetVector) parameters.getInputs().getData(INPUT_DATA).getValue();
            } else {
                src = (DatasetVector) inputDataset.getSelectedDataset();
            }
            DatasetVectorInfo datasetVectorInfo = new DatasetVectorInfo();
            datasetVectorInfo.setName(outputData.getResultDatasource().getDatasets().getAvailableDatasetName(outputData.getDatasetName()));
            datasetVectorInfo.setType(DatasetType.POINT);
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
                Map<String, Object> value = mergePropertyData(resultDataset, recordsetInput.getFieldInfos(), RecordsetUtilities.getFieldValuesIgnoreCase(recordsetInput));
                Point2D point2D = new Point2D(Double.valueOf(recordsetInput.getFieldValue(filedX).toString()), Double.valueOf(recordsetInput.getFieldValue(filedY).toString()));
                GeoPoint geoPoint = new GeoPoint(point2D);
                recordsetResult.addNew(geoPoint, value);
                geoPoint.dispose();
                recordsetInput.moveNext();
            }
            recordsetResult.getBatch().update();
            recordsetInput.close();
            recordsetInput.dispose();
            isSuccessful = resultDataset != null;
            this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(resultDataset);
            fireRunning(new RunningEvent(this,100,"finish"));
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

    @Override
    public String getKey() {
        return MetaKeys.Conversion_TabularToPoint;
    }

    @Override
    public String getTitle() {
        return ProcessProperties.getString("String_Title_TabularToPoint");
    }
}
