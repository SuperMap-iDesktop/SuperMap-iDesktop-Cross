package com.supermap.desktop.CtrlAction.spatialQuery;

import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.ComponentBorderPanel.CompTitledPane;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;

import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;

/**
 * @author XiaJT
 */
public class JDialogSpatialQuery extends SmDialog {

	private JToolBar toolBar = new JToolBar();
	private SmButton buttonSelectAll = new SmButton();
	private SmButton buttonInvert = new SmButton();
	private SmButton buttonReset = new SmButton();
	private JLabel labelSearchLayer = new JLabel();
	private JComboBox<Layer> comboBoxSearchLayer = new JComboBox<>();
	private JLabel labelSelectedCount = new JLabel();
	private JScrollPane scrollPane = new JScrollPane();
	private JTable tableLayers = new JTable();
	private TableModelSpatialQuery tableModelSpatialQuery;
	private JPanel panelDescribe = new JPanel();

	private CompTitledPane compTitledPaneSearchResult;
	private JPanel panelSearchResult;
	private JCheckBox checkBoxSaveResult = new JCheckBox();
	private JLabel labelDatasource = new JLabel();
	private JComboBox<Datasource> comboBoxDatasource = new JComboBox<>();
	private JLabel labelDataset = new JLabel();
	private SmTextFieldLegit smTextFieldLegitDataset = new SmTextFieldLegit();
	private JCheckBox checkBoxOnlySaveSpatial = new JCheckBox();

	private JPanel panelResultShowWay = new JPanel();
	private JCheckBox checkBoxShowInTabular = new JCheckBox();
	private JCheckBox checkBoxShowInMap = new JCheckBox();
//	private JCheckBox checkBoxShowInScene = new JCheckBox();

	private JPanel panelButton = new JPanel();
	private JCheckBox checkBoxAutoClose = new JCheckBox();
	private SmButton smButtonOK = new SmButton();
	private SmButton smButtonCancel = new SmButton();

	public JDialogSpatialQuery() {
		init();
	}

	private void init() {
		initComponent();
		initLayout();
		initListener();
		initResources();
		initComponentState();
	}

	private void initComponent() {
		compTitledPaneSearchResult = new CompTitledPane(checkBoxSaveResult, panelSearchResult);

		IForm activeForm = Application.getActiveApplication().getActiveForm();
		tableModelSpatialQuery = new TableModelSpatialQuery();
		if (activeForm instanceof IFormMap) {
			tableModelSpatialQuery.setLayers(MapUtilities.getLayers((((IFormMap) activeForm).getMapControl()).getMap()));
		} else {
			tableModelSpatialQuery.setLayers(null);
		}
		this.setSize(new Dimension(400, 250));
		tableLayers.setModel(tableModelSpatialQuery);
		this.getRootPane().setDefaultButton(smButtonOK);
	}

	//region 初始化布局
	private void initLayout() {
		initToolBar();
		initPanelSearchResult();
		initPanelResultShowWay();
		initPanelButtons();
		scrollPane.setViewportView(tableLayers);
		this.setLayout(new GridBagLayout());
		this.add(toolBar, new GridBagConstraintsHelper(0, 0, 2, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 0, 0));
		this.add(scrollPane, new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 0, 0));
		this.add(panelSearchResult, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(0.5, 0).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 0, 0));
		this.add(panelResultShowWay, new GridBagConstraintsHelper(1, 2, 1, 1).setWeight(0.5, 0).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 0, 0));
		this.add(panelButton, new GridBagConstraintsHelper(0, 3, 2, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 0, 0).setAnchor(GridBagConstraints.CENTER));

	}

	private void initToolBar() {
		toolBar.setLayout(new GridBagLayout());
		toolBar.add(buttonSelectAll, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 0, 0));
		toolBar.add(buttonInvert, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0));
		toolBar.add(buttonReset, new GridBagConstraintsHelper(2, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0));
		toolBar.add(ToolbarUIUtilities.getVerticalSeparator(), new GridBagConstraintsHelper(3, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0));
		toolBar.add(labelSearchLayer, new GridBagConstraintsHelper(4, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0));
		toolBar.add(comboBoxSearchLayer, new GridBagConstraintsHelper(5, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0).setIpad(250, 0));
		toolBar.add(labelSelectedCount, new GridBagConstraintsHelper(6, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0));
		toolBar.add(new JPanel(), new GridBagConstraintsHelper(7, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0));
	}

	private void initPanelSearchResult() {
		panelSearchResult.setLayout(new GridBagLayout());
		panelSearchResult.add(labelDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 0, 0));
		panelSearchResult.add(comboBoxDatasource, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0).setFill(GridBagConstraints.HORIZONTAL));

		panelSearchResult.add(labelDataset, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0).setFill(GridBagConstraints.NONE));
		panelSearchResult.add(smTextFieldLegitDataset, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0).setFill(GridBagConstraints.HORIZONTAL));

		panelSearchResult.add(checkBoxOnlySaveSpatial, new GridBagConstraintsHelper(0, 2, 2, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 0, 0).setFill(GridBagConstraints.HORIZONTAL));
		panelSearchResult.add(new JPanel(), new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));

	}

	private void initPanelResultShowWay() {
		panelResultShowWay.setBorder(BorderFactory.createTitledBorder(DataViewProperties.getString("String_DisplayResult")));

		panelResultShowWay.setLayout(new GridBagLayout());
		panelResultShowWay.add(checkBoxShowInTabular, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 0, 0));
		panelResultShowWay.add(checkBoxShowInMap, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 0, 0));
//		panelResultShowWay.add(checkBoxShowInScene, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 0, 0));
		panelResultShowWay.add(new JPanel(), new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
	}

	private void initPanelButtons() {
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(checkBoxAutoClose, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 0, 0));
		panelButton.add(smButtonOK, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 0, 0));
		panelButton.add(smButtonCancel, new GridBagConstraintsHelper(2, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setInsets(0, 5, 0, 0));
	}
	//endregion

	private void initListener() {

	}

	private void initResources() {
		this.setTitle(DataViewProperties.getString("String_SpatialQuery"));
		panelDescribe.setBorder(BorderFactory.createTitledBorder(DataViewProperties.getString("String_GroupBoxSQModeDescription")));
		labelSearchLayer.setText(DataViewProperties.getString("String__LabelSearchingLayer"));
		labelSelectedCount.setText(MessageFormat.format(DataViewProperties.getString("String_CountofFeaturesSelecte"), 0));
		checkBoxSaveResult.setText(DataViewProperties.getString("String_SaveResult"));
		labelDatasource.setText(DataViewProperties.getString("String_SQLQueryLabelDatasource"));
		labelDataset.setText(DataViewProperties.getString("String_SQLQueryLabelDataset"));
		checkBoxOnlySaveSpatial.setText(DataViewProperties.getString("String_OnlySaveSpatialInfo"));
		checkBoxShowInTabular.setText(DataViewProperties.getString("String_ShowResultInTabular"));
		checkBoxShowInMap.setText(DataViewProperties.getString("String_ShowResultInMap"));
		checkBoxAutoClose.setText(CommonProperties.getString("String_CheckBox_CloseDialog"));
		smButtonOK.setText(DataViewProperties.getString("String_Query"));
		smButtonCancel.setText(CommonProperties.getString(CommonProperties.Close));

		this.buttonSelectAll.setIcon(CoreResources.getIcon("coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
		this.buttonSelectAll.setToolTipText(CommonProperties.getString(CommonProperties.selectAll));
		this.buttonInvert.setIcon(CoreResources.getIcon("coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
		this.buttonInvert.setToolTipText(CommonProperties.getString(CommonProperties.selectInverse));
		this.buttonReset.setIcon(CoreResources.getIcon("coreresources/ToolBar/Image_ToolButton_UndoSysDefault.png"));
		this.buttonReset.setToolTipText(CommonProperties.getString(CommonProperties.Reset));
	}

	private void initComponentState() {

	}
}
