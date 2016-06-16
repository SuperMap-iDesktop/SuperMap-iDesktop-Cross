package com.supermap.desktop.dialog;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.http.DownloadInfo;
import com.supermap.desktop.http.DownloadProgressCallable;
import com.supermap.desktop.http.DownloadUtils;
import com.supermap.desktop.http.FileManager;
import com.supermap.desktop.http.FileManagerContainer;
import com.supermap.desktop.http.HttpRequest;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.progress.FormProgress;
import com.supermap.desktop.utilities.CursorUtilities;

public class JDialogFileSaveAs extends SmDialog {

//	public static void main(String[] args) {
//		JDialogFileSaveAs f = new JDialogFileSaveAs();
//		f.showDialog();
//	}
	
	public static final String FILE_MANAGER_CONTROL_CLASS = "com.supermap.desktop.http.FileManagerContainer";

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
	private long fileSize;
	
	public JDialogFileSaveAs() {
		initializeComponents();
	}
	
	private String getWebFilePath() {
		String webFilePath = this.webURL + this.webFile;
		return webFilePath;
	}
	
	public void initializeComponents() {
		this.setSize(600, 150);
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
										.addComponent(this.buttonBrowser, 30, 30, 30))))
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

		this.setLocationRelativeTo(null);
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
	
	public long getFileSize() {
		return this.fileSize;
	}
	
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
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
			String modelName = "HDFSFileDownload";
			if (!SmFileChoose.isModuleExist(modelName)) {
				SmFileChoose.addNewNode("", CommonProperties.getString("String_DefaultFilePath"),
						"选择文件", modelName, "SaveOne");
			}
			SmFileChoose smFileChoose = new SmFileChoose(this.webFile);
			smFileChoose.setAcceptAllFileFilterUsed(true);

			int state = smFileChoose.showDefaultDialog();
			if (state == JFileChooser.APPROVE_OPTION) {
				this.textLocalPath.setText(smFileChoose.getFilePath());
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			CursorUtilities.setDefaultCursor();
		}
	}

	/**
	 * 确定按钮点击事件
	 */
	private void buttonOKActionPerformed() {
		try {
//			this.localPath = this.textLocalPath.getText();				
//			webHDFS.getFile(getWebFilePath(), localPath);		
			
			IDockbar dockbarPropertyContainer = Application.getActiveApplication().getMainFrame().getDockbarManager()
					.get(Class.forName(FILE_MANAGER_CONTROL_CLASS));
			
			
			FileManagerContainer fileManagerContainer  = null;			
			if (dockbarPropertyContainer != null) {
				fileManagerContainer = (FileManagerContainer) dockbarPropertyContainer.getComponent();
				dockbarPropertyContainer.setVisible(true);
				dockbarPropertyContainer.active();
			}
			
			if (fileManagerContainer != null) {
				File file = new File(this.textLocalPath.getText());	
				DownloadInfo downloadInfo = new DownloadInfo(getWebFilePath(), file.getName(),  file.getParentFile().getPath(), this.getFileSize(), 1, true);
				
				FileManager fileManager = new FileManager(downloadInfo);
				DownloadProgressCallable downloadProgressCallable = new DownloadProgressCallable(downloadInfo);
				fileManager.doWork(downloadProgressCallable);
				
				fileManagerContainer.addItem(fileManager);
			}
						
			this.dispose();
			this.dialogResult = DialogResult.OK;
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			CursorUtilities.setDefaultCursor();
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
