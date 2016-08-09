package com.supermap.desktop.controls.utilities;

import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class JLabelUIUtilities {
	private JLabelUIUtilities() {
		// 工具类无构造函数
	}

	public static void highLightText(JLabel label, String text) {
		if (label == null || StringUtilities.isNullOrEmpty(label.getText()) || StringUtilities.isNullOrEmpty(text)) {
			return;
		}
		String labelText = label.getText();
		StringBuilder stringBuilder = new StringBuilder("<html>");
		StringBuilder stringBuilderEqual = new StringBuilder();
		StringBuilder stringBuilderUnEqual = new StringBuilder();

		int i = 0, j = 0;
		for (; i < labelText.length(); i++) {
			if (j < text.length() && Character.toLowerCase(labelText.charAt(i)) == Character.toLowerCase(text.charAt(j))) {
				if (stringBuilderUnEqual.length() > 0) {
					stringBuilder.append("<span bgcolor=\"white\">" + stringBuilderUnEqual.toString() + "</span>");
					stringBuilderUnEqual.setLength(0);
				}
				stringBuilderEqual.append(labelText.charAt(i));
				j++;
			} else {
				if (stringBuilderEqual.length() > 0) {
					stringBuilder.append("<span bgcolor=\"yellow\">" + stringBuilderEqual.toString() + "</span>");
					stringBuilderEqual.setLength(0);
				}
				stringBuilderUnEqual.append(labelText.charAt(i));
			}
		}
		if (stringBuilderUnEqual.length() > 0) {
			stringBuilder.append("<span bgcolor=\"white\">" + stringBuilderUnEqual.toString() + "</span>");
			stringBuilderUnEqual.setLength(0);
		}
		if (stringBuilderEqual.length() > 0) {
			stringBuilder.append("<span bgcolor=\"yellow\">" + stringBuilderEqual.toString() + "</span>");
			stringBuilderEqual.setLength(0);
		}
		stringBuilder.append("</html>");
		label.setText(stringBuilder.toString());
	}
}
