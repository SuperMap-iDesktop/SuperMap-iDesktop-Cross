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
import com.supermap.desktop.ui.controls.TextFields.RightValueListener;
import com.supermap.desktop.ui.controls.TextFields.WaringTextField;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.mapping.SnapMode;
import com.supermap.mapping.SnapSetting;
import com.supermap.ui.MapControl;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

/**
 * Created by xie on 2016/12/7.
 */
public class SnapSettingDialog extends SmDialog {
    private JTabbedPane tabbedPane;
    private JPanel panelSnapMode;
    private JPanel panelSnapParams;
    private SnapSetting mapControlSnapSetting;
    private SnapSetting srcSnapSetting;
    private MapControl mapControl;
    private JScrollPane scrollPaneSnapMode;
    private JTable tableSnapMode;
    private JButton buttonMoveToNext;
    private JButton buttonMoveToForward;
    private JButton buttonMoveToFrist;
    private JButton buttonMoveToLast;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton buttonRecover;
    private JLabel labelSnapTolarence;
    private WaringTextField textFieldSnapTolarence;
    private JLabel labelFixedAngle;
    private WaringTextField textFieldFixedAngle;
    //    private JLabel labelMaxSnappedCount;
//    private WaringTextField textFieldMaxSnappedCount;
    private JLabel labelFixedLength;
    private WaringTextField textFieldFixedLength;
    private JLabel labelMinSnappedLength;
    private WaringTextField textFieldMinSnappedLength;
    private JCheckBox checkBoxSnappedLineBroken;
    private JPanel panelTolarenceView;
    private JLabel labelImageCicle;
    private JLabel labelImageArraw;
    private String[] tableTitle = {"", CommonProperties.getString("String_Type"),
            ControlsProperties.getString("String_Description")};
    private static final int TABLE_COLUMN_CHECKABLE = 0;
    private static final int TABLE_COLUMN_TYPE = 1;
    private static final int TABLE_COLUMN_DESCRIPTION = 2;
    private static final DecimalFormat format = new DecimalFormat("0.#######");
    private boolean isRecoverSnapMode = false;
    private boolean isRecoverSnapParams = false;

    private MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int row = tableSnapMode.getSelectedRow();
            int column = tableSnapMode.getSelectedColumn();
            if (1 == e.getClickCount() && column == TABLE_COLUMN_CHECKABLE) {
                CheckableItem item = (CheckableItem) tableSnapMode.getModel().getValueAt(row, column);
                boolean value = !item.isSelected();
                item.setSelected(value);
                tableSnapMode.setValueAt(item, row, column);
                setSnapMode(row, value);
            }
        }
    };
    private ActionListener recoverListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            srcSnapSetting = SnapSettingUtilities.parseDefaultSnapSetting(mapControl);
            if (panelSnapMode.isVisible()) {
                initTable();
                isRecoverSnapMode = true;
            } else {
                textFieldSnapTolarence.setText(format.format(srcSnapSetting.getTolerance()));
                textFieldFixedAngle.setText(format.format(srcSnapSetting.getFixedAngle()));
//            textFieldMaxSnappedCount.setText(format.format(srcSnapSetting.getMaxSnappedCount()));
                textFieldMinSnappedLength.setText(format.format(srcSnapSetting.getMinSnappedLength()));
                textFieldFixedLength.setText(format.format(srcSnapSetting.getFixedLength()));
                checkBoxSnappedLineBroken.setSelected(srcSnapSetting.isSnappedLineBroken());
                isRecoverSnapParams = true;
            }
        }
    };
    private ActionListener cancelListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            isRecoverSnapMode = false;
            isRecoverSnapParams = false;
            dispose();
        }
    };
    private ActionListener moveListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(buttonMoveToFrist)) {
                //移动到最上端
                moveToFrist();
            } else if (e.getSource().equals(buttonMoveToForward)) {
                //上移一位
                moveToForward();
            } else if (e.getSource().equals(buttonMoveToNext)) {
                //下移一位
                moveToNext();
            } else if (e.getSource().equals(buttonMoveToLast)) {
                moveToLast();
            }
        }

        private void moveToFrist() {
            int[] selectRows = tableSnapMode.getSelectedRows();
            int size = selectRows.length;
            if (0 == size) {
                return;
            } else if (1 <= size && 0 == selectRows[0]) {
                return;
            } else {
                //交换到第一个时需要进行双层遍历，且时时修改selectRows才能实现整体位移
                int exchangeSize = selectRows[0] - 1;
                for (int i = exchangeSize; i >= 0; i--) {
                    int row = i;
                    for (int j = 0; j < size; j++) {
                        exchangeItem(selectRows[j], row);
                        selectRows[j] = row;
                        row++;
                    }
                }
                tableSnapMode.clearSelection();
                for (int i = 0; i < size; i++) {
                    tableSnapMode.addRowSelectionInterval(i, i);
                }
                scrollToFrist();
            }
        }

        private void moveToLast() {
            int[] selectRows = tableSnapMode.getSelectedRows();
            int size = selectRows.length;
            if (0 == size) {
                return;
            } else if (1 <= size && tableSnapMode.getRowCount() - 1 == selectRows[size - 1]) {
                return;
            } else {
                //交换到最后一个时需要进行双层遍历，且时时修改selectRows才能实现整体位移,
                //方法和移动到第一个相似，逆序
                int exchangeSize = selectRows[size - 1] + 1;
                int rowCount = tableSnapMode.getRowCount() - 1;
                for (int i = exchangeSize; i <= rowCount; i++) {
                    int row = i;
                    for (int j = size - 1; j >= 0; j--) {
                        exchangeItem(selectRows[j], row);
                        selectRows[j] = row;
                        row--;
                    }
                }
                tableSnapMode.clearSelection();
                for (int i = 0; i < size; i++) {
                    tableSnapMode.addRowSelectionInterval(rowCount - i, rowCount - i);
                }
                scrollToLast();
            }
        }

        private void moveToNext() {
            int[] selectRows = tableSnapMode.getSelectedRows();
            int size = selectRows.length;
            if (0 == size) {
                return;
            } else if (1 <= size && tableSnapMode.getRowCount() - 1 == selectRows[size - 1]) {
                return;
            } else {
                int index = selectRows[size - 1];
                int rowLocation = index + 1;
                int newSize = index - selectRows[0] + 1;
                for (int i = 0; i < newSize; i++) {
                    exchangeItem(index, rowLocation);
                    index--;
                    rowLocation--;
                }

                tableSnapMode.clearSelection();
                for (int i = 0; i < size; i++) {
                    tableSnapMode.addRowSelectionInterval(selectRows[i] + 1, selectRows[i] + 1);
                }
                int length = tableSnapMode.getSelectedRows().length;
                if (length > 0 && tableSnapMode.getSelectedRows()[length - 1] == tableSnapMode.getRowCount() - 1) {
                    scrollToLast();
                }
            }
        }

        private void moveToForward() {
            int[] selectRows = tableSnapMode.getSelectedRows();
            int size = selectRows.length;
            if (0 == size) {
                return;
            } else if (1 <= size && 0 == selectRows[0]) {
                return;
            } else {
                int index = selectRows[0];
                int rowLocation = index - 1;
                int newSize = selectRows[size - 1] - index + 1;
                for (int i = 0; i < newSize; i++) {
                    exchangeItem(index, rowLocation);
                    index++;
                    rowLocation++;
                }
                tableSnapMode.clearSelection();
                for (int i = 0; i < size; i++) {
                    tableSnapMode.addRowSelectionInterval(selectRows[i] - 1, selectRows[i] - 1);
                }
                if (tableSnapMode.getSelectedRows().length > 0 && tableSnapMode.getSelectedRows()[0] == 0) {
                    scrollToFrist();
                }
            }

        }

        private void exchangeItem(int from, int target) {
            CheckableItem item = (CheckableItem) tableSnapMode.getValueAt(from, TABLE_COLUMN_CHECKABLE);
            CheckableItem targetItem = (CheckableItem) tableSnapMode.getValueAt(target, TABLE_COLUMN_CHECKABLE);
            String snapType = (String) tableSnapMode.getValueAt(from, TABLE_COLUMN_TYPE);
            String targetSnapType = (String) tableSnapMode.getValueAt(target, TABLE_COLUMN_TYPE);
            String snapDescription = (String) tableSnapMode.getValueAt(from, TABLE_COLUMN_DESCRIPTION);
            String targetSnapDescription = (String) tableSnapMode.getValueAt(target, TABLE_COLUMN_DESCRIPTION);
            tableSnapMode.setValueAt(targetSnapDescription, from, TABLE_COLUMN_DESCRIPTION);
            tableSnapMode.setValueAt(snapDescription, target, TABLE_COLUMN_DESCRIPTION);
            tableSnapMode.setValueAt(targetSnapType, from, TABLE_COLUMN_TYPE);
            tableSnapMode.setValueAt(snapType, target, TABLE_COLUMN_TYPE);
            tableSnapMode.setValueAt(targetItem, from, TABLE_COLUMN_CHECKABLE);
            tableSnapMode.setValueAt(item, target, TABLE_COLUMN_CHECKABLE);
            mapControlSnapSetting.exchange(from, target);
        }
    };

    private void scrollToFrist() {
        Rectangle treeVisibleRectangle = new Rectangle(tableSnapMode.getVisibleRect().x, 0, tableSnapMode.getVisibleRect().width, tableSnapMode.getVisibleRect().height);
        tableSnapMode.scrollRectToVisible(treeVisibleRectangle);
    }

    private void scrollToLast() {
        Rectangle treeVisibleRectangle = new Rectangle(tableSnapMode.getVisibleRect().x, tableSnapMode.getVisibleRect().height, tableSnapMode.getVisibleRect().width, tableSnapMode.getVisibleRect().height);
        tableSnapMode.scrollRectToVisible(treeVisibleRectangle);
    }

    private RightValueListener tolarenceListener = new RightValueListener() {
        @Override
        public void update(String value) {
            int tolerance = Integer.parseInt(value);
            mapControlSnapSetting.setTolerance(tolerance);
            labelImageCicle.setIcon(buildIcon(tolerance));
        }
    };
    private RightValueListener fixedAngleListener = new RightValueListener() {
        @Override
        public void update(String value) {
            mapControlSnapSetting.setFixedAngle(Double.parseDouble(value));
        }
    };
    private RightValueListener maxSnappedCountListener = new RightValueListener() {
        @Override
        public void update(String value) {
            mapControlSnapSetting.setMaxSnappedCount(Integer.parseInt(value));
        }
    };
    private RightValueListener fixedLengthListener = new RightValueListener() {
        @Override
        public void update(String value) {
            mapControlSnapSetting.setFixedLength(Double.parseDouble(value));
        }
    };
    private RightValueListener minSnappedLengthListener = new RightValueListener() {
        @Override
        public void update(String value) {
            mapControlSnapSetting.setMinSnappedLength(Integer.parseInt(value));
        }
    };
    private ChangeListener snappedLineBrokenListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            mapControlSnapSetting.setSnappedLineBroken(checkBoxSnappedLineBroken.isSelected());
        }
    };
    private ActionListener okListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isRecoverSnapMode) {
                SnapSettingUtilities.replaceSnapMode(srcSnapSetting, mapControlSnapSetting);
            }
            if (isRecoverSnapParams) {
                mapControlSnapSetting.setTolerance(srcSnapSetting.getTolerance());
                mapControlSnapSetting.setFixedAngle(srcSnapSetting.getFixedAngle());
                mapControlSnapSetting.setFixedLength(srcSnapSetting.getFixedLength());
                mapControlSnapSetting.setMinSnappedLength(srcSnapSetting.getMinSnappedLength());
                mapControlSnapSetting.setSnappedLineBroken(srcSnapSetting.isSnappedLineBroken());
            }
            if (!SnapSettingUtilities.isSnapSettingFileExists()) {
                SnapSettingUtilities.createSnapSettingFile(mapControlSnapSetting);
            } else {
                SnapSettingUtilities.resetSnapSettingFile(mapControlSnapSetting);
            }
            mapControl.setSnapSetting(mapControlSnapSetting);
            SnapSettingUtilities.replaceSnapMode(mapControlSnapSetting, mapControl.getSnapSetting());
            removeEvents();
            isRecoverSnapMode = false;
            isRecoverSnapParams = false;
            dispose();
        }
    };

    public SnapSettingDialog(MapControl mapControl) {
        initSnapSetting(mapControl);
    }

    private void initSnapSetting(MapControl mapControl) {
        this.mapControl = mapControl;
        if (!SnapSettingUtilities.isDefaultSnapSettingFileExists()) {
            //如果DefaultSnapSetting.xml文件不存在，则创建该文件
            SnapSettingUtilities.createDefaultSnapSettingFile();
        }
        if (!SnapSettingUtilities.isDefaultSnapSettingExists(mapControl)) {
            //如果默认的SnapSetting不存在，则将该MapControl的SnapSetting添加到DefaultSnapSetting.xml文件中
            SnapSettingUtilities.addDefaultSnapSettingNode(mapControl);
        }
        if (SnapSettingUtilities.isSnapSettingFileExists()) {
            //如果已经有全局设置的SnapSetting，则直接取出来，并设置当前MapControl的SnapSetting为SnapSetting
            this.srcSnapSetting = SnapSettingUtilities.parseSnapSetting();
            this.mapControlSnapSetting = this.srcSnapSetting;
            mapControl.setSnapSetting(srcSnapSetting);
            SnapSettingUtilities.replaceSnapMode(srcSnapSetting, mapControl.getSnapSetting());
        } else {
            this.srcSnapSetting = new SnapSetting(mapControl.getSnapSetting());
            this.mapControlSnapSetting = this.srcSnapSetting;
        }
    }

    private void init() {
        initComponents();
        initLayout();
        initResouces();
        registEvents();
    }

    private void registEvents() {
        removeEvents();
        this.tableSnapMode.addMouseListener(this.mouseListener);
        this.buttonRecover.addActionListener(this.recoverListener);
        this.buttonCancel.addActionListener(this.cancelListener);
        this.buttonMoveToFrist.addActionListener(this.moveListener);
        this.buttonMoveToForward.addActionListener(this.moveListener);
        this.buttonMoveToNext.addActionListener(this.moveListener);
        this.buttonMoveToLast.addActionListener(this.moveListener);
        this.textFieldSnapTolarence.addRightValueListener(this.tolarenceListener);
        this.textFieldFixedAngle.addRightValueListener(this.fixedAngleListener);
//        this.textFieldMaxSnappedCount.addRightValueListener(this.maxSnappedCountListener);
        this.textFieldFixedLength.addRightValueListener(this.fixedLengthListener);
        this.textFieldMinSnappedLength.addRightValueListener(this.minSnappedLengthListener);
        this.checkBoxSnappedLineBroken.addChangeListener(this.snappedLineBrokenListener);
        this.buttonOK.addActionListener(this.okListener);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                removeEvents();
            }
        });
    }

    private void removeEvents() {
        this.tableSnapMode.removeMouseListener(this.mouseListener);
        this.buttonRecover.removeActionListener(this.recoverListener);
        this.buttonCancel.removeActionListener(this.cancelListener);
        this.buttonMoveToFrist.removeActionListener(this.moveListener);
        this.buttonMoveToForward.removeActionListener(this.moveListener);
        this.buttonMoveToNext.removeActionListener(this.moveListener);
        this.buttonMoveToLast.removeActionListener(this.moveListener);
        this.textFieldSnapTolarence.removeRightValueListener(this.tolarenceListener);
        this.textFieldFixedAngle.removeRightValueListener(this.fixedAngleListener);
//        this.textFieldMaxSnappedCount.removeRightValueListener(this.maxSnappedCountListener);
        this.textFieldFixedLength.removeRightValueListener(this.fixedLengthListener);
        this.textFieldMinSnappedLength.removeRightValueListener(this.minSnappedLengthListener);
        this.checkBoxSnappedLineBroken.removeChangeListener(this.snappedLineBrokenListener);
        this.buttonOK.removeActionListener(this.okListener);
    }

    private void initResouces() {
        this.setTitle(DataEditorProperties.getString("String_SnapSetting"));
        this.labelSnapTolarence.setText(DataEditorProperties.getString("String_SnapSettingTolerance"));
        this.labelFixedAngle.setText(DataEditorProperties.getString("String_SnapSettingFixedAngle"));
        this.labelFixedLength.setText(DataEditorProperties.getString("String_SnapSettingFixedLength"));
//        this.labelMaxSnappedCount.setText(DataEditorProperties.getString("String_SnapSettingMaxSnappedCount"));
        this.labelMinSnappedLength.setText(DataEditorProperties.getString("String_SnapSettingMinSnappedLength"));
        this.checkBoxSnappedLineBroken.setText(DataEditorProperties.getString("String_SnapSettingLineBroken"));
        this.buttonRecover.setText(DataEditorProperties.getString("String_SnapSettingResume"));
        this.buttonMoveToForward.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_MoveForward.png"));
        this.buttonMoveToFrist.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_MoveFrist.png"));
        this.buttonMoveToLast.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_MoveLast.png"));
        this.buttonMoveToNext.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_MoveNext.png"));
        this.buttonMoveToForward.setToolTipText(CommonProperties.getString(CommonProperties.moveToForward));
        this.buttonMoveToFrist.setToolTipText(CommonProperties.getString(CommonProperties.moveToFrist));
        this.buttonMoveToNext.setToolTipText(CommonProperties.getString(CommonProperties.moveToNext));
        this.buttonMoveToLast.setToolTipText(CommonProperties.getString(CommonProperties.moveToLast));
    }

    private void initLayout() {
        this.setSize(new Dimension(640, 420));
        this.setLocationRelativeTo(null);
        JPanel panelButton = new JPanel();
        panelButton.setLayout(new GridBagLayout());
        panelButton.add(this.buttonOK, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(10, 0, 10, 5));
        panelButton.add(this.buttonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(10, 0, 10, 10));

        this.setLayout(new GridBagLayout());
        this.add(tabbedPane, new GridBagConstraintsHelper(0, 0, 6, 1).setAnchor(GridBagConstraints.CENTER).setInsets(10, 8, 0, 5).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        this.add(buttonRecover, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 10, 10, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(panelButton, new GridBagConstraintsHelper(4, 1, 2, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0));

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
//        panelSnapParamsDisplay.add(this.labelMaxSnappedCount, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 20, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
//        panelSnapParamsDisplay.add(this.textFieldMaxSnappedCount, new GridBagConstraintsHelper(1, 2, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        panelSnapParamsDisplay.add(this.labelFixedLength, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 20, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelSnapParamsDisplay.add(this.textFieldFixedLength, new GridBagConstraintsHelper(1, 3, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        panelSnapParamsDisplay.add(this.labelMinSnappedLength, new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 20, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelSnapParamsDisplay.add(this.textFieldMinSnappedLength, new GridBagConstraintsHelper(1, 4, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        panelSnapParamsDisplay.add(this.checkBoxSnappedLineBroken, new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 16, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));

        this.panelSnapParams.setLayout(new GridBagLayout());
        this.panelSnapParams.add(panelSnapParamsDisplay, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        this.panelSnapParams.add(panelTolarenceView, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setInsets(20, 0, 0, 0).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.panelSnapParams.add(new JPanel(), new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        this.panelSnapParams.add(new JPanel(), new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        this.panelTolarenceView.setPreferredSize(new Dimension(100, 100));
        this.panelTolarenceView.setBorder(new LineBorder(Color.gray));

        this.panelTolarenceView.setLayout(new GridBagLayout());
        this.labelImageArraw.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Select.png"));
        this.panelTolarenceView.add(labelImageArraw, new GridBagConstraintsHelper(0, 0, 1, 1));
        this.panelTolarenceView.add(labelImageCicle, new GridBagConstraintsHelper(0, 0, 1, 1));
        this.labelImageCicle.setIcon(buildIcon(mapControlSnapSetting.getTolerance()));
    }

    private ImageIcon buildIcon(int width) {
        ImageIcon colorIcon = new ImageIcon();
        BufferedImage bufferedImage = new BufferedImage(98, 98, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.createGraphics();
        g.setColor(panelTolarenceView.getBackground());
        g.fillRect(0, 0, 98, 98);//填充整个屏幕
        g.setColor(Color.red);
        int pointx = 43;
        int pointy = 42;
        if (width > 2) {
            pointx = pointx - width / 2;
            pointy = pointy - width / 2;
        }
        g.drawOval(pointx, pointy, width, width);
        colorIcon.setImage(bufferedImage);
        g.dispose();
        return colorIcon;
    }

    private void initPanelSnapModeLayout() {
        this.panelSnapMode.setLayout(new GridBagLayout());
        JPanel panelModeDisplay = new JPanel();
        JPanel panelButtonDisplay = new JPanel();
        this.panelSnapMode.add(panelModeDisplay, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(20, 20, 10, 0).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        panelModeDisplay.setLayout(new GridBagLayout());
        panelModeDisplay.add(scrollPaneSnapMode, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
        panelModeDisplay.add(panelButtonDisplay, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(0, 1));

        panelButtonDisplay.setLayout(new GridBagLayout());
        panelButtonDisplay.add(this.buttonMoveToFrist, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelButtonDisplay.add(this.buttonMoveToForward, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelButtonDisplay.add(this.buttonMoveToNext, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelButtonDisplay.add(this.buttonMoveToLast, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.buttonMoveToFrist.setPreferredSize(new Dimension(36, 36));
        this.buttonMoveToForward.setPreferredSize(new Dimension(36, 36));
        this.buttonMoveToNext.setPreferredSize(new Dimension(36, 36));
        this.buttonMoveToLast.setPreferredSize(new Dimension(36, 36));
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
        this.tableSnapMode.getColumn(tableSnapMode.getModel().getColumnName(TABLE_COLUMN_DESCRIPTION)).setMinWidth(220);
        this.tableSnapMode.getColumn(tableSnapMode.getModel().getColumnName(TABLE_COLUMN_DESCRIPTION)).setCellRenderer(TableTooltipCellRenderer.getInstance());
        this.tableSnapMode.getTableHeader().setDefaultRenderer(new CheckableHeaderCellRenderer(tableSnapMode, DataEditorProperties.getString("String_Enabled")));
        this.tableSnapMode.setRowHeight(23);
        this.tableSnapMode.setShowHorizontalLines(false);
        this.tableSnapMode.setShowVerticalLines(false);
        this.tableSnapMode.addRowSelectionInterval(0, 0);
    }

    private void setSnapModeAll(boolean value) {
        mapControlSnapSetting.set(SnapMode.POINT_ON_ENDPOINT, value);
        mapControlSnapSetting.set(SnapMode.POINT_ON_MIDPOINT, value);
        mapControlSnapSetting.set(SnapMode.POINT_ON_LINE, value);
        mapControlSnapSetting.set(SnapMode.POINT_ON_POINT, value);
        mapControlSnapSetting.set(SnapMode.POINT_ON_EXTENSION, value);
        mapControlSnapSetting.set(SnapMode.LINE_WITH_HORIZONTAL, value);
        mapControlSnapSetting.set(SnapMode.LINE_WITH_VERTICAL, value);
        mapControlSnapSetting.set(SnapMode.LINE_WITH_PARALLEL, value);
        mapControlSnapSetting.set(SnapMode.LINE_WITH_PERPENDICULAR, value);
        mapControlSnapSetting.set(SnapMode.LINE_WITH_FIXED_ANGLE, value);
        mapControlSnapSetting.set(SnapMode.LINE_WITH_FIXED_LENGTH, value);
    }

    private void setSnapMode(int row, boolean value) {
        SnapMode mode = srcSnapSetting.getAt(row);
        mapControlSnapSetting.set(mode, value);
    }

    private void initItem(CheckableItem item, int row) {
        SnapMode mode = srcSnapSetting.getAt(row);
        item.setMode(mode);
        if (mode == SnapMode.POINT_ON_ENDPOINT) {
            item.setSelected(srcSnapSetting.get(SnapMode.POINT_ON_ENDPOINT));
            item.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_PointOnEndpoint.png"));
        } else if (mode == SnapMode.POINT_ON_MIDPOINT) {
            item.setSelected(srcSnapSetting.get(SnapMode.POINT_ON_MIDPOINT));
            item.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_PointOnMidpoint.png"));
        } else if (mode == SnapMode.POINT_ON_LINE) {
            item.setSelected(srcSnapSetting.get(SnapMode.POINT_ON_LINE));
            item.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_PointOnLine.png"));
        } else if (mode == SnapMode.POINT_ON_POINT) {
            item.setSelected(srcSnapSetting.get(SnapMode.POINT_ON_POINT));
            item.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_PointOnPoint.png"));
        } else if (mode == SnapMode.POINT_ON_EXTENSION) {
            item.setSelected(srcSnapSetting.get(SnapMode.POINT_ON_EXTENSION));
            item.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_PointOnExtension.png"));
        } else if (mode == SnapMode.LINE_WITH_HORIZONTAL) {
            item.setSelected(srcSnapSetting.get(SnapMode.LINE_WITH_HORIZONTAL));
            item.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_LineWithHorizontal.png"));
        } else if (mode == SnapMode.LINE_WITH_VERTICAL) {
            item.setSelected(srcSnapSetting.get(SnapMode.LINE_WITH_VERTICAL));
            item.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_LineWithVertical.png"));
        } else if (mode == SnapMode.LINE_WITH_PARALLEL) {
            item.setSelected(srcSnapSetting.get(SnapMode.LINE_WITH_PARALLEL));
            item.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_LineWithParallel.png"));
        } else if (mode == SnapMode.LINE_WITH_PERPENDICULAR) {
            item.setSelected(srcSnapSetting.get(SnapMode.LINE_WITH_PERPENDICULAR));
            item.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_LineWithPerpendicular.png"));
        } else if (mode == SnapMode.LINE_WITH_FIXED_ANGLE) {
            item.setSelected(srcSnapSetting.get(SnapMode.LINE_WITH_FIXED_ANGLE));
            item.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_LineWithFixedAngle.png"));
        } else if (mode == SnapMode.LINE_WITH_FIXED_LENGTH) {
            item.setSelected(srcSnapSetting.get(SnapMode.LINE_WITH_FIXED_LENGTH));
            item.setIcon(ControlsResources.getIcon("/controlsresources/SnapSetting/Image_LineWithFixedLength.png"));
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
                            setSnapModeAll(value);
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
        private SnapMode mode;

        public CheckableItem() {
        }

        public SnapMode getMode() {
            return mode;
        }

        public void setMode(SnapMode mode) {
            this.mode = mode;
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


    private Object getSnapMode(int row, int tableColumn) {
        Object result = "";
        SnapMode mode = srcSnapSetting.getAt(row);
        if (mode == SnapMode.POINT_ON_ENDPOINT) {
            if (tableColumn == TABLE_COLUMN_TYPE) {
                result = DataEditorProperties.getString("String_Point_On_EndPoint");
            } else {
                result = DataEditorProperties.getString("String_POINT_ON_ENDPOINT");
            }
        } else if (mode == SnapMode.POINT_ON_MIDPOINT) {
            if (tableColumn == TABLE_COLUMN_TYPE) {
                result = DataEditorProperties.getString("String_Point_On_MidPoint");
            } else {
                result = DataEditorProperties.getString("String_POINT_ON_MIDPOINT");
            }
        } else if (mode == SnapMode.POINT_ON_LINE) {
            if (tableColumn == TABLE_COLUMN_TYPE) {
                result = DataEditorProperties.getString("String_Point_On_Line");
            } else {
                result = DataEditorProperties.getString("String_POINT_ON_LINE");
            }
        } else if (mode == SnapMode.POINT_ON_POINT) {
            if (tableColumn == TABLE_COLUMN_TYPE) {
                result = DataEditorProperties.getString("String_Point_On_Point");
            } else {
                result = DataEditorProperties.getString("String_POINT_ON_POINT");
            }
        } else if (mode == SnapMode.POINT_ON_EXTENSION) {
            if (tableColumn == TABLE_COLUMN_TYPE) {
                result = DataEditorProperties.getString("String_Point_On_Extension");
            } else {
                result = DataEditorProperties.getString("String_POINT_ON_EXTENSION");
            }
        } else if (mode == SnapMode.LINE_WITH_HORIZONTAL) {
            if (tableColumn == TABLE_COLUMN_TYPE) {
                result = DataEditorProperties.getString("String_Line_With_Horizontal");
            } else {
                result = DataEditorProperties.getString("String_LINE_WITH_HORIZONTAL");
            }
        } else if (mode == SnapMode.LINE_WITH_VERTICAL) {
            if (tableColumn == TABLE_COLUMN_TYPE) {
                result = DataEditorProperties.getString("String_Line_With_Vertical");
            } else {
                result = DataEditorProperties.getString("String_LINE_WITH_VERTICAL");
            }
        } else if (mode == SnapMode.LINE_WITH_PARALLEL) {
            if (tableColumn == TABLE_COLUMN_TYPE) {
                result = DataEditorProperties.getString("String_Line_With_Parallel");
            } else {
                result = DataEditorProperties.getString("String_LINE_WITH_PARALLEL");
            }
        } else if (mode == SnapMode.LINE_WITH_PERPENDICULAR) {
            if (tableColumn == TABLE_COLUMN_TYPE) {
                result = DataEditorProperties.getString("String_Line_With_Perpendicular");
            } else {
                result = DataEditorProperties.getString("String_LINE_WITH_PERPENDICULAR");
            }
        } else if (mode == SnapMode.LINE_WITH_FIXED_ANGLE) {
            if (tableColumn == TABLE_COLUMN_TYPE) {
                result = DataEditorProperties.getString("String_Line_With_Fixed_Angle");
            } else {
                result = DataEditorProperties.getString("String_LINE_WITH_FIXED_ANGLE");
            }
        } else if (mode == SnapMode.LINE_WITH_FIXED_LENGTH) {
            if (tableColumn == TABLE_COLUMN_TYPE) {
                result = DataEditorProperties.getString("String_Line_With_Fixed_Length");
            } else {
                result = DataEditorProperties.getString("String_LINE_WITH_FIXED_LENGTH");
            }
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
        this.buttonMoveToNext = new JButton();
        this.buttonMoveToForward = new JButton();
        this.buttonMoveToFrist = new JButton();
        this.buttonMoveToLast = new JButton();
        this.buttonOK = ComponentFactory.createButtonOK();
        this.buttonCancel = ComponentFactory.createButtonCancel();
        this.buttonRecover = new SmButton();
        this.labelSnapTolarence = new JLabel();
        this.textFieldSnapTolarence = new WaringTextField(format.format(srcSnapSetting.getTolerance()));
        this.textFieldSnapTolarence.setInitInfo(1, 20, WaringTextField.INTEGER_TYPE, "null");
        this.labelFixedAngle = new JLabel();
        this.textFieldFixedAngle = new WaringTextField(format.format(srcSnapSetting.getFixedAngle()));
        this.textFieldFixedAngle.setInitInfo(0, 360, WaringTextField.FLOAT_TYPE, "2");
//        this.labelMaxSnappedCount = new JLabel();
//        this.textFieldMaxSnappedCount = new WaringTextField(format.format(srcSnapSetting.getMaxSnappedCount()));
//        this.textFieldMaxSnappedCount.setInitInfo(20, 5000, WaringTextField.INTEGER_TYPE, "null");
        this.labelFixedLength = new JLabel();
        this.textFieldFixedLength = new WaringTextField(format.format(srcSnapSetting.getFixedLength()));
        this.textFieldFixedLength.setInitInfo(0, 1000000000, WaringTextField.FLOAT_TYPE, "2");
        this.labelMinSnappedLength = new JLabel();
        this.textFieldMinSnappedLength = new WaringTextField(format.format(srcSnapSetting.getMinSnappedLength()));
        this.textFieldMinSnappedLength.setInitInfo(1, 120, WaringTextField.INTEGER_TYPE, "null");
        this.checkBoxSnappedLineBroken = new JCheckBox();
        this.checkBoxSnappedLineBroken.setSelected(srcSnapSetting.isSnappedLineBroken());
        this.labelImageCicle = new JLabel();
        this.labelImageArraw = new JLabel();
    }

    public DialogResult showDialog() {
        init();
        setVisible(true);
        return dialogResult;
    }
}
