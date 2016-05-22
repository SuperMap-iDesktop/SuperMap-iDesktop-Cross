package com.supermap.desktop.geometryoperation.editor;

import java.util.ArrayList;

import com.supermap.analyst.spatialanalyst.Generalization;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.data.EditType;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.control.JDialogRegionExtractCenter;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilties.CursorUtilties;
import com.supermap.desktop.utilties.ListUtilties;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.mapping.Layer;

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
			JDialogRegionExtractCenter dialog = new JDialogRegionExtractCenter();

			if (dialog.showDialog() == DialogResult.OK) {
				CursorUtilties.setWaitCursor(environment.getMapControl());
				convert(environment, dialog.getDesDatasource(), dialog.getNewDatasetName(), dialog.getMax(), dialog.getMin(), dialog.isRemoveSrc());
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			environment.activateEditor(NullEditor.INSTANCE);
			CursorUtilties.setDefaultCursor(environment.getMapControl());
		}
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return environment.getEditProperties().getSelectedLayers().size() == 1
				&& ListUtilties.isListOnlyContain(environment.getEditProperties().getSelectedDatasetTypes(), DatasetType.REGION);
	}

	private void convert(EditEnvironment environment, Datasource desDatasource, String newDatasetName, double maxWidth, double minWidth, boolean isRemoveSrc) {
		environment.getMapControl().getEditHistory().batchBegin();

		try {
			ArrayList<Layer> layers = MapUtilties.getLayers(environment.getMap());

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
						DatasetVector datasetVector = Generalization.dualLineToCenterLine(srcRecordset, maxWidth, minWidth, desDatasource, newDatasetName);

						if (datasetVector != null) {

							// 图层可编辑才能移除源对象
							if (isRemoveSrc && layer.isEditable()) {
								environment.getMapControl().getEditHistory().add(EditType.DELETE, srcRecordset, false);
								srcRecordset.getBatch().setMaxRecordCount(2000);
								srcRecordset.getBatch().begin();
								srcRecordset.deleteAll();
								srcRecordset.getBatch().update();
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
			environment.getMapControl().getEditHistory().batchEnd();
			environment.getMap().refresh();
		}
	}
}
