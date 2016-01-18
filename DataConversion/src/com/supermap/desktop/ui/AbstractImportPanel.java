package com.supermap.desktop.ui;

import javax.swing.JPanel;

public abstract class AbstractImportPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	/**
	 * 注册事件
	 */
	abstract void registActionListener();
	
	/**
	 * 注销事件
	 */
	abstract void unregistActionListener();
	
	/**
	 * 创建界面入口
	 */
	abstract void initComponents();
	
	/**
	 * 国际化
	 */
	abstract void initResource();
}
