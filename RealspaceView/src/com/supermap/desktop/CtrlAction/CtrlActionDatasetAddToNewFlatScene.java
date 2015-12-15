package com.supermap.desktop.CtrlAction;

import javax.swing.tree.DefaultMutableTreeNode;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.WorkspaceComponentManager;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.realspace.Layer3D;
import com.supermap.realspace.Layer3DSettingVector;
import com.supermap.realspace.Scene;

public class CtrlActionDatasetAddToNewFlatScene extends CtrlAction {

	public CtrlActionDatasetAddToNewFlatScene(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		// 未实现
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		// 未实现
		return enable;
	}

}
