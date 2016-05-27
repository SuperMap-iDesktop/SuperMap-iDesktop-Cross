package com.supermap.desktop.geometryoperation.editor;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import com.supermap.data.GeoCompound;
import com.supermap.data.Geometry;
import com.supermap.data.Point2D;
import com.supermap.data.Recordset;
import com.supermap.desktop.geometryoperation.EditControllerAdapter;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.IEditController;
import com.supermap.desktop.geometryoperation.IEditModel;
import com.supermap.desktop.geometryoperation.NullEditController;
import com.supermap.desktop.geometryoperation.control.MapControlTip;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.mapping.Layer;
import com.supermap.ui.Action;
import com.supermap.ui.TrackMode;

public class GeometryCopyEditor extends AbstractEditor {

	private static final String TAG_GEOMETRYCOPY = "Tag_GeometryCopy";
	private static final Action MAPCONTROL_ACTION = Action.CREATELINE;
	private static final TrackMode MAPCONTROL_TRACKMODE = TrackMode.TRACK;

	private IEditController geometryCopyController = new EditControllerAdapter() {

		public void mousePressed(EditEnvironment environment, MouseEvent e) {

		}

		@Override
		public void mouseClicked(EditEnvironment environment, MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e)) {
				environment.stopEditor();
			}
		}

		@Override
		public void mouseMoved(EditEnvironment environment, MouseEvent e) {

		}
	};

	@Override
	public void activate(EditEnvironment environment) {
		GeometryCopyEditModel editModel = null;
		if (environment.getEditModel() instanceof GeometryCopyEditModel) {
			editModel = (GeometryCopyEditModel) environment.getEditModel();
		} else {
			editModel = new GeometryCopyEditModel();
			environment.setEditModel(editModel);
		}
		environment.setEditController(this.geometryCopyController);

		initializeSrc(environment);
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		if (environment.getEditModel() instanceof GeometryCopyEditModel) {
			GeometryCopyEditModel editModel = (GeometryCopyEditModel) environment.getEditModel();

			try {

			} finally {
				environment.setEditModel(null);
				environment.setEditController(NullEditController.instance());
			}
		}
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return false;
	}

	@Override
	public boolean check(EditEnvironment environment) {
		return environment.getEditor() instanceof GeometryCopyEditor;
	}

	private void initializeSrc(EditEnvironment environment) {
		GeometryCopyEditModel editModel = (GeometryCopyEditModel) environment.getEditModel();
		ArrayList<Layer> layers = MapUtilties.getLayers(environment.getMap());

		for (Layer layer : layers) {
			if (layer.getSelection() != null && layer.getSelection().getCount() > 0) {
				editModel.copyGeometries.put(layer, new ArrayList<Integer>());
				Recordset recordset = layer.getSelection().toRecordset();

				if (recordset == null) {
					continue;
				}

				while (!recordset.isEOF()) {
					Geometry geometry = recordset.getGeometry();

					if (geometry != null) {
						editModel.copyGeometries.get(layer).add(recordset.getID());
						editModel.trackingGeoCompound.addPart(geometry);
						geometry.dispose();
					}
				}
				recordset.close();
				recordset.dispose();
			}
		}
	}

	private class GeometryCopyEditModel implements IEditModel {

		public MapControlTip tip;
		public GeoCompound trackingGeoCompound = new GeoCompound();
		public Map<Layer, List<Integer>> copyGeometries = new HashMap<>();
		public Point2D basePoint;

		public Action oldAction = Action.SELECT2;
		public TrackMode oldTrackMode = TrackMode.EDIT;

		public GeometryCopyEditModel() {
			this.tip = new MapControlTip();
		}

		public void clear() {
			if (this.trackingGeoCompound != null) {
				this.trackingGeoCompound.dispose();
			}

			if (this.copyGeometries != null) {
				this.copyGeometries.clear();
			}
		}
	}
}
