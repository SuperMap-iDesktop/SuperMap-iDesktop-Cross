package com.supermap.desktop.process.meta;

import com.supermap.desktop.process.ProcessResources;
import com.supermap.desktop.process.core.AbstractProcess;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.Inputs;
import com.supermap.desktop.process.parameter.interfaces.datas.Outputs;

import javax.swing.*;

/**
 * Created by highsad on 2017/1/5.
 */
public abstract class MetaProcess extends AbstractProcess {
    protected Outputs outputs;
    protected Inputs inputs;
    protected IParameters parameters;
    protected boolean finished = false;


    public MetaProcess() {
        this.outputs = new Outputs(this);
        this.inputs = new Inputs(this);
    }

    @Override
    public IParameters getParameters() {
        return parameters;
    }

    @Override
    public Inputs getInputs() {
        return inputs;
    }

    @Override
    public Outputs getOutputs() {
        return this.outputs;
    }


    protected Icon getIconByPath(String path) {
        return new ImageIcon(ProcessResources.getResourceURL(path));
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isFinished() {
        return finished;
    }
}
