package com.supermap.desktop.geometryoperation.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.supermap.data.Point2D;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.utilties.ComponentFactory;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.utilties.StringUtilities;

public class JDialogRotateParams extends SmDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel labelCenterX;
	private JLabel labelCenterY;
	private SmTextFieldLegit textFieldCenterX;
	private SmTextFieldLegit textFieldCenterY;
	private JLabel labelAngle;
	private SmTextFieldLegit textFieldAngle;
	private JButton buttonOK = ComponentFactory.createButtonOK();
	private JButton buttonCancel = ComponentFactory.createButtonCancel();

	private DocumentListener documentListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			checkTextFieldValue();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			checkTextFieldValue();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			checkTextFieldValue();
		}
	};

	public JDialogRotateParams(Point2D center) {
		initializeComponents(center);
		registerEvents();
	}

	public double getCenterX() {
		return Double.valueOf(this.textFieldCenterX.getText());
	}

	public double getCenterY() {
		return Double.valueOf(this.textFieldCenterY.getText());
	}

	public double getAngle() {
		return Double.valueOf(this.textFieldAngle.getText());
	}

	private void initializeComponents(Point2D center) {
		setTitle(MapEditorProperties.getString("String_GeometryOperation_Rotate_Title"));
		this.labelCenterX = new JLabel(MapEditorProperties.getString("String_GeometryOperation_Rotate_CenterPointX"));
		this.labelCenterY = new JLabel(MapEditorProperties.getString("String_GeometryOperation_Rotate_CenterPointY"));
		this.labelAngle = new JLabel(MapEditorProperties.getString("String_GeometryOperation_Rotate_Parameter"));
		this.textFieldCenterX = ComponentFactory.createNumericTextField(center.getX(), -1 * Double.MAX_VALUE, Double.MAX_VALUE);
		this.textFieldCenterY = ComponentFactory.createNumericTextField(center.getY(), -1 * Double.MAX_VALUE, Double.MAX_VALUE);
		this.textFieldAngle = ComponentFactory.createNumericTextField(15, -360, 360);

		GroupLayout gl = new GroupLayout(getContentPane());
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);
		getContentPane().setLayout(gl);

		// @formatter:off
		gl.setHorizontalGroup(gl.createParallelGroup(Alignment.LEADING)
				.addGroup(gl.createSequentialGroup()
						.addGroup(gl.createParallelGroup(Alignment.LEADING)
								.addComponent(this.labelCenterX)
								.addComponent(this.labelCenterY)
								.addComponent(this.labelAngle))
						.addGroup(gl.createParallelGroup(Alignment.LEADING)
								.addComponent(this.textFieldCenterX, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
								.addComponent(this.textFieldCenterY, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
								.addComponent(this.textFieldAngle, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)))
				.addGroup(gl.createSequentialGroup()
						.addGap(10, 10, Short.MAX_VALUE)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		
		gl.setVerticalGroup(gl.createSequentialGroup()
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelCenterX)
						.addComponent(this.textFieldCenterX, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelCenterY)
						.addComponent(this.textFieldCenterY, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelAngle)
						.addComponent(this.textFieldAngle, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		// @formatter:on

		setSize(450, 165);
		setLocationRelativeTo(null);
	}

	private void registerEvents() {
		this.textFieldCenterX.getDocument().addDocumentListener(this.documentListener);
		this.textFieldCenterY.getDocument().addDocumentListener(this.documentListener);
		this.textFieldAngle.getDocument().addDocumentListener(this.documentListener);

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
	}

	private void checkTextFieldValue() {
		if (this.textFieldCenterX.isLegitValue(this.textFieldCenterX.getText()) && this.textFieldCenterY.isLegitValue(this.textFieldCenterY.getText())
				&& this.textFieldAngle.isLegitValue(this.textFieldAngle.getText())) {
			this.buttonOK.setEnabled(true);
		} else {
			this.buttonOK.setEnabled(false);

			if (!this.textFieldAngle.isLegitValue(this.textFieldAngle.getText()) && !StringUtilities.isNullOrEmpty(this.textFieldAngle.getText())) {
				Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_GeometryOperation_Rotate_ParameterError"));
			}
		}
	}
}
