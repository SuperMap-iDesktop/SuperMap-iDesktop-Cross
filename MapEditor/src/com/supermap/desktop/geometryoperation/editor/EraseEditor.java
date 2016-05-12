package com.supermap.desktop.geometryoperation.editor;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.EventObject;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.supermap.data.EditHistory;
import com.supermap.data.EditType;
import com.supermap.data.Geometrist;
import com.supermap.data.Geometry;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.ICompoundFeature;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;
import com.supermap.desktop.geometry.Implements.DGeoCompound;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.geometryoperation.EditControllerAdapter;
import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.desktop.geometryoperation.IEditController;
import com.supermap.desktop.geometryoperation.IEditModel;
import com.supermap.desktop.geometryoperation.NullEditController;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.utilties.GeometryUtilties;
import com.supermap.desktop.utilties.ListUtilties;
import com.supermap.desktop.utilties.MapControlUtilties;
import com.supermap.mapping.Layer;
import com.supermap.ui.Action;
import com.supermap.ui.GeometrySelectedEvent;
import com.supermap.ui.MapControl;
import com.supermap.ui.TrackMode;

// @formatter:off
/**
 * 擦除，选中面特性对象或者CAD复合对象时，激活功能。
 * 按下 Ctrl，切换擦除内部或者外部
 * @author highsad
 *
 */
// @formatter:on
public class EraseEditor extends AbstractEditor {

	// 用于在 trackingLayer 中做结果展示的 id
	private static final String TAG_ERASE = "Tag_EraseEditorResult";
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
		public void undone(EditEnvironment environment, EventObject arg0) {

			// undone 的时候清除 trackingLayer
			MapControlUtilties.clearTrackingObjects((MapControl) arg0.getSource(), TAG_ERASE);
		}

		@Override
		public void redone(EditEnvironment environment, EventObject arg0) {

			// redone 的时候清除 trackingLayer
			MapControlUtilties.clearTrackingObjects((MapControl) arg0.getSource(), TAG_ERASE);
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
		environment.setEditController(this.eraseEditController);
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
				environment.setEditController(NullEditController.instance());
				clear(environment);
			} finally {
				editModel.tip.unbind();
			}
		}
	}

	@Override
	public boolean enble(EditEnvironment environment) {
		return environment.getMapControl().getEditableLayers().length > 0
				&& environment.getEditProperties().getSelectedGeometryCount() > 0
				&& ListUtilties.isListContainAny(environment.getEditProperties().getSelectedGeometryTypeFeatures(), IRegionFeature.class,
						ICompoundFeature.class);
	}

	@Override
	public boolean check(EditEnvironment environment) {
		return environment.getEditor() instanceof EraseEditor;
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

			if (editableLayers != null && editableLayers.length > 0) {
				for (int i = 0; i < editableLayers.length; i++) {
					Layer layer = editableLayers[i];

					if (layer.getSelection() != null && layer.getSelection().getCount() > 0) {

						// 单选模式，拿到一个就可以撤了
						recordset = layer.getSelection().toRecordset();
						break;
					}
				}
			}

			if (recordset != null) {
				eraseResult = erase(mapControl.getEditHistory(), recordset, editModel);

				if (eraseResult != null) {
					addResultTracking(mapControl, eraseResult);
				}
			} else {
				Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_EraseEditor_LayerCannotEdit"));
			}
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		} finally {
			mapControl.getEditHistory().batchEnd();
			mapControl.getMap().refresh();

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
	 * @param geometry
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
	 * @param geometry
	 *            被擦除对象
	 * @return
	 */
	private Geometry eraseInside(EditHistory editHistory, Recordset recordset, IGeometry geometry, EraseEditModel editModel) {
		Geometry eraseResult = null;

		try {
			if (geometry instanceof IRegionFeature || geometry instanceof ILineFeature) {
				eraseResult = Geometrist.erase(geometry.getGeometry(), editModel.srRegion);

				// 没有擦除结果，删除被擦除对象
				if (eraseResult == null) {
					editHistory.add(EditType.DELETE, recordset, true);
					recordset.delete();
					recordset.update();
				}
			} else if (geometry instanceof DGeoCompound) {
				eraseCompound((DGeoCompound) geometry, editModel);
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
	private Geometry eraseExternal(EditHistory editHistory, Recordset recordset, IGeometry geometry, EraseEditModel editModel) {
		Geometry eraseResult = null;

		try {
			if (geometry instanceof IRegionFeature || geometry instanceof ILineFeature) {
				eraseResult = Geometrist.clip(geometry.getGeometry(), editModel.srRegion);

				// 没有擦除结果，删除被擦除对象
				if (eraseResult == null) {
					editHistory.add(EditType.DELETE, recordset, true);
					recordset.delete();
					recordset.update();
				}
			} else if (geometry instanceof DGeoCompound) {
				eraseCompound((DGeoCompound) geometry, editModel);
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
		Geometry eraseResult = null;

		for (int i = geoCompound.getPartCount() - 1; i >= 0; i--) {
			IGeometry part = DGeometryFactory.create(geoCompound.getPart(i));
			Geometry partEraseResult = null; // 子对象擦除结果

			if (part instanceof DGeoCompound) {
				partEraseResult = eraseCompound(geoCompound, editModel);
			} else if (part instanceof IRegionFeature || part instanceof ILineFeature) {
				if (editModel.isEraseExternal) {
					partEraseResult = Geometrist.clip(editModel.srRegion, part.getGeometry());
				} else {
					partEraseResult = Geometrist.erase(editModel.srRegion, part.getGeometry());
				}
			}

			if (partEraseResult == null) {

				// 擦除结果为空，移除该子项
				geoCompound.removePart(i);
			} else {
				geoCompound.setPart(i, partEraseResult);
			}
		}
		return eraseResult;
	}

	private void setIsEraseExternal(final boolean isEraseExternal, final EraseEditModel editModel) {
		editModel.isEraseExternal = isEraseExternal;

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				if (isEraseExternal) {
					editModel.labelChangeMode.setText(MapEditorProperties.getString("String_EraseEditor_EraseTipExternal"));
				} else {
					editModel.labelChangeMode.setText(MapEditorProperties.getString("String_EraseEditor_EraseTipInside"));
				}

				editModel.labelChangeMode.repaint();
			}
		});
	}

	private void addResultTracking(MapControl mapControl, Geometry geometry) {
		if (mapControl != null) {
			mapControl.getMap().getTrackingLayer().add(geometry, TAG_ERASE);
		}
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
			Geometry unionLayer = GeometryUtilties.union(selectedLayers.get(i)); // 将该图层中选中的面对象进行合并处理
			editModel.srRegion = GeometryUtilties.union(editModel.srRegion, unionLayer, true);
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

		MapControlUtilties.clearTrackingObjects(environment.getMapControl(), TAG_ERASE);
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

		public EraseEditModel() {
			this.tip.getContentPanel().setLayout(new BoxLayout(this.tip.getContentPanel(), BoxLayout.Y_AXIS));
			this.tip.getContentPanel().add(labelMsg);
			this.tip.getContentPanel().add(labelChangeMode);
			this.tip.getContentPanel().setSize(200, 35);
			this.tip.getContentPanel().setBackground(new Color(255, 255, 255, 150));
		}

		public void clear() {
			if (this.srRegion != null) {
				this.srRegion.dispose();
				this.srRegion = null;
			}
		}
	}
}
