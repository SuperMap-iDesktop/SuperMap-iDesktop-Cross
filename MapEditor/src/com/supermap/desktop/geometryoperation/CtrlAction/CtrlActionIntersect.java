package com.supermap.desktop.geometryoperation.CtrlAction;

import java.util.List;
import java.util.Map;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.EditType;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoStyle;
import com.supermap.data.Geometrist;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.geometry.Abstract.ILineConvertor;
import com.supermap.desktop.geometry.Abstract.IRegionConvertor;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.geometryoperation.JDialogFieldOperationSetting;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.mapping.Layer;

public class CtrlActionIntersect extends CtrlAction {

	FormMap formMap;
	Layer editLayer;

	public CtrlActionIntersect(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			this.formMap = (FormMap) Application.getActiveApplication().getActiveForm();
			formMap.getEditState().checkEnable();
			// 设置目标数据集类型
			DatasetType datasetType = DatasetType.CAD;
			if (formMap.getEditState().getSelectedGeometryTypes().size() == 1) {
				if (formMap.getEditState().getSelectedGeometryTypes().get(0) == GeometryType.GEOCIRCLE3D
						|| formMap.getEditState().getSelectedGeometryTypes().get(0) == GeometryType.GEOPIE3D
						|| formMap.getEditState().getSelectedGeometryTypes().get(0) == GeometryType.GEOREGION3D) {
					datasetType = DatasetType.REGION3D;
				} else {
					datasetType = DatasetType.REGION;
				}
			}
			JDialogFieldOperationSetting form = new JDialogFieldOperationSetting("求交", formMap.getMapControl().getMap(), datasetType);
			if (form.showDialog() == DialogResult.OK) {
				this.editLayer = form.getEditLayer();
				intersect(form.getPropertyData(), 2);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public void intersect(Map<String, Object> propertyData, int type) {
		Recordset recordset = null;
		try {
			Geometry geometry = null;
			GeoStyle resultStyle = null;
			Geometry geometryIntersect = null;
			this.formMap.getMapControl().getEditHistory().batchBegin();
			List<Layer> layers = MapUtilties.getLayers(this.formMap.getMapControl().getMap());
			for (Layer layer : layers) {
				if (layer.getSelection() != null && layer.getSelection().getCount() > 0) {
					recordset = layer.getSelection().toRecordset();
					layer.getSelection().clear();
					Boolean isIntersectInitialGeometry = true;
					if (recordset != null) {
						recordset.getBatch().begin();
						while (!recordset.isEOF()) {
							Geometry geoGeometryTemp = null;
							if (layer.getDataset().getType() == DatasetType.CAD) {
								IRegionConvertor regionConvertor = (IRegionConvertor) DGeometryFactory.create(recordset.getGeometry());
								geoGeometryTemp = regionConvertor.convertToRegion(120);
								if (geoGeometryTemp == null) {
									ILineConvertor lineConvertor = (ILineConvertor) DGeometryFactory.create(recordset.getGeometry());
									geoGeometryTemp = lineConvertor.convertToLine(120);
								}
								if (geoGeometryTemp == null) {
									geoGeometryTemp = recordset.getGeometry();
								}
							} else if (layer.getDataset().getType() == DatasetType.REGION) {
								geoGeometryTemp = recordset.getGeometry();
							} else {
								ILineConvertor lineConvertor = (ILineConvertor) DGeometryFactory.create(recordset.getGeometry());
								geoGeometryTemp = lineConvertor.convertToLine(120);
							}

							Boolean tag = false;
							if (geoGeometryTemp != null) {
								if (geometry == null && isIntersectInitialGeometry) {
									geometry = geoGeometryTemp.clone();
									geometryIntersect = geoGeometryTemp.clone();

									if (geometry.getStyle() != null) {
										resultStyle = geometry.getStyle().clone();
									}
								} else if (geometry != null && geometry.getType() == geoGeometryTemp.getType()) {
									isIntersectInitialGeometry = true;
									switch (type) {
									case 1: // union
										if (geometry.getType() == GeometryType.GEOREGION) {
											geometry = Geometrist.union(geometry, geoGeometryTemp);
										} else if (geometry.getType() == GeometryType.GEOLINE)
											geometry = UnionLine(geometry, geoGeometryTemp);
										break;
									case 2: // intersect
										geometry = Geometrist.intersect(geometry, geoGeometryTemp);
										isIntersectInitialGeometry = false;
										break;
									case 3: // xor
									{
										geometry = Geometrist.union(geometry, geoGeometryTemp);
										if (geometryIntersect != null) {
											geometryIntersect = Geometrist.intersect(geometryIntersect, geoGeometryTemp);
										}
									}
										break;
									default:
										break;
									}
								}
								if (layer.getDataset() == this.editLayer.getDataset()) {
									this.formMap.getMapControl().getEditHistory().add(EditType.DELETE, recordset, true);
									recordset.delete();
									tag = true;
								}
								geoGeometryTemp.dispose();
							}
							if (!tag)
								recordset.moveNext();
						}

					}
					if (recordset != null) {
						recordset.getBatch().update();
						recordset.dispose();
						recordset = null;
					}
				}
			}

			// 异或，考虑到多个对象问题，先并、交，再用并、交后的对象进行异或
			if (type == 3 && geometry != null && geometryIntersect != null) {
				geometry = Geometrist.xOR(geometry, geometryIntersect);
			}

			if (geometry != null) {
				recordset = ((DatasetVector) this.editLayer.getDataset()).getRecordset(true, CursorType.DYNAMIC);
				if (recordset != null) {
					geometry.setStyle(resultStyle);
					Boolean b1 = recordset.addNew(geometry, propertyData);
					Boolean b2 = recordset.update();
					this.formMap.getMapControl().getEditHistory().add(EditType.ADDNEW, recordset, true);
					// SuperMap.Desktop.UI.CommonToolkit.RefreshTabularForm(recordset.Dataset);
				}
			} else {
			}
			this.formMap.getMapControl().getEditHistory().batchEnd();
			if (recordset != null) {
				recordset.getBatch().update();
				recordset.dispose();
				recordset = null;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			if (recordset != null) {
				recordset.getBatch().update();
				recordset.dispose();
				recordset = null;
			}
		}
	}

	private GeoLine UnionLine(Geometry line1, Geometry line2) {
		GeoLine geoLine = null;
		try {
			if (line1 != null && line2 != null && line1 instanceof GeoLine && line2 instanceof GeoLine) {
				geoLine = (GeoLine) line1.clone();
				GeoLine lineTemp = (GeoLine) line2;
				if (!Geometrist.hasOverlap(line1, line2)) {
					for (int i = 0; i < lineTemp.getPartCount(); i++) {
						geoLine.addPart(lineTemp.getPart(i));
					}
				} else {
					// 有问题，不好写
					Point2Ds point2Ds = new Point2Ds();
					for (int i = 0; i < geoLine.getPartCount(); i++) {
						point2Ds.addRange(geoLine.getPart(i).toArray());
					}
					for (int i = 0; i < lineTemp.getPartCount(); i++) {
						Boolean beforeWithin = true;
						for (int j = 0; j < lineTemp.getPart(i).getCount() - 1; j++) {
							if (!Geometrist.isWithin(new GeoLine(new Point2Ds(new Point2D[] { lineTemp.getPart(i).getItem(j),
									lineTemp.getPart(i).getItem(j + 1) })), geoLine)) {
								point2Ds.add(lineTemp.getPart(i).getItem(j));
								beforeWithin = false;
							} else {
								if (!beforeWithin) {
									point2Ds.add(lineTemp.getPart(i).getItem(j));
								}
								beforeWithin = true;
							}
							if (!beforeWithin) {
								if (j == lineTemp.getPart(i).getCount() - 2) {
									point2Ds.add(lineTemp.getPart(i).getItem(j + 1));// 最后一点
								}
							}
						}
					}
					if (point2Ds.getCount() > 0) {
						geoLine.dispose();
						geoLine = new GeoLine(point2Ds);
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return geoLine;
	}

	@Override
	public boolean enable() {
		// Boolean enable = false;
		// if (Application.getActiveApplication().getActiveForm() instanceof FormMap) {
		// enable = ((FormMap) Application.getActiveApplication().getActiveForm()).getEditState().isUnionEnable();
		// }
		// return enable;
		return true;
	}
}
