package com.supermap.desktop.controls.utilties;

import java.text.MessageFormat;
import java.util.ArrayList;

import com.supermap.data.CursorType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Recordset;
import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dialog.JDialogConfirm;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.SmLabel;
import com.supermap.desktop.implement.SmStatusbar;
import com.supermap.desktop.progress.callable.CreateImagePyramidCallable;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.progress.FormProgressTotal;
import com.supermap.desktop.utilties.ImagePyramidUtilties;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.desktop.utilties.TabularUtilties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.ui.Action;
import com.supermap.ui.MapControl;

public class MapViewUtilties {

	private MapViewUtilties() {
		// 工具类，不提供构造方法
	}

	/**
	 * 将数据集添加到指定的地图
	 *
	 * @param map
	 * @param datasets
	 * @param addToHead
	 */
	public static void addDatasetsToMap(Map map, Dataset[] datasets, boolean addToHead) {
		if (datasets == null || datasets.length == 0) {
			return;
		}

		// 预处理较大的栅格或者影像数据集，创建影像金字塔
		ArrayList<Dataset> needCreateImagePyramid = new ArrayList<Dataset>();
		boolean isUsedAsDefault = false; // 是否将当前选择作为后续的默认设置，不再提示
		JDialogConfirm dialogConfirm = new JDialogConfirm();

		for (Dataset dataset : datasets) {
			if (ImagePyramidUtilties.isNeedBuildPyramid(dataset)) {
				dialogConfirm.setMessage(MessageFormat.format(ControlsProperties.getString("String_IsBuildPyramid"), dataset.getName()));
				if (!isUsedAsDefault) {
					dialogConfirm.showDialog();
					isUsedAsDefault = dialogConfirm.isUsedAsDefault();
				}

				if (dialogConfirm.getDialogResult() == DialogResult.OK) {
					needCreateImagePyramid.add(dataset);
				}
			}
		}

		if (!needCreateImagePyramid.isEmpty()) {
			FormProgressTotal formProgressTotal = new FormProgressTotal(ControlsProperties.getString("String_Form_BuildDatasetPyramid"));
			formProgressTotal.doWork(new CreateImagePyramidCallable(needCreateImagePyramid.toArray(new Dataset[needCreateImagePyramid.size()])));
		}

		// 添加到地图
		for (Dataset dataset : datasets) {
			if (dataset.getType() != DatasetType.TABULAR && dataset.getType() != DatasetType.TOPOLOGY) {
				MapUtilties.addDatasetToMap(map, dataset, addToHead);
			}
		}
		// 更新地图属性面板
		Application.getActiveApplication().resetActiveForm();
		map.refresh();
		UICommonToolkit.getLayersManager().setMap(map);
	}

	/**
	 * 将数据集打开到新的窗口
	 *
	 * @param datasets
	 * @param addToHead
	 */
	public static void addDatasetsToNewWindow(Dataset[] datasets, boolean addToHead) {
		ArrayList<Dataset> datasetsToMap = new ArrayList<>(); // 可以添加到地图上的数据集

		for (Dataset dataset : datasets) {
			if (dataset.getType() == DatasetType.TABULAR) {
				// 如果带有纯属性数据集，在单独的属性窗口中打开
				TabularUtilties.openDatasetVectorFormTabular(dataset);
			} else if (dataset.getType() == DatasetType.LINKTABLE) {
				// 暂时什么都不做
			} else if (dataset.getType() == DatasetType.TOPOLOGY) {
				// 暂时什么都不做
			} else {
				datasetsToMap.add(dataset);
			}
		}

		if (!datasetsToMap.isEmpty()) {
			String name = MapUtilties.getAvailableMapName(
					MessageFormat.format("{0}@{1}", datasetsToMap.get(0).getName(), datasetsToMap.get(0).getDatasource().getAlias()), true);
			IFormMap formMap = (IFormMap) CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.MAP, name);
			addDatasetsToMap(formMap.getMapControl().getMap(), datasetsToMap.toArray(new Dataset[datasetsToMap.size()]), addToHead);

			// 打开到新地图时，全幅显示，不使用 EntireView，因为窗口打开之后会动态调整 MapControl 的大小，从而导致此前设置的全幅效果不对
			Rectangle2D viewBounds = getDatasetsBounds(datasetsToMap.toArray(new Dataset[datasetsToMap.size()]));
			if (viewBounds != null && Double.compare(viewBounds.getWidth(), 0.0) != 0 && Double.compare(viewBounds.getHeight(), 0.0) != 0) {
				formMap.getMapControl().getMap().setViewBounds(viewBounds);
			}
			// 新建的地图窗口，修改默认的Action为漫游
			formMap.getMapControl().setAction(Action.PAN);
		}
	}

	/**
	 * 获取数据集的 bounds
	 *
	 * @param datasets
	 * @return
	 */
	public static Rectangle2D getDatasetsBounds(Dataset[] datasets) {
		Rectangle2D bounds = null;

		try {
			for (Dataset dataset : datasets) {
				if (bounds == null) {
					bounds = dataset.getBounds().clone();
				} else {
					bounds.union(dataset.getBounds());
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return bounds;
	}

	/**
	 * 全选指定 IFormMap 可编辑图层的 Geometry
	 *
	 * @param IFormMap 指定的 IFormMap
	 * @return 选中的对象数
	 */
	public static int selectAllGeometry(IFormMap formMap) {
		// 记录选择集
		int count = 0;

		try {
			ArrayList<Layer> layers = MapUtilties.getLayers(formMap.getMapControl().getMap());

			for (Layer layer : layers) {
				if (layer.isVisible() && layer.isSelectable()) {
					DatasetVector dataset = (DatasetVector) layer.getDataset();
					Recordset recordset = dataset.getRecordset(false, CursorType.STATIC);
					layer.getSelection().fromRecordset(recordset);
					count += dataset.getRecordCount();
					recordset.dispose();
				}
			}
			formMap.getMapControl().getMap().refresh();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return count;
	}

	public static int reverseSelection(IFormMap formMap) {
		// 记录选择集
		int count = 0;

		try {
			ArrayList<Layer> layers = MapUtilties.getLayers(formMap.getMapControl().getMap());
			for (Layer layer : layers) {
				if (layer.isVisible() && layer.isSelectable() && layer.getSelection() != null) {
					Recordset preRecordset = layer.getSelection().toRecordset();
					DatasetVector dataset = (DatasetVector) layer.getDataset();
					Recordset recordset = dataset.getRecordset(false, CursorType.STATIC);
					layer.getSelection().fromRecordset(recordset);
					while (!preRecordset.isEOF()) {
						layer.getSelection().remove(preRecordset.getID());
						preRecordset.moveNext();
					}

					count += recordset.getRecordCount() - preRecordset.getRecordCount();
					preRecordset.dispose();
					recordset.dispose();
				}
			}
			formMap.getMapControl().getMap().refresh();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return count;
	}

	public static int clearAllSelection(IFormMap formMap){
		try {
			ArrayList<Layer> layers = MapUtilties.getLayers(formMap.getMapControl().getMap());
			for (Layer layer : layers) {
				if (layer.getSelection() != null && layer.getSelection().getCount() > 0) {
					layer.getSelection().clear();
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		formMap.getMapControl().getMap().refresh();
		return 0;
	}

	public static int calculateSelectNumber(IFormMap formMap){
		ArrayList<Layer> layers = MapUtilties.getLayers(formMap.getMapControl().getMap());
		int count = 0;
		for (Layer layer : layers) {
			if(layer.getSelection()!=null && layer.getSelection().getCount()>0){
				count += layer.getSelection().getCount();
			}
		}
		return count;
	}
}
