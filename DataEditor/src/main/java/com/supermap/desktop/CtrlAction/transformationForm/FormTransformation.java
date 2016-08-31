package com.supermap.desktop.CtrlAction.transformationForm;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormTransformation;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.event.ActiveLayersChangedListener;
import com.supermap.desktop.ui.FormBaseChild;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SortTable.SmSortTable;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.ui.MapControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author XiaJT
 */
public class FormTransformation extends FormBaseChild implements IFormTransformation, IFormMap {
	private JTable tablePoints;
	private JSplitPane splitPaneMapControls;
	private JSplitPane splitPaneMain;
	private FormTransformationTableModel formTransformationTableModel;
	private int rightHeight;
	private TransformationMain transformationMain;
	private TransformationReference transformationReference;
	private IFormMap currentForceWindow;

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
		formTransformationTableModel = new FormTransformationTableModel();
		tablePoints = new SmSortTable();
		tablePoints.setModel(formTransformationTableModel);
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
		// FIXME: 2016/7/8 状态栏为空

//		this.add(getStatusbar(), new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
	}

	private void initListener() {
		splitPaneMain.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				splitPaneMain.setDividerLocation(0.8);
				splitPaneMain.addComponentListener(new ComponentAdapter() {

					@Override
					public void componentResized(ComponentEvent e) {
						splitPaneMain.setDividerLocation(splitPaneMain.getHeight() - rightHeight);
					}
				});
				rightHeight = splitPaneMain.getHeight() - splitPaneMain.getDividerLocation();
				splitPaneMain.removeComponentListener(this);
			}
		});
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
		UICommonToolkit.getLayersManager().getLayersTree().setMap(currentForceWindow.getMapControl().getMap());
	}

	@Override
	public void deactived() {

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
		transformationMain.addDatas(datas);
	}

	@Override
	public void addTransformationMap(Map map) {
		TransformationBean transformationBean = new TransformationBean(map);
		ArrayList<Object> datas = new ArrayList<>();
		datas.add(transformationBean);
		transformationMain.addDatas(datas);
	}

	@Override
	public void addReferenceObjects(List<Object> listObjects) {
		transformationReference.addDatas(listObjects);
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
	public void setText(String text) {

	}
	//endregion
}
