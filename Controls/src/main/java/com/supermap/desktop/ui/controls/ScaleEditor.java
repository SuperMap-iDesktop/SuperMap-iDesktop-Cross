package com.supermap.desktop.ui.controls;

import com.supermap.desktop.Application;
import com.supermap.desktop.ScaleModel;
import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.exception.InvalidScaleException;
import com.supermap.desktop.ui.SMFormattedTextField;

import javax.swing.*;
import javax.swing.plaf.metal.MetalBorders;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;

public class ScaleEditor extends JPanel {

	/**
	 *
	 */

	private static final long serialVersionUID = 1L;


	private JTextField textFieldFirstPart;
	private SMFormattedTextField textFieldSecondPart;
	private transient ScaleModel scaleModel;

	private transient PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			valueChange(evt);
		}
	};
	// 响应 enter 键，提交编辑
	private transient KeyAdapter keyPressedListener = new KeyAdapter() {
		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				try {
					textFieldSecondPart.commitEdit();
				} catch (ParseException e1) {
					textFieldSecondPart.setValue(textFieldSecondPart.getValue());
				}
			}
		}
	};

	/**
	 * @param scale 比例尺的值，比如 0.000005
	 */
	public ScaleEditor(double scale) {
		try {
			setScaleModel(new ScaleModel(scale));
		} catch (InvalidScaleException e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * @param scaleCaption 比例尺的标签，比如 1:5000
	 */
	public ScaleEditor(String scaleCaption) {
		try {
			setScaleModel(new ScaleModel(scaleCaption));
		} catch (InvalidScaleException e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	public ScaleEditor(ScaleModel scaleModel) {
		setScaleModel(scaleModel);
	}

	public double getScale() {
		return this.scaleModel.getScale();
	}

	public void setScale(double scale) {
		this.textFieldSecondPart.setValue(1 / scale);
	}

	public String getScaleCaption() {
		return this.scaleModel.getScaleCaption();
	}

	public Double getScaleDenominator() {
		return this.scaleModel.getScaleDenominator();
	}

	private void setScaleModel(ScaleModel scaleModel) {
		initializeComponents();
		this.scaleModel = scaleModel;
		this.textFieldSecondPart.setValue(scaleModel.getScaleDenominator());
		registerEvents();
	}

	private void initializeComponents() {
		this.textFieldFirstPart = new JTextField();
		this.textFieldFirstPart.setEditable(false);
		this.textFieldFirstPart.setText(ScaleModel.FIRST_PART);
		this.textFieldFirstPart.setBorder(null);

		this.textFieldSecondPart = new SMFormattedTextField();
		this.textFieldSecondPart.setBorder(null);

		setBorder(MetalBorders.getTextFieldBorder());
		setLayout(new BorderLayout());
		add(this.textFieldFirstPart, BorderLayout.WEST);
		add(this.textFieldSecondPart, BorderLayout.CENTER);
	}

	private void registerEvents() {

		this.textFieldSecondPart.addPropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE, this.propertyChangeListener);
		this.textFieldSecondPart.addKeyListener(this.keyPressedListener);
	}

	private void valueChange(PropertyChangeEvent e) {
		try {
			ScaleModel oldModel = this.scaleModel;
			if (ScaleModel.isLegitScaleString(1 / Double.valueOf(String.valueOf(e.getNewValue())))) {
				//输入值判断
				this.scaleModel = new ScaleModel(1 / Double.valueOf(String.valueOf(e.getNewValue())));
			} else {
				this.scaleModel = oldModel;
			}
			firePropertyChange(ControlDefaultValues.PROPERTYNAME_VALUE, oldModel, this.scaleModel);
		} catch (Exception e1) {
			Application.getActiveApplication().getOutput().output(e1);
		}
	}

	@Override
	public String toString() {
		return this.scaleModel.getScaleCaption();
	}
}
