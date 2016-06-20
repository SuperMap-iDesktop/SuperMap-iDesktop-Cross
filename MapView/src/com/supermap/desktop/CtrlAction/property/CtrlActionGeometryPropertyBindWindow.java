package com.supermap.desktop.CtrlAction.property;

import javax.swing.JPopupMenu;

import net.infonode.util.Direction;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.GeometryPropertyBindWindow.BindProperty;
import com.supermap.desktop.GeometryPropertyBindWindow.BindWindow;
import com.supermap.desktop.GeometryPropertyBindWindow.IPropertyBindWindow;
import com.supermap.desktop.GeometryPropertyBindWindow.PropertyBindWindow;
import com.supermap.desktop.Interface.*;
import com.supermap.desktop.event.*;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DockbarManager;
import com.supermap.desktop.ui.docking.*;
import com.supermap.desktop.utilties.TabularUtilties;
import com.supermap.mapping.*;

public class CtrlActionGeometryPropertyBindWindow extends CtrlAction {
	private SplitWindow splitWindow;
	private IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
	private Map map;
	private IFormTabular tabular;
	private DockingWindow newTabWindow;
	private IPropertyBindWindow propertyBindWindow;

	private ActiveFormChangedListener activeFormChangeListener = new LocalFormChangedListener();
	int tabSize = 0;// 属性表个数

	public CtrlActionGeometryPropertyBindWindow(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			this.map = formMap.getMapControl().getMap();
			TabWindow tabWindow = ((DockbarManager) (Application.getActiveApplication().getMainFrame()).getDockbarManager()).getChildFormsWindow();
			// 获取当前活动图层对应的数据集
			Dataset dataset = formMap.getActiveLayers()[0].getDataset();
			UICommonToolkit.getLayersManager().getLayersTree().getMouseListeners();
			if (null != dataset && dataset instanceof DatasetVector && map.findSelection(true).length > 0) {
				Recordset tempRecordset = map.findSelection(true)[0].toRecordset();
				openTabular(dataset, tempRecordset);
				windowBindProperty(formMap, tabWindow, dataset);
				return;
			}
			if (null != dataset && dataset instanceof DatasetVector) {
				Recordset recordset = ((DatasetVector) dataset).getRecordset(false, CursorType.DYNAMIC);
				openTabular(dataset, recordset);
				windowBindProperty(formMap, tabWindow, dataset);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean enable() {
		
		return null != Application.getActiveApplication().getActiveForm() && Application.getActiveApplication().getActiveForm() instanceof IFormMap;
	}

	private void windowBindProperty(IFormMap formMap, TabWindow tabWindow, Dataset dataset) {
		this.newTabWindow = tabWindow.getChildWindow(tabWindow.getChildWindowCount() - 1);
		this.tabSize += 1;
		if (null == splitWindow) {
			this.splitWindow = tabWindow.split(newTabWindow, Direction.DOWN, 0.5f);
		} else if (splitWindow.getChildWindowCount() > 0) {
			((TabWindow) this.splitWindow.getChildWindow(splitWindow.getChildWindowCount() - 1)).addTab(newTabWindow);
		}
		propertyBindWindow = new PropertyBindWindow();
		propertyBindWindow.setFormMap(formMap);
		propertyBindWindow.setBindProperty(new BindProperty(map));
		propertyBindWindow.setBindWindow(new BindWindow(tabular), dataset);
		propertyBindWindow.registEvents();
		this.newTabWindow.addListener(new DockingWindowAdapter() {

			@Override
			public void windowClosed(DockingWindow window) {
				// 当前属性表关闭时清空map
				tabSize -= 1;
				if (0 == tabSize) {
					splitWindow = null;
				}
			}

		});
		this.formManager.addActiveFormChangedListener(this.activeFormChangeListener);

		Application.getActiveApplication().setActiveForm(formMap);
	}

	private void openTabular(Dataset dataset, Recordset recordset) {
		// 打开一个默认的属性表，然后修改属性表的title和数据与当前图层对应的数据匹配
		this.tabular = TabularUtilties.openDatasetVectorFormTabular(dataset);
		this.tabular.setText(dataset.getName() + "@" + dataset.getDatasource().getAlias());
		this.tabular.getIdMap().clear();
		this.tabular.getRowIndexMap().clear();
		this.tabular.setRecordset(recordset);
	}

	class LocalFormChangedListener implements ActiveFormChangedListener {

		@Override
		public void activeFormChanged(ActiveFormChangedEvent e) {
			if (null == e.getNewActiveForm()) {
				// 当所有地图关闭时将splitWindow设置为空，重新关联,并移除事件
				splitWindow = null;
				propertyBindWindow.removeEvents();
			}
		}

	}

}
