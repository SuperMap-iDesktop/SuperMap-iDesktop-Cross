package com.supermap.desktop.ui.controls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.plaf.metal.MetalComboBoxIcon;

public class ColorSelectButton extends JButton {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panelColorDisplay;
	private JLabel labelArraw;
	
	
	private transient ColorSelectionPanel colorSelectionPanel;
	
	private Color color;
	private transient LocalActionListener actionListener = new LocalActionListener();
	
	public ColorSelectButton(Color color){
		this.color = color;
		initComponents();
		registActionListener();
	}
	
	/**
	 * 注册事件
	 */
	private void registActionListener() {
		this.addActionListener(this.actionListener);
	}
	
	/**
	 * 注销事件
	 */
	public void unRegistActionListener(){
		this.removeActionListener(this.actionListener);
	}
	
	private void initComponents(){
		this.panelColorDisplay = new JPanel();
		this.labelArraw = new JLabel();
		this.labelArraw.setIcon(new MetalComboBoxIcon());
		this.labelArraw.setPreferredSize(new Dimension(10,15));
		this.panelColorDisplay.setBackground(color);
		this.setLayout(new GridBagLayout());
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(this.panelColorDisplay, BorderLayout.CENTER);
		panel.add(this.labelArraw, BorderLayout.EAST);
		setBorder(BorderFactory.createEtchedBorder(1));
		
		this.add(panel,new GridBagConstraintsHelper(0,0,1,1).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1).setAnchor(GridBagConstraints.WEST));
	}		
	
	public void selectColor(Color newColor) {
		// 为了每次点击颜色按钮都触发事件，所以传入新颜色和null
		firePropertyChange("m_selectionColors", null, newColor);
	}
	
	class LocalActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			colorSelectionPanel = new ColorSelectionPanel();
			
			final JPopupMenu popupMenu = new JPopupMenu();
			popupMenu.setBorderPainted(false);
			popupMenu.add(colorSelectionPanel, BorderLayout.CENTER);
			colorSelectionPanel.setPreferredSize(new Dimension(170, 205));
			int x = panelColorDisplay.getWidth()-180;
			int y = panelColorDisplay.getHeight();
			popupMenu.show(panelColorDisplay, x, y);
			colorSelectionPanel.addPropertyChangeListener("m_selectionColor", new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					color = (Color) evt.getNewValue();
					selectColor(color);
					panelColorDisplay.setBackground(color);
					panelColorDisplay.updateUI();
					popupMenu.setVisible(false);
				}
			});
		}
	}
	
	/**
	 * 获取选中的颜色
	 * @return
	 */
	public Color getColor(){
		return this.color;
	}
}
