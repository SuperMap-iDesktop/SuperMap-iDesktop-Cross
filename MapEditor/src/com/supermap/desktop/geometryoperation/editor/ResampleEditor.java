package com.supermap.desktop.geometryoperation.editor;

import java.util.ArrayList;

import com.supermap.data.DatasetType;
import com.supermap.data.EditType;
import com.supermap.data.Geometrist;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Recordset;
import com.supermap.data.ResampleType;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.control.JDialogResample;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;

public class ResampleEditor extends AbstractEditor {

	@Override
	public void activate(EditEnvironment environment) {
		try {
			JDialogResample dialog = new JDialogResample();

			if (dialog.showDialog() == DialogResult.OK) {
				resample(environment, dialog.getParameter(), dialog.getResampleType());
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			environment.activateEditor(NullEditor.INSTANCE);
		}
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return ListUtilities.isListOnlyContain(environment.getEditProperties().getEditableSelectedGeometryTypes(), GeometryType.GEOLINE, GeometryType.GEOREGION);
	}

	private void resample(EditEnvironment environment, double tolerance, ResampleType resampleType) {
		try {
			environment.getMapControl().getEditHistory().batchBegin();
			ArrayList<Layer> layers = MapUtilities.getLayers(environment.getMap());

			for (Layer layer : layers) {
				if (layer.getDataset().getType() != DatasetType.POINT && layer.getDataset().getType() != DatasetType.TEXT) {
					Recordset recordset = layer.getSelection().toRecordset();

					try {
						while (!recordset.isEOF()) {
							Geometry geo = recordset.getGeometry();
							Geometry result = null;

							try {
								if (geo.getType() != GeometryType.GEOBOX && geo.getType() != GeometryType.GEOPOINT && geo.getType() != GeometryType.GEOTEXT
										&& geo.getType() != GeometryType.GEOCOMPOUND) {
									result = Geometrist.resample(geo, resampleType, tolerance);

									if (result != null) {
										environment.getMapControl().getEditHistory().add(EditType.MODIFY, recordset, true);
										result.setStyle(geo.getStyle());
										recordset.edit();
										recordset.setGeometry(result);
										recordset.update();
									}
								}
							} finally {
								if (geo != null) {
									geo.dispose();
								}

								if (result != null) {
									result.dispose();
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
			environment.getMapControl().getMap().refresh();
		}
	}
}
