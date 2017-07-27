package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.ProximityAnalyst;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.parameters.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

/**
 * Created by Chen on 2017/7/3 0003.
 */
public class MetaProcessThiessenPolygon extends MetaProcess {
    private static final String INPUT_DATA = "input_data";
    private static final String OUTPUT_DATA = "output_data";

    private ParameterDatasourceConstrained sourceDatasource;
    private ParameterSingleDataset sourceDataset;
    private ParameterSaveDataset resultDataset;

    public MetaProcessThiessenPolygon() {
        initParameters();
        initParameterConstraint();
        initParametersState();
    }

    private void initParameters() {
        sourceDatasource = new ParameterDatasourceConstrained();
        sourceDatasource.setDescribe(CommonProperties.getString("String_Label_Datasource"));
        sourceDataset = new ParameterSingleDataset(DatasetType.POINT);
        sourceDataset.setDescribe(CommonProperties.getString("String_Label_Dataset"));
        ParameterCombine sourceData = new ParameterCombine();
        sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
        sourceData.addParameters(sourceDatasource, sourceDataset);

        resultDataset = new ParameterSaveDataset();
        this.resultDataset.setDatasourceDescribe(CommonProperties.getString("String_TargetDatasource"));
        this.resultDataset.setDatasetDescribe(CommonProperties.getString("String_TargetDataset"));
        ParameterCombine resultData = new ParameterCombine();
        resultData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
        resultData.addParameters(resultDataset);

        parameters.setParameters(sourceData,resultData);
        parameters.addInputParameters(INPUT_DATA, DatasetTypes.POINT, sourceData);
        parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.REGION, resultData);
    }

    private void initParameterConstraint() {
        EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
        equalDatasourceConstraint.constrained(sourceDatasource,ParameterDatasourceConstrained.DATASOURCE_FIELD_NAME);
        equalDatasourceConstraint.constrained(sourceDataset,ParameterSingleDataset.DATASOURCE_FIELD_NAME);
    }

    private void initParametersState() {
        Dataset datasetVector = DatasetUtilities.getDefaultDataset(DatasetType.POINT);
        if (datasetVector != null) {
            sourceDatasource.setSelectedItem(datasetVector.getDatasource());
            sourceDataset.setSelectedItem(datasetVector);
        }
        resultDataset.setDatasetName("result_thiessen");
    }

    @Override
    public boolean execute() {
        boolean isSuccessful = false;

        try {
            fireRunning(new RunningEvent(this,0,"start"));
            ProximityAnalyst.addSteppedListener(steppedListener);
            DatasetVector src = null;
            if (this.getParameters().getInputs().getData(INPUT_DATA).getValue() != null) {
                src = (DatasetVector) getParameters().getInputs().getData(INPUT_DATA).getValue();
            } else {
                src = (DatasetVector) sourceDataset.getSelectedItem();
            }
            String datasetName = resultDataset.getDatasetName();
            datasetName = resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(datasetName);
            DatasetVector result = ProximityAnalyst.createThiessenPolygon(src, resultDataset.getResultDatasource(), datasetName, null);
            this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(result);
            isSuccessful = result != null;
            fireRunning(new RunningEvent(this,100,"finish"));
        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(e);
        }finally {
            ProximityAnalyst.removeSteppedListener(steppedListener);
        }
        return isSuccessful;
    }

    @Override
    public IParameters getParameters() {
        return parameters;
    }

    @Override
    public String getTitle() {
        return ProcessProperties.getString("String_Form_ThiessenPolygon");
    }

    @Override
    public String getKey() {
        return MetaKeys.THIESSENPOLYGON;
    }
}
