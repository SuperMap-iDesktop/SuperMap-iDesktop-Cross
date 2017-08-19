package com.supermap.desktop.CtrlAction.Dataset.createNewDataset;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * Created by yuanR on 2017/8/18 0018.
 * 数据集模版面板
 */
public class PanelModel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JRadioButton useModelRadioButton;
	private JRadioButton noUseModelRadioButton;
	private JLabel datasourceLabel;
	private JLabel datasetLabel;
	private DatasourceComboBox datasourceComboBox;

	private DatasetComboBox datasetComboBox;

	ArrayList<NewDatasetBean> datasetBeans;
	NewDatasetBean datasetBeanFrist;

	public DatasetComboBox getDatasetComboBox() {
		return datasetComboBox;
	}

	private ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == useModelRadioButton) {
				useModelRadioButton.setSelected(true);
				noUseModelRadioButton.setSelected(false);
			} else if (e.getSource() == noUseModelRadioButton) {
				useModelRadioButton.setSelected(false);
				noUseModelRadioButton.setSelected(true);
			}
			radioButtonChanged();
			batchSet();
		}
	};

	private void radioButtonChanged() {
		// 当使用模版单选框被选中时，设置comboBox控件可用，负责一律设置不可用
		if (useModelRadioButton.isSelected()) {
			datasetComboBox.setEnabled(true);
			datasourceComboBox.setEnabled(true);
			datasetComboBox.setDatasets(datasourceComboBox.getSelectedDatasource().getDatasets());
		} else {
			datasetComboBox.setEnabled(false);
			datasourceComboBox.setEnabled(false);
		}
	}

	/**
	 * 下拉列表框改变时
	 */
	private ItemListener itemListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == datasourceComboBox) {
				datasetComboBox.setDatasets(datasourceComboBox.getSelectedDatasource().getDatasets());
			}
			batchSet();
		}
	};

	public PanelModel() {
		initComponents();
		initLayout();
		initResource();
		registerEvent();
//		initStates();
	}

	private void initComponents() {
		this.useModelRadioButton = new JRadioButton();
		this.noUseModelRadioButton = new JRadioButton();

		this.datasourceLabel = new JLabel();
		this.datasetLabel = new JLabel();
		this.datasourceComboBox = new DatasourceComboBox();
		this.datasetComboBox = new DatasetComboBox();
		setPanelEnabled(false);
	}

	private void initLayout() {
		this.setBorder(BorderFactory.createTitledBorder(DataEditorProperties.getString("String_Template")));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);
		//@formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup()
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(this.noUseModelRadioButton)
								.addComponent(this.useModelRadioButton)
								.addComponent(this.datasourceLabel)
								.addComponent(this.datasetLabel))
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(this.datasourceComboBox, 150, 150, Short.MAX_VALUE)
								.addComponent(this.datasetComboBox, 150, 150, Short.MAX_VALUE))));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.noUseModelRadioButton))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.useModelRadioButton))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.datasourceLabel)
						.addComponent(this.datasourceComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.datasetLabel)
						.addComponent(this.datasetComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));
		//@formatter:on
	}

	private void initResource() {
		useModelRadioButton.setText(DataEditorProperties.getString("String_CheckBox_TemplateDataset"));
		noUseModelRadioButton.setText(DataEditorProperties.getString("String_CheckBox_NoTemplate"));
		datasourceLabel.setText(CoreProperties.getString("String_Label_TemplateDatasource"));
		datasetLabel.setText(CoreProperties.getString("String_Label_TemplateDataset"));
	}

	/**
	 * 根据JTable选择情况，初始化模版面板
	 *
	 * @param datasetBeans
	 * @param isSameModelState 所有数据含有*不含有模版情况是否相同
	 */
	public void initStates(ArrayList<NewDatasetBean> datasetBeans, Boolean isSameModelState) {

		// 初始化设置的时候移除所有监听
		removeEvent();
		this.datasetBeans = datasetBeans;
		// 以排头对象的属性值进行面板的初始值设置
		datasetBeanFrist = datasetBeans.get(0);
		// 当只选中了一条数据时，不用考虑模版情况
		if (!isSameModelState && datasetBeans.size() > 1) {
			useModelRadioButton.setSelected(false);
			noUseModelRadioButton.setSelected(false);
			radioButtonChanged();
		} else {
			if (datasetBeanFrist.getTemplateDataset() != null) {
				// 当设置数据源的时候去除datasourceComboBox监听事件
				datasourceComboBox.setSelectedDatasource(datasetBeanFrist.getTemplateDataset().getDatasource());
				useModelRadioButton.setSelected(true);
				noUseModelRadioButton.setSelected(false);
				radioButtonChanged();

				datasetComboBox.setSelectedDataset(datasetBeanFrist.getTemplateDataset());
			} else {
				noUseModelRadioButton.setSelected(true);
				useModelRadioButton.setSelected(false);
				radioButtonChanged();
			}
		}
		registerEvent();
	}

	/**
	 * 重设使用的模版
	 * 只有手动操作模版面板中的控件时才会进行批量设置
	 */
	private void batchSet() {
		if (datasetBeans != null) {
			for (int i = 0; i < datasetBeans.size(); i++) {
				if (useModelRadioButton.isSelected()) {
					Dataset templateDataset = datasetComboBox.getSelectedDataset();
					datasetBeans.get(i).setTemplateDataset(templateDataset);
				} else if (noUseModelRadioButton.isSelected()) {
					datasetBeans.get(i).setTemplateDataset(null);
				}
			}
		}
	}

	private void registerEvent() {
		removeEvent();
		useModelRadioButton.addActionListener(actionListener);
		noUseModelRadioButton.addActionListener(actionListener);
		datasourceComboBox.addItemListener(itemListener);
		datasetComboBox.addItemListener(itemListener);
	}

	private void removeEvent() {
		useModelRadioButton.removeActionListener(actionListener);
		noUseModelRadioButton.removeActionListener(actionListener);
		datasourceComboBox.removeItemListener(itemListener);
		datasetComboBox.removeItemListener(itemListener);
	}

	public void setRadioButtonEnabled(Boolean radioButtonEnabled) {
		this.useModelRadioButton.setEnabled(radioButtonEnabled);
		this.noUseModelRadioButton.setEnabled(radioButtonEnabled);

	}

	public void setPanelEnabled(Boolean panelEnabled) {
		this.useModelRadioButton.setEnabled(panelEnabled);
		this.noUseModelRadioButton.setEnabled(panelEnabled);
		this.datasetComboBox.setEnabled(panelEnabled);
		this.datasourceComboBox.setEnabled(panelEnabled);
	}

	/**
	 * 创建数据集类型的设置方法，放在这里设置的原因是为了在设置的时候不触发监听事件，
	 * 而导致给模版赋值的时候失败
	 *
	 * @param datasetSupportedDatasetTypes
	 */
	public void setDatasetSupportedDatasetTypes(DatasetType[] datasetSupportedDatasetTypes) {
		removeEvent();
		datasetComboBox.setSupportedDatasetTypes(datasetSupportedDatasetTypes);
		registerEvent();
	}
}
