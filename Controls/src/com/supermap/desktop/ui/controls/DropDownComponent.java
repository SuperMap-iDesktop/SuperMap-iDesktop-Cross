package com.supermap.desktop.ui.controls;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.plaf.metal.MetalComboBoxIcon;

/**
 * 颜色选择器弹出组件
 * 
 * @author zhaosy
 */
public class DropDownComponent extends JComponent {

	private static final long serialVersionUID = 1L;
	protected JComponent jComponentDropDown;
	protected JComponent jComponentDisplay;
	protected JButton jButtonArrow;

	// add by xuzw 2010-09-16
	// 弹出组件改用JPopupMenu，可解决点击别处颜色选择面板该消失的问题
	private JPopupMenu popupMenu;

	/**
	 * 构造函数
	 * 
	 * @param display 用于显示的组件
	 * @param dropDownComponent 下拉组件
	 */
	public DropDownComponent(JComponent display, JComponent dropDownComponent) {
		jComponentDisplay = display;
		jComponentDropDown = dropDownComponent;

		jButtonArrow = new ControlButton();
		jButtonArrow.setIcon(new MetalComboBoxIcon());
		jButtonArrow.setPreferredSize(new Dimension(16, jComponentDisplay.getPreferredSize().height));
		Insets insets = jButtonArrow.getMargin();
		jButtonArrow.setMargin(new Insets(insets.top, 1, insets.bottom, 1));

		popupMenu = new JPopupMenu();
		popupMenu.setBorderPainted(false);
		popupMenu.add(jComponentDropDown, BorderLayout.CENTER);
		jComponentDropDown.setPreferredSize(new Dimension(170, 205));
		jButtonArrow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				popupMenu.show(jButtonArrow, -jComponentDisplay.getWidth(), jComponentDisplay.getHeight());
			}

		});
		jButtonArrow.setComponentPopupMenu(popupMenu);

		setupLayout();
	}

	/**
	 * 获取m_popupMenu
	 * 
	 * @return
	 */
	public JPopupMenu getPopupMenu() {
		return popupMenu;
	}

	/**
	 * 获取m_arrow
	 * 
	 * @return
	 */
	protected JButton getArrowButton() {
		return jButtonArrow;
	}

	/** 按钮布局 */
	protected void setupLayout() {
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(gbl);

		c.weightx = 100;
		c.weighty = 100;
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		gbl.setConstraints(jComponentDisplay, c);
		add(jComponentDisplay);

		c.weightx = 0;
		c.gridx++;
		gbl.setConstraints(jButtonArrow, c);
		add(jButtonArrow);
	}
}
