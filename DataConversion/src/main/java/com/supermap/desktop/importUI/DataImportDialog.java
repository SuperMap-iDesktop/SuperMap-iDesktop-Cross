package com.supermap.desktop.importUI;

import com.supermap.data.conversion.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.FileTypeLocale;
import com.supermap.desktop.Interface.IImportPanelFactory;
import com.supermap.desktop.Interface.IImportSettingFactory;
import com.supermap.desktop.Interface.IPanelImport;
import com.supermap.desktop.Interface.IPanelModel;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.iml.ImportCallable;
import com.supermap.desktop.iml.ImportInfo;
import com.supermap.desktop.iml.ImportPanelFactory;
import com.supermap.desktop.iml.ImportSettingFactory;
import com.supermap.desktop.localUtilities.FileUtilities;
import com.supermap.desktop.localUtilities.FiletypeUtilities;
import com.supermap.desktop.localUtilities.ImportUtilities;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.tableModel.ImportTableModel;
import com.supermap.desktop.ui.TableTooltipCellRenderer;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.progress.FormProgressTotal;
import com.supermap.desktop.util.CommonFunction;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by xie on 2016/10/12.
 * 导入展示框
 */
public class DataImportDialog extends SmDialog implements IPanelModel {
    private JSplitPane splitPane;
    private JPanel contentPane;
    private JTable table;
    private ImportTableModel model;
    private JButton buttonAddFile;
    private JButton buttonAddDir;
    private JButton buttonDelete;
    private JButton buttonSelectAll;
    private JButton buttonInvertSelect;
    private SmButton buttonImport;
    private SmButton buttonClose;
    private JPanel panelFiles;
    private JLabel labelTitle;
    private JLabel labelRemind;
    private JPanel panelParams;
    private JPanel panelImportInfo;
    private JCheckBox checkBoxAutoClose;
    private JToolBar toolBar;
    private JScrollPane scrollPane;
    private ArrayList<PanelImport> panelImports;
    private SteppedComboBox steppedComboBox;
    private DropTarget dropTargetTemper;
    private PanelImport panelImportTemp;

    private final int GRID_TYPE = 1;
    private final int VERTICAL_TYPE = 2;
    private final int GRID_AND_VERTICAL_TYPE = 3;
    private final int SAME_TYPE = 4;

    private ActionListener addFileListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            addImportInfo();
        }
    };
    private KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if ((e.isControlDown() == true) && (e.getKeyCode() == KeyEvent.VK_A) && (1 <= table.getRowCount())) {
                // 键盘点击ctrl+A,全选
                replaceImportInfos(panelImports);
                return;
            }
            if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                // 键盘点击delete,删除
                deleteImportInfo();
                return;
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {
            if ((e.isShiftDown() == true) && (e.getKeyCode() == KeyEvent.VK_UP) || ((e.isShiftDown() == true) && (e.getKeyCode() == KeyEvent.VK_DOWN))) {
                // 键盘点击shift+up/down刷新右边界面
                replaceImportInfos();
                return;
            } else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                // 键盘点击up/down刷新右边界面
                relesePanelImportTemp();
                replaceImportInfo(table.getSelectedRow());
                return;
            }
        }
    };

    private ActionListener closeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            removeEvents();
            panelImportsDispose();
            dispose();
        }
    };
    private ActionListener addDirListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 添加文件夹
            int x = (int) buttonAddDir.getLocation().getX() - buttonAddDir.getWidth();
            int y = buttonAddDir.getHeight();
            AddDirDialog addDirDialog = new AddDirDialog();
            addDirDialog.show(buttonAddDir, x, y);
            return;
        }
    };
    private ItemListener aListener;

    private void addImportInfo() {
        //获取并创建需要的导入信息类及导入界面信息类
        SmFileChoose fileChoose = FileUtilities.createImportFileChooser();
        int state = fileChoose.showDefaultDialog();
        File[] files = fileChoose.getSelectFiles();
        String fileFilter = fileChoose.getFileFilter().getDescription();
        if (null != files && state == JFileChooser.APPROVE_OPTION) {
            initImportInfo(files, fileFilter);
        }
    }

    private void initImportInfo(File[] files, String fileFilter) {
        for (File file : files) {
            ImportInfo importInfo = new ImportInfo();
            String fileType = FileUtilities.getFileType(file.getPath());
            if (!".xlsx".equalsIgnoreCase(fileType) && !StringUtilities.isNullOrEmpty(fileType)) {
                //导入信息参数设置
                importInfo.setFileName(file.getName());
                String filetype = FiletypeUtilities.getParseFile(fileType, fileFilter);
                importInfo.setFiletype(filetype);
                importInfo.setState(DataConversionProperties.getString("string_change"));

                IImportSettingFactory importSettingFactory = new ImportSettingFactory();
                ImportSetting importSetting = importSettingFactory.createImportSetting(file.getPath(), fileFilter);
                if (null != importSetting) {
                    importInfo.setImportSetting(importSetting);
                    IImportPanelFactory importPanelFactory = new ImportPanelFactory();
                    PanelImport panelImport = (PanelImport) importPanelFactory.createPanelImport(DataImportDialog.this, importInfo);
                    panelImports.add(panelImport);
                    model.addRow(importInfo);
                }
            }
        }
        if (panelImports.size() > 0) {
            table.setRowSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
            initComboBoxColumns();
            replaceImportInfo(table.getRowCount() - 1);
        }
    }

    private MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent e) {
            if (2 == e.getClickCount()) {
                addImportInfo();
                return;
            } else if (table.getSelectedRows().length == 1) {
                relesePanelImportTemp();
                replaceImportInfo(table.getSelectedRow());
            } else if (table.getSelectedRows().length > 1) {
                replaceImportInfos();
            }
        }
    };

    private void relesePanelImportTemp() {
        if (null != panelImportTemp) {
            panelImportTemp.dispose();
            panelImportTemp = null;
        }
    }

    private void replaceImportInfos() {
        ArrayList<PanelImport> tempPanelImports = new ArrayList<PanelImport>();
        int[] selectRows = table.getSelectedRows();
        int size = selectRows.length;
        for (int i = 0; i < size; i++) {
            tempPanelImports.add(panelImports.get(selectRows[i]));
        }
        replaceImportInfos(tempPanelImports);
    }

    private void replaceImportInfos(ArrayList<PanelImport> panelImports) {
        ArrayList<PanelImport> tempPanelImports = panelImports;
        IImportPanelFactory importPanelFactory = new ImportPanelFactory();
        panelImportTemp = (PanelImport) importPanelFactory.createPanelImport(tempPanelImports);
        int panelImportType = panelImportTemp.getPanelImportType();
        if (panelImportType == SAME_TYPE) {
            String fileType = FileUtilities.getFileType(panelImports.get(0).getImportInfo().getImportSetting().getSourceFilePath());
            if (!StringUtilities.isNullOrEmpty(fileType)) {
                labelTitle.setText(MessageFormat.format(DataConversionProperties.getString("String_ImportFill"), fileType.substring(1, fileType.length()).toUpperCase()));
            }
        } else if (panelImportType == VERTICAL_TYPE) {
            labelTitle.setText(DataConversionProperties.getString("String_FormImportVector_Text"));
        } else if (panelImportType == GRID_TYPE) {
            labelTitle.setText(DataConversionProperties.getString("String_FormImportRaster_Text"));
        } else {
            labelTitle.setText(DataConversionProperties.getString("String_FormImportMix_Text"));
        }
        ImportUtilities.replace(panelImportInfo, panelImportTemp);
        importPanelFactory = null;
        tempPanelImports = null;
    }

    private void replaceImportInfo(int selectRow) {
        IPanelImport panelImport = panelImports.get(selectRow);
        if (null != panelImport.getImportInfo().getImportSetting()) {
            String fileType = FileUtilities.getFileType(panelImport.getImportInfo().getImportSetting().getSourceFilePath());
            if (!StringUtilities.isNullOrEmpty(fileType)) {
                labelTitle.setText(MessageFormat.format(DataConversionProperties.getString("String_ImportFill"), fileType.substring(1, fileType.length()).toUpperCase()));
            }
            initComboBoxColumns();
            ImportUtilities.replace(panelImportInfo, (JPanel) panelImport);
            setButtonState();
        }
    }

    private ActionListener deleteListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            deleteImportInfo();
        }
    };

    private void deleteImportInfo() {
        // 执行删除
        int[] selectedRow = table.getSelectedRows();
        if (!panelImports.isEmpty()) {
            ArrayList<JPanel> removePanel = new ArrayList<JPanel>();
            if (selectedRow.length < table.getRowCount()) {
                for (int i = 0; i < selectedRow.length; i++) {
                    removePanel.add((JPanel) panelImports.get(selectedRow[i]));
                }
                model.removeRows(selectedRow);
                panelImports.removeAll(removePanel);
            } else {
                int[] tempRow = new int[table.getRowCount()];
                for (int i = 0; i < table.getRowCount(); i++) {
                    tempRow[i] = i;
                }
                model.removeRows(tempRow);
                panelImports.removeAll(panelImports);
            }
        }
        // 如果表中没有数据，右边部分显示为默认界面。
        if (panelImports.isEmpty()) {
            labelTitle.setText(DataConversionProperties.getString("string_label_importData"));
            initComboBoxColumns();
            ImportUtilities.replace(panelImportInfo, panelParams);
            setButtonState();
        } else {
            if (selectedRow[0] != table.getRowCount()) {
                table.setRowSelectionInterval(selectedRow[0], selectedRow[0]);
            } else {
                table.setRowSelectionInterval(selectedRow[0] - 1, selectedRow[0] - 1);
            }
            replaceImportInfo(table.getSelectedRow());
        }
    }

    private ActionListener selectAllListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (table.getRowCount() - 1 < 0) {
                table.setRowSelectionAllowed(true);
            } else {
                table.setRowSelectionAllowed(true);
                // 设置所有项全部选中
                table.setRowSelectionInterval(0, table.getRowCount() - 1);
            }
            setButtonState();
        }
    };
    private ActionListener invertSelectListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int[] temp = table.getSelectedRows();
                ArrayList<Integer> selectedRows = new ArrayList<Integer>();
                for (int index = 0; index < temp.length; index++) {
                    selectedRows.add(temp[index]);
                }

                ListSelectionModel selectionModel = table.getSelectionModel();
                selectionModel.clearSelection();
                for (int index = 0; index < table.getRowCount(); index++) {
                    if (!selectedRows.contains(index)) {
                        selectionModel.addSelectionInterval(index, index);
                    }
                }
                int[] selectRows = table.getSelectedRows();
                if (table.getRowCount() == 0) {
                    //没有要导入的项时
                    labelTitle.setText(DataConversionProperties.getString("string_label_importData"));
                    CommonFunction.replace(panelImportInfo, panelParams);
                    setButtonState();
                } else if (1 == selectRows.length) {
                    replaceImportInfo(table.getSelectedRow());
                }
            } catch (Exception ex) {
                Application.getActiveApplication().getOutput().output(ex);
            }
        }
    };
    private ActionListener importListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // 导入表中数据
                if (table.getRowCount() > 0) {
                    final FormProgressTotal formProgress = new FormProgressTotal();
                    formProgress.setTitle(DataConversionProperties.getString("String_FormImport_FormText"));
                    ArrayList<ImportInfo> fileInfos = new ArrayList<ImportInfo>();
                    for (IPanelImport panelImport : panelImports) {
                        fileInfos.add(panelImport.getImportInfo());
                    }
                    if (null != formProgress) {
                        formProgress.doWork(new ImportCallable(fileInfos, table));
                    }
                } else {
                    UICommonToolkit.showMessageDialog(DataConversionProperties.getString("String_ImportSettingPanel_Cue_AddFiles"));
                }
            } catch (Exception ex) {
                Application.getActiveApplication().getOutput().output(ex);
            } finally {
                if (checkBoxAutoClose.isSelected()) {
                    panelImportsDispose();
                    dispose();
                }
            }
        }
    };

    private void panelImportsDispose() {
        for (IPanelImport panelImportTemp : panelImports) {
            panelImportTemp = null;
        }
        panelImports = null;
    }

    public DataImportDialog() {
        super();
        initComponents();
        initLayerout();
        registEvents();
        initResources();
        initDrag();
        this.buttonAddFile.setSelected(false);
        this.componentList.add(this.buttonImport);
        this.componentList.add(this.buttonClose);
        this.setFocusTraversalPolicy(this.policy);
        this.getRootPane().setDefaultButton(this.buttonClose);
        this.setLocationRelativeTo(null);
        this.setBounds(600, 260, 864, 486);
    }

    private void initDrag() {

        dropTargetTemper = new DropTarget(this, new TableDropTargetAdapter(this));

    }

    @Override
    public void initComponents() {
        this.panelImports = new ArrayList<PanelImport>();
        this.splitPane = new JSplitPane();
        this.contentPane = new JPanel();
        this.table = new JTable();
        this.model = new ImportTableModel();
        this.table.setModel(this.model);
        this.buttonAddFile = new JButton();
        this.buttonAddDir = new JButton();
        this.buttonDelete = new JButton();
        this.buttonSelectAll = new JButton();
        this.buttonInvertSelect = new JButton();
        this.buttonImport = new SmButton("import");
        this.buttonClose = new SmButton("close");
        this.panelFiles = new JPanel();
        this.labelTitle = new JLabel();
        this.labelRemind = new JLabel();
        this.panelParams = new JPanel();
        this.panelImportInfo = new JPanel();
        this.checkBoxAutoClose = new JCheckBox();
        this.toolBar = new JToolBar();
        initToolbar();
        initTableTheme();
        this.scrollPane = new JScrollPane();
        this.buttonInvertSelect.setEnabled(false);
        this.checkBoxAutoClose.setSelected(true);
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(contentPane);
        this.labelTitle.setBackground(Color.LIGHT_GRAY);
        this.labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
        this.labelTitle.setOpaque(true);
    }

    private void initTableTheme() {
        TableColumn fileNameColumn = table.getColumn(table.getModel().getColumnName(ImportTableModel.COLUMN_FILENAME));
        TableColumn fileTypeColumn = table.getColumn(table.getModel().getColumnName(ImportTableModel.COLUMN_FILETYPE));
        fileNameColumn.setCellRenderer(TableTooltipCellRenderer.getInstance());
        fileTypeColumn.setCellRenderer(TableTooltipCellRenderer.getInstance());
        fileTypeColumn.setMinWidth(120);
        table.getColumn(table.getModel().getColumnName(ImportTableModel.COLUMN_STATE)).setMaxWidth(50);
    }

    private void initToolbar() {
        this.toolBar.setFloatable(false);
        this.toolBar.add(this.buttonAddFile);
        this.toolBar.add(this.buttonAddDir);
        this.buttonDelete.setEnabled(false);
        this.toolBar.add(this.buttonDelete);
        this.buttonSelectAll.setEnabled(false);
        this.toolBar.add(this.buttonSelectAll);
        this.toolBar.add(this.buttonInvertSelect);
    }

    @Override
    public void initLayerout() {
        initPanelFilesLayerout();
        initPanelImportInfoLayerout();
        initPanelContentLayerout();
    }

    private void initPanelContentLayerout() {
        this.splitPane = new JSplitPane();
        this.splitPane.setContinuousLayout(true);
        this.splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        this.splitPane.setLeftComponent(this.panelFiles);
        this.splitPane.setRightComponent(this.panelImportInfo);
        this.splitPane.setBorder(null);
        this.contentPane.setLayout(new BorderLayout());
        this.contentPane.add(this.splitPane, BorderLayout.CENTER);
    }

    private void initPanelImportInfoLayerout() {
        this.panelImportInfo.setLayout(new GridBagLayout());
        JPanel pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        this.panelImportInfo.add(this.labelTitle, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5).setWeight(1, 0));
        this.panelImportInfo.add(pane, new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraintsHelper.BOTH).setWeight(1, 1));
        this.panelImportInfo.add(this.buttonImport, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.EAST).setInsets(5, 0, 5, 10).setWeight(1, 0));
        this.panelImportInfo.add(this.buttonClose, new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.EAST).setInsets(5, 0, 5, 5).setWeight(0, 0));
        this.labelTitle.setPreferredSize(new Dimension(200, 30));
        this.labelTitle.setMinimumSize(new Dimension(200, 30));
        pane.setBorder(null);
        pane.add(panelParams, new GridBagConstraintsHelper(0, 0).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
        this.panelParams.setLayout(new GridBagLayout());
        this.panelParams.add(this.labelRemind, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER));
        this.buttonImport.setEnabled(false);
    }

    private void initPanelFilesLayerout() {
        this.panelFiles.setLayout(new GridBagLayout());
        this.panelFiles.add(this.toolBar, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5).setWeight(0, 0));
        this.panelFiles.add(this.scrollPane, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 5, 5).setWeight(3, 3).setFill(GridBagConstraints.BOTH));
        this.panelFiles.add(this.checkBoxAutoClose, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 5).setWeight(0, 0));
        this.scrollPane.setViewportView(this.table);
    }

    @Override
    public void registEvents() {
        removeEvents();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                splitPane.setDividerLocation(0.35);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                panelImportsDispose();
                removeEvents();
            }
        });
        this.buttonAddFile.addActionListener(this.addFileListener);
        this.buttonDelete.addActionListener(this.deleteListener);
        this.buttonSelectAll.addActionListener(this.selectAllListener);
        this.buttonInvertSelect.addActionListener(this.invertSelectListener);
        this.buttonImport.addActionListener(this.importListener);
        this.table.addMouseListener(this.mouseListener);
        this.table.addKeyListener(this.keyListener);
        this.scrollPane.addMouseListener(this.mouseListener);
        this.buttonClose.addActionListener(this.closeListener);
        this.buttonAddDir.addActionListener(this.addDirListener);
    }

    @Override
    public void removeEvents() {
        this.buttonAddFile.removeActionListener(this.addFileListener);
        this.buttonDelete.removeActionListener(this.deleteListener);
        this.buttonSelectAll.removeActionListener(this.selectAllListener);
        this.buttonInvertSelect.removeActionListener(this.invertSelectListener);
        this.buttonImport.removeActionListener(this.importListener);
        this.table.removeMouseListener(this.mouseListener);
        this.table.removeKeyListener(this.keyListener);
        this.scrollPane.removeMouseListener(this.mouseListener);
    }

    private void initResources() {
        this.setTitle(DataConversionProperties.getString("String_FormImport_FormText"));
        this.buttonAddDir.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Add.png"));
        this.buttonAddFile.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_AddMap.png"));
        this.buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
        this.buttonInvertSelect.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
        this.buttonDelete.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Delete.png"));
        this.buttonImport.setText(CommonProperties.getString("String_Button_Import"));
        this.buttonClose.setText(CommonProperties.getString("String_Button_Close"));
        this.checkBoxAutoClose.setText(DataConversionProperties.getString("string_chcekbox_autoCloseIn"));
        this.labelTitle.setText(DataConversionProperties.getString("string_label_importData"));
        this.labelRemind.setText(DataConversionProperties.getString("string_label_addFileRemind"));
        this.buttonAddDir.setToolTipText(DataConversionProperties.getString("String_button_addDir"));
        this.buttonAddFile.setToolTipText(DataConversionProperties.getString("String_button_addFile"));
        this.buttonDelete.setToolTipText(DataConversionProperties.getString("string_button_delete"));
        this.buttonSelectAll.setToolTipText(DataConversionProperties.getString("string_button_selectAll"));
        this.buttonInvertSelect.setToolTipText(DataConversionProperties.getString("string_button_invertSelect"));
        this.labelRemind.setForeground(new Color(32, 178, 170));
    }

    /**
     * 设置按键状态
     */
    private void setButtonState() {
        boolean hasImportInfo = false;
        if (0 < table.getRowCount()) {
            hasImportInfo = true;
        }
        if (hasImportInfo) {
            this.buttonImport.setEnabled(true);
            this.buttonDelete.setEnabled(true);
            this.buttonSelectAll.setEnabled(true);
            this.buttonInvertSelect.setEnabled(true);
        } else {
            this.buttonImport.setEnabled(false);
            this.buttonDelete.setEnabled(false);
            this.buttonSelectAll.setEnabled(false);
            this.buttonInvertSelect.setEnabled(false);
        }
    }

    /**
     * 为table添加下拉列表
     */
    @SuppressWarnings("unchecked")
    public void initComboBoxColumns() {
        TableRowCellEditor rowEditor = new TableRowCellEditor(this.table);
        for (int i = 0; i < this.panelImports.size(); i++) {
            final ImportInfo tempFileInfo = this.panelImports.get(i).getImportInfo();
            if (null == tempFileInfo.getImportSetting()) {
                return;
            }
            this.steppedComboBox = new SteppedComboBox(new String[]{});
            this.steppedComboBox.removeAllItems();
            String filePath = tempFileInfo.getImportSetting().getSourceFilePath();
            String fileType = FileUtilities.getFileType(filePath);
            if (fileType.equalsIgnoreCase(FileTypeLocale.DXF_STRING)) {
                this.steppedComboBox.addItem(DataConversionProperties.getString("String_FormImport_CAD"));
                this.steppedComboBox.addItem(DataConversionProperties.getString("String_FormImport_FilterModel"));
            } else if (fileType.equals(FileTypeLocale.BIL_STRING)) {
                this.steppedComboBox.addItem(DataConversionProperties.getString("String_FormImport_GRID"));
                this.steppedComboBox.addItem(DataConversionProperties.getString("String_FormImport_BIL"));
            } else if (fileType.equalsIgnoreCase(FileTypeLocale.DEM_STRING)) {
                steppedComboBox.addItem(DataConversionProperties.getString("String_FormImport_ArcGIS"));
                steppedComboBox.addItem(DataConversionProperties.getString("String_FormImport_DEM"));
                steppedComboBox.addItem(DataConversionProperties.getString("String_FormImport_GBDEM"));
            } else if (fileType.equalsIgnoreCase(FileTypeLocale.TXT_STRING)) {
                this.steppedComboBox.addItem(DataConversionProperties.getString("String_FormImport_ArcGIS"));
                this.steppedComboBox.addItem(DataConversionProperties.getString("String_FormImport_FilterLIDAR"));
            } else {
                this.steppedComboBox.addItem(tempFileInfo.getFileType());
            }
            Dimension d = this.steppedComboBox.getPreferredSize();
            this.steppedComboBox.setPreferredSize(new Dimension(d.width, d.height));
            this.steppedComboBox.setPopupWidth(d.width);

            rowEditor.setEditorAt(i, new DefaultCellEditor(steppedComboBox));
            this.table.getColumn(DataConversionProperties.getString("string_tabletitle_filetype")).setCellEditor(rowEditor);
            this.aListener = new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        String nowType = e.getItem().toString();
                        tempFileInfo.setFiletype(nowType);
                        ImportSetting newImportSetting = null;
                        if (nowType.equalsIgnoreCase(DataConversionProperties.getString("String_FormImport_BIL"))) {
                            // bil 导入为BIL文件
                            newImportSetting = new ImportSettingBIL();
                            replaceImportPanel(newImportSetting);
                        } else if (nowType.equalsIgnoreCase(DataConversionProperties.getString("String_FormImport_GRID"))) {
                            // 导入为电信栅格文件
                            newImportSetting = new ImportSettingTEMSClutter();
                            replaceImportPanel(newImportSetting);
                        } else if (nowType.equalsIgnoreCase(DataConversionProperties.getString("String_FormImport_CAD"))) {
                            // DXF 导入为CAD格式文件
                            newImportSetting = new ImportSettingDXF();
                            replaceImportPanel(newImportSetting);
                        } else if (nowType.equalsIgnoreCase(DataConversionProperties.getString("String_FormImport_FilterModel"))) {
                            // DXF 导入为三维模型文件
                            newImportSetting = new ImportSettingModelDXF();
                            replaceImportPanel(newImportSetting);
                        } else if (nowType.equalsIgnoreCase(DataConversionProperties.getString("String_FormImport_ArcGIS"))) {
                            // txt,dem 导入为ArcGIS类型文件
                            newImportSetting = new ImportSettingGRD();
                            replaceImportPanel(newImportSetting);
                        } else if (nowType.equalsIgnoreCase(DataConversionProperties.getString("String_FormImport_FilterLIDAR"))) {
                            // txt 导入为LIDAR类型文件
                            newImportSetting = new ImportSettingLIDAR();
                            replaceImportPanel(newImportSetting);
                        } else if (nowType.equalsIgnoreCase(DataConversionProperties.getString("String_FormImport_DEM"))) {
                            // dem 导入为美国标准DEM文件
                            newImportSetting = new ImportSettingUSGSDEM();
                            replaceImportPanel(newImportSetting);
                        } else if (nowType.equalsIgnoreCase(DataConversionProperties.getString("String_FormImport_GBDEM"))) {
                            // dem 导入为中国标准DEM文件
                            newImportSetting = new ImportSettingGBDEM();
                            replaceImportPanel(newImportSetting);
                        }
                    }
                }

                private void replaceImportPanel(ImportSetting newImportSetting) {
                    IImportPanelFactory importPanelFactory = new ImportPanelFactory();
                    String filePath = tempFileInfo.getImportSetting().getSourceFilePath();
                    newImportSetting.setSourceFilePath(filePath);
                    tempFileInfo.setImportSetting(newImportSetting);
                    PanelImport panelImport = (PanelImport) importPanelFactory.createPanelImport(DataImportDialog.this, tempFileInfo);
                    String fileType = FileUtilities.getFileType(filePath);
                    if (!StringUtilities.isNullOrEmpty(fileType)) {
                        labelTitle.setText(MessageFormat.format(DataConversionProperties.getString("String_ImportFill"), fileType.substring(1, fileType.length()).toUpperCase()));
                    }
                    int selectRow = table.getSelectedRow();
                    panelImports.remove(selectRow);
                    panelImports.add(selectRow, panelImport);
                    ImportUtilities.replace(panelImportInfo, (JPanel) panelImport);
                }
            };
            this.steppedComboBox.addItemListener(this.aListener);
        }
    }

    class TableDropTargetAdapter extends DropTargetAdapter {
        private DataImportDialog dataImportDialog;

        public TableDropTargetAdapter(DataImportDialog dataImportDialog) {
            this.dataImportDialog = dataImportDialog;
        }

        @Override
        public void drop(DropTargetDropEvent dtde) {
            try {
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dtde.acceptDrop(DnDConstants.ACTION_REFERENCE);// 接收拖拽来的数据
                    // 将文件添加到导入界面
                    @SuppressWarnings("unchecked")
                    java.util.List<File> files = (java.util.List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    initImportInfo(files.toArray(new File[files.size()]), "");
                }
            } catch (Exception e) {
                Application.getActiveApplication().getOutput().output(e);
            }
        }
    }

    class AddDirDialog extends JPopupMenu {

        private static final long serialVersionUID = 1L;
        JList<String> list = new JList<String>();

        public AddDirDialog() {
            initComponent();
        }

        private void initComponent() {
            this.setPreferredSize(new Dimension(160, 90));
            JScrollPane pane = new JScrollPane();
            this.setLayout(new GridBagLayout());
            this.add(pane, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
            pane.setViewportView(list);
            pane.setBorder(null);
            DefaultListModel<String> listModel = new DefaultListModel<String>();
            listModel.addElement(DataConversionProperties.getString("String_FormImportGJB_Text"));
            list.setModel(listModel);
            list.addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!SmFileChoose.isModuleExist("DataImportFrame_ImportDirectories")) {
                        SmFileChoose.addNewNode("", "", DataConversionProperties.getString("String_ScanDir"),
                                "DataImportFrame_ImportDirectories", "GetDirectories");
                    }
                    SmFileChoose tempfileChooser = new SmFileChoose("DataImportFrame_ImportDirectories");
                    tempfileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int state = tempfileChooser.showDefaultDialog();
                    if (state == JFileChooser.APPROVE_OPTION) {
                        String directories = tempfileChooser.getFilePath();
                        ImportInfo importInfo = new ImportInfo();
                        ImportSettingGJB importSettingGJB = new ImportSettingGJB();
                        importSettingGJB.setSourceFilePath(directories);
                        importInfo.setImportSetting(importSettingGJB);
                        importInfo.setFiletype("GJB5068");
                        importInfo.setFileName(directories);
                        importInfo.setState(DataConversionProperties.getString("string_change"));
                        IImportPanelFactory importPanelFactory = new ImportPanelFactory();
                        PanelImport panelImport = (PanelImport) importPanelFactory.createPanelImport(DataImportDialog.this, importInfo);
                        panelImports.add(panelImport);
                        model.addRow(importInfo);
                        if (panelImports.size() > 0) {
                            table.setRowSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
                            labelTitle.setText(MessageFormat.format(DataConversionProperties.getString("String_ImportFill"), "GJB"));
                            initComboBoxColumns();
                            ImportUtilities.replace(panelImportInfo, panelImport);
                            setButtonState();
                        }
                    }

                }
            });
        }
    }

}
