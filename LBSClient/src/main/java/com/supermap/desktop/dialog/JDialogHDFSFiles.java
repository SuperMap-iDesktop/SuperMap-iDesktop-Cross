package com.supermap.desktop.dialog;

import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.WebHDFS;
import com.supermap.desktop.CtrlAction.WebHDFS.HDFSDefine;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.event.FormClosingEvent;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.utilities.CommonUtilities;
import com.supermap.desktop.utilities.CursorUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * 下载,上传主界面
 *
 * @author
 */
public class JDialogHDFSFiles extends SmDialog {

    private static final int COLUMN_INDEX_Name = 0;
    private static final int ROW_HEADER_WIDTH = 70;

    private JLabel labelServerURL;
    private JTextField textServerURL;
    private JButton buttonForward;
    private JButton buttonRefresh;
    private JButton buttonBack;
    private JButton buttonOK;
    private JButton buttonCancel;
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

    };
    private ActionListener refreshListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            refresh();
        }
    };
    private ActionListener urlActionListener;
    private ActionListener cancelListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            removeAndDispose();
        }
    };

    private void removeAndDispose() {
        removeEvents();
        JDialogHDFSFiles.this.dispose();
    }

    private ActionListener okListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!StringUtilities.isNullOrEmpty(textServerURL.getText())) {
                String name = (String) table.getModel().getValueAt(table.getSelectedRow(), COLUMN_INDEX_Name);
                WebHDFS.webURL = textServerURL.getText() + name;
                dialogResult = DialogResult.OK;
                removeAndDispose();
            }
        }
    };

    public JDialogHDFSFiles() {
        initializeComponents();
        initializeResources();
        initializeLayout();
        registEvents();
        this.setTitle(LBSClientProperties.getString("String_Scale"));
        setLocationRelativeTo(null);
    }

    private void initializeResources() {
        this.buttonBack.setIcon(CommonUtilities.getImageIcon("back.png"));
        this.buttonForward.setIcon(CommonUtilities.getImageIcon("forward.png"));
        this.buttonRefresh.setIcon(CommonUtilities.getImageIcon("scale.png"));
        this.buttonBack.setToolTipText(ControlsProperties.getString("String_Back"));
        this.buttonForward.setToolTipText(ControlsProperties.getString("String_Forward"));
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
        this.buttonOK.addActionListener(this.okListener);
        this.buttonCancel.addActionListener(this.cancelListener);
    }

    private void removeEvents() {
        this.textServerURL.removeKeyListener(this.textServerURLKeyListener);
        this.buttonBack.removeActionListener(this.urlActionListener);
        this.buttonForward.removeActionListener(this.urlActionListener);
        this.buttonRefresh.removeActionListener(this.refreshListener);
        this.table.removeMouseListener(this.tableMouseListener);
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
        this.buttonOK = ComponentFactory.createButtonOK();
        this.buttonCancel = ComponentFactory.createButtonCancel();
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
        initializeLayout();
        listDirectory(this.textServerURL.getText(), "", this.getIsOutputFolder());
        if (0 < table.getRowCount()) {
            table.setRowSelectionInterval(0, 0);
        }
    }

    private void initializeLayout() {
        GroupLayout gLayout = new GroupLayout(this.getContentPane());
        gLayout.setAutoCreateContainerGaps(true);
        gLayout.setAutoCreateGaps(true);
        this.getContentPane().setLayout(gLayout);

        // @formatter:off
        gLayout.setHorizontalGroup(gLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(gLayout.createSequentialGroup()
                        .addComponent(this.labelServerURL)
                        .addComponent(this.buttonBack, 60, 60, 60)
                        .addComponent(this.buttonForward, 60, 60, 60)
                        .addComponent(this.textServerURL, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(this.buttonRefresh, 60, 60, 60))
                .addComponent(scrollPaneFormLBSControl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(gLayout.createSequentialGroup()
                        .addGap(10, 10, Short.MAX_VALUE)
                        .addComponent(this.buttonOK, 60, 60, 60)
                        .addComponent(this.buttonCancel, 60, 60, 60))
        );

        gLayout.setVerticalGroup(gLayout.createSequentialGroup()
                .addGroup(gLayout.createParallelGroup(Alignment.CENTER).addComponent(this.labelServerURL)
                        .addComponent(this.buttonBack, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(this.buttonForward, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(this.textServerURL, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(this.buttonRefresh, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addComponent(scrollPaneFormLBSControl, 100, 200, Short.MAX_VALUE)
                .addGroup(gLayout.createParallelGroup(Alignment.CENTER)
                        .addComponent(this.buttonOK)
                        .addComponent(this.buttonCancel)));
        scrollPaneFormLBSControl.setViewportView(table);
        // @formatter:on
    }

    public DialogResult showDialog() {
        this.setVisible(true);
        return this.dialogResult;
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
        }
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
//							WebHDFS.webFile = "";
                            WebHDFS.webURL = root + define.getName() + "/";
                        }
                    } else {
                        WebHDFS.webURL = this.textServerURL.getText();
                        if (define.getName().endsWith(".idx")) {
//							WebHDFS.webFile = "";
                        }
                    }
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

    public int getSelectRow() {
        return table.getSelectedRow();
    }

    public String getURL() {
        return this.textServerURL.getText();
    }

    public JTable getTable() {
        return this.table;
    }

}
