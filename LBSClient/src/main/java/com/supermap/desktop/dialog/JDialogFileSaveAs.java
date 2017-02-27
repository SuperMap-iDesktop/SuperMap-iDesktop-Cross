package com.supermap.desktop.dialog;

import com.supermap.Interface.ILBSTask;
import com.supermap.Interface.ITaskFactory;
import com.supermap.Interface.TaskEnum;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.http.FileManagerContainer;
import com.supermap.desktop.http.callable.DownloadProgressCallable;
import com.supermap.desktop.ui.lbs.impl.FileInfo;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.task.TaskFactory;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.CommonUtilities;
import com.supermap.desktop.utilities.CursorUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * 文件下载对话框
 *
 * @author
 */
public class JDialogFileSaveAs extends SmDialog {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JLabel labelServerURL;
    private JTextField textServerURL;
    private JButton buttonBrowser;
    private JLabel labelFileName;
    private JTextField textFieldFileName;

    private JLabel labelLocalPath;
    private JTextField textLocalPath;

    private JButton buttonOK;
    private JButton buttonCancel;

    private String fileName = "";
    private String realName = "";

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
        this.labelFileName.setText(LBSClientProperties.getString("String_FileName"));
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
        String webFilePath = this.webURL;
        return webFilePath;
    }

    public void initializeComponents() {
        this.setSize(600, 180);
        this.setTitle(LBSClientProperties.getString("String_Download"));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.labelServerURL = new JLabel("url");
        this.textServerURL = new JTextField("Web URL");
        this.textServerURL.setEditable(false);
        this.buttonBrowser = new JButton("");

        this.labelLocalPath = new JLabel("");
        this.textLocalPath = new JTextField("Local Path");
        this.labelFileName = new JLabel();
        this.textFieldFileName = new JTextField();

        this.buttonOK = new SmButton("");
        this.buttonCancel = ComponentFactory.createButtonCancel();

        // @formatter:off
        getContentPane().setLayout(new GridBagLayout());
        JPanel panelButton = new JPanel();
        panelButton.setLayout(new GridBagLayout());
        panelButton.add(this.buttonOK, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(10, 0, 10, 10));
        panelButton.add(this.buttonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(10, 0, 10, 10));
        getContentPane().add(this.labelServerURL, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 0, 0).setWeight(0, 1));
        getContentPane().add(this.textServerURL, new GridBagConstraintsHelper(1, 0, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 10, 0, 10).setWeight(1, 1));
        getContentPane().add(this.labelFileName, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 0, 0).setWeight(0, 1));
        getContentPane().add(this.textFieldFileName, new GridBagConstraintsHelper(1, 1, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 10, 0, 10).setWeight(1, 1));
        getContentPane().add(this.labelLocalPath, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 0, 0).setWeight(0, 1));
        getContentPane().add(this.textLocalPath, new GridBagConstraintsHelper(1, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 10, 0, 0).setWeight(1, 1));
        getContentPane().add(this.buttonBrowser, new GridBagConstraintsHelper(3, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 5, 0, 10).setWeight(0, 1));
        getContentPane().add(panelButton, new GridBagConstraintsHelper(0, 3, 4, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0));
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
                SmFileChoose.addNewNode("", CommonProperties.getString("String_DefaultFilePath"), LBSClientProperties.getString("String_ChooseFile"),
                        modelName, "GetDirectories");
            }
            SmFileChoose smFileChoose = new SmFileChoose(modelName);
            smFileChoose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int state = smFileChoose.showSaveDialog(null);
            if (state == JFileChooser.APPROVE_OPTION) {
                String path = smFileChoose.getFilePath();
                if (!smFileChoose.getFilePath().endsWith(File.separator)) {
                    path += File.separator;
                }
                this.textLocalPath.setText(path);
                localPath = path;
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
                if (!localPath.endsWith(File.separator)) {
                    localPath += File.separator;
                }
                File directory = new File(localPath);
                directory.mkdirs();
                File file = new File(localPath + fileName);
                FileInfo downloadInfo = new FileInfo(getWebFilePath(), file.getName(), realName, file.getParentFile().getPath(), this.getFileSize(), 1, true);
                ITaskFactory taskFactory = TaskFactory.getInstance();
                ILBSTask task = taskFactory.getTask(TaskEnum.DOWNLOADTASK, downloadInfo);
                DownloadProgressCallable downloadProgressCallable = new DownloadProgressCallable(downloadInfo, true);
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

    public void setFileName(String fileName) {
        this.fileName = fileName;
        if (!this.localPath.endsWith("/")) {
            this.localPath += "/";
        }
        String file = this.localPath + fileName;
        int count = 0;
        String name = "";
        String fileType = "";
        if (fileName.contains(".")) {
            name = fileName.substring(0, fileName.lastIndexOf("."));
            fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        } else {
            name = fileName;
            fileType = "";
        }
        this.realName = name + fileType;
        while (true) {
            if (new File(file).exists()) {
                ++count;
                this.realName = name + "(" + count + ")" + fileType;
                file = this.localPath + realName;
            } else {
                break;
            }
        }
        this.textFieldFileName.setText(this.realName);
    }

}
