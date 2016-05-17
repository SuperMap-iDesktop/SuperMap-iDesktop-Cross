package com.supermap.desktop.geometryoperation.editor;

import java.util.ArrayList;
import java.util.Map;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.EditType;
import com.supermap.data.GeoRegion;
import com.supermap.data.GeoStyle;
import com.supermap.data.Geometrist;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.JDialogFieldOperationSetting;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.utilties.CursorUtilties;
import com.supermap.desktop.utilties.ListUtilties;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.desktop.utilties.TabularUtilties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Selection;

public class HoleyRegionEditor extends AbstractEditor {

	@Override
	public void activate(EditEnvironment environment) {
		try {
			JDialogFieldOperationSetting formCombination = new JDialogFieldOperationSetting(
					MapEditorProperties.getString("String_GeometryOperation_Combination"), environment.getMap(), DatasetType.REGION);
			if (formCombination.showDialog() == DialogResult.OK) {
				CursorUtilties.setWaitCursor(environment.getMapControl());
				holeyRegion(environment, formCombination.getEditLayer(), formCombination.getPropertyData());
				TabularUtilties.refreshTabularForm((DatasetVector) formCombination.getEditLayer().getDataset());
			}
		} finally {
			CursorUtilties.setDefaultCursor(environment.getMapControl());
		}
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		boolean result = false;

		if (ListUtilties.isListContainAny(environment.getEditProperties().getEditableDatasetTypes(), DatasetType.CAD, DatasetType.REGION)
				&& environment.getEditProperties().getSelectedGeometryCount() > 1
				&& ListUtilties.isListOnlyContain(environment.getEditProperties().getSelectedGeometryTypeFeatures(), IRegionFeature.class)) {
			result = true;
		}
		return result;
	}

	private void holeyRegion(EditEnvironment environment, Layer editLayer, Map<String, Object> propertyData) {
		Recordset resultRecordset = null;
		GeoRegion resultRegion = null;
		GeoStyle resultStyle = null;
		environment.getMapControl().getEditHistory().batchBegin();

		try {
			ArrayList<Layer> layers = MapUtilties.getLayers(environment.getMap());

			for (Layer layer : layers) {
				if (layer.getDataset().getType() == DatasetType.REGION || layer.getDataset().getType() == DatasetType.CAD) {
					Selection selection = new Selection(layer.getSelection());
					layer.getSelection().clear();

					if (selection.getCount() > 0) {
						Recordset recordset = ((DatasetVector) layer.getDataset()).getRecordset(false, CursorType.DYNAMIC);

						try {
							for (int i = 0; i < selection.getCount(); i++) {
								int id = selection.get(i);
								if (recordset.seekID(id)) {
									IGeometry geometry = DGeometryFactory.create(recordset.getGeometry());
									GeoRegion region = null;

									try {
										if (geometry instanceof IRegionFeature) {
											region = ((IRegionFeature) geometry).convertToRegion(120);

											if (resultRegion == null) {
												resultRegion = region.clone();

												if (geometry.getGeometry().getStyle() != null) {
													resultStyle = geometry.getGeometry().getStyle().clone();
												}
											} else {
												resultRegion = (GeoRegion) Geometrist.xOR(resultRegion, region);
											}

											if (layer.isEditable()) {
												environment.getMapControl().getEditHistory().add(EditType.DELETE, recordset, true);
												recordset.delete();
											}
										}
									} finally {
										if (geometry != null) {
											geometry.dispose();
										}

										if (region != null) {
											region.dispose();
										}
									}
								}
							}
						} finally {
							if (recordset != null) {
								recordset.close();
								recordset.dispose();
							}

							if (selection != null) {
								selection.dispose();
							}
						}
					}
				}
			}

			if (resultRegion != null) {
				resultRegion.setStyle(resultStyle);
				editLayer.getSelection().clear();
				resultRecordset = ((DatasetVector) editLayer.getDataset()).getRecordset(false, CursorType.DYNAMIC);
				resultRecordset.addNew(resultRegion, propertyData);
				resultRecordset.update();
				editLayer.getSelection().add(resultRecordset.getID());
				TabularUtilties.refreshTabularForm(resultRecordset.getDataset());
				environment.getMapControl().getEditHistory().add(EditType.ADDNEW, resultRecordset, true);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			if (resultRecordset != null) {
				resultRecordset.close();
				resultRecordset.dispose();
			}

			if (resultRegion != null) {
				resultRegion.dispose();
			}
			environment.getMapControl().getEditHistory().batchEnd();
		}
	}
}
