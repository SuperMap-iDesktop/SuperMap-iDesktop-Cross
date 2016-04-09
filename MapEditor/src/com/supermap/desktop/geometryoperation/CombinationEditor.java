package com.supermap.desktop.geometryoperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.EditType;
import com.supermap.data.GeoCompound;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoLine3D;
import com.supermap.data.GeoLineM;
import com.supermap.data.GeoRegion;
import com.supermap.data.GeoRegion3D;
import com.supermap.data.GeoText;
import com.supermap.data.Geometry;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.core.recordset.RecordsetDelete;
import com.supermap.desktop.mapeditor.MapEditorEnv;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.mapping.Layer;

public class CombinationEditor extends AbstractEditor {

	public CombinationEditor(GeometryEditEnv geometryEditEnv) {
		super(geometryEditEnv);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void activate() {
		try {
			GeometryEditEnv geometryEdit = MapEditorEnv.getGeometryEditManager().instance();
			DatasetType datasetType = DatasetType.CAD;
			if (getGeometryEditEnv().getEditProperties().getSelectedDatasetTypes().size() == 1) {
				datasetType = getGeometryEditEnv().getEditProperties().getSelectedDatasetTypes().get(0);
			}
			Layer resultLayer = null;

			JDialogFieldOperationSetting formCombination = new JDialogFieldOperationSetting("组合", geometryEdit.getMap(), datasetType);
			if (formCombination.showDialog() == DialogResult.OK) {
				resultLayer = formCombination.getEditLayer();
				Map<String, Object> values = formCombination.getPropertyData();
				combinationObjects(geometryEdit.getFormMap(), resultLayer, values);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean enble() {
		boolean result = false;
		try {
			if (getGeometryEditEnv().getEditProperties().getSelectedGeometryCount() > 1) {
				if (getGeometryEditEnv().getEditProperties().getSelectedDatasetTypes().size() > 1)// 多种数据集时，目标要为CAD
				{
					if (getGeometryEditEnv().getEditProperties().getEditableDatasetTypes().contains(DatasetType.CAD)) {
						result = true;
					}
				} else if (getGeometryEditEnv().getEditProperties().getSelectedDatasetTypes().size() == 1) // 只有一种时，目标相同或为CAD
				{
					if (!(getGeometryEditEnv().getEditProperties().getSelectedDatasetTypes().get(0) == DatasetType.POINT || getGeometryEditEnv()
							.getEditProperties().getSelectedDatasetTypes().get(0) == DatasetType.POINT3D)) {
						if (getGeometryEditEnv().getEditProperties().getEditableDatasetTypes().contains(DatasetType.CAD)
								|| getGeometryEditEnv().getEditProperties().getEditableDatasetTypes()
										.contains(getGeometryEditEnv().getEditProperties().getSelectedDatasetTypes().get(0))) {
							result = true;
						}
					}
				} else {
					// 不做任何处理
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	@Override
	public boolean check() {
		return false;
	}

	private void combinationObjects(IFormMap formMap, Layer resultLayer, Map<String, Object> propertyData) {
		Recordset recordset = null;
		Geometry geometry = null;
		try {
			formMap.getMapControl().getEditHistory().batchBegin();
			List<Geometry> objectGeometrys = new ArrayList<Geometry>();
			List<Layer> layers = MapUtilties.getLayers(formMap.getMapControl().getMap());
			for (Layer layer : layers) {
				if (layer.getSelection() != null) {
					recordset = layer.getSelection().toRecordset();
					while (!recordset.isEOF()) {
						geometry = recordset.getGeometry();
						objectGeometrys.add(geometry);
						recordset.moveNext();
					}
					if (layer.getName().equals(resultLayer.getName())) {
						RecordsetDelete delete = new RecordsetDelete(recordset, formMap.getMapControl().getEditHistory());
						delete.begin();
						for (int dd = 0; dd < layer.getSelection().getCount(); dd++) {
							delete.delete(layer.getSelection().get(dd));
						}
						delete.update();
					}
					recordset.dispose();
				}
			}
			DatasetVector resultDataset = (DatasetVector) resultLayer.getDataset();
			recordset = resultDataset.getRecordset(false, CursorType.DYNAMIC);
			if (resultDataset.getType() == DatasetType.CAD) {
				GeoCompound geoObject = new GeoCompound();
				for (Geometry g : objectGeometrys) {
					geoObject.addPart(g);
				}
				recordset.addNew(geoObject, propertyData);
				recordset.update();
				geoObject.dispose();
			} else if (resultDataset.getType() == DatasetType.LINE) {
				GeoLine geoObject = new GeoLine();
				for (Geometry g : objectGeometrys) {
					for (int i = 0; i < ((GeoLine) g).getPartCount(); i++) {
						geoObject.addPart(((GeoLine) g).getPart(i));
					}
				}
				recordset.addNew(geoObject, propertyData);
				recordset.update();
				geoObject.dispose();
			} else if (resultDataset.getType() == DatasetType.LINE3D) {
				GeoLine3D geoObject = new GeoLine3D();
				for (Geometry g : objectGeometrys) {
					for (int i = 0; i < ((GeoLine3D) g).getPartCount(); i++) {
						geoObject.addPart(((GeoLine3D) g).getPart(i));
					}
				}
				recordset.addNew(geoObject, propertyData);
				recordset.update();
				geoObject.dispose();
			} else if (resultDataset.getType() == DatasetType.REGION) {
				GeoRegion geoObject = new GeoRegion();
				for (Geometry g : objectGeometrys) {
					for (int i = 0; i < ((GeoRegion) g).getPartCount(); i++) {
						geoObject.addPart(((GeoRegion) g).getPart(i));
					}
				}
				recordset.addNew(geoObject, propertyData);
				recordset.update();
				geoObject.dispose();
			} else if (resultDataset.getType() == DatasetType.REGION3D) {
				GeoRegion3D geoObject = new GeoRegion3D();
				for (Geometry g : objectGeometrys) {
					for (int i = 0; i < ((GeoRegion3D) g).getPartCount(); i++) {
						geoObject.addPart(((GeoRegion3D) g).getPart(i));
					}
				}
				recordset.addNew(geoObject, propertyData);
				recordset.update();
				geoObject.dispose();
			} else if (resultDataset.getType() == DatasetType.LINEM) {
				GeoLineM geoObject = new GeoLineM();
				for (Geometry g : objectGeometrys) {
					for (int i = 0; i < ((GeoLineM) g).getPartCount(); i++) {
						geoObject.addPart(((GeoLineM) g).getPart(i));
					}
				}
				recordset.addNew(geoObject, propertyData);
				recordset.update();
				geoObject.dispose();
			} else if (resultDataset.getType() == DatasetType.TEXT) {
				GeoText geoObject = new GeoText();
				int index = 0;
				for (Geometry g : objectGeometrys) {
					if (index++ == 0) {
						geoObject.setTextStyle(((GeoText) g).getTextStyle());
					}
					for (int i = 0; i < ((GeoText) g).getPartCount(); i++) {
						geoObject.addPart(((GeoText) g).getPart(i));
					}
				}
				recordset.addNew(geoObject, propertyData);
				recordset.update();
				geoObject.dispose();
			}

			int resultID = recordset.getID();
			resultLayer.getSelection().clear();
			resultLayer.getSelection().add(resultID);
			// SuperMap.Desktop.UI.CommonToolkit.RefreshTabularForm(resultLayer.Dataset as DatasetVector);
			// _Toolkit.InvokeGeometrySelectedEvent(formMap.MapControl, new GeometrySelectedEventArgs(resultLayer.Selection.Count));
			formMap.getMapControl().getEditHistory().add(EditType.ADDNEW, recordset, true);
			formMap.getMapControl().getEditHistory().batchEnd();
			formMap.getMapControl().getMap().refresh();
			Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_Completed"));
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			if (recordset != null) {
				recordset.close();
				recordset.dispose();
			}

			if (geometry != null) {
				geometry.dispose();
			}
		}
	}
}
