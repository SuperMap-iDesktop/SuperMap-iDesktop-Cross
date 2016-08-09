package com.supermap.desktop.Interface;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;

/**
 * @author XiaJT
 */
public interface IFormTransformation extends IForm {
	void setTransformationDataset(Dataset transformationDataset);

	void addReferenceDataset(Dataset referenceDataset);

	void setResultDataSource(Datasource resultDatasource);

	void setResultDatasetName(String resultDatasetName);
}
