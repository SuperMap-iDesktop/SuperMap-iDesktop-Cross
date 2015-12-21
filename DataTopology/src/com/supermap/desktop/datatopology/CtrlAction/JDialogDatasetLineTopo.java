package com.supermap.desktop.datatopology.CtrlAction;

import com.supermap.data.*;
import com.supermap.data.topology.TopologyProcessingOptions;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.datatopology.DataTopologyProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.progress.FormProgress;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JDialogDatasetLineTopo extends SmDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton buttonMore = new JButton("String_Button_Advance");
	private JButton buttonSure = new JButton("String_Button_OK");
	private JButton buttonQuite = new JButton("String_Button_Cancel");
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

	public JDialogDatasetLineTopo(JFrame owner, boolean model) {
		super(owner, model);
		initComponents();
		initResources();
	}

	private void initResources() {
		setTitle(DataTopologyProperties.getString("String_TopoLineTitle"));
		buttonMore.setText(CommonProperties.getString("String_Button_Advance"));
		buttonSure.setText(CommonProperties.getString("String_Button_OK"));
		buttonQuite.setText(CommonProperties.getString("String_Button_Cancel"));
		checkboxLinesIntersected.setText(DataTopologyProperties.getString("String_LinesIntersected"));
		checkboxOvershootsCleaned.setText(DataTopologyProperties.getString("String_CleanOvershoots"));
		checkboxPseudoNodesCleaned.setText(DataTopologyProperties.getString("String_CleanPseudoNodes"));
		checkboxAdjacentEndpointsMerged.setText(DataTopologyProperties.getString("String_MergeAdjacentEndpoints"));
		checkboxDuplicatedLinesCleaned.setText(DataTopologyProperties.getString("String_CleanDuplicatedLines"));
		checkboxUndershootsExtended.setText(DataTopologyProperties.getString("String_CleanUndershoots"));
		checkboxRedundantVerticesCleaned.setText(DataTopologyProperties.getString("String_RedundantVertices"));
		labelDatasource.setText(CommonProperties.getString("String_Label_Datasource"));
		labelDataset.setText(CommonProperties.getString("String_Label_Dataset"));
		panelDatasource.setBorder(
				new TitledBorder(null, CommonProperties.getString("String_ColumnHeader_SourceData"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelTopoProcessingOptions.setBorder(
				new TitledBorder(null, DataTopologyProperties.getString("String_FixTopoErrorSettings"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
	}

	private void initComponents() {
		setBounds(600, 300, 300, 315);
		initComboBoxColumn();
		// @formatter:off
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING,groupLayout.createSequentialGroup()
								.addComponent(buttonMore)
								.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE)
								.addComponent(buttonSure)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(buttonQuite))
						.addComponent(panelDatasource, GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
						.addComponent(panelTopoProcessingOptions, GroupLayout.PREFERRED_SIZE, 270, Short.MAX_VALUE))
						.addGap(26)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addContainerGap()
						.addComponent(panelDatasource)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panelTopoProcessingOptions)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(buttonMore)
								.addComponent(buttonSure)
								.addComponent(buttonQuite))
						.addContainerGap(13, Short.MAX_VALUE)));

		GroupLayout gl_panelTopoProcessingOptions = new GroupLayout(panelTopoProcessingOptions);
		gl_panelTopoProcessingOptions.setAutoCreateContainerGaps(true);
		gl_panelTopoProcessingOptions.setAutoCreateGaps(true);
		gl_panelTopoProcessingOptions.setHorizontalGroup(gl_panelTopoProcessingOptions.createSequentialGroup()
				.addGroup(gl_panelTopoProcessingOptions.createParallelGroup(Alignment.LEADING)
						.addComponent(checkboxLinesIntersected, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE,
								Short.MAX_VALUE)
						.addComponent(checkboxOvershootsCleaned, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE,
								Short.MAX_VALUE)
						.addComponent(checkboxPseudoNodesCleaned, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE,
								Short.MAX_VALUE)
						.addComponent(checkboxAdjacentEndpointsMerged, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panelTopoProcessingOptions.createParallelGroup(Alignment.LEADING)
						.addComponent(checkboxDuplicatedLinesCleaned, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
						.addComponent(checkboxUndershootsExtended, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE,
								Short.MAX_VALUE)
						.addComponent(checkboxRedundantVerticesCleaned, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)));
		gl_panelTopoProcessingOptions
				.setVerticalGroup(gl_panelTopoProcessingOptions.createParallelGroup(Alignment.BASELINE)
						.addGroup(gl_panelTopoProcessingOptions.createSequentialGroup()
								.addComponent(checkboxLinesIntersected).addComponent(checkboxOvershootsCleaned)
								.addComponent(checkboxPseudoNodesCleaned).addComponent(checkboxAdjacentEndpointsMerged))
				.addGroup(gl_panelTopoProcessingOptions.createSequentialGroup()
						.addComponent(checkboxDuplicatedLinesCleaned)
						.addComponent(checkboxUndershootsExtended)
						.addComponent(checkboxRedundantVerticesCleaned)));
		panelTopoProcessingOptions.setLayout(gl_panelTopoProcessingOptions);

		GroupLayout gl_panelDatasource = new GroupLayout(panelDatasource);
		gl_panelDatasource.setAutoCreateContainerGaps(true);
		gl_panelDatasource.setAutoCreateGaps(true);

		gl_panelDatasource.setHorizontalGroup(gl_panelDatasource.createParallelGroup()
						.addGroup(gl_panelDatasource.createSequentialGroup()
								.addComponent(labelDatasource, 0, 72, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(comboBoxDatasource, 0, 144, Short.MAX_VALUE))
				.addGroup(gl_panelDatasource.createSequentialGroup().addComponent(labelDataset, 0, 72, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(comboBoxDataset, 0, 144, Short.MAX_VALUE)));
		gl_panelDatasource.setVerticalGroup(gl_panelDatasource.createSequentialGroup()
				.addGroup(gl_panelDatasource.createParallelGroup()
						.addComponent(labelDatasource)
						.addComponent(comboBoxDatasource,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panelDatasource.createParallelGroup()
						.addComponent(labelDataset)
						.addComponent(comboBoxDataset,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)));
		// @formatter:on
		panelDatasource.setLayout(gl_panelDatasource);
		getContentPane().setLayout(groupLayout);
		checkboxAdjacentEndpointsMerged.setSelected(true);
		checkboxDuplicatedLinesCleaned.setSelected(true);
		checkboxLinesIntersected.setSelected(true);
		checkboxOvershootsCleaned.setSelected(true);
		checkboxPseudoNodesCleaned.setSelected(true);
		checkboxRedundantVerticesCleaned.setSelected(true);
		checkboxUndershootsExtended.setSelected(true);

		checkboxAdjacentEndpointsMerged.addActionListener(new CommonCheckBoxListener());
		checkboxDuplicatedLinesCleaned.addActionListener(new CommonCheckBoxListener());
		checkboxLinesIntersected.addActionListener(new CommonCheckBoxListener());
		checkboxOvershootsCleaned.addActionListener(new CommonCheckBoxListener());
		checkboxPseudoNodesCleaned.addActionListener(new CommonCheckBoxListener());
		checkboxRedundantVerticesCleaned.addActionListener(new CommonCheckBoxListener());
		checkboxUndershootsExtended.addActionListener(new CommonCheckBoxListener());
		buttonSure.addActionListener(new CommonButtonListener());
		buttonMore.addActionListener(new CommonButtonListener());
		buttonQuite.addActionListener(new CommonButtonListener());
		comboBoxDatasource.addActionListener(new CommonButtonListener());

	}

	class CommonButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JComponent c = (JComponent) e.getSource();
			if (c == buttonSure) {
				topologyProcess();
				dispose();
			}
			if (c == buttonQuite) {
				dispose();
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
	 * 改变下拉选项时修改comboBoxDataset的值
	 */
	private void changeComboBoxItem() {
		String datasourceName = comboBoxDatasource.getSelectItem();
		Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceName);
		hasDataset = insertItemToComboBox(datasource);
		if (!hasDataset) {
			buttonSure.setEnabled(false);
			buttonMore.setEnabled(false);
		} else {
			buttonSure.setEnabled(true);
			buttonMore.setEnabled(true);
		}
	}

	/**
	 * 打开高级参数设置页面
	 * 
	 * @param topologyProcessingOptions
	 */
	private void openAdvanceDialog(TopologyProcessingOptions topologyProcessingOptions) {
		try {
			String datasetName = comboBoxDataset.getSelectItem();
			String datasourceName = comboBoxDatasource.getSelectItem();
			Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceName);
			Dataset targetDataset = CommonToolkit.DatasetWrap.getDatasetFromDatasource(datasetName, datasource);
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
				comboBoxDatasource = new DatasourceComboBox(datasources);
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
				if (!hasDataset) {
					buttonSure.setEnabled(false);
					buttonMore.setEnabled(false);
				} else {
					buttonSure.setEnabled(true);
					buttonMore.setEnabled(true);
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

}
