package com.supermap.desktop.CtrlAction.SceneOperator;

import com.supermap.data.CoordSysTranslator;
import com.supermap.data.Dataset;
import com.supermap.data.GeoPoint;
import com.supermap.data.GeoRegion;
import com.supermap.data.Geometry;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.PrjCoordSysType;
import com.supermap.data.ProjectionType;
import com.supermap.data.Rectangle2D;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.realspace.Camera;
import com.supermap.realspace.Layer3DDataset;
import com.supermap.realspace.Layer3DType;
import com.supermap.realspace.Selection3D;

public class CtrlActionSceneGeometryFlying extends CtrlAction {

	protected Double flyingCircleSpeedRatio = 2.0;

	public CtrlActionSceneGeometryFlying(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	public DesktopFlyingMode getFlyingMode() {
		return DesktopFlyingMode.FLYINGTO;
	}

	@Override
	public void run() {
		try {
			IFormScene formScene = (IFormScene) Application.getActiveApplication().getActiveForm();

			Geometry geometry = null;
			boolean needDisposeGeometry = false;
			boolean needInverse = false;
			PrjCoordSys prjCoordSys = null;
			DesktopFlyingMode flyingMode = getFlyingMode();
			Selection3D[] selection3Ds = formScene.getSceneControl().getScene().findSelection(true);
			Selection3D selection = selection3Ds[0];
			if (selection.getLayer().getType() == Layer3DType.DATASETVECTOR) {
				Recordset recordset = selection.toRecordset();
				geometry = recordset.getGeometry();
				needDisposeGeometry = true;
				Dataset dataset = ((Layer3DDataset) selection.getLayer()).getDataset();

				if (dataset.getPrjCoordSys().getType() != PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE && dataset.getPrjCoordSys().getProjection() != null
						&& dataset.getPrjCoordSys().getProjection().getType() != ProjectionType.PRJ_NONPROJECTION) {
					needInverse = true;
					prjCoordSys = dataset.getPrjCoordSys();
				}
			} 

			if (geometry != null) {
				Rectangle2D bounds = geometry.getBounds();
				if (needInverse && prjCoordSys != null) {
					if (geometry.getBounds().isEmpty()) {
						Point2Ds point2Ds = new Point2Ds();
						point2Ds.add(geometry.getInnerPoint());
						CoordSysTranslator.inverse(point2Ds, prjCoordSys);
						geometry = new GeoPoint(point2Ds.getItem(0));
					} else {
						Point2D leftBottomPoint = new Point2D(bounds.getLeft(), bounds.getBottom());
						Point2D rightBottomPoint = new Point2D(bounds.getRight(), bounds.getBottom());
						Point2D rightTopPoint = new Point2D(bounds.getRight(), bounds.getTop());
						Point2D leftTopPoint = new Point2D(bounds.getLeft(), bounds.getTop());
						Point2Ds point2Ds = new Point2Ds();
						point2Ds.add(leftBottomPoint);
						point2Ds.add(rightBottomPoint);
						point2Ds.add(rightTopPoint);
						point2Ds.add(leftTopPoint);
						point2Ds.add(leftBottomPoint);
						CoordSysTranslator.inverse(point2Ds, prjCoordSys);
						geometry = new GeoRegion(point2Ds);
					}
				}
				if (flyingMode == DesktopFlyingMode.FLYINGTO) {
					if (geometry.getBounds().isEmpty()) {
						Camera flyCamera = formScene.getSceneControl().getScene().getCamera();
						flyCamera.setLatitude(geometry.getInnerPoint().getX());
						flyCamera.setLatitude(geometry.getInnerPoint().getY());
						flyCamera.setAltitude(1000);
						formScene.getSceneControl().getScene().fly(flyCamera, 0);
					} else {
						formScene.getSceneControl().getScene().ensureVisible(geometry.getBounds(), 0);
					}
				} else if (flyingMode == DesktopFlyingMode.FLYINGPLAY) {
					if (geometry.getBounds().isEmpty()) {
						Camera flyCamera = formScene.getSceneControl().getScene().getCamera();
						flyCamera.setLatitude(geometry.getInnerPoint().getX());
						flyCamera.setLatitude(geometry.getInnerPoint().getY());
						flyCamera.setAltitude(1000);
						formScene.getSceneControl().getScene().fly(flyCamera);
					} else {
						formScene.getSceneControl().getScene().ensureVisible(geometry.getBounds());
					}
				} else {
					if (this.flyingCircleSpeedRatio < 0.0000000001) {
						formScene.getSceneControl().getScene().flyCircle(geometry, 2);
					} else {
						formScene.getSceneControl().getScene().flyCircle(geometry, this.flyingCircleSpeedRatio);
					}
				}
				if (needDisposeGeometry) {
					geometry.dispose();
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		IFormScene formScene = (IFormScene) Application.getActiveApplication().getActiveForm();
		Selection3D[] selection3Ds = formScene.getSceneControl().getScene().findSelection(true);
		if (selection3Ds.length > 0) {
			Selection3D selection = selection3Ds[0];
			if (selection.getLayer().getType() == Layer3DType.DATASETVECTOR) {
				enable = true;
			}
		}
		return enable;
	}

	public enum DesktopFlyingMode {
		FLYINGTO, FLYINGPLAY, FLYINGCIRCLE,
	}
}
