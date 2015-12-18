package com.supermap.scene;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.plaf.metal.MetalComboBoxIcon;

public class DropDownComponent extends JComponent {
	private static final long serialVersionUID = 1L;
	protected JComponent m_dropdownComponent;
	protected JComponent m_displayComponent;
	protected JButton m_arrow;

	// add by xuzw 2010-09-16
	// 弹出组件改用JPopupMenu，可解决点击别处颜色选择面板该消失的问题
	private JPopupMenu m_popupMenu;

	/** 构造函数
	 * 
	 * @param display 用于显示的组件
	 * @param dropDownComponent 下拉组件 */
	public DropDownComponent(JComponent display, JComponent dropDownComponent) {
		m_displayComponent = display;
		m_dropdownComponent = dropDownComponent;

		m_arrow = new ControlButton();
		m_arrow.setIcon(new MetalComboBoxIcon());
		m_arrow.setPreferredSize(new Dimension(16, m_displayComponent.getPreferredSize().height));
		Insets insets = m_arrow.getMargin();
		m_arrow.setMargin(new Insets(insets.top, 1, insets.bottom, 1));

		m_popupMenu = new JPopupMenu();
//		m_popupMenu.setBorderPainted(false);
		
		String path = "F:/iDesktopJava/Resources/MapView/Menu/MapOperator/Measure.png";
		ImageIcon defaultIcon = new ImageIcon(path);
        JMenuItem item3 = new JMenuItem("1111");
        item3.setIcon(defaultIcon);
        m_popupMenu.add(item3);
		JMenuItem item4 = new JMenuItem("2222");
		item4.setIcon(defaultIcon);
		m_popupMenu.add(item4);		
		
//		m_dropdownComponent.setPreferredSize(new Dimension(170, 205));
		m_arrow.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
//				m_popupMenu.show(m_arrow, -m_displayComponent.getWidth(), m_displayComponent.getHeight());
				int x = (int)m_displayComponent.getLocation().getX() - m_displayComponent.getWidth();
				int y = (int)m_displayComponent.getLocation().getY() + m_displayComponent.getHeight();
				m_popupMenu.show(m_arrow, x, y);
			}

		});
		m_arrow.setComponentPopupMenu(m_popupMenu);

		setupLayout();
	}

	/** 获取m_popupMenu
	 * 
	 * @return */
	protected JPopupMenu getPopupMenu() {
		return m_popupMenu;
	}

	/** 获取m_arrow
	 * 
	 * @return */
	protected JButton getArrowButton() {
		return m_arrow;
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
		c.fill = c.BOTH;
		gbl.setConstraints(m_displayComponent, c);
		add(m_displayComponent);

		c.weightx = 0;
		c.gridx++;
		gbl.setConstraints(m_arrow, c);
		add(m_arrow);
	}
}
