package com.supermap.desktop.datatopology.CtrlAction;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.data.topology.TopologyProcessingOptions;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.datatopology.DataTopologyProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.ComponentBorderPanel.CompTitledPane;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.progress.FormProgress;
import com.supermap.desktop.utilties.StringUtilties;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class JDialogTopoBuildRegions extends SmDialog {
	private static final long serialVersionUID = 1L;
	private SmButton buttonMore = new SmButton("String_Button_Advance");
	private SmButton buttonOk = new SmButton("String_Button_OK");
	private SmButton buttonCancel = new SmButton("String_Button_Cancel");
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
	private CompTitledPane panelTopoProcessingOptions;
	private JPanel panelResultData = new JPanel();
	private JLabel labelResultDatasource = new JLabel("New label");
	private JLabel labelResultDataset = new JLabel("New label");
	private DatasourceComboBox comboBoxResultDatasource;
	private SmTextFieldLegit textFieldResultDataset = new SmTextFieldLegit("BuildRegion");
	private boolean isAdjacentEndpointsMerged = true;
	private boolean isDuplicatedLinesCleaned = true;
	private boolean isLinesIntersected = true;
	private boolean isOvershootsCleaned = true;
	private boolean isPseudoNodesCleaned = true;
	private boolean isRedundantVerticesCleaned = true;
	private boolean isUndershootsExtended = true;
	private transient TopologyProcessingOptions topologyProcessingOptions = new TopologyProcessingOptions();
	private JCheckBox checkBoxtopologyPropress = new JCheckBox();
	private boolean hasDataset;
	private CommonButtonListener buttonListener = new CommonButtonListener();
	private CheckBoxListener checkBoxListener = new CheckBoxListener();

	private KeyListener keyListener = new KeyAdapter() {

		@Override
		public void keyReleased(KeyEvent e) {
			String datasourceName = comboBoxResultDatasource.getSelectItem();
			if (!StringUtilties.isNullOrEmpty(datasourceName)) {
				Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceName);
				if (!datasource.getDatasets().isAvailableDatasetName(textFieldResultDataset.getText())) {
					buttonOk.setEnabled(false);
				} else {
					buttonOk.setEnabled(true);
				}
			}
		}
	};

	public JDialogTopoBuildRegions(JFrame owner, boolean model) {
		super(owner, model);
		setLocationRelativeTo(owner);
		initComponents();
		initResources();
	}

	private void initResources() {
		setTitle(DataTopologyProperties.getString("String_TopoRegionTitle"));
		this.buttonMore.setText(CommonProperties.getString("String_Button_Advance"));
		this.buttonOk.setText(CommonProperties.getString("String_Button_OK"));
		this.buttonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
		this.checkboxLinesIntersected.setSelected(true);
		this.checkboxLinesIntersected.setText(DataTopologyProperties.getString("String_LinesIntersected"));
		this.checkboxOvershootsCleaned.setSelected(true);
		this.checkboxOvershootsCleaned.setText(DataTopologyProperties.getString("String_CleanOvershoots"));
		this.checkboxPseudoNodesCleaned.setSelected(true);
		this.checkboxPseudoNodesCleaned.setText(DataTopologyProperties.getString("String_CleanPseudoNodes"));
		this.checkboxAdjacentEndpointsMerged.setSelected(true);
		this.checkboxAdjacentEndpointsMerged.setText(DataTopologyProperties.getString("String_MergeAdjacentEndpoints"));
		this.checkboxDuplicatedLinesCleaned.setSelected(true);
		this.checkboxDuplicatedLinesCleaned.setText(DataTopologyProperties.getString("String_CleanDuplicatedLines"));
		this.checkboxUndershootsExtended.setSelected(true);
		this.checkboxUndershootsExtended.setText(DataTopologyProperties.getString("String_Label_UndershootsTolerance"));
		this.checkboxRedundantVerticesCleaned.setSelected(true);
		this.checkboxRedundantVerticesCleaned.setText(DataTopologyProperties.getString("String_RedundantVertices"));
		this.labelDatasource.setText(CommonProperties.getString("String_Label_Datasource"));
		this.labelDataset.setText(CommonProperties.getString("String_Label_Dataset"));
		this.labelResultDatasource.setText(CommonProperties.getString("String_Label_Datasource"));
		this.labelResultDataset.setText(CommonProperties.getString("String_Label_Dataset"));
		this.checkBoxtopologyPropress.setText(DataTopologyProperties.getString("String_Topo_Build"));
		this.checkBoxtopologyPropress.setToolTipText(DataTopologyProperties.getString("String_TopoLineTipsInfo"));
		this.panelDatasource.setBorder(new TitledBorder(null, CommonProperties.getString("String_ColumnHeader_SourceData"), TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		this.panelResultData.setBorder(new TitledBorder(null, CommonProperties.getString("String_GroupBox_ResultData"), TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
	}

	/**
	 * 修改textField的内容
	 *
	 * @param datasource
	 */

	private void initTextFieldName(Datasource datasource) {
		String regionDatasetName = this.textFieldResultDataset.getText();
		try {
			String availableName = datasource.getDatasets().getAvailableDatasetName(regionDatasetName);
			this.textFieldResultDataset.setText(availableName);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 初始化ComboBox下拉选项
	 */

	private void initComboBoxItem() {
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
			java.util.List<Datasource> datasourceList = new ArrayList<Datasource>();
			for (int i = 0; i < datasources.getCount(); i++) {
				if (!datasources.get(i).isReadOnly()) {
					datasourceList.add(datasources.get(i));
				}
			}

			Datasource currentDatasource = null;
			if (lineDataset != null && !lineDataset.getDatasource().isReadOnly()) {
				currentDatasource = lineDataset.getDatasource();
			} else if (null != datasourceList && null != datasourceList.get(0)) {
				currentDatasource = datasourceList.get(0);
			}
			Datasource[] datasourceArray = new Datasource[datasourceList.size()];
			if (null != currentDatasource) {
				this.comboBoxResultDatasource = new DatasourceComboBox(datasourceList.toArray(datasourceArray));
				for (int i = 0; i < this.comboBoxResultDatasource.getItemCount(); i++) {
					if (((DataCell) this.comboBoxResultDatasource.getItemAt(i)).getDataName().equals(currentDatasource.getAlias())) {
						this.comboBoxResultDatasource.setSelectedIndex(i);
						break;
					}
				}

				this.comboBoxDatasource = new DatasourceComboBox(datasourceList.toArray(datasourceArray));
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
				initTextFieldName(currentDatasource);
				setCheckBoxSelected(this.hasDataset);
			}

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	// 初始化contentPanel
	private void initContentPanel() {
		// @formatter:off
		this.getContentPane().setLayout(new GridBagLayout());
		this.getContentPane().add(this.panelDatasource,           new GridBagConstraintsHelper(0, 0, 4, 1).setAnchor(GridBagConstraints.CENTER).setInsets(10, 10, 5, 10).setWeight(3, 1).setIpad(60, 0).setFill(GridBagConstraints.BOTH));
		this.getContentPane().add(this.panelTopoProcessingOptions,new GridBagConstraintsHelper(0, 1, 4, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0, 10, 5, 10).setWeight(3, 1).setFill(GridBagConstraints.BOTH));
		this.getContentPane().add(this.panelResultData,           new GridBagConstraintsHelper(0, 2, 4, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0, 10, 5, 10).setWeight(3, 1).setIpad(60, 0).setFill(GridBagConstraints.BOTH));
		this.getContentPane().add(this.buttonOk,                  new GridBagConstraintsHelper(2, 3, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(3, 1).setInsets(0, 20, 10, 10));
		this.getContentPane().add(this.buttonCancel,              new GridBagConstraintsHelper(3, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(3, 1).setInsets(0, 5, 10, 0));
	}

	private void registAction() {
		this.checkBoxtopologyPropress.addActionListener(checkBoxListener);
		this.buttonMore.addActionListener(buttonListener);
		this.buttonCancel.addActionListener(buttonListener);
		this.buttonOk.addActionListener(buttonListener);
		this.comboBoxDatasource.addActionListener(buttonListener);
		this.comboBoxResultDatasource.addActionListener(buttonListener);
		this.textFieldResultDataset.addKeyListener(keyListener);
	}

	// 注销注册的事件
	private void unregistAction() {
		this.checkBoxtopologyPropress.removeActionListener(checkBoxListener);
		this.buttonMore.removeActionListener(buttonListener);
		this.buttonOk.removeActionListener(buttonListener);
		this.comboBoxDatasource.removeActionListener(buttonListener);
		this.comboBoxResultDatasource.removeActionListener(buttonListener);
		this.textFieldResultDataset.removeKeyListener(keyListener);
	}

	private void initPanelResultData() {
		// labelResultDatasource comboBoxResultDatasource
		// labelResultDataset textFieldResultDataset
		// @formatter:off
		this.panelResultData.setLayout(new GridBagLayout());
		this.panelResultData.add(this.labelResultDatasource,    new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
		this.panelResultData.add(this.comboBoxResultDatasource, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 0, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(60, 0));
		this.panelResultData.add(this.labelResultDataset,       new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 10, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
		this.panelResultData.add(this.textFieldResultDataset,   new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 10, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(60, 0));
		// @formatter:on
	}

	private void initPanelDatasource() {
		// labelDatasource comboBoxDatasource
		// labelDataset comboBoxDataset
		// @formatter:off
		this.panelDatasource.setLayout(new GridBagLayout());
		this.panelDatasource.add(this.labelDatasource,    new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 0, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
		this.panelDatasource.add(this.comboBoxDatasource, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 0, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(60, 0));
		this.panelDatasource.add(this.labelDataset,       new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 10, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
		this.panelDatasource.add(this.comboBoxDataset,    new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 10, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(60, 0));
		// @formatter:on
	}

	private void initPanelTopoProcessingOptions() {
		// checkboxLinesIntersected checkboxDuplicatedLinesCleaned
		// checkboxOvershootsCleaned checkboxUndershootsExtended
		// checkboxPseudoNodesCleaned checkboxRedundantVerticesCleaned
		// checkboxAdjacentEndpointsMerged buttonMore
		JPanel panelTemp = new JPanel();
		// @formatter:off
		panelTemp.setLayout(new GridBagLayout());
		panelTemp.add(this.checkboxLinesIntersected,         new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
		panelTemp.add(this.checkboxDuplicatedLinesCleaned,   new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
		panelTemp.add(this.checkboxOvershootsCleaned,        new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
		panelTemp.add(this.checkboxUndershootsExtended,      new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
		panelTemp.add(this.checkboxPseudoNodesCleaned,       new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
		panelTemp.add(this.checkboxRedundantVerticesCleaned, new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
		panelTemp.add(this.checkboxAdjacentEndpointsMerged,  new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
		panelTemp.add(this.buttonMore,                       new GridBagConstraintsHelper(1, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5).setWeight(0, 1).setIpad(30, 0).setWeight(1, 0));
		this.panelTopoProcessingOptions = new CompTitledPane(this.checkBoxtopologyPropress, panelTemp);
		
		// @formatter:on
	}

	private void initComponents() {
		initComboBoxItem();
		this.setSize(new Dimension(315, 440));
		this.setLocationRelativeTo(null);
		initPanelResultData();
		initPanelTopoProcessingOptions();
		this.checkboxLinesIntersected.setEnabled(false);
		this.checkboxOvershootsCleaned.setEnabled(false);
		this.checkboxPseudoNodesCleaned.setEnabled(false);
		this.checkboxAdjacentEndpointsMerged.setEnabled(false);
		this.checkboxDuplicatedLinesCleaned.setEnabled(false);
		this.checkboxUndershootsExtended.setEnabled(false);
		this.checkboxRedundantVerticesCleaned.setEnabled(false);
		this.buttonMore.setEnabled(false);

		this.textFieldResultDataset.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {

				Datasource selectedDatasource = comboBoxDatasource.getSelectedDatasource();
				return selectedDatasource == null || selectedDatasource.getDatasets().isAvailableDatasetName(textFieldValue);
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return comboBoxDatasource.getSelectedDatasource().getDatasets().getAvailableDatasetName(currentValue);
			}
		});
		initPanelDatasource();
		initContentPanel();
		registAction();
	}

	class CheckBoxListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean isPologyProgress = checkBoxtopologyPropress.isSelected();
			checkboxLinesIntersected.setEnabled(isPologyProgress);
			checkboxOvershootsCleaned.setEnabled(isPologyProgress);
			checkboxPseudoNodesCleaned.setEnabled(isPologyProgress);
			checkboxAdjacentEndpointsMerged.setEnabled(isPologyProgress);
			checkboxDuplicatedLinesCleaned.setEnabled(isPologyProgress);
			checkboxUndershootsExtended.setEnabled(isPologyProgress);
			checkboxRedundantVerticesCleaned.setEnabled(isPologyProgress);
			buttonMore.setEnabled(isPologyProgress);
		}

	}

	class CommonButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JComponent c = (JComponent) e.getSource();
			if (c == buttonOk) {
				boolean isTopoprogress = checkBoxtopologyPropress.isSelected();
				if (isTopoprogress) {
					topologyProcess();
				}
				topologyBuildRegion();
				unregistAction();
				buttonCancel.removeActionListener(buttonListener);
				dispose();
			}
			if (c == buttonCancel) {
				unregistAction();
				buttonCancel.removeActionListener(buttonListener);
				dispose();
			}
			if (c == buttonMore) {
				openAdvanceDialog(topologyProcessingOptions);
			}
			if (c == comboBoxDatasource) {
				changeComboBoxItem();
			}
			if (c == comboBoxResultDatasource) {
				String datasourceName = comboBoxResultDatasource.getSelectItem();
				Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceName);
				initTextFieldName(datasource);
			}
		}
	}

	/**
	 * 改变下拉选项时修改comboBoxDataset的值
	 */

	private void changeComboBoxItem() {
		String datasourceName = this.comboBoxDatasource.getSelectItem();
		Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceName);
		this.hasDataset = insertItemToComboBox(datasource);
		setCheckBoxSelected(this.hasDataset);
	}

	private void setCheckBoxSelected(boolean isSelected) {
		this.buttonOk.setEnabled(isSelected);
		this.buttonMore.setEnabled(isSelected);
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
			progress.setTitle(DataTopologyProperties.getString("String_TopoRegionTitle"));
			progress.doWork(new LineTopoCleanCallable(datasetName, this.topologyProcessingOptions, datasource));
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 拓扑构面
	 */
	private void topologyBuildRegion() {
		try {
			String datasetName = this.comboBoxDataset.getSelectItem();
			String resultDatasourceName = this.comboBoxResultDatasource.getSelectItem();
			String targetDatasetName = this.textFieldResultDataset.getText();
			String targetDatasourceName = this.comboBoxDatasource.getSelectItem();
			Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(targetDatasourceName);
			Datasource resultDatasource = Application.getActiveApplication().getWorkspace().getDatasources().get(resultDatasourceName);
			Dataset dataset = CommonToolkit.DatasetWrap.getDatasetFromDatasource(datasetName, datasource);
			// 进度条实现
			FormProgress progress = new FormProgress();
			progress.setTitle(DataTopologyProperties.getString("String_TopoRegionTitle"));
			progress.doWork(new TopoBuildRegionsCallable(resultDatasource, dataset, targetDatasetName, topologyProcessingOptions));
		} catch (Exception e) {

		} finally {
			dispose();
		}
	}

	/**
	 * 打开高级参数设置页面
	 *
	 * @param topologyProcessingOptions
	 */

	private void openAdvanceDialog(TopologyProcessingOptions topologyProcessingOptions) {
		try {
			if (0 < this.comboBoxDataset.getItemCount()) {
				String datasetName = this.comboBoxDataset.getSelectItem();
				String datasourceName = this.comboBoxDatasource.getSelectItem();
				Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceName);
				Dataset targetDataset = CommonToolkit.DatasetWrap.getDatasetFromDatasource(datasetName, datasource);
				JDialogTopoAdvance advance = new JDialogTopoAdvance(this, true, topologyProcessingOptions, (DatasetVector) targetDataset, datasource);
				advance.setVisible(true);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}
}
