package com.supermap.desktop.newtheme.commonUtils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.supermap.desktop.utilities.StringUtilities;

public class LimitedDmt extends PlainDocument {

	private int limit;
	private boolean isNumber;

	public LimitedDmt(int limit, boolean isNumber) {
		super();
		this.isNumber = isNumber;
		this.limit = limit;
	}

	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		if (str == null) {
			return;
		}
		if ((getLength() + str.length()) <= limit && isNumber) {

			char[] upper = str.toCharArray();
			int length = 0;
			for (int i = 0; i < upper.length; i++) {
				if (upper[i] >= '0' && upper[i] <= '9') {
					upper[length++] = upper[i];
				}
			}
			String insertStr = new String(upper, 0, length);
			if (!StringUtilities.isNullOrEmptyString(insertStr) && StringUtilities.isNumeric(insertStr)) {
				super.insertString(offset, new String(upper, 0, length), attr);
			}
		}
		if ((getLength() + str.length()) <= limit && !isNumber) {
			char[] upper = str.toCharArray();
			int length = 0;
			for (int i = 0; i < upper.length; i++) {
				upper[length++] = upper[i];
			}
			super.insertString(offset, new String(upper, 0, length), attr);
		}
	}
}