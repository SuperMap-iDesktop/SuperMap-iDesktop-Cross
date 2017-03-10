package com.supermap.desktop.ui.controls;

import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.controls.utilities.ControlsResources;

import javax.swing.*;
import java.awt.*;

/**
 * 带搜索图标的 TextField
 *
 * @author highsad
 */
public class TextFieldSearch extends JTextField {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private ImageIcon searchIcon;

	public TextFieldSearch() {
		this.searchIcon = (ImageIcon) ControlsResources.getIcon("/controlsresources/SortType/Image_FindFiles.png");
		this.setMargin(new Insets(0, 0, 0, 20));
		this.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
	}

	@Override
	public void paintComponent(Graphics g) {
		Insets insets = getInsets();
		super.paintComponent(g);
		int iconWidth = this.searchIcon.getIconWidth();
		int iconHeight = this.searchIcon.getIconHeight();
		this.searchIcon.paintIcon(this, g, this.getWidth() - insets.right + (insets.right - iconWidth) / 2, (this.getHeight() - iconHeight) / 2);
	}
}
