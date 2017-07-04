package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.implement.ParameterDatasourceConstrained;
import com.supermap.desktop.process.parameter.implement.ParameterSingleDataset;
import com.supermap.desktop.process.parameter.interfaces.IParameters;

/**
 * Created by Chen on 2017/7/3 0003.
 */
public class MetaProcessComputeDistance extends MetaProcess {

    private ParameterDatasourceConstrained sourceDatasource;
    private ParameterSingleDataset sourceDataset;

    public MetaProcessComputeDistance() {
        initParameters();
        initParameterConstraint();
        initParametersState();
        registerListener();
    }

    private void initParameters() {

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
