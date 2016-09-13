package com.supermap.desktop.CtrlAction.transformationForm;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTransformation;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.DialogResult;

import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class CtrlActionTransformationAddLayer extends CtrlAction {
	public CtrlActionTransformationAddLayer(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		JDialogAddLayer jDialogAddLayer = new JDialogAddLayer();
		if (jDialogAddLayer.showDialog() == DialogResult.OK) {
			ArrayList<AddLayerItemBean> addItems = jDialogAddLayer.getAddItems();
			ArrayList<Object> targetObject = new ArrayList<>();
			ArrayList<Object> referObject = new ArrayList<>();
			for (AddLayerItemBean addItem : addItems) {
				if (addItem.getType() == FormTransformationSubFormType.Target) {
					targetObject.add(addItem.getData());
				} else {
					referObject.add(addItem.getData());
				}
			}
			IFormTransformation activeForm = (IFormTransformation) Application.getActiveApplication().getActiveForm();
			activeForm.addReferenceObjects(referObject);
			// TODO: 2016/9/13 targetObject添加到当前地图左侧
		}
	}

	@Override
	public boolean enable() {
		return Application.getActiveApplication().getActiveForm() instanceof IFormTransformation;
	}
}
