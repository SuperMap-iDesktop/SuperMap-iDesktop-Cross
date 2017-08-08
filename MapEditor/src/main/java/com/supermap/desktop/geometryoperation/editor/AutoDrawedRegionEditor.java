package com.supermap.desktop.geometryoperation.editor;
/**
 * @author lixiaoyao
 */

import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.EditType;
import com.supermap.data.GeoRegion;
import com.supermap.data.Geometrist;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.QueryParameter;
import com.supermap.data.Recordset;
import com.supermap.data.Rectangle2D;
import com.supermap.data.SpatialQueryMode;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometryoperation.EditControllerAdapter;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.IEditController;
import com.supermap.desktop.geometryoperation.IEditModel;
import com.supermap.desktop.geometryoperation.NullEditController;
import com.supermap.desktop.geometryoperation.control.MapControlTip;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.TabularUtilities;
import com.supermap.mapping.Layer;
import com.supermap.ui.Action;
import com.supermap.ui.ActionChangedEvent;
import com.supermap.ui.TrackMode;
import com.supermap.ui.TrackedEvent;

import javax.swing.*;
import java.awt.event.MouseEvent;


public abstract class AutoDrawedRegionEditor extends AbstractEditor {

    public abstract String getTagTip();

    public abstract String getDrawedTip();

    public abstract Action getMapControlAction();

    public abstract TrackMode getTrackMode();

    public abstract Geometry[] runSuccessedRegion(Layer layer, Rectangle2D rectangle2D, Geometry geometry);

    private IEditController autoDrawedRegionEditControler = new EditControllerAdapter() {
        @Override
        public void mouseClicked(EditEnvironment environment, MouseEvent e) {
            AutoDrawedRegionModel editModel = (AutoDrawedRegionModel) environment.getEditModel();
            if (SwingUtilities.isRightMouseButton(e)) {
                if (editModel.clickNum == 2) {
                    clear(environment);
                }
            }
        }

        //  实现连续操作，即当点击右键之后，可以继续自动构面，也可以再次点击右键彻底结束自动构面功能
        @Override
        public void mousePressed(EditEnvironment environment, MouseEvent e) {
            AutoDrawedRegionModel editModel = (AutoDrawedRegionModel) environment.getEditModel();
            if (!editModel.isTracking && e.getButton() == MouseEvent.BUTTON1) {
                editModel.clickNum = 0;
                editModel.isTracking = true;
                editModel.geometry = null;
                editModel.setTipMessage(MapEditorProperties.getString("String_RightClickToEnd"));
            } else if (editModel.isTracking && e.getButton() == MouseEvent.BUTTON3) {
                editModel.clickNum = 1;
                runDrawedRegion(environment);
                editModel.isTracking = false;
            } else if (!editModel.isTracking && e.getButton() == MouseEvent.BUTTON3) {
                editModel.clickNum = editModel.clickNum + 1;
                if (editModel.clickNum == 2 || editModel.geometry == null) {
                    environment.stopEditor();
                    clear(environment);
                }
            }
        }

        @Override
        public void tracked(EditEnvironment environment, TrackedEvent e) {
            mapControlTracked(environment, e);
        }

        @Override
        public void actionChanged(EditEnvironment environment, ActionChangedEvent e) {
            if (e.getOldAction() == getMapControlAction() && e.getNewAction() != Action.PAN && e.getNewAction() != getMapControlAction()) {
                if (environment.getEditModel() instanceof AutoDrawedRegionModel) {
                    environment.stopEditor();
                }
            }
        }
    };

    @Override
    public void activate(EditEnvironment environment) {
        AutoDrawedRegionModel editModel;
        if (environment.getEditModel() instanceof AutoDrawedRegionModel) {
            editModel = (AutoDrawedRegionModel) environment.getEditModel();
        } else {
            editModel = new AutoDrawedRegionModel();
            environment.setEditModel(editModel);
        }
        environment.setEditController(this.autoDrawedRegionEditControler);
        environment.getMapControl().setAction(getMapControlAction());
        environment.getMapControl().setTrackMode(getTrackMode());
        editModel.tip.bind(environment.getMapControl());
    }

    @Override
    public void deactivate(EditEnvironment environment) {
        if (environment.getEditModel() instanceof AutoDrawedRegionModel) {
            AutoDrawedRegionModel editModel = (AutoDrawedRegionModel) environment.getEditModel();
            try {
                if (environment.getMapControl().getAction() == Action.CREATEPOLYGON || environment.getMapControl().getAction() == Action.CREATEPOLYLINE) {
                    environment.getMapControl().setAction(Action.SELECT2);
                } else {
                    environment.getMapControl().setAction(environment.getMapControl().getAction());
                }
                environment.getMapControl().setTrackMode(editModel.oldTrackMode);
                clear(environment);
            } finally {
                editModel.tip.unbind();
                environment.setEditController(NullEditController.instance());
                environment.setEditModel(null);
            }
        }
    }

    @Override
    public boolean enble(EditEnvironment environment) {
        return ListUtilities.isListContainAny(environment.getEditProperties().getEditableDatasetTypes(),
                DatasetType.REGION);
    }

    @Override
    public boolean check(EditEnvironment environment) {
        return environment.getEditor() instanceof AutoDrawedRegionEditor;
    }

    /**
     * 绘制结束后，获取所绘制的对象
     */
    private void mapControlTracked(EditEnvironment environment, TrackedEvent e) {
        if (!(environment.getEditModel() instanceof AutoDrawedRegionModel)) {
            return;
        }
        AutoDrawedRegionModel editModel = (AutoDrawedRegionModel) environment.getEditModel();

        try {
            editModel.geometry = e.getGeometry();
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
    }

    private void runDrawedRegion(EditEnvironment environment) {
        AutoDrawedRegionModel editModel = (AutoDrawedRegionModel) environment.getEditModel();
        environment.getMapControl().getEditHistory().batchBegin();
        Layer layer = environment.getMapControl().getActiveEditableLayer();
        Geometry geometry = null;
        Recordset targetRecordset = null;
        Geometry resultGeometry[] = null;
        try {
            if (editModel.geometry != null && getBounds(layer, environment) && layer.getDataset().getType() == DatasetType.REGION) {

                resultGeometry = runSuccessedRegion(layer, editModel.searchBounds, editModel.geometry);
                if (resultGeometry != null) {
                    if (resultGeometry.length >= 1) {
                        geometry = resultGeometry[0];
                    }
                    for (int i = 1; i < resultGeometry.length; ++i) {
                        geometry = Geometrist.union(geometry, resultGeometry[i]);
                    }

                    if (geometry != null) {
                        targetRecordset = ((DatasetVector) environment.getFormMap().getMapControl().getActiveEditableLayer().getDataset()).getRecordset(false, CursorType.DYNAMIC);
                        targetRecordset.addNew(geometry);
                        targetRecordset.update();
                        environment.getActiveEditableLayer().getSelection().clear();
                        environment.getActiveEditableLayer().getSelection().add(targetRecordset.getID());
                        environment.getMapControl().getEditHistory().add(EditType.ADDNEW, targetRecordset, true);
	                    TabularUtilities.refreshTabularStructure(targetRecordset.getDataset());
                    }
                }
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex.toString());
        } finally {
            environment.getMapControl().getEditHistory().batchEnd();
            environment.getMapControl().getMap().refresh();

            if (targetRecordset != null) {
                targetRecordset.close();
                targetRecordset.dispose();
            }
            resultGeometry = null;
            geometry = null;
        }
    }

    private void clear(EditEnvironment environment) {
        if (!(environment.getEditModel() instanceof AutoDrawedRegionModel)) {
            return;
        }
        AutoDrawedRegionModel editModel = (AutoDrawedRegionModel) environment.getEditModel();
        editModel.clear();
        MapUtilities.clearTrackingObjects(environment.getMap(), getTagTip());
    }

    //通过空间查询获取当前所绘制的面相交的所有面对象的范围
    private boolean getBounds(Layer layer, EditEnvironment environment) {
        boolean result = false;
        AutoDrawedRegionModel editModel = (AutoDrawedRegionModel) environment.getEditModel();
        Geometry drawedGeometry = editModel.geometry;
        Recordset resultRecordset = null;
        boolean isCanQuery = true;
        try {
            if (editModel.geometry.getType() == GeometryType.GEOREGION && ((GeoRegion) editModel.geometry).getPart(0).getCount() <= 2) {
                isCanQuery = false;
            }
            if (isCanQuery) {
                resultRecordset = queryGeometryTouchSelectedGeometry(drawedGeometry, (DatasetVector) layer.getDataset());
                if (resultRecordset.getRecordCount() >= 1) {
                    result = true;
                    editModel.searchBounds = resultRecordset.getBounds();
                }
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex.toString());
        } finally {
            if (resultRecordset != null) {
                resultRecordset.close();
                resultRecordset.dispose();
                drawedGeometry = null;
            }
        }
        return result;
    }

    private Recordset queryGeometryTouchSelectedGeometry(Geometry selectedGeometry, DatasetVector nowDatasetVector) {
        Recordset resultRecordset = null;
        QueryParameter parameter = new QueryParameter();
        parameter.setCursorType(CursorType.STATIC);
        parameter.setSpatialQueryMode(SpatialQueryMode.INTERSECT);
        parameter.setSpatialQueryObject(selectedGeometry);

        resultRecordset = nowDatasetVector.query(parameter);
        return resultRecordset;
    }

    private class AutoDrawedRegionModel implements IEditModel {
        public com.supermap.ui.Action oldMapControlAction = Action.SELECT2;
        public TrackMode oldTrackMode = TrackMode.EDIT;
        public MapControlTip tip = new MapControlTip();
        private JLabel tipLabel = new JLabel(MapEditorProperties.getString(getDrawedTip()));
        public boolean isTracking = false;
        public Geometry geometry = null;
        public Rectangle2D searchBounds = null;
        public int clickNum = 0;

        public AutoDrawedRegionModel() {
            this.tip.addLabel(this.tipLabel);
        }

        public void setTipMessage(String tipMessage) {
            this.tipLabel.setText(tipMessage);
            this.tipLabel.repaint();
        }

        public void clear() {
            this.oldMapControlAction = com.supermap.ui.Action.SELECT2;
            this.oldTrackMode = TrackMode.EDIT;
            this.isTracking = false;
            this.geometry = null;
            this.searchBounds = null;
            this.clickNum = 0;
            this.tipLabel.setText(MapEditorProperties.getString(getDrawedTip()));
        }
    }
}
