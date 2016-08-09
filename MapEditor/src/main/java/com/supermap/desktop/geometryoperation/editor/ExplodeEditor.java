package com.supermap.desktop.geometryoperation.editor;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.core.recordset.RecordsetAddNew;
import com.supermap.desktop.core.recordset.RecordsetSet;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.utilities.CursorUtilities;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.desktop.utilities.TabularUtilities;
import com.supermap.mapping.Layer;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ExplodeEditor extends AbstractEditor {

	@Override
	public void activate(EditEnvironment environment) {
		CursorUtilities.setWaitCursor(environment.getMapControl());

		try {
			environment.getMapControl().getEditHistory().batchBegin();

			for (int i = 0; i < environment.getEditableLayers().length; i++) {
				Layer layer = environment.getEditableLayers()[i];

				if (layer.getSelection() != null && layer.getSelection().getCount() > 0) {
					Recordset recordset = ((DatasetVector) layer.getDataset()).getRecordset(false, CursorType.DYNAMIC);
					EngineType engineType = layer.getDataset().getDatasource().getEngineType();
					HashMap<Integer, ArrayList<GeoLine>> resultGeoLines = new HashMap<Integer, ArrayList<GeoLine>>();

					try {
						if (recordset != null) {
							for (int index = 0; index < layer.getSelection().getCount(); index++) {
								int id = layer.getSelection().get(index);
								recordset.seekID(id);

								IGeometry geometry = DGeometryFactory.create(recordset.getGeometry());

								try {
									if (!(geometry instanceof ILineFeature)) {
										continue;
									}

									GeoLine line = ((ILineFeature) geometry).convertToLine(120);
									GeoStyle style = line.getStyle() == null ? null : line.getStyle().clone();
									ArrayList<GeoLine> explodedLines = explodeGeoLine(line);

									if (explodedLines == null) {
										continue;
									}
									setStyle(style, explodedLines);
									resultGeoLines.put(id, explodedLines);
								} finally {
									if (geometry != null) {
										geometry.dispose();
									}
								}
							}

							if (updateRecordset(environment, resultGeoLines, recordset, engineType)) {
								layer.getSelection().clear();
								for (int id : resultGeoLines.keySet()) {
									Application
											.getActiveApplication()
											.getOutput()
											.output(MessageFormat.format(MapEditorProperties.getString("String_GeometryEdit_ExplodeLineOutput"),
													layer.getName(), id, resultGeoLines.get(id).size()));
								}
								TabularUtilities.refreshTabularForm(recordset.getDataset());
							}
						}
					} finally {
						releaseGeometryDictionary(resultGeoLines);
						if (recordset != null) {
							recordset.close();
							recordset.dispose();
						}
					}
				}
			}

			environment.getMapControl().getEditHistory().batchEnd();
			environment.getMap().refresh();
			environment.getMapControl().revalidate();
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
		return ListUtilities.isListOnlyContain(environment.getEditProperties().getEditableSelectedGeometryTypeFeatures(), ILineFeature.class);
	}

	private void setStyle(GeoStyle style, ArrayList<GeoLine> lines) {
		if (lines != null) {
			if (lines.size() > 0 && style != null) {
				for (GeoLine line : lines) {
					line.setStyle(style);
				}
			}
		}
	}

	private void releaseGeometryDictionary(HashMap<Integer, ArrayList<GeoLine>> geoLineDictionary) {
		if (geoLineDictionary != null) {
			for (ArrayList<GeoLine> lines : geoLineDictionary.values()) {
				releaseGeometryList(lines);
			}
		}
	}

	private void releaseGeometryList(ArrayList<GeoLine> lines) {
		if (lines != null) {
			if (lines.size() > 0) {
				for (int i = lines.size() - 1; i >= 0; i--) {
					GeoLine line = lines.get(i);

					if (line != null) {
						line.dispose();
					}
					lines.remove(i);
				}
			}
		}
	}

	private boolean updateRecordset(EditEnvironment environment, HashMap<Integer, ArrayList<GeoLine>> resultGeometrys, Recordset recordset,
			EngineType engineType) {
		Boolean result = true;

		if (resultGeometrys != null) {
			try {
				RecordsetSet set = new RecordsetSet(recordset, environment.getMapControl().getEditHistory());
				RecordsetAddNew add = new RecordsetAddNew(recordset, environment.getMapControl().getEditHistory());
				add.begin();
				set.begin();

				for (Integer id : resultGeometrys.keySet()) {
					Boolean hasReplaced = false;

					if (recordset.seekID(id)) {
						// 获取非系统字段值
						HashMap<String, Object> fieldValues = new HashMap<String, Object>();

						if (resultGeometrys.get(id).size() > 1) {
							FieldInfos fieldInfos = recordset.getFieldInfos();

							for (Integer i = 0; i < fieldInfos.getCount(); i++) {
								FieldInfo fieldInfo = fieldInfos.get(i);

								if (!fieldInfo.isSystemField()) {
									Object value = recordset.getFieldValue(fieldInfo.getName());
									fieldValues.put(fieldInfo.getName(), value);
								}
							}
						}

						for (GeoLine geoLine : resultGeometrys.get(id)) {
							// 仅对原数据设置一次
							if (!hasReplaced) {
								set.setGeometry(id, geoLine);
								hasReplaced = true;
							} else {
								add.addNew(geoLine, fieldValues);
							}
						}
					}
				}
				add.update();
				set.update();
			} catch (Exception e) {
			}
		} else {
			result = false;
		}

		return result;
	}

	private ArrayList<GeoLine> explodeGeoLine(GeoLine sourceLine) {
		ArrayList<GeoLine> resultLines = null;
		try {
			if (sourceLine != null) {
				resultLines = new ArrayList<GeoLine>();
				for (int partCount = 0; partCount < sourceLine.getPartCount(); partCount++) {
					for (int pointSequence = 0; pointSequence < sourceLine.getPart(partCount).getCount() - 1; pointSequence++) {
						Point2Ds points = new Point2Ds(new Point2D[] { sourceLine.getPart(partCount).getItem(pointSequence),
								sourceLine.getPart(partCount).getItem(pointSequence + 1) });
						GeoLine resultLine = new GeoLine(points);
						resultLines.add(resultLine);
					}
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (sourceLine != null) {
				sourceLine.dispose();
			}
		}

		return resultLines;
	}
}
