package com.supermap.desktop.geometryoperation.editor;

import java.awt.Color;
import java.text.MessageFormat;
import java.util.ArrayList;

import com.supermap.analyst.spatialanalyst.Generalization;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.data.EditType;
import com.supermap.data.GeoStyle;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.control.JDialogRegionExtractCenter;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilities.CursorUtilities;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerSettingVector;

/**
 * 面提取中心线仅选中单图层对象时可用，并且不支持 CAD
 * 
 * @author highsad
 *
 */
public class RegionExtractCenterEditor extends AbstractEditor {

	@Override
	public void activate(EditEnvironment environment) {
		try {
			JDialogRegionExtractCenter dialog = new JDialogRegionExtractCenter(canRemoveSrc(environment));

			if (dialog.showDialog() == DialogResult.OK) {
				CursorUtilities.setWaitCursor(environment.getMapControl());
				DatasetVector datasetVector = convert(environment, dialog.getDesDatasource(), dialog.getNewDatasetName(), dialog.getMax(), dialog.getMin(),
						dialog.isRemoveSrc());

				if (datasetVector != null) {
					addToCurrentMap(environment, datasetVector);
					Application
							.getActiveApplication()
							.getOutput()
							.output(MessageFormat.format(MapEditorProperties.getString("String_GeometryOperation_ConvertSuccess"),
									MapEditorProperties.getString("String_GeometryOperation_RegionExtractCenter"), dialog.getNewDatasetName()));
				} else {
					Application
							.getActiveApplication()
							.getOutput()
							.output(MessageFormat.format(MapEditorProperties.getString("String_GeometryOperation_ConvertFailed"),
									MapEditorProperties.getString("String_GeometryOperation_RegionExtractCenter")));
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			environment.activateEditor(NullEditor.INSTANCE);
			CursorUtilities.setDefaultCursor(environment.getMapControl());
		}
	}

	protected boolean canRemoveSrc(EditEnvironment environment) {
		boolean canRemoveSrc = false;

		Layer[] editableLayers = environment.getMapControl().getEditableLayers();
		for (int i = 0; i < editableLayers.length; i++) {
			canRemoveSrc = editableLayers[i].getSelection().getCount() > 0;
			if (canRemoveSrc) {
				break;
			}
		}
		return canRemoveSrc;
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return environment.getEditProperties().getSelectedLayers().size() == 1
				&& ListUtilities.isListOnlyContain(environment.getEditProperties().getSelectedDatasetTypes(), DatasetType.REGION);
	}

	private DatasetVector convert(EditEnvironment environment, Datasource desDatasource, String newDatasetName, double maxWidth, double minWidth,
			boolean isRemoveSrc) {
		DatasetVector datasetVector = null;

		try {
			ArrayList<Layer> layers = MapUtilities.getLayers(environment.getMap());

			for (Layer layer : layers) {
				// @formatter:off
				if (layer.getDataset().getType() == DatasetType.REGION 
						&& layer.getSelection() != null
						&& layer.getSelection().getCount() > 0) {
				// @formatter:on

					// 获取选中面图层的选中数据
					Recordset srcRecordset = layer.getSelection().toRecordset();

					try {
						// 提取中心线
						datasetVector = Generalization.dualLineToCenterLine(srcRecordset, maxWidth, minWidth, desDatasource, newDatasetName);

						if (datasetVector != null) {
							// 图层可编辑才能移除源对象
							if (isRemoveSrc && layer.isEditable()) {
								environment.getMapControl().getEditHistory().batchBegin();
								environment.getMapControl().getEditHistory().add(EditType.DELETE, srcRecordset, false);
								srcRecordset.getBatch().setMaxRecordCount(2000);
								srcRecordset.getBatch().begin();
								srcRecordset.deleteAll();
								srcRecordset.getBatch().update();
								environment.getMapControl().getEditHistory().batchEnd();
								layer.getSelection().clear();
							}

							// 成功执行一次就行
							break;
						}
					} finally {
						if (srcRecordset != null) {
							srcRecordset.close();
							srcRecordset.dispose();
						}
					}
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			environment.getMap().refresh();
			environment.getMapControl().revalidate();
		}
		return datasetVector;
	}

	private void addToCurrentMap(EditEnvironment environment, Dataset dataset) {

		// 设置线图层风格
		GeoStyle geoStyle = new GeoStyle();
		geoStyle.setLineColor(new Color(66, 183, 213));
		geoStyle.setLineWidth(1);
		LayerSettingVector layerSettingVector = new LayerSettingVector();
		layerSettingVector.setStyle(geoStyle);

		// 添加到当前地图
		Layer layer = environment.getMap().getLayers().add(dataset, layerSettingVector, true);
		environment.getMap().refresh();
	}
}
