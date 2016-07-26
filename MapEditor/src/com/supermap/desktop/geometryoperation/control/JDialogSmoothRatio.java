package com.supermap.desktop.geometryoperation.control;

import com.supermap.desktop.Application;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 光滑系数
 *
 * @author highsad
 */
public class JDialogSmoothRatio extends SmDialog {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JLabel labelSmoothRatio;
	private SmTextFieldLegit textFieldSmoothRatio = ComponentFactory.createIntegerTextField(4, 2, 10);
	private JButton buttonOK = ComponentFactory.createButtonOK();
	private JButton buttonCancel = ComponentFactory.createButtonCancel();

	public Integer getSmoothRatio() {
		return Integer.valueOf(this.textFieldSmoothRatio.getText());
	}

	public JDialogSmoothRatio() {
		initializeComponents();
		registerEvents();
		setSize(350, 115);
		setLocationRelativeTo(null);
	}

	private void initializeComponents() {
		setTitle(MapEditorProperties.getString("String_GeometryOperation_Smooth_Title"));
		this.labelSmoothRatio = new JLabel(MapEditorProperties.getString("String_Label_SmoothRatio"));

		GroupLayout gl = new GroupLayout(getContentPane());
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);
		getContentPane().setLayout(gl);

		// @formatter:off
		gl.setHorizontalGroup(gl.createParallelGroup(Alignment.LEADING)
				.addGroup(gl.createSequentialGroup()
						.addComponent(this.labelSmoothRatio)
						.addComponent(this.textFieldSmoothRatio, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
				.addGroup(gl.createSequentialGroup()
						.addGap(10, 10, Short.MAX_VALUE)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		
		gl.setVerticalGroup(gl.createSequentialGroup()
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelSmoothRatio)
						.addComponent(this.textFieldSmoothRatio, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(5, 5, Short.MAX_VALUE)
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		// @formatter:on
	}

	private void registerEvents() {
		this.buttonOK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setDialogResult(DialogResult.OK);
				setVisible(false);
			}
		});

		this.buttonCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setDialogResult(DialogResult.CANCEL);
				setVisible(false);
			}
		});

		this.textFieldSmoothRatio.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				textFieldTextChanged();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				textFieldTextChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				textFieldTextChanged();
			}
		});
	}

	private void textFieldTextChanged() {
		if (this.textFieldSmoothRatio.isLegitValue(this.textFieldSmoothRatio.getText())) {
			this.buttonOK.setEnabled(true);
		} else {
			this.buttonOK.setEnabled(false);

			if (!StringUtilities.isNullOrEmpty(this.textFieldSmoothRatio.getText())) {
				Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_GeometryOperation_Smooth_RatioError"));
			}
		}
	}
}
