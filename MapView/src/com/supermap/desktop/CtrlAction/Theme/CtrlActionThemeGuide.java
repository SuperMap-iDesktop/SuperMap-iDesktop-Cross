package com.supermap.desktop.CtrlAction.Theme;

import java.util.ArrayList;

import javax.swing.JFrame;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.newtheme.ThemeGuidDialog;
import com.supermap.mapping.Layer;

public class CtrlActionThemeGuide extends CtrlAction {
	// 判断是否是cad类型的数据集，cad类型的数据集只能创建标签类专题图
	private boolean isCadType = false;
	// 判断是否是Grid类型的数据集
	private boolean isGrid = false;

	public CtrlActionThemeGuide(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		JFrame frame = (JFrame) Application.getActiveApplication().getMainFrame();
		ThemeGuidDialog themeGuidDialog = new ThemeGuidDialog(frame, true, isCadType,isGrid);
		themeGuidDialog.setVisible(true);
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		ArrayList<DatasetType> enableDatasetTypes = new ArrayList<DatasetType>();
		enableDatasetTypes.add(DatasetType.LINE);
		enableDatasetTypes.add(DatasetType.LINE3D);
		enableDatasetTypes.add(DatasetType.LINEM);
		enableDatasetTypes.add(DatasetType.NETWORK);
		enableDatasetTypes.add(DatasetType.NETWORK3D);
		enableDatasetTypes.add(DatasetType.POINT);
		enableDatasetTypes.add(DatasetType.POINT3D);
		enableDatasetTypes.add(DatasetType.REGION);
		enableDatasetTypes.add(DatasetType.REGION3D);
		enableDatasetTypes.add(DatasetType.CAD);
		enableDatasetTypes.add(DatasetType.GRID);

		if (null != Application.getActiveApplication().getActiveForm()) {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			// 选择多个图层时，不能制造专题图
			Layer[] layers = formMap.getActiveLayers();
			if (layers.length > 1 || layers.length == 0) {
				enable = false;
			} else if (layers.length == 1 && null != layers[0].getDataset()) {
				// 当前活动的图层不能创建专题图
				for (int i = 0; i < enableDatasetTypes.size(); i++) {
					if (layers[0].getDataset().getType() == enableDatasetTypes.get(i)) {
						enable = true;
					}
				}
				// 活动图层的数据集类型为cad
				if (layers[0].getDataset().getType() == DatasetType.CAD) {
					isCadType = true;
				} else {
					isCadType = false;
				}
				// 活动图层的数据集类型为Grid
				if (layers[0].getDataset().getType() == DatasetType.GRID) {
					isGrid = true;
				} else {
					isGrid = false;
				}
			}

		} else if (Application.getActiveApplication().getActiveDatasets().length > 0) {
			Dataset activeDataset = Application.getActiveApplication().getActiveDatasets()[0];
			if (enableDatasetTypes.contains(activeDataset.getType())) {
				enable = true;
			}
			// 存在活动的数据集且类型为cad类型
			if (activeDataset.getType() == DatasetType.CAD) {
				isCadType = true;
			} else {
				isCadType = false;
			}
			// 存在活动的数据集且类型为gird类型
			if (activeDataset.getType() == DatasetType.GRID) {
				isGrid = true;
			} else {
				isGrid = false;
			}
		}
		return enable;
	}
}
