package com.supermap.desktop.dialog;

import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author XiaJT
 */
public class JDialogFormSaveAs extends SmDialog {
	private JLabel labelFormName = new JLabel();
	private SmTextFieldLegit textFieldFormName = new SmTextFieldLegit();

	private String name;
	private SmButton buttonOk = new SmButton(CommonProperties.getString(CommonProperties.OK));
	private SmButton buttonCancle = new SmButton(CommonProperties.getString(CommonProperties.Cancel));
	private ArrayList<String> existNames = new ArrayList<>();

	public JDialogFormSaveAs() {
		initComponents();
		initLayout();
		initListener();
		initResources();
		initComponentState();
	}

	private void initComponents() {
		setSize(new Dimension(359, 127));
		setLocationRelativeTo(null);
		textFieldFormName.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				return !StringUtilities.isNullOrEmpty(textFieldValue) && !existNames.contains(textFieldValue);
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		});
	}

	private void initLayout() {
		this.setLayout(new GridBagLayout());
		this.add(labelFormName, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setInsets(10, 10, 0, 10).setAnchor(GridBagConstraints.WEST));
		this.add(textFieldFormName, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 0).setInsets(5, 10, 0, 10).setFill(GridBagConstraints.HORIZONTAL));
		this.add(new JPanel(), new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH));

		JPanel jPanelButton = new JPanel();
		jPanelButton.setLayout(new GridBagLayout());
		jPanelButton.add(buttonOk, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setInsets(5, 10, 10, 0));
		jPanelButton.add(buttonCancle, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 10, 10));

		this.add(jPanelButton, new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
	}

	private void initListener() {
		buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogResult = DialogResult.OK;
				setVisible(false);
			}
		});

		buttonCancle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogResult = DialogResult.CANCEL;
				setVisible(false);
			}
		});
	}

	private void initResources() {

	}

	private void initComponentState() {

	}

	public void setDescribeText(String text) {
		labelFormName.setText(text);
	}

	public void addExistNames(String... names) {
		Collections.addAll(existNames, names);
	}

	public void setCurrentFormName(String name) {
		this.name = name;
		textFieldFormName.setText(name);
	}

	public String getCurrentFormName() {
		return textFieldFormName.getBackUpValue();
	}
}
