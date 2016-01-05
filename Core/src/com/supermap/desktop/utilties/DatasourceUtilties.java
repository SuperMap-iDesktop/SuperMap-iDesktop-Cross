package com.supermap.desktop.utilties;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.desktop.Application;

/**
 * 数据源工具类
 * 
 * @author highsad
 *
 */
public class DatasourceUtilties {

	/**
	 * 根据指定名称获取指定数据源下的数据集
	 * 
	 * @param datasetName
	 * @param datasource
	 * @return
	 */
	private DatasourceUtilties() {
		// 工具类不提供构造函数

	}

	public static final Dataset getDataset(String datasetName, Datasource datasource) {
		Dataset result = null;

		try {
			if (null == datasetName || null == datasource) {
				return null;
			}

			result = datasource.getDatasets().get(datasetName);

			// 如果到这里数据集还是 null，那么就扫描网络数据集等的子数据集进行匹配
			if (result == null) {
				for (int i = 0; i < datasource.getDatasets().getCount(); i++) {
					Dataset dataset = datasource.getDatasets().get(i);

					if (dataset instanceof DatasetVector) {
						DatasetVector datasetVector = (DatasetVector) dataset;
						if (datasetVector.getChildDataset() != null && datasetName.equals(datasetVector.getChildDataset().getName())) {
							result = datasetVector.getChildDataset();
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}
}
