package com.supermap.desktop.geometryoperation.editor;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.ICompoundFeature;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;
import com.supermap.desktop.geometry.Implements.DGeoCompound;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.geometryoperation.*;
import com.supermap.desktop.geometryoperation.control.MapControlTip;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.utilities.GeometryUtilities;
import com.supermap.desktop.utilities.ListUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.ui.Action;
import com.supermap.ui.GeometrySelectedEvent;
import com.supermap.ui.MapControl;
import com.supermap.ui.TrackMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// @formatter:off
/**
 * 擦除，选中面特性对象或者CAD复合对象时，激活功能。
 * 按下 Ctrl，切换擦除内部或者外部
 *
 * @author highsad
 */
// @formatter:on
public class EraseEditor extends AbstractEditor {

	// 用于在 trackingLayer 中做结果展示的 id
	private static final String TAG_SOURCE = "Tag_EraseEditorSource";
	private static final Action MAP_CONTROL_ACTION = Action.SELECT;

	private IEditController eraseEditController = new EditControllerAdapter() {

		/**
		 * 由于 Ctrl 是常用组合键功能，比如 Ctrl + Z 回退。因此按 Ctrl 切换的功能点需要过滤掉使用组合键的情况。
		 */
		@Override
		public void keyTyped(EditEnvironment environment, KeyEvent e) {
			if (environment.getEditModel() instanceof EraseEditModel) {
				EraseEditModel editModel = (EraseEditModel) environment.getEditModel();
				editModel.pressedKey = editModel.pressedKey | e.getKeyCode();
			}
		}

		/**
		 * Invoked when a key has been pressed.
		 */
		@Override
		public void keyPressed(EditEnvironment environment, KeyEvent e) {
			if (environment.getEditModel() instanceof EraseEditModel) {
				EraseEditModel editModel = (EraseEditModel) environment.getEditModel();
				editModel.pressedKey = editModel.pressedKey | e.getKeyCode();
			}
		}

		@Override
		public void keyReleased(EditEnvironment environment, KeyEvent e) {
			if (environment.getEditModel() instanceof EraseEditModel) {
				EraseEditModel editModel = (EraseEditModel) environment.getEditModel();

				try {
					// 判断如果只按下了 Ctrl 键，则进行擦除模式的切换
					if (editModel.pressedKey == KeyEvent.VK_CONTROL) {
						setIsEraseExternal(!editModel.isEraseExternal, editModel);

						// 设置完 MapControlTip 上的 Tip 文本之后，需要刷新 MapControl 以解决 MapControlTip 的标签文本会有第三者遮盖的问题
						environment.getMapControl().repaint();
					}
				} finally {
					editModel.pressedKey = 0;
				}
			}
		}

		@Override
		public void geometrySelected(EditEnvironment environment, GeometrySelectedEvent arg0) {
			EraseEditor.this.geometrySelected(environment, arg0);
		}

		@Override
		public void mouseClicked(EditEnvironment environment, MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e)) {
				environment.stopEditor();
			}
		}
	};

	public EraseEditor() {
		super();

	}

	@Override
	public void activate(EditEnvironment environment) {
		EraseEditModel editModel;
		if (environment.getEditModel() instanceof EraseEditModel) {
			editModel = (EraseEditModel) environment.getEditModel();
		} else {
			editModel = new EraseEditModel();
			environment.setEditModel(editModel);
		}
		environment.setEditController(this.eraseEditController);

		editModel.oldMapControlAction = environment.getMapControl().getAction();
		editModel.oldTrackMode = environment.getMapControl().getTrackMode();
		environment.getMapControl().setAction(MAP_CONTROL_ACTION);
		environment.getMapControl().setTrackMode(TrackMode.TRACK);
		editModel.tip.bind(environment.getMapControl());
		getSrRegion(environment);
		setIsEraseExternal(false, editModel);
		Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_EraseEditor_EraseGeometry"));
	}

	@Override
	public void deactivate(EditEnvironment environment) {
		if (environment.getEditModel() instanceof EraseEditModel) {
			EraseEditModel editModel = (EraseEditModel) environment.getEditModel();

			try {
				environment.getMapControl().setAction(editModel.oldMapControlAction);
				environment.getMapControl().setTrackMode(editModel.oldTrackMode);
				clear(environment);
			} finally {
				editModel.tip.unbind();
				environment.setEditController(NullEditController.instance());
				environment.setEditModel(null);
			}
		}
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return environment.getMapControl().getEditableLayers().length > 0
				&& environment.getEditProperties().getSelectedGeometryCount() > 0
				&& ListUtilities.isListOnlyContain(environment.getEditProperties().getSelectedGeometryTypeFeatures(), IRegionFeature.class,
						ICompoundFeature.class)
				&& ListUtilities.isListContainAny(environment.getEditProperties().getEditableDatasetTypes(), DatasetType.LINE, DatasetType.REGION,
						DatasetType.CAD);
	}

	@Override
	public boolean check(EditEnvironment environment) {
		return environment.getEditor() instanceof EraseEditor;
	}

	/**
	 * 获取擦除对象的 Tracking 风格
	 *
	 * @return
	 */
	private GeoStyle getSourceStyle() {
		GeoStyle style = new GeoStyle();
		style.setLineColor(Color.RED);
		style.setLineWidth(0.5);
		style.setFillOpaqueRate(0);
		return style;
	}

	private void geometrySelected(EditEnvironment environment, GeometrySelectedEvent e) {
		if (!(environment.getEditModel() instanceof EraseEditModel)) {
			return;
		}

		EraseEditModel editModel = (EraseEditModel) environment.getEditModel();
		MapControl mapControl = (MapControl) e.getSource();
		mapControl.getEditHistory().batchBegin();
		Recordset recordset = null; // 有选中可擦除几何对象（面特性、线特性）的可编辑记录集
		Geometry eraseResult = null; // 擦除结果

		try {
			Layer[] editableLayers = mapControl.getEditableLayers();
			Layer selectLayer = null;

			if (editableLayers != null && editableLayers.length > 0) {
				for (int i = 0; i < editableLayers.length; i++) {
					Layer layer = editableLayers[i];

					if (layer.getSelection() != null && layer.getSelection().getCount() > 0) {

						// 单选模式，拿到一个就可以撤了
						selectLayer = layer;
						break;
					}
				}
			}

			if (selectLayer != null) {
				// 判断选中的对象是否是源擦除对象，如果是，则什么都不做
				if (editModel.srcInfos.containsKey(selectLayer)) {
					int selectedId = selectLayer.getSelection().get(0);

					if (editModel.srcInfos.get(selectLayer).contains(selectedId)) {
						return;
					}
				}

				recordset = selectLayer.getSelection().toRecordset();
				eraseResult = erase(mapControl.getEditHistory(), recordset, editModel);
			} else {
				Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_EraseEditor_LayerCannotEdit"));
			}
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		} finally {
			mapControl.getEditHistory().batchEnd();
			mapControl.getMap().refresh();
			mapControl.revalidate();

			if (recordset != null) {
				recordset.close();
				recordset.dispose();
			}

			if (eraseResult != null) {
				eraseResult.dispose();
			}
		}
	}

	/**
	 * 擦除对象
	 *
	 * @return
	 */
	private Geometry erase(EditHistory editHistory, Recordset recordset, EraseEditModel editModel) {
		Geometry result = null; // 擦除结果
		IGeometry geometry = null; // 被擦除对象

		try {
			recordset.moveFirst();
			geometry = DGeometryFactory.create(recordset.getGeometry());
			if (editModel.isEraseExternal) {
				result = eraseExternal(editHistory, recordset, geometry, editModel);
			} else {
				result = eraseInside(editHistory, recordset, geometry, editModel);
			}

			if (result != null) {
				editHistory.add(EditType.MODIFY, recordset, true);
				recordset.edit();
				recordset.setGeometry(result);
				recordset.update();
			} else {
				// 没有擦除结果，删除被擦除对象

				editHistory.add(EditType.DELETE, recordset, true);
				recordset.delete();
				recordset.update();
			}
		} finally {
			if (geometry != null) {
				geometry.dispose();
			}
		}
		return result;
	}

	/**
	 * 擦除内部
	 *
	 * @param editHistory
	 * @param recordset
	 * @param geometry    被擦除对象
	 * @return
	 */
	private Geometry eraseInside(EditHistory editHistory, Recordset recordset, IGeometry geometry, EraseEditModel editModel) {
		Geometry eraseResult = null;

		try {
			if (geometry instanceof IRegionFeature) {
				GeoRegion geoRegion = ((IRegionFeature) geometry).convertToRegion(120);
				try {
					eraseResult = Geometrist.erase(geoRegion, editModel.srRegion);
					if (eraseResult != null) {
						eraseResult.setStyle(geometry.getGeometry().getStyle());
					}
				} finally {
					if (geoRegion != null) {
						geoRegion.dispose();
					}
				}
			} else if (geometry instanceof ILineFeature) {
				GeoLine geoLine = ((ILineFeature) geometry).convertToLine(120);
				try {
					eraseResult = Geometrist.erase(geometry.getGeometry(), editModel.srRegion);
					if (eraseResult != null) {
						eraseResult.setStyle(geometry.getGeometry().getStyle());
					}
				} finally {
					if (geoLine != null) {
						geoLine.dispose();
					}
				}
			} else if (geometry instanceof DGeoCompound) {
				eraseResult = eraseCompound((DGeoCompound) geometry, editModel);
			} else {
				Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_EraseEditor_InvalidGeometryType"));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return eraseResult;
	}

	/**
	 * 擦除外部
	 * 
	 * @param editHistory
	 * @param recordset
	 * @param geometry
	 *            被擦除对象
	 * @return
	 */
	/**
	 * @param editHistory
	 * @param recordset
	 * @param geometry    被擦除对象
	 * @param editModel   与 EditEnvironment 绑定的过程数据
	 * @return
	 */
	private Geometry eraseExternal(EditHistory editHistory, Recordset recordset, IGeometry geometry, EraseEditModel editModel) {
		Geometry eraseResult = null;

		try {
			if (geometry instanceof IRegionFeature) {
				GeoRegion geoRegion = ((IRegionFeature) geometry).convertToRegion(120);
				try {
					eraseResult = Geometrist.clip(geoRegion, editModel.srRegion);
					if (eraseResult != null) {
						eraseResult.setStyle(geometry.getGeometry().getStyle());
					}
				} finally {
					if (geoRegion != null) {
						geoRegion.dispose();
					}
				}
			} else if (geometry instanceof ILineFeature) {
				GeoLine geoLine = ((ILineFeature) geometry).convertToLine(120);
				try {
					eraseResult = Geometrist.clip(geometry.getGeometry(), editModel.srRegion);
					if (eraseResult != null) {
						eraseResult.setStyle(geometry.getGeometry().getStyle());
					}
				} finally {
					if (geoLine != null) {
						geoLine.dispose();
					}
				}
			} else if (geometry instanceof DGeoCompound) {
				eraseResult = eraseCompound((DGeoCompound) geometry, editModel);
			} else {
				Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_EraseEditor_InvalidGeometryType"));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return eraseResult;
	}

	/**
	 * 擦除复合对象
	 *
	 * @param geoCompound
	 * @return
	 */
	private Geometry eraseCompound(DGeoCompound geoCompound, EraseEditModel editModel) {
		for (int i = geoCompound.getPartCount() - 1; i >= 0; i--) {
			IGeometry part = DGeometryFactory.create(geoCompound.getPart(i));
			Geometry partEraseResult = null; // 子对象擦除结果

			if (part instanceof DGeoCompound) {
				partEraseResult = eraseCompound(geoCompound, editModel);
			} else {
				try {
					if (part instanceof IRegionFeature) {
						GeoRegion geoRegion = ((IRegionFeature) part).convertToRegion(120);
						partEraseResult = editModel.isEraseExternal ? Geometrist.clip(editModel.srRegion, geoRegion) : Geometrist.erase(editModel.srRegion,
								geoRegion);
					} else if (part instanceof ILineFeature) {
						GeoLine geoLine = ((ILineFeature) part).convertToLine(120);
						partEraseResult = editModel.isEraseExternal ? Geometrist.clip(editModel.srRegion, geoLine) : Geometrist.erase(editModel.srRegion,
								geoLine);
					}

					if (partEraseResult == null) {

						// 擦除结果为空，移除该子项
						geoCompound.removePart(i);
					} else {
						partEraseResult.setStyle(part.getGeometry().getStyle());
						geoCompound.setPart(i, partEraseResult);
					}
				} finally {
					if (part != null) {
						part.dispose();
					}
				}
			}
		}

		return geoCompound.getPartCount() == 0 ? null : geoCompound.getGeometry();
	}

	private void setIsEraseExternal(final boolean isEraseExternal, final EraseEditModel editModel) {
		editModel.isEraseExternal = isEraseExternal;

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
//				Container parent = editModel.labelChangeMode.getParent();
				if (isEraseExternal) {
//					parent.remove(editModel.labelChangeMode);
					editModel.labelChangeMode.setText(MapEditorProperties.getString("String_EraseEditor_EraseTipExternal"));
//					editModel.tip.addLabel(editModel.labelChangeMode);
				} else {
//					parent.remove(editModel.labelChangeMode);
					editModel.labelChangeMode.setText(MapEditorProperties.getString("String_EraseEditor_EraseTipInside"));
//					editModel.tip.addLabel(editModel.labelChangeMode);

				}
			}
		});
	}

	// @formatter:off
	/**
	 * 获取用来擦除的面对象，将所有选中的面对象合并。只要重叠，做擦除就可能产生岛洞关系与预期不一致。
	 * 如果选中的对象中本就有复杂对象，则维持复杂对象的岛洞关系。
	 * 
	 * @param environment
	 */
	// @formatter:on
	private void getSrRegion(EditEnvironment environment) {
		if (!(environment.getEditModel() instanceof EraseEditModel)) {
			return;
		}

		EraseEditModel editModel = (EraseEditModel) environment.getEditModel();
		List<Layer> selectedLayers = environment.getEditProperties().getSelectedLayers();

		for (int i = 0; i < selectedLayers.size(); i++) {
			Layer layer = selectedLayers.get(i);
			ArrayList<Integer> selectedIds = new ArrayList<>();
			for (int j = 0; j < layer.getSelection().getCount(); j++) {
				selectedIds.add(layer.getSelection().get(j));
			}
			editModel.srcInfos.put(layer, selectedIds);
			Geometry unionLayer = GeometryUtilities.union(selectedLayers.get(i)); // 将该图层中选中的面对象进行合并处理
			editModel.srRegion = GeometryUtilities.union(editModel.srRegion, unionLayer, true);
			editModel.srRegion.setStyle(getSourceStyle());

			// 将结果对象添加到 TrackingLayer 做高亮显示
			environment.getMapControl().getMap().getTrackingLayer().add(editModel.srRegion, TAG_SOURCE);
			environment.getMap().refreshTrackingLayer();
		}
	}

	/**
	 * 编辑结束清理资源
	 */
	private void clear(EditEnvironment environment) {
		if (!(environment.getEditModel() instanceof EraseEditModel)) {
			return;
		}

		EraseEditModel editModel = (EraseEditModel) environment.getEditModel();
		editModel.clear();

		MapUtilities.clearTrackingObjects(environment.getMap(), TAG_SOURCE);
	}

	private class EraseEditModel implements IEditModel {
		public boolean isEraseExternal = false; // 是否擦除外部
		public Geometry srRegion; // 用来擦除的面对象

		public Action oldMapControlAction = Action.SELECT2;
		public TrackMode oldTrackMode = TrackMode.EDIT;

		public int pressedKey = 0;

		public MapControlTip tip = new MapControlTip();
		public JLabel labelMsg = new JLabel(MapEditorProperties.getString("String_EraseEditor_EraseGeometry"));
		public JLabel labelChangeMode = new JLabel();
		public HashMap<Layer, ArrayList<Integer>> srcInfos = new HashMap<>(); // 用来记录擦除对象所在图层以及对应的 id 集合

		public EraseEditModel() {
			this.tip.addLabel(labelMsg);
			this.tip.addLabel(labelChangeMode);
		}

		public void clear() {
			if (this.srRegion != null) {
				this.srRegion.dispose();
				this.srRegion = null;
			}

			this.srcInfos.clear();
			this.pressedKey = 0;
		}
	}
}
