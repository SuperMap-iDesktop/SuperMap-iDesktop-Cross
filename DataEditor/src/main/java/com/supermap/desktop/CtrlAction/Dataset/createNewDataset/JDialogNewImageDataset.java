package com.supermap.desktop.CtrlAction.Dataset.createNewDataset;

import com.supermap.data.DatasetType;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by yuanR on 2017/8/15 0015.
 */
public class JDialogNewImageDataset extends SmDialog {

	private BasicInfoPanel basicInfoPanel;
	private ResolutionPanel resolutionPanel;
	private ImageDatasetPropertyPanel imagePropertyPanel;
	private DatasetBoundsPanel datasetBoundsPanel;
	private SmButton buttonOk;
	private SmButton buttonCancel;

	public JDialogNewImageDataset() {
		initComponents();
		initLayout();
		registerEvent();
		this.setTitle(DataEditorProperties.getString("String_NewDatasetImage"));
		this.setModal(true);
		setSize(700, 420);
		this.setLocationRelativeTo(null);
	}


	private void initComponents() {

		basicInfoPanel = new BasicInfoPanel(DatasetType.IMAGE);
		resolutionPanel = new ResolutionPanel();
		resolutionPanel.setBorder(BorderFactory.createTitledBorder(DataEditorProperties.getString("String_NewDataset_RatioInfo")));
		imagePropertyPanel = new ImageDatasetPropertyPanel();
		datasetBoundsPanel = new DatasetBoundsPanel();

		// 按钮
		buttonOk = new SmButton(CommonProperties.getString(CommonProperties.OK));
		buttonCancel = new SmButton(CommonProperties.getString(CommonProperties.Cancel));
		this.getRootPane().setDefaultButton(this.buttonOk);
	}

	private void initLayout() {
		Panel centerPanel = new Panel();
		GroupLayout groupLayout = new GroupLayout(centerPanel);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		centerPanel.setLayout(groupLayout);
		//@formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup()
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(this.basicInfoPanel)
								.addComponent(this.resolutionPanel))
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(this.imagePropertyPanel)
								.addComponent(this.datasetBoundsPanel))));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.basicInfoPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.imagePropertyPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.resolutionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.datasetBoundsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));
		//@formatter:on

		// 按钮面板
		Panel buttonPanel = new Panel();
		buttonPanel.setLayout(new GridBagLayout());

		buttonPanel.add(buttonOk, new GridBagConstraintsHelper(0, 0).setAnchor(GridBagConstraints.EAST).setInsets(5).setWeight(1, 1));
		buttonPanel.add(buttonCancel, new GridBagConstraintsHelper(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(5, 0, 5, 5).setWeight(0, 1));

		this.setLayout(new GridBagLayout());
		this.add(centerPanel, new GridBagConstraintsHelper(0, 0).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setInsets(5));
		this.add(buttonPanel, new GridBagConstraintsHelper(0, 1).setWeight(1, 0).setFill(GridBagConstraints.BOTH).setInsets(5));
		// @formatter:on

	}

	private void registerEvent() {
		this.buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonOk_Clicked();
			}
		});

		this.buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonCancel_Clicked();
			}
		});
	}

	private void buttonOk_Clicked() {
		setDialogResult(DialogResult.OK);
		this.dispose();
	}

	private void buttonCancel_Clicked() {
		setDialogResult(DialogResult.CANCEL);
		this.dispose();
	}
}
