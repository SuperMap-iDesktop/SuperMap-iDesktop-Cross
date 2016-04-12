package com.supermap.desktop.datatopology.CtrlAction;

import com.supermap.data.CursorType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.data.Recordset;
import com.supermap.data.topology.TopologyProcessingOptions;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.datatopology.DataTopologyProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SQLExpressionDialog;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilties.StringUtilties;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class JDialogTopoAdvance extends SmDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textFieldFilterExpression;
	private JTextField textFieldOvershootsTolerance = new JTextField();
	private JTextField textFieldUndershootsTolerance = new JTextField();
	private JTextField textFieldVertexTorance = new JTextField();
	private JLabel labelOvershootsTolerance = new JLabel("String_Label_OvershootsTolerance");
	private JLabel labelUndershootsTolerance = new JLabel("String_Label_UndershootsTolerance");
	private JLabel labelVertexTolerance = new JLabel("String_Label_VertexTolerance");
	private JLabel labelFilterExpression = new JLabel("String_FilterExpression");
	private JLabel labelNotCutting = new JLabel("String_NotCutting");
	private JPanel panelLinesIntersected = new JPanel();
	private JPanel panelToleranceSetting = new JPanel();
	private SmButton buttonMore = new SmButton("...");
	private SmButton buttonSure = new SmButton("String_Button_OK");
	private SmButton buttonQuite = new SmButton("String_Button_Cancel");
	private transient TopologyProcessingOptions topologyProcessingOptions = new TopologyProcessingOptions();
	private DatasetComboBox comboBoxNotCutting;
	private SQLExpressionDialog sqlExpressionDialog;

	private transient DatasetVector targetDataset;
	private transient Datasource datasource;
	private DialogResult dialogResult;

	private CommonButtonListener listener = new CommonButtonListener();

	/**
	 * @wbp.parser.constructor
	 */
	public JDialogTopoAdvance(JDialog owner, boolean model, Datasource datasource) {
		super(owner, model);
		this.datasource = datasource;
		setLocationRelativeTo(owner);
		initComponents();
		initResources();
		registActionListener();
		initTraversalPolicy();
	}

	public JDialogTopoAdvance(JDialog owner, boolean model, TopologyProcessingOptions topologyProcessingOptions, DatasetVector targetDataset,
			Datasource datasource) {
		super(owner, model);
		setLocationRelativeTo(owner);
		this.datasource = datasource;
		this.topologyProcessingOptions = topologyProcessingOptions;
		this.targetDataset = targetDataset;
		initComponents();
		initResources();
		registActionListener();
		initTraversalPolicy();
	}

	private void initTraversalPolicy() {
		if (this.componentList.size() > 0) {
			componentList.clear();
		}
		this.componentList.add(this.buttonSure);
		this.componentList.add(this.buttonQuite);
		this.componentList.add(this.buttonMore);
		this.setFocusTraversalPolicy(policy);
	}

	private void registActionListener() {
		this.buttonSure.addActionListener(this.listener);
		this.buttonQuite.addActionListener(this.listener);
		this.buttonMore.addActionListener(this.listener);
	}

	private void unregistActionListener() {
		this.buttonSure.removeActionListener(this.listener);
		this.buttonQuite.removeActionListener(this.listener);
		this.buttonMore.removeActionListener(this.listener);
	}

	private void initResources() {
		setTitle(DataTopologyProperties.getString("String_Form_AdvanceSettings"));
		this.labelOvershootsTolerance.setText(DataTopologyProperties.getString("String_Label_OvershootsTolerance"));
		this.labelUndershootsTolerance.setText(DataTopologyProperties.getString("String_Label_UndershootsTolerance"));
		this.labelVertexTolerance.setText(DataTopologyProperties.getString("String_Label_VertexTolerance"));
		this.labelFilterExpression.setText(DataTopologyProperties.getString("String_FilterExpression"));
		this.labelNotCutting.setText(DataTopologyProperties.getString("String_NotCutting"));
		this.buttonSure.setText(CommonProperties.getString("String_Button_OK"));
		this.buttonQuite.setText(CommonProperties.getString("String_Button_Cancel"));
		this.panelLinesIntersected.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), DataTopologyProperties
				.getString("String_LinesIntersected"), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		this.panelToleranceSetting.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), DataTopologyProperties
				.getString("String_GroupBox_ToleranceSetting"), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
	}

	private void initComponents() {
		initComboBoxItem();
		setSize(320, 305);
		initContentPane();
		initPanelToleranceSetting();
		initPanelLinesIntersected();
		getRootPane().setDefaultButton(buttonSure);
	}

	private void initPanelLinesIntersected() {
		// @formatter:off
		this.textFieldFilterExpression = new JTextField();
		this.textFieldFilterExpression.setColumns(10);
		this.panelLinesIntersected.setLayout(new GridBagLayout());
		this.panelLinesIntersected.add(this.labelFilterExpression,     new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setInsets(10,10,5,5));
		this.panelLinesIntersected.add(this.textFieldFilterExpression, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(60, 0).setInsets(10,0,5,5).setIpad(20, 0));
		this.panelLinesIntersected.add(this.buttonMore,                new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(5, 0).setInsets(10,0,5,10));
		this.panelLinesIntersected.add(this.labelNotCutting,           new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setInsets(0,10,10,5));
		this.panelLinesIntersected.add(this.comboBoxNotCutting,        new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(100, 0).setInsets(0,0,10,10));
		// @formatter:on
	}

	private void initPanelToleranceSetting() {
		// @formatter:off
		this.textFieldOvershootsTolerance.setColumns(10);
		this.textFieldUndershootsTolerance.setColumns(10);
		this.textFieldVertexTorance.setColumns(10);
		this.panelToleranceSetting.setLayout(new GridBagLayout());
		this.panelToleranceSetting.add(this.labelOvershootsTolerance,      new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setInsets(10,10,5,5));
		this.panelToleranceSetting.add(this.textFieldOvershootsTolerance,  new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(60, 0).setInsets(10,0,5,10));
		this.panelToleranceSetting.add(this.labelUndershootsTolerance,     new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setInsets(0,10,5,5));
		this.panelToleranceSetting.add(this.textFieldUndershootsTolerance, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(60, 0).setInsets(0,0,5,10));
		this.panelToleranceSetting.add(this.labelVertexTolerance,          new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setInsets(0,10,10,5));
		this.panelToleranceSetting.add(this.textFieldVertexTorance,        new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(60, 0).setInsets(0,0,10,10));
		// @formatter:on
	}

	private void initContentPane() {
		// @formatter:off
		getContentPane().setLayout(new GridBagLayout());
		getContentPane().add(this.panelLinesIntersected, new GridBagConstraintsHelper(0, 0, 4, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(3, 1).setInsets(10, 10, 5, 10));
		getContentPane().add(this.panelToleranceSetting, new GridBagConstraintsHelper(0, 1, 4, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(3, 1).setInsets(0, 10, 5, 10));
		getContentPane().add(this.buttonSure,            new GridBagConstraintsHelper(2, 2, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(1, 0).setInsets(0, 0, 10, 10));
		getContentPane().add(this.buttonQuite,           new GridBagConstraintsHelper(3, 2, 1, 1).setAnchor(GridBagConstraints.EAST).setInsets(0, 0, 10, 10));
		// @formatter:on
	}

	private void initComboBoxItem() {
		try {
			if (null != targetDataset) {
				this.comboBoxNotCutting = new DatasetComboBox(new Dataset[0]);
				for (int i = 0; i < targetDataset.getDatasource().getDatasets().getCount(); i++) {
					Dataset tempDataset = targetDataset.getDatasource().getDatasets().get(i);
					if (tempDataset.getType() == DatasetType.POINT) {
						DataCell cell = new DataCell();
						cell.initDatasetType(tempDataset);
						this.comboBoxNotCutting.addItem(cell);
					}
				}
				this.textFieldOvershootsTolerance.setText(Double.toString(targetDataset.getTolerance().getDangle()));
				this.textFieldUndershootsTolerance.setText(Double.toString(targetDataset.getTolerance().getExtend()));
				this.textFieldVertexTorance.setText(Double.toString(targetDataset.getTolerance().getNodeSnap()));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	class CommonButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JComponent c = (JComponent) e.getSource();
			if (buttonSure == c) {
				buttonSureClicked();
			}
			if (buttonQuite == c) {
				quiteButtonClicked();
			}
			if (buttonMore == c) {
				addItemToTextFieldFilterExpression();
			}
		}

		private void addItemToTextFieldFilterExpression() {
			sqlExpressionDialog = new SQLExpressionDialog();
			Dataset[] datasets = new Dataset[1];
			datasets[0] = targetDataset;
			DialogResult dialogResult = sqlExpressionDialog.showDialog("", datasets);
			if (dialogResult == DialogResult.OK) {
				String filter = sqlExpressionDialog.getQueryParameter().getAttributeFilter();
				if (filter != null && !"".equals(filter.trim())) {
					textFieldFilterExpression.setText(filter);
				}
			}
		}
	}

	private void setTopologyInfo() {
		try {
			String arcFilterString = this.textFieldFilterExpression.getText();
			if (!StringUtilties.isNullOrEmpty(arcFilterString)) {
				this.topologyProcessingOptions.setArcFilterString(arcFilterString);
			}
			if (0 < this.comboBoxNotCutting.getItemCount()) {
				String datasetName = this.comboBoxNotCutting.getSelectItem();
				DatasetVector dataset = (DatasetVector) CommonToolkit.DatasetWrap.getDatasetFromDatasource(datasetName, datasource);
				Recordset recordset = dataset.getRecordset(false, CursorType.STATIC);
				this.topologyProcessingOptions.setVertexFilterRecordset(recordset);
				recordset.dispose();
			}

			double overshootsTolerance = Double.parseDouble(this.textFieldOvershootsTolerance.getText());
			this.topologyProcessingOptions.setOvershootsTolerance(overshootsTolerance);
			double undershootsTolerance = Double.parseDouble(this.textFieldUndershootsTolerance.getText());
			this.topologyProcessingOptions.setUndershootsTolerance(undershootsTolerance);
			double vertexTorance = Double.parseDouble(this.textFieldVertexTorance.getText());
			this.topologyProcessingOptions.setVertexTolerance(vertexTorance);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public DialogResult getDialogResult() {
		return this.dialogResult;
	}

	@Override
	public void setDialogResult(DialogResult dialogResult) {
		this.dialogResult = dialogResult;
	}

	private void quiteButtonClicked() {
		setDialogResult(DialogResult.CANCEL);
		unregistActionListener();
		dispose();
	}

	private void buttonSureClicked() {
		setDialogResult(DialogResult.OK);
		setTopologyInfo();
		unregistActionListener();
		dispose();
	}
}
