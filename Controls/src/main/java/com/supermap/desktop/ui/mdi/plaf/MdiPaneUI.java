package com.supermap.desktop.ui.mdi.plaf;

import java.awt.Graphics;

import com.supermap.desktop.ui.mdi.MdiPane;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

public class MdiPaneUI extends ComponentUI {

	private MdiPane mdiPane;

	public static ComponentUI createUI(JComponent c) {
		return new MdiPaneUI();
	}

	@Override
	public void installUI(JComponent c) {
		this.mdiPane = (MdiPane) c;
	}

	@Override
	public void uninstallUI(JComponent c) {
		c.setLayout(null);
		this.mdiPane = null;
	}

	@Override
	public void paint(Graphics g, JComponent c) {

	}
}
