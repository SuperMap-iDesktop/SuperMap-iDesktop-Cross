package com.supermap.desktop.CtrlAction.Edit.SnapSetting;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TableTooltipCellRenderer;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.mapping.SnapMode;
import com.supermap.mapping.SnapSetting;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

/**
 * Created by xie on 2016/12/7.
 */
public class SnapSettingDialog extends SmDialog {
    private JTabbedPane tabbedPane;
    private JPanel panelSnapMode;
    private JPanel panelSnapParams;
    private SnapSetting defaultSnapSetting;
    private JScrollPane scrollPaneSnapMode;
    private JTable tableSnapMode;
    private JButton buttonMoveNext;
    private JButton buttonMoveForward;
    private JButton buttonMoveFrist;
    private JButton buttonMoveLast;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton buttonRecover;
    private JLabel labelSnapTolarence;
    private WarningTextFeild textFieldSnapTolarence;
    private JLabel labelFixedAngle;
    private WarningTextFeild textFieldFixedAngle;
    private JLabel labelMaxSnappedCount;
    private WarningTextFeild textFieldMaxSnappedCount;
    private JLabel labelFixedLength;
    private WarningTextFeild textFieldFixedLength;
    private JLabel labelMinSnappedLength;
    private WarningTextFeild textFieldMinSnappedLength;
    private JCheckBox checkBoxSnappedLineBroken;
    private JPanel panelTolarenceView;
    private String[] tableTitle = {"", CommonProperties.getString("String_Type"),
            ControlsProperties.getString("String_Description")};
    private static final int TABLE_COLUMN_CHECKABLE = 0;
    private static final int TABLE_COLUMN_TYPE = 1;
    private static final int TABLE_COLUMN_DESCRIPTION = 2;

    private static final int POINT_ON_ENDPOINT = 0, POINT_ON_MIDPOINT = 1, POINT_ON_LINE = 2,
            POINT_ON_POINT = 3, POINT_ON_EXTENSION = 4, LINE_WITH_HORIZONTAL = 5,
            LINE_WITH_VERTICAL = 6, LINE_WITH_PARALLEL = 7, LINE_WITH_PERPENDICULAR = 8,
            LINE_WITH_FIXED_ANGLE = 9, LINE_WITH_FIXED_LENGTH = 10;

    public SnapSettingDialog(SnapSetting snapSetting) {
        this.defaultSnapSetting = new SnapSetting(snapSetting);
    }

    private void init() {
        initComponents();
        initLayout();
        initResouces();
        registEvents();
    }

    private void registEvents() {
        removeEvents();
    }

    private void removeEvents() {
    }

    private void initResouces() {
        this.setTitle(DataEditorProperties.getString("String_SnapSetting"));
        this.labelSnapTolarence.setText(DataEditorProperties.getString("String_SnapSettingTolerance"));
        this.labelFixedAngle.setText(DataEditorProperties.getString("String_SnapSettingFixedAngle"));
        this.labelFixedLength.setText(DataEditorProperties.getString("String_SnapSettingFixedLength"));
        this.labelMaxSnappedCount.setText(DataEditorProperties.getString("String_SnapSettingMaxSnappedCount"));
        this.labelMinSnappedLength.setText(DataEditorProperties.getString("String_SnapSettingMinSnappedLength"));
        this.checkBoxSnappedLineBroken.setText(DataEditorProperties.getString("String_SnapSettingLineBroken"));
        this.buttonRecover.setText(DataEditorProperties.getString("String_SnapSettingResume"));
        this.buttonMoveForward.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_MoveForward.png"));
        this.buttonMoveFrist.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_MoveFrist.png"));
        this.buttonMoveLast.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_MoveLast.png"));
        this.buttonMoveNext.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_MoveNext.png"));
    }

    private void initLayout() {
        this.setSize(new Dimension(560, 420));
        this.setLocationRelativeTo(null);
        JPanel panelButton = new JPanel();
        panelButton.setLayout(new GridBagLayout());
        panelButton.add(this.buttonRecover, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 0, 10, 5));
        panelButton.add(this.buttonOK, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(5, 0, 10, 5));
        panelButton.add(this.buttonCancel, new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(5, 0, 10, 10));

        this.setLayout(new GridBagLayout());
        this.add(tabbedPane, new GridBagConstraintsHelper(0, 0, 4, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        this.add(panelButton, new GridBagConstraintsHelper(0, 1, 4, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0));

        this.tabbedPane.addTab(DataEditorProperties.getString("String_SnapSetting_TabPageType"), panelSnapMode);
        this.tabbedPane.addTab(DataEditorProperties.getString("String_SnapSetting_TabPageParameters"), panelSnapParams);
        this.tabbedPane.setBorder(null);
        initPanelSnapModeLayout();
        initPanelSnapParamsLayout();
    }

    private void initPanelSnapParamsLayout() {
        JPanel panelSnapParamsDisplay = new JPanel();

        panelSnapParamsDisplay.setLayout(new GridBagLayout());
        panelSnapParamsDisplay.add(this.labelSnapTolarence, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(20, 20, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelSnapParamsDisplay.add(this.textFieldSnapTolarence, new GridBagConstraintsHelper(1, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(20, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        panelSnapParamsDisplay.add(this.labelFixedAngle, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 20, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelSnapParamsDisplay.add(this.textFieldFixedAngle, new GridBagConstraintsHelper(1, 1, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        panelSnapParamsDisplay.add(this.labelMaxSnappedCount, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 20, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelSnapParamsDisplay.add(this.textFieldMaxSnappedCount, new GridBagConstraintsHelper(1, 2, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        panelSnapParamsDisplay.add(this.labelFixedLength, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 20, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelSnapParamsDisplay.add(this.textFieldFixedLength, new GridBagConstraintsHelper(1, 3, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        panelSnapParamsDisplay.add(this.labelMinSnappedLength, new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 20, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelSnapParamsDisplay.add(this.textFieldMinSnappedLength, new GridBagConstraintsHelper(1, 4, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        panelSnapParamsDisplay.add(this.checkBoxSnappedLineBroken, new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 20, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));

        this.panelSnapParams.setLayout(new GridBagLayout());
        this.panelSnapParams.add(panelSnapParamsDisplay, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        this.panelSnapParams.add(panelTolarenceView, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setInsets(20, 0, 0, 0).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.panelSnapParams.add(new JPanel(), new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        this.panelSnapParams.add(new JPanel(), new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        this.panelTolarenceView.setPreferredSize(new Dimension(100, 100));
        this.panelTolarenceView.setBorder(new LineBorder(Color.gray));
    }

    private void initPanelSnapModeLayout() {
        this.panelSnapMode.setLayout(new GridBagLayout());
        JPanel panelModeDisplay = new JPanel();
        JPanel panelButtonDisplay = new JPanel();
        this.panelSnapMode.add(panelModeDisplay, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(20).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        panelModeDisplay.setLayout(new GridBagLayout());
        panelModeDisplay.add(scrollPaneSnapMode, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        panelModeDisplay.add(panelButtonDisplay, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(0, 1));

        panelButtonDisplay.setLayout(new GridBagLayout());
        panelButtonDisplay.add(this.buttonMoveFrist, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelButtonDisplay.add(this.buttonMoveForward, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        panelButtonDisplay.add(this.buttonMoveNext, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelButtonDisplay.add(this.buttonMoveLast, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        this.scrollPaneSnapMode.setViewportView(tableSnapMode);
        this.scrollPaneSnapMode.setBackground(Color.white);
        this.tableSnapMode.setBackground(Color.white);
        initTable();
    }

    private void initTable() {
        CheckableTableModle checkTableModle = new CheckableTableModle(new Object[11][3], tableTitle, 0);
        this.tableSnapMode.setModel(checkTableModle);
        for (int i = 0; i < 11; i++) {
            CheckableItem item = new CheckableItem();
            initItem(item, i);
            this.tableSnapMode.setValueAt(item, i, TABLE_COLUMN_CHECKABLE);
            this.tableSnapMode.setValueAt(getSnapMode(i, TABLE_COLUMN_TYPE), i, TABLE_COLUMN_TYPE);
            this.tableSnapMode.setValueAt(getSnapMode(i, TABLE_COLUMN_DESCRIPTION), i, TABLE_COLUMN_DESCRIPTION);
        }
        this.tableSnapMode.getColumn(tableSnapMode.getModel().getColumnName(TABLE_COLUMN_CHECKABLE)).setMaxWidth(100);
        this.tableSnapMode.getColumn(tableSnapMode.getModel().getColumnName(TABLE_COLUMN_CHECKABLE)).setCellRenderer(new CheckTableRenderer());
        this.tableSnapMode.getColumn(tableSnapMode.getModel().getColumnName(TABLE_COLUMN_DESCRIPTION)).setCellRenderer(TableTooltipCellRenderer.getInstance());
        this.tableSnapMode.getTableHeader().setDefaultRenderer(new CheckableHeaderCellRenderer(tableSnapMode, DataEditorProperties.getString("String_Enabled")));
        this.tableSnapMode.setRowHeight(23);
        this.tableSnapMode.setShowHorizontalLines(false);
        this.tableSnapMode.setShowVerticalLines(false);
    }

    private void initItem(CheckableItem item, int row) {
        switch (row) {
            case POINT_ON_ENDPOINT:
                item.setSelected(defaultSnapSetting.get(SnapMode.POINT_ON_ENDPOINT));
                item.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_PointOnEndpoint.png"));
                break;
            case POINT_ON_MIDPOINT:
                item.setSelected(defaultSnapSetting.get(SnapMode.POINT_ON_MIDPOINT));
                item.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_PointOnMidpoint.png"));
                break;
            case POINT_ON_LINE:
                item.setSelected(defaultSnapSetting.get(SnapMode.POINT_ON_LINE));
                item.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_PointOnLine.png"));
                break;
            case POINT_ON_POINT:
                item.setSelected(defaultSnapSetting.get(SnapMode.POINT_ON_POINT));
                item.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_PointOnPoint.png"));
                break;
            case POINT_ON_EXTENSION:
                item.setSelected(defaultSnapSetting.get(SnapMode.POINT_ON_EXTENSION));
                item.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_PointOnExtension.png"));
                break;
            case LINE_WITH_HORIZONTAL:
                item.setSelected(defaultSnapSetting.get(SnapMode.LINE_WITH_HORIZONTAL));
                item.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_LineWithHorizontal.png"));
                break;
            case LINE_WITH_VERTICAL:
                item.setSelected(defaultSnapSetting.get(SnapMode.LINE_WITH_VERTICAL));
                item.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_LineWithVertical.png"));
                break;
            case LINE_WITH_PARALLEL:
                item.setSelected(defaultSnapSetting.get(SnapMode.LINE_WITH_PARALLEL));
                item.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_LineWithParallel.png"));
                break;
            case LINE_WITH_PERPENDICULAR:
                item.setSelected(defaultSnapSetting.get(SnapMode.LINE_WITH_PERPENDICULAR));
                item.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_LineWithPerpendicular.png"));
                break;
            case LINE_WITH_FIXED_ANGLE:
                item.setSelected(defaultSnapSetting.get(SnapMode.LINE_WITH_FIXED_ANGLE));
                item.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_LineWithFixedAngle.png"));
                break;
            case LINE_WITH_FIXED_LENGTH:
                item.setSelected(defaultSnapSetting.get(SnapMode.LINE_WITH_FIXED_LENGTH));
                item.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_LineWithFixedLength.png"));
                break;
            default:
                break;

        }
    }

    class CheckableHeaderCellRenderer implements TableCellRenderer {
        CheckableTableModle tableModel;
        JTableHeader tableHeader;
        final JCheckBox checkBox;

        public CheckableHeaderCellRenderer(JTable table, String title) {
            this.tableModel = (CheckableTableModle) table.getModel();
            this.tableHeader = table.getTableHeader();
            this.checkBox = new JCheckBox(tableModel.getColumnName(0));
            this.checkBox.setSelected(false);
            this.checkBox.setText(title);
            this.tableHeader.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() > 0) {
                        //获得选中列
                        int selectColumn = tableHeader.columnAtPoint(e.getPoint());
                        if (selectColumn == 0) {
                            boolean value = !checkBox.isSelected();
                            checkBox.setSelected(value);
                            tableModel.selectAllOrNull(value);
                            tableHeader.repaint();
                        }
                    }
                }
            });
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            String valueStr = (String) value;
            JLabel label = new JLabel(valueStr);
            label.setHorizontalAlignment(SwingConstants.LEFT); // 表头标签居左边
            this.checkBox.setHorizontalAlignment(SwingConstants.LEFT);// 表头checkBox居中
            checkBox.setBorderPainted(true);
            JComponent component = (column == 0) ? checkBox : label;

            component.setForeground(tableHeader.getForeground());
            component.setBackground(tableHeader.getBackground());
            component.setFont(tableHeader.getFont());
            component.setBorder(UIManager.getBorder("TableHeader.cellBorder"));

            return component;
        }

    }

    class CheckableItem {
        private Icon icon;
        private boolean isSelected;

        public CheckableItem() {
        }

        public void setSelected(boolean b) {
            this.isSelected = b;
        }

        public boolean isSelected() {
            return this.isSelected;
        }

        public Icon getIcon() {
            return icon;
        }

        public void setIcon(Icon icon) {
            this.icon = icon;
        }
    }

    class CheckableTableModle extends DefaultTableModel {
        private int enableColumn;

        public CheckableTableModle(Object[][] data, Object[] columnNames, int enableColumn) {
            super(data, columnNames);
            this.enableColumn = enableColumn;
        }


        // /**
        // * 根据类型返回显示控件
        // * 布尔类型返回显示checkbox
        // */
        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (enableColumn == columnIndex) {
                return true;
            }
            return false;
        }

        public void selectAllOrNull(boolean value) {
            for (int i = 0; i < getRowCount(); i++) {
                CheckableItem item = (CheckableItem) this.getValueAt(i, 0);
                item.setSelected(value);
                this.setValueAt(item, i, 0);
            }
            tableSnapMode.updateUI();
        }

    }


    class CheckTableRenderer implements TableCellRenderer {

        public CheckTableRenderer() {
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel result = initRenderPanel(table, value);
            if (isSelected) {
                result.setOpaque(true);
                result.setBackground(new Color(51, 153, 255));
                result.setForeground(Color.white);
            } else {
                result.setOpaque(true);
                result.setBackground(Color.white);
                result.setForeground(Color.black);
            }
            return result;
        }

        private JPanel initRenderPanel(JTable table, Object value) {
            JPanel panelContent = new JPanel();
            JCheckBox checkBox = new JCheckBox();
            JLabel label = new JLabel();
            if (value instanceof CheckableItem) {
                checkBox.setSelected(((CheckableItem) value).isSelected());
                label.setIcon(((CheckableItem) value).getIcon());
            }
            panelContent.setLayout(new GridBagLayout());
            panelContent.setLayout(new FlowLayout(FlowLayout.LEFT));
            panelContent.add(checkBox);
            panelContent.add(label);
            label.setFont(table.getFont());
            setComponentTheme(panelContent);
            setComponentTheme(checkBox);
            setComponentTheme(label);
            return panelContent;
        }

        private void setComponentTheme(JComponent component) {
            component.setBackground(UIManager.getColor("List.textBackground"));
            component.setForeground(UIManager.getColor("List.textForeground"));
        }
    }


    private Object getSnapMode(int i, int tableColumn) {
        Object result = "";
        switch (i) {
            case POINT_ON_ENDPOINT:
                if (tableColumn == TABLE_COLUMN_TYPE) {
                    result = DataEditorProperties.getString("String_Point_On_EndPoint");
                } else {
                    result = DataEditorProperties.getString("String_POINT_ON_ENDPOINT");
                }
                break;
            case POINT_ON_MIDPOINT:
                if (tableColumn == TABLE_COLUMN_TYPE) {
                    result = DataEditorProperties.getString("String_Point_On_MidPoint");
                } else {
                    result = DataEditorProperties.getString("String_POINT_ON_MIDPOINT");
                }
                break;
            case POINT_ON_LINE:
                if (tableColumn == TABLE_COLUMN_TYPE) {
                    result = DataEditorProperties.getString("String_Point_On_Line");
                } else {
                    result = DataEditorProperties.getString("String_POINT_ON_LINE");
                }
                break;
            case POINT_ON_POINT:
                if (tableColumn == TABLE_COLUMN_TYPE) {
                    result = DataEditorProperties.getString("String_Point_On_Point");
                } else {
                    result = DataEditorProperties.getString("String_POINT_ON_POINT");
                }
                break;
            case POINT_ON_EXTENSION:
                if (tableColumn == TABLE_COLUMN_TYPE) {
                    result = DataEditorProperties.getString("String_Point_On_Extension");
                } else {
                    result = DataEditorProperties.getString("String_POINT_ON_EXTENSION");
                }
                break;
            case LINE_WITH_HORIZONTAL:
                if (tableColumn == TABLE_COLUMN_TYPE) {
                    result = DataEditorProperties.getString("String_Line_With_Horizontal");
                } else {
                    result = DataEditorProperties.getString("String_LINE_WITH_HORIZONTAL");
                }
                break;
            case LINE_WITH_VERTICAL:
                if (tableColumn == TABLE_COLUMN_TYPE) {
                    result = DataEditorProperties.getString("String_Line_With_Vertical");
                } else {
                    result = DataEditorProperties.getString("String_LINE_WITH_VERTICAL");
                }
                break;
            case LINE_WITH_PARALLEL:
                if (tableColumn == TABLE_COLUMN_TYPE) {
                    result = DataEditorProperties.getString("String_Line_With_Parallel");
                } else {
                    result = DataEditorProperties.getString("String_LINE_WITH_PARALLEL");
                }
                break;
            case LINE_WITH_PERPENDICULAR:
                if (tableColumn == TABLE_COLUMN_TYPE) {
                    result = DataEditorProperties.getString("String_Line_With_Perpendicular");
                } else {
                    result = DataEditorProperties.getString("String_LINE_WITH_PERPENDICULAR");
                }
                break;
            case LINE_WITH_FIXED_ANGLE:
                if (tableColumn == TABLE_COLUMN_TYPE) {
                    result = DataEditorProperties.getString("String_Line_With_Fixed_Angle");
                } else {
                    result = DataEditorProperties.getString("String_LINE_WITH_FIXED_ANGLE");
                }
                break;
            case LINE_WITH_FIXED_LENGTH:
                if (tableColumn == TABLE_COLUMN_TYPE) {
                    result = DataEditorProperties.getString("String_Line_With_Fixed_Length");
                } else {
                    result = DataEditorProperties.getString("String_LINE_WITH_FIXED_LENGTH");
                }
                break;
            default:
                break;

        }
        return result;
    }

    private void initComponents() {
        this.tabbedPane = new JTabbedPane();
        this.panelSnapMode = new JPanel();
        this.panelSnapParams = new JPanel();
        this.panelTolarenceView = new JPanel();
        this.scrollPaneSnapMode = new JScrollPane();
        this.tableSnapMode = new JTable();
        this.buttonMoveNext = new JButton();
        this.buttonMoveForward = new JButton();
        this.buttonMoveFrist = new JButton();
        this.buttonMoveLast = new JButton();
        this.buttonOK = ComponentFactory.createButtonOK();
        this.buttonCancel = ComponentFactory.createButtonCancel();
        this.buttonRecover = new SmButton();
        this.labelSnapTolarence = new JLabel();
        DecimalFormat format = new DecimalFormat("0");
        this.textFieldSnapTolarence = new WarningTextFeild(format.format(defaultSnapSetting.getTolerance()));
        this.textFieldSnapTolarence.setInitInfo(1, 20, 0, "null");
        this.labelFixedAngle = new JLabel();
        this.textFieldFixedAngle = new WarningTextFeild(format.format(defaultSnapSetting.getFixedAngle()));
        this.textFieldFixedAngle.setInitInfo(0, 360, 1, "2");
        this.labelMaxSnappedCount = new JLabel();
        this.textFieldMaxSnappedCount = new WarningTextFeild(format.format(defaultSnapSetting.getMaxSnappedCount()));
        this.labelFixedLength = new JLabel();
        this.textFieldFixedLength = new WarningTextFeild(format.format(defaultSnapSetting.getFixedLength()));
        this.textFieldFixedLength.setInitInfo(20, 5000, 0, "null");
        this.labelMinSnappedLength = new JLabel();
        this.textFieldMinSnappedLength = new WarningTextFeild(format.format(defaultSnapSetting.getMinSnappedLength()));
        this.textFieldMinSnappedLength.setInitInfo(1, 120, 0, "null");
        this.checkBoxSnappedLineBroken = new JCheckBox();

    }

    public DialogResult showDialog() {
        init();
        setVisible(true);
        return dialogResult;
    }
}
