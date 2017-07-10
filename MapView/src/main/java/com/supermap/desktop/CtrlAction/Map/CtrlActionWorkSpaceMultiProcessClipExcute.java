package com.supermap.desktop.CtrlAction.Map;

import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.dialog.cacheClip.DialogCacheBuilder;
import com.supermap.desktop.dialog.cacheClip.cache.CacheUtilities;
import com.supermap.desktop.dialog.cacheClip.cache.LogWriter;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.NodeDataType;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.mapping.Map;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created by xie on 2017/7/7.
 */
public class CtrlActionWorkSpaceMultiProcessClipExcute extends CtrlAction {
	public CtrlActionWorkSpaceMultiProcessClipExcute(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	protected void run() {
		Map map = CacheUtilities.getWorkspaceSelectedMap();
		if (CacheUtilities.volatileDatasource(map)) {
			String[] params = {"Multi", "null", map.getName(), "null"};
			CacheUtilities.startProcess(params, DialogCacheBuilder.class.getName(), LogWriter.BUILD_CACHE);
		}
	}

	@Override
	public boolean enable() {
		return CacheUtilities.selectedMapIsEnabled();
	}
}