package com.supermap.desktop.geometryoperation.editor;

import java.util.ArrayList;

import com.supermap.data.DatasetType;
import com.supermap.data.EditType;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.IReverse;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.utilities.CursorUtilities;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;

public class ReverseEditor extends AbstractEditor {

	@Override
	public void activate(EditEnvironment environment) {
		try {
			CursorUtilities.setWaitCursor(environment.getMapControl());
			reverse(environment);
		} finally {
			environment.activateEditor(NullEditor.INSTANCE);
			CursorUtilities.setDefaultCursor(environment.getMapControl());
		}
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return environment.getEditProperties().getEditableSelectedGeometryCount() > 0
				&& ListUtilities.isListContainAny(environment.getEditProperties().getEditableSelectedGeometryTypes(), GeometryType.GEOLINE,
						GeometryType.GEOLINEM, GeometryType.GEOREGION, GeometryType.GEOREGION3D, GeometryType.GEOLINE3D);
	}

	private void reverse(EditEnvironment environment) {
		environment.getMapControl().getEditHistory().batchBegin();

		try {
			ArrayList<Layer> layers = MapUtilities.getLayers(environment.getMap());

			for (Layer layer : layers) {
				if (layer.isEditable()
						&& (layer.getDataset().getType() == DatasetType.LINE || layer.getDataset().getType() == DatasetType.LINEM
								|| layer.getDataset().getType() == DatasetType.CAD || layer.getDataset().getType() == DatasetType.NETWORK
								|| layer.getDataset().getType() == DatasetType.REGION || layer.getDataset().getType() == DatasetType.REGION3D || layer
								.getDataset().getType() == DatasetType.LINE3D) && layer.getSelection().getCount() > 0) {
					Recordset recordset = layer.getSelection().toRecordset();

					try {
						while (!recordset.isEOF()) {
							IGeometry geometry = DGeometryFactory.create(recordset.getGeometry());
							Geometry reverseGeometry = null;

							try {
								if (geometry instanceof IReverse) {
									reverseGeometry = ((IReverse) geometry).reverse();

									recordset.edit();
									environment.getMapControl().getEditHistory().add(EditType.MODIFY, recordset, true);
									recordset.setGeometry(reverseGeometry);
									recordset.update();
								}
							} finally {
								if (geometry != null) {
									geometry.dispose();
								}

								if (reverseGeometry != null) {
									reverseGeometry.dispose();
								}
								recordset.moveNext();
							}
						}
					} finally {
						if (recordset != null) {
							recordset.close();
							recordset.dispose();
						}
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			environment.getMapControl().getEditHistory().batchEnd();
			environment.getMap().refresh();
		}
	}
}
