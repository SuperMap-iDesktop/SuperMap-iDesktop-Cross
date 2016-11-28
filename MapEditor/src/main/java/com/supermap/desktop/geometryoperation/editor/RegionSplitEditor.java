package com.supermap.desktop.geometryoperation.editor;

/**
 * @author lixiaoyao
 */

import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.EditType;
import com.supermap.data.GeoRegion;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.core.recordset.RecordsetDelete;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;
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
import com.supermap.ui.TrackMode;
import com.supermap.ui.TrackedEvent;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.List;


public abstract class RegionSplitEditor extends AbstractEditor{

	public abstract String getTagTip();

	public abstract String getSplitTip();

	public abstract Action getMapControlAction();

	public abstract TrackMode getTrackMode();

	public abstract boolean runSplit(GeoRegion sourceRegion,Geometry splitGeometry,GeoRegion resultRegion1,GeoRegion resultRegion2);

	private IEditController regionSplitController = new EditControllerAdapter() {
		@Override
		public void mouseClicked(EditEnvironment environment, MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e)) {
				environment.stopEditor();
			}
		}

		@Override
		public void mousePressed(EditEnvironment environment, MouseEvent e) {
			RegionSplitModel editModel = (RegionSplitModel) environment.getEditModel();
			if (!editModel.isTracking && e.getButton() == MouseEvent.BUTTON1) {
				editModel.isTracking = true;
				editModel.setTipMessage(MapEditorProperties.getString("String_RightClickToEnd"));
			} else if (editModel.isTracking && e.getButton() == MouseEvent.BUTTON3) {
				runRegionSplit(environment);
				environment.stopEditor();
			}
		}

		@Override
		public void tracked(EditEnvironment environment, TrackedEvent e) {
			mapControlTracked(environment, e);
		}
	};

	@Override
	public void activate(EditEnvironment environment) {
		RegionSplitModel editModel;
		if (environment.getEditModel() instanceof RegionSplitModel) {
			editModel = (RegionSplitModel) environment.getEditModel();
		} else {
			editModel = new RegionSplitModel();
			environment.setEditModel(editModel);
		}
		environment.setEditController(this.regionSplitController);

		editModel.oldMapControlAction = environment.getMapControl().getAction();
		editModel.oldTrackMode = environment.getMapControl().getTrackMode();
		environment.getMapControl().setAction(getMapControlAction());
		environment.getMapControl().setTrackMode(getTrackMode());
		editModel.tip.bind(environment.getMapControl());
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		if (environment.getEditModel() instanceof RegionSplitModel) {
			RegionSplitModel editModel = (RegionSplitModel) environment.getEditModel();

			try {
				environment.getMapControl().setAction(editModel.oldMapControlAction);
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
		return environment.getEditProperties().getEditableSelectedGeometryCount() > 0
				&& ListUtilities.isListContainAny(environment.getEditProperties().getEditableSelectedGeometryTypeFeatures(),
				IRegionFeature.class);
	}

	@Override
	public boolean check(EditEnvironment environment) {
		return environment.getEditor() instanceof RegionSplitEditor;
	}

	private void mapControlTracked(EditEnvironment environment, TrackedEvent e) {
		if (!(environment.getEditModel() instanceof RegionSplitModel)) {
			return;
		}
		RegionSplitModel editModel = (RegionSplitModel) environment.getEditModel();

		try {
			editModel.geometry = e.getGeometry();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void runRegionSplit(EditEnvironment environment)
	{
		RegionSplitModel editModel = (RegionSplitModel) environment.getEditModel();
		try {
			environment.getMapControl().getEditHistory().batchBegin();
			editModel.setTipMessage(MapEditorProperties.getString(getSplitTip()));
			MapUtilities.clearTrackingObjects(environment.getMap(), getTagTip());

			List<Layer> layers = MapUtilities.getLayers(environment.getMap());
			for (Layer layer : layers) {
				if (layer.isEditable() && (layer.getDataset() instanceof DatasetVector) && layer.getSelection().getCount() > 0) {
					Recordset recordset = null;//选中的记录集集合
					Recordset newRecordset = null;//保存分割后的记录集对象
					RecordsetDelete delete = null;//保存要删除的记录集集合

					try {
						recordset = layer.getSelection().toRecordset();
						newRecordset = ((DatasetVector) layer.getDataset()).getRecordset(false, CursorType.DYNAMIC);
						Recordset.BatchEditor editor = newRecordset.getBatch();
						editor.setMaxRecordCount(2000);
						editor.begin();

						if (layer.isEditable()) {
							delete = new RecordsetDelete(newRecordset.getDataset(), environment.getMapControl().getEditHistory());
							delete.begin();
						}

						while (!recordset.isEOF()) {
							Geometry geometry = recordset.getGeometry();
							if (geometry.getType() == GeometryType.GEOREGION) {
								GeoRegion resultGeoRegion1 = new GeoRegion();
								GeoRegion resultGeoRegion2 = new GeoRegion();
								GeoRegion tempGeoRegion = (GeoRegion) geometry;

								boolean resultSplit =runSplit(tempGeoRegion,editModel.geometry,resultGeoRegion1,resultGeoRegion2);
								//boolean resultSplit = Geometrist.splitRegion(tempGeoRegion, editModel.geoLine, resultGeoRegion1, resultGeoRegion2);
								if (resultSplit) {
									Geometry newGeometry = (Geometry) resultGeoRegion1;
									newRecordset.addNew(newGeometry);
									environment.getMapControl().getEditHistory().add(EditType.ADDNEW, newRecordset, true);
									newGeometry = (Geometry) resultGeoRegion2;
									newRecordset.addNew(newGeometry);
									environment.getMapControl().getEditHistory().add(EditType.ADDNEW, newRecordset, true);
									geometry.dispose();
									delete.delete(recordset.getID());
								} else {//如果转换失败就什么都不做，保留原对象
								}
								resultGeoRegion1.dispose();
								resultGeoRegion2.dispose();
							}
							recordset.moveNext();
						}
						if (delete != null) {
							delete.update();
							editor.update();
							layer.getSelection().clear();
						}
						TabularUtilities.refreshTabularForm(newRecordset.getDataset());
						Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_GeometryOperation_SucessResultTip"));
					} catch (Exception ex) {
						Application.getActiveApplication().getOutput().output(ex);
					} finally {
						if (recordset != null) {
							recordset.close();
							recordset.dispose();
						}
						if (newRecordset != null) {
							newRecordset.close();
							newRecordset.dispose();
						}
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			environment.getMapControl().getEditHistory().batchEnd();
			environment.getMapControl().getMap().refresh();
		}
	}

	private void clear(EditEnvironment environment) {
		if (!(environment.getEditModel() instanceof RegionSplitModel)) {
			return;
		}
		RegionSplitModel editModel = (RegionSplitModel) environment.getEditModel();
		editModel.clear();
		MapUtilities.clearTrackingObjects(environment.getMap(), getTagTip());
	}

	private class RegionSplitModel implements IEditModel{
		public Action oldMapControlAction = Action.SELECT2;
		public TrackMode oldTrackMode = TrackMode.EDIT;
		public MapControlTip tip = new MapControlTip();
		private JLabel tipLabel = new JLabel(MapEditorProperties.getString(getSplitTip()));
		public boolean isTracking = false;
		//public GeoLine geoLine = null;
		public Geometry geometry=null;

		public RegionSplitModel() {
			this.tip.addLabel(this.tipLabel);
		}

		public void setTipMessage(String tipMessage) {
			this.tipLabel.setText(tipMessage);
			this.tipLabel.repaint();
		}

		public void clear() {
			this.oldMapControlAction = Action.SELECT2;
			this.oldTrackMode = TrackMode.EDIT;
			this.isTracking = false;
			//this.geoLine = null;
			this.tipLabel.setText(MapEditorProperties.getString(getSplitTip()));
		}
	}

}
