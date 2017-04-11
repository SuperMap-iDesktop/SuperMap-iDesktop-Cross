package com.supermap.desktop.CtrlAction.Map.MapClip;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.implement.DefaultComboBoxUI;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.CellRenders.ListDataCellRender;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.borderPanel.PanelButton;
import com.supermap.mapping.Layer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import static com.supermap.desktop.CtrlAction.Map.MapClip.MapClipTableModel.*;

/**
 * @author YuanR 2017.3.28
 *         地图裁剪统一设置Dialog
 */
public class MapClipSetDialog extends SmDialog {

    private JCheckBox checkBoxAimDataDatasource;
    private DatasourceComboBox aimDataDatasourceComboBox;
    private JCheckBox checkBoxClipMode;
    private JComboBox clipModeComboBox;
    private JCheckBox accuratelyClipCheckBox;
    private JComboBox accuratelyClipComboBox;
    private JCheckBox eraseCheckBox;
    private JComboBox eraseComboBox;
    private PanelButton panelButton;
    private MapClipJTable mapClipJTable;

    public static final int DEFAULT_PREFERREDSIZE_GAP = 10;

    private ItemListener checkBoxListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getSource().equals(checkBoxAimDataDatasource)) {
                aimDataDatasourceComboBox.setEnabled(checkBoxAimDataDatasource.isSelected());
            } else if (e.getSource().equals(accuratelyClipCheckBox)) {
                accuratelyClipComboBox.setEnabled(accuratelyClipCheckBox.isSelected());
            } else if (e.getSource().equals(checkBoxClipMode)) {
                clipModeComboBox.setEnabled(checkBoxClipMode.isSelected());
            } else if (e.getSource().equals(eraseCheckBox)) {
                eraseComboBox.setEnabled(eraseCheckBox.isSelected());
            }
        }
    };

    public MapClipSetDialog(MapClipJTable mapClipJTable) {
        this.mapClipJTable = mapClipJTable;
        initComponent();
        initLayout();
        initResources();
        initListeners();
        isAccuratelyClipCheckBoxEnabled();
        isEraseCheckBoxEnabled();
        this.pack();
        this.setSize(new Dimension(350, this.getPreferredSize().height));
        this.setLocationRelativeTo(null);
    }

    private void initComponent() {
        this.checkBoxAimDataDatasource = new JCheckBox();
        this.aimDataDatasourceComboBox = new DatasourceComboBox();
        this.aimDataDatasourceComboBox.setEnabled(false);

        this.checkBoxClipMode = new JCheckBox();
        this.clipModeComboBox = new JComboBox(new String[]{MapViewProperties.getString("String_MapClip_InsideRegion"), MapViewProperties.getString("String_MapClip_OutsideRegion")});
        this.clipModeComboBox.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
        this.clipModeComboBox.setRenderer(new ListDataCellRender());
        this.clipModeComboBox.setUI(new DefaultComboBoxUI());
        this.clipModeComboBox.setEnabled(false);

        this.accuratelyClipCheckBox = new JCheckBox();
        this.accuratelyClipComboBox = new JComboBox(new String[]{MapViewProperties.getString("String_MapClip_No"), MapViewProperties.getString("String_MapClip_Yes")});
        this.accuratelyClipComboBox.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
        this.accuratelyClipComboBox.setRenderer(new ListDataCellRender());
        this.accuratelyClipComboBox.setUI(new DefaultComboBoxUI());
        this.accuratelyClipComboBox.setEnabled(false);

        this.eraseCheckBox = new JCheckBox();
        this.eraseComboBox = new JComboBox(new String[]{MapViewProperties.getString("String_MapClip_No"), MapViewProperties.getString("String_MapClip_Yes")});
        this.eraseComboBox.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
        this.eraseComboBox.setRenderer(new ListDataCellRender());
        this.eraseComboBox.setUI(new DefaultComboBoxUI());
        this.eraseComboBox.setEnabled(false);

        this.panelButton = new PanelButton();

    }

    private void initLayout() {
        JPanel mainPanel = new JPanel();
        GroupLayout panelLayout = new GroupLayout(mainPanel);
        panelLayout.setAutoCreateContainerGaps(true);
        panelLayout.setAutoCreateGaps(true);
        mainPanel.setLayout(panelLayout);

        //@formatter:off
        panelLayout.setHorizontalGroup(panelLayout.createSequentialGroup()
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(checkBoxAimDataDatasource)
                        .addComponent(checkBoxClipMode)
                        .addComponent(eraseCheckBox)
                        .addComponent(accuratelyClipCheckBox))
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(aimDataDatasourceComboBox)
                        .addComponent(clipModeComboBox)
                        .addComponent(eraseComboBox)
                        .addComponent(accuratelyClipComboBox)));
        panelLayout.setVerticalGroup(panelLayout.createSequentialGroup()
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(checkBoxAimDataDatasource)
                        .addComponent(aimDataDatasourceComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(checkBoxClipMode)
                        .addComponent(clipModeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(eraseCheckBox)
                        .addComponent(eraseComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(accuratelyClipCheckBox)
                        .addComponent(accuratelyClipComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
//				.addComponent(this.exactClippingCheckBox)
                .addGap(0, 0, Short.MAX_VALUE));

        //@formatter:on
        this.setLayout(new BorderLayout());
        this.add(mainPanel, BorderLayout.CENTER);
        this.add(this.panelButton, BorderLayout.SOUTH);
    }

    private void initResources() {
        this.setTitle(MapViewProperties.getString("String_MapClip_GiveSameValue"));

        this.accuratelyClipCheckBox.setText(MapViewProperties.getString("String_MapClip_Image_ExactClip"));
        this.checkBoxAimDataDatasource.setText(ControlsProperties.getString("String_Label_TargetDatasource"));
        this.checkBoxClipMode.setText(MapViewProperties.getString("String_MapClip_ClipTypeD"));
        this.eraseCheckBox.setText(MapViewProperties.getString("String_MapClip_EraseCheck"));

//		this.exactClippingCheckBox.setText(MapViewProperties.getString("String_MapClip_Image_ExactClip"));
    }

    private void initListeners() {
        removeEvents();
        this.accuratelyClipCheckBox.addItemListener(checkBoxListener);
        this.checkBoxAimDataDatasource.addItemListener(checkBoxListener);
        this.checkBoxClipMode.addItemListener(checkBoxListener);
        this.eraseCheckBox.addItemListener(checkBoxListener);


        this.panelButton.getButtonOk().addActionListener(this.OKListener);
        this.panelButton.getButtonCancel().addActionListener(this.cancelListener);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                removeEvents();
            }
        });
    }

    private void removeEvents() {

        this.accuratelyClipCheckBox.removeItemListener(checkBoxListener);
        this.checkBoxAimDataDatasource.removeItemListener(checkBoxListener);
        this.checkBoxClipMode.removeItemListener(checkBoxListener);
        this.eraseCheckBox.removeItemListener(checkBoxListener);

        this.panelButton.getButtonOk().removeActionListener(this.OKListener);
        this.panelButton.getButtonCancel().removeActionListener(this.cancelListener);
    }

    private void isAccuratelyClipCheckBoxEnabled() {
        boolean result = false;
        int[] selectrows = mapClipJTable.getSelectedRows();// 获得选中的行
        if (selectrows != null && selectrows.length >= 1) {
            for (int i = 0; i < selectrows.length; i++) {
                Layer layer = (Layer) mapClipJTable.getModel().getValueAt(selectrows[i], COLUMN_INDEX_LAYERCAPTION);
                if (layer.getDataset().getType() == DatasetType.GRID || layer.getDataset().getType() == DatasetType.IMAGE) {
                    result = true;
                    break;
                }
            }
        }
        this.accuratelyClipCheckBox.setEnabled(result);
    }

    private void isEraseCheckBoxEnabled() {
        boolean result = false;
        int[] selectrows = mapClipJTable.getSelectedRows();// 获得选中的行
        if (selectrows != null && selectrows.length >= 1) {
            for (int i = 0; i < selectrows.length; i++) {
                Layer layer = (Layer) mapClipJTable.getModel().getValueAt(selectrows[i], COLUMN_INDEX_LAYERCAPTION);
                if (layer.getDataset() instanceof DatasetVector) {
                    result = true;
                    break;
                }
            }
        }
        this.eraseCheckBox.setEnabled(result);
    }

    /**
     * 确定按钮事件
     */
    private ActionListener OKListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Boolean accurateClip = null;
                Datasource targetDatasource = null;
                String clipType = null;
                String erase = null;
                if (accuratelyClipCheckBox.isSelected()) {
                    if (accuratelyClipComboBox.getSelectedItem().equals(MapViewProperties.getString("String_MapClip_No"))) {
                        accurateClip = false;
                    } else {
                        accurateClip = true;
                    }
                }
                // 目标数据集
                if (checkBoxAimDataDatasource.isSelected()) {
                    targetDatasource = (Datasource) aimDataDatasourceComboBox.getSelectedItem();
                }
                //裁剪方式
                if (checkBoxClipMode.isSelected()) {
                    if (clipModeComboBox.getSelectedItem().equals(MapViewProperties.getString("String_MapClip_InsideRegion"))) {
                        clipType = MapViewProperties.getString("String_MapClip_In");
                    } else {
                        clipType = MapViewProperties.getString("String_MapClip_Out");
                    }
                }
                // 是否擦除
                if (eraseCheckBox.isSelected()) {
                    erase = eraseComboBox.getSelectedItem().toString();
                }
                // 获得选中的行
                int[] selectrows = mapClipJTable.getSelectedRows();
                int size = selectrows.length;
                if (selectrows!=null &&selectrows.length>=1) {
                    for (int i = 0; i < size; i++) {
                        Layer layer = (Layer) mapClipJTable.getModel().getValueAt(selectrows[i], COLUMN_INDEX_LAYERCAPTION);
                        if (accurateClip != null) {
                            if (layer.getDataset().getType() == DatasetType.IMAGE || layer.getDataset().getType() == DatasetType.GRID) {
                                ((Vector) (mapClipJTable.getMapClipTableModel().getLayerInfo().get(i))).set(COLUMN_INDEX_EXACTCLIP,accurateClip);
//                                mapClipJTable.getModel().setValueAt(accurateClip, selectrows[i], COLUMN_INDEX_EXACTCLIP);
                            }
                        }
                        if (targetDatasource != null) {
                            mapClipJTable.getModel().setValueAt(targetDatasource, selectrows[i], COLUMN_INDEX_AIMDATASOURCE);
                        }
                        if (clipType != null) {
                            mapClipJTable.getModel().setValueAt(clipType, selectrows[i], COLUMN_INDEX_CLIPTYPE);
                        }
                        if (erase != null) {
                            // 当数据集类型是矢量数据集时才能进行是否擦除操作的设置
                            Dataset dataset = ((Layer) (mapClipJTable.getValueAt(selectrows[i], COLUMN_INDEX_LAYERCAPTION))).getDataset();
                            if (dataset instanceof DatasetVector && !dataset.isReadOnly()) {
                                mapClipJTable.getModel().setValueAt(erase, selectrows[i], COLUMN_INDEX_ERASE);
                            }
                        }
                    }
                }
            } catch (Exception e1) {
                Application.getActiveApplication().getOutput().output(e1);
            } finally {
                MapClipSetDialog.this.dispose();
            }
        }
    };


    /**
     * 取消按钮事件
     */
    private ActionListener cancelListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            removeEvents();
            MapClipSetDialog.this.dispose();
        }
    };
}
