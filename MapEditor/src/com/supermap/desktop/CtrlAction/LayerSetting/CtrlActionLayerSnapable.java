package com.supermap.desktop.CtrlAction.LayerSetting;

import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerGroup;
import com.supermap.mapping.ThemeCustom;
import com.supermap.mapping.ThemeRange;
import com.supermap.mapping.ThemeType;
import com.supermap.mapping.ThemeUnique;

public class CtrlActionLayerSnapable extends CtrlAction {

	public CtrlActionLayerSnapable(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			boolean isSnap = !formMap.getActiveLayers()[0].isSnapable();
			for (Layer layer : formMap.getActiveLayers()) {
				layer.setSnapable(isSnap);
			}
			formMap.getMapControl().getMap().refresh();
			UICommonToolkit.getLayersManager().getLayersTree().updateUI();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		if (Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			if (formMap != null) {
				for (Layer layer : formMap.getActiveLayers()) {
					if (layer.isVisible() && layer.getTheme() != null) {
						if (layer.getTheme() instanceof ThemeUnique || layer.getTheme() instanceof ThemeRange || layer.getTheme() instanceof ThemeCustom) {
							enable = true;
							break;
						}
					} else if (layer.isVisible() && layer.getDataset() instanceof DatasetVector)// 三维网络数据集支持编辑
					{
						enable = true;
						break;
					}
				}

				// 实现多选的可用控制
				for (Layer layer : formMap.getActiveLayers()) {
					if (layer.getDataset() == null) {
						enable = false;
						break;
					}

					if (layer.getDataset() == null) {
						enable = false;
						break;
					}

					DatasetType type = layer.getDataset().getType();
					if (layer.getDataset() != null
							&& (type == DatasetType.GRID || type == DatasetType.IMAGE || type == DatasetType.IMAGECOLLECTION || type == DatasetType.GRIDCOLLECTION)
							|| layer instanceof LayerGroup) {
						enable = false;
						break;
					}
					if (layer.getDataset() != null && layer.getTheme() != null && layer.getTheme().getType() != ThemeType.UNIQUE
							&& layer.getTheme().getType() != ThemeType.RANGE && layer.getTheme().getType() != ThemeType.CUSTOM) {
						enable = false;
						break;
					}
				}
			}
		}
		return enable;
	}

	@Override
	public boolean check() {
		boolean check = false;
		if (Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();

			if (formMap.getActiveLayers()[0].isSnapable()) {
				check = true;
			}
		}
		return check;
	}
}