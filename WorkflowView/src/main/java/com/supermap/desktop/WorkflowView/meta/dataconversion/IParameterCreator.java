package com.supermap.desktop.WorkflowView.meta.dataconversion;

import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.ipls.ParameterButton;
import com.supermap.desktop.process.parameter.ipls.ParameterCharset;
import com.supermap.desktop.process.parameter.ipls.ParameterDatasource;
import com.supermap.desktop.process.parameter.ipls.ParameterFile;
import com.supermap.desktop.process.parameter.ipls.ParameterRadioButton;
import com.supermap.desktop.process.parameter.ipls.ParameterTextArea;
import com.supermap.desktop.process.parameter.ipls.ParameterTextField;

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
	 * Create result info parameter
	 *
	 * @return
	 */
	CopyOnWriteArrayList<ReflectInfo> createResult(T t, String type);

	CopyOnWriteArrayList<ReflectInfo> createSourceInfo(T t, String type);

	IParameter getParameterCombineResultSet();

	IParameter getParameterCombineParamSet();

	IParameter getParameterCombineSourceInfoSet();

	ParameterFile getParameterFile();

	ParameterFile getParameterFileFolder();

	ParameterTextField getParameterDataset();

	ParameterCharset getParameterCharset();

	ParameterButton getParameterButton();

	ParameterTextArea getParameterTextArea();

	ParameterFile getParameterChooseFile();

	ParameterRadioButton getParameterSetRadioButton();

	ParameterRadioButton getParameterRadioButtonFolderOrFile();

	ParameterDatasource getParameterResultDatasource();
}
