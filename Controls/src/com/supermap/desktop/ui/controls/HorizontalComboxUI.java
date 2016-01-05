package com.supermap.desktop.ui.controls;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

public class HorizontalComboxUI extends BasicComboBoxUI {
	@Override
	protected ComboPopup createPopup() {

		BasicComboPopup popup = new BasicComboPopup(comboBox) {

			private static final long serialVersionUID = 1L;

			@Override
			protected JScrollPane createScroller() {
				return new JScrollPane(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			}
		};
		return popup;
	}

}
