package com.supermap.desktop.geometryoperation.CtrlAction;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.EditType;
import com.supermap.data.GeoCompound;
import com.supermap.data.GeoStyle;
import com.supermap.data.Geometrist;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.geometry.Abstract.ILineConvertor;
import com.supermap.desktop.geometry.Abstract.IRegionConvertor;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.mapview.geometry.operation.EditAction;
import com.supermap.desktop.utilties.ArrayUtilties;
import com.supermap.desktop.utilties.GeometryUtilties;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Selection;
import com.supermap.ui.Action;
import com.supermap.ui.GeometrySelectedEvent;
import com.supermap.ui.GeometrySelectedListener;
import com.supermap.ui.TrackMode;

public class CtrlActionErase extends CtrlActionGeometryEditBase {

	private static final String G_TRACKINGTAG = "GeometryEditErase";
	// private UserTipsControl this.userTips = null;
	private Geometry eraseGeometry = null;

	private Map<Layer, List<Integer>> forEraseGeometryIDs;

	private GeometrySelectedListener geometrySelectedListener = new GeometrySelectedListener() {

		@Override
		public void geometrySelected(GeometrySelectedEvent arg0) {
			mapControl_GeometrySelected(arg0);
		}
	};

	public CtrlActionErase(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean getEditEnable() {
		boolean result = false;
		if (Application.getActiveApplication().getActiveForm() instanceof FormMap) {
			result = ((FormMap) Application.getActiveApplication().getActiveForm()).getEditState().isEraseEnable();
		}

		return result;
	}

	@Override
	protected EditAction getEditAction() {
		return EditAction.ERASE;
	}

	@Override
	protected void startEdit() {
		try {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			formMap.getMapControl().setAction(Action.SELECT);
			formMap.getMapControl().setTrackMode(TrackMode.TRACK);
			// if (this.userTips == null) {
			// this.userTips = new UserTipsControl(formMap.MapControl);
			// }
			// this.userTips.Text = Properties.MapEditorResources.String_Tip_Eidt_Erase;
			// this.userTips.StartToShow();

			super.startEdit();
			this.forEraseGeometryIDs = new HashMap<Layer, List<Integer>>();
			GeoStyle style = new GeoStyle();
			style.setLineColor(Color.RED);
			style.setFillOpaqueRate(0);

			List<Layer> layers = MapUtilties.getLayers(formMap.getMapControl().getMap());
			for (Layer layer : layers) {
				if (layer.isEditable() && layer.getDataset() != null && layer.getDataset() instanceof DatasetVector
						&& layer.getDataset().getType() != DatasetType.TEXT && layer.getDataset().getType() != DatasetType.POINT
						&& layer.getDataset().getType() != DatasetType.POINT3D && layer.getSelection().getCount() > 0) {
					Recordset recordset = layer.getSelection().toRecordset();
					DatasetVector vector = (DatasetVector) layer.getDataset();
					Geometry geometry = null;
					try {
						if (!this.forEraseGeometryIDs.containsKey(layer)) {
							this.forEraseGeometryIDs.put(layer, new ArrayList<Integer>());
						}
						while (!recordset.isEOF()) {
							geometry = recordset.getGeometry();
							if (GeometryUtilties.isLineGeometry(geometry) || GeometryUtilties.isRegionGeometry(geometry)
									|| geometry.getType() == GeometryType.GEOCOMPOUND) {
								GeometryUtilties.setGeometryStyle(geometry, style);
								formMap.getMapControl().getMap().getTrackingLayer().add(geometry, G_TRACKINGTAG);
								this.forEraseGeometryIDs.get(layer).add(recordset.getID());
							}
							geometry.dispose();
							geometry = null;
							recordset.moveNext();
						}
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

				layer.getSelection().clear();
				formMap.getMapControl().getMap().refreshTrackingLayer();
				// formMap.getMapControl().getMap().Layers.LayerRemoving += new LayerRemovingEventHandler(Layers_LayerRemoving);
				formMap.getMapControl().addGeometrySelectedListener(this.geometrySelectedListener);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	protected void endEdit() {
		try {
			super.endEdit();

			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			formMap.getMapControl().removeGeometrySelectedListener(this.geometrySelectedListener);
			// formMap.MapControl.Map.Layers.LayerRemoving -= new LayerRemovingEventHandler(Layers_LayerRemoving);
			formMap.getMapControl().setAction(Action.SELECT2);
			// _EditToolkit.RemoveTrackingTag(formMap.MapControl, g_TrackingTag);
			// if (this.userTips != null)
			// {
			// this.userTips.Close();
			// this.userTips.Dispose();
			// this.userTips = null;
			// }

			// 还原选择
			for (Layer layer : this.forEraseGeometryIDs.keySet()) {
				int[] ids = ArrayUtilties.convertToInt(this.forEraseGeometryIDs.get(layer).toArray(new Integer[this.forEraseGeometryIDs.get(layer).size()]));
				layer.getSelection().addRange(ids);
			}
			this.forEraseGeometryIDs.clear();
			this.forEraseGeometryIDs = null;
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private GeoCompound eraseGeocompound(GeoCompound compound, Geometry eraseGeometry) {
		try {
			for (int i = compound.getPartCount() - 1; i >= 0; i--) {
				if (compound.getPart(i) instanceof GeoCompound) {
					compound.setPart(i, eraseGeocompound((GeoCompound) compound.getPart(i), eraseGeometry));
				} else {
					if (Geometrist.canContain(eraseGeometry, compound.getPart(i))) {
						compound.removePart(i);
					} else {
						Boolean canConClip = false;
						GeoStyle style = null;
						if (GeometryUtilties.isRegionGeometry(compound.getPart(i))) {
							if (compound.getPart(i).getType() != GeometryType.GEOREGION) {
								IRegionConvertor regionConvertor = (IRegionConvertor) DGeometryFactory.create(compound.getPart(i));
								compound.setPart(i, regionConvertor.convertToRegion(60));
							}
							canConClip = true;
						} else if (GeometryUtilties.isLineGeometry(compound.getPart(i))) {
							if (compound.getPart(i).getType() != GeometryType.GEOLINE) {
								ILineConvertor lineConvertor = (ILineConvertor) DGeometryFactory.create(compound.getPart(i));
								compound.setPart(i, lineConvertor.convertToLine(60));
							}

							canConClip = true;
						}
						if (canConClip) {
							compound.setPart(i, Geometrist.erase(compound.getPart(i), eraseGeometry));// Erase会释放compound[i]
							compound.getPart(i).setStyle(style);
						}
					}

				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return compound;
	}

	void mapControl_GeometrySelected(GeometrySelectedEvent e) {
		this.eraseGeometry = null;
		try {
			if (this.forEraseGeometryIDs != null && this.forEraseGeometryIDs.size() > 0) {
				IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
				// 获取用于擦的面对象
				Selection[] selections = formMap.getMapControl().getMap().findSelection(true);
				if (selections.length > 0) {
					for (Selection selection : selections) {
						Recordset recordset = selection.toRecordset();
						Geometry regionGeometry = null;
						try {
							regionGeometry = recordset.getGeometry();// 单选
							if (this.eraseGeometry == null && regionGeometry != null) {
								if (regionGeometry.getType() == GeometryType.GEOREGION) {
									this.eraseGeometry = regionGeometry;
								} else if (GeometryUtilties.isRegionGeometry(regionGeometry)
										|| (regionGeometry instanceof GeoCompound && GeometryUtilties.isCompoundContainRegion((GeoCompound) regionGeometry))) {
									IRegionConvertor regionConvertor = (IRegionConvertor) DGeometryFactory.create(regionGeometry);
									this.eraseGeometry = regionConvertor.convertToRegion(120);
								}
							}
							if (this.eraseGeometry != null) {
								selection.clear();
							}
						} catch (Exception ex) {
							Application.getActiveApplication().getOutput().output(ex);
						} finally {
							if (recordset != null) {
								recordset.dispose();
							}
							if (this.eraseGeometry != regionGeometry && regionGeometry != null) {
								regionGeometry.dispose();
							}
						}
					}
				}
				if (this.eraseGeometry != null) {
					formMap.getMapControl().getEditHistory().batchBegin();
					for (Layer layer : this.forEraseGeometryIDs.keySet()) {
						DatasetVector datasetVector = (DatasetVector) layer.getDataset();
						Recordset recordset = datasetVector.getRecordset(false, CursorType.DYNAMIC);
						try {
							for (int id : this.forEraseGeometryIDs.get(layer)) {
								if (id != this.eraseGeometry.getID() && recordset.seekID(id)) {
									Geometry geometry = recordset.getGeometry();
									if (!(geometry instanceof GeoCompound) && Geometrist.canContain(this.eraseGeometry, geometry)) {
										formMap.getMapControl().getEditHistory().add(EditType.DELETE, recordset, true);
										recordset.delete();
										recordset.update();
									} else {
										Geometry newGeometry = null;
										if (geometry instanceof GeoCompound) {
											newGeometry = eraseGeocompound((GeoCompound) geometry, this.eraseGeometry);
										} else {
											newGeometry = Geometrist.erase(geometry, this.eraseGeometry);
										}
										if (newGeometry != null) {
											if (!(geometry instanceof GeoCompound)) {
												newGeometry.setStyle(geometry.getStyle());
											}
											formMap.getMapControl().getEditHistory().add(EditType.MODIFY, recordset, true);
											recordset.edit();
											recordset.setGeometry(newGeometry);
											recordset.update();
										}
										if (newGeometry != null) {
											newGeometry.dispose();
										}
									}
									if (geometry != null) {
										geometry.dispose();
									}
								}
							}
						} catch (Exception ex) {
							Application.getActiveApplication().getOutput().output(ex);
						} finally {
							if (recordset != null) {
								recordset.close();
								recordset.dispose();
							}
						}
					}
					if (this.eraseGeometry != null) {
						this.eraseGeometry.dispose();
					}
					formMap.getMapControl().getEditHistory().batchEnd();
					formMap.getMapControl().getMap().refresh();
					endEdit();
				} else {
					Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_Output_Edit_SelectedNotARegion"));
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	// void Layers_LayerRemoving(LayerRemovingEvent e) {
	// if (this.forEraseGeometryIDs.ContainsKey(e.Layer)) {
	// this.forEraseGeometryIDs.Remove(e.Layer);
	// }
	// }

	@Override
	public boolean enable() {
		if (this.check()) {
			return true;
		} else {
			return super.enable();
		}
	}
}
