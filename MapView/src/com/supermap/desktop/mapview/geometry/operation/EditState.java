package com.supermap.desktop.mapview.geometry.operation;

import javax.swing.event.EventListenerList;

import java.util.ArrayList;
import java.util.List;

import com.supermap.desktop.Application;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.utilties.GeometryUtilties;
import com.supermap.desktop.utilties.ListUtilties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Selection;
import com.supermap.data.DatasetType;
import com.supermap.data.GeoCompound;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoLine3D;
import com.supermap.data.GeoLineM;
import com.supermap.data.GeoRegion;
import com.supermap.data.GeoRegion3D;
import com.supermap.data.GeoStyle;
import com.supermap.data.GeoText;
import com.supermap.data.GeoText3D;
import com.supermap.data.Geometry;
import com.supermap.data.Geometry3D;
import com.supermap.data.GeometryType;
import com.supermap.data.Recordset;

public class EditState {
	private FormMap formMap;
	EditAction editAction;
	GeoStyle trackingStyle;
	int selectedGeometryCount; // 当前选中对象的数目
	boolean has2DGeometrySelected;
	boolean has3DGeometrySelected;
	int editableSelectedGeometryCount;
	List<DatasetType> selectedDatasetTypes;
	List<DatasetType> editableDatasetTypes;
	List<GeometryType> selectedGeometryTypes;
	List<GeometryKind> selectedGeometryKinds;
	List<GeometryKind> editableSelectedGeometryKinds;
	List<GeometryType> editableSelectedGeometryTypes;

	private EventListenerList listenerList = new EventListenerList();

	public EditState(FormMap formMap) {
		this.formMap = formMap;
		this.selectedDatasetTypes = new ArrayList<DatasetType>();
		this.editableDatasetTypes = new ArrayList<DatasetType>();
		this.selectedGeometryTypes = new ArrayList<GeometryType>();
		this.selectedGeometryKinds = new ArrayList<GeometryKind>();
		this.editableSelectedGeometryKinds = new ArrayList<GeometryKind>();
		this.editableSelectedGeometryTypes = new ArrayList<GeometryType>();
		this.selectedGeometryCount = 0;
		this.editableSelectedGeometryCount = 0;
	}

	public void addEditActionChangeListener(EditActionChangeListener listener) {
		this.listenerList.add(EditActionChangeListener.class, listener);
	}

	public void removeEditActionChangeListener(EditActionChangeListener listener) {
		this.listenerList.remove(EditActionChangeListener.class, listener);
	}

	public EditAction getEditAction() {
		return this.editAction;
	}

	public void setEditAction(EditAction editAction) {
		if (this.editAction != editAction) {
			fireEditActionChange(new EditActionChangeEvent(this, this.editAction, editAction));
		}
	}

	protected void fireEditActionChange(EditActionChangeEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == EditActionChangeListener.class) {
				((EditActionChangeListener) listeners[i + 1]).editActionChange(e);
			}
		}
	}

	public GeoStyle getTrackingStyle() {
		if (this.trackingStyle == null) {
			this.trackingStyle = new GeoStyle();
			this.trackingStyle.setLineSymbolID(2);
			this.trackingStyle.setFillOpaqueRate(0);
		}
		return this.trackingStyle;
	}

	public int getSelectedGeometryCount() {
		return this.selectedGeometryCount;
	}

	public List<DatasetType> getselectedDatasetTypes() {
		return this.selectedDatasetTypes;
	}

	public List<DatasetType> getEditableDatasetTypes() {
		return this.editableDatasetTypes;
	}

	public List<GeometryType> getSelectedGeometryTypes() {
		return this.editableSelectedGeometryTypes;
	}

	// 因为基本都会跑完，争取跑一遍拿完所有想要的状态，提高效率
	// 用一个Action来跑，存取状态，其它的Action直接拿。
	//

	// @formatter:off
	/**
	 * 因为基本都会跑完，争取跑一遍拿完所有想要的状态，提高效率。
	 *  用一个Action来跑，存取状态，其它的Action直接拿。
	 *  目前是用_CtrlActionCombination来跑，但这个工具条或者菜单栏一旦隐藏就完了，最好以后实现一个专门机制，后续优化。
	 */
	// @formatter:on
	public void checkEnable() {
		try {
			// 选中对象数目
			this.selectedGeometryCount = 0;
			this.editableSelectedGeometryCount = 0;
			this.has3DGeometrySelected = false;
			this.has2DGeometrySelected = false;
			this.selectedDatasetTypes.clear();
			this.editableDatasetTypes.clear();
			this.selectedGeometryTypes.clear();
			this.selectedGeometryKinds.clear();
			this.editableSelectedGeometryKinds.clear();
			this.editableSelectedGeometryTypes.clear();

			Selection[] selections = this.formMap.getMapControl().getMap().findSelection(true);
			for (Selection selection : selections) {
				// 选中对象数目
				this.selectedGeometryCount += selection.getCount();
				// 选中图层类型
				if (!this.selectedDatasetTypes.contains(selection.getDataset().getType())) {
					this.selectedDatasetTypes.add(selection.getDataset().getType());
				}

				this.processGeometryTypeList(selection, false);
			}

			for (int i = 0; i < this.formMap.getMapControl().getEditableLayers().length; i++) {
				Layer layer = this.formMap.getMapControl().getEditableLayers()[i];
				if (layer.getDataset() != null) {
					this.editableDatasetTypes.add(layer.getDataset().getType());
				}

				if (layer.getSelection() != null && layer.getSelection().getCount() > 0) {
					this.editableSelectedGeometryCount += layer.getSelection().getCount();

					this.processGeometryTypeList(layer.getSelection(), true);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void processGeometryTypeList(Selection selection, Boolean isEditableOnly) {
		// 选中对象类型
		Recordset recordset = null;
		Geometry geometry = null;
		try {
			List<GeometryType> typeList = null;
			List<GeometryKind> kindList = null;
			if (isEditableOnly) {
				kindList = editableSelectedGeometryKinds;
				typeList = editableSelectedGeometryTypes;
			} else {
				kindList = selectedGeometryKinds;
				typeList = selectedGeometryTypes;
			}

			recordset = selection.toRecordset();
			while (!recordset.isEOF()) {
				geometry = recordset.getGeometry();
				if (geometry != null) {
					if (!this.has3DGeometrySelected && geometry instanceof Geometry3D) {
						this.has3DGeometrySelected = true;
					}

					if (!this.has2DGeometrySelected && !(geometry instanceof Geometry3D)) {
						this.has2DGeometrySelected = true;
					}

					if (!typeList.contains(geometry.getType())) {
						typeList.add(geometry.getType());
					}

					if (GeometryUtilties.isPointGeometry(geometry)) {
						if (!kindList.contains(GeometryKind.Point)) {
							kindList.add(GeometryKind.Point);
						}
					} else if (GeometryUtilties.isLineGeometry(geometry)) {
						if (!kindList.contains(GeometryKind.Line)) {
							kindList.add(GeometryKind.Line);
						}
					} else if (GeometryUtilties.isRegionGeometry(geometry)) {
						if (!kindList.contains(GeometryKind.Region)) {
							kindList.add(GeometryKind.Region);
						}
					} else if (GeometryUtilties.isTextGeometry(geometry)) {
						if (!kindList.contains(GeometryKind.Text)) {
							kindList.add(GeometryKind.Text);
						}
					} else if (geometry.getType() == GeometryType.GEOCOMPOUND) {
						if (!kindList.contains(GeometryKind.Compound)) {
							kindList.add(GeometryKind.Compound);
						}
					} else if (geometry.getType() == GeometryType.GEOLINEM) {
						if (!kindList.contains(GeometryKind.LineM)) {
							kindList.add(GeometryKind.LineM);
						}
					} else {
						if (!kindList.contains(GeometryKind.Other)) {
							kindList.add(GeometryKind.Other);
						}
					}
				}
				if (geometry != null) {
					geometry.dispose();
				}
				recordset.moveNext();
			}
		} catch (

		Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			if (recordset != null) {
				recordset.dispose();
				recordset = null;
			}
			if (geometry != null) {
				geometry.dispose();
				geometry = null;
			}
		}
	}

	public boolean isEraseEnable() {
		return this.editableSelectedGeometryCount > 0
				&& ListUtilties.isListContainAny(this.editableSelectedGeometryKinds, GeometryKind.Line, GeometryKind.Region, GeometryKind.Compound);
	}

	public boolean isEraseOutPartEnable() {
		return this.selectedGeometryCount == 1 && ListUtilties.isListContainAny(this.selectedGeometryKinds, GeometryKind.Region, GeometryKind.Compound);
	}

	public boolean isEditLineMEnable() {
		return this.editableSelectedGeometryCount == 1 && ListUtilties.isListContainAny(this.selectedGeometryKinds, GeometryKind.LineM);
	}

	public boolean isDeleteLineMvalueEnable() {
		return this.editableSelectedGeometryCount > 0 && ListUtilties.isListContainAny(this.selectedGeometryKinds, GeometryKind.LineM);

	}

	public boolean isSmoothEnable() {
		return ListUtilties.isListContainAny(this.editableSelectedGeometryTypes, GeometryType.GEOLINE,
		// GeometryType.GeoLineM,
				GeometryType.GEOREGION); // 排除参数化曲线
	}

	public boolean isUnionEnable() {
		Boolean enable = false;
		if (this.selectedGeometryCount > 1 && // 选中数至少2个
				(this.has2DGeometrySelected != this.has3DGeometrySelected) && // 不能即有二维对象又有三维对象
				ListUtilties.isListOnlyContain(this.selectedGeometryKinds, GeometryKind.Region, GeometryKind.Line))// 只支持面
		{
			// 是会否存在可编辑的“可操作保存”图层
			DatasetType datasetType = DatasetType.CAD;
			if (this.selectedGeometryTypes.get(0) == GeometryType.GEOCIRCLE3D || this.selectedGeometryTypes.get(0) == GeometryType.GEOPIE3D
					|| this.selectedGeometryTypes.get(0) == GeometryType.GEOREGION3D) {
				datasetType = DatasetType.REGION3D;
			} else {
				datasetType = DatasetType.REGION;
			}

			if (this.editableDatasetTypes.size() > 0 && ListUtilties.isListContainAny(this.editableDatasetTypes, DatasetType.CAD, datasetType)) {
				enable = true;
			}
		}
		return enable;
	}

	public boolean isSplitByRegionEnable() {
		return (this.editableSelectedGeometryCount > 0 && ListUtilties.isListContainAny(this.editableDatasetTypes, DatasetType.REGION, DatasetType.LINE,
				DatasetType.CAD));
	}

	public boolean isSplitByLineEnable() {
		return (this.editableSelectedGeometryCount > 0 && ListUtilties.isListContainAny(this.editableDatasetTypes, DatasetType.REGION, DatasetType.LINE,
				DatasetType.CAD));
	}

	public boolean isSplitByGeometryEnable() {
		return (this.editableSelectedGeometryCount > 0 && ListUtilties.isListContainAny(this.editableDatasetTypes, DatasetType.REGION, DatasetType.LINE,
				DatasetType.CAD));
	}

	public boolean isRotateEnable() {
		return (this.editableSelectedGeometryCount > 0 && ListUtilties.isListOnlyContain(this.editableSelectedGeometryKinds, GeometryKind.Compound,
				GeometryKind.Text, GeometryKind.Point, GeometryKind.Region, GeometryKind.Line));
	}

	public boolean isReshapeEnable() {
		return (this.editableSelectedGeometryCount == 1 && ListUtilties.isListOnlyContain(this.editableSelectedGeometryKinds, GeometryKind.Compound));
	}

	public boolean isResampleEnable() {
		return ListUtilties.isListOnlyContain(this.editableSelectedGeometryTypes, GeometryType.GEOLINE, GeometryType.GEOREGION);
	}

	public boolean isOffsetEnable() {
		return ListUtilties.isListContainAny(this.editableDatasetTypes, DatasetType.LINE, DatasetType.REGION, DatasetType.CAD);
	}

	public boolean isMirrorEnable() {
		return (this.editableSelectedGeometryCount > 0 && !ListUtilties.isListContainAny(this.editableSelectedGeometryKinds, GeometryKind.Text,
				GeometryKind.Other));
	}

	public boolean isPartialUpdateEnable() {
		return ListUtilties.isListContainAny(this.editableDatasetTypes, DatasetType.CAD, DatasetType.REGION, DatasetType.LINE);
	}

	public boolean isMoveObjEnable() {
		return (this.editableSelectedGeometryCount > 0);
	}

	public boolean isPointsAvgErrorEnable() {
		return ListUtilties.isListContainAny(this.editableDatasetTypes, DatasetType.LINE, DatasetType.CAD);
	}

	public boolean isLineReverseEnable() {
		return ListUtilties.isListContainAny(this.editableSelectedGeometryTypes, GeometryType.GEOLINE, GeometryType.GEOLINEM);
	}

	public boolean isFilletEnable() {
		Boolean result = false;
		Recordset recordset = null;
		try {
			if (this.editableSelectedGeometryCount == 2) {
				Layer layer = this.formMap.getMapControl().getActiveEditableLayer();
				if (layer.isEditable()
						&& layer.getSelection().getCount() == 2
						&& (layer.getDataset().getType() == DatasetType.LINE || layer.getDataset().getType() == DatasetType.CAD || layer.getDataset().getType() == DatasetType.LINEM)) {
					recordset = layer.getSelection().toRecordset();
					GeoLine geometry1 = (GeoLine) recordset.getGeometry();
					recordset.moveNext();
					GeoLine geometry2 = (GeoLine) recordset.getGeometry();
					if (geometry1 != null && geometry2 != null && geometry1.getPartCount() == 1 && geometry1.getPart(0).getCount() == 2
							&& geometry2.getPartCount() == 1 && geometry2.getPart(0).getCount() == 2) {
						result = true;
					}
					if (geometry1 != null) {
						geometry1.dispose();
						geometry1 = null;
					}
					if (geometry2 != null) {
						geometry2.dispose();
						geometry2 = null;
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			if (recordset != null) {
				recordset.dispose();
				recordset = null;
			}
		}
		return result;
	}

	public boolean isDecomposeEnable() {
		Boolean result = false;
		Recordset recordset = null;
		try {
			if (this.editableSelectedGeometryCount == 1) {
				Layer layer = this.formMap.getMapControl().getActiveEditableLayer();
				recordset = layer.getSelection().toRecordset();
				Geometry geometry = recordset.getGeometry();
				if (geometry != null) {
					if (geometry.getType() == GeometryType.GEOCOMPOUND) {
						result = true;
					} else if (geometry.getType() == GeometryType.GEOLINE) {
						result = (((GeoLine) geometry).getPartCount() > 1);
					} else if (geometry.getType() == GeometryType.GEOLINE3D) {
						result = (((GeoLine3D) geometry).getPartCount() > 1);
					} else if (geometry.getType() == GeometryType.GEOREGION) {
						result = (((GeoRegion) geometry).getPartCount() > 1);
					} else if (geometry.getType() == GeometryType.GEOREGION3D) {
						result = (((GeoRegion3D) geometry).getPartCount() > 1);
					} else if (geometry.getType() == GeometryType.GEOLINEM) {
						result = (((GeoLineM) geometry).getPartCount() > 1);
					} else if (geometry.getType() == GeometryType.GEOTEXT) {
						result = (((GeoText) geometry).getPartCount() > 1);
					} else {
						// 其它类型默认不做处理
					}
					if (geometry != null) {
						geometry.dispose();
						geometry = null;
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			if (recordset != null) {
				recordset.dispose();
				recordset = null;
			}
		}
		return result;
	}

	public boolean isCombinationEnable() {
		Boolean result = false;
		try {
			if (this.selectedGeometryCount > 1) {
				if (this.selectedDatasetTypes.size() > 1)// 多种数据集时，目标要为CAD
				{
					if (this.editableDatasetTypes.contains(DatasetType.CAD)) {
						result = true;
					}
				} else if (this.selectedDatasetTypes.size() == 1) // 只有一种时，目标相同或为CAD
				{
					if (!(this.selectedDatasetTypes.get(0) == DatasetType.POINT || this.selectedDatasetTypes.get(0) == DatasetType.POINT3D)) {
						if (this.editableDatasetTypes.contains(DatasetType.CAD) || this.editableDatasetTypes.contains(this.selectedDatasetTypes.get(0))) {
							result = true;
						}
					}
				} else {
					// 不做任何处理
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	public boolean isClipRegionByRegionEnable() {
		return (this.editableDatasetTypes.size() == 1 && this.editableDatasetTypes.get(0) == DatasetType.REGION);
	}

	public boolean isExplodeLineEnable() {
		Boolean result = false;
		try {
			if (this.editableSelectedGeometryCount > 0) {
				result = this.editableSelectedGeometryKinds.contains(GeometryKind.Line);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	public boolean isJointLineEnable() {
		Boolean result = false;
		if (ListUtilties.isListContainAny(this.editableDatasetTypes, DatasetType.CAD, DatasetType.LINE) && this.selectedGeometryCount > 1) {
			if (ListUtilties.isListOnlyContain(this.selectedGeometryKinds, GeometryKind.Line)) {
				result = true;
			}
		}
		return result;
	}

	public boolean isCopyObjEnable() {
		return (this.editableSelectedGeometryCount > 0);
	}

	public boolean isHolyRegionEnable() {
		Boolean result = false;
		if (ListUtilties.isListContainAny(this.editableDatasetTypes, DatasetType.CAD, DatasetType.REGION) && this.selectedGeometryCount > 1) {
			if (ListUtilties.isListOnlyContain(this.selectedGeometryKinds, GeometryKind.Region)) {
				result = true;
			}
		}
		return result;
	}

	public boolean isChamferEnable() {
		Boolean result = false;
		Recordset recordset = null;
		try {
			if (this.editableSelectedGeometryCount == 2) {
				Layer layer = this.formMap.getMapControl().getActiveEditableLayer();
				if (layer != null
						&& layer.isEditable()
						&& layer.getSelection().getCount() == 2
						&& (layer.getDataset().getType() == DatasetType.LINE || layer.getDataset().getType() == DatasetType.CAD || layer.getDataset().getType() == DatasetType.LINEM)) {
					recordset = layer.getSelection().toRecordset();
					GeoLine geometry1 = (GeoLine) recordset.getGeometry();
					recordset.moveNext();
					GeoLine geometry2 = (GeoLine) recordset.getGeometry();
					if (geometry1 != null && geometry2 != null && geometry1.getPartCount() == 1 && geometry1.getPart(0).getCount() == 2
							&& geometry2.getPartCount() == 1 && geometry2.getPart(0).getCount() == 2) {
						result = true;
					}
					if (geometry1 != null) {
						geometry1.dispose();
						geometry1 = null;
					}
					if (geometry2 != null) {
						geometry2.dispose();
						geometry2 = null;
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			if (recordset != null) {
				recordset.dispose();
				recordset = null;
			}
		}
		return result;
	}

	public boolean isBreakEnable() {
		Boolean enable = false;
		try {
			if (ListUtilties.isListContainAny(this.editableDatasetTypes, DatasetType.LINE, DatasetType.LINEM, DatasetType.CAD)
					&& ListUtilties.isListOnlyContain(this.selectedGeometryTypes, GeometryType.GEOLINE)) {
				enable = true;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return enable;
	}
}
