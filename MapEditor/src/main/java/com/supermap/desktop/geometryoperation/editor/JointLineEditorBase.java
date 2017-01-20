package com.supermap.desktop.geometryoperation.editor;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.EditType;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoStyle;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Point2D;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.core.recordset.RecordsetDelete;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.control.JDialogFieldOperationSetting;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilities.CursorUtilities;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.TabularUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Selection;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Map;


public abstract class JointLineEditorBase extends AbstractEditor {


    @Override
    public void activate(EditEnvironment environment) {
        CursorUtilities.setWaitCursor(environment.getMapControl());

        try {
            JDialogFieldOperationSetting formCombination = new JDialogFieldOperationSetting(getTitle(), environment.getMap(), DatasetType.LINE);

            if (formCombination.showDialog() == DialogResult.OK) {
                jointLine(environment, formCombination.getEditLayer(), formCombination.getPropertyData());
                TabularUtilities.refreshTabularForm((DatasetVector) formCombination.getEditLayer().getDataset());
            }
        } finally {
            CursorUtilities.setDefaultCursor(environment.getMapControl());

            // 结束当前编辑。如果是交互性编辑，environment 会自动管理结束，就无需主动调用。
            environment.activateEditor(NullEditor.INSTANCE);
        }
    }

    @Override
    public boolean enble(EditEnvironment environment) {
        boolean result = false;

        if (ListUtilities.isListContainAny(environment.getEditProperties().getEditableDatasetTypes(), DatasetType.CAD, DatasetType.LINE)
                && environment.getEditProperties().getSelectedGeometryCount() > 1
                && ListUtilities.isListOnlyContain(environment.getEditProperties().getSelectedGeometryTypeFeatures(), ILineFeature.class)) {
            result = true;
        }
        return result;
    }

    protected abstract GeoLine jointTwoLines(GeoLine baseLine, GeoLine targetLine, Boolean isOriginalBaseLine);

    /**
     * 子类重写这个拿标题
     *
     * @return
     */
    protected String getTitle() {
        return MapEditorProperties.getString("String_GeometryOperation_JointLine");
    }

    protected double getDistance(Point2D point1, Point2D point2) {
        return Math.sqrt(Math.pow((point1.getX() - point2.getX()), 2) + Math.pow((point1.getY() - point2.getY()), 2));
    }

    protected void releaseGeometryList(ArrayList<GeoLine> lines) {
        if (lines != null) {
            for (int i = lines.size() - 1; i >= 0; i--) {
                GeoLine line = lines.get(i);
                if (line != null) {
                    line.dispose();
                    lines.remove(i);
                }
            }
        }
    }

    private double geolineClostDistance(GeoLine geoLine1, GeoLine geoLine2) {
        Point2D startPoint1 = geoLine1.getPart(0).getItem(0);
        Point2D endPoint1 = geoLine1.getPart(0).getItem(geoLine1.getPart(0).getCount() - 1);

        Point2D startPoint2 = geoLine2.getPart(0).getItem(0);
        Point2D endPoint2 = geoLine2.getPart(0).getItem(geoLine2.getPart(0).getCount() - 1);

        double startToStart = getDistance(startPoint1, startPoint2);
        double startToEnd = getDistance(startPoint1, endPoint2);
        double endToStart = getDistance(endPoint1, startPoint2);
        double endtToEnd = getDistance(endPoint1, endPoint2);

        double minimumDistance = Math.min(startToStart, startToEnd);
        minimumDistance = Math.min(minimumDistance, endToStart);
        minimumDistance = Math.min(minimumDistance, endtToEnd);

        return minimumDistance;
    }

    private void jointLine(EditEnvironment environment, Layer editLayer, Map<String, Object> propertyData) {
        GeoLine baseGeoLine = null;
        environment.getMapControl().getEditHistory().batchBegin();

        try {
            int partLineCounts = 0;
            boolean isOriginalBaseLine = true;
            ArrayList<Integer> deleteIDs = new ArrayList<Integer>();
            // 统一管理后面从记录集中取出来的GeoLine对象的释放
            ArrayList<GeoLine> releaseGeoLines = new ArrayList<GeoLine>();
            ArrayList<Layer> layers = MapUtilities.getLayers(environment.getMapControl().getMap());
            ArrayList<GeoLine> currentGeolines = new ArrayList<GeoLine>();

            for (Layer layer : layers) {
                Selection selection = null;

                if (layer.getDataset() instanceof DatasetVector) {
                    selection = new Selection(layer.getSelection());
                    layer.getSelection().clear();
                }

                if (selection != null && selection.getCount() > 0) {
                    Recordset recordset = ((DatasetVector) layer.getDataset()).getRecordset(false, CursorType.DYNAMIC);

                    for (int i = 0; i < selection.getCount(); i++) {
                        recordset.seekID(selection.get(i));
                        IGeometry geometry = DGeometryFactory.create(recordset.getGeometry());
                        GeoLine currentLine = null;

                        if (geometry.getGeometry().getType() == GeometryType.GEOLINE) {
                            currentLine = ((ILineFeature) geometry).convertToLine(120);

                            currentGeolines.add(currentLine);
                            if (editLayer.getDataset().equals(layer.getDataset()) && currentLine != null) {
                                deleteIDs.add(selection.get(i));
                            }
                        }
                    }
                    selection.dispose();
                    if (recordset != null) {
                        recordset.dispose();
                    }
                }
            }

            if (environment.getEditor() instanceof JointLineNearEditor) {
                int endIndex = currentGeolines.size();
                for (int i = 0; i < endIndex; i++) {
                    GeoLine currentLine = null;
                    int t = 0;
                    if (i == 0) {
                        t = 0;
                    } else {
                        if (baseGeoLine != null) {
                            double initDistance = Double.MAX_VALUE;
                            for (int j = 0; j < currentGeolines.size(); j++) {
                                double tempDistance = geolineClostDistance(baseGeoLine, currentGeolines.get(j));
                                if (Double.compare(tempDistance, initDistance) == -1) {
                                    t = j;
                                    initDistance = tempDistance;
                                }
                                if (Double.compare(initDistance, 0) == 0) {
                                    break;
                                }
                            }
                        }
                    }

                    currentLine = currentGeolines.get(t);
                    if (currentLine.getPartCount() > 1) {
                        partLineCounts++;
                        continue;
                    }
                    baseGeoLine = jointTwoLines(baseGeoLine, currentLine, isOriginalBaseLine);
                    if (baseGeoLine != null && currentLine != null && !baseGeoLine.equals(currentLine)) {
                        isOriginalBaseLine = false;
                    }
                    releaseGeoLines.add(currentLine);
                    currentGeolines.remove(t);
                }

            } else if (environment.getEditor() instanceof JointLineDirectionEditor) {
                for (int i = 0; i < currentGeolines.size(); i++) {
                    GeoLine currentLine = null;
                    currentLine = currentGeolines.get(i);
                    if (currentLine.getPartCount() > 1) {
                        partLineCounts++;
                        continue;
                    }

                    baseGeoLine = jointTwoLines(baseGeoLine, currentLine, isOriginalBaseLine);
                    if (baseGeoLine != null && currentLine != null && !baseGeoLine.equals(currentLine)) {
                        isOriginalBaseLine = false;
                    }
                    releaseGeoLines.add(currentLine);
                }
            }


            if (!isOriginalBaseLine) {
                editLayer.getSelection().clear();
                Recordset resultRecordset = ((DatasetVector) editLayer.getDataset()).getRecordset(false, CursorType.DYNAMIC);
                GeoStyle style = null;
                RecordsetDelete delete = new RecordsetDelete((DatasetVector) editLayer.getDataset(), environment.getMapControl().getEditHistory());
                delete.update();

                if (deleteIDs.size() > 0) {
                    for (int id : deleteIDs) {
                        if (resultRecordset.seekID(id)) {
                            // 记录结果图层中某一个对象的风格
                            if (style == null) {
                                Geometry geometry = resultRecordset.getGeometry();
                                if (geometry.getStyle() != null) {
                                    style = geometry.getStyle().clone();
                                }
                                if (geometry != null) {
                                    geometry.dispose();
                                }
                            }

                            delete.delete(id);
                        }
                    }
                }
                delete.update();

                baseGeoLine.setStyle(style);
                boolean isSucceed = false;

                if (resultRecordset.addNew(baseGeoLine, propertyData)) {
                    if (resultRecordset.update()) {
                        editLayer.getSelection().add(resultRecordset.getID());
                        environment.getMapControl().getEditHistory().add(EditType.ADDNEW, resultRecordset, true);
                        isSucceed = true;

                        if (partLineCounts > 0) {
                            Application.getActiveApplication().getOutput()
                                    .output(MessageFormat.format(MapEditorProperties.getString("String_GeometryEdit_ConnectLineErrorOutPut"), partLineCounts));
                        }
                        Application
                                .getActiveApplication()
                                .getOutput()
                                .output(MessageFormat.format(MapEditorProperties.getString("String_GeometryEdit_ConnectLineTrueOutPut"),
                                        releaseGeoLines.size(), editLayer.getName()));
                    }
                }

                if (!isSucceed) {
                    Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_GeometryEdit_ConnectLineUpdateFaleOutPut"));
                }

                environment.getMapControl().getEditHistory().batchEnd();
                if (resultRecordset != null) {
                    resultRecordset.close();
                    resultRecordset.dispose();
                }
            } else {
                environment.getMapControl().getEditHistory().batchCancel();

                if (partLineCounts > 0) {
                    Application.getActiveApplication().getOutput()
                            .output(MessageFormat.format(MapEditorProperties.getString("String_GeometryEdit_ConnectLineErrorOutPut"), partLineCounts));
                }
                Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_GeometryEdit_ConnectLineFalseOutPut"));
            }

            if (baseGeoLine != null) {
                baseGeoLine.dispose();
            }
            releaseGeometryList(releaseGeoLines);
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
    }

//    private class GeoLineSort {
//        public ArrayList<GeoLine> arrayList = new ArrayList<GeoLine>();
//        private GeoLine tempGeoLine = null;
//
//        public void sort() {
//            for (int i = 0; i < arrayList.size() - 2; ++i) {
//                double tempDistance = pointDistance(arrayList.get(i), arrayList.get(i + 1));
//                for (int j = i + 2; j < arrayList.size(); ++j) {
//                    double distance = pointDistance(arrayList.get(i), arrayList.get(j));
//                    if (Double.compare(tempDistance, distance) == 1) {
//                        tempGeoLine = null;
//                        tempGeoLine = arrayList.get(i + 1);
//                        arrayList.set(i + 1, arrayList.get(j));
//                        arrayList.set(j, tempGeoLine);
//                        tempDistance = distance;
//                    }
//                }
//            }
//        }
//
//        private double pointDistance(GeoLine geoLine1, GeoLine geoLine2) {
//            Point2D startPoint1 = geoLine1.getPart(0).getItem(0);
//            Point2D endPoint1 = geoLine1.getPart(0).getItem(geoLine1.getPart(0).getCount() - 1);
//
//            Point2D startPoint2 = geoLine2.getPart(0).getItem(0);
//            Point2D endPoint2 = geoLine2.getPart(0).getItem(geoLine2.getPart(0).getCount() - 1);
//
//            double startToStart = getDistance(startPoint1, startPoint2);
//            double startToEnd = getDistance(startPoint1, endPoint2);
//            double endToStart = getDistance(endPoint1, startPoint2);
//            double endtToEnd = getDistance(endPoint1, endPoint2);
//
//            double minimumDistance = Math.min(startToStart, startToEnd);
//            minimumDistance = Math.min(minimumDistance, endToStart);
//            minimumDistance = Math.min(minimumDistance, endtToEnd);
//
//            return minimumDistance;
//        }
//    }
}
