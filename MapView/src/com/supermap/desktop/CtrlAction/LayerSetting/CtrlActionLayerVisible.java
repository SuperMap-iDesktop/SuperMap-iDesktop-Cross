package com.supermap.desktop.CtrlAction.LayerSetting;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerGroup;

public class CtrlActionLayerVisible extends CtrlAction {

	public CtrlActionLayerVisible(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();

			boolean visible = !formMap.getActiveLayers()[0].isVisible();
			for (Layer layer : formMap.getActiveLayers()) {
				layer.setVisible(visible);
				if (!layer.isVisible() && layer.getSelection() != null) {
					layer.getSelection().clear();
				}
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
		IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
		if (formMap != null && formMap.getActiveLayers().length > 0) {
			enable = true;

			for (Layer layer : formMap.getActiveLayers()) {
				if (layer.getDataset() == null && !(layer instanceof LayerGroup)) {
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
		// 不支持三态，这里先简单的设置两个状态了
		if ((formMap != null && formMap.getActiveLayers().length > 0) && (formMap.getActiveLayers()[0].isVisible())) {
			check = true;
		}
		return check;
	}
}