package com.supermap.desktop.geometryoperation.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.EditType;
import com.supermap.data.FieldInfos;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoPoint;
import com.supermap.data.GeoRegion;
import com.supermap.data.GeoStyle;
import com.supermap.data.Geometrist;
import com.supermap.data.Geometry;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.IRegionConvertor;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
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
import com.supermap.mapping.Selection;
import com.supermap.ui.Action;
import com.supermap.ui.TrackMode;

public class OffsetEditor extends AbstractEditor {

	private static final String TAG_OFFSET = "Tag_offsetTracking";
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
			int trackingIndex = environment.getMap().getTrackingLayer().indexOf(TAG_OFFSET);

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
					Map<String, Object> values = new HashMap<String, Object>();
					FieldInfos fieldInfos = recordset.getFieldInfos();
					Object[] fieldValues = recordset.getValues();
					for (int i = 0; i < fieldValues.length; i++) {
						if (!fieldInfos.get(i).isSystemField()) {
							values.put(fieldInfos.get(i).getName(), fieldValues[i]);
						}
					}
					recordset.addNew(desGeometry, values);
					recordset.update();
					desGeometry.dispose();
				}
				environment.getMapControl().getEditHistory().add(EditType.ADDNEW, recordset, true);

				Selection[] selections = environment.getMap().findSelection(true);
				for (Selection selection : selections) {
					selection.clear();
				}
				MapUtilities.clearTrackingObjects(environment.getMap(), TAG_OFFSET);
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
	 * @param environment
	 * @param mouseLocation
	 */
	// @formatter:on
	private void showPreview(EditEnvironment environment, Point2D mouseLocation) {
		OffsetEditModel editModel = (OffsetEditModel) environment.getEditModel();

		try {
			Point2Ds points = getPoint2Ds(editModel.desGeometry);

			if (editModel.desDataset.getTolerance().getNodeSnap() == 0) {
				editModel.desDataset.getTolerance().setDefault();
			}

			Object[] data = EditorUtilties.getMinDistance(mouseLocation, points, editModel.desDataset.getTolerance().getNodeSnap());
			double distance = (Double) data[0];
			int segment = (Integer) data[2];

			if (segment == points.getCount() - 1) {
				segment--;
			}

			if (!EditorUtilties.isPntLeft(points.getItem(segment), points.getItem(segment + 1), mouseLocation)) {
				distance = -distance;
			}
			editModel.setMsg(MessageFormat.format(MapEditorProperties.getString("String_Tip_Edit_OffsetDistance"), distance));

			GeoLine tempLine = null;
			if (editModel.desGeometry instanceof GeoLine) {
				tempLine = (GeoLine) editModel.desGeometry;
			} else if (editModel.desGeometry instanceof GeoRegion) {
				tempLine = ((GeoRegion) editModel.desGeometry).convertToLine();
			}
			GeoLine resultLine = Geometrist.computeParallel(tempLine, distance);

			MapUtilities.clearTrackingObjects(environment.getMap(), TAG_OFFSET);
			Geometry resultGeometry = null;
			if (editModel.desGeometry instanceof GeoLine) {
				resultGeometry = resultLine;
			} else if (editModel.desGeometry instanceof GeoRegion) {
				resultGeometry = resultLine.convertToRegion();
			}
			resultGeometry.setStyle(getTrackingStyle());
			environment.getMap().getTrackingLayer().add(resultGeometry, TAG_OFFSET);
			environment.getMap().refreshTrackingLayer();
			resultGeometry.dispose();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private GeoStyle getTrackingStyle() {
		GeoStyle trackingStyle = new GeoStyle();
		trackingStyle.setLineSymbolID(2);
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
		MapUtilities.clearTrackingObjects(environment.getMap(), TAG_OFFSET);
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
