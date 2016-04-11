package com.supermap.desktop.geometryoperation.editor;

import java.util.List;
import java.util.Map;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.EditType;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.JDialogFieldOperationSetting;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilties.GeometryUtilties;
import com.supermap.desktop.utilties.ListUtilties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Selection;

// @formatter:off
/**
 * 对象编辑—求交。多个对象，两两依次求交。
 * 仅支持面特征的集合对象求交。
 * 不支持二维和三维几何对象混合求交。
 * 选中的对象如果有不支持的对象，直接忽略。
 * 结果对象风格以结果图层为主。
 * 考虑到求交的对象不会有很多，因此实现中历史记录不使用提升性能的批量编辑模式。
 * （历史记录提示性能的批量编辑操作需要以 recordset 整体为单位进行操作，而本功能使用的历史记录批量操作的目的是将多个单独的操作合并为一条历史记录）
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
			JDialogFieldOperationSetting form = new JDialogFieldOperationSetting(MapEditorProperties.getString("String_GeometryOperation_Intersect"),
					environment.getMapControl().getMap(), datasetType);
			if (form.showDialog() == DialogResult.OK) {
				intersect(environment, form.getEditLayer(), form.getPropertyData());
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	// @formatter:off
	/* 
	 * 1.仅支持面面求交，线线求交意义不大，不支持。
	 * 2.不支持二维对象、三维对象混合。
	 */
	// @formatter:on
	@Override
	public boolean enble(EditEnvironment environment) {
		boolean enable = false;
		if (environment.getEditProperties().getSelectedGeometryCount() > 1 && // 选中数至少2个
				// (this.has2DGeometrySelected != this.has3DGeometrySelected) && // 不能即有二维对象又有三维对象
				ListUtilties.isListOnlyContain(environment.getEditProperties().getSelectedGeometryFeatures(), IRegionFeature.class)) {
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

	private void intersect(EditEnvironment environment, Layer editLayer, Map<String, Object> propertyData) {
		Geometry result = null;
		Recordset targetRecordset = null;
		environment.getMapControl().getEditHistory().batchBegin();

		try {

			// 对选中数据求交
			List<Layer> selectedLayers = environment.getEditProperties().getSelectedLayers();

			for (Layer layer : selectedLayers) {
				if (layer.getDataset().getType() == DatasetType.CAD || layer.getDataset().getType() == DatasetType.REGION) {
					result = GeometryUtilties.intersetct(result, GeometryUtilties.intersect(layer), true);
				}
			}

			if (editLayer != null) {
				Selection selection = editLayer.getSelection();
				targetRecordset = ((DatasetVector) editLayer.getDataset()).getRecordset(false, CursorType.DYNAMIC);

				// 删除目标图层上的选中几何对象
				targetRecordset.getBatch().begin();
				for (int i = 0; i < selection.getCount(); i++) {
					int id = selection.get(i);
					targetRecordset.seekID(id);
					environment.getMapControl().getEditHistory().add(EditType.DELETE, targetRecordset, true);
					targetRecordset.delete();
				}
				targetRecordset.getBatch().update();

				// 添加结果几何对象
				targetRecordset.addNew(result, propertyData);
				targetRecordset.update();
				environment.getMapControl().getEditHistory().add(EditType.ADDNEW, targetRecordset, true);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			environment.getMapControl().getEditHistory().batchEnd();

			if (result != null) {
				result.dispose();
			}

			if (targetRecordset != null) {
				targetRecordset.close();
				targetRecordset.dispose();
			}
		}
	}
}
