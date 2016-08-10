package com.supermap.desktop.geometryoperation.editor;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.*;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.control.JDialogRotateParams;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilities.ArrayUtilities;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;

import java.util.ArrayList;

public class RotateEditor extends AbstractEditor {

	@Override
	public void activate(EditEnvironment environment) {
		try {
			Point2D center = getCenter(environment);
			JDialogRotateParams dialog = new JDialogRotateParams(center);
			if (dialog.showDialog() == DialogResult.OK) {
				rotate(environment, new Point2D(dialog.getCenterX(), dialog.getCenterY()), dialog.getAngle());
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			environment.activateEditor(NullEditor.INSTANCE);
		}
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return environment.getEditProperties().getEditableSelectedGeometryCount() > 0
				&& ListUtilities.isListOnlyContain(environment.getEditProperties().getEditableSelectedGeometryTypeFeatures(), ICompoundFeature.class,
						ITextFeature.class, IPointFeature.class, IRegionFeature.class, ILineFeature.class);
	}

	private Point2D getCenter(EditEnvironment environment) {
		Point2D center = Point2D.getEMPTY();

		if (environment.getEditProperties().getEditableSelectedGeometryCount() == 1) {

			// 只有一个对象，直接取对象的内点，作为旋转基点
			Recordset recordset = environment.getActiveEditableLayer().getSelection().toRecordset();
			recordset.moveFirst();
			Geometry selectedGeometry = recordset.getGeometry();
			center = selectedGeometry.getInnerPoint();
			recordset.close();
			recordset.dispose();
			selectedGeometry.dispose();
		} else {

			// 多个对象，取所有对象外接矩形的中心点，作为旋转基点
			Rectangle2D bounds = new Rectangle2D(0, 0, 0, 0);
			ArrayList<Layer> layers = MapUtilities.getLayers(environment.getMap());

			for (Layer layer : layers) {
				if (layer.isEditable() && layer.getSelection().getCount() > 0) {
					Recordset recordset = layer.getSelection().toRecordset();

					try {
						if (DoubleUtilities.equals(bounds.getWidth(), 0d, 8)) {
							bounds = recordset.getBounds();
						} else {
							bounds.union(recordset.getBounds());
						}
					} finally {
						recordset.close();
						recordset.dispose();
					}
				}
			}
			center = bounds.getCenter();
		}
		return center;
	}

	private void rotate(EditEnvironment environment, Point2D basePoint, double angle) {
		environment.getMapControl().getEditHistory().batchBegin();

		try {
			ArrayList<Layer> layers = MapUtilities.getLayers(environment.getMap());

			for (Layer layer : layers) {
				if (layer.isEditable() && layer.getSelection().getCount() > 0) {
					Recordset recordset = layer.getSelection().toRecordset();
					// 记录当前图层旋转成功的对象的ID，在操作结束的时候重置一下它们的选中，用以刷新属性面板等
					ArrayList<Integer> succeededIDs = new ArrayList<>();

					try {
						// 这句话会导致 recordset 的指针到最后
						environment.getMapControl().getEditHistory().add(EditType.MODIFY, recordset, false);
						recordset.moveFirst();

						while (!recordset.isEOF()) {
							Geometry geo = recordset.getGeometry();

							try {
								geo.rotate(basePoint, angle);
								recordset.edit();
								recordset.setGeometry(geo);
								recordset.update();
								succeededIDs.add(recordset.getID());
							} finally {
								if (geo != null) {
									geo.dispose();
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

					layer.getSelection().clear();
					layer.getSelection().addRange(ArrayUtilities.convertToInt(succeededIDs.toArray(new Integer[succeededIDs.size()])));
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			environment.getMapControl().getEditHistory().batchEnd();
			environment.getMapControl().getMap().refresh();
			environment.getMapControl().revalidate();
		}
	}
}
