package com.supermap.desktop.geometryoperation;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.SwingUtilities;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.core.MouseButtons;
import com.supermap.desktop.geometry.Abstract.ICompoundFeature;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILine3DFeature;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IMultiPartFeature;
import com.supermap.desktop.geometry.Abstract.INormalFeature;
import com.supermap.desktop.geometry.Abstract.IPointFeature;
import com.supermap.desktop.geometry.Abstract.IRegion3DFeature;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;
import com.supermap.desktop.geometry.Abstract.ITextFeature;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.geometryoperation.editor.IEditor;
import com.supermap.desktop.geometryoperation.editor.NullEditor;
import com.supermap.desktop.utilties.ListUtilties;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerEditableChangedEvent;
import com.supermap.mapping.LayerEditableChangedListener;
import com.supermap.mapping.LayerRemovedEvent;
import com.supermap.mapping.LayerRemovedListener;
import com.supermap.mapping.Layers;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapClosedEvent;
import com.supermap.mapping.MapClosedListener;
import com.supermap.mapping.MapOpenedEvent;
import com.supermap.mapping.MapOpenedListener;
import com.supermap.mapping.Selection;
import com.supermap.ui.Action;
import com.supermap.ui.ActionChangedEvent;
import com.supermap.ui.ActionChangedListener;
import com.supermap.ui.GeometrySelectChangedEvent;
import com.supermap.ui.GeometrySelectChangedListener;
import com.supermap.ui.GeometrySelectedEvent;
import com.supermap.ui.GeometrySelectedListener;
import com.supermap.ui.MapControl;
import com.supermap.ui.RedoneListener;
import com.supermap.ui.TrackedEvent;
import com.supermap.ui.TrackedListener;
import com.supermap.ui.TrackingEvent;
import com.supermap.ui.TrackingListener;
import com.supermap.ui.UndoneListener;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Recordset;

// @formatter:off
/**
 * 1.需要与地图交互的编辑
 * 2.选中之后，点击执行出结果的编辑
 * 地图窗口任何时候只允许存在一种编辑状态
 * @author highsad
 *
 */
// @formatter:on
public class EditEnvironment {

	private IFormMap formMap;
	private EditProperties properties = new EditProperties();
	private IEditor editor = NullEditor.INSTANCE;
	private boolean isInitialAction = false; // 某些编辑功能需要搭配 MapControl 的 Action 使用，这时候不需要执行一些 ActionChanged 的回调方法
	private boolean isMiddleMousePressed = false;

	private IEditModel editModel;
	private IEditController editController = NullEditController.instance();

	private MouseListener mouseListener = new MouseListener() {

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getButton() == MouseButtons.MIDDLE_BUTTON) {
				EditEnvironment.this.isMiddleMousePressed = false;
			}

			EditEnvironment.this.editController.mouseReleased(EditEnvironment.this, e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseButtons.MIDDLE_BUTTON) {
				EditEnvironment.this.isMiddleMousePressed = true;
			}

			EditEnvironment.this.editController.mousePressed(EditEnvironment.this, e);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			EditEnvironment.this.editController.mouseExited(EditEnvironment.this, e);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			EditEnvironment.this.editController.mouseEntered(EditEnvironment.this, e);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e)) {
				activateEditor(NullEditor.INSTANCE);
			} else {
				EditEnvironment.this.editController.mouseClicked(EditEnvironment.this, e);
			}
		}
	};
	private KeyListener keyListener = new KeyListener() {

		@Override
		public void keyTyped(KeyEvent e) {
			EditEnvironment.this.editController.keyTyped(EditEnvironment.this, e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			EditEnvironment.this.editController.keyReleased(EditEnvironment.this, e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				activateEditor(NullEditor.INSTANCE);
			}

			EditEnvironment.this.editController.keyPressed(EditEnvironment.this, e);
		}
	};
	private ActionChangedListener actionChangedListener = new ActionChangedListener() {

		@Override
		public void actionChanged(ActionChangedEvent arg0) {
			if (!EditEnvironment.this.isInitialAction && isMiddleMousePressed && arg0.getOldAction() != Action.PAN) {
				activateEditor(NullEditor.INSTANCE);
			}
		}
	};
	private GeometrySelectedListener geometrySelectedListener = new GeometrySelectedListener() {

		@Override
		public void geometrySelected(GeometrySelectedEvent arg0) {
			EditEnvironment.this.editController.geometrySelected(EditEnvironment.this, arg0);
		}
	};
	private GeometrySelectChangedListener geometrySelectChangedListener = new GeometrySelectChangedListener() {

		@Override
		public void geometrySelectChanged(GeometrySelectChangedEvent arg0) {
			EditEnvironment.this.geometrySelectChanged(arg0);
			EditEnvironment.this.editController.geometrySelectChanged(EditEnvironment.this, arg0);
		}
	};
	private LayerEditableChangedListener layerEditableChangedListener = new LayerEditableChangedListener() {

		@Override
		public void editableChanged(LayerEditableChangedEvent arg0) {
			EditEnvironment.this.editableChanged(arg0);
		}
	};
	private LayerRemovedListener layerRemovedListener = new LayerRemovedListener() {

		@Override
		public void layerRemoved(LayerRemovedEvent arg0) {
			EditEnvironment.this.layerRemoved(arg0);
		}
	};
	private MapClosedListener mapClosedListener = new MapClosedListener() {

		@Override
		public void mapClosed(MapClosedEvent arg0) {
			EditEnvironment.this.mapClosed(arg0);
		}
	};
	private MapOpenedListener mapOpenedListener = new MapOpenedListener() {

		@Override
		public void mapOpened(MapOpenedEvent arg0) {
			EditEnvironment.this.mapOpened(arg0);
		}
	};
	private RedoneListener redoneListener = new RedoneListener() {

		@Override
		public void redone(EventObject arg0) {
			EditEnvironment.this.editController.redone(EditEnvironment.this, arg0);
		}
	};
	private UndoneListener undoneListener = new UndoneListener() {

		@Override
		public void undone(EventObject arg0) {
			EditEnvironment.this.editController.undone(EditEnvironment.this, arg0);
		}
	};
	private TrackingListener trackingListener = new TrackingListener() {

		@Override
		public void tracking(TrackingEvent arg0) {
			EditEnvironment.this.editController.tracking(EditEnvironment.this, arg0);
		}
	};
	private TrackedListener trackedListener = new TrackedListener() {

		@Override
		public void tracked(TrackedEvent arg0) {
			EditEnvironment.this.editController.tracked(EditEnvironment.this, arg0);
		}
	};

	private EditEnvironment(IFormMap formMap) {
		this.formMap = formMap;

		if (this.formMap != null) {
			this.formMap.getMapControl().addMouseListener(this.mouseListener);
			this.formMap.getMapControl().addKeyListener(this.keyListener);
			this.formMap.getMapControl().addActionChangedListener(this.actionChangedListener);

			this.formMap.getMapControl().addRedoneListener(this.redoneListener);
			this.formMap.getMapControl().addUndoneListener(this.undoneListener);
			this.formMap.getMapControl().addTrackingListener(this.trackingListener);
			this.formMap.getMapControl().addTrackedListener(this.trackedListener);
			// 选中对象状态改变
			this.formMap.getMapControl().addGeometrySelectChangedListener(this.geometrySelectChangedListener);
			this.formMap.getMapControl().addGeometrySelectedListener(this.geometrySelectedListener);
			// 图层可编辑状态改变
			this.formMap.getMapControl().getMap().getLayers().addLayerEditableChangedListener(this.layerEditableChangedListener);
			this.formMap.getMapControl().getMap().getLayers().addLayerRemovedListener(this.layerRemovedListener);
			this.formMap.getMapControl().getMap().addMapClosedListener(this.mapClosedListener);
			this.formMap.getMapControl().getMap().addMapOpenedListener(this.mapOpenedListener);

			// 工具条上的下拉按钮在点开的时候才会构造，在那之前对地图的操作都没有记录，因此在这里初始化一下。
			geometryStatusChange();
			layersStatusChange();
		}
	}

	public IEditModel getEditModel() {
		return this.editModel;
	}

	public void setEditModel(IEditModel editModel) {
		this.editModel = editModel;
	}

	public void setEditController(IEditController editController) {
		this.editController = editController;
	}

	/**
	 * 获取所有图层
	 * 
	 * @return
	 */
	public List<Layer> getAllLayers() {
		return MapUtilties.getLayers(this.formMap.getMapControl().getMap());
	}

	public IFormMap getFormMap() {
		return this.formMap;
	}

	public MapControl getMapControl() {
		return this.formMap.getMapControl();
	}

	public Map getMap() {
		return this.formMap.getMapControl().getMap();
	}

	public Layer getActiveEditableLayer() {
		return this.formMap.getMapControl().getActiveEditableLayer();
	}

	public Layer[] getEditableLayers() {
		return this.formMap.getMapControl().getEditableLayers();
	}

	public EditProperties getEditProperties() {
		return this.properties;
	}

	public IEditor getEditor() {
		return this.editor;
	}

	public void activateEditor(IEditor editor) {
		try {
			this.isInitialAction = true;
			this.editor.deactivate(this);

			if (this.editor != NullEditor.INSTANCE && this.editor == editor) {
				this.editor = NullEditor.INSTANCE;
			} else {
				this.editor = editor;
			}
			this.editor.activate(this);
		} finally {
			this.isInitialAction = false;
		}
	}

	public void stopEditor() {
		activateEditor(NullEditor.INSTANCE);
	}

	/**
	 * 创建一个实例，由此可以保证实例中的 formMap 必定有效
	 * 
	 * @param formMap
	 * @return
	 */
	public static EditEnvironment createInstance(IFormMap formMap) {
		if (formMap == null) {
			throw new IllegalArgumentException("formMap can not be null.");
		}

		return new EditEnvironment(formMap);
	}

	public void geometrySelectChanged(GeometrySelectChangedEvent arg0) {
		geometryStatusChange();
	}

	public void editableChanged(LayerEditableChangedEvent arg0) {
		geometryStatusChange();
		layersStatusChange();
	}

	public void layerRemoved(LayerRemovedEvent arg0) {
		geometryStatusChange();
		layersStatusChange();
	}

	/**
	 * 获取当前地图窗口中，所需要的状态数据
	 */
	private void geometryStatusChange() {
		try {
			// 选中对象数目
			resetGeometryStatus();

			Layers layers = this.formMap.getMapControl().getMap().getLayers();
			for (int i = 0; i < layers.getCount(); i++) {
				Layer layer = layers.get(i);

				if (layer.getDataset() == null) {
					continue;
				}

				if (layer.getSelection() == null || layer.getSelection().getCount() == 0) {
					continue;
				}

				if (!this.properties.getSelectedDatasetTypes().contains(layer.getDataset().getType())) {
					this.properties.getSelectedDatasetTypes().add(layer.getDataset().getType());
				}
				this.properties.getSelectedLayers().add(layer);
				statisticGeometryData(layer);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 统计指定图层 Geometry 的状态
	 * 
	 * @param layer
	 */
	private void statisticGeometryData(Layer layer) {
		Recordset recordset = null;

		try {
			ArrayList<Class<?>> features = new ArrayList<>();
			ArrayList<Class<?>> typeFeatures = new ArrayList<>();
			ArrayList<GeometryType> types = new ArrayList<>();
			int editableSelectedMaxPartCount = 0;

			Selection selection = layer.getSelection();
			recordset = selection.toRecordset();

			recordset.moveFirst();
			while (!recordset.isEOF()) {
				Geometry geometry = recordset.getGeometry();

				if (geometry == null) {
					recordset.moveNext();
				}

				try {
					GeometryType type = geometry.getType();
					if (!types.contains(type)) {
						types.add(type);
					}

					IGeometry dGeometry = DGeometryFactory.create(geometry);

					Class<?> typeFeature = getTypeFeature(dGeometry);
					if (!typeFeatures.contains(typeFeature)) {
						typeFeatures.add(typeFeature);
					}
					if (dGeometry instanceof IMultiPartFeature<?>) {
						editableSelectedMaxPartCount = Math.max(editableSelectedMaxPartCount, ((IMultiPartFeature<?>) dGeometry).getPartCount());
					}
				} finally {
					if (geometry != null) {
						geometry.dispose();
					}
				}
				recordset.moveNext();
			}

			this.properties.setSelectedGeometryCount(this.properties.getSelectedGeometryCount() + selection.getCount());
			ListUtilties.addArraySingle(this.properties.getSelectedGeometryTypeFeatures(), typeFeatures.toArray(new Class<?>[typeFeatures.size()]));
			ListUtilties.addArraySingle(this.properties.getSelectedGeometryTypes(), types.toArray(new GeometryType[types.size()]));
			if (layer.isEditable()) {
				this.properties.setEditableSelectedGeometryCount(this.properties.getEditableSelectedGeometryCount() + selection.getCount());
				this.properties.setEditableSelectedMaxPartCount(Math.max(this.properties.getEditableSelectedMaxPartCount(), editableSelectedMaxPartCount));
				ListUtilties.addArraySingle(this.properties.getEditableSelectedGeometryTypeFeatures(), typeFeatures.toArray(new Class<?>[typeFeatures.size()]));
				ListUtilties.addArraySingle(this.properties.getEditableSelectedGeometryTypes(), types.toArray(new GeometryType[types.size()]));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (recordset != null) {
				recordset.close();
				recordset.dispose();
			}
		}
	}

	private Class<?> getTypeFeature(IGeometry dGeometry) {
		if (dGeometry instanceof IPointFeature) {
			return IPointFeature.class;
		} else if (dGeometry instanceof ILineFeature) {
			return ILineFeature.class;
		} else if (dGeometry instanceof IRegionFeature) {
			return IRegionFeature.class;
		} else if (dGeometry instanceof ITextFeature) {
			return ITextFeature.class;
		} else if (dGeometry instanceof ICompoundFeature) {
			return ICompoundFeature.class;
		} else if (dGeometry instanceof ILine3DFeature) {
			return ILine3DFeature.class;
		} else if (dGeometry instanceof IRegion3DFeature) {
			return IRegion3DFeature.class;
		}
		return INormalFeature.class;
	}

	private void resetGeometryStatus() {
		// 选中对象数目
		this.properties.setSelectedGeometryCount(0);
		this.properties.setEditableSelectedGeometryCount(0);
		this.properties.setEditableSelectedMaxPartCount(0);
		this.properties.getSelectedDatasetTypes().clear();
		this.properties.getSelectedLayers().clear();
		this.properties.getSelectedGeometryTypes().clear();
		this.properties.getEditableSelectedGeometryTypes().clear();
		this.properties.getSelectedGeometryFeatures().clear();
		this.properties.getEditableSelectedGeometryFeatures().clear();
		this.properties.getSelectedGeometryTypeFeatures().clear();
		this.properties.getEditableSelectedGeometryTypeFeatures().clear();
	}

	private void layersStatusChange() {
		try {
			resetLayersStatus();

			for (int i = 0; i < this.formMap.getMapControl().getEditableLayers().length; i++) {
				Layer layer = this.formMap.getMapControl().getEditableLayers()[i];
				if (layer.getDataset() != null && !this.properties.getEditableDatasetTypes().contains(layer.getDataset().getType())) {
					this.properties.getEditableDatasetTypes().add(layer.getDataset().getType());
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void resetLayersStatus() {
		this.properties.getEditableDatasetTypes().clear();
	}

	/**
	 * 直接打开地图会导致 Layers 对象的改变，因此需要重新处理一下 Layers 相关事件的监听
	 *
	 */
	public void mapOpened(MapOpenedEvent arg0) {
		this.formMap.getMapControl().getMap().getLayers().addLayerEditableChangedListener(this.layerEditableChangedListener);
		this.formMap.getMapControl().getMap().getLayers().addLayerRemovedListener(this.layerRemovedListener);
	}

	public void mapClosed(MapClosedEvent arg0) {
		this.formMap.getMapControl().getMap().getLayers().removeLayerEditableChangedListener(this.layerEditableChangedListener);
		this.formMap.getMapControl().getMap().getLayers().removeLayerRemovedListener(this.layerRemovedListener);
	}
}
