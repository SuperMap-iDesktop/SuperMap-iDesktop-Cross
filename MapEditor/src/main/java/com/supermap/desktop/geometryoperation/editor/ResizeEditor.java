package com.supermap.desktop.geometryoperation.editor;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.IRegionConvertor;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.geometryoperation.*;
import com.supermap.desktop.geometryoperation.control.MapControlTip;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.TabularUtilities;
import com.supermap.mapping.Selection;
import com.supermap.ui.Action;
import com.supermap.ui.TrackMode;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;

/**
 * Created by xie on 2016/8/23.
 */
public class ResizeEditor extends AbstractEditor {

    private static final String TAG_RESIZE = "Tag_resizeTracking";
    private static final Action MAPCONTROL_ACTION = Action.SELECT;
    private static final TrackMode MAPCONTROL_TRACKMODE = TrackMode.TRACK;

    private IEditController offsetEditController = new EditControllerAdapter() {

        public void mousePressed(EditEnvironment environment, MouseEvent e) {
            mapControl_MousePressed(environment, e);
        }

        public void mouseMoved(EditEnvironment environment, MouseEvent e) {
            OffsetEditModel editModel = (OffsetEditModel) environment.getEditModel();

            if (editModel.desDataset != null && editModel.desGeometry != null) {
                showPreview(environment, environment.getMapControl().getMap().pixelToMap(e.getPoint()));
            }
        }

        public void mouseClicked(EditEnvironment environment, MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                environment.stopEditor();
                environment.getMap().refreshTrackingLayer();
            }
        }
    };

    @Override
    public void activate(EditEnvironment environment) {
        OffsetEditModel editModel = null;
        if (environment.getEditModel() instanceof OffsetEditModel) {
            editModel = (OffsetEditModel) environment.getEditModel();
        } else {
            editModel = new OffsetEditModel();
            environment.setEditModel(editModel);
        }
        environment.setEditController(this.offsetEditController);

        editModel.oldAction = environment.getMapControl().getAction();
        editModel.oldTrackMode = environment.getMapControl().getTrackMode();
        environment.getMapControl().setAction(MAPCONTROL_ACTION);
        environment.getMapControl().setTrackMode(MAPCONTROL_TRACKMODE);
        editModel.tip.bind(environment.getMapControl());
    }

    @Override
    public void deactivate(EditEnvironment environment) {
        if (environment.getEditModel() instanceof OffsetEditModel) {
            OffsetEditModel editModel = (OffsetEditModel) environment.getEditModel();

            try {
                environment.getMapControl().setAction(editModel.oldAction);
                environment.getMapControl().setTrackMode(editModel.oldTrackMode);
                clear(environment);
            } finally {
                editModel.tip.unbind();
                environment.setEditModel(null);
                environment.setEditController(NullEditController.instance());
            }
        }
    }

    @Override
    public boolean enble(EditEnvironment environment) {
        return ListUtilities.isListContainAny(environment.getEditProperties().getEditableDatasetTypes(), DatasetType.LINE, DatasetType.REGION, DatasetType.CAD);
    }

    @Override
    public boolean check(EditEnvironment environment) {
        return environment.getEditor() instanceof OffsetEditor;
    }

    private void mapControl_MousePressed(EditEnvironment environment, MouseEvent e) {
        OffsetEditModel editModel = (OffsetEditModel) environment.getEditModel();

        if (SwingUtilities.isLeftMouseButton(e)) {
            if (editModel.desGeometry == null) {
                initializeSrc(environment);
            } else {
                offset(environment);
                environment.stopEditor();
            }
        }
    }

    private void initializeSrc(EditEnvironment environment) {
        Recordset recordset = null;
        OffsetEditModel editModel = (OffsetEditModel) environment.getEditModel();

        try {
            // 获取目标线
            Selection selection = environment.getActiveEditableLayer().getSelection();
            if (selection.getCount() == 1) {
                recordset = selection.toRecordset();

                if (recordset != null) {
                    editModel.desDataset = recordset.getDataset();

                    while (!recordset.isEOF() && editModel.desGeometry == null) {
                        Geometry geometry = recordset.getGeometry();
                        if (editModel.desDataset.getType() == DatasetType.LINE || editModel.desDataset.getType() == DatasetType.REGION) {
                            editModel.desGeometry = geometry;
                        } else if (editModel.desDataset.getType() == DatasetType.CAD && ((geometry instanceof GeoLine) || (geometry instanceof GeoRegion))) {
                            editModel.desGeometry = geometry;
                        }
                        recordset.moveNext();
                    }
                }
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        } finally {
            if (recordset != null) {
                recordset.close();
                recordset.dispose();
            }
        }
    }

    private void offset(EditEnvironment environment) {
        Recordset recordset = null;
        OffsetEditModel editModel = (OffsetEditModel) environment.getEditModel();

        try {
            int trackingIndex = environment.getMap().getTrackingLayer().indexOf(TAG_RESIZE);

            if (trackingIndex < 0) {

                // 缩放开启之后选中对象，然后保持鼠标不动，再次点击鼠标确认，此时结果对象为空，直接返回
                Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_OffsetEditor_Null"));
                return;
            }
            Geometry desGeometry = environment.getMap().getTrackingLayer().get(trackingIndex);

            if (desGeometry != null) {
                Geometry region = null;

                if (editModel.desGeometry instanceof GeoRegion)// 如果原来是面的话，还得从偏移后的线对象转回面对象。
                {
                    IRegionConvertor convertor = (IRegionConvertor) DGeometryFactory.create(desGeometry);
                    region = convertor.convertToRegion(((GeoRegion) editModel.desGeometry).getPartCount());
                    if (region != null) {

                        // 如果 desGeometry 是 GeoRegion，上面的转换操作返回的是它自己，不能释放
                        if (!(desGeometry instanceof GeoRegion)) {
                            desGeometry.dispose();
                        }
                        desGeometry = region;
                    }
                }
                desGeometry.setStyle(editModel.desGeometry.getStyle());
                recordset = editModel.desDataset.getRecordset(false, CursorType.DYNAMIC);

                if (recordset != null) {
                    recordset.seekID(editModel.desGeometry.getID());
                    environment.getMapControl().getEditHistory().add(EditType.MODIFY, recordset, true);
                    recordset.edit();
                    recordset.setGeometry(desGeometry);
                    recordset.update();
                    desGeometry.dispose();
                }
//                environment.getMapControl().getEditHistory().add(EditType.ADDNEW, recordset, true);

                Selection[] selections = environment.getMap().findSelection(true);
                for (Selection selection : selections) {
                    selection.clear();
                }
                MapUtilities.clearTrackingObjects(environment.getMap(), TAG_RESIZE);
                TabularUtilities.refreshTabularForm(recordset.getDataset());

                environment.getMap().refresh();
                environment.getMapControl().revalidate();
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        } finally {
            if (recordset != null) {
                recordset.close();
                recordset.dispose();
            }
        }
    }

    // @formatter:off

    /**
     * 平行线方法中的平行距离与线方向有关
     * 线方向的左边为正，线方向的右边为负
     *
     * @param environment
     * @param mouseLocation
     */
    // @formatter:on
    private void showPreview(EditEnvironment environment, Point2D mouseLocation) {
        OffsetEditModel editModel = (OffsetEditModel) environment.getEditModel();

        try {
            Point2Ds points = getPoint2Ds(editModel.desGeometry);
            // 获取带缩放图形
            Rectangle2D desGeometryRectangle = editModel.desGeometry.getBounds();

            // 获取待缩放图形中心点
            Point2D pointCenter = desGeometryRectangle.getCenter();
            // 获取鼠标点到边界线的距离，用来计算缩放比列
            double offsetX = mouseLocation.getX() - pointCenter.getX();
            double offsetY = mouseLocation.getY() - pointCenter.getY();
            double distance = 0.0;
            double resizeFactor = 0.0;
            // 计算缩放比例
            if (Math.abs(offsetX) - Math.abs(offsetY) > 0) {
                distance = Math.sqrt(offsetX * offsetX);
                resizeFactor = (distance * 2) / desGeometryRectangle.getWidth();
            } else {
                distance = Math.sqrt(offsetY * offsetY);
                resizeFactor = (distance * 2) / desGeometryRectangle.getHeight();
            }

            if (editModel.desDataset.getTolerance().getNodeSnap() == 0) {
                editModel.desDataset.getTolerance().setDefault();
            }

            if (resizeFactor - 0 > 0) {
                editModel.setMsg(MessageFormat.format(MapEditorProperties.getString("String_Tip_Edit_offsetFactor"), resizeFactor));
                // 新图形的上下左右距离
                double left = desGeometryRectangle.getLeft() * resizeFactor;
                double right = desGeometryRectangle.getRight() * resizeFactor;
                double top = desGeometryRectangle.getTop() * resizeFactor;
                double bottom = desGeometryRectangle.getBottom() * resizeFactor;
                Rectangle2D newRectangle2D = new Rectangle2D(left, bottom, right, top);
                Geometry tempGeometry = editModel.desGeometry.clone();
                // 缩放后设置预览
                tempGeometry.resize(newRectangle2D);
                Point2D newPointCenter = tempGeometry.getBounds().getCenter();
                Geometry resultGeometry = tempGeometry;

                MapUtilities.clearTrackingObjects(environment.getMap(), TAG_RESIZE);

                resultGeometry.setStyle(getTrackingStyle());
                //由于缩放后中心点发生变化，用offset方法来重新设置中心点
                resultGeometry.offset(pointCenter.getX() - newPointCenter.getX(), pointCenter.getY() - newPointCenter.getY());
                environment.getMap().getTrackingLayer().add(resultGeometry, TAG_RESIZE);
                environment.getMap().refreshTrackingLayer();
                resultGeometry.dispose();
                tempGeometry.dispose();
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
    }

    private GeoStyle getTrackingStyle() {
        GeoStyle trackingStyle = new GeoStyle();
        trackingStyle.setLineSymbolID(1);
        trackingStyle.setLineWidth(0.2);
        trackingStyle.setFillOpaqueRate(0);
        return trackingStyle;
    }

    private Point2Ds getPoint2Ds(Geometry desGeometry) {
        Point2Ds point2Ds = new Point2Ds();
        try {
            if (desGeometry instanceof GeoLine) {
                GeoLine desLine = (GeoLine) desGeometry;
                for (int i = 0; i < desLine.getPartCount(); i++) {
                    point2Ds.addRange(desLine.getPart(i).toArray());
                }
            } else if (desGeometry instanceof GeoRegion) {
                GeoRegion desRegion = (GeoRegion) desGeometry;
                for (int i = 0; i < desRegion.getPartCount(); i++) {
                    point2Ds.addRange(desRegion.getPart(i).toArray());
                }
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }

        return point2Ds;
    }

    private void clear(EditEnvironment environment) {
        OffsetEditModel editModel = (OffsetEditModel) environment.getEditModel();
        editModel.clear();
        MapUtilities.clearTrackingObjects(environment.getMap(), TAG_RESIZE);
    }

    private class OffsetEditModel implements IEditModel {
        public MapControlTip tip = new MapControlTip();
        private JLabel label = new JLabel(MapEditorProperties.getString("String_Tip_SelectOffsetObject"));

        public Geometry desGeometry;
        public DatasetVector desDataset;

        public Action oldAction = Action.SELECT2;
        public TrackMode oldTrackMode = TrackMode.EDIT;

        public void setMsg(String msg) {
            this.label.setText(msg);
            this.label.repaint();
        }

        public OffsetEditModel() {
            this.tip.addLabel(this.label);
        }

        public void clear() {
            setMsg(MapEditorProperties.getString("String_Tip_SelectOffsetObject"));

            if (this.desGeometry != null) {
                this.desGeometry.dispose();
                this.desGeometry = null;
            }

            if (this.desDataset != null) {
                this.desDataset = null;
            }
        }
    }
}
