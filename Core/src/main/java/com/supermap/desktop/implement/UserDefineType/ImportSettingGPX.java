package com.supermap.desktop.implement.UserDefineType;

import com.supermap.data.*;
import com.supermap.data.conversion.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Created by xie on 2017/3/28.
 */
public class ImportSettingGPX extends ImportSettingCSV{
    private Vector steppedListeners;

    public ImportSettingGPX() {

    }

    public UserDefineImportResult run() {
        UserDefineImportResult result = null;
        try {
            GPXAnalytic analyticClass = new GPXAnalytic();
            //调用解析方法
            List<GPXBean> list = analyticClass.parseXml(this.getSourceFilePath());
            DatasetVectorInfo info = new DatasetVectorInfo();
            Datasets datasets = this.getTargetDatasource().getDatasets();
            String name = datasets.getAvailableDatasetName(this.getTargetDatasetName());
            info.setName(name);
            info.setType(DatasetType.POINT);
            DatasetVector datasetVector = datasets.create(info);
            GeoCoordSys geoCoordSys = new GeoCoordSys(GeoCoordSysType.GCS_WGS_1984, GeoSpatialRefType.SPATIALREF_EARTH_LONGITUDE_LATITUDE);
            PrjCoordSys prjCoordSys = new PrjCoordSys(PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE);
            prjCoordSys.setGeoCoordSys(geoCoordSys);
            datasetVector.setPrjCoordSys(prjCoordSys);
            datasetVector.open();

            // 创建字段
            FieldInfo fieldInfo = new FieldInfo();
            fieldInfo.setName("ele");
            fieldInfo.setType(FieldType.SINGLE);
            FieldInfos fieldInfos = datasetVector.getFieldInfos();
            fieldInfos.add(fieldInfo);
            fieldInfo.dispose();

            fieldInfo = new FieldInfo();
            fieldInfo.setName("time");
            fieldInfo.setType(FieldType.CHAR);
            //将字段添加到数据集中
            fieldInfos.add(fieldInfo);
            fieldInfo.dispose();

            Recordset recordset = datasetVector.getRecordset(false, CursorType.DYNAMIC);
            Recordset.BatchEditor editor = recordset.getBatch();
            editor.begin();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                GeoPoint point = new GeoPoint();
                HashMap map = new HashMap();
                point.setX(list.get(i).getLon());
                point.setY(list.get(i).getLat());
                map.put("ele", list.get(i).getEle());
                map.put("time", list.get(i).getTime());
                recordset.addNew(point, map);
                point.dispose();
                int percent = i * 100 / size;
                ImportSteppedEvent steppedEvent = new ImportSteppedEvent(this, 100, percent, this, 1, false);
                fireStepped(steppedEvent);
            }
            editor.update();
            int count = datasetVector.getRecordCount();
            // 释放占用资源
            info.dispose();
            recordset.dispose();
            if (count == size) {
                this.setTargetDatasetName(datasetVector.getName());
                result = new UserDefineImportResult(this, null);
            } else {
                result = new UserDefineImportResult(null, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public synchronized void addImportSteppedListener(ImportSteppedListener listener) {
        if (this.steppedListeners == null) {
            this.steppedListeners = new Vector();
        }

        if (!this.steppedListeners.contains(listener)) {
            this.steppedListeners.add(listener);
        }

    }

    public synchronized void removeImportSteppedListener(ImportSteppedListener listener) {
        if (this.steppedListeners != null && this.steppedListeners.contains(listener)) {
            this.steppedListeners.remove(listener);
        }

    }

    protected void fireStepped(ImportSteppedEvent event) {
        if (this.steppedListeners != null) {
            Vector listeners = this.steppedListeners;
            int size = listeners.size();

            for (int i = 0; i < size; ++i) {
                ((ImportSteppedListener) listeners.elementAt(i)).stepped(event);
            }
        }

    }

    @Override
    public void dispose() {

    }
}
