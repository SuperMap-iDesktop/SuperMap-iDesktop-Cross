package com.supermap.desktop.Interface;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.mapping.Map;
import com.supermap.ui.Action;

import javax.swing.*;
import java.util.List;

/**
 * @author XiaJT
 */
public interface IFormTransformation extends IForm {
	void addTransformationDataset(Dataset transformationDataset, Datasource resultDatasource, String resultDatasetName);

	void addTransformationMap(Map map);

	void addReferenceObjects(List<Object> listObjects);

	void startAddPoint();

	boolean isAddPointing();

	JTable getTable();

	void centerOriginal();

	void deleteTableSelectedRow();

	void setAction(Action action);
}
