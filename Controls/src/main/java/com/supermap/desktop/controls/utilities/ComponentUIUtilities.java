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

	public static void setName(Component c,String name){
		if(c != null){
			c.setName(name);
		}
	}
}
