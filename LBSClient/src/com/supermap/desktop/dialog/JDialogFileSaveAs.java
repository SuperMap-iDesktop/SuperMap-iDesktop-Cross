package com.supermap.desktop.dialog;

import java.awt.event.*;
import java.io.File;
import java.text.MessageFormat;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

import com.supermap.Interface.ITask;
import com.supermap.Interface.ITaskFactory;
import com.supermap.Interface.TaskEnum;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.http.*;
import com.supermap.desktop.http.callable.DownloadProgressCallable;
import com.supermap.desktop.http.download.FileInfo;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.task.TaskFactory;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.*;

/**
 * 文件下载对话框
 * 
 * @author
 *
 */
public class JDialogFileSaveAs extends SmDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel labelServerURL;
	private JTextField textServerURL;
	private JButton buttonBrowser;

	private JLabel labelLocalPath;
	private JTextField textLocalPath;

	private JButton buttonOK;
	private JButton buttonCancel;

	private String fileName = "";

	private String webURL;
	private String webFile;
	private String localPath;
	private long fileSize;
	private ActionListener buttonBrowserListener;
	private ActionListener buttonOKListener;
	private ActionListener buttonCancelListener;

	public JDialogFileSaveAs() {
		initializeComponents();
		initializeResources();
		registEvnets();
	}

	private void initializeResources() {
		this.labelServerURL.setText(LBSClientProperties.getString("String_URL"));
		this.buttonBrowser.setText(LBSClientProperties.getString("String_Scale"));
		this.labelLocalPath.setText(LBSClientProperties.getString("String_LocalPath"));
		this.buttonOK.setText(LBSClientProperties.getString("String_Download"));
	}

	private void registEvnets() {
		this.buttonBrowserListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonBrowserActionPerformed();
			}
		};
		this.buttonOKListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonOKActionPerformed();
			}
		};
		this.buttonCancelListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonCancelActionPerformed();
			}

		};
		removeEvents();
		this.buttonBrowser.addActionListener(this.buttonBrowserListener);
		this.buttonOK.addActionListener(buttonOKListener);
		this.buttonCancel.addActionListener(buttonCancelListener);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				removeEvents();
			}

		});
	}

	private void removeEvents() {
		this.buttonBrowser.removeActionListener(this.buttonBrowserListener);
		this.buttonOK.removeActionListener(buttonOKListener);
		this.buttonCancel.removeActionListener(buttonCancelListener);
	}

	private String getWebFilePath() {
		String webFilePath = this.webURL + this.webFile;
		return webFilePath;
	}

	public void initializeComponents() {
		this.setSize(600, 150);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		this.labelServerURL = new JLabel("url");
		this.textServerURL = new JTextField("Web URL");
		this.textServerURL.setEditable(false);
		this.buttonBrowser = new JButton("");

		this.labelLocalPath = new JLabel("");
		this.textLocalPath = new JTextField("Local Path");

		this.buttonOK = new SmButton("");
		this.buttonCancel = ComponentFactory.createButtonCancel();
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
			this.textLocalPath.setText(this.localPath + fileName);
		}
	}

	private void buttonBrowserActionPerformed() {
		try {
			String modelName = "HDFSFileDownload";
			if (!SmFileChoose.isModuleExist(modelName)) {
				SmFileChoose.addNewNode("", CommonProperties.getString("String_DefaultFilePath"), LBSClientProperties.getString("String_ChooseFile"),
						modelName, "GetDirectories");
			}
			SmFileChoose smFileChoose = new SmFileChoose(modelName);
			smFileChoose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int state = smFileChoose.showDefaultDialog();
			if (state == JFileChooser.APPROVE_OPTION) {
				this.textLocalPath.setText(smFileChoose.getFilePath() + File.separator + fileName);
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

			FileManagerContainer fileManagerContainer = CommonUtilities.getFileManagerContainer();
			if (fileManagerContainer != null) {
				File directory = new File(localPath);
				if (!directory.exists()
						&& UICommonToolkit.showConfirmDialog(MessageFormat.format(LBSClientProperties.getString("String_isMakeDirectory"), localPath)) == JOptionPane.OK_OPTION) {
					directory.mkdirs();
				}
				File file = new File(this.textLocalPath.getText());
				FileInfo downloadInfo = new FileInfo(getWebFilePath(), file.getName(), file.getParentFile().getPath(), this.getFileSize(), 1, true);
				ITaskFactory taskFactory = TaskFactory.getInstance();
				ITask task = taskFactory.getTask(TaskEnum.DOWNLOADTASK, downloadInfo);
				DownloadProgressCallable downloadProgressCallable = new DownloadProgressCallable(downloadInfo,true);
				task.doWork(downloadProgressCallable);
				fileManagerContainer.addItem(task);
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
		removeEvents();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
