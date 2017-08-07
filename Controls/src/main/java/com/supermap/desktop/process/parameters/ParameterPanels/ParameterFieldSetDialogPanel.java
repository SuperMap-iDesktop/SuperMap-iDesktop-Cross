package com.supermap.desktop.process.parameters.ParameterPanels;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.parameter.ipls.ParameterFieldSetDialog;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.FieldsSetDialog;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author XiaJT
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.FIELD_SET_DIALOG)
public class ParameterFieldSetDialogPanel extends SwingPanel {
	private ParameterFieldSetDialog parameterFieldSetDialog;
	private JButton button = new JButton();
	private FieldsSetDialog fieldsSetDialog = new FieldsSetDialog();

	public ParameterFieldSetDialogPanel(IParameter parameter) {
		super(parameter);
		parameterFieldSetDialog = ((ParameterFieldSetDialog) parameter);
		button.setEnabled(parameterFieldSetDialog.isEnabled());
		initComponents();
		initLayout();
	}

	private void initComponents() {
		String describe = parameterFieldSetDialog.getDescribe() == null ? CommonProperties.getString("String_FieldsSetting") : parameterFieldSetDialog.getDescribe();
		button.setText(describe);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (fieldsSetDialog.showDialog(parameterFieldSetDialog.getSourceDataset(), parameterFieldSetDialog.getResultDataset()) == DialogResult.OK) {
					parameterFieldSetDialog.setSourceFieldNames(fieldsSetDialog.getSourceFields());
					parameterFieldSetDialog.setResultFieldNames(fieldsSetDialog.getOverlayAnalystFields());
				}
			}
		});
	}

	private void initLayout() {
		panel.setLayout(new GridBagLayout());
		panel.add(button, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL));
	}


}
