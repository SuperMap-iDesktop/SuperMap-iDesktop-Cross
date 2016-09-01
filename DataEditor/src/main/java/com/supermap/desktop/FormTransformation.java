package com.supermap.desktop;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.desktop.CtrlAction.transformationForm.FormTransformationTableModel;
import com.supermap.desktop.CtrlAction.transformationForm.TransformationBean;
import com.supermap.desktop.CtrlAction.transformationForm.TransformationMain;
import com.supermap.desktop.CtrlAction.transformationForm.TransformationReference;
import com.supermap.desktop.Interface.IContextMenuManager;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormTransformation;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.event.ActiveLayersChangedListener;
import com.supermap.desktop.ui.FormBaseChild;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SortTable.SmSortTable;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.ui.MapControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author XiaJT
 */
public class FormTransformation extends FormBaseChild implements IFormTransformation, IFormMap {
	private JPopupMenu tableContextMenu;
	private JPopupMenu formTransformationContextMenu;
	private JTable tablePoints;
	private JSplitPane splitPaneMapControls;
	private JSplitPane splitPaneMain;
	private FormTransformationTableModel formTransformationTableModel;
	private TransformationMain transformationMain;
	private TransformationReference transformationReference;
	private IFormMap currentForceWindow;
	private ArrayList<Object> transformationObjects = new ArrayList<>();
	private ArrayList<Object> transformationReferenceObjects = new ArrayList<>();
	private MouseAdapter mapControlMouseAdapter = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {
			Object source = e.getSource();
			if (source != currentForceWindow.getMapControl()) {
				currentForceWindow.deactived();
				currentForceWindow = currentForceWindow == transformationMain ? transformationReference : transformationMain;
				currentForceWindow.actived();
				Application.getActiveApplication().getMainFrame().getFormManager().resetActiveForm();
			}
			if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1) {
				getFormTransformationContextMenu().show(getMapControl(), e.getX(), e.getY());
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO: 2016/9/1 投影信息修改
		}


	};

	public FormTransformation() {
		this(null);
	}

	public FormTransformation(String name) {
		this(name, null, null);
	}

	public FormTransformation(String name, Icon icon, Component component) {
		super(name, icon, component);
		setText(name);
		transformationMain = new TransformationMain(this);
		transformationReference = new TransformationReference(this);
		currentForceWindow = transformationMain;
		currentForceWindow.actived();
		formTransformationTableModel = new FormTransformationTableModel();
		tablePoints = new SmSortTable();
		tablePoints.setModel(formTransformationTableModel);
		if (Application.getActiveApplication().getMainFrame() != null) {
			IContextMenuManager manager = Application.getActiveApplication().getMainFrame().getContextMenuManager();
			this.formTransformationContextMenu = (JPopupMenu) manager.get("SuperMap.Desktop.FormTransformation.TransformationMapsContextMenu");
			this.tableContextMenu = (JPopupMenu) manager.get("SuperMap.Desktop.FormTransformation.TransformationItemsContextMenu");
		}
		initLayout();
		initListener();
	}

	private void initLayout() {
		this.setLayout(new GridBagLayout());
		this.splitPaneMapControls = new JSplitPane();
		this.splitPaneMapControls.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		this.splitPaneMapControls.setLeftComponent(transformationMain.getMapControl());
		this.splitPaneMapControls.setRightComponent(transformationReference.getMapControl());
		splitPaneMapControls.setResizeWeight(0.5);

		this.splitPaneMain = new JSplitPane();
		this.splitPaneMain.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.splitPaneMain.setLeftComponent(this.splitPaneMapControls);
		this.splitPaneMain.setRightComponent(new JScrollPane(tablePoints));
		splitPaneMain.setResizeWeight(1);

		this.add(this.splitPaneMain, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1));

		this.add(getStatusbar(), new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
	}

	private void initListener() {
		//region 一次性事件
		splitPaneMain.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				splitPaneMain.setDividerLocation(0.8);
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						if (transformationReferenceObjects.size() > 0) {
							transformationReference.addDatas(transformationReferenceObjects);
							transformationReferenceObjects.clear();
						}
						if (transformationObjects.size() > 0) {
							transformationMain.addDatas(transformationObjects);
							transformationObjects.clear();
						}
					}
				});
				splitPaneMain.removeComponentListener(this);
			}
		});
		//endregion
		addMapControlListener();
	}

	private void addMapControlListener() {
		transformationMain.getMapControl().addMouseListener(mapControlMouseAdapter);
		transformationReference.getMapControl().addMouseListener(mapControlMouseAdapter);
	}

	@Override
	public String getText() {
		return null;
	}


	@Override
	public WindowType getWindowType() {
		return WindowType.TRANSFORMATION;
	}

	@Override
	public boolean save() {
		return true;
	}

	@Override
	public boolean save(boolean notify, boolean isNewWindow) {
		return true;
	}

	@Override
	public boolean saveFormInfos() {
		return false;
	}

	@Override
	public boolean saveAs(boolean isNewWindow) {
		return false;
	}

	@Override
	public boolean isNeedSave() {
		return false;
	}

	@Override
	public void setNeedSave(boolean needSave) {

	}

	@Override
	public boolean isActivated() {
		return false;
	}

	@Override
	public void actived() {
		currentForceWindow.actived();
		Application.getActiveApplication().getMainFrame().getPropertyManager().setProperty(null);
	}

	@Override
	public void deactived() {
		currentForceWindow.deactived();
	}

	@Override
	public void windowShown() {


	}

	@Override
	public void windowHidden() {

	}

	@Override
	public void clean() {
		transformationMain.clean();
		transformationReference.clean();
	}


	@Override
	public void addTransformationDataset(Dataset transformationDataset, Datasource resultDatasource, String resultDatasetName) {
		TransformationBean transformationBean = new TransformationBean(transformationDataset, resultDatasource, resultDatasetName);
		ArrayList<Object> datas = new ArrayList<>();
		datas.add(transformationBean);
		if (getWidth() != 0) {
			transformationMain.addDatas(datas);
		} else {
			transformationObjects = new ArrayList<>();
			transformationObjects.add(transformationBean);
		}
	}

	@Override
	public void addTransformationMap(Map map) {
		TransformationBean transformationBean = new TransformationBean(map);
		ArrayList<Object> datas = new ArrayList<>();
		datas.add(transformationBean);
		if (getWidth() != 0) {
			transformationMain.addDatas(datas);
		} else {
			transformationObjects = new ArrayList<>();
			transformationObjects.add(transformationBean);
		}
	}

	@Override
	public void addReferenceObjects(List<Object> listObjects) {
		if (getWidth() == 0) {
			for (Object listObject : listObjects) {
				transformationReferenceObjects.add(listObject);
			}
		} else {
			transformationReference.addDatas(listObjects);
		}
	}


	@Override
	public MapControl getMapControl() {
		return currentForceWindow.getMapControl();
	}

	@Override
	public Layer[] getActiveLayers() {
		return currentForceWindow.getActiveLayers();
	}

	@Override
	public void setActiveLayers(Layer... activeLayers) {
		currentForceWindow.setActiveLayers(activeLayers);
	}

	@Override
	public void addActiveLayersChangedListener(ActiveLayersChangedListener listener) {
		currentForceWindow.addActiveLayersChangedListener(listener);
	}

	@Override
	public void removeActiveLayersChangedListener(ActiveLayersChangedListener listener) {
		currentForceWindow.removeActiveLayersChangedListener(listener);
	}

	@Override
	public void removeActiveLayersByDatasets(Dataset... datasets) {
		transformationMain.removeActiveLayersByDatasets(datasets);
		transformationReference.removeActiveLayersByDatasets(datasets);
	}

	private JPopupMenu getFormTransformationContextMenu() {
		return formTransformationContextMenu;
	}

	private JPopupMenu getTableContextMenu() {
		return tableContextMenu;
	}

	//region 不需要的方法
	@Override
	public void dontShowPopupMenu() {

	}

	@Override
	public void showPopupMenu() {

	}

	@Override
	public int getIsShowPopupMenu() {
		return 0;
	}

	@Override
	public void updataSelectNumber() {

	}

	@Override
	public void setSelectedGeometryProperty() {

	}

	@Override
	public void openMap(String mapName) {

	}

	@Override
	public int getSelectedCount() {
		return 0;
	}

	@Override
	public void removeLayers(Layer[] activeLayers) {
		currentForceWindow.removeLayers(activeLayers);
	}

	@Override
	public void setText(String text) {

	}
	//endregion
}
