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
public class SmDialogFormSaveAs extends SmDialog {
	private JLabel labelFormName = new JLabel();
	private SmTextFieldLegit textFieldFormName = new SmTextFieldLegit();

	private SmButton buttonOk = new SmButton(CommonProperties.getString(CommonProperties.OK));
	private SmButton buttonCancle = new SmButton(CommonProperties.getString(CommonProperties.Cancel));
	private ArrayList<String> existNames = new ArrayList<>();

	public SmDialogFormSaveAs() {
		initComponents();
		initLayout();
		initListener();
		initResources();
		initComponentState();
		this.getRootPane().setDefaultButton(buttonOk);
	}

	private void initComponents() {
		setSize(new Dimension(359, 127));
		setLocationRelativeTo(null);
		textFieldFormName.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				boolean isLegit = !StringUtilities.isNullOrEmpty(textFieldValue) && !existNames.contains(textFieldValue);
				buttonOk.setEnabled(isLegit);
				return isLegit;
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return currentValue;
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

	public void setDescription(String text) {
		labelFormName.setText(text);
	}

	public void addExistNames(String... names) {
		Collections.addAll(existNames, names);
	}

	public void setCurrentFormName(String name) {
		textFieldFormName.setText(name);
	}

	public String getCurrentFormName() {
		return textFieldFormName.getBackUpValue();
	}

	@Override
	public DialogResult showDialog() {
		if (!textFieldFormName.isLegitValue(textFieldFormName.getText())) {
			int i = 1;
			String name = textFieldFormName.getText();
			while (!textFieldFormName.isLegitValue(name + "_" + i)) {
				i++;
			}
			textFieldFormName.setText(name + "_" + i);
		}
		return super.showDialog();
	}
}
