package com.supermap.desktop.CtrlAction.Layer3DSetting;

import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.realspace.Layer3D;
import com.supermap.realspace.Layer3DDataset;
import com.supermap.realspace.Layer3DType;

public class CtrlActionLayer3DEditable extends CtrlAction {
	    
	public CtrlActionLayer3DEditable(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			IFormScene formScene = (IFormScene) Application.getActiveApplication().getActiveForm();
			boolean isEditable = !formScene.getActiveLayer3Ds()[0].isEditable();
			formScene.getActiveLayer3Ds()[0].setEditable(isEditable);

			if (isEditable) {
				formScene.getSceneControl().setActiveEditableLayer(formScene.getActiveLayer3Ds()[0]);
			}
			formScene.getSceneControl().getScene().refresh();

			if (this.getCurrentLayer3D() != null) {
				this.getCurrentLayer3D().setEditable(!this.getCurrentLayer3D().isEditable());

				// 如果图层设置为可编辑,设置此图层为当前编辑图层
				if (this.getCurrentLayer3D().isEditable()) {
					formScene.getSceneControl().setActiveEditableLayer(this.getCurrentLayer3D());
				} else {
					formScene.getSceneControl().setActiveEditableLayer(null);
					for (Layer3D item : formScene.getActiveLayer3Ds()) {
						if (item.isEditable()) {
							formScene.getSceneControl().setActiveEditableLayer(item);
							break;
						}
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		IFormScene formScene = (IFormScene) Application.getActiveApplication().getActiveForm();
		if (formScene != null && formScene.getActiveLayer3Ds().length == 1) {
			if (this.getCurrentLayer3D() != null && this.getCurrentLayer3D().isVisible() && this.getCurrentLayer3D().getType() == Layer3DType.DATASET) {
				Layer3DDataset layer3DDataset = (Layer3DDataset) this.getCurrentLayer3D();
				if (layer3DDataset.getDataset().getType() == DatasetType.CAD || layer3DDataset.getDataset().getType() == DatasetType.REGION
						|| layer3DDataset.getDataset().getType() == DatasetType.REGION3D) {
					enable = true;
				}
			} else if (this.getCurrentLayer3D() != null
					&& (this.getCurrentLayer3D().getType() == Layer3DType.KML || this.getCurrentLayer3D().getType() == Layer3DType.MODEL)) {
				// notify by huchenpu
				// 缓存图层（模型缓存）组件发现自己的问题太多，不再支持编辑
				enable = true;
			}
		}
		return enable;
	}

	@Override
	public boolean check() {
		boolean check = false;
		IFormScene formScene = (IFormScene) Application.getActiveApplication().getActiveForm();
		if (formScene.getActiveLayer3Ds()[0].isEditable()) {
			check = true;
		}
        
		return check;
	}	

    private Layer3D getCurrentLayer3D() {
    	Layer3D layer3D = null;
    	IFormScene formScene = (IFormScene) Application.getActiveApplication().getActiveForm();
    	if (formScene != null && formScene.getActiveLayer3Ds() != null && formScene.getActiveLayer3Ds().length > 0)
    		layer3D = formScene.getActiveLayer3Ds()[0];
    	return layer3D;
    }
}