package com.supermap.desktop.mapview.layer.propertycontrols;

import com.supermap.data.ColorDictionary;
import com.supermap.data.Colors;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.colorScheme.ColorsComboBox;
import com.supermap.desktop.controls.colorScheme.ColorsWithKeysTableModel;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.event.ColorTableChangeEvent;
import com.supermap.desktop.event.ColorTableChangeListener;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.mapview.layer.propertymodel.LayerGridParamPropertyModel;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.FontUtilities;
import com.supermap.desktop.utilities.TableUtilities;
import com.supermap.mapping.LayerSettingGrid;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Chens on 2016/12/29 0029.
 * 栅格图层属性颜色表设置
 */
public class LayerGridParamColorTableDialog extends SmDialog {

    //region控件
    //工具条
    private JToolBar toolBar;
    private SmButton buttonMoveBottom;
    private SmButton buttonMoveDown;
    private SmButton buttonMoveUp;
    private SmButton buttonMoveTop;
    private SmButton buttonRemoveColor;
    private SmButton buttonInsertColor;
    private SmButton buttonAddColor;
    private SmButton buttonBatchAddColor;
    private SmButton buttonSelectInvert;
    private SmButton buttonSelectAll;
    private SmButton buttonInvertColors;
    private SmButton buttonInputColorTable;
    private SmButton buttonExportColorTable;
    private SmButton buttonDefaultColotTable;
    private ColorsComboBox comboBoxColor;

    //表
    private JTable tableColor;
    private ColorsWithKeysTableModel colorsWithKeysTableModel;

    //窗口操作按钮
    private JPanel panelButton;//用于放置“应用”“取消”按钮
    private JButton buttonCancel;
    private JButton buttonApply;
    //endregion

    private transient LayerGridParamPropertyModel modelModified;
    private Color[] colorsOrigin;
    private double[] keysOrigin;
    private double[] initDatasetKeysOrigin;
    private ArrayList<ColorTableChangeListener> listeners;

    //by lixiaoyao 栅格颜色表的非普遍性调用进行优化
    LayerSettingGrid currentLayerSettingGrid;


    public LayerGridParamColorTableDialog(LayerGridParamPropertyModel modelModified) {
        super();
        this.modelModified = modelModified;
        LayerSettingGrid setting = (LayerSettingGrid) modelModified.getLayers()[0].getAdditionalSetting();
        ColorDictionary colorDictionary = setting.getColorDictionary();
        colorsOrigin = colorDictionary.getColors();
        keysOrigin = colorDictionary.getKeys();
        this.initDatasetKeysOrigin=this.modelModified.getOriginKeyCount();
        init();
    }

    public LayerGridParamColorTableDialog(LayerSettingGrid layerSettingGrid,double inputOriginKeys[]) {
        super();
        this.currentLayerSettingGrid = new LayerSettingGrid(layerSettingGrid);
        ColorDictionary colorDictionary = this.currentLayerSettingGrid.getColorDictionary();
        colorsOrigin = colorDictionary.getColors();
        keysOrigin = colorDictionary.getKeys();
        this.initDatasetKeysOrigin=inputOriginKeys;
        init();
    }

    private void init() {
        setSize(new Dimension(888, 500));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.setLocationRelativeTo(null);
        setTitle(ControlsProperties.getString("String_ColorTable"));

        initComponents();
        initLayout();
        initResources();
        initListeners();
        initComponentStates();
        componentList.add(buttonApply);
        componentList.add(buttonCancel);
        this.getRootPane().setDefaultButton(buttonApply);
    }

    private void initListeners() {
        buttonSelectAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableColor.selectAll();
            }
        });
        buttonSelectInvert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableUtilities.stopEditing(tableColor);
                TableUtilities.invertSelection(tableColor);
            }

        });

        buttonMoveTop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int length = tableColor.getSelectedRows().length;
                colorsWithKeysTableModel.moveToTop(tableColor.getSelectedRows());
                tableColor.setRowSelectionInterval(0, length - 1);
                tableChange();
                Rectangle cellRect = tableColor.getCellRect(0, 0, true);
                tableColor.scrollRectToVisible(cellRect);
            }
        });

        buttonMoveUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = tableColor.getSelectedRows();
                colorsWithKeysTableModel.moveUp(selectedRows);
                tableColor.clearSelection();
                for (int selectedRow : selectedRows) {
                    tableColor.addRowSelectionInterval(selectedRow - 1, selectedRow - 1);
                }
                selectedRows = tableColor.getSelectedRows();
                Rectangle cellRect = tableColor.getCellRect(selectedRows[0], 0, true);
                tableColor.scrollRectToVisible(cellRect);
                tableChange();
            }
        });

        buttonMoveDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = tableColor.getSelectedRows();
                colorsWithKeysTableModel.moveDown(selectedRows);
                tableColor.clearSelection();
                for (int selectedRow : selectedRows) {
                    tableColor.addRowSelectionInterval(selectedRow + 1, selectedRow + 1);
                }
                selectedRows = tableColor.getSelectedRows();
                Rectangle cellRect = tableColor.getCellRect(selectedRows[selectedRows.length - 1], 0, true);
                tableColor.scrollRectToVisible(cellRect);
                tableChange();
            }
        });

        buttonMoveBottom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int length = tableColor.getSelectedRows().length;
                colorsWithKeysTableModel.moveToBottom(tableColor.getSelectedRows());
                tableColor.setRowSelectionInterval(tableColor.getRowCount() - length, tableColor.getRowCount() - 1);
                Rectangle cellRect = tableColor.getCellRect(tableColor.getRowCount() - 1, 0, true);
                tableColor.scrollRectToVisible(cellRect);
                tableChange();
            }
        });

        buttonAddColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JPopupMenu popupMenu = new JPopupMenu();
                popupMenu.setBorderPainted(false);
                ColorSelectionPanel colorSelectionPanel = new ColorSelectionPanel();
                popupMenu.add(colorSelectionPanel, BorderLayout.CENTER);
                colorSelectionPanel.setPreferredSize(new Dimension(170, 205));
                popupMenu.show(buttonAddColor, 0, buttonAddColor.getBounds().height);
                colorSelectionPanel.addPropertyChangeListener("m_selectionColor", new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        Color color = (Color) evt.getNewValue();
                        colorsWithKeysTableModel.add(color);
                        tableColor.setRowSelectionInterval(tableColor.getRowCount() - 1, tableColor.getRowCount() - 1);
                        tableColor.scrollRectToVisible(tableColor.getCellRect(tableColor.getRowCount() - 1, 0, true));
                        popupMenu.setVisible(false);
                    }
                });
                tableChange();
            }
        });
        buttonBatchAddColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BatchAddColorTableDailog batchAddColorTableDailog;
                int startIndex=colorsWithKeysTableModel.getRowCount();
                if (colorsWithKeysTableModel.getRowCount()<=1){
                    batchAddColorTableDailog=new BatchAddColorTableDailog(initDatasetKeysOrigin[0],initDatasetKeysOrigin[initDatasetKeysOrigin.length-1],initDatasetKeysOrigin.length,(JFrame) Application.getActiveApplication().getMainFrame(), true);
                }else{
                    List<Double> currentColorTableKeys=colorsWithKeysTableModel.getKeys();
                    Collections.sort(currentColorTableKeys);
                    batchAddColorTableDailog=new BatchAddColorTableDailog(currentColorTableKeys.get(0),currentColorTableKeys.get(currentColorTableKeys.size()-1),initDatasetKeysOrigin.length,(JFrame) Application.getActiveApplication().getMainFrame(), true);
                }
                DialogResult result=batchAddColorTableDailog.showDialog();
                if (result==DialogResult.OK && batchAddColorTableDailog.getResultKeys()!=null){
                    int count = batchAddColorTableDailog.getResultKeys().length;
                    Colors colors = comboBoxColor.getGradientColors(count);
                    if (colors == null) {
                        return;
                    }
                    colorsWithKeysTableModel.addColorNodes(colors.toArray(),batchAddColorTableDailog.getResultKeys());
                    tableChange();
                    colorsWithKeysTableModel.fireTableDataChanged();
                    tableColor.setRowSelectionInterval(startIndex, tableColor.getRowCount() - 1);
                    tableColor.scrollRectToVisible(tableColor.getCellRect(tableColor.getRowCount() - 1, 0, true));
                }
            }
        });

        buttonInsertColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tableColor.getSelectedRowCount() > 0) {
                    final int selectedRow = tableColor.getSelectionModel().getMaxSelectionIndex();
                    final JPopupMenu popupMenu = new JPopupMenu();
                    popupMenu.setBorderPainted(false);
                    ColorSelectionPanel colorSelectionPanel = new ColorSelectionPanel();
                    popupMenu.add(colorSelectionPanel, BorderLayout.CENTER);
                    colorSelectionPanel.setPreferredSize(new Dimension(170, 205));
                    popupMenu.show(buttonInsertColor, 0, buttonInsertColor.getBounds().height);
                    colorSelectionPanel.addPropertyChangeListener("m_selectionColor", new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            Color color = (Color) evt.getNewValue();
                            colorsWithKeysTableModel.insert(selectedRow, color);
                            tableColor.setRowSelectionInterval(selectedRow + 1, selectedRow + 1);
                            popupMenu.setVisible(false);
                        }
                    });
                    tableChange();
                }
            }
        });

        buttonRemoveColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRow = tableColor.getSelectedRows();
                colorsWithKeysTableModel.removeRow(selectedRow);
                if (selectedRow[0] < tableColor.getRowCount()) {
                    tableColor.setRowSelectionInterval(selectedRow[0], selectedRow[0]);
                } else if (tableColor.getRowCount() > 0) {
                    tableColor.setRowSelectionInterval(tableColor.getRowCount() - 1, tableColor.getRowCount() - 1);
                }

                tableChange();
            }

        });

        buttonApply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogResult = DialogResult.OK;
                setVisible(false);
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //还原图层
                actionCanceled();
                dialogResult = DialogResult.CANCEL;
                setVisible(false);
            }

        });

        buttonInvertColors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRow = tableColor.getSelectedRows();
                if (selectedRow.length > 1) {
                    colorsWithKeysTableModel.colorInvert(selectedRow);
                } else {
                    colorsWithKeysTableModel.colorInvert();
                }
                tableChange();
            }
        });

        buttonExportColorTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportColorTable();
            }
        });

        buttonInputColorTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputColorTable();
            }
        });

        //  The default color scheme is the default color scheme to generate the same number of colors in the current layer
        buttonDefaultColotTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int count =initDatasetKeysOrigin.length;
                Colors colors = comboBoxColor.getGradientColors(count);
                if (colors == null) {
                    return;
                }
                colorsWithKeysTableModel.setColorNodes(colors.toArray(), initDatasetKeysOrigin);
                tableColor.repaint();//解决颜色列刷新不全的问题
                tableChange();
                colorsWithKeysTableModel.fireTableDataChanged();
            }
        });

        tableColor.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                checkButtonStates();
            }
        });

        tableColor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1
                        && tableColor.columnAtPoint(e.getPoint()) == 1 && tableColor.rowAtPoint(e.getPoint()) != -1) {
                    final JPopupMenu popupMenu = new JPopupMenu();
                    final int selectedRow = tableColor.getSelectedRow();
                    popupMenu.setBorderPainted(false);
                    ColorSelectionPanel colorSelectionPanel = new ColorSelectionPanel();
                    popupMenu.add(colorSelectionPanel, BorderLayout.CENTER);
                    colorSelectionPanel.setPreferredSize(new Dimension(170, 205));
                    popupMenu.show(tableColor, (int) e.getPoint().getX(), (int) e.getPoint().getY());
                    colorSelectionPanel.addPropertyChangeListener("m_selectionColor", new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            Color color = (Color) evt.getNewValue();
                            tableColor.setValueAt(color, selectedRow, 1);
                            popupMenu.setVisible(false);
                        }
                    });
                    tableColor.setRowSelectionInterval(selectedRow, selectedRow);
                }
            }
        });

        comboBoxColor.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int count = colorsWithKeysTableModel.getRowCount();
                Colors colors = comboBoxColor.getGradientColors(count);
                if (colors == null) {
                    return;
                }
                colorsWithKeysTableModel.setColors(colors);
                tableColor.repaint();//解决颜色列刷新不全的问题
                tableChange();
            }
        });

        colorsWithKeysTableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    tableChange();
                }
            }
        });
    }

    private void initResources() {
        buttonAddColor.setToolTipText(ControlsProperties.getString("String_AddColor"));
        buttonBatchAddColor.setToolTipText(ControlsProperties.getString("String_AddRange"));
        buttonInsertColor.setToolTipText(ControlsProperties.getString("String_Insert"));
        buttonRemoveColor.setToolTipText(ControlsProperties.getString("String_RemoveColor"));
        buttonSelectAll.setToolTipText(ControlsProperties.getString("String_SelectAll"));
        buttonSelectInvert.setToolTipText(ControlsProperties.getString("String_SelectReverse"));
        buttonInvertColors.setToolTipText(ControlsProperties.getString("String_ReverseColor"));
        buttonMoveTop.setToolTipText(ControlsProperties.getString("String_FirstColor"));
        buttonMoveUp.setToolTipText(ControlsProperties.getString("String_UpColor"));
        buttonMoveDown.setToolTipText(ControlsProperties.getString("String_DownColor"));
        buttonMoveBottom.setToolTipText(ControlsProperties.getString("String_LastColor"));

        buttonApply.setText(CommonProperties.getString(CommonProperties.OK));
        buttonCancel.setText(CommonProperties.getString(CommonProperties.Cancel));
    }

    private void initLayout() {
        initToolBar();
        initPanelButton();
        this.setLayout(new GridBagLayout());
        this.add(toolBar, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 10));
        this.add(new JScrollPane(tableColor), new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(5, 10, 0, 10));
        this.add(panelButton, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 10, 10));
    }

    private void initComponents() {
        initColorsTable();
        buttonMoveTop = new SmButton();
        buttonMoveTop.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/moveToTop.png"));

        buttonMoveUp = new SmButton();
        buttonMoveUp.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/moveUp.png"));

        buttonMoveDown = new SmButton();
        buttonMoveDown.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/moveDown.png"));

        buttonMoveBottom = new SmButton();
        buttonMoveBottom.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/moveBottom.png"));

        buttonSelectAll = new SmButton();
        buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));

        buttonSelectInvert = new SmButton();
        buttonSelectInvert.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));

        buttonAddColor = new SmButton();
        buttonAddColor.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/addColor.png"));

        buttonBatchAddColor = new SmButton();
        buttonBatchAddColor.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/batchAdd.png"));

        buttonInsertColor = new SmButton();
        buttonInsertColor.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/insert.png"));

        buttonRemoveColor = new SmButton();
        buttonRemoveColor.setToolTipText(ControlsProperties.getString("String_RemoveColorScheme"));
        buttonRemoveColor.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Delete.png"));

        buttonInvertColors = new SmButton();
        buttonInvertColors.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/invert.png"));

        buttonInputColorTable = new SmButton();
        buttonInputColorTable.setToolTipText(MapViewProperties.getString("String_InputColorTable"));
        buttonInputColorTable.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/Image_ToolButton_Import.png"));

        buttonExportColorTable = new SmButton();
        buttonExportColorTable.setToolTipText(MapViewProperties.getString("String_ExprotColorTable"));
        buttonExportColorTable.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/Image_ToolButton_Export.png"));

        buttonDefaultColotTable = new SmButton();
        buttonDefaultColotTable.setToolTipText(MapViewProperties.getString("String_DefaultColorTable"));
        buttonDefaultColotTable.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/defaultColors.png"));

        comboBoxColor = new ColorsComboBox(ControlsProperties.getString("String_ColorSchemeManager_Grid_DEM"));
        comboBoxColor.setPreferredSize(new Dimension(200, 25));
        comboBoxColor.setMaximumSize(new Dimension(200, 25));
        comboBoxColor.setMinimumSize(new Dimension(200, 25));

        buttonApply = new SmButton();
        buttonCancel = new SmButton();
    }

    private void initToolBar() {
        if (toolBar == null) {
            toolBar = new JToolBar();
            toolBar.setFloatable(false);
            toolBar.add(buttonAddColor);
            toolBar.add(buttonBatchAddColor);
            toolBar.add(buttonInsertColor);
            toolBar.add(buttonRemoveColor);
            toolBar.add(ToolbarUIUtilities.getVerticalSeparator());
            toolBar.add(buttonSelectAll);
            toolBar.add(buttonSelectInvert);
            toolBar.add(ToolbarUIUtilities.getVerticalSeparator());
            toolBar.add(buttonMoveTop);
            toolBar.add(buttonMoveUp);
            toolBar.add(buttonMoveDown);
            toolBar.add(buttonMoveBottom);
            toolBar.add(comboBoxColor);
            toolBar.add(buttonDefaultColotTable);
            toolBar.add(buttonInvertColors);
            toolBar.add(buttonInputColorTable);
            toolBar.add(buttonExportColorTable);

            buttonAddColor.setFocusable(false);
            buttonBatchAddColor.setFocusable(false);
            buttonInsertColor.setFocusable(false);
            buttonRemoveColor.setFocusable(false);
            buttonSelectAll.setFocusable(false);
            buttonSelectInvert.setFocusable(false);
            buttonMoveUp.setFocusable(false);
            buttonMoveTop.setFocusable(false);
            buttonMoveDown.setFocusable(false);
            buttonMoveBottom.setFocusable(false);
            buttonInvertColors.setFocusable(false);
            buttonInputColorTable.setFocusable(false);
            buttonExportColorTable.setFocusable(false);
            buttonDefaultColotTable.setFocusable(false);
            comboBoxColor.setFocusable(false);
        }
    }

    private void initPanelButton() {
        panelButton = new JPanel();
        panelButton.setLayout(new GridBagLayout());
        panelButton.add(buttonApply, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setWeight(1, 1));
        panelButton.add(buttonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST).setWeight(0, 1).setInsets(0, 5, 0, 0));
    }

    /**
     * 填充控件初始值
     */
    private void initComponentStates() {
        LayerSettingGrid setting = null;
        if (modelModified != null) {
            setting = (LayerSettingGrid) modelModified.getLayers()[0].getAdditionalSetting();
        } else if (this.currentLayerSettingGrid != null) {
            setting = this.currentLayerSettingGrid;
        }
        colorsWithKeysTableModel.setColorNodes(setting.getColorDictionary().getColors(), setting.getColorDictionary().getKeys());
        if (tableColor.getRowCount() > 0) {
            tableColor.setRowSelectionInterval(0, 0);
        } else {
            checkButtonStates();
        }
    }

    /**
     * 设置各按钮是否可用
     */
    private void checkButtonStates() {
        int rowCount = tableColor.getRowCount();
        int selectedRowCount = tableColor.getSelectedRowCount();
        buttonRemoveColor.setEnabled(selectedRowCount > 0);
        buttonSelectAll.setEnabled(rowCount > 0);
        buttonSelectInvert.setEnabled(rowCount > 0);
        buttonInvertColors.setEnabled(rowCount > 0);
        buttonInsertColor.setEnabled(selectedRowCount > 0);

        buttonMoveTop.setEnabled(selectedRowCount > 0 && tableColor.getSelectedRows()[selectedRowCount - 1] != selectedRowCount - 1);
        buttonMoveUp.setEnabled(selectedRowCount > 0 && !tableColor.isRowSelected(0));
        buttonMoveDown.setEnabled(selectedRowCount > 0 && !tableColor.isRowSelected(rowCount - 1));
        buttonMoveBottom.setEnabled(selectedRowCount > 0 && tableColor.getSelectedRow() != rowCount - selectedRowCount);

        buttonApply.setEnabled(rowCount > 0);
    }

    private JTable initColorsTable() {
        if (tableColor == null) {
            tableColor = new JTable();
            tableColor.setRowHeight(25);
            colorsWithKeysTableModel = new ColorsWithKeysTableModel();
            tableColor.setModel(colorsWithKeysTableModel);

            TableColumn indexColumn = tableColor.getColumnModel().getColumn(0);
            TableColumn colorColumn = tableColor.getColumnModel().getColumn(1);
            TableColumn valueColumn = tableColor.getColumnModel().getColumn(2);

            colorColumn.setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel label = new JLabel();
                    label.setOpaque(true);
                    if (value instanceof Color) {
                        label.setBackground((Color) value);
                    }
                    return label;
                }

            });

            int indexWidth = FontUtilities.getStringWidth(ControlsProperties.getString("String_identifier"), tableColor.getTableHeader().getFont()) + 30;
            indexColumn.setMaxWidth(indexWidth);
            indexColumn.setPreferredWidth(indexWidth);
            indexColumn.setMinWidth(indexWidth);
            indexColumn.setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component rendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (rendererComponent instanceof JLabel) {
                        ((JLabel) rendererComponent).setHorizontalAlignment(CENTER);
                    }
                    return rendererComponent;
                }
            });

            //格式化数值显示1.0显示为1
            TableCellRenderer numberRenderer = new TableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel jLabel = new JLabel();
                    jLabel.setOpaque(true);
                    jLabel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                    jLabel.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                    if (value != null) {
                        jLabel.setText(DoubleUtilities.getFormatString(Double.valueOf(String.valueOf(value))));
                    }
                    return jLabel;
                }
            };
            tableColor.setDefaultRenderer(Double.class, numberRenderer);

            class ValueCellEditor extends DefaultCellEditor {
                public ValueCellEditor(JTextField textField) {
                    super(textField);
                }

                public double originalValue;

                @Override
                public Object getCellEditorValue() {
                    String s = super.getCellEditorValue().toString();
                    double aDouble;
                    try {
                        aDouble = Double.parseDouble(s);
                    } catch (NumberFormatException e) {
                        return originalValue;
                    }
                    return aDouble;
                }

                @Override
                public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                    originalValue = (double) table.getModel().getValueAt(row, column);
                    Component component = super.getTableCellEditorComponent(table, value, isSelected, row, column);
                    if (component instanceof JTextField) {
                        ((JTextField) component).setHorizontalAlignment(SwingConstants.LEFT);
                    }
                    return component;
                }
            }
            valueColumn.setCellEditor(new ValueCellEditor(new JTextField()));
        }


        return tableColor;
    }

    /**
     * 颜色表发生改动时更改图层设置类
     */
    private void tableChange() {
        double[] keys = new double[colorsWithKeysTableModel.getRowCount()];
        Color[] colors = new Color[colorsWithKeysTableModel.getRowCount()];
        for (int i = 0; i < colorsWithKeysTableModel.getRowCount(); i++) {
            keys[i] = colorsWithKeysTableModel.getKeys().get(i);
            colors[i] = colorsWithKeysTableModel.getColors().get(i);
        }
        if (modelModified != null) {
            modelModified.setLayerGridColorDictionary(keys, colors);
        } else {
            resetCurrentLayerSettingGrid(keys, colors);
        }

        fireColorTableChange(new ColorTableChangeEvent(this, keys, colors));
    }

    public void actionCanceled() {
        //还原图层
        if (modelModified != null) {
            modelModified.setLayerGridColorDictionary(keysOrigin, colorsOrigin);
            modelModified.refresh();
        } else {
            resetCurrentLayerSettingGrid(keysOrigin, colorsOrigin);
        }
    }

    /**
     * 颜色表更改信息传递
     */
    protected void fireColorTableChange(ColorTableChangeEvent event) {
        double[] keys = event.getKeys();
        Color[] colors = event.getColors();

        if (keys != null || colors != null) {
            if (listeners != null) {
                for (ColorTableChangeListener listener : listeners) {
                    listener.colorTableChange(event);
                }
            }
        }
    }

    public void addColorTableChangeListener(ColorTableChangeListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        if (listeners.indexOf(listener) == -1) {
            listeners.add(listener);
        }
    }

    public void removeColorTableChangeListener(ColorTableChangeListener listener) {
        if (listeners.indexOf(listener) > -1) {
            listeners.remove(listener);
        }
    }

    private void resetCurrentLayerSettingGrid(double[] keys, Color[] colors) {
        if (this.currentLayerSettingGrid != null) {
            ColorDictionary colorDictionary = this.currentLayerSettingGrid.getColorDictionary();
            colorDictionary.clear();
            for (int i = 0; i < colors.length; i++) {
                colorDictionary.setColor(keys[i], colors[i]);
            }
            this.currentLayerSettingGrid.setColorDictionary(colorDictionary);
        }
    }

    private boolean exportColorTable() {
        boolean result = true;
        String moduleName = "ExportColorsTable";
        if (!SmFileChoose.isModuleExist(moduleName)) {
            String fileFilters = SmFileChoose.createFileFilter(MapViewProperties.getString("String_DialogColorTable"), "sctu");
            SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
                    MapViewProperties.getString("String_SaveAsFile"), moduleName, "SaveOne");
        }
        SmFileChoose smFileChoose = new SmFileChoose(moduleName);
        smFileChoose.setSelectedFile(new File("ColorTable.sctu"));
        int state = smFileChoose.showDefaultDialog();
        String filePath = "";
        if (state == JFileChooser.APPROVE_OPTION) {
            filePath = smFileChoose.getFilePath();
            File oleFile = new File(filePath);
            filePath = filePath.substring(0, filePath.lastIndexOf("."))+".sctu";
            File NewFile =new File(filePath);
            oleFile.renameTo(NewFile);
            if (oleFile.isFile() && oleFile.exists()) {
                oleFile.delete();
            }
            ColorTableXmlImpl colorTableXml = new ColorTableXmlImpl();
            ColorDictionary colorDictionary = new ColorDictionary();
            colorDictionary.clear();
            for (int i = 0; i < colorsWithKeysTableModel.getRowCount(); i++) {
                colorDictionary.setColor(colorsWithKeysTableModel.getKeys().get(i), colorsWithKeysTableModel.getColors().get(i));
            }
            if (colorTableXml.createXml(filePath, colorDictionary)) {
                Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_DialogColorTableExportSucess") + filePath);
            } else {
                Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_DialogColorTableExportFailed"));
            }
        }

        return result;
    }

    private void inputColorTable() {
        String moduleName = "InputColorsTable";
        if (!SmFileChoose.isModuleExist(moduleName)) {
            String fileFilters=SmFileChoose.bulidFileFilters(SmFileChoose.createFileFilter(MapViewProperties.getString("String_DialogColorTable"), "sctu"),
                    SmFileChoose.createFileFilter(MapViewProperties.getString("String_DialogColorTableSct"), "sct"));
            SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
                    MapViewProperties.getString("String_OpenColorTable"), moduleName, "OpenMany");
        }
        SmFileChoose smFileChoose = new SmFileChoose(moduleName);
        int state = smFileChoose.showDefaultDialog();
        String filePath = "";
        if (state == JFileChooser.APPROVE_OPTION) {
            filePath = smFileChoose.getFilePath();
            ColorTableXmlImpl colorTableXml = new ColorTableXmlImpl();
            ColorDictionary colorDictionary = new ColorDictionary();
            colorDictionary.clear();
            colorDictionary = colorTableXml.parserXml(filePath);
            if (colorDictionary != null) {
                inputDataChangeColorTable(colorDictionary);
                Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_DialogColorTableInputSucess") + filePath);
            } else {
                Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_DialogColorTableInputFailed"));
            }
        }
    }

    private void inputDataChangeColorTable(ColorDictionary colorDictionary) {
        Color[] colors = colorDictionary.getColors();
        double[] keys = colorDictionary.getKeys();
        colorsWithKeysTableModel.setColorNodes(colors, keys);
        if (modelModified != null) {
            modelModified.setLayerGridColorDictionary(keys, colors);
        } else {
            resetCurrentLayerSettingGrid(keys, colors);
        }
        colorsWithKeysTableModel.fireTableDataChanged();
        fireColorTableChange(new ColorTableChangeEvent(this, keys, colors));
    }

    public LayerSettingGrid getCurrentLayerSettingGrid() {
        return this.currentLayerSettingGrid;
    }
}

