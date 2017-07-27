package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.typeConversion;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameters.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.RecordsetUtilities;

import java.util.Map;

public abstract class MetaProcessPointLineRegion extends MetaProcessTypeConversion{
    private static final String INPUT_DATA = "InputData";
    protected static String OUTPUT_DATA = "OutputData";

    private DatasetType inputType;
    private DatasetType outputType;

    private ParameterDatasourceConstrained inputDatasource;
    private ParameterSingleDataset inputDataset;
    private ParameterSaveDataset outputData;

    public MetaProcessPointLineRegion(DatasetType inputType, DatasetType outputType) {
        this.inputType = inputType;
        this.outputType = outputType;

        initHook();
        initParameters();
        initParameterConstraint();
    }

    protected abstract void initHook();

    protected abstract String getOutputName();

    private void initParameters() {
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
        parameters.addInputParameters(INPUT_DATA, datasetTypeToTypes(inputType),inputCombine);
        parameters.addOutputParameters(OUTPUT_DATA, datasetTypeToTypes(outputType),outputCombine);
    }

    private void initParameterConstraint() {
        EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
        equalDatasourceConstraint.constrained(inputDatasource,ParameterDatasourceConstrained.DATASOURCE_FIELD_NAME);
        equalDatasourceConstraint.constrained(inputDataset,ParameterSingleDataset.DATASOURCE_FIELD_NAME);
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

            Recordset recordsetInput = src.getRecordset(false, CursorType.DYNAMIC);
            while (!recordsetInput.isEOF()) {
                IGeometry geometry = null;
                try {
                    geometry = DGeometryFactory.create(recordsetInput.getGeometry());
                    Map<String, Object> value = mergePropertyData(resultDataset, recordsetInput.getFieldInfos(), RecordsetUtilities.getFieldValuesIgnoreCase(recordsetInput));
                    isSuccessful = convert(recordsetResult, geometry, value);
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

    protected abstract boolean convert(Recordset recordset, IGeometry geometry, Map<String, Object> value);
}
