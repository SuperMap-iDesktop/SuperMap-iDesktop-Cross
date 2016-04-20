package com.supermap.desktop.CtrlAction.Utilties;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Geometry;
import com.supermap.data.Recordset;
import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.ui.controls.NodeDataType;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.realspace.Camera;
import com.supermap.realspace.Layer3D;
import com.supermap.realspace.Layer3DDataset;
import com.supermap.realspace.Layer3DType;
import com.supermap.realspace.TerrainLayer;

/**
 * @author XiaJt
 */
public class SceneJumpUtilties {

	public static void zoomToLayer(IFormScene formScene, TreeNodeData data) {
		if (data.getType() == NodeDataType.LAYER3D_DATASET) {
			Layer3D layer = (Layer3D) data.getData();
			Rectangle2D rectangle2D = layer.getBounds();
			// 处理数据集的Bounds为一个点的情况
			if (layer.getType() == Layer3DType.DATASET && Double.doubleToLongBits(rectangle2D.getHeight()) == 0 && Double.doubleToLongBits(rectangle2D.getWidth()) == 0) {
				DatasetVector datasetVector = (DatasetVector) ((Layer3DDataset) layer).getDataset();
				Recordset recordset = datasetVector.getRecordset(false, CursorType.STATIC);
				Geometry geometry = null;
				if (recordset != null) {
					recordset.moveFirst();
					geometry = recordset.getGeometry();
					recordset.close();
					recordset.dispose();
					recordset = null;
				}
				if (geometry != null) {
					Camera flyCamera = formScene.getSceneControl().getScene().getCamera();
					flyCamera.setLongitude(geometry.getInnerPoint().getX());
					flyCamera.setLatitude(geometry.getInnerPoint().getY());
					flyCamera.setAltitude(1000);
					formScene.getSceneControl().getScene().fly(flyCamera);
				}
			} else {
				formScene.getSceneControl().getScene().ensureVisible(rectangle2D, 2000);
			}
			formScene.getSceneControl().getScene().refresh();
		} else if (data.getType() == NodeDataType.TERRAIN_LAYER) {
			TerrainLayer terrainLayer = (TerrainLayer) data.getData();
			Rectangle2D rectangle2D = terrainLayer.getBounds();
			formScene.getSceneControl().getScene().ensureVisible(rectangle2D, 2000);
			formScene.getSceneControl().getScene().refresh();
		}
	}

	public static void jumpToLayer(IFormScene formScene, TreeNodeData data) {
		if (data.getType() == NodeDataType.LAYER3D_DATASET) {
			Layer3D layer = (Layer3D) data.getData();
			Rectangle2D rectangle2D = layer.getBounds();
			// 处理数据集的Bounds为一个点的情况
			if (layer.getType() == Layer3DType.DATASET && Double.doubleToLongBits(rectangle2D.getHeight()) == 0 && Double.doubleToLongBits(rectangle2D.getWidth()) == 0) {
				DatasetVector datasetVector = (DatasetVector) ((Layer3DDataset) layer).getDataset();
				Recordset recordset = datasetVector.getRecordset(false, CursorType.STATIC);
				Geometry geometry = null;
				if (recordset != null) {
					recordset.moveFirst();
					geometry = recordset.getGeometry();
					recordset.close();
					recordset.dispose();
					recordset = null;
				}
				if (geometry != null) {
					Camera flyCamera = formScene.getSceneControl().getScene().getCamera();
					flyCamera.setLongitude(geometry.getInnerPoint().getX());
					flyCamera.setLatitude(geometry.getInnerPoint().getY());
					flyCamera.setAltitude(1000);
					formScene.getSceneControl().getScene().fly(flyCamera, 0);
				}
			} else {
				formScene.getSceneControl().getScene().ensureVisible(rectangle2D, 0);
			}
			formScene.getSceneControl().getScene().refresh();
		} else if (data.getType() == NodeDataType.TERRAIN_LAYER) {
			TerrainLayer terrainLayer = (TerrainLayer) data.getData();
			Rectangle2D rectangle2D = terrainLayer.getBounds();
			formScene.getSceneControl().getScene().ensureVisible(rectangle2D, 0);
			formScene.getSceneControl().getScene().refresh();
		}
	}
}
