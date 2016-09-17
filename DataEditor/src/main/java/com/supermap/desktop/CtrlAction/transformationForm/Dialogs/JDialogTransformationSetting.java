package com.supermap.desktop.CtrlAction.transformationForm.Dialogs;

import com.supermap.data.TransformationMode;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.ColorSelectButton;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import java.awt.*;

/**
 * Created by SillyB on 2016/9/17.
 */
public class JDialogTransformationSetting extends SmDialog {
	private JLabel labelSelectedColor = new JLabel();
	private ColorSelectButton buttonSelectedColor = new ColorSelectButton(Color.blue);
	private JLabel labelUnSelectedColor = new JLabel();
	private ColorSelectButton buttonUnSelectedColor = new ColorSelectButton(Color.red);
	private JLabel labelTransformationMode = new JLabel();
	private JComboBox<TransformationMode> comboBoxTransformationMode = new JComboBox<>();
	private SmButton buttonOK = new SmButton();
	private SmButton buttonCancel = new SmButton();

	public JDialogTransformationSetting() {
		setSize(200, 200);
		setTitle(DataEditorProperties.getString("String_TransformationSetting"));
		init();
	}

	private void init() {
		initComponent();
		initListener();
		initResources();
		initLayout();
		initComponentState();
	}

	private void initComponent() {

	}

	private void initListener() {

	}

	private void initResources() {
		labelSelectedColor.setText(DataEditorProperties.getString("String_SelectedColor"));
		labelUnSelectedColor.setText(DataEditorProperties.getString("String_UnSelectedColor"));
		labelTransformationMode.setText(DataEditorProperties.getString("String_TransformationMode"));
		buttonOK.setText(CommonProperties.getString(CommonProperties.OK));
		buttonCancel.setText(CommonProperties.getString(CommonProperties.Cancel));
	}

	private void initLayout() {
		this.setLayout(new GridBagLayout());
		this.add(labelSelectedColor, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 0, 0));
		this.add(buttonSelectedColor, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setInsets(10, 5, 0, 10));

		this.add(labelUnSelectedColor, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 0));
		this.add(buttonUnSelectedColor, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 0, 10));

		this.add(labelTransformationMode, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.WEST).setInsets(5, 10, 0, 0));
		this.add(comboBoxTransformationMode, new GridBagConstraintsHelper(1, 2, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 0, 10));


	}

	private void initComponentState() {

	}
}
