package com.supermap.desktop.CtrlAction.Map.MapClip;

import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.SortTable.SortableTableModel;
import com.supermap.mapping.Layer;

import java.util.Vector;


/**
 * 2017.3.28
 *
 * @author YuanR
 *         地图裁剪JTable@model
 */
public class MapClipTableModel extends SortableTableModel {
    public Vector layerInfo;

    public static final int COLUMN_INDEX_LAYERCAPTION = 0;
    public static final int COLUMN_INDEX_AIMDATASOURCE = 1;
    public static final int COLUMN_INDEX_AIMDATASET = 2;
    public static final int COLUMN_INDEX_CLIPTYPE = 3;
    public static final int COLUMN_INDEX_ERASE = 4;
    public static final int COLUMN_INDEX_EXACTCLIP = 5;
    public static final int COLUMNS_NUMBER = 6;

    public MapClipTableModel() {
        super();
        this.layerInfo = new Vector();
    }

    /**
     * 增加一条记录
     *
     * @param layer            图层
     * @param targetDatasource 目标数据源
     * @param targetDataset    目标数据集
     * @param clipType         裁剪类型（区域内/区域外）
     * @param erase            是否擦除
     * @param exactClip        是否精确裁剪
     */
    public void addRowLayerInfo(Layer layer, Datasource targetDatasource,
                                String targetDataset, String clipType, String erase, String exactClip) {
        Vector v = new Vector(6);
        v.add(COLUMN_INDEX_LAYERCAPTION, layer);
        v.add(COLUMN_INDEX_AIMDATASOURCE, targetDatasource);
        v.add(COLUMN_INDEX_AIMDATASET, targetDataset);
        v.add(COLUMN_INDEX_CLIPTYPE, clipType);
        v.add(COLUMN_INDEX_ERASE, erase);
        v.add(COLUMN_INDEX_EXACTCLIP, exactClip);
        this.layerInfo.add(v);
    }


    @Override
    public String getColumnName(int column) {
        if (column == COLUMN_INDEX_LAYERCAPTION) {
            return MapViewProperties.getString("String_MapClip_LayerCaption");
        } else if (column == COLUMN_INDEX_AIMDATASOURCE) {
            return MapViewProperties.getString("String_MapClip_TargetDatasource");
        } else if (column == COLUMN_INDEX_AIMDATASET) {
            return MapViewProperties.getString("String_MapClip_TargetDataset");
        } else if (column == COLUMN_INDEX_CLIPTYPE) {
            return MapViewProperties.getString("String_MapClip_ClipType");
        } else if (column == COLUMN_INDEX_ERASE) {
            return MapViewProperties.getString("String_MapClip_Erase");
        }else if (column == COLUMN_INDEX_EXACTCLIP) {
            return MapViewProperties.getString("String_MapClip_AcurrentClip");
        }
        return "";
    }

    @Override
    public int getRowCount() {
        if (this.layerInfo != null) {
            return this.layerInfo.size();
        }
        return 0;
    }

    @Override
    public int getColumnCount() {
        return COLUMNS_NUMBER;
    }

    /**
     * 设置单元格是否可修改
     *
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == COLUMN_INDEX_LAYERCAPTION) {
            return false;
        }
        // 是否擦除列需要添加判断，当数据集类型是矢量数据集时才能进行是否擦除操作的设置
        if (columnIndex == COLUMN_INDEX_ERASE) {
            if (((Layer) (this.getValueAt(rowIndex, COLUMN_INDEX_LAYERCAPTION))).getDataset() instanceof DatasetVector &&
                    !((Layer) (this.getValueAt(rowIndex, COLUMN_INDEX_LAYERCAPTION))).getDataset().isReadOnly()) {
                return true;
            } else {
                return false;
            }
        }
        if (columnIndex == COLUMN_INDEX_EXACTCLIP) {
            if (!((Layer) (this.getValueAt(rowIndex, COLUMN_INDEX_LAYERCAPTION))).getDataset().isReadOnly()){
                if (((Layer) (this.getValueAt(rowIndex, COLUMN_INDEX_LAYERCAPTION))).getDataset().getType()== DatasetType.GRID
                        ||((Layer) (this.getValueAt(rowIndex, COLUMN_INDEX_LAYERCAPTION))).getDataset().getType()== DatasetType.IMAGE){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }
        return true;
    }

    /**
     * 使修改的内容生效
     *
     * @param value
     * @param row
     * @param col
     */
    public void setValueAt(Object value, int row, int col) {
        int realRow = getIndexRow(row)[0];
        ((Vector) layerInfo.get(realRow)).remove(col);
        ((Vector) layerInfo.get(realRow)).add(col, value);
        this.fireTableCellUpdated(row, col);
    }

    @Override
    public Object getValueAt(int row, int col) {
        int realRow = getIndexRow(row)[0];
        return ((Vector) this.layerInfo.get(realRow)).get(col);
    }

    public Class getColumnClass(int col) {
        return getValueAt(0, col).getClass();
    }

    public Vector getLayerInfo() {
        return this.layerInfo;
    }

    public Object getLayerInfo(int rowIndex) {
        int realRow = getIndexRow(rowIndex)[0];
        return this.layerInfo.get(realRow);
    }
    public void addRow(Object object) {
        this.layerInfo.add(object);
        super.addIndexRow(this.layerInfo.size() - 1);
        fireTableDataChanged();
    }

    public void removeRow(int rowIndex) {
        int realRow = getIndexRow(rowIndex)[0];
        this.layerInfo.remove(realRow);
        super.removeRow(rowIndex);
    }


}
