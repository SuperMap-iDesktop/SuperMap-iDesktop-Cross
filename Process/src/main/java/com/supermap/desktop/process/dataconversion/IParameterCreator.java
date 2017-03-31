package com.supermap.desktop.process.dataconversion;

import com.supermap.desktop.process.parameter.interfaces.IParameter;

import java.util.ArrayList;

/**
 * Created by xie on 2017/3/31.
 */
public interface IParameterCreator<T> {
    /**
     * Use t to create a parameter
     * @param t
     * @return
     */
    ArrayList<ReflectInfo> create(T t);
    /**
     * Create basic import info parameter
     * @return
     */
    ArrayList<ReflectInfo> createDefault(T t);

}
