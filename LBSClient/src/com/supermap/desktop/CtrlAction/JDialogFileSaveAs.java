package com.supermap.desktop.CtrlAction;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.table.AbstractTableModel;

import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.webHDFS.HDFSDefine;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilties.CursorUtilties;

public class JDialogFileSaveAs extends SmDialog {

	public static void main(String[] args) {
		JDialogFileSaveAs f = new JDialogFileSaveAs();
		f.showDialog();
	}
	
	private JLabel labelServerURL;
	private JTextField textServerURL;
	private JButton buttonBrowser;
	
	private JLabel labelLocalPath;
	private JTextField textLocalPath;

	private JButton buttonOK;
	private JButton buttonCancel;
	
	private String webURL;
	private String webFile;
	private String localPath;
	
	public JDialogFileSaveAs() {
		initializeComponents();
	}
	
	class WorkThead extends Thread {

		@Override
		public void run() {
			try {
				webHDFS.getFile(getWebFilePath(), localPath);
			} finally {
			}
		}
	}
	
	private String getWebFilePath() {
		String webFilePath = this.webURL + this.webFile;
		return webFilePath;
	}
	
	public void initializeComponents() {
		this.setSize(600, 150);
		this.setLocation(400, 300);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.labelServerURL = new JLabel("服务器地址:");
		this.textServerURL = new JTextField("Web URL");
		this.textServerURL.setEditable(false);
		this.buttonBrowser = new JButton("浏览");
		
		this.labelLocalPath = new JLabel("本地路径:");
		this.textLocalPath = new JTextField("Local Path");
		
		this.buttonOK = new SmButton("下载");
		this.buttonCancel = new SmButton(CommonProperties.getString("String_Button_Cancel"));
		
		this.buttonBrowser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonBrowserActionPerformed();
			}
		});
		
		this.buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonOKActionPerformed();
			}
		});
		
		this.buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonCancelActionPerformed();
			}
			
		});
		
		GroupLayout gLayout = new GroupLayout(this.getContentPane());
		gLayout.setAutoCreateContainerGaps(true);
		gLayout.setAutoCreateGaps(true);
		this.getContentPane().setLayout(gLayout);	
		
		// @formatter:off
		gLayout.setHorizontalGroup(gLayout.createParallelGroup(Alignment.CENTER)
				.addGroup(gLayout.createSequentialGroup()
						.addGroup(gLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.labelServerURL)
								.addComponent(this.labelLocalPath))
						.addGroup(gLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.textServerURL)
								.addGroup(gLayout.createSequentialGroup()
										.addComponent(this.textLocalPath, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
										.addComponent(this.buttonBrowser, 30, 30, 30))
								))
				.addGroup(gLayout.createSequentialGroup()
						.addGap(10, 10, Short.MAX_VALUE)
						.addComponent(this.buttonOK, 75, 75, 75)
						.addComponent(this.buttonCancel, 75, 75, 75)));
		gLayout.setVerticalGroup(gLayout.createSequentialGroup()
				.addGroup(gLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelServerURL)
						.addComponent(this.textServerURL, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelLocalPath)
						.addComponent(this.textLocalPath, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.buttonBrowser, 23, 23, 23))
				.addGroup(gLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		// @formatter:on
	}

	public String getWebURL() {
		return this.webURL;
	}
	
	public void setWebURL(String webURL) {
		this.webURL = webURL;
		if (this.textServerURL != null) {
			this.textServerURL.setText(this.getWebFilePath());
		}
	}
	
	public String getWebFile() {
		return this.webFile;
	}
	
	public void setWebFile(String webFile) {
		this.webFile = webFile;
		if (this.textServerURL != null) {
			this.textServerURL.setText(this.getWebFilePath());
		}
	}
	
	public String getLocalPath() {
		return this.localPath;
	}
	
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
		if (this.textLocalPath != null) {
			this.textLocalPath.setText(this.localPath);
		}
	}
	
	private void buttonBrowserActionPerformed() {
		try {	
			String moduleName = ControlsProperties.getString("String_SmFileChooseName_WorkpaceSaveAsFile");
			if (!SmFileChoose.isModuleExist(moduleName)) {
				String fileFilters = SmFileChoose.bulidFileFilters(
						SmFileChoose.createFileFilter(ControlsProperties.getString("String_WorkspaceSMWUFilterName"),
								ControlsProperties.getString("String_WorkspaceSMWUFilters")),
						SmFileChoose.createFileFilter(ControlsProperties.getString("String_WorkspaceSXWUFilterName"),
								ControlsProperties.getString("String_WorkspaceSXWUFilters")));
				SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
						ControlsProperties.getString("String_Title_WorkSpaceSaveAs"), moduleName, "SaveOne");
			}
			SmFileChoose smFileChoose = new SmFileChoose(moduleName);

			int state = smFileChoose.showDefaultDialog();
			if (state == JFileChooser.APPROVE_OPTION) {
				this.textLocalPath.setText(smFileChoose.getFilePath());
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			CursorUtilties.setDefaultCursor();
		}
	}
	
	/**
	 * 确定按钮点击事件
	 */
	private void buttonOKActionPerformed() {
		try {
			this.localPath = this.textLocalPath.getText();			
			WorkThead thread = new WorkThead();
			thread.start();
			
//			this.dispose();
//			this.dialogResult = DialogResult.OK;
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			CursorUtilties.setDefaultCursor();
		}
	}

	/**
	 * 关闭按钮点击事件
	 */
	private void buttonCancelActionPerformed() {
		this.dispose();
		this.dialogResult = DialogResult.CANCEL;
	}
}
