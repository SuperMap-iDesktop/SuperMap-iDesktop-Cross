package com.supermap.desktop.geometryoperation.editor;

import java.util.ArrayList;
import java.util.HashMap;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.DatasetVectorInfo;
import com.supermap.data.Datasource;
import com.supermap.data.FieldInfo;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.control.JDialogGeometryConvert;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.mapping.Layer;

public class LineToPointEditor extends AbstractEditor {

	@Override
	public void activate(EditEnvironment environment) {
		try {
			JDialogGeometryConvert dialog = new JDialogGeometryConvert(MapEditorProperties.getString("String_GeometryOperation_LineToPoint"),
					getDesDatasetType());

			if (dialog.showDialog() == DialogResult.OK) {
				DatasetVector dataset = null;

				if (dialog.isNewDataset()) {
					dataset = createNewDataset(environment, dialog.getDesDatasource(), dialog.getName());
				} else {
					dataset = dialog.getDesDataset();
				}
				LineToPoint(environment, dataset);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			environment.activateEditor(NullEditor.INSTANCE);
		}
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return false;
	}

	DatasetType getDesDatasetType() {
		return DatasetType.POINT;
	}

	private DatasetVector createNewDataset(EditEnvironment environment, Datasource datasource, String name) {
		DatasetVector newDataset = null;

		try {
			PrjCoordSys prj = null;
			FieldInfo[] srcFieldInfos = null;

			// 取选中的第一个图层的数据结构(即便选中了多个也只取第一个)
			ArrayList<Layer> layers = MapUtilties.getLayers(environment.getMap());

			for (Layer layer : layers) {
				if (layer.getSelection() != null && layer.getSelection().getCount() > 0) {
					DatasetVector dataset = (DatasetVector) layer.getDataset();
					srcFieldInfos = dataset.getFieldInfos().toArray();
					prj = dataset.getPrjCoordSys();
					break;
				}
			}

			// 创建目标数据集，初始化字段和投影
			DatasetVectorInfo datasetVectorInfo = new DatasetVectorInfo();
			datasetVectorInfo.setName(name);
			datasetVectorInfo.setType(getDesDatasetType());
			newDataset = datasource.getDatasets().create(datasetVectorInfo);

			// 设置投影，新建的数据集需要和源数据所在数据集保持一致
			newDataset.setPrjCoordSys(prj);

			// 初始化字段
			for (int i = 0; i < srcFieldInfos.length; i++) {
				FieldInfo fieldInfo = srcFieldInfos[i];

				if (!fieldInfo.isSystemField() && !fieldInfo.getName().toLowerCase().equals("smuserid")) {
					newDataset.getFieldInfos().add(fieldInfo);
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return newDataset;
	}

	private void LineToPoint(EditEnvironment environment, DatasetVector desDataset) {
		environment.getMapControl().getEditHistory().batchBegin();
		Recordset desRecordset = null;

		try {
			desRecordset = desDataset.getRecordset(false, CursorType.DYNAMIC);
			ArrayList<Layer> layers = MapUtilties.getLayers(environment.getMap());

			for (Layer layer : layers) {
				if (layer.getDataset().getType() == DatasetType.LINE && layer.getSelection() != null && layer.getSelection().getCount() > 0) {
					Recordset recordset = layer.getSelection().toRecordset();

					try {
						while (!recordset.isEOF()) {

							recordset.moveNext();
						}
					} finally {
						if (recordset != null) {
							recordset.close();
							recordset.dispose();
						}
					}
				}
			}

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			environment.getMapControl().getEditHistory().batchEnd();

			if (desDataset != null) {
				desRecordset.close();
				desRecordset.dispose();
			}
		}
	}

	/**
	 * 合并属性值，字段名相同则进行赋值
	 * @param des
	 * @param properties
	 * @return
	 */
	private HashMap<String, Object> mergePropertyData(DatasetVector des, HashMap<String, Object> properties) {
		HashMap<String, Object> results = null;
		return results;
	}
}
