package com.supermap.desktop.utilties;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetGridCollection;
import com.supermap.data.DatasetImage;
import com.supermap.data.DatasetImageCollection;
import com.supermap.data.DatasetType;
import com.supermap.data.EngineType;
import com.supermap.desktop.Application;

public class ImagePyramidUtilties {

	// 提示创建影像金字塔相关
	private static final int RASTERDT_MAXWIDTH = 3000;
	private static final int RASTERDT_MAXHEIGHT = 3000;

	private ImagePyramidUtilties() {
		// 工具类，不提供构造函数
	}

	public static boolean isNeedBuildPyramid(Dataset dataset) {
		boolean result = false;

		try {
			if (dataset instanceof DatasetImage) {
				DatasetImage datasetImage = (DatasetImage) dataset;
				Boolean imagePlugins = false;
				String server = dataset.getDatasource().getConnectionInfo().getServer();
				if (server.toLowerCase().contains(".img") || server.toLowerCase().contains(".tif") || server.toLowerCase().contains(".tiff")) {
					imagePlugins = true;
				}

				if (!datasetImage.getHasPyramid()
						&& (datasetImage.getWidth() > RASTERDT_MAXWIDTH || datasetImage.getHeight() > RASTERDT_MAXHEIGHT)
						&& (datasetImage.getDatasource().getEngineType() != EngineType.IMAGEPLUGINS && !datasetImage.isReadOnly()
								&& !datasetImage.getDatasource().isReadOnly() || imagePlugins)) {
					result = true;
				}
			} else if (dataset instanceof DatasetGrid) {
				DatasetGrid datasetGrid = (DatasetGrid) dataset;
				Boolean imagePlugins = false;
				String server = dataset.getDatasource().getConnectionInfo().getServer();
				if (server.toLowerCase().contains(".img") || server.toLowerCase().contains(".tif") || server.toLowerCase().contains(".tiff")) {
					imagePlugins = true;
				}
				if (!datasetGrid.getHasPyramid()
						&& (datasetGrid.getWidth() > RASTERDT_MAXWIDTH || datasetGrid.getHeight() > RASTERDT_MAXHEIGHT)
						&& (datasetGrid.getDatasource().getConnectionInfo().getEngineType() != EngineType.IMAGEPLUGINS && !datasetGrid.isReadOnly()
								&& !datasetGrid.getDatasource().isReadOnly() || imagePlugins)) {
					result = true;
				}
			} else if (dataset instanceof DatasetImageCollection) {
				DatasetImageCollection datasetImage = (DatasetImageCollection) dataset;

				if (!datasetImage.getHasPyramid() && !datasetImage.isReadOnly() && !datasetImage.getDatasource().isReadOnly()) {
					result = true;
				}
			} else if (dataset instanceof DatasetGridCollection) {
				DatasetGridCollection datasetGrid = (DatasetGridCollection) dataset;
				if (!datasetGrid.getHasPyramid() && !datasetGrid.isReadOnly() && !datasetGrid.getDatasource().isReadOnly()) {
					result = true;
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}
}
