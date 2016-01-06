package com.supermap.desktop.ui.controls;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.supermap.data.Resources;
import com.supermap.data.SymbolType;
import com.supermap.desktop.Application;

public class SymbolLibraryDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private transient SymbolLibraryPanel symbolLibraryPanel;
	
	/**
	 * Create the dialog
	 */
	public SymbolLibraryDialog() {
		super();
		setSize(560, 500);
		this.setModal(true);
		initialize();
	}
	
	/**
	 * 获取指定的符号库文件路径
	 * @return
	 */
	public String getCurrentSymPath() {
		return symbolLibraryPanel.getCurrentSymPath();
	}

	/**
	 * 设置符号库文件路径
	 * @param filePath
	 */
	public void setCurrentSymPath(String filePath) {
		symbolLibraryPanel.setCurrentSymPath(filePath);
	}
	
	/**
	 * 获取资源
	 * 
	 * @return
	 */
	public Resources getResources() {
		return symbolLibraryPanel.getResources();
	}

	/**
	 * 设置资源
	 * 
	 * @param resources
	 */
	public void setResources(Resources resources) {	
		symbolLibraryPanel.setResources(resources);
	}

	/**
	 * 获取符号面板类型
	 * 
	 * @return
	 */
	public SymbolType getSymbolType() {
		return symbolLibraryPanel.getType();
	}

	/**
	 * 设置符号面板类型
	 * 
	 * @param symbolType
	 */
	public void setType(SymbolType symbolType) {
		symbolLibraryPanel.setType(symbolType);
	} 
	
	/**
	 * 显示对话框
	 * @return
	 */
	public void showDialog() {
		this.setVisible(true);
	}
	
	/**
	 * 显示符号基类面板对话框
	 * 
	 * @param resources
	 *            资源
	 * @param symbolType
	 *            符号选择器类型
	 */
	public static void showDialog(Resources resources, SymbolType symbolType) {
		SymbolLibraryDialog symbolLibraryDialog = new SymbolLibraryDialog();
		symbolLibraryDialog.setResources(resources);
		symbolLibraryDialog.setType(symbolType);
		symbolLibraryDialog.setVisible(true);
	}
	
	private void initialize(){
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().add(getSymbolLibraryPanel(), BorderLayout.CENTER);
		try {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension frameSize = this.getSize();
			if (frameSize.height > screenSize.height) {
				frameSize.height = screenSize.height;
			}
			if (frameSize.width > screenSize.width) {
				frameSize.width = screenSize.width;
			}
			this.setLocation((screenSize.width - frameSize.width) / 2,
					(screenSize.height - frameSize.height) / 2);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}
	
	/**
	 * @return
	 */
	protected SymbolLibraryPanel getSymbolLibraryPanel() {
		if (symbolLibraryPanel == null) {
			symbolLibraryPanel = new SymbolLibraryPanel();
		}
		return symbolLibraryPanel;
	}
}
