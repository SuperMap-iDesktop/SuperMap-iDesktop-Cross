package com.supermap.desktop.geometryoperation.editor;

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
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.JDialogFieldOperationSetting;
import com.supermap.desktop.mapeditor.PluginEnvironment;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.mapping.Layer;

//@formatter:off
/**
* 1.组合之后更改 Selection，无法触发 GeometrySelected 
* 2.暂时没有实现刷新打开的属性窗口
* 3.221行，.net 没有释放，因为大数据会崩溃，测试可以留意一下是否如此
* 4.需要想办法解决编辑功能 enable() 频繁读写 recordset 的问题
* 
* @author highsad
*
*/
//@formatter:on
public class CombinationEditor extends AbstractEditor {

	@Override
	public void activate(EditEnvironment environment) {
		try {
			EditEnvironment geometryEdit = PluginEnvironment.getGeometryEditManager().instance();
			DatasetType datasetType = DatasetType.CAD;
			if (environment.getEditProperties().getSelectedDatasetTypes().size() == 1) {
				datasetType = environment.getEditProperties().getSelectedDatasetTypes().get(0);
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
	public boolean enble(EditEnvironment environment) {
		boolean result = false;
		try {
			if (environment.getEditProperties().getSelectedGeometryCount() > 1) {
				if (environment.getEditProperties().getSelectedDatasetTypes().size() > 1)// 多种数据集时，目标要为CAD
				{
					if (environment.getEditProperties().getEditableDatasetTypes().contains(DatasetType.CAD)) {
						result = true;
					}
				} else if (environment.getEditProperties().getSelectedDatasetTypes().size() == 1) // 只有一种时，目标相同或为CAD
				{
					if (!(environment.getEditProperties().getSelectedDatasetTypes().get(0) == DatasetType.POINT || environment.getEditProperties()
							.getSelectedDatasetTypes().get(0) == DatasetType.POINT3D)) {
						if (environment.getEditProperties().getEditableDatasetTypes().contains(DatasetType.CAD)
								|| environment.getEditProperties().getEditableDatasetTypes()
										.contains(environment.getEditProperties().getSelectedDatasetTypes().get(0))) {
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
