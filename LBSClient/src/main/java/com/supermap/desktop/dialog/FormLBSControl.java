package com.supermap.desktop.dialog;

import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.WebHDFS;
import com.supermap.desktop.CtrlAction.WebHDFS.HDFSDefine;
import com.supermap.desktop.Interface.IContextMenuManager;
import com.supermap.desktop.Interface.IFormLBSControl;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.event.FormClosingEvent;
import com.supermap.desktop.http.CreateFile;
import com.supermap.desktop.http.DeleteFile;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.FormBaseChild;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilities.CommonUtilities;
import com.supermap.desktop.utilities.CursorUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.SystemPropertyUtilities;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;

public class FormLBSControl extends FormBaseChild implements IFormLBSControl {

    /**
     *
     */
    // private static final int COLUMN_INDEX_Permission = 5;
    // private static final int COLUMN_INDEX_Owner = 3;
    // private static final int COLUMN_INDEX_Group = 4;
    // private static final int COLUMN_INDEX_Size = 1;
    // private static final int COLUMN_INDEX_Replication = 6;
    // private static final int COLUMN_INDEX_BlockSize = 2;
    private static final int COLUMN_INDEX_Name = 0;
    private static final int ROW_HEADER_WIDTH = 70;

    private JPopupMenu contextPopuMenu;
    private JLabel labelServerURL;
    private JTextField textServerURL;
    private JButton buttonForward;
    private JButton buttonRefresh;
    private JButton buttonBack;
    private JList rowHeader;
    private ArrayList<String> urlList;
    private int urlPathIndex = 0;
    private JScrollPane scrollPaneFormLBSControl;
    private JTable table;
    private Boolean isOutputFolder = false;
    private KeyListener textServerURLKeyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                refresh();
            }
        }
    };
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
            tableMouseReleased(e);
        }
    };
    private ActionListener refreshListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            refresh();
        }
    };
    private ActionListener urlActionListener;
    private KeyListener tableKeyListener = new KeyAdapter() {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_F2) {
                if (getSelectRow() > -1) {
                    String oldName = (String) getTable().getValueAt(getSelectRow(), 0);
                    RenameDialog dialog = new RenameDialog(oldName);
                    if (dialog.showDialog().equals(DialogResult.OK) && !StringUtilities.isNullOrEmpty(dialog.getNewName())) {
                        CreateFile createFile = new CreateFile();
                        HDFSDefine define = (HDFSDefine) ((HDFSTableModel) getTable().getModel()).getRowTagAt(getTable().getSelectedRow());
                        createFile.renameFile(getURL(), oldName, dialog.getNewName(), define.isDir());
                    }
                }
            }
        }
    };

    public FormLBSControl(String title, Icon icon, Component component) {
        super(title, icon, component);
        initializeComponents();
        initializeResources();
        registEvents();
        setText("FormHDFSManager");
    }

    private void initializeResources() {
        this.buttonBack.setIcon(CommonUtilities.getImageIcon("back.png"));
        this.buttonForward.setIcon(CommonUtilities.getImageIcon("forward.png"));
        this.buttonRefresh.setIcon(CommonUtilities.getImageIcon("scale.png"));
        this.buttonBack.setToolTipText(ControlsProperties.getString("String_Back"));
        this.buttonForward.setToolTipText(ControlsProperties.getString("String_Forward"));
    }

    public FormLBSControl(String title) {
        this(title, null, null);
    }

    public FormLBSControl() {
        this("");
    }

    private void registEvents() {
        this.urlActionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == buttonBack) {
                    if (urlPathIndex > 0) {
                        urlPathIndex--;
                        if (null != urlList.get(urlPathIndex)) {
                            listDirectory(urlList.get(urlPathIndex), "", getIsOutputFolder());
                            textServerURL.setText(urlList.get(urlPathIndex));
                            buttonForward.setEnabled(true);
                            if (urlPathIndex <= 0) {
                                buttonBack.setEnabled(false);
                            }
                        }
                    }
                } else {
                    if (urlPathIndex < urlList.size() - 1) {
                        urlPathIndex++;
                        if (null != urlList.get(urlPathIndex)) {
                            listDirectory(urlList.get(urlPathIndex), "", getIsOutputFolder());
                            textServerURL.setText(urlList.get(urlPathIndex));
                            buttonBack.setEnabled(true);
                            if (urlPathIndex >= urlList.size() - 1) {
                                buttonForward.setEnabled(false);
                            }
                        }
                    }
                }
            }
        };
        removeEvents();
        this.textServerURL.addKeyListener(this.textServerURLKeyListener);
        this.buttonBack.addActionListener(this.urlActionListener);
        this.buttonForward.addActionListener(this.urlActionListener);
        this.buttonRefresh.addActionListener(this.refreshListener);
        this.table.addMouseListener(this.tableMouseListener);
        this.table.addKeyListener(this.tableKeyListener);
    }

    private void removeEvents() {
        this.textServerURL.removeKeyListener(this.textServerURLKeyListener);
        this.buttonBack.removeActionListener(this.urlActionListener);
        this.buttonForward.removeActionListener(this.urlActionListener);
        this.buttonRefresh.removeActionListener(this.refreshListener);
        this.table.removeMouseListener(this.tableMouseListener);
        this.table.removeKeyListener(this.tableKeyListener);
    }

    public void initializeComponents() {
        this.setSize(900, 600);
        this.scrollPaneFormLBSControl = new JScrollPane();
        this.labelServerURL = new JLabel();
        this.textServerURL = new JTextField(WebHDFS.webURL);
        this.urlList = new ArrayList<>();
        urlList.add(WebHDFS.webURL);
        this.table = new JTable();
        this.buttonRefresh = new JButton();
        this.buttonForward = new JButton();
        this.buttonBack = new JButton();
        this.buttonBack.setEnabled(false);
        this.buttonForward.setEnabled(false);
        HDFSTableModel tableModel = new HDFSTableModel();
        this.table.setModel(tableModel);
        this.table.putClientProperty("terminateEditOnFocusLost", true);
        this.table.setRowHeight(23);
        ListModel<?> listModel = new LeftTableHeaderListModel(table);
        this.rowHeader = new JList(listModel);
        this.rowHeader.setFixedCellWidth(ROW_HEADER_WIDTH);
        this.rowHeader.setFixedCellHeight(table.getRowHeight());
        this.rowHeader.setCellRenderer(new RowHeaderRenderer(table));
        this.scrollPaneFormLBSControl.setRowHeaderView(rowHeader);
        JLabel scrollPaneUpperLeftLabel=new JLabel(CommonProperties.getString("String_ColumnHeader_Index"),SwingConstants.CENTER);
        scrollPaneFormLBSControl.setCorner(JScrollPane.UPPER_LEFT_CORNER,scrollPaneUpperLeftLabel);
        if (Application.getActiveApplication().getMainFrame() != null) {
            IContextMenuManager manager = Application.getActiveApplication().getMainFrame().getContextMenuManager();
            this.contextPopuMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LBSControlManager.ContextMenuLBSControl");
        }
        initializeLayout();
        listDirectory(this.textServerURL.getText(), "", this.getIsOutputFolder());
        if (0 < table.getRowCount()) {
            table.setRowSelectionInterval(0, 0);
        }
    }

    private void initializeLayout() {
        GroupLayout gLayout = new GroupLayout(this);
        gLayout.setAutoCreateContainerGaps(true);
        gLayout.setAutoCreateGaps(true);
        // @formatter:off
        gLayout.setHorizontalGroup(gLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(gLayout.createSequentialGroup().addComponent(this.labelServerURL)
                        .addComponent(this.buttonBack, 60, 60, 60)
                        .addComponent(this.buttonForward, 60, 60, 60)
                        .addComponent(this.textServerURL, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(this.buttonRefresh, 60, 60, 60))
                .addComponent(scrollPaneFormLBSControl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        gLayout.setVerticalGroup(gLayout.createSequentialGroup()
                .addGroup(gLayout.createParallelGroup(Alignment.CENTER).addComponent(this.labelServerURL)
                        .addComponent(this.buttonBack, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(this.buttonForward, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(this.textServerURL, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(this.buttonRefresh, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addComponent(scrollPaneFormLBSControl, 100, 200, Short.MAX_VALUE));
        scrollPaneFormLBSControl.setViewportView(table);
        this.setLayout(gLayout);
        // @formatter:on
    }

    /**
     * 添加一行记录
     *
     * @param hdfsDefine
     */
    private void addFileInfo(WebHDFS.HDFSDefine hdfsDefine) {
        try {
            HDFSTableModel tableModel = (HDFSTableModel) this.table.getModel();
            tableModel.addRow(hdfsDefine);
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
    }

    public void tableMouseClicked(MouseEvent e) {
        //
        if (table.getSelectedRow() != -1 && e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
            HDFSDefine define = (HDFSDefine) ((HDFSTableModel) this.table.getModel()).getRowTagAt(table.getSelectedRow());
            if (define != null) {
                // if mouse double click foler, list folder files
                if (define.isDir()) {
                    String name = (String) this.table.getModel().getValueAt(table.getSelectedRow(), COLUMN_INDEX_Name);
                    String root = this.textServerURL.getText();
                    if (!root.endsWith("/")) {
                        root += "/";
                    }
                    String url = this.listDirectory(root, name, this.getIsOutputFolder());
                    this.textServerURL.setText(url);
                    urlList.add(url);
                    // urlPathIndex始终为最新加入的url对应的索引号
                    urlPathIndex = urlList.size() - 1;
                    this.buttonBack.setEnabled(true);
                } else {
                    this.buttonOKActionPerformed();
                }
            }
        } else if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3) {
            showContextMenu(e);
        }
    }

    private void showContextMenu(MouseEvent e) {
        this.contextPopuMenu.show(table, e.getPoint().x, e.getPoint().y);
    }

    public void tableMouseReleased(MouseEvent e) {

    }

    private String listDirectory(String urlPath, String childFolder, Boolean isFolderOnly) {
        // 删除后设置第0行被选中
        if (0 < table.getRowCount()) {
            HDFSTableModel tableModel = (HDFSTableModel) table.getModel();
            tableModel.removeRows(0, tableModel.getRowCount());
            table.updateUI();
        }

        if (!"".equals(childFolder)) {
            if (!childFolder.endsWith("/")) {
                childFolder += "/";
            }

            if (childFolder.startsWith("/")) {
                childFolder.substring(1, childFolder.length() - 1);
            }

            urlPath += childFolder;
        }

        WebHDFS.HDFSDefine[] defines = WebHDFS.listDirectory(urlPath, "", isFolderOnly);
        for (WebHDFS.HDFSDefine define : defines) {
            this.addFileInfo(define);
        }
        this.table.updateUI();
        return urlPath;
    }

    /**
     * 刷新按钮点击事件
     */
    public void refresh() {
        try {
            if (!urlList.get(urlList.size() - 1).equals(this.textServerURL.getText())) {
                urlList.add(this.textServerURL.getText());
                urlPathIndex = urlList.size() - 1;
            }
            listDirectory(this.textServerURL.getText(), "", this.getIsOutputFolder());
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
            if (this.textServerURL.isFocusOwner()) {
                return;
            }
            Boolean fileSelected = false;
            if (table.getSelectedRow() != -1) {
                HDFSDefine define = (HDFSDefine) ((HDFSTableModel) this.table.getModel()).getRowTagAt(table.getSelectedRow());
                if (define != null) {
                    String root = this.textServerURL.getText();
                    if (!root.endsWith("/")) {
                        root += "/";
                    }

                    if (define.isDir()) {
                        if (this.getIsOutputFolder()) {
                            WebHDFS.outputURL = root + define.getName() + "/";
                        } else {
                            WebHDFS.webFile = "";
                            WebHDFS.webURL = root + define.getName() + "/";
                        }
                    } else {
                        WebHDFS.webURL = this.textServerURL.getText();
                        if (define.getName().endsWith(".idx")) {
                            WebHDFS.webFile = "";
                        }
                    }
                    this.textServerURL.setText(root + define.getName());
                    fileSelected = true;
                }
            }

            if (!fileSelected) {
                UICommonToolkit.showMessageDialog("please select a file");
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        } finally {
            CursorUtilities.setDefaultCursor();
        }
    }

    public Boolean getIsOutputFolder() {
        return isOutputFolder;
    }

    public void setIsOutputFolder(Boolean isOutputFolder) {
        this.isOutputFolder = isOutputFolder;
    }

    @Override
    public void formClosing(FormClosingEvent e) {
        removeEvents();
        urlList.clear();
    }

    /**
     * describe a HDFS file DataModel
     *
     * @author
     */

    class LeftTableHeaderListModel extends AbstractListModel {
        private static final long serialVersionUID = 1L;

        JTable table;

        public LeftTableHeaderListModel(JTable table) {
            super();
            this.table = table;
        }

        @Override
        public int getSize() {
            return table.getRowCount();
        }

        @Override
        public Object getElementAt(int index) {
            return index;
        }
    }

    /**
     * 自定义JTable头部渲染器
     *
     * @author
     */
    public class RowHeaderRenderer extends JLabel implements ListCellRenderer {
        JTable table;

        RowHeaderRenderer(JTable table) {
            this.table = table;
            JTableHeader header = table.getTableHeader();
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            setForeground(header.getForeground());
            setBackground(header.getBackground());
            setFont(header.getFont());
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            HDFSDefine define = (HDFSDefine) ((HDFSTableModel) this.table.getModel()).getRowTagAt(index);
            this.setText(String.valueOf(index + 1));
            if (define.isDir()) {
                this.setToolTipText(LBSClientProperties.getString("String_Dir"));
                this.setIcon(ControlsResources.getIcon("/controlsresources/Image_DatasetGroup_Normal.png"));
            } else {
                this.setToolTipText(LBSClientProperties.getString("String_File"));
                this.setIcon(ControlsResources.getIcon("/controlsresources/file.png"));
            }
            this.setPreferredSize(new Dimension(100, 50));
            return this;
        }

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
    public String getURL() {
        return this.textServerURL.getText();
    }

    @Override
    public JTable getTable() {
        return this.table;
    }

    @Override
    public void delete() {
        // 从hdfs文件系统中删除文件
        try {
            String webFile = "";
            String webURL = this.textServerURL.getText();
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
        WebHDFS.HDFSDefine[] nowDefines = WebHDFS.listDirectory(webURL, "", getIsOutputFolder());
        if (!define.isDir()) {
            DeleteFile deleteFile = new DeleteFile(webURL, define.getName(), false);
            deleteFile.delete();
        } else if (nowDefines.length == 0) {
            DeleteFile deleteFile = new DeleteFile(webURL, define.getName(), true);
            deleteFile.delete();
        } else {
            WebHDFS.HDFSDefine[] defines = WebHDFS.listDirectory(webURL, "", getIsOutputFolder());
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
                    dialogFileSaveAs.setWebURL(this.textServerURL.getText());
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
}
