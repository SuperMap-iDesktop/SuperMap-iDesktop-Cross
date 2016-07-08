package com.supermap.desktop;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.desktop.Interface.IFormTransformation;
import com.supermap.desktop.controls.utilities.MapViewUIUtilities;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.ui.FormBaseChild;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.ui.MapControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class FormTransformation extends FormBaseChild implements IFormTransformation {
	private MapControl mapControlTransformation;
	private MapControl mapControlReference;
	private Datasource resultDatasource;
	private String resultDatasetName;
	private Dataset transformationDataset;
	private java.util.List<Dataset> referenceDatasetList;
	private JTable tablePoints;
	private JSplitPane splitPaneMapControls;
	private JSplitPane splitPaneMain;

	public FormTransformation() {
		this(null);

	}

	public FormTransformation(String name) {
		this(name, null, null);
		setText(name);
	}

	public FormTransformation(String name, Icon icon, Component component) {
		super(name, icon, component);
		mapControlTransformation = new MapControl();
		mapControlTransformation.getMap().setWorkspace(Application.getActiveApplication().getWorkspace());
		mapControlReference = new MapControl();
		mapControlReference.getMap().setWorkspace(Application.getActiveApplication().getWorkspace());
		referenceDatasetList = new ArrayList<>();
		tablePoints = new JTable();
		initLayout();
		initListener();
	}

	private void initLayout() {
		this.setLayout(new GridBagLayout());
		this.splitPaneMapControls = new JSplitPane();
		this.splitPaneMapControls.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		this.splitPaneMapControls.setLeftComponent(mapControlTransformation);
		this.splitPaneMapControls.setRightComponent(mapControlReference);

		this.splitPaneMain = new JSplitPane();
		this.splitPaneMain.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.splitPaneMain.setLeftComponent(this.splitPaneMapControls);
		this.splitPaneMain.setRightComponent(new JScrollPane(tablePoints));

		this.add(this.splitPaneMain, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		// FIXME: 2016/7/8 状态栏为空
//		this.add(getStatusbar(), new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
	}

	private void initListener() {
		splitPaneMain.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				splitPaneMain.setDividerLocation(0.8);
				splitPaneMain.removeComponentListener(this);
			}
		});
		splitPaneMapControls.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				splitPaneMapControls.setDividerLocation(0.5);
				splitPaneMapControls.removeComponentListener(this);
			}
		});
	}

	@Override
	public String getText() {
		return null;
	}

	@Override
	public void setText(String text) {

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

	}

	@Override
	public void setTransformationDataset(Dataset transformationDataset) {
		this.transformationDataset = transformationDataset;
		MapViewUIUtilities.addDatasetsToMap(mapControlTransformation.getMap(), new Dataset[]{transformationDataset}, true);
	}

	@Override
	public void addReferenceDataset(Dataset referenceDataset) {
		if (referenceDataset != null) {
			referenceDatasetList.add(referenceDataset);
			MapViewUIUtilities.addDatasetsToMap(mapControlReference.getMap(), new Dataset[]{referenceDataset}, true);
		}
	}

	@Override
	public void setResultDataSource(Datasource resultDatasource) {
		this.resultDatasource = resultDatasource;
	}

	@Override
	public void setResultDatasetName(String resultDatasetName) {
		this.resultDatasetName = resultDatasetName;
	}
}
