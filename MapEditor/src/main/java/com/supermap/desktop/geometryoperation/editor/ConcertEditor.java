package com.supermap.desktop.geometryoperation.editor;

/**
 * @author lixiaoyao
 */

import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.EditType;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoRegion;
import com.supermap.data.Geometrist;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.QueryParameter;
import com.supermap.data.Recordset;
import com.supermap.data.SpatialQueryMode;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometryoperation.EditControllerAdapter;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.IEditController;
import com.supermap.desktop.geometryoperation.NullEditController;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.ui.Action;
import com.supermap.ui.ActionChangedEvent;
import com.supermap.ui.GeometryEvent;
import com.supermap.ui.GeometrySelectedEvent;

import java.util.List;

public class ConcertEditor extends AbstractEditor {
	boolean isCanEdit = false;
	Geometry oldGeometry = null;
	Point2D changePoint2D = new Point2D();
	Point2D moveAfterPoint2D = new Point2D();

	private IEditController concertEditController = new EditControllerAdapter() {
		@Override
		public void actionChanged(EditEnvironment environment, ActionChangedEvent e) {
			if (e.getOldAction() == Action.VERTEXEDIT) {
				// @formatter:off
				// 组件在很多情况下会自动结束编辑状态，比如右键，比如框选一堆对象，
				// 比如当前操作对象所在图层变为不可编辑状态，这时候桌面自定义的 Editor 还没有结束编辑，处理一下
				// @formatter:on
				environment.stopEditor();
			}
		}

		@Override
		public void geometrySelected(EditEnvironment environment, GeometrySelectedEvent arg0) {
			isCanEdit = isCanConcertEdit(environment);
		}

		@Override
		public void geometryModified(EditEnvironment environment, GeometryEvent event) {
			if (isCanEdit) {
				runConcertEdit(environment);
			}
		}
	};


	@Override
	public void activate(EditEnvironment environment) {
		isCanEdit = isCanConcertEdit(environment);
		environment.getMapControl().setAction(Action.VERTEXEDIT);

		if (environment.getMapControl().getAction() != Action.VERTEXEDIT) {
			// 因为这个功能是组件控制的，有一些导致 Action 设置失败的原因我们的封装无法知道，因此在这里处理一下漏网之鱼
			environment.stopEditor();
		} else {
			environment.setEditController(this.concertEditController);
			environment.getMap().refresh();
		}
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		environment.getMapControl().setAction(Action.SELECT2);
		environment.setEditController(NullEditController.instance());
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return environment.getEditProperties().getSelectedGeometryCount() == 1
				&& environment.getEditProperties().getEditableSelectedGeometryCount() == 1
				&& ListUtilities.isListOnlyContain(environment.getEditProperties().getSelectedGeometryTypes(), GeometryType.GEOLINE,
				GeometryType.GEOREGION);
	}

	@Override
	public boolean check(EditEnvironment environment) {
		return environment.getEditor() instanceof ConcertEditor;
	}

	/**
	 * 判断当前选中的对象有没有共同节点的对象，如果有就返回true，当作协调编辑处理
	 * 否则，当作节点编辑处理
	 */
	private boolean isCanConcertEdit(EditEnvironment environment) {
		List<Layer> layers = environment.getEditProperties().getSelectedLayers();
		Recordset selectRecordset = null;
		Recordset sourceRecordset = null;
		boolean result = false;

		if (layers.size() == 0) {
			return false;
		}
		for (Layer layer : layers) {
			selectRecordset = layer.getSelection().toRecordset();
			sourceRecordset = ((DatasetVector) layer.getDataset()).getRecordset(false, CursorType.DYNAMIC);
		}

		/*Geometry selectGeometry = selectRecordset.getGeometry();
		int selectID = selectGeometry.getID();
		oldGeometry = selectGeometry;
		sourceRecordset.moveFirst();

		try {
			for (int i = 0; i < sourceRecordset.getRecordCount(); ++i) {
				Geometry tempGeometry = sourceRecordset.getGeometry();
				int sourceID = tempGeometry.getID();
				if (sourceID == selectID) {
					sourceRecordset.moveNext();
					continue;
				}
				boolean resultHasCommonPoint = Geometrist.hasCommonPoint(selectGeometry, tempGeometry);
				if (result == false && resultHasCommonPoint == true) {
					result = true;
					break;
				}
				sourceRecordset.moveNext();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex.toString());
		} finally {
			if (selectRecordset != null) {
				selectRecordset.close();
				selectRecordset.dispose();
			}
			if (sourceRecordset != null) {
				sourceRecordset.close();
				sourceRecordset.dispose();
			}
		}*/

		Recordset resultRecordset=queryGeometryTouchSelectedGeometry(selectRecordset,selectRecordset.getDataset());
		if (resultRecordset!=null)
		{
			result=true;
		}
		return result;
	}

	private Recordset queryGeometryTouchSelectedGeometry(Recordset selectedRecordset,DatasetVector nowDatasetVector){
		Recordset resultRecordset=null;

		QueryParameter parameter=new QueryParameter();
		//parameter.setAttributeFilter();
		parameter.setCursorType(CursorType.STATIC);
		parameter.setSpatialQueryMode(SpatialQueryMode.TOUCH);
		parameter.setSpatialQueryObject(selectedRecordset);

		resultRecordset=nowDatasetVector.query(parameter);
		return resultRecordset;
	}

	/**
	 * 进行协调编辑
	 */
	private void runConcertEdit(EditEnvironment environment) {
		environment.getMapControl().getEditHistory().batchBegin();
		List<Layer> layers = MapUtilities.getLayers(environment.getMap());
		Recordset selectRecordset = null;
		Recordset sourceRecordset = null;

		for (Layer layer : layers) {
			if (layer.isEditable() && layer.getSelection().getCount() == 1
					&& (layer.getDataset().getType() == DatasetType.LINE
					|| layer.getDataset().getType() == DatasetType.REGION)) {

				selectRecordset = layer.getSelection().toRecordset();
				sourceRecordset = ((DatasetVector) layer.getDataset()).getRecordset(false, CursorType.DYNAMIC);
				Geometry selectGeometry = selectRecordset.getGeometry();
				boolean moveOrDelete = isMoveOrDelete(selectGeometry);
				int selectID = selectGeometry.getID();

				sourceRecordset.moveFirst();

				while (!sourceRecordset.isEOF()) {
					Geometry tempGeometry = sourceRecordset.getGeometry();
					int sourceID = tempGeometry.getID();
					if (sourceID != selectID) {
						boolean resultHasCommonPoint = Geometrist.hasCommonPoint(this.oldGeometry, tempGeometry);
						if (resultHasCommonPoint) {
							environment.getMapControl().getEditHistory().add(EditType.MODIFY, sourceRecordset, true);
							Geometry newGeometry = null;
							if (tempGeometry.getType() == GeometryType.GEOLINE) {
								newGeometry = lineNodeEdit(tempGeometry, moveOrDelete);
							} else if (tempGeometry.getType() == GeometryType.GEOREGION) {
								newGeometry = regionNodeEdit(tempGeometry, moveOrDelete);
							}
							sourceRecordset.edit();
							sourceRecordset.setGeometry(newGeometry);
							sourceRecordset.update();
							newGeometry.dispose();
						}
					}
					sourceRecordset.moveNext();
				}
			}
		}
		if (sourceRecordset != null) {
			sourceRecordset.close();
			sourceRecordset.dispose();
		}
		environment.getMapControl().getEditHistory().batchEnd();
		environment.getMap().refresh();
		environment.getMapControl().revalidate();
		isCanEdit = isCanConcertEdit(environment);
	}

	/**
	 * 获取当前移动的节点是那个节点并保留没移动前的节点和移动后新的节点
	 */
	private Point2D moveNode(Point2Ds oldPoints, Point2Ds selectPoints) {
		Point2D movePoint2D = new Point2D();
		try {
			for (int i = 0; i < oldPoints.getCount(); ++i) {
				Point2D oldPoint = oldPoints.getItem(i);
				Point2D selectPoint = selectPoints.getItem(i);
				if (!oldPoint.equals(selectPoint)) {
					movePoint2D = oldPoint;
					this.moveAfterPoint2D = selectPoint;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex.toString());
		}
		return movePoint2D;
	}

	/**
	 * 获取当前删除的节点是那个节点并保留删除前那个节点的信息
	 */
	private Point2D deleteNode(Point2Ds oldPoints, Point2Ds selectPoints) {
		Point2D deletePoint2D = new Point2D();
		try {
			for (int i = 0; i < oldPoints.getCount(); ++i) {
				Point2D oldPoint = oldPoints.getItem(i);
				for (int j = 0; j < selectPoints.getCount(); ++j) {
					Point2D selectPoint = selectPoints.getItem(j);
					if (oldPoint.equals(selectPoint)) {
						break;
					} else if (!oldPoint.equals(selectPoint) && j == (selectPoints.getCount() - 1)) {
						deletePoint2D = oldPoint;
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex.toString());
		}
		return deletePoint2D;
	}

	private Point2Ds RegionPoint2Ds(Geometry geometry) {
		GeoRegion tempGeoRegion = (GeoRegion) geometry;
		Point2Ds resultPoint2Ds = new Point2Ds();
		try {
			for (int i = 0; i < tempGeoRegion.getPartCount(); ++i) {
				Point2Ds tempPoint2Ds = tempGeoRegion.getPart(i);
				for (int j = 0; j < tempPoint2Ds.getCount(); ++j) {
					Point2D tempPoint2D = tempPoint2Ds.getItem(j);
					resultPoint2Ds.add(tempPoint2D);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex.toString());
		}
		return resultPoint2Ds;
	}

	private Point2Ds LinePoint2Ds(Geometry geometry) {
		GeoLine tempGeoLine = (GeoLine) geometry;
		Point2Ds resultPoint2Ds = new Point2Ds();
		try {
			for (int i = 0; i < tempGeoLine.getPartCount(); ++i) {
				Point2Ds tempPoint2Ds = tempGeoLine.getPart(i);
				for (int j = 0; j < tempPoint2Ds.getCount(); ++j) {
					Point2D tempPoint2D = tempPoint2Ds.getItem(j);
					resultPoint2Ds.add(tempPoint2D);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex.toString());
		}
		return resultPoint2Ds;
	}

	/**
	 * 判断当前进行的是移动节点还是删除节点操作，
	 */
	private boolean isMoveOrDelete(Geometry nowSelectGeometry) {
		boolean result = false;
		Point2Ds selectGeoPoints = new Point2Ds();
		Point2Ds oldGeoPoints = new Point2Ds();

		if (this.oldGeometry.getType() == GeometryType.GEOLINE) {
			selectGeoPoints = LinePoint2Ds(nowSelectGeometry);
			oldGeoPoints = LinePoint2Ds(this.oldGeometry);
		} else if (this.oldGeometry.getType() == GeometryType.GEOREGION) {
			selectGeoPoints = RegionPoint2Ds(nowSelectGeometry);
			oldGeoPoints = RegionPoint2Ds(this.oldGeometry);
		}

		if (selectGeoPoints.getCount() == oldGeoPoints.getCount()) {
			this.changePoint2D = moveNode(oldGeoPoints, selectGeoPoints);
		} else {
			this.changePoint2D = deleteNode(oldGeoPoints, selectGeoPoints);
			result = true;
		}
		return result;
	}

	private Geometry regionNodeEdit(Geometry inputGeometry, boolean moveOrDelete) {
		Point2Ds inputGeoPoint2Ds = RegionPoint2Ds(inputGeometry);

		try {
			int deleteID = -1;
			for (int i = 0; i < inputGeoPoint2Ds.getCount(); ++i) {
				Point2D tempPoint2D = inputGeoPoint2Ds.getItem(i);
				if (tempPoint2D.equals(this.changePoint2D)) {
					if (moveOrDelete) {
						deleteID = i;
						break;
					} else {
						inputGeoPoint2Ds.setItem(i, this.moveAfterPoint2D);
						break;
					}
				}
			}

			if (deleteID != -1) {
				inputGeoPoint2Ds.remove(deleteID);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex.toString());
		}

		GeoRegion result = (GeoRegion) inputGeometry;
		result.setPart(0, inputGeoPoint2Ds);
		Geometry newGeometry = (Geometry) result;

		return newGeometry;
	}

	private Geometry lineNodeEdit(Geometry inputGeometry, boolean moveOrDelete) {
		Point2Ds inputGeoPoint2Ds = LinePoint2Ds(inputGeometry);

		try {
			int deleteID = -1;
			for (int i = 0; i < inputGeoPoint2Ds.getCount(); ++i) {
				Point2D tempPoint2D = inputGeoPoint2Ds.getItem(i);
				if (tempPoint2D.equals(this.changePoint2D)) {
					if (moveOrDelete) {
						deleteID = i;
						break;
					} else {
						inputGeoPoint2Ds.setItem(i, this.moveAfterPoint2D);
						break;
					}
				}
			}

			if (deleteID != -1) {
				inputGeoPoint2Ds.remove(deleteID);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex.toString());
		}

		GeoLine result = (GeoLine) inputGeometry;
		result.setPart(0, inputGeoPoint2Ds);
		Geometry newGeometry = (Geometry) result;

		return newGeometry;
	}
}
