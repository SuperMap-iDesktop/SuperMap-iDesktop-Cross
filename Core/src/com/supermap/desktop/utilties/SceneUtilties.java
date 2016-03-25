package com.supermap.desktop.utilties;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetImage;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Geometry;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.realspace.Camera;
import com.supermap.realspace.Layer3DSetting;
import com.supermap.realspace.Layer3DSettingGrid;
import com.supermap.realspace.Layer3DSettingImage;
import com.supermap.realspace.Layer3DSettingVector;
import com.supermap.realspace.Scene;

import javax.swing.*;
import java.util.ArrayList;

/**
 * 场景相关工具类
 * 
 * @author highsad
 *
 */
public class SceneUtilties {


	private SceneUtilties() {
		// 工具类不提供构造函数
	}

	/**
	 * 添加指定数据集到指定场景上
	 *
	 * @param scene
	 * @param dataset
	 * @param addToHead
	 */
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
			if (JOptionPaneUtilties.showConfirmDialog(message) == JOptionPane.OK_OPTION) {
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
}
