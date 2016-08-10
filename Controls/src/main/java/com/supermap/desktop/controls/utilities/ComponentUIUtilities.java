package com.supermap.desktop.controls.utilities;

import javax.swing.*;
import java.awt.*;

/**
 * @author XiaJT
 */
public class ComponentUIUtilities {
	private ComponentUIUtilities() {

	}

	public static Window getParentWindow(Component c) {
		if (c == null) {
			return JOptionPane.getRootFrame();
		} else if (c instanceof Window) {
			return (Window) c;
		}
		return getParentWindow(c.getParent());
	}
}
