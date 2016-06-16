package com.supermap.desktop.controls.utilties;

import javax.swing.*;
import java.awt.*;

/**
 * @author XiaJT
 */
public class ComponentUtilties {
	private ComponentUtilties() {

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
