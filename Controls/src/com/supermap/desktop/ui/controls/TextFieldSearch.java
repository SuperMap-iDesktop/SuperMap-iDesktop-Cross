package com.supermap.desktop.ui.controls;

import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTextField;

/**
 * 带搜索图标的 TextField
 * 
 * @author highsad
 *
 */
public class TextFieldSearch extends JTextField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ImageIcon searchIcon;

	public TextFieldSearch() {
		this.searchIcon = new ImageIcon(getClass().getResource("/com/supermap/desktop/controlsresources/SortType/Image_FindFiles.png"));
		this.setMargin(new Insets(0, 0, 0, 20));
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
