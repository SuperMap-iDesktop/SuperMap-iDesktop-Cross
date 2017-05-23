package com.supermap.desktop.ui.lbs.ui;

import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.event.FormClosingEvent;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.lbs.impl.HDFSDefine;
import com.supermap.desktop.ui.lbs.impl.HDFSTableModel;
import com.supermap.desktop.ui.lbs.impl.WebHDFS;
import com.supermap.desktop.utilities.CursorUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by xie on 2017/2/25.
 */
public class JPanelHDFSFiles extends JPanel {
    private static final int COLUMN_INDEX_Name = 0;
    private static final int ROW_HEADER_WIDTH = 70;

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

    private ActionListener refreshListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            refresh();
        }
    };
    private ActionListener urlActionListener;

    public JPanelHDFSFiles() {
        initializeComponents();
        initializeLayout();
        initializeResources();
        registEvents();
    }

    private ImageIcon getImageIcon(String path) {
        ImageIcon imageIcon = null;
        try {
            if (!StringUtilities.isNullOrEmptyString(path)) {
                imageIcon = new ImageIcon(JPanelHDFSFiles.class.getResource("/controlsresources/ToolBar/" + path));
            }
        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(e);
        }
        return imageIcon;
    }

    private void initializeResources() {
        this.buttonBack.setIcon(getImageIcon("back.png"));
        this.buttonForward.setIcon(getImageIcon("forward.png"));
        this.buttonRefresh.setIcon(getImageIcon("scale.png"));
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
    }

    private void removeEvents() {
        this.textServerURL.removeKeyListener(this.textServerURLKeyListener);
        this.buttonBack.removeActionListener(this.urlActionListener);
        this.buttonForward.removeActionListener(this.urlActionListener);
        this.buttonRefresh.removeActionListener(this.refreshListener);
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
        this.rowHeader.setBackground(this.getBackground());
        this.scrollPaneFormLBSControl.setRowHeaderView(rowHeader);
        JLabel scrollPaneUpperLeftLabel = new JLabel(CommonProperties.getString("String_ColumnHeader_Index"), SwingConstants.CENTER);
        scrollPaneFormLBSControl.setCorner(JScrollPane.UPPER_LEFT_CORNER, scrollPaneUpperLeftLabel);
        listDirectory(this.textServerURL.getText(), "", this.getIsOutputFolder());
        if (0 < table.getRowCount()) {
            table.setRowSelectionInterval(0, 0);
        }
    }

    private void initializeLayout() {
        GroupLayout gLayout = new GroupLayout(this);
        gLayout.setAutoCreateContainerGaps(true);
        gLayout.setAutoCreateGaps(true);
        this.setLayout(gLayout);

        // @formatter:off
        gLayout.setHorizontalGroup(gLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(gLayout.createSequentialGroup()
                        .addComponent(this.labelServerURL)
                        .addComponent(this.buttonBack, 60, 60, 60)
                        .addComponent(this.buttonForward, 60, 60, 60)
                        .addComponent(this.textServerURL, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(this.buttonRefresh, 60, 60, 60))
                .addComponent(scrollPaneFormLBSControl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(gLayout.createSequentialGroup()
                        .addGap(10, 10, Short.MAX_VALUE))
        );

        gLayout.setVerticalGroup(gLayout.createSequentialGroup()
                .addGroup(gLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(this.labelServerURL)
                        .addComponent(this.buttonBack, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(this.buttonForward, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(this.textServerURL, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(this.buttonRefresh, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addComponent(scrollPaneFormLBSControl, 100, 200, Short.MAX_VALUE)
                .addGroup(gLayout.createParallelGroup(GroupLayout.Alignment.CENTER)));
        scrollPaneFormLBSControl.setViewportView(table);
        // @formatter:on
    }


    /**
     * 添加一行记录
     *
     * @param hdfsDefine
     */
    private void addFileInfo(HDFSDefine hdfsDefine) {
        try {
            HDFSTableModel tableModel = (HDFSTableModel) this.table.getModel();
            tableModel.addRow(hdfsDefine);
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
    }

    public void resetHDFSPath() {
        HDFSDefine define = (HDFSDefine) ((HDFSTableModel) this.table.getModel()).getRowTagAt(table.getSelectedRow());
        if (define != null) {
            // if mouse double click foler, list folder files
            if (define.isDir()) {
                String name = (String) this.table.getModel().getValueAt(table.getSelectedRow(), 0);
                String root = getURL();
                if (!root.endsWith("/")) {
                    root += "/";
                }
                String url = listDirectory(root, name, this.getIsOutputFolder());
                textServerURL.setText(url);
                urlList.add(url);
                // urlPathIndex始终为最新加入的url对应的索引号
                urlPathIndex = urlList.size() - 1;
                buttonBack.setEnabled(true);
            } else {
                buttonOKActionPerformed();
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

        HDFSDefine[] defines = WebHDFS.listDirectory(urlPath, "", isFolderOnly);
        for (HDFSDefine define : defines) {
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
            setBorder(new LineBorder(Color.LIGHT_GRAY));
            setForeground(header.getForeground());
            setBackground(header.getBackground());
            setFont(header.getFont());
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            HDFSDefine define = (HDFSDefine) ((HDFSTableModel) this.table.getModel()).getRowTagAt(index);
            this.setText(String.valueOf(index + 1));
            if (define.isDir()) {
                this.setToolTipText(CommonProperties.getString("String_Dir"));
                this.setIcon(ControlsResources.getIcon("/controlsresources/Image_DatasetGroup_Normal.png"));
            } else {
                this.setToolTipText(CommonProperties.getString("String_File"));
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
