package com.supermap.desktop.CtrlAction.Dataset.createNewDataset;

import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by yuanR on 2017/8/18 0018.
 * 数据集模版面板
 */
public class PanelModel extends JPanel {

	private static final long serialVersionUID = 1L;

	private ButtonGroup buttonGroup;
	private JRadioButton usemodelRadioButton;
	private JRadioButton noUseModelRadioButton;
	private JLabel datasourceLabel;
	private JLabel datasetLabel;
	private DatasourceComboBox datasourceComboBox;
	private DatasetComboBox datasetComboBox;

	private ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == usemodelRadioButton) {
				setPanelEnabled(true);
			} else if (e.getSource() == noUseModelRadioButton) {
				setPanelEnabled(false);
			}
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
		this.buttonGroup = new ButtonGroup();
		this.usemodelRadioButton = new JRadioButton();
		this.noUseModelRadioButton = new JRadioButton();
		this.buttonGroup.add(this.usemodelRadioButton);
		this.buttonGroup.add(this.noUseModelRadioButton);

		this.datasourceLabel = new JLabel();
		this.datasetLabel = new JLabel();
		this.datasourceComboBox = new DatasourceComboBox();
		this.datasetComboBox = new DatasetComboBox();
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
								.addComponent(this.usemodelRadioButton)
								.addComponent(this.datasourceLabel)
								.addComponent(this.datasetLabel))
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(this.datasourceComboBox, 150, 150, Short.MAX_VALUE)
								.addComponent(this.datasetComboBox, 150, 150, Short.MAX_VALUE))));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.noUseModelRadioButton))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.usemodelRadioButton))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.datasourceLabel)
						.addComponent(this.datasourceComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.datasetLabel)
						.addComponent(this.datasetComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));
		//@formatter:on
	}

	private void initResource() {
		usemodelRadioButton.setText(DataEditorProperties.getString("String_CheckBox_TemplateDataset"));
		noUseModelRadioButton.setText(DataEditorProperties.getString("String_CheckBox_NoTemplate"));
		datasourceLabel.setText(CoreProperties.getString("String_Label_TemplateDatasource"));
		datasetLabel.setText(CoreProperties.getString("String_Label_TemplateDataset"));
	}

	public void initStates(ArrayList<NewDatasetBean> datasetBean) {

	}

	private void registerEvent() {
		usemodelRadioButton.addActionListener(actionListener);
		noUseModelRadioButton.addActionListener(actionListener);
	}

	private void setPanelEnabled(Boolean panelEnabled) {
		this.datasetComboBox.setEnabled(panelEnabled);
		this.datasourceComboBox.setEnabled(panelEnabled);
	}
}
