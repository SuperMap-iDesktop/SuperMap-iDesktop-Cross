package com.supermap.desktop.CtrlAction.CADStyle;

import com.supermap.data.DatasetType;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.event.ActiveFormChangedEvent;
import com.supermap.desktop.event.ActiveFormChangedListener;
import com.supermap.desktop.event.DockbarClosedEvent;
import com.supermap.desktop.event.DockbarClosedListener;
import com.supermap.desktop.geometryoperation.EditControllerAdapter;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.IEditController;
import com.supermap.desktop.geometryoperation.NullEditController;
import com.supermap.desktop.geometryoperation.editor.AbstractEditor;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapOpenedEvent;
import com.supermap.mapping.MapOpenedListener;
import com.supermap.ui.*;

import java.util.ArrayList;

/**
 * Created by xie on 2016/8/10.
 */
public class CADStyleEditor extends AbstractEditor {

	private final String CADSTYLECONTAINER = "com.supermap.desktop.CtrlAction.CADStyle.CADStyleContainer";

	private CADStyleContainer cadStyleContainer;
	private IDockbar dockbarCADStyleContainer;

	private GeometrySelectChangedListener geometrySelectChangedListener = new GeometrySelectChangedListener() {
		@Override
		public void geometrySelectChanged(GeometrySelectChangedEvent geometrySelectChangedEvent) {
			if (geometrySelectChangedEvent.getCount() > 0) {
				// FIXME: 2016/12/30 用geometryselected事件修改cad风格显示面板
				ArrayList<Recordset> recordsets = CADStyleUtilities.getActiveRecordset(((MapControl) geometrySelectChangedEvent.getSource()).getMap());
				if (null != cadStyleContainer && null != recordsets && null != dockbarCADStyleContainer) {
					cadStyleContainer.setModify(false);
					cadStyleContainer.showDialog(recordsets);
				}
			} else {
				if (null != cadStyleContainer && null != dockbarCADStyleContainer) {
					cadStyleContainer.enabled(false);
				}
			}
		}

	};


	@Override
	public void activate(final EditEnvironment environment) {
		try {
			dockbarCADStyleContainer = Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(CADSTYLECONTAINER));
			if (dockbarCADStyleContainer != null && null != dockbarCADStyleContainer.getInnerComponent()) {
				dockbarCADStyleContainer.setVisible(true);
				dockbarCADStyleContainer.active();
				cadStyleContainer = (CADStyleContainer) dockbarCADStyleContainer.getInnerComponent();
				ArrayList<Recordset> recordsets = CADStyleUtilities.getActiveRecordset(environment.getMap());
				if (null != recordsets) {
					cadStyleContainer.init(recordsets);
				}
			}

			Application.getActiveApplication().getMainFrame().getDockbarManager().addDockbarClosedListener(new DockbarClosedListener() {
				@Override
				public void dockbarClosed(DockbarClosedEvent e) {
					// 关闭dockbar时，关闭编辑
					environment.stopEditor();
				}
			});

			registEvents(environment);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void registEvents(final EditEnvironment environment) {
		removeEvents(environment);
		environment.getMapControl().addGeometrySelectChangedListener(this.geometrySelectChangedListener);

		// 这个先暂时这样，因为 EditEnvironment 绑定了 Form，之后再做一个 EditEnvironment 的激活机制就可以了
		Application.getActiveApplication().getMainFrame().getFormManager().addActiveFormChangedListener(new ActiveFormChangedListener() {
			@Override
			public void activeFormChanged(ActiveFormChangedEvent e) {
				if (e.getNewActiveForm() != null && !e.getNewActiveForm().equals(environment.getFormMap())) {
					if (null != cadStyleContainer && null != dockbarCADStyleContainer) {
						cadStyleContainer.enabled(false);
					}
				} else if (e.getNewActiveForm() == null) {// 2017/1/11   针对CAD面板打开的情况下，关闭所有地图窗口抛异常的缺陷进行修改  lixiaoyao
					cadStyleContainer.setNullPanel();
				} else {
					if (null != cadStyleContainer && null != dockbarCADStyleContainer) {
						cadStyleContainer.enabled(true);
					}
				}
			}
		});
	}

	private void removeEvents(EditEnvironment environment) {
		environment.getMapControl().removeGeometrySelectChangedListener(this.geometrySelectChangedListener);
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		removeEvents(environment);
	}


	@Override
	public boolean enble(EditEnvironment environment) {
		// FIXME: 2016/12/30 enable()方法中集成的响应太多，包括地图刷新事件，而cad操作中包含很多地图刷新事件会造成文本风格中事件的重复调用,将选择事件单独移出
		boolean result = ListUtilities.isListContainAny(environment.getEditProperties().getSelectedDatasetTypes(), DatasetType.CAD, DatasetType.TEXT)
				&& isEditable(environment.getMap());
		return result;
	}

	private boolean isEditable(Map map) {
		ArrayList<Recordset> recordset = CADStyleUtilities.getActiveRecordset(map);
		if (null == recordset) {
			return true;
		}
		int count = recordset.size();
		try {
			ArrayList<Layer> layers = MapUtilities.getLayers(map);
			for (Layer layer : layers) {
				try {
					for (int i = 0; i < count; i++) {
						if (layer.isEditable()) {
							return true;
						}
					}
				} catch (Exception e) {
					// ignore
				} finally {
					if (recordset != null) {
						for (int i = 0; i < count; i++) {
							recordset.get(i).dispose();
						}
					}
				}
			}
		} catch (Exception ignore) {
			// 地图dispose没接口判断
		}
		return false;
	}

	@Override
	public boolean check(EditEnvironment environment) {
		return true;
	}

}
