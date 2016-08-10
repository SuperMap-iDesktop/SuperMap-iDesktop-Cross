package com.supermap.desktop.geometryoperation.editor;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.control.JDialogSmoothRatio;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilities.ArrayUtilities;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;

import java.util.ArrayList;

public class SmoothEditor extends AbstractEditor {

	@Override
	public void activate(EditEnvironment environment) {
		try {
			JDialogSmoothRatio dialog = new JDialogSmoothRatio();

			if (dialog.showDialog() == DialogResult.OK) {
				smooth(environment, dialog.getSmoothRatio());
			}
		} finally {
			environment.activateEditor(NullEditor.INSTANCE);
		}
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return ListUtilities
				.isListOnlyContain(environment.getEditProperties().getEditableSelectedGeometryTypes(), GeometryType.GEOLINE, GeometryType.GEOREGION);
	}

	private void smooth(EditEnvironment environment, int smoothRatio) {
		try {
			environment.getMapControl().getEditHistory().batchBegin();
			ArrayList<Layer> layers = MapUtilities.getLayers(environment.getMapControl().getMap());

			for (Layer layer : layers) {
				if (layer.isEditable() && layer.getSelection().getCount() > 0 && layer.getDataset().getType() != DatasetType.POINT
						&& layer.getDataset().getType() != DatasetType.TEXT) {
					Recordset recordset = layer.getSelection().toRecordset();
					// 记录当前图层光滑操作成功的对象的ID，在操作结束的时候重置一下它们的选中，用以刷新属性面板等
					ArrayList<Integer> succeededIDs = new ArrayList<>();

					try {
						while (!recordset.isEOF()) {
							Geometry geometry = recordset.getGeometry();

							if (geometry.getType() != GeometryType.GEOBOX && geometry.getType() != GeometryType.GEOPOINT
									&& geometry.getType() != GeometryType.GEOTEXT && geometry.getType() != GeometryType.GEOCOMPOUND) {
								Geometry result = getGeometry(geometry, smoothRatio);

								if (result != null) {
									environment.getMapControl().getEditHistory().add(EditType.MODIFY, recordset, true);
									result.setStyle(geometry.getStyle());
									recordset.edit();
									recordset.setGeometry(result);
									recordset.update();
									succeededIDs.add(recordset.getID());

									result.dispose();
								}

								geometry.dispose();
								recordset.moveNext();
							}
						}
					} finally {
						if (recordset != null) {
							recordset.close();
							recordset.dispose();
						}
					}

					layer.getSelection().clear();
					layer.getSelection().addRange(ArrayUtilities.convertToInt(succeededIDs.toArray(new Integer[succeededIDs.size()])));
				}
			}

			environment.getMapControl().getEditHistory().batchEnd();
			environment.getMap().refresh();
			environment.getMapControl().revalidate();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private Geometry getGeometry(Geometry befroeGeometry, int smoothRatio) {
		Geometry result = null;

		try {
			if (befroeGeometry instanceof GeoLine) {
				GeoLine line = new GeoLine();
				GeoLine beforeLine = (GeoLine) befroeGeometry;

				for (int i = 0; i < beforeLine.getPartCount(); i++) {
					Point2Ds pnts = beforeLine.getPart(i);
					Point2Ds temp = Geometrist.smooth(pnts, smoothRatio);
					line.addPart(temp);
				}

				if (line.getPartCount() > 0) {
					result = line;
				} else {
					line.dispose();
				}
			} else if (befroeGeometry instanceof GeoRegion) {
				GeoRegion region = new GeoRegion();
				GeoRegion beforeRegion = (GeoRegion) befroeGeometry;

				for (int i = 0; i < beforeRegion.getPartCount(); i++) {
					Point2Ds pnts = beforeRegion.getPart(i);
					region.addPart(Geometrist.smooth(pnts, smoothRatio));
				}

				if (region.getPartCount() > 0) {
					result = region;
				} else {
					region.dispose();
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex.getMessage());
		}
		return result;
	}
}
