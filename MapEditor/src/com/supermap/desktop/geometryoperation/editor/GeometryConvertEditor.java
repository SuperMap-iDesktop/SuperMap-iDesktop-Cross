package com.supermap.desktop.geometryoperation.editor;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.DatasetVectorInfo;
import com.supermap.data.Datasource;
import com.supermap.data.FieldInfo;
import com.supermap.data.FieldInfos;
import com.supermap.data.FieldType;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.core.recordset.RecordsetDelete;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.control.JDialogGeometryConvert;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilities.CursorUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.RecordsetUtilities;
import com.supermap.mapping.Layer;

public abstract class GeometryConvertEditor extends AbstractEditor {

	public abstract String getTitle();

	public abstract DatasetType getDesDatasetType();

	public abstract DatasetType getSrcDatasetType();

	public abstract boolean convert(Recordset desRecordset, IGeometry srcGeometry, Map<String, Object> properties);

	@Override
	public void activate(EditEnvironment environment) {
		try {
			JDialogGeometryConvert dialog = new JDialogGeometryConvert(getTitle(), getDesDatasetType());

			if (dialog.showDialog() == DialogResult.OK) {
				CursorUtilities.setWaitCursor(environment.getMapControl());
				DatasetVector dataset = null;

				if (dialog.isNewDataset()) {
					dataset = createNewDataset(environment, dialog.getDesDatasource(), dialog.getNewDatasetName());
				} else {
					dataset = dialog.getDesDataset();
				}
				boolean isConverted = convert(environment, dataset, dialog.isRemoveSrc());

				if (isConverted) {
					Application
							.getActiveApplication()
							.getOutput()
							.output(MessageFormat.format(MapEditorProperties.getString("String_GeometryOperation_ConvertSuccess"), getTitle(),
									dataset.getName()));
				} else {
					Application.getActiveApplication().getOutput()
							.output(MessageFormat.format(MapEditorProperties.getString("String_GeometryOperation_ConvertFailed"), getTitle()));
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			environment.activateEditor(NullEditor.INSTANCE);
			CursorUtilities.setDefaultCursor(environment.getMapControl());
		}
	}

	private DatasetVector createNewDataset(EditEnvironment environment, Datasource datasource, String name) {
		DatasetVector newDataset = null;

		try {
			PrjCoordSys prj = null;
			FieldInfos srcFieldInfos = null;

			// 取选中的第一个图层的数据结构(即便选中了多个也只取第一个)
			ArrayList<Layer> layers = MapUtilities.getLayers(environment.getMap());

			for (Layer layer : layers) {
				if (layer.getSelection() != null && layer.getSelection().getCount() > 0) {
					DatasetVector dataset = (DatasetVector) layer.getDataset();
					srcFieldInfos = dataset.getFieldInfos();
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
			for (int i = 0; i < srcFieldInfos.getCount(); i++) {
				FieldInfo fieldInfo = srcFieldInfos.get(i);

				if (!fieldInfo.isSystemField() && !fieldInfo.getName().toLowerCase().equals("smuserid")) {
					newDataset.getFieldInfos().add(fieldInfo);
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return newDataset;
	}

	private boolean convert(EditEnvironment environment, DatasetVector desDataset, boolean isRemoveSrc) {
		environment.getMapControl().getEditHistory().batchBegin();
		Recordset desRecordset = null;
		boolean isConverted = false;

		try {
			desRecordset = desDataset.getRecordset(false, CursorType.DYNAMIC);
			desRecordset.getBatch().setMaxRecordCount(2000);
			desRecordset.getBatch().begin();
			ArrayList<Layer> layers = MapUtilities.getLayers(environment.getMap());

			for (Layer layer : layers) {
				// @formatter:off
				if ((layer.getDataset().getType() == getSrcDatasetType() || layer.getDataset().getType() == DatasetType.CAD) 
						&& layer.getSelection() != null
						&& layer.getSelection().getCount() > 0) {
				// @formatter:on
					Recordset recordset = layer.getSelection().toRecordset();
					RecordsetDelete delete = null;
					if (isRemoveSrc && layer.isEditable()) {
						delete = new RecordsetDelete((DatasetVector) layer.getDataset(), environment.getMapControl().getEditHistory());
					}

					try {
						while (!recordset.isEOF()) {
							IGeometry geometry = null;

							try {
								geometry = DGeometryFactory.create(recordset.getGeometry());

								// 获取当前对象自身的属性
								Map<String, Object> currentValues = RecordsetUtilities.getFieldValues(recordset);

								// 拿目标数据集的属性结构来和当前对象属性进行合并处理，相同的字段赋值，目标数据集没有的字段不要
								Map<String, Object> desValues = mergePropertyData(desDataset, recordset.getFieldInfos(), currentValues);

								// 执行转换
								isConverted = convert(desRecordset, geometry, desValues);

								// 如果转换失败，就不移除源对象
								if (isConverted && delete != null) {
									delete.delete(recordset.getID());
								}
							} finally {
								if (geometry != null) {
									geometry.dispose();
								}
							}
							recordset.moveNext();
						}

						// 移除源对象
						if (delete != null) {
							delete.update();
							layer.getSelection().clear();
							environment.getMap().refresh();
						}
					} finally {
						if (recordset != null) {
							recordset.close();
							recordset.dispose();
						}
					}
				}
			}

			desRecordset.getBatch().update();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
			isConverted = false;
		} finally {
			environment.getMapControl().getEditHistory().batchEnd();

			if (desRecordset != null) {
				desRecordset.close();
				desRecordset.dispose();
			}
		}
		return isConverted;
	}

	// @formatter:off
	/**
	 * 合并属性值，字段名相同则进行赋值，具体处理逻辑如下。
	 * 1. 如果目标字段是文本型字段，则源数据类型均作 toString 处理
	 * 2. 如果目标字段不是文本型字段，则要求字段名和字段类型均匹配才能进行合并处理
	 * 
	 * @param des
	 * @param properties
	 * @return
	 */
	// @formatter:on
	HashMap<String, Object> mergePropertyData(DatasetVector des, FieldInfos srcFieldInfos, Map<String, Object> properties) {
		HashMap<String, Object> results = new HashMap<>();
		FieldInfos desFieldInfos = des.getFieldInfos();

		for (int i = 0; i < desFieldInfos.getCount(); i++) {
			FieldInfo desFieldInfo = desFieldInfos.get(i);

			if (!desFieldInfo.isSystemField() && properties.containsKey(desFieldInfo.getName())) {
				FieldInfo srcFieldInfo = srcFieldInfos.get(desFieldInfo.getName());

				if (desFieldInfo.getType() == srcFieldInfo.getType()) {
					// 如果要源字段和目标字段类型一致，直接做保存
					results.put(desFieldInfo.getName(), properties.get(desFieldInfo.getName()));
				} else if (desFieldInfo.getType() == FieldType.WTEXT || desFieldInfo.getType() == FieldType.TEXT) {

					// 如果目标字段与源字段类型不一致，则只有目标字段是文本型字段时，将源字段值做 toString 处理
					results.put(desFieldInfo.getName(), properties.get(desFieldInfo.getName()).toString());
				}
			}
		}
		return results;
	}
}
