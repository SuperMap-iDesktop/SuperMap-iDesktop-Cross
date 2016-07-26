package com.supermap.desktop.CtrlAction.settings;

import javax.swing.*;

/**
 * @author XiaJT
 */
public interface ISetting {
	JPanel getPanel();

	void apply();

	void dispose();
}
