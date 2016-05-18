package com.supermap.desktop.geometryoperation.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;

import com.supermap.desktop.controls.utilties.ComponentFactory;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.ui.SMFormattedTextField;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;

/**
 * 光滑系数
 * 
 * @author highsad
 *
 */
public class JDialogSmoothRatio extends SmDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel labelSmoothRatio;
	private SMFormattedTextField textFieldSmoothRatio;
	private JButton buttonOK = ComponentFactory.createButtonOK();
	private JButton buttonCancel = ComponentFactory.createButtonCancel();

	public Integer getSmoothRatio() {
		return (Integer) this.textFieldSmoothRatio.getValue();
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
		NumberFormat format = NumberFormat.getIntegerInstance();
		format.setMaximumFractionDigits(10);
		format.setMinimumIntegerDigits(2);
		this.textFieldSmoothRatio = new SMFormattedTextField(format);
		this.textFieldSmoothRatio.setValue(4);

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
	}
}
