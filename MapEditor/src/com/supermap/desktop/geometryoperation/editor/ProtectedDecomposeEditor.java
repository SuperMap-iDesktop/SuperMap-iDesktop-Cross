package com.supermap.desktop.geometryoperation.editor;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
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
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.utilties.ArrayUtilties;
import com.supermap.desktop.utilties.ListUtilties;
import com.supermap.desktop.utilties.TabularUtilties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Selection;

public class ProtectedDecomposeEditor extends AbstractEditor {

	private List<Geometry> m_listgeometrys;
	private Geometry[] m_geometrys = null;

	@Override
	public void activate(EditEnvironment environment) {
		m_listgeometrys = new ArrayList<Geometry>();
		Recordset recordset = null;
		Geometry geometry = null;
		try {
			IFormMap formMap = environment.getFormMap();
			if (formMap != null) {
				List<Integer> resultIDs = new ArrayList<>();
				formMap.getMapControl().getEditHistory().batchBegin();
				Layer layer = formMap.getMapControl().getActiveEditableLayer();
				if (layer != null && (layer.getDataset().getType() == DatasetType.REGION || layer.getDataset().getType() == DatasetType.CAD)) {
					recordset = layer.getSelection().toRecordset();
					if (recordset != null) {
						recordset.getBatch().setMaxRecordCount(200);
						recordset.getBatch().begin();
						recordset.moveLast();
						Geometry[] geometrys = null;
						// Boolean isMove = true;
						while (!recordset.isBOF()) {
							// isMove = true;
							geometrys = null;
							geometry = recordset.getGeometry();
							if (geometry != null && geometry.getType() == GeometryType.GEOREGION && ((GeoRegion) geometry).getPartCount() > 1) {
								geometrys = ((GeoRegion) geometry).protectedDecompose();
							}
							// 复合对象
							if (geometry.getType() == GeometryType.GEOCOMPOUND) {
								geometrys = ((GeoCompound) geometry).divide(false);// 深层分解，分解的对象为简单对象
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
								// isMove = false;
								Map<String, Object> values = new HashMap<String, Object>();
								FieldInfos fieldInfos = recordset.getFieldInfos();
								Object[] fieldValues = recordset.getValues();
								formMap.getMapControl().getEditHistory().add(EditType.DELETE, recordset, true);
								// if (resultIDs.Count>0&& resultIDs.Contains(recordset.GetID()))
								// {
								// resultIDs.Remove(recordset.GetID());
								// }
								Boolean dr = recordset.delete();
								for (int i = 0; i < fieldValues.length; i++) {
									if (!fieldInfos.get(i).isSystemField()) {
										values.put(fieldInfos.get(i).getName(), fieldValues[i]);
									}
								}
								for (int j = 0; j < geometrys.length; j++) {
									recordset.addNew(geometrys[j], values);
									resultIDs.add(recordset.getID());
									formMap.getMapControl().getEditHistory().add(EditType.ADDNEW, recordset, true);
								}
							}
							// if (isMove)
							// {
							recordset.movePrev();
							// }
							// else
							// {
							// recordset.SeekID(recordset.GetID());
							// }
						}
						recordset.getBatch().update();
						formMap.getMapControl().getEditHistory().batchEnd();
						layer.getSelection().clear();
						if (resultIDs.size() > 0) {
							layer.getSelection().addRange(ArrayUtilties.convertToInt(resultIDs.toArray(new Integer[resultIDs.size()])));
							TabularUtilties.refreshTabularForm((DatasetVector) layer.getDataset());
							// _Toolkit.InvokeGeometrySelectedEvent(formMap.MapControl, new SuperMap.UI.GeometrySelectedEventArgs(resultIDs.Count));
							Application.getActiveApplication().getOutput()
									.output(MessageFormat.format(MapEditorProperties.getString("String_GeometryEdit_DecomposeSuccess"), resultIDs.size()));
						} else {
							Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_OutputMessage"));
						}
						formMap.getMapControl().getMap().refresh();
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			recordset.getBatch().update();
			if (geometry != null) {
				geometry.dispose();
			}

			if (recordset != null) {
				recordset.close();
				recordset.dispose();
			}
		}
	}

	private void divideCompound(GeoCompound geoCompound) {
		for (int index = 0; index < geoCompound.getPartCount(); index++) {
			this.m_geometrys = null;
			Geometry currentgeometry = geoCompound.getPart(index);
			if (currentgeometry.getType() == GeometryType.GEOLINE) {
				m_geometrys = new Geometry[((GeoLine) currentgeometry).getPartCount()];
				for (int i = 0; i < ((GeoLine) currentgeometry).getPartCount(); i++) {
					m_geometrys[i] = new GeoLine(((GeoLine) currentgeometry).getPart(i));
				}
			} else if (currentgeometry.getType() == GeometryType.GEOLINE3D) {
				m_geometrys = new Geometry[((GeoLine3D) currentgeometry).getPartCount()];
				for (int i = 0; i < ((GeoLine3D) currentgeometry).getPartCount(); i++) {
					m_geometrys[i] = new GeoLine3D(((GeoLine3D) currentgeometry).getPart(i));
				}
			} else if (currentgeometry.getType() == GeometryType.GEOREGION) {
				if (((GeoRegion) currentgeometry).getPartCount() > 1) {
					m_geometrys = ((GeoRegion) currentgeometry).protectedDecompose();
				} else {
					m_geometrys = new Geometry[1];
					m_geometrys[0] = currentgeometry;
				}
			} else if (currentgeometry.getType() == GeometryType.GEOREGION3D) {
				m_geometrys = new Geometry[((GeoRegion3D) currentgeometry).getPartCount()];
				for (int i = 0; i < ((GeoRegion3D) currentgeometry).getPartCount(); i++) {
					m_geometrys[i] = new GeoRegion3D(((GeoRegion3D) currentgeometry).getPart(i));
				}
			} else if (currentgeometry.getType() == GeometryType.GEOLINEM && ((GeoLineM) currentgeometry).getPartCount() > 1) {
				m_geometrys = new Geometry[((GeoLineM) currentgeometry).getPartCount()];
				for (int i = 0; i < ((GeoLineM) currentgeometry).getPartCount(); i++) {
					m_geometrys[i] = new GeoLineM(((GeoLineM) currentgeometry).getPart(i));
				}
			} else if (currentgeometry.getType() == GeometryType.GEOTEXT && ((GeoText) currentgeometry).getPartCount() > 1) {
				m_geometrys = new Geometry[((GeoText) currentgeometry).getPartCount()];
				for (int i = 0; i < ((GeoText) currentgeometry).getPartCount(); i++) {
					m_geometrys[i] = new GeoText(((GeoText) currentgeometry).getPart(i), ((GeoText) currentgeometry).getTextStyle());
					((GeoText) m_geometrys[i]).getPart(0).setRotation(((GeoText) currentgeometry).getPart(i).getRotation());
				}
			} else if (currentgeometry.getType() == GeometryType.GEOCOMPOUND) {
				divideCompound((GeoCompound) currentgeometry);
			} else {
				m_geometrys = new Geometry[1];
				m_geometrys[0] = currentgeometry;
			}
			ListUtilties.addArray(this.m_listgeometrys, this.m_geometrys);
		}
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		boolean result = false;
		Geometry geometry = null;
		Recordset recordset = null;
		IFormMap formMap = environment.getFormMap();

		try {
			if (formMap != null) {
				Layer layer = formMap.getMapControl().getActiveEditableLayer();
				if (layer != null) {
					Selection selection = layer.getSelection();
					if (selection.getCount() >= 1) {
						recordset = selection.toRecordset();
						recordset.moveFirst();
						while (!recordset.isEOF()) {
							geometry = recordset.getGeometry();
							if (geometry != null && (geometry.getType() == GeometryType.GEOREGION || geometry.getType() == GeometryType.GEOCOMPOUND)) {
								if (geometry instanceof GeoRegion) {
									result = ((GeoRegion) geometry).getPartCount() >= 2;
									break;
								}
								if (geometry instanceof GeoCompound) {
									result = ((GeoCompound) geometry).getPartCount() >= 2;
									break;
								}
							}
							// CommonToolkit.ReleaseGeometry(ref geometry);//不释放对象，大数据容易崩溃UGDC-1240
							recordset.moveNext();
						}
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			if (geometry != null) {
				geometry.dispose();
			}

			if (recordset != null) {
				recordset.close();
				recordset.dispose();
			}
		}
		return result;
	}
}
