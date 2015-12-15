package com.supermap.desktop.CtrlAction.LayerSetting;

import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.LayersTreeUtilties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerChart;
import com.supermap.mapping.LayerGroup;
import com.supermap.mapping.ThemeCustom;
import com.supermap.mapping.ThemeRange;
import com.supermap.mapping.ThemeType;
import com.supermap.mapping.ThemeUnique;

public class CtrlActionLayerEditable extends CtrlAction {

	public CtrlActionLayerEditable(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			boolean isEditable = !formMap.getActiveLayers()[0].isEditable();
			if (formMap.getMapControl().isMultiLayerEditEnabled()) {
				for (Layer layer : formMap.getActiveLayers()) {
					layer.setEditable(isEditable);
				}
			} else {
				formMap.getActiveLayers()[0].setEditable(isEditable);
			}

			if (isEditable) {
				formMap.getMapControl().setActiveEditableLayer(formMap.getActiveLayers()[0]);
			}
			formMap.getMapControl().getMap().refresh();
			UICommonToolkit.getLayersManager().getLayersTree().updateUI();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = true;
		IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();

		if (formMap != null) {
			for (Layer layer : formMap.getActiveLayers()) {
				if (layer == null || !layer.isVisible()) {
					enable = false;
					break;
				}

				// 丢失数据集的图层，不可编辑
				boolean isDatasetNull = layer.getDataset() == null;

				boolean isDatasetVector = !isDatasetNull && layer.getDataset() instanceof DatasetVector;

				// 只读数据源下的数据集以及只读数据集不可编辑
				boolean isReadOnly = !isDatasetNull && (layer.getDataset().getDatasource().isReadOnly() || layer.getDataset().isReadOnly());

				// 海图数据集不可编辑
				boolean isLayerChart = layer instanceof LayerChart;

				// 标签、统计、等级符号专题图，不可选择，不可编辑，不可捕捉
				boolean isInvalidThemeLayer = layer.getTheme() != null
						&& (layer.getTheme().getType() == ThemeType.LABEL || layer.getTheme().getType() == ThemeType.GRADUATEDSYMBOL || layer.getTheme()
								.getType() == ThemeType.GRAPH);

				if (isDatasetNull || isReadOnly || !isDatasetVector || isLayerChart || isInvalidThemeLayer) {
					enable = false;
					break;
				}
			}
		}
		return enable;
	}

	@Override
	public boolean check() {
		boolean check = false;
		IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
		if (formMap.getActiveLayers()[0].isEditable()) {
			check = true;
		}

		return check;
	}
}