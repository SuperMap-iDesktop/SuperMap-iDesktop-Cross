package com.supermap.desktop.geometryoperation.editor;

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
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILineConvertor;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IRegionConvertor;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.JDialogFieldOperationSetting;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilties.ListUtilties;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.mapping.Layer;
import com.supermap.ui.MapControl;

// @formatter:off
/**
 * 对象编辑—求交。多个对象，两两依次求交。
 * 仅支持面特征的集合对象求交。
 * 不支持二维和三维几何对象混合求交。
 * 选中的对象如果有不支持的对象，直接忽略。
 * 结果对象风格以结果图层为主。
 * 
 * @author highsad
 *
 */
// @formatter:on
public class IntersectEditor extends AbstractEditor {

	@Override
	public void activate(EditEnvironment environment) {
		try {
			// 设置目标数据集类型
			DatasetType datasetType = DatasetType.CAD;
			if (environment.getEditProperties().getSelectedGeometryTypes().size() == 1) {
				if (environment.getEditProperties().getSelectedGeometryTypes().get(0) == GeometryType.GEOCIRCLE3D
						|| environment.getEditProperties().getSelectedGeometryTypes().get(0) == GeometryType.GEOPIE3D
						|| environment.getEditProperties().getSelectedGeometryTypes().get(0) == GeometryType.GEOREGION3D) {
					datasetType = DatasetType.REGION3D;
				} else {
					datasetType = DatasetType.REGION;
				}
			}
			JDialogFieldOperationSetting form = new JDialogFieldOperationSetting("求交", environment.getMapControl().getMap(), datasetType);
			if (form.showDialog() == DialogResult.OK) {
				intersect(environment, form.getEditLayer(), form.getPropertyData());
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		// 按需重写
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		boolean enable = false;
		if (environment.getEditProperties().getSelectedGeometryCount() > 1 && // 选中数至少2个
				// (this.has2DGeometrySelected != this.has3DGeometrySelected) && // 不能即有二维对象又有三维对象
				ListUtilties.isListOnlyContain(environment.getEditProperties().getSelectedGeometryFeatures(), IRegionFeature.class)) // 只支持面
		{
			// 是会否存在可编辑的“可操作保存”图层
			DatasetType datasetType = DatasetType.CAD;
			if (environment.getEditProperties().getSelectedGeometryTypes().get(0) == GeometryType.GEOCIRCLE3D
					|| environment.getEditProperties().getSelectedGeometryTypes().get(0) == GeometryType.GEOPIE3D
					|| environment.getEditProperties().getSelectedGeometryTypes().get(0) == GeometryType.GEOREGION3D) {
				datasetType = DatasetType.REGION3D;
			} else {
				datasetType = DatasetType.REGION;
			}

			if (environment.getEditProperties().getEditableDatasetTypes().size() > 0
					&& ListUtilties.isListContainAny(environment.getEditProperties().getEditableDatasetTypes(), DatasetType.CAD, datasetType)) {
				enable = true;
			}
		}
		return enable;
	}

	/**
	 * 将指定图层的选中对象
	 * 
	 * @param layer
	 * @return
	 */
	private Geometry intersect(Layer layer) {
		Geometry result = null;

		try {

		} catch (Exception e) {
			// TODO: handle exception
		} finally {

		}
		return result;
	}

	public void intersect(EditEnvironment environment, Layer editLayer, Map<String, Object> propertyData) {
//		Geometry result = null;
		// Recordset resultRecordset = null;
		// environment.getMapControl().getEditHistory().batchBegin();
		//
		// try {
		// GeoStyle resultStyle = null;
		// List<Layer> layers = MapUtilties.getLayers(environment.getMapControl().getMap());
		//
		// for (Layer layer : layers) {
		// if (layer.getSelection() != null && layer.getSelection().getCount() > 0) {
		// Recordset recordset = layer.getSelection().toRecordset();
		//
		// try {
		// if (recordset != null) {
		// recordset.getBatch().begin();
		// recordset.moveFirst();
		//
		// while (!recordset.isEOF()) {
		// Geometry geometryTemp = recordset.getGeometry();
		// recordset.moveNext();
		// }
		// }
		// } finally {
		// if (recordset != null) {
		// recordset.close();
		// recordset.dispose();
		// }
		// }
		// layer.getSelection().clear();
		// boolean isIntersectInitialGeometry = true;
		// if (recordset != null) {
		// recordset.getBatch().begin();
		// while (!recordset.isEOF()) {
		// Geometry geoGeometryTemp = null;
		// if (layer.getDataset().getType() == DatasetType.CAD) {
		// IGeometry dGeometry = DGeometryFactory.create(recordset.getGeometry());
		//
		// if (dGeometry instanceof IRegionFeature && dGeometry instanceof IRegionConvertor) {
		// geoGeometryTemp = ((IRegionConvertor) dGeometry).convertToRegion(120);
		// } else if (dGeometry instanceof ILineFeature && dGeometry instanceof ILineConvertor) {
		// geoGeometryTemp = ((ILineConvertor) dGeometry).convertToLine(120);
		// } else {
		// geoGeometryTemp = recordset.getGeometry();
		// }
		// } else if (layer.getDataset().getType() == DatasetType.REGION) {
		// geoGeometryTemp = recordset.getGeometry();
		// } else {
		// ILineConvertor lineConvertor = (ILineConvertor) DGeometryFactory.create(recordset.getGeometry());
		// geoGeometryTemp = lineConvertor.convertToLine(120);
		// }
		//
		// boolean tag = false;
		// if (geoGeometryTemp != null) {
		// if (geometry == null && isIntersectInitialGeometry) {
		// geometry = geoGeometryTemp.clone();
		//
		// if (geometry.getStyle() != null) {
		// resultStyle = geometry.getStyle().clone();
		// }
		// } else if (geometry != null && geometry.getType() == geoGeometryTemp.getType()) {
		// isIntersectInitialGeometry = true;
		// geometry = Geometrist.intersect(geometry, geoGeometryTemp);
		// isIntersectInitialGeometry = false;
		// }
		// if (layer.getDataset() == editLayer.getDataset()) {
		// environment.getMapControl().getEditHistory().add(EditType.DELETE, recordset, true);
		// recordset.delete();
		// tag = true;
		// }
		// geoGeometryTemp.dispose();
		// }
		// if (!tag)
		// recordset.moveNext();
		// }
		//
		// }
		// if (recordset != null) {
		// recordset.getBatch().update();
		// recordset.dispose();
		// recordset = null;
		// }
		// }
		// }
		//
		// if (geometry != null) {
		// recordset = ((DatasetVector) editLayer.getDataset()).getRecordset(true, CursorType.DYNAMIC);
		// if (recordset != null) {
		// geometry.setStyle(resultStyle);
		// recordset.addNew(geometry, propertyData);
		// recordset.update();
		// environment.getMapControl().getEditHistory().add(EditType.ADDNEW, recordset, true);
		// // SuperMap.Desktop.UI.CommonToolkit.RefreshTabularForm(recordset.Dataset);
		// }
		// }
		// } catch (Exception ex) {
		// Application.getActiveApplication().getOutput().output(ex);
		// } finally {
		// environment.getMapControl().getEditHistory().batchEnd();
		//
		// if (result != null) {
		// result.dispose();
		// }
		//
		// if (resultRecordset != null) {
		// resultRecordset.close();
		// resultRecordset.dispose();
		// }
		// }
	}
}
