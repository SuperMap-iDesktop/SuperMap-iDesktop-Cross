package com.supermap.desktop.CtrlAction.Map.MapClip;

import com.supermap.analyst.spatialanalyst.RasterClip;
import com.supermap.analyst.spatialanalyst.VectorClip;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.CancellationException;

import static com.supermap.desktop.CtrlAction.Map.MapClip.DialogMapClip.*;

/**
 * @author YuanR
 *         地图裁剪进度条类
 */
public class MapClipProgressCallable extends UpdateProgressCallable {

    private Vector VectorInfo;
    private Dataset resultDataset;
    private Map resultMap;

    ArrayList<Dataset> datasetsArrayList;

    public Dataset[] getResultDatasets() {
        this.resultDatasets = new Dataset[datasetsArrayList.size()];
        for (int i = 0; i < datasetsArrayList.size(); i++) {
            this.resultDatasets[i] = datasetsArrayList.get(i);
        }
        return resultDatasets;
    }

    Dataset[] resultDatasets;
    private boolean createMapClip;
    private PercentListener percentListener;

    /**
     *
     */
    class PercentListener implements SteppedListener {
        private boolean isCancel = false;
        private int count;
        private int i;
        private String datasetName;

        PercentListener(int i, int count, String datasetName) {
            this.count = count;
            this.i = i;
            this.datasetName = datasetName;
        }

        public boolean isCancel() {
            return this.isCancel;
        }

        @Override
        public void stepped(SteppedEvent arg0) {
            try {
                int totalPercent = (100 * (this.i - 1) + arg0.getPercent()) / count;
                updateProgressTotal(arg0.getPercent(), arg0.getMessage(), totalPercent,
                        MessageFormat.format(MapViewProperties.getString("String_MapClip_ProgressCurrentLayer"), datasetName));
//                updateProgress(totalPercent,
//                        MapViewProperties.getString("String_MapClip_ProgressCurrentLayer")+ datasetName,MessageFormat.format(MapViewProperties.getString("String_MapClip_HasRunLayer"),i+1,count));
            } catch (CancellationException e) {
                arg0.setCancel(true);
                this.isCancel = true;
            }
        }
    }

    /**
     * @param vector
     */
    public MapClipProgressCallable(Vector vector, Map saveMap) {
        this.VectorInfo = vector;
        this.resultMap = saveMap;
    }

    @Override
    public Boolean call() throws Exception {
        try {
            Application.getActiveApplication().getOutput().output((MapViewProperties.getString("String_MapClip_Start")));
            this.createMapClip = false;
            this.VectorInfo.size();

            long startTime = System.currentTimeMillis();
            this.datasetsArrayList = new ArrayList<>();

            IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();

            for (int i = 0; i < this.VectorInfo.size(); i++) {
                try {
                    Dataset dataset = (Dataset) ((Vector) (this.VectorInfo.get(i))).get(COLUMN_INDEX_SOURCEDATASET);
                    if (percentListener!=null && percentListener.isCancel){
                        this.resultMap=null;
                        if (datasetsArrayList!=null && datasetsArrayList.size()>0) {
                            for (int j=0;j<datasetsArrayList.size();j++){
                                Datasets datasets= datasetsArrayList.get(j).getDatasource().getDatasets();
                                datasets.delete(datasetsArrayList.get(j).getName());
                            }
                            datasetsArrayList=null;
                        }
                        break;
                    }
                    if (percentListener!=null){
                        VectorClip.removeSteppedListener(percentListener);
                        RasterClip.removeSteppedListener(percentListener);
                    }
                    percentListener = new PercentListener(i, this.VectorInfo.size(), dataset.getName());
                    String targetDatasetName;

                    Layer layer = (Layer) ((Vector) (this.VectorInfo.get(i))).get(COLUMN_LAYER);
                    if (dataset instanceof DatasetVector) {
                        VectorClip.addSteppedListener(percentListener);

                        DatasetVector sourceDataset = (DatasetVector) ((Vector) (this.VectorInfo.get(i))).get(COLUMN_INDEX_SOURCEDATASET);
                        GeoRegion userRegion = (GeoRegion) ((Vector) (this.VectorInfo.get(i))).get(COLUMN_INDEX_USERREGION);
                        boolean isClipInRegion = (Boolean) ((Vector) (this.VectorInfo.get(i))).get(COLUMN_INDEX_ISCLIPINREGION);
                        boolean isEraseSource = (Boolean) ((Vector) (this.VectorInfo.get(i))).get(COLUMN_INDEX_ISEXACTCLIPorISERASESOURCE);
                        Datasource targetDatasource = (Datasource) ((Vector) (this.VectorInfo.get(i))).get(COLUMN_INDEX_TARGETDATASETSOURCE);
                        targetDatasetName = (String) ((Vector) (this.VectorInfo.get(i))).get(COLUMN_INDEX_TARGETDATASETNAME);

                        // 对要裁剪的数据集通过名称做一个判断，如果名字已经存在，说明数据集已经裁剪过了，不需要裁剪，将之前裁剪的结果再次加入地图
                        if (targetDatasource.getDatasets().isAvailableDatasetName(targetDatasetName)) {
                            this.resultDataset = VectorClip.clipDatasetVector(sourceDataset, userRegion, isClipInRegion,
                                    isEraseSource, targetDatasource, targetDatasetName);
                        } else {
                            if (this.resultMap != null) {
                                MapUtilities.findLayerByName(this.resultMap, layer.getName()).setDataset(targetDatasource.getDatasets().get(targetDatasetName));
//							this.resultMap.getLayers().get(layer.getName()).setDataset(targetDatasource.getDatasets().get(targetDatasetName));
                            }
                            this.resultDataset = null;
                        }
                    } else {
                        RasterClip.addSteppedListener(percentListener);
                        Dataset sourceDataset = (Dataset) ((Vector) (this.VectorInfo.get(i))).get(COLUMN_INDEX_SOURCEDATASET);
                        GeoRegion userRegion = (GeoRegion) ((Vector) (this.VectorInfo.get(i))).get(COLUMN_INDEX_USERREGION);
                        boolean isClipInRegion = (Boolean) ((Vector) (this.VectorInfo.get(i))).get(COLUMN_INDEX_ISCLIPINREGION);
                        boolean isExactClip = (Boolean) ((Vector) (this.VectorInfo.get(i))).get(COLUMN_INDEX_ISEXACTCLIPorISERASESOURCE);
                        Datasource targetDatasource = (Datasource) ((Vector) (this.VectorInfo.get(i))).get(COLUMN_INDEX_TARGETDATASETSOURCE);
                        targetDatasetName = (String) ((Vector) (this.VectorInfo.get(i))).get(COLUMN_INDEX_TARGETDATASETNAME);


                        // 对要裁剪的数据集通过名称做一个判断，如果名字已经存在，说明数据集已经裁剪过了，不需要裁剪，将之前裁剪的结果再次加入地图
                        if (targetDatasource.getDatasets().isAvailableDatasetName(targetDatasetName)) {
                            this.resultDataset = RasterClip.clip(sourceDataset, userRegion, isClipInRegion,
                                    isExactClip, targetDatasource, targetDatasetName);
                        } else {
                            if (this.resultMap != null) {
                                MapUtilities.findLayerByName(this.resultMap, layer.getName()).setDataset(targetDatasource.getDatasets().get(targetDatasetName));
                            }
                            this.resultDataset = null;
                        }
                    }

                    if (this.resultDataset != null) {
                        datasetsArrayList.add(this.resultDataset);
                        if (this.resultMap != null) {
                            MapUtilities.findLayerByName(this.resultMap, layer.getName()).setDataset(this.resultDataset);
                        }
                    }
                }catch (Exception e){
                    continue;
                }
            }
            if (this.resultMap != null) {
                formMap.getMapControl().getMap().getWorkspace().getMaps().add(this.resultMap.getName(), this.resultMap.toXML());
            }
            long endTime = System.currentTimeMillis();
            String time = String.valueOf((endTime - startTime) / 1000.0);
            Application.getActiveApplication().getOutput().output(MessageFormat.format(MapViewProperties.getString("String_MapClip_Finished"), time));
            this.createMapClip = true;
        } catch (Exception e) {
            this.createMapClip = false;
            Application.getActiveApplication().getOutput().output(e);
        } finally {
            VectorClip.removeSteppedListener(percentListener);
            RasterClip.removeSteppedListener(percentListener);
        }
        return this.createMapClip;
    }

}
