package com.supermap.desktop.geometryoperation.CtrlAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.supermap.data.EditType;
import com.supermap.data.FieldInfos;
import com.supermap.data.GeoCompound;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoLine3D;
import com.supermap.data.GeoLineM;
import com.supermap.data.GeoRegion;
import com.supermap.data.GeoRegion3D;
import com.supermap.data.GeoText;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.mapping.Layer;

public class CtrlActionDecompose extends CtrlAction {

	public CtrlActionDecompose(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		Recordset recordset = null;
		Geometry geometry = null;
		try {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getMainFrame().getFormManager().getActiveForm();
			Layer layer = formMap.getMapControl().getActiveEditableLayer();
			List<Integer> resultIDs = new ArrayList<Integer>();
			recordset = layer.getSelection().toRecordset();
			formMap.getMapControl().getEditHistory().batchBegin();
			recordset.getBatch().setMaxRecordCount(200);
			recordset.getBatch().begin();
			recordset.moveLast();
			while (!recordset.isBOF()) {
				geometry = recordset.getGeometry();
				Geometry[] geometrys = null;

				if (geometry.getType() == GeometryType.GEOCOMPOUND) {
					geometrys = ((GeoCompound) geometry).divide(false);
					// CAD上的组合对象都是复杂对象，通过Divide能够分解到最简单对象
					// 只有一种情况，CAD上的岛洞数据是面对象需要再次分解，所以对CAD复杂对象深度分解的结果存在岛洞数据的情况需要单独再处理
					// 如果每一个对象不一次性分解完成，操作历史记录的时候会出现回退的时候多了一个中间对象
					List<Geometry> tempgeometrys = new ArrayList<Geometry>();
					Geometry[] tempgeometry = null;
					for (Geometry currentgeomentry : geometrys) {
						if (currentgeomentry.getType() == GeometryType.GEOREGION && ((GeoRegion) currentgeomentry).getPartCount() > 1) {
							tempgeometry = new Geometry[((GeoRegion) currentgeomentry).getPartCount()];
							for (int i = 0; i < ((GeoRegion) currentgeomentry).getPartCount(); i++) {
								tempgeometry[i] = new GeoRegion(((GeoRegion) currentgeomentry).getPart(i));
								tempgeometrys.add(tempgeometry[i]);
							}
						} else {
							tempgeometrys.add(currentgeomentry);
						}
					}
					// geometrys = tempgeometrys.toArray();
				} else if (geometry.getType() == GeometryType.GEOLINE && ((GeoLine) geometry).getPartCount() > 1) {
					geometrys = new Geometry[((GeoLine) geometry).getPartCount()];
					for (int i = 0; i < ((GeoLine) geometry).getPartCount(); i++) {
						geometrys[i] = new GeoLine(((GeoLine) geometry).getPart(i));
					}
				} else if (geometry.getType() == GeometryType.GEOLINE3D && ((GeoLine3D) geometry).getPartCount() > 1) {
					geometrys = new Geometry[((GeoLine3D) geometry).getPartCount()];
					for (int i = 0; i < ((GeoLine3D) geometry).getPartCount(); i++) {
						geometrys[i] = new GeoLine3D(((GeoLine3D) geometry).getPart(i));
					}
				} else if (geometry.getType() == GeometryType.GEOREGION && ((GeoRegion) geometry).getPartCount() > 1) {
					geometrys = new Geometry[((GeoRegion) geometry).getPartCount()];
					for (int i = 0; i < ((GeoRegion) geometry).getPartCount(); i++) {
						geometrys[i] = new GeoRegion(((GeoRegion) geometry).getPart(i));
					}
				} else if (geometry.getType() == GeometryType.GEOREGION3D && ((GeoRegion3D) geometry).getPartCount() > 1) {
					geometrys = new Geometry[((GeoRegion3D) geometry).getPartCount()];
					for (int i = 0; i < ((GeoRegion3D) geometry).getPartCount(); i++) {
						geometrys[i] = new GeoRegion3D(((GeoRegion3D) geometry).getPart(i));
					}
				} else if (geometry.getType() == GeometryType.GEOLINEM && ((GeoLineM) geometry).getPartCount() > 1) {
					geometrys = new Geometry[((GeoLineM) geometry).getPartCount()];
					for (int i = 0; i < ((GeoLineM) geometry).getPartCount(); i++) {
						geometrys[i] = new GeoLineM(((GeoLineM) geometry).getPart(i));
					}
				} else if (geometry.getType() == GeometryType.GEOTEXT && ((GeoText) geometry).getPartCount() > 1) {
					geometrys = new Geometry[((GeoText) geometry).getPartCount()];
					for (int i = 0; i < ((GeoText) geometry).getPartCount(); i++) {
						geometrys[i] = new GeoText(((GeoText) geometry).getPart(i), ((GeoText) geometry).getTextStyle());
						((GeoText) geometrys[i]).getPart(0).setRotation(((GeoText) geometry).getPart(i).getRotation());
					}
				}

				if (geometrys != null && geometrys.length > 1) {
					HashMap<String, Object> values = new HashMap<String, Object>();
					FieldInfos fieldInfos = recordset.getFieldInfos();
					Object[] fieldValues = recordset.getValues();

					formMap.getMapControl().getEditHistory().add(EditType.DELETE, recordset, true);
					Boolean dr = recordset.delete();
					for (int i = 0; i < fieldValues.length; i++) {
						if (!fieldInfos.get(i).isSystemField()) {
							// values.add(fieldInfos.get(i).getName(), fieldValues[i]);
						}
					}
					for (int j = 0; j < geometrys.length; j++) {
						recordset.addNew(geometrys[j], values);
						resultIDs.add(recordset.getID());
						formMap.getMapControl().getEditHistory().add(EditType.ADDNEW, recordset, true);
					}
				}
				recordset.movePrev();
			}
			recordset.getBatch().update();
			formMap.getMapControl().getEditHistory().batchEnd();
			layer.getSelection().clear();
			if (resultIDs.size() > 0) {
				// layer.getSelection().addRange(resultIDs.ToArray());
				// SuperMap.Desktop.UI.CommonToolkit.RefreshTabularForm(recordset.Dataset);
				// _Toolkit.InvokeGeometrySelectedEvent(formMap.MapControl, new GeometrySelectedEventArgs(resultIDs.Count));
				// Application.getActiveApplication().getOutput().output(String.Format(Properties.MapEditorResources.String_GeometryEdit_DecomposeSuccess,
				// resultIDs.Count));
			}
			formMap.getMapControl().getMap().refresh();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			recordset.getBatch().update();
			// CommonToolkit.ReleaseGeometry(ref geometry);
			// CommonToolkit.ReleaseRecordset(ref recordset);
		}
	}

	@Override
	public boolean enable() {
		Boolean bEnable = false;
		Recordset recordset = null;
		Geometry geometry = null;
		try {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getMainFrame().getFormManager().getActiveForm();
			if (formMap != null && formMap.getMapControl().getActiveEditableLayer() != null) {
				Layer layer = formMap.getMapControl().getActiveEditableLayer();
				if (layer.getSelection().getCount() >= 1) {
					recordset = layer.getSelection().toRecordset();
					recordset.moveFirst();
					while (!recordset.isEOF()) {
						geometry = recordset.getGeometry();
						if (geometry == null) {

						} else if (geometry.getType() == GeometryType.GEOCOMPOUND) {
							bEnable = true;
							break;
						} else if (geometry.getType() == GeometryType.GEOLINE) {
							bEnable = ((GeoLine) geometry).getPartCount() > 1;
							if (bEnable) {
								break;
							}
						} else if (geometry.getType() == GeometryType.GEOLINE3D) {
							bEnable = ((GeoLine3D) geometry).getPartCount() > 1;
							if (bEnable) {
								break;
							}
						} else if (geometry.getType() == GeometryType.GEOREGION) {
							bEnable = ((GeoRegion) geometry).getPartCount() > 1;
							if (bEnable) {
								break;
							}
						} else if (geometry.getType() == GeometryType.GEOREGION3D) {
							bEnable = ((GeoRegion3D) geometry).getPartCount() > 1;
							if (bEnable) {
								break;
							}
						} else if (geometry.getType() == GeometryType.GEOLINEM) {
							bEnable = ((GeoLineM) geometry).getPartCount() > 1;
							if (bEnable) {
								break;
							}
						} else if (geometry.getType() == GeometryType.GEOTEXT) {
							bEnable = ((GeoText) geometry).getPartCount() > 1;
							if (bEnable) {
								break;
							}
						}
						// CommonToolkit.ReleaseGeometry(ref geometry);//不释放对象，大数据容易崩溃UGDC-1240
						recordset.moveNext();
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		// finally
		// {
		// CommonToolkit.ReleaseGeometry(ref geometry);
		// CommonToolkit.ReleaseRecordset(ref recordset);
		// }

		return bEnable;
	}
}
