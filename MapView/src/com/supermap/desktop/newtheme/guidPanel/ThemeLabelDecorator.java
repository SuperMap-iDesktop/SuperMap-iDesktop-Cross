package com.supermap.desktop.newtheme.guidPanel;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

public class ThemeLabelDecorator extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  JLabel labelIcon;
	private JLabel labelText;
	private final Color selectColor = new Color(185, 214, 255);
	
	public ThemeLabelDecorator(){
		
	}
	
	public  ThemeLabelDecorator(ImageIcon icon,String text){
		init(icon,text);
	}
	private void init(ImageIcon icon,String text) {
		this.setBackground(Color.WHITE);
		this.labelIcon = new JLabel(icon);
		this.labelText = new JLabel(text);
		this.setLayout(new GridBagLayout());
		this.add(this.labelIcon, new GridBagConstraintsHelper(0, 0, 1, 1).setInsets(5).setAnchor(GridBagConstraints.CENTER));
		this.add(this.labelText, new GridBagConstraintsHelper(0, 1, 1, 1).setInsets(0,5,5,5).setAnchor(GridBagConstraints.CENTER));
	}
	
	public void selected(boolean selected){
		if (selected) {
			this.labelIcon.setOpaque(true);
			this.labelText.setOpaque(true);
			this.labelIcon.setBackground(Color.WHITE);
			this.labelText.setBackground(selectColor);
		}else {
			this.labelIcon.setOpaque(true);
			this.labelText.setOpaque(true);
			this.labelIcon.setBackground(Color.WHITE);
			this.labelText.setBackground(Color.WHITE);
		}
	}
	
}
