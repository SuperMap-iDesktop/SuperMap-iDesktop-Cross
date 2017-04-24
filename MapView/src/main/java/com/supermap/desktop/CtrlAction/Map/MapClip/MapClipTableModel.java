package com.supermap.desktop.CtrlAction.Map.MapClip;

import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.mapping.Layer;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;


/**
 * 2017.3.28
 *
 * @author YuanR
 *         地图裁剪JTable@model
 */
public class MapClipTableModel extends DefaultTableModel {
    public static final int COLUMN_INDEX_LAYERCAPTION = 0;
    public static final int COLUMN_INDEX_AIMDATASOURCE = 1;
    public static final int COLUMN_INDEX_AIMDATASET = 2;
    public static final int COLUMN_INDEX_CLIPTYPE = 3;
    public static final int COLUMN_INDEX_ERASE = 4;
    public static final int COLUMN_INDEX_EXACTCLIP = 5;
    public static final int COLUMNS_NUMBER = 6;

    public MapClipTableModel() {
        super();
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
        super.addRow(v);
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

}
