package com.supermap.desktop.geometryoperation.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.supermap.data.ResampleType;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.utilities.ResampleTypeUtilities;

public class JDialogResample extends SmDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static double DEFAULT_PARAMETER = 0.4;

	private JLabel labelResampleType;
	private JLabel labelParameter;
	private JComboBox<String> comboBoxResampleType;
	private SmTextFieldLegit textFieldParameter;
	private JButton buttonOK = ComponentFactory.createButtonOK();
	private JButton buttonCancel = ComponentFactory.createButtonCancel();

	public JDialogResample() {
		initializeComponents();
		registerEvents();
	}

	public ResampleType getResampleType() {
		return ResampleTypeUtilities.valueOf(this.comboBoxResampleType.getSelectedItem().toString());
	}

	public double getParameter() {
		return Double.valueOf(this.textFieldParameter.getText());
	}

	private void initializeComponents() {
		setTitle(MapEditorProperties.getString("String_GeometryOperation_Resample_Title"));
		this.labelResampleType = new JLabel(MapEditorProperties.getString("String_Label_ResampleType"));
		this.labelParameter = new JLabel(MapEditorProperties.getString("String_GeometryOperation_Resample_Parameter"));
		this.comboBoxResampleType = new JComboBox<>();
		this.comboBoxResampleType.addItem(ResampleTypeUtilities.toString(ResampleType.RTBEND));
		this.comboBoxResampleType.addItem(ResampleTypeUtilities.toString(ResampleType.RTGENERAL));
		this.comboBoxResampleType.setSelectedIndex(0);
		this.textFieldParameter = ComponentFactory.createNumericTextField(DEFAULT_PARAMETER, 0, Double.MAX_VALUE);

		GroupLayout gl = new GroupLayout(getContentPane());
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);
		getContentPane().setLayout(gl);

		// @formatter:off
		gl.setHorizontalGroup(gl.createParallelGroup(Alignment.LEADING)
				.addGroup(gl.createSequentialGroup()
						.addGroup(gl.createParallelGroup(Alignment.LEADING)
								.addComponent(this.labelResampleType)
								.addComponent(this.labelParameter))
						.addGroup(gl.createParallelGroup(Alignment.LEADING)
								.addComponent(this.comboBoxResampleType, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
								.addComponent(this.textFieldParameter, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)))
				.addGroup(gl.createSequentialGroup()
						.addGap(10, 10, Short.MAX_VALUE)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		
		gl.setVerticalGroup(gl.createSequentialGroup()
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelResampleType)
						.addComponent(this.comboBoxResampleType, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelParameter)
						.addComponent(this.textFieldParameter, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		// @formatter:on

		setSize(450, 150);
		setLocationRelativeTo(null);
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

		this.textFieldParameter.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				textFieldParameterTextChange();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				textFieldParameterTextChange();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				textFieldParameterTextChange();
			}
		});
	}

	private void textFieldParameterTextChange() {
		if (this.textFieldParameter.isLegitValue(this.textFieldParameter.getText())) {
			this.buttonOK.setEnabled(true);
		} else {
			this.buttonOK.setEnabled(false);
		}
	}
}
