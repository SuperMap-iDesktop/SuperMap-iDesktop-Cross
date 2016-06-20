package com.supermap.desktop.dialog.symbolDialogs;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.DecimalFormat;
import java.text.MessageFormat;

/**
 * @author XiaJt
 */
public class SymbolSpinnerUtilties {
	private static DecimalFormat df = new DecimalFormat("0.0");

	private static final FocusAdapter selectAllFocusAdapter = new FocusAdapter() {
		@Override
		public void focusGained(final FocusEvent e) {
			if (e.getSource() instanceof JTextComponent) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						((JTextComponent) e.getSource()).selectAll();
					}
				});
			}
		}
	};

	public static void initSpinners(double min, double max, double step, String applyPattern, JSpinner... spinners) {
		for (JSpinner spinner : spinners) {
			spinner.setToolTipText(MessageFormat.format(ControlsProperties.getString("String_Range"), df.format(min), max));
			SpinnerNumberModel model = new SpinnerNumberModel(min, min, max, step);
			spinner.setModel(model);
			JSpinner.NumberEditor numberEditor = (JSpinner.NumberEditor) spinner.getEditor();
			final JTextField widthTextField = numberEditor.getTextField();
			numberEditor.getFormat().applyPattern(applyPattern);
			widthTextField.addFocusListener(selectAllFocusAdapter);
		}
	}

	public static boolean isLegitNumber(double min, double max, String value) {
		if (StringUtilities.isNullOrEmpty(value)) {
			return false;
		}
		if (value.contains("d")) {
			return false;
		}
		try {
			Double dValue = Double.valueOf(value);
			if (dValue > max || dValue < min) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean isLegitNumber(int min, int max, String value) {
		if (StringUtilities.isNullOrEmpty(value)) {
			return false;
		}
		if (value.contains("d")) {
			return false;
		}
		try {
			Integer integer = Integer.valueOf(value);
			if (integer > max || integer < min) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
