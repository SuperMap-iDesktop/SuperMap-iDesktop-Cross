package com.supermap.desktop.ui.controls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * 颜色选择器面板
 * 
 * @author zhaosy
 *
 */
public class DropDownColor extends JPanel {

	private static final long serialVersionUID = 1L;
	private Color selectColor;

	private transient DropDownComponent dropDownComponent;

	private transient ColorSelectionPanel colorSelectionPanel;

	/**
	 * 创建颜色选择下拉面板
	 * 
	 * @param componentDisplay 用于展示的组件
	 */
	public DropDownColor(final JComponent componentDisplay) {
		colorSelectionPanel = new ColorSelectionPanel();
		dropDownComponent = new DropDownComponent(componentDisplay, colorSelectionPanel);
		colorSelectionPanel.addPropertyChangeListener("m_selectionColor", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				dropDownComponent.getPopupMenu().setVisible(false);
				selectColor = (Color) evt.getNewValue();
				selectColor(selectColor);
			}
		});
		add(dropDownComponent, BorderLayout.CENTER);

	}

	public void selectColor(Color newColor) {
		// modify by xuzw 2010-09-20 UGOSPII-1506
		// 为了每次点击颜色按钮都触发事件，所以传入新颜色和null
		firePropertyChange("m_selectionColors", null, newColor);
	}

	public Color getColor() {
		return selectColor;
	}

	/**
	 * 获取触发按钮
	 * 
	 * @return
	 */
	public JButton getArrowButton() {
		return dropDownComponent.getArrowButton();
	}
}
