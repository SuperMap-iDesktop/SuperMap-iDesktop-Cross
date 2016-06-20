package com.supermap.desktop.controls.utilities;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetGridCollection;
import com.supermap.data.DatasetImage;
import com.supermap.data.DatasetImageCollection;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Geometry;
import com.supermap.data.PrjCoordSysType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dialog.JDialogConfirm;
import com.supermap.desktop.progress.callable.CreateImagePyramidCallable;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.NodeDataType;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.progress.FormProgressTotal;
import com.supermap.desktop.utilities.DatasetTypeUtilities;
import com.supermap.desktop.utilities.GeoStyleUtilities;
import com.supermap.desktop.utilities.JOptionPaneUtilities;
import com.supermap.realspace.Camera;
import com.supermap.realspace.Feature3D;
import com.supermap.realspace.Feature3Ds;
import com.supermap.realspace.Layer3D;
import com.supermap.realspace.Layer3DDataset;
import com.supermap.realspace.Layer3DSetting;
import com.supermap.realspace.Layer3DSettingGrid;
import com.supermap.realspace.Layer3DSettingImage;
import com.supermap.realspace.Layer3DSettingVector;
import com.supermap.realspace.Scene;
import com.supermap.realspace.ScreenLayer3D;
import com.supermap.realspace.TerrainLayer;
import com.supermap.realspace.Theme3DRangeItem;
import com.supermap.realspace.Theme3DUniqueItem;

import javax.swing.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 场景相关工具类
 *
 * @author highsad
 */
public class SceneUIUtilities {


	private SceneUIUtilities() {
		// 工具类不提供构造函数
	}

	/**
	 * 添加指定数据集到指定场景上
	 *
	 * @param scene
	 * @param datasets
	 */
	public static void addDatasetToScene(Scene scene, Dataset... datasets) {
		Layer3DSetting layer3DSetting = null;

		datasets = getLimitAddToSceneDataset(datasets);
		for (Dataset dataset : datasets) {
			if (dataset instanceof DatasetVector) {
				layer3DSetting = new Layer3DSettingVector();
			} else if (dataset instanceof DatasetImage) {
				layer3DSetting = new Layer3DSettingImage();
			} else if (dataset instanceof DatasetGrid) {
				layer3DSetting = new Layer3DSettingGrid();
			} else {
				throw new UnsupportedOperationException();
			}

			Layer3DDataset layer = scene.getLayers().add(dataset, layer3DSetting, true);

			if (dataset.getType() == DatasetType.REGION || dataset.getType() == DatasetType.REGION3D
					|| dataset.getType() == DatasetType.PARAMETRICREGION) {
				Layer3DSettingVector setting = (Layer3DSettingVector) layer.getAdditionalSetting();
				setting.getStyle().setFillForeColor(GeoStyleUtilities.getFillColor());
				setting.getStyle().setLineColor(GeoStyleUtilities.getLineColor());
			} else if (dataset.getType() == DatasetType.LINE || dataset.getType() == DatasetType.NETWORK || dataset.getType() == DatasetType.NETWORK3D
					|| dataset.getType() == DatasetType.PARAMETRICLINE || dataset.getType() == DatasetType.LINEM
					|| dataset.getType() == DatasetType.LINE3D) {
				Layer3DSettingVector setting = (Layer3DSettingVector) layer.getAdditionalSetting();
				setting.getStyle().setLineColor(GeoStyleUtilities.getLineColor());
			} else if (dataset.getType() == DatasetType.POINT || dataset.getType() == DatasetType.POINT3D) {
				Layer3DSettingVector setting = (Layer3DSettingVector) layer.getAdditionalSetting();
				setting.getStyle().setLineColor(GeoStyleUtilities.getLineColor());
			}
			// 网络数据集和三维网络数据集添加到场景上，同时添加点图层
			if (dataset instanceof DatasetVector && (dataset.getType() == DatasetType.NETWORK || dataset.getType() == DatasetType.NETWORK3D)) {
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

	private static Dataset[] getLimitAddToSceneDataset(Dataset[] datasets) {
		List<Dataset> datasetLimitList = new ArrayList<>();
		List<Dataset> needBuildPyramidDatasetList = new ArrayList<>();
		boolean isUsedAsDefault = false; // 是否将当前选择作为后续的默认设置，不再提示
		JDialogConfirm dialogConfirm = new JDialogConfirm();
		for (Dataset dataset : datasets) {
			if (dataset.getPrjCoordSys().getType() == PrjCoordSysType.PCS_NON_EARTH) {
				String message = MessageFormat.format(ControlsProperties.getString("String_FormScene_SceneGlobleAddDatasetPrjError"), dataset.getName());
				Application.getActiveApplication().getOutput().output(message);
				continue;
			}
			if (dataset instanceof DatasetImageCollection || dataset instanceof DatasetGridCollection || dataset.getType() == DatasetType.TABULAR
					|| dataset.getType() == DatasetType.LINKTABLE) {
				String message = MessageFormat.format(ControlsProperties.getString("String_Scene_NotSuportedDatasetType"), DatasetTypeUtilities.toString(dataset.getType()));
				Application.getActiveApplication().getOutput().output(message);
				continue;
			}
			if (dataset instanceof DatasetImage || dataset instanceof DatasetGrid) {
				// 影像，栅格创建影像金字塔
				if (!((dataset instanceof DatasetImage && ((DatasetImage) dataset).getHasPyramid()) || (dataset instanceof DatasetGrid && ((DatasetGrid) dataset).getHasPyramid()))) {
					dialogConfirm.setMessage(MessageFormat.format(ControlsProperties.getString("String_MustBuildPyramid"), dataset.getName()));
					if (!isUsedAsDefault) {
						dialogConfirm.showDialog();
						isUsedAsDefault = dialogConfirm.isUsedAsDefault();
					}

					if (dialogConfirm.getDialogResult() == DialogResult.OK) {
						needBuildPyramidDatasetList.add(dataset);
					}
				} else {
					datasetLimitList.add(dataset);
				}
			} else {
				datasetLimitList.add(dataset);
			}
		}
		if (needBuildPyramidDatasetList.size() > 0) {
			FormProgressTotal formProgressTotal = new FormProgressTotal(ControlsProperties.getString("String_Form_BuildDatasetPyramid"));
			formProgressTotal.doWork(new CreateImagePyramidCallable(needBuildPyramidDatasetList.toArray(new Dataset[needBuildPyramidDatasetList.size()])));

		}
		for (Dataset dataset : needBuildPyramidDatasetList) {
			if (dataset instanceof DatasetImage) {
				if (((DatasetImage) dataset).getHasPyramid()) {
					datasetLimitList.add(dataset);
				}
			} else if (dataset instanceof DatasetGrid) {
				if (((DatasetGrid) dataset).getHasPyramid()) {
					datasetLimitList.add(dataset);
				}
			}
		}
		return datasetLimitList.toArray(new Dataset[datasetLimitList.size()]);
	}

	/**
	 * 判断场景新名称是否可用
	 *
	 * @param newSceneName
	 * @param oldSceneName
	 * @return
	 */
	public static boolean checkAvailableSceneName(String newSceneName, String oldSceneName) {
		boolean flag = false;
		String newSceneNameTemp = newSceneName;
		String oldSceneNameTemp = oldSceneName;
		newSceneNameTemp = newSceneNameTemp.toLowerCase();
		oldSceneNameTemp = oldSceneNameTemp.toLowerCase();
		if (null == newSceneNameTemp || newSceneNameTemp.length() <= 0) {
			flag = false;
		} else {
			ArrayList<String> allSceneNames = new ArrayList<String>();

			for (int index = 0; index < Application.getActiveApplication().getWorkspace().getScenes().getCount(); index++) {
				allSceneNames.add(Application.getActiveApplication().getWorkspace().getScenes().get(index).toLowerCase());
			}

			for (int index = 0; index < Application.getActiveApplication().getMainFrame().getFormManager().getCount(); index++) {
				IForm form = Application.getActiveApplication().getMainFrame().getFormManager().get(index);
				if (!form.getText().equalsIgnoreCase(oldSceneNameTemp)) {
					allSceneNames.add(form.getText().toLowerCase());
				}
			}
			if (!allSceneNames.contains(newSceneNameTemp)) {
				flag = true;
			}
		}

		return flag;
	}

	/**
	 * 在当前的工作空间中获取一个唯一可用的场景名称
	 *
	 * @param sceneName   指定前缀
	 * @param isNewWindow 是否是新窗口
	 * @return
	 */
	public static String getAvailableSceneName(String sceneName, boolean isNewWindow) {
		String ssceneNameTemp = sceneName;
		String availableSceneName = ssceneNameTemp.toLowerCase();
		if (null == ssceneNameTemp || ssceneNameTemp.length() <= 0) {
			ssceneNameTemp = CoreProperties.getString("String_WorkspaceNodeCaptionScene");
		}

		ArrayList<String> allSceneNames = new ArrayList<>();

		for (int index = 0; index < Application.getActiveApplication().getWorkspace().getScenes().getCount(); index++) {
			allSceneNames.add(Application.getActiveApplication().getWorkspace().getScenes().get(index).toLowerCase());
		}

		for (int index = 0; index < Application.getActiveApplication().getMainFrame().getFormManager().getCount(); index++) {
			IForm form = Application.getActiveApplication().getMainFrame().getFormManager().get(index);
			if (form instanceof IFormScene) {
				allSceneNames.add(form.getText().toLowerCase());
			}
		}

		if (!allSceneNames.contains(availableSceneName)) {
			availableSceneName = ssceneNameTemp;
		} else {
			int indexSceneName = 1;
			while (true) {
				availableSceneName = String.format("%s%d", ssceneNameTemp, indexSceneName);
				availableSceneName = availableSceneName.toLowerCase();
				if (!allSceneNames.contains(availableSceneName)) {
					break;
				}

				indexSceneName += 1;
			}
		}

		return availableSceneName;
	}

	/**
	 * 删除指定的场景
	 *
	 * @param sceneNames
	 */
	public static void deleteScenes(String[] sceneNames) {
		try {
			String message = CoreProperties.getString("String_SceneDelete_Confirm");
			if (sceneNames.length == 1) {
				message = message + System.lineSeparator() + String.format(CoreProperties.getString("String_SceneDelete_Confirm_One"), sceneNames[0]);
			} else {
				message = message + System.lineSeparator() + String.format(CoreProperties.getString("String_SceneDelete_Confirm_Multi"), sceneNames.length);
			}
			if (JOptionPaneUtilities.showConfirmDialog(message) == JOptionPane.OK_OPTION) {
				for (String sceneName : sceneNames) {
					IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
					for (int i = formManager.getCount() - 1; i >= 0; i--) {
						if (formManager.get(i) instanceof IFormScene && formManager.get(i).getText().equals(sceneName)) {
							formManager.close(formManager.get(i));
						}
					}
					Application.getActiveApplication().getWorkspace().getScenes().remove(sceneName);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public static void flyToObject(Scene scene, Geometry geometry, int timeSpan) {
		if (scene != null && geometry != null) {
			if (Double.doubleToRawLongBits(geometry.getBounds().getWidth()) == 0 && Double.doubleToRawLongBits(geometry.getBounds().getHeight()) == 0) {
				Camera flyCamera = scene.getCamera();
				flyCamera.setLongitude(geometry.getInnerPoint().getX());
				flyCamera.setLatitude(geometry.getInnerPoint().getY());
				flyCamera.setAltitude(1000);
				scene.fly(flyCamera, timeSpan);
			} else {
				scene.ensureVisible(geometry.getBounds(), timeSpan);
			}
		}
	}

	public static void flyToObject(Scene scene, Geometry geometry) {
		if (scene != null && geometry != null) {
			if (Double.doubleToRawLongBits(geometry.getBounds().getWidth()) == 0 && Double.doubleToRawLongBits(geometry.getBounds().getHeight()) == 0) {
				Camera flyCamera = scene.getCamera();
				flyCamera.setLongitude(geometry.getInnerPoint().getX());
				flyCamera.setLatitude(geometry.getInnerPoint().getY());
				flyCamera.setAltitude(1000);
				scene.fly(flyCamera);
			} else {
				scene.ensureVisible(geometry.getBounds());
			}
		}
	}

	public static boolean getObjectVisitble(TreeNodeData treeNode) {
		boolean visible = true;
		try {
			if (treeNode.getType() == NodeDataType.UNKNOWN || treeNode.getType() == NodeDataType.LAYER3DS || treeNode.getType() == NodeDataType.TERRAIN_LAYERS) {
				visible = false;
			} else if (treeNode.getType() == NodeDataType.SCREEN_LAYER3D) {
				visible = ((ScreenLayer3D) treeNode.getData()).isVisible();
			} else if (treeNode.getType() == NodeDataType.LAYER3D_DATASET || treeNode.getType() == NodeDataType.LAYER_IMAGE || treeNode.getType() == NodeDataType.LAYER_GRID) {
				visible = ((Layer3D) treeNode.getData()).isVisible();
			} else if (treeNode.getType() == NodeDataType.TERRAIN_LAYER) {
				visible = ((TerrainLayer) treeNode.getData()).isVisible();
			} else if (treeNode.getType() == NodeDataType.FEATURE3DS) {
				visible = ((Feature3Ds) treeNode.getData()).isVisible();
			} else if (treeNode.getType() == NodeDataType.FEATURE3D) {
				visible = ((Feature3D) treeNode.getData()).isVisible();
			} else if (treeNode.getType() == NodeDataType.THEME3D_UNIQUE_ITEM) {
				visible = ((Theme3DUniqueItem) treeNode.getData()).isVisible();
			} else if (treeNode.getType() == NodeDataType.THEME3D_RANGE_ITEM) {
				visible = ((Theme3DRangeItem) treeNode.getData()).isVisible();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return visible;
	}
}
