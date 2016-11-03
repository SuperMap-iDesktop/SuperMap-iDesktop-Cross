package com.supermap.desktop.exportUI;

import com.supermap.data.Dataset;
import com.supermap.data.conversion.ExportSetting;
import com.supermap.data.conversion.FileType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IExportPanelFactory;
import com.supermap.desktop.Interface.IPanelModel;
import com.supermap.desktop.baseUI.PanelExportTransform;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.iml.*;
import com.supermap.desktop.localUtilities.CommonUtilities;
import com.supermap.desktop.localUtilities.FiletypeUtilities;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.mutiTable.DDLExportTableModel;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTable;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTableModel;
import com.supermap.desktop.ui.controls.progress.FormProgressTotal;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by xie on 2016/10/26.
 * 导出展示界面
 */
public class DataExportDialog extends SmDialog implements IPanelModel {
    private JButton buttonAddDataset;
    private JButton buttonDelete;
    private JButton buttonSelectAll;
    private JButton buttonInvertSelect;
    private JButton buttonExportsSet;
    private JCheckBox checkBoxAutoClose;
    private SmButton buttonExport;
    private SmButton buttonClose;
    private JPanel panelExport;
    private JPanel panelExportInfo;
    private MutiTable tableExport;
    private JToolBar toolBar;
    private JScrollPane scrollPane;
    private JSplitPane splitPane;
    private JPanel contentPane;
    private Dataset[] datasets;
    public static final int COLUMN_DATASET = 0;
    public static final int COLUMN_EXPORTTYPE = 1;
    public static final int COLUMN_ISOVERWRITE = 2;
    public static final int COLUMN_STATE = 3;
    public static final int COLUMN_FILENAME = 4;
    public static final int COLUMN_FILEPATH = 5;
    private ArrayList<PanelExportTransform> panelExports;
    private PanelExportTransform panelExportsTemp;

    private String[] title = {DataConversionProperties.getString("String_Dataset"),
            DataConversionProperties.getString("string_outputtype"),
            DataConversionProperties.getString("string_coverage"),
            DataConversionProperties.getString("string_tabletitle_state"),
            DataConversionProperties.getString("string_tabletitle_filename"),
            DataConversionProperties.getString("string_directory")};

    private ArrayList<ExportFileInfo> exportsFileInfos;

    private ActionListener addDatasetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                addExportInfo();

            } catch (Exception ex) {
                Application.getActiveApplication().getOutput().output(ex);
            }
        }
    };
    private SteppedComboBox steppedComboBox;
    private TableColumn exportTypeColumn;
    private ItemListener itemListener;

    private void addExportInfo() {
        DatasetChooserDataExport datasetChooser = new DatasetChooserDataExport(DataExportDialog.this, tableExport);
        datasetChooser = null;
    }

    private ActionListener selectAllListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                CommonUtilities.selectAll(tableExport);
                setButtonState();
            } catch (Exception ex) {
                Application.getActiveApplication().getOutput().output(ex);
            }
        }
    };
    private ActionListener invertSelectListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                CommonUtilities.invertSelect(tableExport);
            } catch (Exception ex) {
                Application.getActiveApplication().getOutput().output(ex);
            }
        }
    };
    private ActionListener exportListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 导出
            if (panelExports.isEmpty()) {
                UICommonToolkit.showMessageDialog(DataConversionProperties.getString("String_ExportSettingPanel_Cue_AddFiles"));
            } else {
                FormProgressTotal formProgress = new FormProgressTotal();
                formProgress.setTitle(DataConversionProperties.getString("String_FormExport_FormText"));
                formProgress.doWork(new ExportCallable(panelExports, tableExport));
                if (checkBoxAutoClose.isSelected()) {
                    dispose();
                }
            }
        }
    };
    private ActionListener closeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            dispose();
        }
    };
    private ActionListener deleteListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            deleteExportInfo();
        }
    };
    private KeyListener exportKeyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if ((e.isControlDown() == true) && (e.getKeyCode() == KeyEvent.VK_A) && (1 <= tableExport.getRowCount())) {
                // 键盘点击ctrl+A,全选
                replaceExportInfos(panelExports);
                return;
            }
            if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                // 键盘点击delete,删除
                relesePanelExportTemp();
                deleteExportInfo();
                return;
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {
            if ((e.isShiftDown() == true) && (e.getKeyCode() == KeyEvent.VK_UP) || ((e.isShiftDown() == true) && (e.getKeyCode() == KeyEvent.VK_DOWN))) {
                // 键盘点击shift+up/down刷新右边界面
                replaceExportInfos();
                return;
            } else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                // 键盘点击up/down刷新右边界面
                relesePanelExportTemp();
                CommonUtilities.replace(panelExportInfo, panelExports.get(tableExport.getSelectedRow()));
                return;
            }
        }
    };

    private void replaceExportInfos() {
        ArrayList<PanelExportTransform> tempPanels = new ArrayList<>();
        int[] selectRows = tableExport.getSelectedRows();
        int size = selectRows.length;
        for (int i = 0; i < size; i++) {
            tempPanels.add(panelExports.get(selectRows[i]));
        }
        replaceExportInfos(tempPanels);
    }

    private void relesePanelExportTemp() {
        if (null != panelExportsTemp) {
            panelExportsTemp.dispose();
            panelExportsTemp = null;
        }
    }

    private void replaceExportInfos(ArrayList<PanelExportTransform> tempPanels) {
        IExportPanelFactory panelFactory = new ExportPanelFactory();
        panelExportsTemp = panelFactory.createExportPanel(tempPanels);
        CommonUtilities.replace(panelExportInfo, panelExportsTemp);
    }

    private MouseListener exportMouseListener = new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent e) {
            if (2 == e.getClickCount()) {
                int selectColumn = tableExport.getSelectedColumn();
                if (e.getSource().equals(tableExport) && selectColumn == COLUMN_FILEPATH) {
                    setExportPath();
                } else if (selectColumn != COLUMN_FILENAME && selectColumn != COLUMN_ISOVERWRITE && selectColumn != COLUMN_EXPORTTYPE) {
                    addExportInfo();
                }
            } else if (tableExport.getSelectedRows().length == 1) {
                //刷新右边界面
                relesePanelExportTemp();
                CommonUtilities.replace(panelExportInfo, panelExports.get(tableExport.getSelectedRow()));
            } else if (tableExport.getSelectedRows().length > 1) {
                //刷新右边界面
                replaceExportInfos();
            }
        }

        private void setExportPath() {
            if (!SmFileChoose.isModuleExist("DataExportFrame_OutPutDirectories")) {
                SmFileChoose.addNewNode("", CommonProperties.getString("String_DefaultFilePath"), DataConversionProperties.getString("String_Export"),
                        "DataExportFrame_OutPutDirectories", "GetDirectories");
            }
            SmFileChoose tempfileChooser = new SmFileChoose("DataExportFrame_OutPutDirectories");
            tempfileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int state = tempfileChooser.showSaveDialog(null);
            if (state == JFileChooser.APPROVE_OPTION) {
                String directories = tempfileChooser.getFilePath();
                tableExport.setValueAt(directories, tableExport.getSelectedRow(), COLUMN_FILEPATH);
                tableExport.updateUI();
            }
        }
    };

    private void deleteExportInfo() {
        if (null != tableExport.getSelectedRows() && tableExport.getSelectedRows().length != 0) {
            int size = tableExport.getSelectedRows().length;
            for (int i = size - 1; i >= 0; i--) {
                panelExports.remove(i);
            }
            ((MutiTableModel) tableExport.getModel()).removeRows(tableExport.getSelectedRows());
            this.tableExport.updateUI();
            if (0 < tableExport.getRowCount()) {
                this.tableExport.setRowSelectionInterval(tableExport.getRowCount() - 1, tableExport.getRowCount() - 1);
            }
            setButtonState();
        }
    }

    private WindowListener windowListener = new WindowAdapter() {
        @Override
        public void windowOpened(WindowEvent e) {
            splitPane.setDividerLocation(0.7);
        }

        @Override
        public void windowClosing(WindowEvent e) {
            removeEvents();
        }
    };
    private TableModelListener tableModelListener = new TableModelListener() {
        @Override
        public void tableChanged(TableModelEvent e) {
            try {
                int column = e.getColumn();
                int row = e.getFirstRow();
                String filePath = tableExport.getValueAt(row, COLUMN_FILEPATH).toString();
                String fileName = tableExport.getValueAt(row, COLUMN_FILENAME).toString();
                ExportFileInfo fileInfo = panelExports.get(row).getExportsFileInfo();
                ExportSetting tempExportSetting = fileInfo.getExportSetting();
                if (column == COLUMN_ISOVERWRITE && tableExport.getValueAt(row, column) instanceof Boolean) {
                    tempExportSetting.setOverwrite((Boolean) tableExport.getValueAt(row, column));
                } else if (column == COLUMN_FILENAME || column == COLUMN_FILEPATH) {
                    filePath = getFilePath(filePath, fileInfo, fileName);
                    tempExportSetting.setTargetFilePath(filePath);
                }
            } catch (Exception ex) {
                Application.getActiveApplication().getOutput().output(ex);
            }
        }
    };

    public DataExportDialog(Dataset[] datasets) {
        super();
        this.datasets = datasets;
        initComponents();
        initLayerout();
        initResources();
        registEvents();
        this.componentList.add(this.buttonExport);
        this.componentList.add(this.buttonClose);
        this.setFocusTraversalPolicy(this.policy);
        this.getRootPane().setDefaultButton(this.buttonExport);
    }

    @Override
    public void initComponents() {
        this.buttonAddDataset = new JButton();
        this.buttonDelete = new JButton();
        this.buttonSelectAll = new JButton();
        this.buttonInvertSelect = new JButton();
        this.buttonExportsSet = new JButton();
        this.checkBoxAutoClose = new JCheckBox();
        this.buttonExport = new SmButton();
        this.buttonClose = new SmButton();
        this.panelExport = new JPanel();
        this.exportsFileInfos = new ArrayList<>();
        this.tableExport = new MutiTable();
        this.panelExportInfo = new JPanel();
        this.scrollPane = new JScrollPane();
        this.toolBar = new JToolBar();
        this.contentPane = new JPanel();
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.checkBoxAutoClose.setSelected(true);
        this.setContentPane(contentPane);
        this.tableExport.setRowHeight(23);
        panelExports = new ArrayList<>();
        @SuppressWarnings("serial")
        DDLExportTableModel tableModel = new DDLExportTableModel(title) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == COLUMN_ISOVERWRITE || column == COLUMN_FILENAME || column == COLUMN_EXPORTTYPE) {
                    return true;
                }
                return false;
            }
        };
        this.tableExport.setModel(tableModel);
        initTableTheme();
        if (null != datasets && datasets.length > 0) {
            addTableInfo(datasets);
        }
        initToolbar();
        this.setBounds(600, 260, 800, 450);
    }

    private void initTableTheme() {
        TableColumn datasetColumn = tableExport.getColumn(tableExport.getModel().getColumnName(COLUMN_DATASET));
        this.exportTypeColumn = tableExport.getColumn(tableExport.getModel().getColumnName(COLUMN_EXPORTTYPE));
        TableColumn overwriteColumn = tableExport.getColumn(tableExport.getModel().getColumnName(COLUMN_ISOVERWRITE));
        TableColumn stateColumn = tableExport.getColumn(tableExport.getModel().getColumnName(COLUMN_STATE));
        TableColumn fileNameColumn = tableExport.getColumn(tableExport.getModel().getColumnName(COLUMN_FILENAME));
        TableColumn filePathColumn = tableExport.getColumn(tableExport.getModel().getColumnName(COLUMN_FILEPATH));
        stateColumn.setMaxWidth(40);
        overwriteColumn.setMaxWidth(40);
        this.exportTypeColumn.setMinWidth(120);
        filePathColumn.setMinWidth(140);
        exportTypeColumn.setCellRenderer(TableTooltipCellRenderer.getInstance());
        fileNameColumn.setCellRenderer(TableTooltipCellRenderer.getInstance());
        filePathColumn.setCellRenderer(TableTooltipCellRenderer.getInstance());
        stateColumn.setMaxWidth(50);
        datasetColumn.setCellRenderer(new CommonListCellRenderer());
    }

    public void addTableInfo(Dataset[] datasets) {
        ExportSettingFactory exportSettingFactory = new ExportSettingFactory();
        IExportPanelFactory exportPanelFactory = new ExportPanelFactory();
        for (Dataset dataset : datasets) {
            ExportSetting exportSetting = new ExportSetting();
            exportSetting.setSourceData(dataset);

            ExportFileInfo exportsFileInfo = new ExportFileInfo();
            ExportSetting newExportSetting = new ExportSetting();
            int size = exportSetting.getSupportedFileType().length;
            Object[] temp = new Object[6];
            temp[COLUMN_DATASET] = new DataCell(dataset);
            for (int i = 0; i < size; i++) {
                FileType fileType = exportSetting.getSupportedFileType()[i];
                if (!fileType.equals(FileType.CSV) && !fileType.equals(FileType.GEOJSON)) {
                    newExportSetting = exportSettingFactory.createExportSetting(fileType);
                    temp[COLUMN_EXPORTTYPE] = CommonUtilities.getDatasetName(fileType.toString());
                    exportsFileInfo.setFileType(fileType);
                    break;
                }
            }
            newExportSetting.setSourceData(dataset);
            String filePath = System.getProperty("user.dir");
            String fileName = dataset.getName();
            exportsFileInfo.setExportSetting(newExportSetting);
            filePath = getFilePath(filePath, exportsFileInfo, fileName);
            newExportSetting.setTargetFilePath(filePath);

            exportsFileInfo.setState(DataConversionProperties.getString("string_change"));
            this.exportsFileInfos.add(exportsFileInfo);
            temp[COLUMN_ISOVERWRITE] = newExportSetting.isOverwrite();
            temp[COLUMN_STATE] = exportsFileInfo.getState();
            temp[COLUMN_FILENAME] = fileName;
            temp[COLUMN_FILEPATH] = System.getProperty("user.dir");
            this.panelExports.add(exportPanelFactory.createExportPanel(exportsFileInfo));
            this.tableExport.addRow(temp);
        }
        CommonUtilities.replace(panelExportInfo, panelExports.get(panelExports.size() - 1));
        this.tableExport.setRowSelectionInterval(tableExport.getRowCount() - 1, tableExport.getRowCount() - 1);
        initComboBoxColumns();
        setButtonState();
    }

    private String getFilePath(String filePath, ExportFileInfo exportFileInfo, String fileName) {
        String result = "";
        if (FileType.TEMSClutter == exportFileInfo.getExportSetting().getTargetFileType()) {
            if (!filePath.endsWith(File.separator)) {
                result = filePath + File.separator + fileName + DataConversionProperties.getString("string_index_pause") + "b";
            } else {
                result = filePath + fileName + DataConversionProperties.getString("string_index_pause") + "b";
            }
        } else {
            if (!filePath.endsWith(File.separator)) {
                result = filePath + File.separator + fileName + DataConversionProperties.getString("string_index_pause")
                        + exportFileInfo.getFileType().toString().toLowerCase();
            } else {
                result = filePath + fileName + DataConversionProperties.getString("string_index_pause")
                        + exportFileInfo.getFileType().toString().toLowerCase();
            }
        }
        return result;
    }

    /**
     * 为table添加下拉列表
     */
    public void initComboBoxColumns() {
        final ExportSettingFactory exportSettingFactory = new ExportSettingFactory();

        TableRowCellEditor rowEditor = new TableRowCellEditor(this.tableExport);
        for (int i = 0; i < this.panelExports.size(); i++) {
            final ExportSetting exportSetting = this.panelExports.get(i).getExportsFileInfo().getExportSetting();
            final FileType[] fileTypes = exportSetting.getSupportedFileType();
            final int size = fileTypes.length;
            this.steppedComboBox = new SteppedComboBox(new String[]{});
            CommonUtilities.setComboBoxTheme(this.steppedComboBox);
            this.steppedComboBox.removeAllItems();
            for (int j = 0; j < size; j++) {
                FileType fileType = fileTypes[j];
                if (!fileType.equals(FileType.CSV) && !fileType.equals(FileType.GEOJSON)) {
                    String datasetName = CommonUtilities.getDatasetName(fileType.toString());
                    if (!StringUtilities.isNullOrEmpty(datasetName)) {
                        this.steppedComboBox.addItem(datasetName);
                    }
                }
            }
            Dimension d = this.steppedComboBox.getPreferredSize();
            this.steppedComboBox.setPreferredSize(new Dimension(d.width, d.height));
            this.steppedComboBox.setPopupWidth(d.width);

            rowEditor.setEditorAt(i, new DefaultCellEditor(steppedComboBox));
            exportTypeColumn.setCellEditor(rowEditor);
            this.itemListener = new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        String fileTypeAlias = e.getItem().toString();
                        int row = tableExport.getSelectedRow();
                        FileType fileType = FiletypeUtilities.getFileType(fileTypeAlias);
                        ExportFileInfo exportsFileInfo = panelExports.get(row).getExportsFileInfo();
                        ExportSetting newExportSetting = exportSettingFactory.createExportSetting(fileType);
                        exportsFileInfo.setFileType(fileType);
                        newExportSetting.setSourceData(exportSetting.getSourceData());
                        String filePath = tableExport.getValueAt(row, COLUMN_FILEPATH).toString();
                        String fileName = tableExport.getValueAt(row, COLUMN_FILENAME).toString();
                        filePath = getFilePath(filePath, exportsFileInfo, fileName);
                        newExportSetting.setTargetFilePath(filePath);
                        newExportSetting.setTargetFileType(fileType);
                        exportsFileInfo.setExportSetting(newExportSetting);
                        replaceExportPanel(exportsFileInfo);
                    }
                }

                private void replaceExportPanel(ExportFileInfo exportsFileInfo) {
                    int selectRow = tableExport.getSelectedRow();
                    IExportPanelFactory exportPanelFactory = new ExportPanelFactory();
                    PanelExportTransform exportTransform = exportPanelFactory.createExportPanel(exportsFileInfo);
                    panelExports.remove(selectRow);
                    panelExports.add(selectRow, exportTransform);
                    CommonUtilities.replace(panelExportInfo, exportTransform);
                }
            };
            this.steppedComboBox.addItemListener(this.itemListener);
        }
    }

    private void initToolbar() {
        this.toolBar.setFloatable(false);
        this.toolBar.add(this.buttonAddDataset);
        this.toolBar.add(this.buttonDelete);
        this.toolBar.add(this.buttonSelectAll);
        this.toolBar.add(this.buttonInvertSelect);
        this.toolBar.add(this.buttonExportsSet);
    }

    /**
     * 设置按键状态
     */
    private void setButtonState() {
        if (0 < tableExport.getRowCount()) {
            this.buttonExport.setEnabled(true);
            this.buttonDelete.setEnabled(true);
            this.buttonSelectAll.setEnabled(true);
            this.buttonInvertSelect.setEnabled(true);
        } else {
            this.buttonExport.setEnabled(false);
            this.buttonDelete.setEnabled(false);
            this.buttonSelectAll.setEnabled(false);
            this.buttonInvertSelect.setEnabled(false);
        }
    }

    @Override
    public void initLayerout() {
        initPanelExportLayerout();
        initPanelExportInfoLayout();
        initPanelContentLayerout();
    }

    private void initPanelContentLayerout() {
        this.splitPane = new JSplitPane();
        this.splitPane.setContinuousLayout(true);
        this.splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        this.splitPane.setLeftComponent(this.panelExport);
        this.splitPane.setRightComponent(this.panelExportInfo);
        this.splitPane.setBorder(null);
        this.contentPane.setLayout(new BorderLayout());
        this.contentPane.add(this.splitPane, BorderLayout.CENTER);
    }

    private void initPanelExportInfoLayout() {
        this.panelExportInfo.setLayout(new GridBagLayout());
        IExportPanelFactory exportPanelFactory = new ExportPanelFactory();
        JPanel panel = new JPanel();
        JPanel panelContent = new JPanel();
        if (null != datasets && datasets.length > 0) {
            panelContent = exportPanelFactory.createExportPanel(panelExports.get(panelExports.size() - 1).getExportsFileInfo());
        } else {
            panelContent = new PanelExportTransformForGrid(new ExportFileInfo());
        }
        panel.setLayout(new GridBagLayout());
        panel.add(panelContent, new GridBagConstraintsHelper(0, 0).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        this.panelExportInfo.add((Component) panel, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraintsHelper.BOTH).setWeight(1, 1));
        this.panelExportInfo.add(this.buttonExport, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.EAST).setInsets(5, 0, 5, 10).setWeight(1, 0));
        this.panelExportInfo.add(this.buttonClose, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.EAST).setInsets(5, 0, 5, 5).setWeight(0, 0));
    }

    private void initPanelExportLayerout() {
        this.panelExport.setLayout(new GridBagLayout());
        this.panelExport.add(this.toolBar, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5).setWeight(0, 0));
        this.panelExport.add(this.scrollPane, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 5, 5).setWeight(3, 3).setFill(GridBagConstraints.BOTH));
        this.panelExport.add(this.checkBoxAutoClose, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 5).setWeight(0, 0));
        this.scrollPane.setViewportView(this.tableExport);
    }

    @Override
    public void registEvents() {
        removeEvents();
        this.buttonAddDataset.addActionListener(this.addDatasetListener);
        this.buttonSelectAll.addActionListener(this.selectAllListener);
        this.buttonDelete.addActionListener(this.deleteListener);
        this.buttonInvertSelect.addActionListener(this.invertSelectListener);
        this.buttonExport.addActionListener(this.exportListener);
        this.buttonClose.addActionListener(this.closeListener);
        this.tableExport.getModel().addTableModelListener(this.tableModelListener);

        this.tableExport.addKeyListener(this.exportKeyListener);
        this.tableExport.addMouseListener(this.exportMouseListener);
        this.scrollPane.addMouseListener(this.exportMouseListener);
        this.addWindowListener(this.windowListener);
    }

    @Override
    public void removeEvents() {

    }

    private void initResources() {
        this.setTitle(DataConversionProperties.getString("String_FormExport_FormText"));
        this.buttonAddDataset.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_AddMap.png"));
        this.buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
        this.buttonInvertSelect.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
        this.buttonDelete.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Delete.png"));
        this.buttonAddDataset.setToolTipText(DataConversionProperties.getString("string_button_add"));
        this.buttonDelete.setToolTipText(DataConversionProperties.getString("string_button_delete"));
        this.buttonSelectAll.setToolTipText(DataConversionProperties.getString("string_button_selectAll"));
        this.buttonInvertSelect.setToolTipText(DataConversionProperties.getString("string_button_invertSelect"));
        this.buttonExport.setText(CommonProperties.getString("String_Button_Export"));
        this.buttonExport.setToolTipText(CommonProperties.getString("String_Button_Export"));
        this.buttonClose.setText(CommonProperties.getString("String_Button_Close"));
        this.buttonClose.setToolTipText(CommonProperties.getString("String_Button_Close"));
        this.checkBoxAutoClose.setText(DataConversionProperties.getString("String_CloseAfterExportEnd"));
    }
}
