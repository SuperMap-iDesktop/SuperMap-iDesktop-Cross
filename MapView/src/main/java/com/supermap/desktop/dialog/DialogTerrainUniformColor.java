package com.supermap.desktop.dialog;

import com.supermap.data.ColorDictionary;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.mapview.layer.propertycontrols.LayerGridParamColorTableDialog;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.CommonListCellRenderer;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.mutiTable.DDLExportTableModel;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTable;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerSettingGrid;
import com.supermap.mapping.Layers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * Created by lixiaoyao on 2017/2/17.
 */
public class DialogTerrainUniformColor extends SmDialog {
    private MutiTable mutiTable;
    private JComboBox comboBoxLayer;
    private JToolBar toolBar = new JToolBar();
    private JScrollPane scrollPane = new JScrollPane();
    private SmButton buttonSure = new SmButton("String_Button_OK");
    private SmButton buttonQuite = new SmButton("String_Button_Cancel");
    private SmButton buttonEditColorTable = new SmButton("String_TerrainUniformColorEditColorTable");
    private JLabel labelLayer = new JLabel("String_TerrainUniformColorLayer");
    private JCheckBox checkBoxTip = new JCheckBox("String_TerrainUniformColorTip");
    private final int COLUMN_INDEX_CHECK = 0;
    private final int COLUMN_INDEX_LAYER = 1;
    private final int COLUMN_INDEX_MINVALUE = 2;
    private final int COLUMN_INDEX_MAXVALUE = 3;
    private final int ROW_HRIGHT=23;
    private final int MAX_COLUMN_WIDTH=100;
    private final int BUTTON_COLOR_EDIT_WIDTH=110;
    private boolean isSelectedCheckTip = true;
    private HashMap<String, Layer> datasetLayerMap = new HashMap<String, Layer>();
    private ColorDictionary editColorDictionary = null;

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == DialogTerrainUniformColor.this.buttonSure) {
                run();
                DialogTerrainUniformColor.this.setDialogResult(DialogResult.OK);
                DialogTerrainUniformColor.this.dispose();
            } else if (e.getSource() == DialogTerrainUniformColor.this.buttonQuite) {
                DialogTerrainUniformColor.this.dispose();
            } else if (e.getSource() == DialogTerrainUniformColor.this.buttonEditColorTable) {
                DataCell dataCell = (DataCell) DialogTerrainUniformColor.this.comboBoxLayer.getSelectedItem();
                Layer selectedLayer = datasetLayerMap.get(dataCell.getDataName());
                LayerSettingGrid layerSettingGrid = (LayerSettingGrid) selectedLayer.getAdditionalSetting();

                LayerGridParamColorTableDialog layerGridParamColorTableDialog = new LayerGridParamColorTableDialog(layerSettingGrid);
                DialogResult result = layerGridParamColorTableDialog.showDialog();
                if (result == DialogResult.OK) {
                    ColorDictionary colorDictionary = layerSettingGrid.getColorDictionary();
                    colorDictionary.clear();
                    double[] keys = layerGridParamColorTableDialog.getCurrentLayerSettingGrid().getColorDictionary().getKeys();
                    Color[] colors = layerGridParamColorTableDialog.getCurrentLayerSettingGrid().getColorDictionary().getColors();
                    for (int i = 0; i < keys.length; i++) {
                        colorDictionary.setColor(keys[i], colors[i]);
                    }
                    layerSettingGrid.setColorDictionary(colorDictionary);
                    selectedLayer.setAdditionalSetting(layerSettingGrid);
                    editColorDictionary = new ColorDictionary(colorDictionary);
                }else{
                    if (editColorDictionary!=null){
                        editColorDictionary=null;
                    }
                }
            } else if (e.getSource() == DialogTerrainUniformColor.this.checkBoxTip) {
                isSelectedCheckTip = DialogTerrainUniformColor.this.checkBoxTip.isSelected();
            }
        }
    };


    public DialogTerrainUniformColor(JFrame owner, boolean model) {
        super(owner, model);
        initComponents();
        initResources();
        initTableInfo();
        this.componentList.add(this.buttonSure);
        this.componentList.add(this.buttonQuite);
        this.setFocusTraversalPolicy(policy);
        unRegisterEvents();
        registerEvents();
    }

    private void initComponents() {
        toolBar.setFloatable(false);
        Dimension size = new Dimension(500, 375);
        setSize(size);
        setMinimumSize(size);
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(buttonSure);
        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);

        //@formatter:off
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                .addGroup(groupLayout.createSequentialGroup()
                        .addComponent(toolBar)
                        .addComponent(buttonEditColorTable, BUTTON_COLOR_EDIT_WIDTH, BUTTON_COLOR_EDIT_WIDTH, BUTTON_COLOR_EDIT_WIDTH))
                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                .addGroup(groupLayout.createSequentialGroup()
                        .addComponent(checkBoxTip)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                        .addComponent(buttonSure)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonQuite))
        );
        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(toolBar, ROW_HRIGHT, ROW_HRIGHT, ROW_HRIGHT)
                        .addComponent(buttonEditColorTable, ROW_HRIGHT, ROW_HRIGHT, ROW_HRIGHT))
                .addComponent(scrollPane)
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(checkBoxTip)
                        .addComponent(buttonSure)
                        .addComponent(buttonQuite)));
        //@formatter:on

        mutiTable = new MutiTable();
        scrollPane.setViewportView(mutiTable);

        DDLExportTableModel tableModel = new DDLExportTableModel(new String[]{"check", "layer", "minValue", "maxValue"}) {
            boolean[] columnEditables = new boolean[]{false, false, false, false};

            @Override
            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        };
        mutiTable.setModel(tableModel);
        mutiTable.setCheckHeaderColumn(COLUMN_INDEX_CHECK);
        mutiTable.getColumnModel().getColumn(COLUMN_INDEX_LAYER).setCellRenderer(new CommonListCellRenderer());
        mutiTable.setRowHeight(ROW_HRIGHT);
        comboBoxLayer = new JComboBox();
        comboBoxLayer.setRenderer(new CommonListCellRenderer());
        toolBar.add(labelLayer);
        toolBar.add(comboBoxLayer);
        checkBoxTip.setSelected(true);
        getContentPane().setLayout(groupLayout);
    }

    private void initResources() {
        setTitle(MapViewProperties.getString("String_TerrainUniformColor"));
        labelLayer.setText(MapViewProperties.getString("String_TerrainUniformColorLayer"));
        buttonEditColorTable.setText(MapViewProperties.getString("String_TerrainUniformColorEditColorTable"));
        buttonSure.setText(CommonProperties.getString("String_Button_OK"));
        buttonQuite.setText(CommonProperties.getString("String_Button_Cancel"));
        checkBoxTip.setText(MapViewProperties.getString("Sring_TerrainUniformColorCheckBox"));
        checkBoxTip.setToolTipText(MapViewProperties.getString("String_TerrainUniformColorTip"));
        mutiTable.getColumnModel().getColumn(COLUMN_INDEX_LAYER).setHeaderValue(MapViewProperties.getString("String_TerrainUniformLayer"));
        mutiTable.getColumnModel().getColumn(COLUMN_INDEX_MINVALUE).setHeaderValue(MapViewProperties.getString("String_TerrainUniformLayerMinValue"));
        mutiTable.getColumnModel().getColumn(COLUMN_INDEX_MINVALUE).setMaxWidth(MAX_COLUMN_WIDTH);
        mutiTable.getColumnModel().getColumn(COLUMN_INDEX_MAXVALUE).setHeaderValue(MapViewProperties.getString("String_TerrainUniformLayerMaxValue"));
        mutiTable.getColumnModel().getColumn(COLUMN_INDEX_MAXVALUE).setMaxWidth(MAX_COLUMN_WIDTH);
    }

    private void initTableInfo() {
        IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
        Layers currentFormMapLayer = formMap.getMapControl().getMap().getLayers();
        for (int i = 0; i < currentFormMapLayer.getCount(); i++) {
            if (currentFormMapLayer.get(i).getDataset().getType() == DatasetType.GRID) {
                Object[] temp = new Object[4];
                temp[COLUMN_INDEX_CHECK] = true;
                DataCell datasetCell = new DataCell(currentFormMapLayer.get(i));
                comboBoxLayer.addItem(datasetCell);
                datasetLayerMap.put(currentFormMapLayer.get(i).getName(), currentFormMapLayer.get(i));
                temp[COLUMN_INDEX_LAYER] = datasetCell;
                double tempValue = ((DatasetGrid) currentFormMapLayer.get(i).getDataset()).getMinValue();
                temp[COLUMN_INDEX_MINVALUE] = tempValue;
                tempValue = ((DatasetGrid) currentFormMapLayer.get(i).getDataset()).getMaxValue();
                temp[COLUMN_INDEX_MAXVALUE] = tempValue;
                mutiTable.addRow(temp);
            }
        }
    }

    private void run() {
        java.util.List<Object> selectedLayerName = mutiTable.getAllCheckedColumn(COLUMN_INDEX_LAYER);
        DataCell dataCell = (DataCell) this.comboBoxLayer.getSelectedItem();
        Layer selectedLayer = datasetLayerMap.get(dataCell.getDataName());
        DatasetGrid selectedDataset = (DatasetGrid) selectedLayer.getDataset();

        double colorsTableMinValue = selectedDataset.getMinValue();
        double colorsTableMaxValue = selectedDataset.getMaxValue();
        LayerSettingGrid layerSettingGrid;
        ColorDictionary colorDictionary;

        if (editColorDictionary!=null){
            colorDictionary = new ColorDictionary(editColorDictionary);
        }else{
            layerSettingGrid = new LayerSettingGrid((LayerSettingGrid) selectedLayer.getAdditionalSetting());
            colorDictionary = layerSettingGrid.getColorDictionary();
        }

        Color[] originColors = colorDictionary.getColors();
        boolean isNeedReCalculatorRange = false;

        for (int i = 0; i < selectedLayerName.size(); i++) {
            dataCell = (DataCell) selectedLayerName.get(i);
            Layer tempLayer = datasetLayerMap.get(dataCell.getDataName());
            DatasetGrid tempDataset = (DatasetGrid) tempLayer.getDataset();
            if (Double.compare(colorsTableMaxValue, tempDataset.getMaxValue()) == -1) {
                colorsTableMaxValue = tempDataset.getMaxValue();
                isNeedReCalculatorRange = true;
            }
            if (Double.compare(colorsTableMinValue, tempDataset.getMinValue()) == 1) {
                isNeedReCalculatorRange = true;
                colorsTableMinValue = tempDataset.getMinValue();
            }
        }

        if (this.isSelectedCheckTip) {
            if (isNeedReCalculatorRange) {
                double valueGap = (colorsTableMaxValue - colorsTableMinValue) / (originColors.length - 1);
                double[] newKeys = new double[originColors.length];
                Color[] newColors = new Color[originColors.length];
                for (int i = 0; i < originColors.length; i++) {
                    newKeys[i] = colorsTableMinValue + valueGap * i;
                    newColors[i] = originColors[i];
                }
                colorDictionary.clear();
                for (int j = 0; j < newKeys.length; j++) {
                    colorDictionary.setColor(newKeys[j], newColors[j]);
                }
            }
        }

        for (int i = 0; i < selectedLayerName.size(); i++) {
            dataCell = (DataCell) selectedLayerName.get(i);
            Layer tempLayer = datasetLayerMap.get(dataCell.getDataName());
            ((LayerSettingGrid) tempLayer.getAdditionalSetting()).setColorDictionary(colorDictionary);
        }

        IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
        formMap.getMapControl().getMap().refresh();
    }

    private void registerEvents() {
        buttonSure.addActionListener(this.actionListener);
        buttonQuite.addActionListener(this.actionListener);
        buttonEditColorTable.addActionListener(this.actionListener);
        checkBoxTip.addActionListener(this.actionListener);
    }

    private void unRegisterEvents() {
        buttonSure.removeActionListener(this.actionListener);
        buttonQuite.removeActionListener(this.actionListener);
        buttonEditColorTable.removeActionListener(this.actionListener);
        checkBoxTip.removeActionListener(this.actionListener);
    }
}
