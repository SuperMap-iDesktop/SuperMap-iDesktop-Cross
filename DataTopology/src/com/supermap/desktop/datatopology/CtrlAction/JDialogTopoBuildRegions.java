package com.supermap.desktop.datatopology.CtrlAction;

import com.supermap.data.*;
import com.supermap.data.topology.TopologyProcessingOptions;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.datatopology.DataTopologyProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.progress.FormProgress;
import com.supermap.desktop.utilties.StringUtilties;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class JDialogTopoBuildRegions extends SmDialog {
	private static final long serialVersionUID = 1L;
	private JButton buttonMore = new JButton("String_Button_Advance");
	private JButton buttonOk = new JButton("String_Button_OK");
	private JButton buttonCancel = new JButton("String_Button_Cancel");
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
	private JPanel panelResultData = new JPanel();
	private JLabel labelResultDatasource = new JLabel("New label");
	private JLabel labelResultDataset = new JLabel("New label");
	private DatasourceComboBox comboBoxResultDatasource;
	private JTextField textFieldResultDataset = new JTextField("BuildRegion");
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
		buttonMore.setText(CommonProperties.getString("String_Button_Advance"));
		buttonOk.setText(CommonProperties.getString("String_Button_OK"));
		buttonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
		checkboxLinesIntersected.setSelected(true);
		checkboxLinesIntersected.setText(DataTopologyProperties.getString("String_LinesIntersected"));
		checkboxOvershootsCleaned.setSelected(true);
		checkboxOvershootsCleaned.setText(DataTopologyProperties.getString("String_CleanOvershoots"));
		checkboxPseudoNodesCleaned.setSelected(true);
		checkboxPseudoNodesCleaned.setText(DataTopologyProperties.getString("String_CleanPseudoNodes"));
		checkboxAdjacentEndpointsMerged.setSelected(true);
		checkboxAdjacentEndpointsMerged.setText(DataTopologyProperties.getString("String_MergeAdjacentEndpoints"));
		checkboxDuplicatedLinesCleaned.setSelected(true);
		checkboxDuplicatedLinesCleaned.setText(DataTopologyProperties.getString("String_CleanDuplicatedLines"));
		checkboxUndershootsExtended.setSelected(true);
		checkboxUndershootsExtended.setText(DataTopologyProperties.getString("String_Label_UndershootsTolerance"));
		checkboxRedundantVerticesCleaned.setSelected(true);
		checkboxRedundantVerticesCleaned.setText(DataTopologyProperties.getString("String_RedundantVertices"));
		labelDatasource.setText(CommonProperties.getString("String_Label_Datasource"));
		labelDataset.setText(CommonProperties.getString("String_Label_Dataset"));
		labelResultDatasource.setText(CommonProperties.getString("String_Label_Datasource"));
		labelResultDataset.setText(CommonProperties.getString("String_Label_Dataset"));
		checkBoxtopologyPropress.setText(DataTopologyProperties.getString("String_Topo_Build"));
		checkBoxtopologyPropress.setToolTipText(DataTopologyProperties.getString("String_TopoLineTipsInfo"));
		panelDatasource.setBorder(new TitledBorder(null, CommonProperties.getString("String_ColumnHeader_SourceData"), TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		panelTopoProcessingOptions.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelResultData.setBorder(new TitledBorder(null, CommonProperties.getString("String_GroupBox_ResultData"), TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
	}

	/**
	 * 修改textField的内容
	 *
	 * @param datasource
	 */

	private void initTextFieldName(Datasource datasource) {
		String regionDatasetName = textFieldResultDataset.getText();
		try {
			String availableName = datasource.getDatasets().getAvailableDatasetName(regionDatasetName);
			textFieldResultDataset.setText(availableName);
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
				comboBoxResultDatasource = new DatasourceComboBox(datasourceList.toArray(datasourceArray));
				for (int i = 0; i < comboBoxResultDatasource.getItemCount(); i++) {
					if (((DataCell) comboBoxResultDatasource.getItemAt(i)).getDatasetName().equals(currentDatasource.getAlias())) {
						comboBoxResultDatasource.setSelectedIndex(i);
						break;
					}
				}

				comboBoxDatasource = new DatasourceComboBox((Datasource[]) datasourceList.toArray(datasourceArray));
				for (int i = 0; i < comboBoxDatasource.getItemCount(); i++) {
					if (((DataCell) comboBoxDatasource.getItemAt(i)).getDatasetName().equals(currentDatasource.getAlias())) {
						comboBoxDatasource.setSelectedIndex(i);
						break;
					}
				}

				comboBoxDataset = new DatasetComboBox(new Dataset[0]);
				hasDataset = insertItemToComboBox(currentDatasource);
				if (null != lineDataset) {
					for (int i = 0; i < comboBoxDataset.getItemCount(); i++) {
						if (((DataCell) comboBoxDataset.getItemAt(i)).getDatasetName().equals(lineDataset.getName())) {
							comboBoxDataset.setSelectedIndex(i);
							break;
						}
					}
				}
				initTextFieldName(currentDatasource);
				setCheckBoxSelected(hasDataset);
			}

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	// 初始化contentPanel
	private void initContentPanel() {
		// @formatter:off
		this.getContentPane().setLayout(new GridBagLayout());
		this.getContentPane().add(panelDatasource,           new GridBagConstraintsHelper(0, 0, 4, 1).setAnchor(GridBagConstraints.CENTER).setInsets(10, 10, 5, 10).setWeight(3, 1).setIpad(60, 0).setFill(GridBagConstraints.BOTH));
		this.getContentPane().add(checkBoxtopologyPropress,  new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,20,0,10).setWeight(3, 1));
		this.getContentPane().add(panelTopoProcessingOptions,new GridBagConstraintsHelper(0, 2, 4, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 5, 10).setWeight(3, 1).setFill(GridBagConstraints.BOTH));
		this.getContentPane().add(panelResultData,           new GridBagConstraintsHelper(0, 3, 4, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 5, 10).setWeight(3, 1).setIpad(60, 0).setFill(GridBagConstraints.BOTH));
		this.getContentPane().add(buttonOk,                  new GridBagConstraintsHelper(2, 4, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(3, 1).setInsets(0, 20, 10, 10));
		this.getContentPane().add(buttonCancel,              new GridBagConstraintsHelper(3, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(3, 1).setInsets(0, 5, 10, 0));
	}

	private void registAction() {
		checkBoxtopologyPropress.addActionListener(checkBoxListener);
		buttonMore.addActionListener(buttonListener);
		buttonCancel.addActionListener(buttonListener);
		buttonOk.addActionListener(buttonListener);
		comboBoxDatasource.addActionListener(buttonListener);
		comboBoxResultDatasource.addActionListener(buttonListener);
		textFieldResultDataset.addKeyListener(keyListener);
	}

	// 注销注册的事件
	private void unregistAction() {
		checkBoxtopologyPropress.removeActionListener(checkBoxListener);
		buttonMore.removeActionListener(buttonListener);
		buttonOk.removeActionListener(buttonListener);
		comboBoxDatasource.removeActionListener(buttonListener);
		comboBoxResultDatasource.removeActionListener(buttonListener);
		textFieldResultDataset.removeKeyListener(keyListener);
	}

	private void initPanelResultData() {
		// labelResultDatasource comboBoxResultDatasource
		// labelResultDataset textFieldResultDataset
		// @formatter:off
		panelResultData.setLayout(new GridBagLayout());
		panelResultData.add(labelResultDatasource,    new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 10).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		panelResultData.add(comboBoxResultDatasource, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 5, 10).setFill(GridBagConstraints.BOTH).setWeight(60, 1));
		panelResultData.add(labelResultDataset,       new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 10).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		panelResultData.add(textFieldResultDataset,   new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 5, 10).setFill(GridBagConstraints.BOTH).setWeight(60, 1));
		// @formatter:on
	}

	private void initPanelDatasource() {
		// labelDatasource comboBoxDatasource
		// labelDataset comboBoxDataset
		// @formatter:off
		panelDatasource.setLayout(new GridBagLayout());
		panelDatasource.add(labelDatasource,    new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 10).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		panelDatasource.add(comboBoxDatasource, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 5, 10).setFill(GridBagConstraints.BOTH).setWeight(60, 1));
		panelDatasource.add(labelDataset,       new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 5, 10).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		panelDatasource.add(comboBoxDataset,    new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 5, 10).setFill(GridBagConstraints.BOTH).setWeight(60, 1));
		// @formatter:on
	}

	private void initPanelTopoProcessingOptions() {
		// checkboxLinesIntersected checkboxDuplicatedLinesCleaned
		// checkboxOvershootsCleaned checkboxUndershootsExtended
		// checkboxPseudoNodesCleaned checkboxRedundantVerticesCleaned
		// checkboxAdjacentEndpointsMerged buttonMore
		// @formatter:off
		panelTopoProcessingOptions.setLayout(new GridBagLayout());
		panelTopoProcessingOptions.add(checkboxLinesIntersected,         new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
		panelTopoProcessingOptions.add(checkboxDuplicatedLinesCleaned,   new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
		panelTopoProcessingOptions.add(checkboxOvershootsCleaned,        new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
		panelTopoProcessingOptions.add(checkboxUndershootsExtended,      new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
		panelTopoProcessingOptions.add(checkboxPseudoNodesCleaned,       new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
		panelTopoProcessingOptions.add(checkboxRedundantVerticesCleaned, new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
		panelTopoProcessingOptions.add(checkboxAdjacentEndpointsMerged,  new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
		panelTopoProcessingOptions.add(buttonMore,                       new GridBagConstraintsHelper(1, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
		// @formatter:on
	}

	private void initComponents() {
		initComboBoxItem();
		this.setSize(new Dimension(315, 440));
		this.setLocationRelativeTo(null);
		initContentPanel();
		initPanelResultData();
		initPanelTopoProcessingOptions();
		checkboxLinesIntersected.setEnabled(false);
		checkboxOvershootsCleaned.setEnabled(false);
		checkboxPseudoNodesCleaned.setEnabled(false);
		checkboxAdjacentEndpointsMerged.setEnabled(false);
		checkboxDuplicatedLinesCleaned.setEnabled(false);
		checkboxUndershootsExtended.setEnabled(false);
		checkboxRedundantVerticesCleaned.setEnabled(false);
		buttonMore.setEnabled(false);
		initPanelDatasource();
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
		String datasourceName = comboBoxDatasource.getSelectItem();
		Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceName);
		hasDataset = insertItemToComboBox(datasource);
		setCheckBoxSelected(hasDataset);
	}

	private void setCheckBoxSelected(boolean isSelected) {
		buttonOk.setEnabled(isSelected);
		buttonMore.setEnabled(isSelected);
	}

	/**
	 * 为comboBoxDataset插入选项
	 *
	 * @param datasource
	 */

	private boolean insertItemToComboBox(Datasource datasource) {
		comboBoxDataset.removeAllItems();
		int count = 0;
		if (null != datasource) {
			Datasets datasets = datasource.getDatasets();
			for (int i = 0; i < datasets.getCount(); i++) {
				if (datasets.get(i).getType() == DatasetType.LINE || datasets.get(i).getType() == DatasetType.NETWORK) {
					String path = CommonToolkit.DatasetImageWrap.getImageIconPath(datasets.get(i).getType());
					DataCell cell = new DataCell(path, datasets.get(i).getName());
					comboBoxDataset.addItem(cell);
					count++;
				}
			}
		}
		if (0 < count) {
			return true;
		}
		return false;
	}

	/**
	 * 线拓扑处理
	 */

	private void topologyProcess() {
		try {
			String datasetName = comboBoxDataset.getSelectItem();
			String datasourceName = comboBoxDatasource.getSelectItem();
			Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceName);
			topologyProcessingOptions.setAdjacentEndpointsMerged(isAdjacentEndpointsMerged);
			topologyProcessingOptions.setDuplicatedLinesCleaned(isDuplicatedLinesCleaned);
			topologyProcessingOptions.setLinesIntersected(isLinesIntersected);
			topologyProcessingOptions.setOvershootsCleaned(isOvershootsCleaned);
			topologyProcessingOptions.setPseudoNodesCleaned(isPseudoNodesCleaned);
			topologyProcessingOptions.setRedundantVerticesCleaned(isRedundantVerticesCleaned);
			topologyProcessingOptions.setUndershootsExtended(isUndershootsExtended);
			// 进度条实现
			FormProgress progress = new FormProgress();
			progress.doWork(new LineTopoCleanCallable(datasetName, topologyProcessingOptions, datasource));
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 拓扑构面
	 */
	private void topologyBuildRegion() {
		try {
			String datasetName = comboBoxDataset.getSelectItem();
			String resultDatasourceName = comboBoxResultDatasource.getSelectItem();
			String targetDatasetName = textFieldResultDataset.getText();
			String targetDatasourceName = comboBoxDatasource.getSelectItem();
			Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(targetDatasourceName);
			Datasource resultDatasource = Application.getActiveApplication().getWorkspace().getDatasources().get(resultDatasourceName);
			Dataset dataset = CommonToolkit.DatasetWrap.getDatasetFromDatasource(datasetName, datasource);
			// 进度条实现
			FormProgress progress = new FormProgress();
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
			if (0 < comboBoxDataset.getItemCount()) {
				String datasetName = comboBoxDataset.getSelectItem();
				String datasourceName = comboBoxDatasource.getSelectItem();
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
