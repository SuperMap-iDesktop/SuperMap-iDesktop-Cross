package com.supermap.desktop.datatopology.CtrlAction;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.data.topology.TopologyProcessingOptions;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.utilities.DatasetUIUtilities;
import com.supermap.desktop.datatopology.DataTopologyProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.progress.FormProgress;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JDialogDatasetLineTopo extends SmDialog {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private SmButton buttonMore = new SmButton("String_Button_Advance");
	private SmButton buttonSure = new SmButton("String_Button_OK");
	private SmButton buttonQuite = new SmButton("String_Button_Cancel");
	private JCheckBox checkboxLinesIntersected = new JCheckBox("String_LinesIntersected");
	private JCheckBox checkboxOvershootsCleaned = new JCheckBox("String_CleanOvershoots");
	private JCheckBox checkboxPseudoNodesCleaned = new JCheckBox("String_CleanPseudoNodes");
	private JCheckBox checkboxAdjacentEndpointsMerged = new JCheckBox("String_MergeAdjacentEndpoints");
	private JCheckBox checkboxDuplicatedLinesCleaned = new JCheckBox("String_CleanDuplicatedLines");
	private JCheckBox checkboxUndershootsExtended = new JCheckBox("String_Label_UndershootsTolerance");
	private JCheckBox checkboxRedundantVerticesCleaned = new JCheckBox("String_RedundantVertices");
	private JLabel labelDatasource = new JLabel("String_Label_Datasource");
	private JLabel labelDataset = new JLabel("String_Label_Dataset");
	private DatasourceComboBox comboBoxDatasource;
	private DatasetComboBox comboBoxDataset;
	private JPanel panelDatasource = new JPanel();
	private JPanel panelTopoProcessingOptions = new JPanel();
	private boolean isAdjacentEndpointsMerged = true;
	private boolean isDuplicatedLinesCleaned = true;
	private boolean isLinesIntersected = true;
	private boolean isOvershootsCleaned = true;
	private boolean isPseudoNodesCleaned = true;
	private boolean isRedundantVerticesCleaned = true;
	private boolean isUndershootsExtended = true;
	private transient TopologyProcessingOptions topologyProcessingOptions = new TopologyProcessingOptions();
	private boolean hasDataset;

	private transient CommonCheckBoxListener checkBoxListener = new CommonCheckBoxListener();
	private transient CommonButtonListener commonButtonListener = new CommonButtonListener();

	public JDialogDatasetLineTopo(JFrame owner, boolean model) {
		super(owner, model);
		initComponents();
		initResources();
		registActionListener();
		this.componentList.add(this.buttonSure);
		this.componentList.add(this.buttonQuite);
		this.componentList.add(this.buttonMore);
		this.setFocusTraversalPolicy(policy);
	}

	private void initResources() {
		setTitle(DataTopologyProperties.getString("String_TopoLineTitle"));
		this.buttonMore.setText(CommonProperties.getString("String_Button_Advance"));
		this.buttonSure.setText(CommonProperties.getString("String_Button_OK"));
		this.buttonQuite.setText(CommonProperties.getString("String_Button_Cancel"));
		this.checkboxLinesIntersected.setText(DataTopologyProperties.getString("String_LinesIntersected"));
		this.checkboxOvershootsCleaned.setText(DataTopologyProperties.getString("String_CleanOvershoots"));
		this.checkboxPseudoNodesCleaned.setText(DataTopologyProperties.getString("String_CleanPseudoNodes"));
		this.checkboxAdjacentEndpointsMerged.setText(DataTopologyProperties.getString("String_MergeAdjacentEndpoints"));
		this.checkboxDuplicatedLinesCleaned.setText(DataTopologyProperties.getString("String_CleanDuplicatedLines"));
		this.checkboxUndershootsExtended.setText(DataTopologyProperties.getString("String_CleanUndershoots"));
		this.checkboxRedundantVerticesCleaned.setText(DataTopologyProperties.getString("String_RedundantVertices"));
		this.labelDatasource.setText(CommonProperties.getString("String_Label_Datasource"));
		this.labelDataset.setText(CommonProperties.getString("String_Label_Dataset"));
		this.panelDatasource.setBorder(new TitledBorder(null, CommonProperties.getString("String_ColumnHeader_SourceData"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		this.panelTopoProcessingOptions.setBorder(new TitledBorder(null, DataTopologyProperties.getString("String_FixTopoErrorSettings"), TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
	}

	private void initComponents() {
		setSize(300, 360);
		setLocationRelativeTo(null);
		getRootPane().setDefaultButton(buttonSure);
		initComboBoxColumn();
		// @formatter:off
		initContentPane();
		initPanelDatasource();
		initPanelTopoProcessingOptions();
		// @formatter:on
	}

	private void initPanelTopoProcessingOptions() {
		this.checkboxAdjacentEndpointsMerged.setSelected(true);
		this.checkboxDuplicatedLinesCleaned.setSelected(true);
		this.checkboxLinesIntersected.setSelected(true);
		this.checkboxOvershootsCleaned.setSelected(true);
		this.checkboxPseudoNodesCleaned.setSelected(true);
		this.checkboxRedundantVerticesCleaned.setSelected(true);
		this.checkboxUndershootsExtended.setSelected(true);
		//@formatter:off
		this.panelTopoProcessingOptions.setLayout(new GridBagLayout());
		this.panelTopoProcessingOptions.add(this.checkboxLinesIntersected,         new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(10,10,5,10).setWeight(1, 0));
		this.panelTopoProcessingOptions.add(this.checkboxDuplicatedLinesCleaned,   new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(10,10,5,10).setWeight(1, 0));
		this.panelTopoProcessingOptions.add(this.checkboxOvershootsCleaned,        new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(10,10,5,10).setWeight(1, 0));
		this.panelTopoProcessingOptions.add(this.checkboxUndershootsExtended,      new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(10,10,5,10).setWeight(1, 0));
		this.panelTopoProcessingOptions.add(this.checkboxPseudoNodesCleaned,       new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(10,10,5,10).setWeight(1, 0));
		this.panelTopoProcessingOptions.add(this.checkboxRedundantVerticesCleaned, new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(10,10,5,10).setWeight(1, 0));
		this.panelTopoProcessingOptions.add(this.checkboxAdjacentEndpointsMerged,  new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(10,10,5,10).setWeight(1, 0));
		//@formatter:on
	}

	private void initPanelDatasource() {
		//@formatter:off
		this.panelDatasource.setLayout(new GridBagLayout());
		this.panelDatasource.add(this.labelDatasource,    new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(10,10,0,10));
		this.panelDatasource.add(this.comboBoxDatasource, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(10,10,0,10).setWeight(60, 0));
		this.panelDatasource.add(this.labelDataset,       new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5,10,10,10));
		this.panelDatasource.add(this.comboBoxDataset,    new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5,10,10,10).setWeight(60, 0));
		//@formatter:on
	}

	private void initContentPane() {

		//@formatter:off
		getContentPane().setLayout(new GridBagLayout());
		getContentPane().add(this.panelDatasource,            new GridBagConstraintsHelper(0, 0, 4, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(10,10,5,10).setWeight(1, 1));
		getContentPane().add(this.panelTopoProcessingOptions, new GridBagConstraintsHelper(0, 1, 4, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(10,10,5,10).setWeight(1, 1));
		getContentPane().add(this.buttonMore,                 new GridBagConstraintsHelper(0, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,10,10).setWeight(0, 0));
		getContentPane().add(this.buttonSure,                 new GridBagConstraintsHelper(2, 2, 1, 1).setAnchor(GridBagConstraints.EAST).setInsets(0,0,10,10).setWeight(1, 0));
		getContentPane().add(this.buttonQuite,                new GridBagConstraintsHelper(3, 2, 1, 1).setAnchor(GridBagConstraints.EAST).setInsets(0,0,10,10).setWeight(0, 0));
		getRootPane().setDefaultButton(this.buttonSure);
		//@formatter:on
	}

	private void registActionListener() {
		unregistActionListener();
		this.checkboxAdjacentEndpointsMerged.addActionListener(this.checkBoxListener);
		this.checkboxDuplicatedLinesCleaned.addActionListener(this.checkBoxListener);
		this.checkboxLinesIntersected.addActionListener(this.checkBoxListener);
		this.checkboxOvershootsCleaned.addActionListener(this.checkBoxListener);
		this.checkboxPseudoNodesCleaned.addActionListener(this.checkBoxListener);
		this.checkboxRedundantVerticesCleaned.addActionListener(this.checkBoxListener);
		this.checkboxUndershootsExtended.addActionListener(this.checkBoxListener);
		this.buttonSure.addActionListener(this.commonButtonListener);
		this.buttonMore.addActionListener(this.commonButtonListener);
		this.buttonQuite.addActionListener(this.commonButtonListener);
		this.comboBoxDatasource.addActionListener(this.commonButtonListener);

	}

	private void unregistActionListener() {
		this.checkboxAdjacentEndpointsMerged.removeActionListener(this.checkBoxListener);
		this.checkboxDuplicatedLinesCleaned.removeActionListener(this.checkBoxListener);
		this.checkboxLinesIntersected.removeActionListener(this.checkBoxListener);
		this.checkboxOvershootsCleaned.removeActionListener(this.checkBoxListener);
		this.checkboxPseudoNodesCleaned.removeActionListener(this.checkBoxListener);
		this.checkboxRedundantVerticesCleaned.removeActionListener(this.checkBoxListener);
		this.checkboxUndershootsExtended.removeActionListener(this.checkBoxListener);
		this.buttonSure.removeActionListener(this.commonButtonListener);
		this.buttonMore.removeActionListener(this.commonButtonListener);
		this.buttonQuite.removeActionListener(this.commonButtonListener);
		this.comboBoxDatasource.removeActionListener(this.commonButtonListener);
	}

	class CommonButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JComponent c = (JComponent) e.getSource();
			if (c == buttonSure) {
				topologyProcess();
				unregistActionListener();
				dispose();
			}
			if (c == buttonQuite) {
				dispose();
				unregistActionListener();
			}
			if (c == buttonMore) {
				openAdvanceDialog(topologyProcessingOptions);
			}
			if (c == comboBoxDatasource) {
				changeComboBoxItem();
			}
		}
	}

	/**
	 * 线拓扑处理
	 */
	private void topologyProcess() {
		try {
			String datasetName = this.comboBoxDataset.getSelectItem();
			String datasourceName = this.comboBoxDatasource.getSelectItem();
			Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceName);
			this.topologyProcessingOptions.setAdjacentEndpointsMerged(this.isAdjacentEndpointsMerged);
			this.topologyProcessingOptions.setDuplicatedLinesCleaned(this.isDuplicatedLinesCleaned);
			this.topologyProcessingOptions.setLinesIntersected(this.isLinesIntersected);
			this.topologyProcessingOptions.setOvershootsCleaned(this.isOvershootsCleaned);
			this.topologyProcessingOptions.setPseudoNodesCleaned(this.isPseudoNodesCleaned);
			this.topologyProcessingOptions.setRedundantVerticesCleaned(this.isRedundantVerticesCleaned);
			this.topologyProcessingOptions.setUndershootsExtended(this.isUndershootsExtended);
			// 进度条实现
			FormProgress progress = new FormProgress();
			progress.setTitle(DataTopologyProperties.getString("String_TopoLineTitle"));
			progress.doWork(new LineTopoCleanCallable(datasetName, this.topologyProcessingOptions, datasource));

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 改变下拉选项时修改comboBoxDataset的值
	 */
	private void changeComboBoxItem() {

		String datasourceName = this.comboBoxDatasource.getSelectItem();
		Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceName);
		this.hasDataset = insertItemToComboBox(datasource);
		if (!this.hasDataset) {
			this.buttonSure.setEnabled(false);
			this.buttonMore.setEnabled(false);
		} else {
			this.buttonSure.setEnabled(true);
			this.buttonMore.setEnabled(true);
		}
	}

	/**
	 * 打开高级参数设置页面
	 *
	 * @param topologyProcessingOptions
	 */
	private void openAdvanceDialog(TopologyProcessingOptions topologyProcessingOptions) {
		try {
			String datasetName = this.comboBoxDataset.getSelectItem();
			String datasourceName = this.comboBoxDatasource.getSelectItem();
			Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceName);
			Dataset targetDataset = DatasetUIUtilities.getDatasetFromDatasource(datasetName, datasource);
			JDialogTopoAdvance advance = new JDialogTopoAdvance(this, true, topologyProcessingOptions, (DatasetVector) targetDataset, datasource);
			advance.setVisible(true);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	class CommonCheckBoxListener implements ActionListener {
		// 有可以处理的数据集并且有选中的处理选项时可以进行线拓扑处理
		@Override
		public void actionPerformed(ActionEvent e) {
			isAdjacentEndpointsMerged = checkboxAdjacentEndpointsMerged.isSelected();
			isDuplicatedLinesCleaned = checkboxDuplicatedLinesCleaned.isSelected();
			isLinesIntersected = checkboxLinesIntersected.isSelected();
			isOvershootsCleaned = checkboxOvershootsCleaned.isSelected();
			isPseudoNodesCleaned = checkboxPseudoNodesCleaned.isSelected();
			isRedundantVerticesCleaned = checkboxRedundantVerticesCleaned.isSelected();
			isUndershootsExtended = checkboxUndershootsExtended.isSelected();
			try {
				if (hasDataset && !isAdjacentEndpointsMerged && !isDuplicatedLinesCleaned && !isLinesIntersected && !isOvershootsCleaned
						&& !isPseudoNodesCleaned && !isRedundantVerticesCleaned && !isUndershootsExtended) {
					buttonSure.setEnabled(false);
				} else {
					buttonSure.setEnabled(true);
				}
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
		}

	}

	/**
	 * 初始化ComboBox的选项
	 */
	private void initComboBoxColumn() {
		try {
			Dataset[] selectedDataset = Application.getActiveApplication().getActiveDatasets();
			Dataset lineDataset = null;
			for (int i = 0; i < selectedDataset.length; i++) {
				if (DatasetType.LINE.equals(selectedDataset[i].getType()) || DatasetType.NETWORK.equals(selectedDataset[i].getType())) {
					lineDataset = selectedDataset[i];
					break;
				}
			}
			Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
			Datasource currentDatasource = null;
			if (lineDataset != null) {
				currentDatasource = lineDataset.getDatasource();
			} else if (null != datasources && null != datasources.get(0)) {
				currentDatasource = datasources.get(0);
			}
			if (null != currentDatasource) {
				this.comboBoxDatasource = new DatasourceComboBox(datasources);
				for (int i = 0; i < this.comboBoxDatasource.getItemCount(); i++) {
					if (((DataCell) this.comboBoxDatasource.getItemAt(i)).getDataName().equals(currentDatasource.getAlias())) {
						this.comboBoxDatasource.setSelectedIndex(i);
						break;
					}
				}

				this.comboBoxDataset = new DatasetComboBox(new Dataset[0]);
				this.hasDataset = insertItemToComboBox(currentDatasource);
				if (null != lineDataset) {
					for (int i = 0; i < this.comboBoxDataset.getItemCount(); i++) {
						if (((DataCell) this.comboBoxDataset.getItemAt(i)).getDataName().equals(lineDataset.getName())) {
							this.comboBoxDataset.setSelectedIndex(i);
							break;
						}
					}
				}
				if (!this.hasDataset) {
					this.buttonSure.setEnabled(false);
					this.buttonMore.setEnabled(false);
				} else {
					this.buttonSure.setEnabled(true);
					this.buttonMore.setEnabled(true);
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 为comboBoxDataset插入选项
	 *
	 * @param datasource
	 */
	private boolean insertItemToComboBox(Datasource datasource) {
		this.comboBoxDataset.removeAllItems();
		int count = 0;
		if (null != datasource) {
			Datasets datasets = datasource.getDatasets();
			for (int i = 0; i < datasets.getCount(); i++) {
				if (datasets.get(i).getType() == DatasetType.LINE || datasets.get(i).getType() == DatasetType.NETWORK) {
					DataCell cell = new DataCell();
					cell.initDatasetType(datasets.get(i));
					this.comboBoxDataset.addItem(cell);
					count++;
				}
			}
		}
		return 0 < count;
	}

}
