package com.supermap.desktop.utilties;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetImage;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.realspace.Layer3DSetting;
import com.supermap.realspace.Layer3DSettingGrid;
import com.supermap.realspace.Layer3DSettingImage;
import com.supermap.realspace.Layer3DSettingVector;
import com.supermap.realspace.Scene;

/**
 * 场景相关工具类
 * 
 * @author highsad
 *
 */
public class SceneUtilties {

	/**
	 * 添加指定数据集到指定场景上
	 * 
	 * @param scene
	 * @param dataset
	 * @param addToHead
	 */
	private SceneUtilties() {
		// 工具类不提供构造函数
	}

	public static void addDatasetToScene(Scene scene, Dataset dataset, boolean addToHead) {
		Layer3DSetting layer3DSetting = null;

		if (dataset instanceof DatasetVector) {
			layer3DSetting = new Layer3DSettingVector();
		} else if (dataset instanceof DatasetImage) {
			layer3DSetting = new Layer3DSettingImage();
		} else if (dataset instanceof DatasetGrid) {
			layer3DSetting = new Layer3DSettingGrid();
		} else {
			throw new UnsupportedOperationException();
		}

		if (layer3DSetting != null) {
			scene.getLayers().add(dataset, layer3DSetting, true);

			// 网络数据集和三维网络数据集添加到场景上，同时添加点图层
			if (dataset.getType() == DatasetType.NETWORK || dataset.getType() == DatasetType.NETWORK3D) {
				Layer3DSettingVector layer3DSettingVector = new Layer3DSettingVector();
				Dataset datasetPoint = ((DatasetVector) dataset).getChildDataset();

				if (datasetPoint != null) {
					scene.getLayers().add(datasetPoint, layer3DSettingVector, true);
				} else {
					throw new IllegalArgumentException();
				}
			}
		}
	}
}
