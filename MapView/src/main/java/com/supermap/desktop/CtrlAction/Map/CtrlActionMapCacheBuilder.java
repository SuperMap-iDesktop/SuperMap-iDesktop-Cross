package com.supermap.desktop.CtrlAction.Map;

import com.supermap.data.Datasource;
import com.supermap.data.processing.MapCacheBuilder;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.dialog.SmOptionPane;
import com.supermap.desktop.dialog.cacheClip.DialogCacheBuilder;
import com.supermap.desktop.dialog.cacheClip.DialogMapCacheClip;
import com.supermap.desktop.dialog.cacheClip.DialogMapCacheClipBuilder;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.implement.ToolbarUIUtilties;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;

import java.util.ArrayList;

/**
 * Created by lixiaoyao on 2017/3/15.
 */
public class CtrlActionMapCacheBuilder extends CtrlAction {
	public CtrlActionMapCacheBuilder(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
//        IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
//        DialogMapCacheBuilder dialogMapCacheBuilder = new DialogMapCacheBuilder((JFrame) Application.getActiveApplication().getMainFrame(), true, formMap.getMapControl().getMap());
//        DialogResult result = dialogMapCacheBuilder.showDialog();
		DialogMapCacheClip dialogMapCacheClip = new DialogMapCacheClip();
		if (dialogMapCacheClip.showDialog() == DialogResult.OK) {
			Datasource datasource = ((IFormMap) Application.getActiveApplication().getActiveForm()).getActiveLayers()[0].getDataset().getDatasource();
			if (!datasource.isReadOnly() && !dialogMapCacheClip.isSingleProcess()) {
				SmOptionPane pane = new SmOptionPane();
				pane.showConfirmDialog(MapViewProperties.getString("String_DatasourceOpenedNotReadOnly"));
				return;
			}
			datasource.getWorkspace().save();
			ToolbarUIUtilities.updataToolbarsState();
			Map map = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().getMap();
			MapCacheBuilder mapCacheBuilder = new MapCacheBuilder();
			Map newMap = new Map(Application.getActiveApplication().getWorkspace());
			newMap.fromXML(map.toXML());
			mapCacheBuilder.setMap(newMap);
			if (dialogMapCacheClip.isBuildTask()) {
				int cmdType = dialogMapCacheClip.isSingleProcess() ? DialogMapCacheClipBuilder.SingleProcessClip : DialogMapCacheClipBuilder.MultiProcessClip;
				new DialogMapCacheClipBuilder(cmdType, mapCacheBuilder).showDialog();
			} else {
				DialogCacheBuilder cacheBuilder = new DialogCacheBuilder(DialogMapCacheClipBuilder.MultiProcessClip);
				cacheBuilder.textFieldMapName.setText(map.getName());
				cacheBuilder.showDialog();
			}
		}
	}

	@Override
	public boolean enable() {
		boolean result = false;
		IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
		if (formMap != null) {
			ArrayList<Layer> arrayList = MapUtilities.getLayers(formMap.getMapControl().getMap(), true);
			if (arrayList.size() > 0) {
				result = true;
			}
		}
		return result;
	}
}
