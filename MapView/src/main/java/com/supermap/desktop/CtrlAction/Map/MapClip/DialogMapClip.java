package com.supermap.desktop.CtrlAction.Map.MapClip;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.ComponentBorderPanel.CompTitledPane;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.borderPanel.PanelButton;
import com.supermap.desktop.ui.controls.progress.FormProgressTotal;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Vector;

import static com.supermap.desktop.CtrlAction.Map.MapClip.MapClipTableModel.*;

/**
 * @author YuanR
 *         2017.3.21
 *         地图裁剪主窗体
 */
public class DialogMapClip extends SmDialog {
    private GeoRegion geoRegion;

    private JToolBar toolBar;
    private JButton buttonAddLayers;
    private JButton buttonSelectAll;
    private JButton buttonInvertSelect;
    private JButton buttonSet;
    private JButton buttonDelete;
    private MapClipSetDialog mapClipSetDialog;
    private MapClipAddLayersDialog mapClipAddLayersDialog;
    private JScrollPane scrollPane;
    private MapClipJTable mapClipJTable;

    private MapClipSaveMapPanel mapClipSaveMapPanel;
    private CompTitledPane saveMapcompTitledPane;
    private JPanel clipSetPanel;
    private JCheckBox exactClip;

    private PanelButton panelButton;


    private Vector layerJTableInfo;//  从JTable中拿到的vector
    private Vector resultInfo;//  处理后的vector
    private Vector deletedInfo;
    private Vector selectedDeletedInfo;

    private String saveMapName;
    private Map resultMap = null;
    //private boolean isTableRowCanUse=true;
    private boolean isSaveMapNameIsValid = true;

    private MapClipProgressCallable mapClipProgressCallable;

    public static final int COLUMN_INDEX_SOURCEDATASET = 0;
    public static final int COLUMN_INDEX_USERREGION = 1;
    public static final int COLUMN_INDEX_ISCLIPINREGION = 2;
    public static final int COLUMN_INDEX_ISEXACTCLIPorISERASESOURCE = 3;
    public static final int COLUMN_INDEX_TARGETDATASETSOURCE = 4;
    public static final int COLUMN_INDEX_TARGETDATASETNAME = 5;
    public static final int COLUMN_LAYER = 6;

    /**
     * 对JTable中model数据进行处理,得到适用于裁剪接口的数据
     *
     * @return
     */
    private void initResultVectorInfo() {
        this.layerJTableInfo = this.mapClipJTable.getMapClipTableModel().getLayerInfo();
        if (this.layerJTableInfo != null) {
            try {
                Vector tempVector = new Vector();
                for (int i = 0; i < this.layerJTableInfo.size(); i++) {
                    // 从JTable中筛选出需要裁剪的图层对象，按照裁剪接口参数构建新的Vector
//                    boolean isClip = (Boolean) ((Vector) (this.layerJTableInfo.get(i))).get(COLUMN_INDEX_ISCLIP);
//                    if (isClip) {
                    Dataset targetDataset;
                    GeoRegion userRegion;
                    boolean isClipInRegion = false;
                    boolean isExactClipOrIsEarsesource = false;
                    Datasource targetDatasource;
                    String targetDatasetName;
                    // 获得源数据集
                    Layer layer = (Layer) ((Vector) (this.layerJTableInfo.get(i))).get(COLUMN_INDEX_LAYERCAPTION);
                    targetDataset = layer.getDataset();
                    // 获得裁剪范围
                    userRegion = this.geoRegion;
                    // 获得是否区域内裁剪
                    String clipType = (String) ((Vector) (this.layerJTableInfo.get(i))).get(COLUMN_INDEX_CLIPTYPE);
                    if (clipType.equals(MapViewProperties.getString("String_MapClip_In"))) {
                        isClipInRegion = true;
                    }
                    // 获得是否擦除或者精确裁剪，需要根据数据集类型分别赋值
                    if (targetDataset instanceof DatasetVector) {
                        String isErase = (String) ((Vector) (this.layerJTableInfo.get(i))).get(COLUMN_INDEX_ERASE);
                        if (isErase.equals(MapViewProperties.getString("String_MapClip_Yes"))) {
                            isExactClipOrIsEarsesource = true;
                        }
                    } else {
                        isExactClipOrIsEarsesource = (Boolean) ((Vector) (this.layerJTableInfo.get(i))).get(COLUMN_INDEX_EXACTCLIP);
                    }
                    // 获得目标数据源
                    targetDatasource = (Datasource) ((Vector) (this.layerJTableInfo.get(i))).get(COLUMN_INDEX_AIMDATASOURCE);
                    //获得数据集名称，此时获得目标数据集名称时
                    targetDatasetName = (String) ((Vector) (this.layerJTableInfo.get(i))).get(COLUMN_INDEX_AIMDATASET);


                    // 将处理后的数据添加到新的Vector
                    Vector v = new Vector(7);
                    v.add(COLUMN_INDEX_SOURCEDATASET, targetDataset);
                    v.add(COLUMN_INDEX_USERREGION, userRegion);
                    v.add(COLUMN_INDEX_ISCLIPINREGION, isClipInRegion);
                    v.add(COLUMN_INDEX_ISEXACTCLIPorISERASESOURCE, isExactClipOrIsEarsesource);
                    v.add(COLUMN_INDEX_TARGETDATASETSOURCE, targetDatasource);
                    v.add(COLUMN_INDEX_TARGETDATASETNAME, targetDatasetName);
                    v.add(COLUMN_LAYER, layer);
                    tempVector.add(v);
//                    } else {
//                        if (this.resultMap != null) {
//                            Layer layer = (Layer) ((Vector) (this.layerJTableInfo.get(i))).get(COLUMN_INDEX_LAYERCAPTION);
//                            MapUtilities.removeLayer(this.resultMap, layer.getName());
//                        }
//                        continue;
//                    }
                }
                this.resultInfo = tempVector;
            } catch (Exception e) {
                Application.getActiveApplication().getOutput().output(e);
                this.resultInfo = null;
            }
        } else {
            this.resultInfo = null;
        }
        if (this.deletedInfo != null && this.deletedInfo.size() >= 1 && this.resultMap != null) {
            for (int i = 0; i < deletedInfo.size(); i++) {
                Layer layer = (Layer) ((Vector) (this.deletedInfo.get(i))).get(COLUMN_INDEX_LAYERCAPTION);
                MapUtilities.removeLayer(this.resultMap, layer.getName());
            }
        }
    }

    /**
     * ToolBar按钮响应事件
     */
    private ActionListener toolBarActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(buttonSelectAll)) {
                // 全选
                if (mapClipJTable.getRowCount() - 1 < 0) {
                    mapClipJTable.setRowSelectionAllowed(true);
                } else {
                    mapClipJTable.setRowSelectionAllowed(true);
                    // 设置所有项全部选中
                    mapClipJTable.setRowSelectionInterval(0, mapClipJTable.getRowCount() - 1);
                }

            } else if (e.getSource().equals(buttonInvertSelect)) {
                //反选
                int[] temp = mapClipJTable.getSelectedRows();
                ArrayList<Integer> selectedRows = new ArrayList<Integer>();
                for (int index = 0; index < temp.length; index++) {
                    selectedRows.add(temp[index]);
                }
                ListSelectionModel selectionModel = mapClipJTable.getSelectionModel();
                selectionModel.clearSelection();
                for (int index = 0; index < mapClipJTable.getRowCount(); index++) {
                    if (!selectedRows.contains(index)) {
                        selectionModel.addSelectionInterval(index, index);
                    }
                }
            } else if (e.getSource().equals(buttonSet)) {
                // 弹出统一设置面板
                mapClipSetDialog = new MapClipSetDialog(mapClipJTable);
                mapClipSetDialog.showDialog();
            } else if (e.getSource().equals(buttonAddLayers)) {
                mapClipAddLayersDialog = new MapClipAddLayersDialog(deletedInfo);
                DialogResult dialogResult = mapClipAddLayersDialog.showDialog();
                if (dialogResult == DialogResult.OK) {
                    selectedDeletedInfo = new Vector(6);
                    selectedDeletedInfo = mapClipAddLayersDialog.getResultAddLayers();
                    MapClipTableModel mapClipTableModel = (MapClipTableModel) mapClipJTable.getModel();
                    for (int i = 0; i < selectedDeletedInfo.size(); i++) {
                        mapClipTableModel.addRow(selectedDeletedInfo.get(i));
                        deletedInfo.remove(selectedDeletedInfo.get(i));
                    }
                    mapClipJTable.updateUI();
                    mapClipJTable.setRowSelectionInterval(mapClipJTable.getRowCount() - 1, mapClipJTable.getRowCount() - 1);
                    isCanRun();
                }
            } else if (e.getSource().equals(buttonDelete)) {
                if (deletedInfo == null) {
                    deletedInfo = new Vector(6);
                }
                int selectedRowsIndex[] = mapClipJTable.getSelectedRows();
                if (selectedRowsIndex != null && selectedRowsIndex.length >= 1) {
                    mapClipJTable.clearSelection();
                    for (int i = 0; i < selectedRowsIndex.length; i++) {
                        deletedInfo.add(mapClipJTable.getMapClipTableModel().getLayerInfo().get(selectedRowsIndex[i] - i));
                        MapClipTableModel mapClipTableModel = (MapClipTableModel) mapClipJTable.getModel();
                        mapClipTableModel.removeRow(selectedRowsIndex[i] - i);
                    }
                    mapClipJTable.updateUI();
                    if (mapClipJTable.getRowCount() == selectedRowsIndex[0] && mapClipJTable.getRowCount() >= 1) {
                        mapClipJTable.setRowSelectionInterval(selectedRowsIndex[0] - 1, selectedRowsIndex[0] - 1);
                    } else if (mapClipJTable.getRowCount() > 0) {
                        mapClipJTable.setRowSelectionInterval(selectedRowsIndex[0], selectedRowsIndex[0]);
                    }
                    isCanRun();
                }
            }
        }
    };

    /**
     * checkBox相应事件
     */
    private ActionListener checkBoxActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(mapClipSaveMapPanel.getCheckBox())) {
                if (mapClipSaveMapPanel.getCheckBox().isSelected()) {
                    // 设置一个保存为地图的默认名称
                    if (StringUtilities.isNullOrEmpty(mapClipSaveMapPanel.getSaveMapTextField().getText())) {
                        String tempMapName = "MapClip@" + ((Datasource) mapClipJTable.getValueAt(mapClipJTable.getSelectedRow(), COLUMN_INDEX_AIMDATASOURCE)).getAlias();
                        tempMapName = MapUtilities.getAvailableMapName(tempMapName, true);
                        mapClipSaveMapPanel.getSaveMapTextField().setText(tempMapName);
                    }
                    isSaveMapNameIsValid = true;
                    isCanRun();
                    // 当保存地图复选框选中是，给保存地图文本框添加监听，主要判断其内容是否为空
                    mapClipSaveMapPanel.getSaveMapTextField().addKeyListener(textFileKeyListener);
                    mapClipSaveMapPanel.getSaveMapTextField().addCaretListener(textFiledCaretListener);

                } else {
                    mapClipSaveMapPanel.getSaveMapTextField().removeKeyListener(textFileKeyListener);
                    mapClipSaveMapPanel.getSaveMapTextField().removeCaretListener(textFiledCaretListener);
                    isSaveMapNameIsValid = true;
                    isCanRun();
                }
            } else if (e.getSource().equals(exactClip)) {
                for (int i = 0; i < mapClipJTable.getRowCount(); i++) {
                    Layer layer = (Layer) ((Vector) (mapClipJTable.getMapClipTableModel().getLayerInfo().get(i))).get(COLUMN_INDEX_LAYERCAPTION);
                    if (layer.getDataset().getType() == DatasetType.GRID || layer.getDataset().getType() == DatasetType.IMAGE) {
                        ((Vector) (mapClipJTable.getMapClipTableModel().getLayerInfo().get(i))).set(COLUMN_INDEX_EXACTCLIP, exactClip.isSelected());
                    }
                }
                // 获得选中的全部 栅格图层，设置其是否精确裁剪
//                int[] tempSelectedRows = mapClipJTable.getSelectedRows();
//                for (int i = 0; i < tempSelectedRows.length; i++) {
//                    Boolean exact = exactClip.isSelected();
//                    ((Vector) layerJTableInfo.get(tempSelectedRows[i])).remove(COLUMN_INDEX_EXACTCLIP);
//                    ((Vector) layerJTableInfo.get(tempSelectedRows[i])).add(COLUMN_INDEX_EXACTCLIP, exact);
//                }
            }
        }
    };

    /**
     * 保存为地图textFiled相应事件
     */
    private CaretListener textFiledCaretListener = new CaretListener() {
        @Override
        public void caretUpdate(CaretEvent e) {
            try {
                String name = mapClipSaveMapPanel.getSaveMapTextField().getText();
                // 用获得的文件名
                String tempText = MapUtilities.getAvailableMapName(name, true);
                if (tempText.equals(name)) {
                    isSaveMapNameIsValid = true;
                    isCanRun();
                    //panelButton.getButtonOk().setEnabled(true);
                } else {
                    Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_MapClip_DatasetNameError"));
                    isSaveMapNameIsValid = false;
                    isCanRun();
                    //panelButton.getButtonOk().setEnabled(false);
                }
            } catch (Exception ex) {
                Application.getActiveApplication().getOutput().output(ex);
            }
        }
    };

    /**
     * 保存为地图键盘相应事件
     */
    private KeyListener textFileKeyListener = new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            try {
                String name = mapClipSaveMapPanel.getSaveMapTextField().getText();
                if (name == null || name.length() <= 0) {
                    panelButton.getButtonOk().setEnabled(false);
                    isSaveMapNameIsValid = false;
                    isCanRun();
                    Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_MapClip_DatasetNameError"));
                } else if (!UICommonToolkit.isLawName(name, false)) {
                    isSaveMapNameIsValid = false;
                    isCanRun();
                    Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_MapClip_DatasetNameError"));
                }
            } catch (Exception ex) {
                Application.getActiveApplication().getOutput().output(ex);
            }
        }
    };

    /**
     * JTableSelection改变事件
     */
    private ListSelectionListener tableSelectionListener = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            // 当JTable选择的行改变时，需要根据选中行的情况获得精确裁剪是否可用，以及属性值
            initInfo();
        }
    };

    /**
     * model内容改变监听，主要对数据集名称做以判断
     */
    private TableModelListener tableModelListener = new TableModelListener() {
        @Override
        public void tableChanged(TableModelEvent e) {
            isCanRun();
            // 当结果数据集名称或者数据源内容改变时，都需要判断其
            if (e.getColumn() == COLUMN_INDEX_AIMDATASOURCE) {
                // 当数据源改变，重新设置文件名称
                int[] selectedRow = mapClipJTable.getSelectedRows();
                for (int i = 0; i < selectedRow.length; i++) {
                    Datasource tempDatasource = (Datasource) mapClipJTable.getModel().getValueAt(selectedRow[i], COLUMN_INDEX_AIMDATASOURCE);
                    String tempDatasetName = ((String) mapClipJTable.getModel().getValueAt(selectedRow[i], COLUMN_INDEX_AIMDATASET));
                    while (!tempDatasource.getDatasets().isAvailableDatasetName(tempDatasetName)) {
                        tempDatasetName = tempDatasource.getDatasets().getAvailableDatasetName(tempDatasetName);
                    }
                    mapClipJTable.setValueAt(tempDatasetName, selectedRow[i], COLUMN_INDEX_AIMDATASET);
                }
            }
            if (e.getColumn() == COLUMN_INDEX_AIMDATASET) {
                //此时手动修改了结果数据集名称
                Datasource tempDatasource = (Datasource) mapClipJTable.getModel().getValueAt(mapClipJTable.getSelectedRow(), COLUMN_INDEX_AIMDATASOURCE);
                String tempDatasetName = (String) mapClipJTable.getModel().getValueAt(mapClipJTable.getSelectedRow(), COLUMN_INDEX_AIMDATASET);
                if (!tempDatasource.getDatasets().isAvailableDatasetName(tempDatasetName)) {
                    Application.getActiveApplication().getOutput().output(MessageFormat.format(MapViewProperties.getString("String_MapClip_DatasetNameErrorOne"), tempDatasetName));
                    panelButton.getButtonOk().setEnabled(false);
                } else {
                    panelButton.getButtonOk().setEnabled(true);
                }
                // 当手动修改JTable中结果数据集名称时，需要做一个判断，如果JTable中有相同数据集的条目，同时修改其结果数据集名称
                Layer layer = (Layer) mapClipJTable.getModel().getValueAt(mapClipJTable.getSelectedRow(), COLUMN_INDEX_LAYERCAPTION);
                Dataset dataset = layer.getDataset();
                ArrayList<Integer> array = new ArrayList<>();
                for (int i = 0; i < mapClipJTable.getModel().getRowCount(); i++) {
                    if (i == mapClipJTable.getSelectedRow()) {
                        continue;
                    }
                    Layer tempLayer = (Layer) mapClipJTable.getModel().getValueAt(i, COLUMN_INDEX_LAYERCAPTION);
                    Dataset tempDataset = tempLayer.getDataset();
                    if (tempDataset.equals(dataset)) {
                        array.add(i);
                    }
                }
                if (array.size() > 0) {
                    //改变值之前先移除监听
                    mapClipJTable.getModel().removeTableModelListener(tableModelListener);
                    for (int i = 0; i < array.size(); i++) {
                        int row = array.get(i);
                        mapClipJTable.getModel().setValueAt(tempDatasetName, row, COLUMN_INDEX_AIMDATASET);
                    }
                    mapClipJTable.getModel().addTableModelListener(tableModelListener);
                }
            }
            if (e.getColumn() == COLUMN_INDEX_CLIPTYPE) {
                String clipType = (String) mapClipJTable.getModel().getValueAt(mapClipJTable.getSelectedRow(), COLUMN_INDEX_CLIPTYPE);
                Layer layer = (Layer) mapClipJTable.getModel().getValueAt(mapClipJTable.getSelectedRow(), COLUMN_INDEX_LAYERCAPTION);
                Dataset dataset = layer.getDataset();
                ArrayList<Integer> array = new ArrayList<>();
                for (int i = 0; i < mapClipJTable.getModel().getRowCount(); i++) {
                    if (i == mapClipJTable.getSelectedRow()) {
                        continue;
                    }
                    Layer tempLayer = (Layer) mapClipJTable.getModel().getValueAt(i, COLUMN_INDEX_LAYERCAPTION);
                    Dataset tempDataset = tempLayer.getDataset();
                    if (tempDataset.equals(dataset)) {
                        array.add(i);
                    }
                }
                if (array.size() > 0) {
                    //改变值之前先移除监听
                    mapClipJTable.getModel().removeTableModelListener(tableModelListener);
                    for (int i = 0; i < array.size(); i++) {
                        int row = array.get(i);
                        mapClipJTable.getModel().setValueAt(clipType, row, COLUMN_INDEX_CLIPTYPE);
                    }
                    mapClipJTable.getModel().addTableModelListener(tableModelListener);
                }
            }
            if (e.getColumn() == COLUMN_INDEX_ERASE) {
                String isEarse = (String) mapClipJTable.getModel().getValueAt(mapClipJTable.getSelectedRow(), COLUMN_INDEX_ERASE);
                Layer layer = (Layer) mapClipJTable.getModel().getValueAt(mapClipJTable.getSelectedRow(), COLUMN_INDEX_LAYERCAPTION);
                Dataset dataset = layer.getDataset();
                ArrayList<Integer> array = new ArrayList<>();
                for (int i = 0; i < mapClipJTable.getModel().getRowCount(); i++) {
                    if (i == mapClipJTable.getSelectedRow()) {
                        continue;
                    }
                    Layer tempLayer = (Layer) mapClipJTable.getModel().getValueAt(i, COLUMN_INDEX_LAYERCAPTION);
                    Dataset tempDataset = tempLayer.getDataset();
                    if (tempDataset.equals(dataset)) {
                        array.add(i);
                    }
                }
                if (array.size() > 0) {
                    //改变值之前先移除监听
                    mapClipJTable.getModel().removeTableModelListener(tableModelListener);
                    for (int i = 0; i < array.size(); i++) {
                        int row = array.get(i);
                        mapClipJTable.getModel().setValueAt(isEarse, row, COLUMN_INDEX_ERASE);
                    }
                    mapClipJTable.getModel().addTableModelListener(tableModelListener);
                }
            }
        }
    };

    /**
     * 确定取消按钮事件
     */
    private ActionListener cancelButtonActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            removeEvents();
            DialogMapClip.this.dispose();
        }
    };

    /**
     * 确定按钮事件
     */
    private ActionListener OKButtonActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 当点击了确定按钮，进行裁剪操作，首先对JTable中model数据进行处理,得到适用于裁剪接口的数据
            saveMapName = mapClipSaveMapPanel.getSaveMapTextField().getText();
            IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
            if (mapClipSaveMapPanel.getCheckBox().isSelected() && !StringUtilities.isNullOrEmpty(saveMapName)) {
                resultMap = new Map(formMap.getMapControl().getMap().getWorkspace());
                resultMap.fromXML(formMap.getMapControl().getMap().toXML());
                resultMap.setName(saveMapName);
            }
            initResultVectorInfo();
            if (resultInfo != null && resultInfo.size() > 0) {
                FormProgressTotal formProgress = new FormProgressTotal();
                formProgress.setTitle(MapViewProperties.getString("String_MapClip_MapClip"));
                //获得要保存的地图名称
                mapClipProgressCallable = new MapClipProgressCallable(resultInfo, resultMap);
                formProgress.doWork(mapClipProgressCallable);
                // 获得采集后的数据集，添加到地图
                DialogMapClip.this.dispose();
            } else {
                Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_MapClip_SelectLayer"));
            }
        }
    };

    private void isCanRun() {
        if (this.mapClipJTable.getRowCount() >= 1 && this.isSaveMapNameIsValid) {
            this.panelButton.getButtonOk().setEnabled(true);
        } else {
            this.panelButton.getButtonOk().setEnabled(false);
        }
    }


    public DialogMapClip(GeoRegion region) {
        super();
        this.geoRegion = region;
        initComponent();
        initLayout();
        initResources();
        registEvents();
        initInfo();
        this.pack();
        this.setSize(new Dimension(650, 450));
        this.setLocationRelativeTo(null);
        this.componentList.add(this.panelButton.getButtonOk());
        this.componentList.add(this.panelButton.getButtonCancel());
        this.setFocusTraversalPolicy(policy);
    }

    /**
     * 初始化地图裁剪控件属性
     * 目前主要设置是否精确裁剪，需要根据选中数据的类型决定
     */
    private void initInfo() {
        boolean result = false;
        for (int i = 0; i < mapClipJTable.getRowCount(); i++) {
            Layer layer = (Layer) mapClipJTable.getModel().getValueAt(i, COLUMN_INDEX_LAYERCAPTION);
            if (layer.getDataset().getType() == DatasetType.GRID || layer.getDataset().getType() == DatasetType.IMAGE) {
                result = true;
                break;
            }
        }
        this.exactClip.setEnabled(result);
        if (result){
            this.exactClip.setSelected(true);
            for (int i = 0; i < this.mapClipJTable.getRowCount(); i++) {
                Layer layer = (Layer) ((Vector) (this.mapClipJTable.getMapClipTableModel().getLayerInfo().get(i))).get(COLUMN_INDEX_LAYERCAPTION);
                if (layer.getDataset().getType() == DatasetType.GRID || layer.getDataset().getType() == DatasetType.IMAGE) {
                    ((Vector) (this.mapClipJTable.getMapClipTableModel().getLayerInfo().get(i))).set(COLUMN_INDEX_EXACTCLIP, exactClip.isSelected());
                }
            }
        }
        this.exactClip.setSelected(this.exactClip.isEnabled());
//        if (this.mapClipJTable.getSelectedRowCount() > 0) {
//            Layer selectedLayer = (Layer) this.mapClipJTable.getValueAt(this.mapClipJTable.getSelectedRow(), COLUMN_INDEX_LAYERCAPTION);
//            if ((selectedLayer.getDataset().getType()).equals(DatasetType.GRID) || (selectedLayer.getDataset().getType()).equals(DatasetType.IMAGE)) {
//                this.exactClip.setEnabled(true);
//                // 获得是否精确裁剪属性信息
//                Boolean exactClip = (Boolean) ((Vector) this.mapClipJTable.getMapClipTableModel().getLayerInfo().get(this.mapClipJTable.getSelectedRow())).get(COLUMN_INDEX_EXACTCLIP);
//                // 设置精确裁剪复选框状态属性
//                this.exactClip.setSelected(exactClip);
//            }
////            else {
////                this.exactClip.setSelected(false);
////                this.exactClip.setEnabled(false);
////            }
//        }
    }


    private void initComponent() {
        initToolbar();
        // 地图裁剪JTable
        this.mapClipJTable = new MapClipJTable();
        this.scrollPane = new JScrollPane(this.mapClipJTable);
        // 地图裁剪保存地图面板
        this.mapClipSaveMapPanel = new MapClipSaveMapPanel();
        this.saveMapcompTitledPane = mapClipSaveMapPanel.getCompTitledPane();
        // 地图裁剪影像栅格数据集裁剪设置
        intiClipSetPanel();
        // 确定取消按钮面板
        this.panelButton = new PanelButton();

    }

    private void initToolbar() {
        this.toolBar = new JToolBar();
        this.buttonAddLayers = new JButton();
        this.buttonDelete = new JButton();
        this.buttonSelectAll = new JButton();
        this.buttonInvertSelect = new JButton();
        this.buttonSet = new JButton();

        this.toolBar.setFloatable(false);
        this.toolBar.add(this.buttonAddLayers);
        this.toolBar.add(this.buttonDelete);
        this.toolBar.addSeparator();
        this.toolBar.add(this.buttonSelectAll);
        this.toolBar.add(this.buttonInvertSelect);
        this.toolBar.addSeparator();
        this.toolBar.add(this.buttonSet);
    }

    //栅格、影像数据集裁剪设置——布局
    private void intiClipSetPanel() {
        this.clipSetPanel = new JPanel();
        this.exactClip = new JCheckBox();
        GroupLayout clipSetPanelLayout = new GroupLayout(this.clipSetPanel);
        clipSetPanelLayout.setAutoCreateContainerGaps(true);
        clipSetPanelLayout.setAutoCreateGaps(true);
        this.clipSetPanel.setLayout(clipSetPanelLayout);
        //@formatter:off
        clipSetPanelLayout.setHorizontalGroup(clipSetPanelLayout.createParallelGroup()
                .addGroup(clipSetPanelLayout.createSequentialGroup()
                        .addComponent(this.exactClip)
                        .addGap(5, 5, Short.MAX_VALUE)));
        clipSetPanelLayout.setVerticalGroup(clipSetPanelLayout.createSequentialGroup()
                .addGap(20)
                .addComponent(this.exactClip)
                .addGap(5, 5, Short.MAX_VALUE));
        //@formatter:on
    }

    private void initLayout() {
        JPanel mainPanel = new JPanel();
        GroupLayout panelLayout = new GroupLayout(mainPanel);
        panelLayout.setAutoCreateContainerGaps(true);
        panelLayout.setAutoCreateGaps(true);
        mainPanel.setLayout(panelLayout);
        //@formatter:off
        panelLayout.setHorizontalGroup(panelLayout.createParallelGroup()
                .addComponent(this.toolBar)
                .addComponent(this.scrollPane)
                .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(saveMapcompTitledPane)
                        .addComponent(clipSetPanel))
                .addComponent(this.panelButton));
        panelLayout.setVerticalGroup(panelLayout.createSequentialGroup()
                .addComponent(this.toolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(this.scrollPane)
                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(saveMapcompTitledPane)
                        .addComponent(clipSetPanel))
                .addComponent(this.panelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE));
        //@formatter:on

        this.setLayout(new BorderLayout());
        this.add(mainPanel, BorderLayout.CENTER);
    }

    private void registEvents() {
        removeEvents();
        this.buttonAddLayers.addActionListener(this.toolBarActionListener);
        this.buttonDelete.addActionListener(this.toolBarActionListener);
        this.buttonSelectAll.addActionListener(this.toolBarActionListener);
        this.buttonInvertSelect.addActionListener(this.toolBarActionListener);
        this.buttonSet.addActionListener(this.toolBarActionListener);
        this.mapClipJTable.getSelectionModel().addListSelectionListener(this.tableSelectionListener);
        this.mapClipJTable.getModel().addTableModelListener(tableModelListener);
        this.mapClipSaveMapPanel.getCheckBox().addActionListener(this.checkBoxActionListener);
        this.exactClip.addActionListener(checkBoxActionListener);
        this.panelButton.getButtonOk().addActionListener(OKButtonActionListener);
        this.panelButton.getButtonCancel().addActionListener(cancelButtonActionListener);
    }

    private void removeEvents() {
        this.buttonAddLayers.removeActionListener(this.toolBarActionListener);
        this.buttonDelete.removeActionListener(this.toolBarActionListener);
        this.buttonSelectAll.removeActionListener(this.toolBarActionListener);
        this.buttonInvertSelect.removeActionListener(this.toolBarActionListener);
        this.buttonSet.removeActionListener(this.toolBarActionListener);
        this.mapClipJTable.getSelectionModel().removeListSelectionListener(this.tableSelectionListener);
        this.mapClipJTable.getModel().removeTableModelListener(tableModelListener);
        this.mapClipSaveMapPanel.getCheckBox().removeActionListener(this.checkBoxActionListener);
        this.exactClip.removeActionListener(checkBoxActionListener);

        this.panelButton.getButtonOk().removeActionListener(OKButtonActionListener);
        this.panelButton.getButtonCancel().removeActionListener(cancelButtonActionListener);
    }

    private void initResources() {
        this.setTitle(MapViewProperties.getString("String_MapClip_MapClip"));
        this.buttonAddLayers.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Add.png"));
        this.buttonAddLayers.setToolTipText(MapViewProperties.getString("String_MapClip_AddLayers"));
        this.buttonDelete.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Delete.png"));
        this.buttonDelete.setToolTipText(CommonProperties.getString("String_Delete"));
        this.buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
        this.buttonInvertSelect.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
        this.buttonSet.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Setting.PNG"));
        this.buttonSelectAll.setToolTipText(CommonProperties.getString("String_ToolBar_SelectAll"));
        this.buttonInvertSelect.setToolTipText(CommonProperties.getString("String_ToolBar_SelectInverse"));
        this.buttonSet.setToolTipText(CommonProperties.getString("String_ToolBar_SetBatch"));
        this.clipSetPanel.setBorder(BorderFactory.createTitledBorder(MapViewProperties.getString("String_MapClip_Image_ClipSetting")));
        this.exactClip.setText(MapViewProperties.getString("String_MapClip_ExactClip"));
    }
}

