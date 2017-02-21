package com.supermap.desktop.mapview.layer.propertycontrols;

import com.supermap.data.ColorDictionary;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.colorScheme.ColorsComboBox;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.event.ColorTableChangeEvent;
import com.supermap.desktop.event.ColorTableChangeListener;
import com.supermap.desktop.mapview.layer.propertymodel.LayerGridParamPropertyModel;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.FontUtilities;
import com.supermap.desktop.utilities.TableUtilities;
import com.supermap.desktop.controls.colorScheme.ColorsWithKeysTableModel;
import com.supermap.mapping.LayerSettingGrid;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.util.List;

/**
 * Created by Chens on 2016/12/29 0029.
 * 栅格图层属性颜色表设置
 */
public class LayerGridParamColorTableDialog extends SmDialog{

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
    private SmButton buttonSelectInvert;
    private SmButton buttonSelectAll;
    private SmButton buttonInvertColors;
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
    private ArrayList<ColorTableChangeListener> listeners;

    //by lixiaoyao 栅格颜色表的非普遍性调用进行优化
    LayerSettingGrid currentLayerSettingGrid;




    public LayerGridParamColorTableDialog(LayerGridParamPropertyModel modelModified){
        super();
        this.modelModified = modelModified;
        LayerSettingGrid setting = (LayerSettingGrid) modelModified.getLayers()[0].getAdditionalSetting();
        ColorDictionary colorDictionary = setting.getColorDictionary();
        colorsOrigin = colorDictionary.getColors();
        keysOrigin = colorDictionary.getKeys();

        init();
    }

    public LayerGridParamColorTableDialog(LayerSettingGrid layerSettingGrid){
        super();
        this.currentLayerSettingGrid  = layerSettingGrid;
        ColorDictionary colorDictionary = this.currentLayerSettingGrid.getColorDictionary();
        colorsOrigin = colorDictionary.getColors();
        keysOrigin = colorDictionary.getKeys();

        init();
    }

    private void init() {
        setSize(new Dimension(700, 500));
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
                tableChange();
            }
        });

        buttonMoveBottom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int length = tableColor.getSelectedRows().length;
                colorsWithKeysTableModel.moveToBottom(tableColor.getSelectedRows());
                tableColor.setRowSelectionInterval(tableColor.getRowCount() - length, tableColor.getRowCount() - 1);
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
                colorSelectionPanel.setPreferredSize(new Dimension(170, 200));
                popupMenu.show(buttonAddColor, buttonAddColor.getX(), buttonAddColor.getY());
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

        buttonInsertColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tableColor.getSelectedRowCount() > 0) {
                    final int selectedRow = tableColor.getSelectionModel().getMaxSelectionIndex();
                    final JPopupMenu popupMenu = new JPopupMenu();
                    popupMenu.setBorderPainted(false);
                    ColorSelectionPanel colorSelectionPanel = new ColorSelectionPanel();
                    popupMenu.add(colorSelectionPanel, BorderLayout.CENTER);
                    colorSelectionPanel.setPreferredSize(new Dimension(170, 200));
                    popupMenu.show(buttonInsertColor, buttonInsertColor.getX(), buttonInsertColor.getY());
                    colorSelectionPanel.addPropertyChangeListener("m_selectionColor", new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            Color color = (Color) evt.getNewValue();
                            colorsWithKeysTableModel.insert(selectedRow,color);
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
                if (modelModified!=null) {
                    modelModified.setLayerGridColorDictionary(keysOrigin, colorsOrigin);
                    modelModified.refresh();
                }else{
                    resetCurrentLayerSettingGrid(keysOrigin, colorsOrigin);
                }

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
                    colorSelectionPanel.setPreferredSize(new Dimension(170, 200));
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
                List<Color> colors = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    colors.add(comboBoxColor.getSelectedItem().toArray()[i]);
                }
                colorsWithKeysTableModel.setColors(colors);
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

        buttonInsertColor = new SmButton();
        buttonInsertColor.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/add.png"));

        buttonRemoveColor = new SmButton();
        buttonRemoveColor.setToolTipText(ControlsProperties.getString("String_RemoveColorScheme"));
        buttonRemoveColor.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Delete.png"));

        buttonInvertColors = new SmButton();
        buttonInvertColors.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/ColorScheme/invert.png"));

        comboBoxColor = new ColorsComboBox(ControlsProperties.getString("String_ColorSchemeManager_Grid_DEM"));
        comboBoxColor.setPreferredSize(new Dimension(200,25));
        comboBoxColor.setMaximumSize(new Dimension(200, 25));
        comboBoxColor.setMinimumSize(new Dimension(200,25));

        buttonApply = new SmButton();
        buttonCancel = new SmButton();
    }

    private void initToolBar() {
        if (toolBar == null) {
            toolBar = new JToolBar();
            toolBar.setFloatable(false);
            toolBar.add(buttonAddColor);
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
            toolBar.add(buttonInvertColors);

            buttonAddColor.setFocusable(false);
            buttonRemoveColor.setFocusable(false);
            buttonSelectAll.setFocusable(false);
            buttonSelectInvert.setFocusable(false);
            buttonInvertColors.setFocusable(false);
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
        LayerSettingGrid setting=null;
        if (modelModified!=null) {
            setting = (LayerSettingGrid) modelModified.getLayers()[0].getAdditionalSetting();
        }else if (this.currentLayerSettingGrid!=null){
            setting=this.currentLayerSettingGrid;
        }
        comboBoxColor.setSelectedItem(setting.getColorTable());
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
        buttonInsertColor.setEnabled(selectedRowCount>0);
        buttonMoveTop.setEnabled(rowCount>0);
        buttonMoveUp.setEnabled(rowCount>0);
        buttonMoveDown.setEnabled(rowCount>0);
        buttonMoveBottom.setEnabled(rowCount>0);

        buttonApply.setEnabled(rowCount > 1);
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

            valueColumn.setCellRenderer(new DefaultTableCellRenderer(){
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component rendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (rendererComponent instanceof JLabel) {
                        ((JLabel) rendererComponent).setHorizontalAlignment(CENTER);
                    }
                    return rendererComponent;
                }
            });
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
        if (modelModified!=null) {
            modelModified.setLayerGridColorDictionary(keys, colors);
        }else {
            resetCurrentLayerSettingGrid(keys, colors);
        }

        fireColorTableChange(new ColorTableChangeEvent(this,keys,colors));
    }

    /**
     * 颜色表更改信息传递
     * @param event
     */
    protected void fireColorTableChange(ColorTableChangeEvent event) {
        double[] keys = event.getKeys();
        Color[] colors = event.getColors();

        if (keys == null || colors == null || !keys.equals(keysOrigin)||!colors.equals(colorsOrigin)) {
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

    private void resetCurrentLayerSettingGrid(double[] keys,Color[] colors){
        if (this.currentLayerSettingGrid!=null){
            ColorDictionary colorDictionary=this.currentLayerSettingGrid.getColorDictionary();
            colorDictionary.clear();
            for (int i = 0; i < colors.length; i++) {
                colorDictionary.setColor(keys[i], colors[i]);
            }
            this.currentLayerSettingGrid.setColorDictionary(colorDictionary);
        }
    }

    public LayerSettingGrid getCurrentLayerSettingGrid(){
        return this.currentLayerSettingGrid;
    }
}

