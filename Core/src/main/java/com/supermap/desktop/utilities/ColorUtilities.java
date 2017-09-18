package com.supermap.desktop.utilities;

import java.awt.*;

/**
 * Created by lixiaoyao on 2017/9/15.
 */
public class ColorUtilities {

	public ColorUtilities(){
		// do nothing
	}

	/**
	 * Regenerate colors according to transparency
	 * @param oldColor
	 * @param transparence  Effective range:0————255
	 */
	public static Color resetColor(Color oldColor, int transparence) {
		Color newColor = new Color((float) (oldColor.getRed() / 255.0), (float) (oldColor.getGreen() / 255.0), (float) (oldColor.getBlue() / 255.0), (float) (1.0 - transparence / 100.0));
		return newColor;
	}
}
