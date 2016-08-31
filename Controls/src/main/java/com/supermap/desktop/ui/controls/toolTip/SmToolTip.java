package com.supermap.desktop.ui.controls.toolTip;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class SmToolTip extends JToolTip {

	protected String tipText;


	@Override
	public void setTipText(String tipText) {
		this.tipText = tipText;
		super.setTipText(tipText);
	}

	@Override
	public void setComponent(JComponent c) {
		super.setComponent(c);
	}
}
