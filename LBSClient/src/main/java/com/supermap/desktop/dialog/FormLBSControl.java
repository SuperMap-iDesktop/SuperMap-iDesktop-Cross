package com.supermap.desktop.dialog;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IContextMenuManager;
import com.supermap.desktop.Interface.IFormLBSControl;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.event.FormClosingEvent;
import com.supermap.desktop.http.DeleteFile;
import com.supermap.desktop.http.upload.LocalCreateFile;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.ui.FormBaseChild;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.lbs.impl.HDFSDefine;
import com.supermap.desktop.ui.lbs.impl.HDFSTableModel;
import com.supermap.desktop.ui.lbs.impl.WebHDFS;
import com.supermap.desktop.ui.lbs.ui.JPanelHDFSFiles;
import com.supermap.desktop.utilities.CursorUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.SystemPropertyUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.MessageFormat;

public class FormLBSControl extends FormBaseChild implements IFormLBSControl {

    private JPopupMenu contextPopuMenu;
    private JPanelHDFSFiles panelHDFSFiles;
    private JTable table;
    private Boolean isOutputFolder = false;

    private MouseAdapter tableMouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            tableMouseClicked(e);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ToolbarUIUtilities.updataToolbarsState();
                }
            });
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }
    };

    private KeyListener tableKeyListener = new KeyAdapter() {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_F2) {
                if (getSelectRow() > -1) {
                    String oldName = (String) getTable().getValueAt(getSelectRow(), 0);
                    RenameDialog dialog = new RenameDialog(oldName);
                    if (dialog.showDialog().equals(DialogResult.OK) && !StringUtilities.isNullOrEmpty(dialog.getNewName())) {
                        LocalCreateFile createFile = new LocalCreateFile();
                        HDFSDefine define = (HDFSDefine) ((HDFSTableModel) getTable().getModel()).getRowTagAt(getTable().getSelectedRow());
                        createFile.renameFile(getURL(), oldName, dialog.getNewName(), define.isDir());
                    }
                }
            }
        }
    };

    public FormLBSControl(String title, Icon icon, Component component) {
        super(title, icon, component);
        panelHDFSFiles = new JPanelHDFSFiles();
        this.add(panelHDFSFiles, BorderLayout.CENTER);
        initializeComponents();
        registEvents();
        setText("FormHDFSManager");
    }

    public FormLBSControl(String title) {
        this(title, null, null);
    }

    private void registEvents() {
        this.table = panelHDFSFiles.getTable();
        removeEvents();
        this.table.addMouseListener(this.tableMouseListener);
        this.table.addKeyListener(this.tableKeyListener);
    }

    private void removeEvents() {
        this.table.removeMouseListener(this.tableMouseListener);
        this.table.removeKeyListener(this.tableKeyListener);
    }

    public void initializeComponents() {
        this.setSize(900, 600);
        if (Application.getActiveApplication().getMainFrame() != null) {
            IContextMenuManager manager = Application.getActiveApplication().getMainFrame().getContextMenuManager();
            this.contextPopuMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LBSControlManager.ContextMenuLBSControl");
        }
    }


    public void tableMouseClicked(MouseEvent e) {
        if (table.getSelectedRow() != -1 && e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
            panelHDFSFiles.resetHDFSPath();
        }else if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3) {
            showContextMenu(e);
        }
    }

    private void showContextMenu(MouseEvent e) {
        this.contextPopuMenu.show(table, e.getPoint().x, e.getPoint().y);
    }


    public Boolean getIsOutputFolder() {
        return isOutputFolder;
    }

    @Override
    public void formClosing(FormClosingEvent e) {
        removeEvents();
    }


    @Override
    public int getSelectRow() {
        return table.getSelectedRow();
    }

    @Override
    public int[] getSelectRows() {
        return table.getSelectedRows();
    }

    @Override
    public void delete() {
        // 从hdfs文件系统中删除文件
        try {
            String webFile = "";
            String webURL = panelHDFSFiles.getURL();
            if (table.getSelectedRowCount() == 1) {
                HDFSDefine define = (HDFSDefine) ((HDFSTableModel) this.table.getModel()).getRowTagAt(table.getSelectedRow());
                if (define != null && !define.isDir()) {
                    webFile = define.getName();
                    if (UICommonToolkit.showConfirmDialog(MessageFormat.format(LBSClientProperties.getString("String_DeleteFile"), webFile)) == JOptionPane.OK_OPTION) {
                        String nowUrl = addSeparator(webURL) + define.getName();
                        DeleteFile deleteFile = new DeleteFile(nowUrl, webFile, false);
                        deleteFile.delete();
                    }
                } else if (define != null && define.isDir()) {
                    webFile = define.getName();
                    if (UICommonToolkit.showConfirmDialog(MessageFormat.format(LBSClientProperties.getString("String_DeleteDir"), webFile)) == JOptionPane.OK_OPTION) {
                        String nowUrl = addSeparator(webURL) + define.getName();
                        deleteDir(nowUrl, define);
                    }
                }
            } else if (table.getSelectedRowCount() > 1) {
                int[] indexs = table.getSelectedRows();
                if (UICommonToolkit.showConfirmDialog(MessageFormat.format(LBSClientProperties.getString("String_DeleteFiles"), indexs.length)) == JOptionPane.OK_OPTION) {
                    // 全是文件
                    for (int index : indexs) {
                        HDFSDefine define = (HDFSDefine) ((HDFSTableModel) this.table.getModel()).getRowTagAt(index);
                        if (define != null && !define.isDir()) {
                            String nowUrl = addSeparator(webURL) + define.getName();
                            DeleteFile deleteFile = new DeleteFile(nowUrl, define.getName(), false);
                            deleteFile.delete();
                        } else if (define != null && define.isDir()) {
                            String nowUrl = addSeparator(webURL) + define.getName();
                            deleteDir(nowUrl, define);
                        }
                    }
                }
            }

        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        } finally {
            refresh();
            CursorUtilities.setDefaultCursor();
        }
    }


    /**
     * 删除文件夹
     *
     * @param
     * @param
     * @throws IOException
     */
    private void deleteDir(String webURL, HDFSDefine define) throws IOException {
        HDFSDefine[] nowDefines = WebHDFS.listDirectory(webURL, "", getIsOutputFolder());
        if (!define.isDir()) {
            DeleteFile deleteFile = new DeleteFile(webURL, define.getName(), false);
            deleteFile.delete();
        } else if (nowDefines.length == 0) {
            DeleteFile deleteFile = new DeleteFile(webURL, define.getName(), true);
            deleteFile.delete();
        } else {
            HDFSDefine[] defines = WebHDFS.listDirectory(webURL, "", getIsOutputFolder());
            for (HDFSDefine tempDefine : defines) {
                String tempUrl = addSeparator(webURL) + tempDefine.getName();
                deleteDir(tempUrl, tempDefine);
            }
            DeleteFile deleteFile = new DeleteFile(webURL, define.getName(), true);
            deleteFile.delete();
        }
    }

    private String addSeparator(String url) {
        String result = url;
        if (!result.endsWith("/")) {
            result += "/";
        }
        return result;
    }

    @Override
    public void download() {
        // 下载文件
        try {
            Boolean fileSelected = false;
            if (table.getSelectedRow() != -1 && table.getRowCount() > 0) {
                HDFSDefine define = (HDFSDefine) ((HDFSTableModel) this.table.getModel()).getRowTagAt(table.getSelectedRow());
                if (define != null && !define.isDir()) {
                    fileSelected = true;
                    // show save file dialog
                    JDialogFileSaveAs dialogFileSaveAs = new JDialogFileSaveAs();
                    dialogFileSaveAs.setWebURL(panelHDFSFiles.getURL());
                    dialogFileSaveAs.setWebFile(define.getName());
                    dialogFileSaveAs.setFileSize(Long.parseLong(define.getSize()));
                    String filePath = "";
                    if (SystemPropertyUtilities.isWindows()) {
                        filePath = "F:/temp/";
                    } else {
                        filePath = "/opt";
                    }
                    dialogFileSaveAs.setLocalPath(filePath);
                    dialogFileSaveAs.setFileName(define.getName());
                    dialogFileSaveAs.showDialog();
                }
            }

            if (!fileSelected) {
                UICommonToolkit.showMessageDialog(LBSClientProperties.getString("String_DeleteWarning"));
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        } finally {
            CursorUtilities.setDefaultCursor();
        }
    }

    @Override
    public String getURL() {
        return panelHDFSFiles.getURL();
    }

    @Override
    public JTable getTable() {
        return table;
    }

    @Override
    public void refresh() {
        panelHDFSFiles.refresh();
    }
}
