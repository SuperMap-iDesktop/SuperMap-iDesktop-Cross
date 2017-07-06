package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.data.DatasetType;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.properties.CommonProperties;

/**
 * Created by Chen on 2017/7/3 0003.
 */
public class MetaProcessComputeDistance extends MetaProcess {

    private ParameterDatasourceConstrained sourceDatasource;
    private ParameterSingleDataset sourceDataset;
    private ParameterDatasourceConstrained proximityDatasource;
    private ParameterSingleDataset proximityDataset;
    private ParameterComboBox comboBox;
    private ParameterLabel label;
    private ParameterCheckBox checkBoxMin;
    private ParameterCheckBox checkBoxMax;
    private ParameterTextField textFieldMin;
    private ParameterTextField textFieldMax;
    private ParameterSaveDataset resultDataset;

    public MetaProcessComputeDistance() {
        initParameters();
        initParameterConstraint();
        initParametersState();
        registerListener();
    }

    private void initParameters() {
        sourceDatasource = new ParameterDatasourceConstrained();
        sourceDatasource.setDescribe(CommonProperties.getString("String_Label_Datasource"));
        sourceDataset = new ParameterSingleDataset(DatasetType.POINT);
        sourceDataset.setDescribe(CommonProperties.getString("String_Label_Dataset"));

        proximityDatasource = new ParameterDatasourceConstrained();
        proximityDatasource.setDescribe(CommonProperties.getString("String_Label_Datasource"));
        proximityDataset = new ParameterSingleDataset(DatasetType.POINT);
        proximityDataset.setDescribe(CommonProperties.getString("String_Label_Dataset"));

        comboBox = new ParameterComboBox(ProcessProperties.getString("String_Label_ComputeMethod"));
        label = new ParameterLabel();
        label.setDescribe(ProcessProperties.getString("String_Label_SearchRangeSetting"));
        checkBoxMax = new ParameterCheckBox(ProcessProperties.getString("String_Label_MaxDistance"));
        checkBoxMin = new ParameterCheckBox(ProcessProperties.getString("String_Label_MinDistance"));
        //TODO
    }

    private void initParameterConstraint() {

    }

    private void initParametersState() {

    }

    private void registerListener() {

    }

    @Override
    public boolean execute() {
        return false;
    }

    @Override
    public IParameters getParameters() {
        return parameters;
    }

    @Override
    public String getTitle() {
        return ProcessProperties.getString("String_Form_ComputeDistance");
    }

    @Override
    public String getKey() {
        return MetaKeys.COMPUTRDISTANCE;
    }
}
