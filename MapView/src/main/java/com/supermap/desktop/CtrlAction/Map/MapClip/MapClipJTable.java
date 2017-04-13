package com.supermap.desktop.CtrlAction.Map.MapClip;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.CellRenders.TableDataCellRender;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTable;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.ui.MapControl;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static com.supermap.desktop.Application.getActiveApplication;
import static com.supermap.desktop.CtrlAction.Map.MapClip.MapClipTableModel.*;

/**
 * @author YuanR
 *         2017.3.28
 */
public class MapClipJTable extends MutiTable {

    private TableColumn layerCaption;
    private TableColumn aimDatasourceColumn;
    private TableColumn clipTypeColumn;
    private TableColumn eraseColumn;
    private TableColumn acurrentClipColumn;
    public MapClipTableModel mapClipTableModel;
    private ArrayList<Datasource> isCanUseDatasources;
    private HashMap<String,String> datasetsName=new HashMap<>();

    public MapClipTableModel getMapClipTableModel() {
        return mapClipTableModel;
    }

    public MapClipJTable() {
        super();
        initComponents();
        initLayerInfo();
    }

    /**
     * 初始化JTable控件
     */
    private void initComponents() {
        //设置标题行不能移动
        this.getTableHeader().setReorderingAllowed(false);
        this.setRowHeight(23);
        mapClipTableModel = new MapClipTableModel();
        this.setModel(mapClipTableModel);

        Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
        this.isCanUseDatasources = new ArrayList<>();
        for (int i = 0; i < datasources.getCount(); i++) {
            if (!datasources.get(i).isReadOnly()) {
                this.isCanUseDatasources.add(datasources.get(i));
            }
        }
        DatasourceComboBox datasourceComboBox = null;
        if (this.isCanUseDatasources != null && this.isCanUseDatasources.size() > 0) {
            datasourceComboBox = new DatasourceComboBox(this.isCanUseDatasources);
        } else {
            return;
        }

        String[] clipType = {MapViewProperties.getString("String_MapClip_Out"), MapViewProperties.getString("String_MapClip_In")};
        JComboBox clipTypeComboBox = new JComboBox(clipType);
        String[] erase = {MapViewProperties.getString("String_MapClip_Yes"), MapViewProperties.getString("String_MapClip_No")};
        JComboBox eraseComboBox = new JComboBox(erase);
        String[] acurrent = {MapViewProperties.getString("String_MapClip_Yes"), MapViewProperties.getString("String_MapClip_No")};
        JComboBox acurrentComboBox = new JComboBox(acurrent);

        this.layerCaption = this.getColumn(this.getModel().getColumnName(COLUMN_INDEX_LAYERCAPTION));
        this.aimDatasourceColumn = this.getColumn(this.getModel().getColumnName(COLUMN_INDEX_AIMDATASOURCE));
        this.clipTypeColumn = this.getColumn(this.getModel().getColumnName(COLUMN_INDEX_CLIPTYPE));
        this.eraseColumn = this.getColumn(this.getModel().getColumnName(COLUMN_INDEX_ERASE));
        this.acurrentClipColumn=this.getColumn(this.getModel().getColumnName(COLUMN_INDEX_EXACTCLIP));

        DefaultCellEditor defaultCellEditorAimDatasourceColumn =new DefaultCellEditor(datasourceComboBox);
        defaultCellEditorAimDatasourceColumn.setClickCountToStart(2);
        this.aimDatasourceColumn.setCellEditor(defaultCellEditorAimDatasourceColumn);
        DefaultCellEditor defaultCellEditorClipTypeColumn =new DefaultCellEditor(clipTypeComboBox);
        defaultCellEditorClipTypeColumn.setClickCountToStart(2);
        this.clipTypeColumn.setCellEditor(defaultCellEditorClipTypeColumn);
        DefaultCellEditor defaultCellEditorEraseColumn =new DefaultCellEditor(eraseComboBox);
        defaultCellEditorEraseColumn.setClickCountToStart(2);
        this.eraseColumn.setCellEditor(defaultCellEditorEraseColumn);
        DefaultCellEditor defaultCellEditorAcurrentClipColumn =new DefaultCellEditor(acurrentComboBox);
        defaultCellEditorAcurrentClipColumn.setClickCountToStart(2);
        this.acurrentClipColumn.setCellEditor(defaultCellEditorAcurrentClipColumn);

        //设置渲染器
        this.aimDatasourceColumn.setCellRenderer(new TableDataCellRender());
        this.layerCaption.setCellRenderer(new MapClipLayerCaptionTableRender());

        // 给是否参与裁剪表头添加CheckBox
        //this.parClip.setHeaderRenderer(new CheckHeaderCellRenderer(this, MapViewProperties.getString("String_MapClip_IsClip"), false));

        this.clipTypeColumn.setHeaderRenderer(new JComponentTableCellRenderer());
        ImageAndTextTableHeaderCell clipTypeTip = new ImageAndTextTableHeaderCell(MapViewProperties.getString("String_MapClip_ClipType"), ControlsResources.getIcon("/controlsresources/Icon_Help.png"), ControlsResources.getIcon("/controlsresources/Icon_Help.png"));
        clipTypeTip.setToolTipText("");
        this.clipTypeColumn.setHeaderValue(clipTypeTip);

        this.eraseColumn.setHeaderRenderer(new JComponentTableCellRenderer());
        ImageAndTextTableHeaderCell eraseTip = new ImageAndTextTableHeaderCell(MapViewProperties.getString("String_MapClip_Erase"), ControlsResources.getIcon("/controlsresources/Icon_Warning.png"), "擦除裁剪区域会修改原数据");
        eraseTip.setToolTipText("");
        this.eraseColumn.setHeaderValue(eraseTip);
    }

    /**
     * 初始化JTable内容
     */
    private void initLayerInfo() {
        ArrayList<Layer> resultLayer = new ArrayList();
        MapControl activeMapControl = ((IFormMap) getActiveApplication().getActiveForm()).getMapControl();
        ArrayList<Layer> arrayList;
        arrayList = MapUtilities.getLayers(activeMapControl.getMap(), true);
        //是否存在图层
        if (arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).getDataset() == null) {
                    continue;
                }
                resultLayer.add(arrayList.get(i));
            }
        }

        for (int i = 0; i < resultLayer.size(); i++) {
            // 对获得的图层进行遍历，当数据集类型属于矢量和影像时才进行添加
            if (resultLayer.get(i).getDataset() instanceof DatasetVector ||
                    resultLayer.get(i).getDataset() instanceof DatasetImage ||
                    resultLayer.get(i).getDataset() instanceof DatasetGrid) {
                //boolean clip = false;
                Layer layerCaption = resultLayer.get(i);
                Datasource targetDatasource = resultLayer.get(i).getDataset().getDatasource();
                if (targetDatasource.isReadOnly()) {
                    if (this.isCanUseDatasources != null && this.isCanUseDatasources.size() > 0) {
                        targetDatasource = this.isCanUseDatasources.get(0);
                    } else {
                        continue;
                    }
                }

                // 当初始化的时候就通过判断设置好结果数据集的名称
                String targetDataset = resultLayer.get(i).getDataset().getName();
                String origionDatasetName=targetDataset;
                if (this.datasetsName.containsKey(targetDataset)){
                    targetDataset=this.datasetsName.get(targetDataset);
                }else {
                    while (!targetDatasource.getDatasets().isAvailableDatasetName(targetDataset)) {
                        if (targetDataset.lastIndexOf("_") != -1) {
                            targetDataset = targetDataset.substring(0, targetDataset.lastIndexOf("_"));
                        }
                        targetDataset = targetDatasource.getDatasets().getAvailableDatasetName(targetDataset);
                    }
                    this.datasetsName.put(origionDatasetName,targetDataset);
                }
                String clipType = MapViewProperties.getString("String_MapClip_In");
                String erase = MapViewProperties.getString("String_MapClip_No");
                String exactClip = MapViewProperties.getString("String_MapClip_No");

                this.mapClipTableModel.addRowLayerInfo(layerCaption, targetDatasource, targetDataset, clipType, erase, exactClip);
                this.updateUI();
            } else {
                continue;
            }
        }
        // 设置首行记录被选中
        this.setRowSelectionInterval(0, 0);
    }

    /**
     * 负责对layerCaption列渲染的内部类
     */
    class MapClipLayerCaptionTableRender extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                    row, column);
            this.setText(((Layer) value).getCaption());
            return this;
        }
    }

     class ImageAndTextTableHeaderCell extends JLabel {
        JLabel labelCaption;
        JLabel labelTip;

        public ImageAndTextTableHeaderCell(String showText, Icon showIcon, Icon tipIcon) {
            this.labelCaption = new JLabel(showText);
            this.labelTip = new JLabel(showIcon);
            initLayout();
        }

        public ImageAndTextTableHeaderCell(String showText, Icon showIcon, String tipText) {
            this.labelCaption = new JLabel(showText);
            this.labelTip = new JLabel(showIcon);
            this.labelTip.setToolTipText(tipText);
            initLayout();
        }
        private void initLayout() {
            this.labelTip.setMinimumSize(new Dimension(23,23));
            this.labelTip.setMaximumSize(new Dimension(23,23));
            this.setLayout(new BorderLayout());
            this.add(this.labelCaption, BorderLayout.WEST);
            this.add(this.labelTip, BorderLayout.EAST);
        }
    }

    private class JComponentTableCellRenderer implements TableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JComponent jComponent = (JComponent) value;
            jComponent.setBackground(table.getBackground());
            jComponent.setForeground(table.getForeground());
            return jComponent;
        }
    }

}
