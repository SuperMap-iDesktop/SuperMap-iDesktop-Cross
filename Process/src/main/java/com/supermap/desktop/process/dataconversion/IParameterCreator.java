package com.supermap.desktop.process.dataconversion;

import com.supermap.desktop.process.parameter.implement.ParameterFile;
import com.supermap.desktop.process.parameter.implement.ParameterTextField;
import com.supermap.desktop.process.parameter.interfaces.IParameter;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xie on 2017/3/31.
 */
public interface IParameterCreator<T> {
    /**
     * Use t to create a parameter
     *
     * @param t
     * @return
     */
    CopyOnWriteArrayList<ReflectInfo> create(T t);

    /**
     * Create basic import info parameter
     *
     * @return
     */
    CopyOnWriteArrayList<ReflectInfo> createDefault(T t, String type);

    IParameter getParameterCombineResultSet();

    IParameter getParameterCombineParamSet();

    ParameterFile getParameterFile();

    ParameterTextField getDatasetName();

}
