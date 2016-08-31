package com.supermap.desktop.Interface;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.mapping.Map;

import java.util.List;

/**
 * @author XiaJT
 */
public interface IFormTransformation extends IForm {
	void addTransformationDataset(Dataset transformationDataset, Datasource resultDatasource, String resultDatasetName);

	void addTransformationMap(Map map);

	void addReferenceObjects(List<Object> listObjects);
}
