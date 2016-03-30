package com.supermap.desktop.geometryoperation.CtrlAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.supermap.desktop.FormMap;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.core.recordset.RecordsetDelete;
import com.supermap.desktop.geometryoperation.JDialogFieldOperationSetting;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.mapping.Layer;

// @formatter:off
/**
 * 1.组合之后更改 Selection，无法触发 GeometrySelected
 * 2.暂时没有实现刷新打开的属性窗口
 * 3.需要想办法解决编辑功能 enable() 频繁读写 recordset 的问题
 * 
 * @author highsad
 *
 */
// @formatter:on
public class CtrlActionCombination extends CtrlAction {

	public CtrlActionCombination(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			FormMap formMap = (FormMap) Application.getActiveApplication().getMainFrame().getFormManager().getActiveForm();
			DatasetType datasetType = DatasetType.CAD;
			// if (formMap.EditState.SelectedDatasetTypes.Count == 1)
			// {
			// datasetType = formMap.EditState.SelectedDatasetTypes[0];
			// }
			Layer resultLayer = null;
			List<Layer> layers = MapUtilties.getLayers(formMap.getMapControl().getMap());
			for (Layer layer : layers) {
				if (layer.isEditable() && layer.getDataset().getType() == datasetType) {
					resultLayer = layer;
				}
			}

			HashMap<String, Object> values = new HashMap<>();
			JDialogFieldOperationSetting formCombination = new JDialogFieldOperationSetting("组合", formMap.getMapControl().getMap());
			// JDialogFieldOperationSetting formCombination = new JDialogFieldOperationSetting(formMap.getMapControl().getMap(), datasetType);
			// if (formCombination.ShowDialog() == DialogResult.OK)
			// {
			resultLayer = formCombination.getEditLayer();
			// values = formCombination.PropertyData;
			combinationObjects(formMap, resultLayer, values);
			// }
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

	}

	private void combinationObjects(FormMap formMap, Layer resultLayer, HashMap<String, Object> propertyData) {
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
					if (layer.getName() == resultLayer.getName()) {
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

	@Override
	public boolean enable() {
		boolean enable = false;
		// if ((Application.ActiveForm as FormMap) != null)
		// {
		// //(Application.ActiveForm as FormMap).EditState.CheckEnable();
		// enable = (Application.ActiveForm as FormMap).EditState.IsCombinationEnable;
		// }
		return enable;
	}
}
