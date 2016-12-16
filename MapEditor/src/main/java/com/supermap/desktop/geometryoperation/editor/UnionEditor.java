package com.supermap.desktop.geometryoperation.editor;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.EditType;
import com.supermap.data.GeoStyle;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.control.JDialogFieldOperationSetting;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilities.CursorUtilities;
import com.supermap.desktop.utilities.GeometryUtilities;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.desktop.utilities.TabularUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Selection;

import java.util.List;
import java.util.Map;

public class UnionEditor extends AbstractEditor {

	@Override
	public void activate(EditEnvironment environment) {
		try {
			// 设置目标数据集类型
			DatasetType datasetType = DatasetType.CAD;
			if (environment.getEditProperties().getSelectedGeometryTypes().size() == 1) {
				datasetType = environment.getEditProperties().getSelectedDatasetTypes().get(0);
			}
			JDialogFieldOperationSetting form = new JDialogFieldOperationSetting(MapEditorProperties.getString("String_GeometryOperation_Union"), environment
					.getMapControl().getMap(), datasetType);
			if (form.showDialog() == DialogResult.OK) {
				CursorUtilities.setWaitCursor(environment.getMapControl());
				union(environment, form.getEditLayer(), form.getPropertyData());
				TabularUtilities.refreshTabularForm((DatasetVector) form.getEditLayer().getDataset());
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			CursorUtilities.setDefaultCursor(environment.getMapControl());

			// 结束当前编辑。如果是交互性编辑，environment 会自动管理结束，就无需主动调用。
			environment.activateEditor(NullEditor.INSTANCE);
		}
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		boolean enable = false;
		if (environment.getEditProperties().getSelectedGeometryCount() > 1 // 选中数至少2个
				&& ListUtilities.isListOnlyContain(environment.getEditProperties().getSelectedGeometryTypeFeatures(), IRegionFeature.class, ILineFeature.class)
				&& environment.getEditProperties().getEditableDatasetTypes().size() > 0
				&& (ListUtilities.isListOnlyContain(environment.getEditProperties().getSelectedGeometryTypeFeatures(), IRegionFeature.class)
				|| ListUtilities.isListOnlyContain(environment.getEditProperties().getSelectedGeometryTypeFeatures(), ILineFeature.class))
				&& ListUtilities.isListContainAny(environment.getEditProperties().getEditableDatasetTypes(), DatasetType.CAD, DatasetType.REGION, DatasetType.LINE)) {
			enable = true;
		}
		return enable;
	}

	private void union(EditEnvironment environment, Layer editLayer, Map<String, Object> propertyData) {
		Geometry result = null;
		Recordset targetRecordset = null;
		environment.getMapControl().getEditHistory().batchBegin();
		GeoStyle geoStyle = null;

		try {

			// 对选中数据求交
			List<Layer> selectedLayers = environment.getEditProperties().getSelectedLayers();

			for (Layer layer : selectedLayers) {
				if (layer.getDataset().getType() == DatasetType.CAD || layer.getDataset().getType() == DatasetType.REGION
						|| layer.getDataset().getType() == DatasetType.LINE) {
					if (layer.getDataset().getType() == DatasetType.CAD && geoStyle == null) {
						geoStyle = layer.getSelection().toRecordset().getGeometry().getStyle();
					}
					result = GeometryUtilities.union(result, GeometryUtilities.union(layer), true);
				}
			}

			if (editLayer != null && result != null) {
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

				result.setStyle(geoStyle);
				// 添加结果几何对象
				targetRecordset.addNew(result, propertyData);
				targetRecordset.update();

				// 清空选择集，并选中结果对象
				selection.clear();
				int addedId = targetRecordset.getID();
				if (addedId > -1) {
					selection.add(addedId);
				}
				environment.getMapControl().getEditHistory().add(EditType.ADDNEW, targetRecordset, true);
			} else {
				if (environment.getEditProperties().getEditableSelectedGeometryTypes().get(0) == GeometryType.GEOLINE) {
					Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("string_LineUnionFailed"));
				} else {
					Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("string_UnionFailed"));
				}
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
