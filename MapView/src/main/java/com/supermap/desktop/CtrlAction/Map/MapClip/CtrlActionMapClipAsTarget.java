package com.supermap.desktop.CtrlAction.Map.MapClip;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.core.recordset.RecordsetSet;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.ILineFeature;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.mapview.map.propertycontrols.MapActionSelectTargetInfoPanel;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Layers;
import com.supermap.ui.Action;
import com.supermap.ui.MapControl;
import com.supermap.ui.TrackMode;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author YuanR
 *         2017.3.21
 *         根据选中的对象进行地图裁剪
 */
public class CtrlActionMapClipAsTarget extends CtrlAction {
	public CtrlActionMapClipAsTarget(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	private MapActionSelectTargetInfoPanel panelSelectTargetInfo = new MapActionSelectTargetInfoPanel();
	private transient GeoRegion geoRegion;
	private static final int SEGMENTCOUNT = 50;

	@Override
	public void run() {
		super.run();
		MapControl mapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
		boolean hasSelection = false;
		ArrayList<Layer> layers = MapUtilities.getLayers(mapControl.getMap());
		for (Layer layer : layers) {
			if (layer.getSelection() != null && layer.getSelection().getCount() > 0 && layer.getDataset() != null && layer.getDataset().getType() == DatasetType.REGION) {
				hasSelection = true;
				break;
			}
		}
		if (hasSelection) {
			abstractActiveMapcontrol(((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl());
		} else {
			setAction();
		}
	}

	@Override
	public boolean enable() {
		boolean result = true;
		MapControl activeMapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
		ArrayList<Layer> arrayList;
		arrayList = MapUtilities.getLayers(activeMapControl.getMap(), true);
		//是否存在图层
		if (arrayList.size() > 0) {
			HashMap<Dataset, Layer> layerMap = new HashMap<>();
			for (int i = 0; i < arrayList.size(); i++) {
				if (arrayList.get(i).getDataset() != null) {
					result = true;
					break;
				}
			}
		}

		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		ArrayList<Datasource> isCanUseDatasources = new ArrayList<>();
		for (int i = 0; i < datasources.getCount(); i++) {
			if (!datasources.get(i).isReadOnly()) {
				isCanUseDatasources.add(datasources.get(i));
			}
		}
		if (isCanUseDatasources == null || isCanUseDatasources.size() == 0) {
			result = false;
		}
		return result;
	}


	/**
	 * 设置Action事件
	 */
	private void setAction() {
		final IFormMap activeForm = (IFormMap) Application.getActiveApplication().getActiveForm();
		final MapControl activeMapControl = activeForm.getMapControl();
		activeMapControl.setAction(Action.SELECT);
		activeMapControl.setLayout(null);
		activeMapControl.add(panelSelectTargetInfo);

		final MouseMotionListener mouseMotionListener = new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Point point = e.getPoint();
				panelSelectTargetInfo.setLocation(point.x + 15, point.y);
				panelSelectTargetInfo.setVisible(true);
				panelSelectTargetInfo.updateUI();
			}
		};
		activeMapControl.addMouseMotionListener(mouseMotionListener);
		//选择时不弹出右键菜单
		activeForm.dontShowPopupMenu();
		activeMapControl.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					activeForm.showPopupMenu();
					activeMapControl.removeMouseMotionListener(mouseMotionListener);
					panelSelectTargetInfo.setVisible(false);
					abstractActiveMapcontrol(activeMapControl);
					exitEdit();
					activeForm.getMapControl().removeMouseListener(this);
					if (activeForm instanceof FormMap) {
						((FormMap) Application.getActiveApplication().getActiveForm()).clearSelection();
					}
				}
			}
		});

	}

	private void exitEdit() {
		MapControl activeMapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
		activeMapControl.setAction(Action.SELECT2);
		activeMapControl.setTrackMode(TrackMode.EDIT);
	}

	private void abstractActiveMapcontrol(final MapControl activeMapControl) {
		boolean isChanged = false;
		boolean isMutiObjectClip = true;
		int layerChangeID = -1; // 从一个图层选择对象的图层id
		GeoRegion geoClipRegion = new GeoRegion();
		ArrayList<Layer> layers = MapUtilities.getLayers(activeMapControl.getMap(), true);
		ArrayList<GeoRegion> selectedGeoregions = new ArrayList<>();
		for (int i = 0; i < layers.size(); i++) {
			Layer layer = layers.get(i);
			if (layer.getDataset() == null || !layer.isSelectable()) {
				continue;
			}
			Recordset recordset = layer.getSelection().toRecordset();
			for (int k = 0; k < recordset.getRecordCount(); k++, recordset.moveNext()) {
				Geometry geometry = recordset.getGeometry();
				GeoRegion geoRegionTemp = null;

				if (layer.getDataset().getType() == DatasetType.CAD) {
					Geometry oldGeo = geometry;
					IGeometry dGeometry = DGeometryFactory.create(geometry);
					if (dGeometry instanceof ILineFeature) {
						geometry = ((ILineFeature) dGeometry).convertToLine(120);
					} else if (dGeometry instanceof IRegionFeature) {
						geometry = ((IRegionFeature) dGeometry).convertToRegion(120);
					} else {
						geometry = null;
					}
					if (geometry != null) {
						if (geometry != oldGeo) {
							oldGeo.dispose();// 转换前的几何对象应该及时释放掉。
						}
					}
				}

				if (geometry instanceof GeoPie) { // 扇面几何对象类
					// 将扇面几何对象转换为面几何对象
					//参数为：等分扇面几何对象对应的椭圆弧的段数
					geoRegionTemp = ((GeoPie) geometry).convertToRegion(SEGMENTCOUNT);
				} else if (geometry instanceof GeoRegion) { // 面几何对象类
					geoRegionTemp = (GeoRegion) geometry;
				} else if (geometry instanceof GeoEllipse) { // 椭圆几何对象类
					// 将椭圆几何对象转换为面几何对象。
					// 参数为：等分椭圆几何对象的段数
					geoRegionTemp = ((GeoEllipse) geometry).convertToRegion(SEGMENTCOUNT);
				} else if (geometry instanceof GeoCompound) { // 复合几何对象类

					geoRegionTemp = getCompoundGeoRegion((GeoCompound) geometry);
				}
				if (geoRegionTemp != null && geoRegionTemp.getPartCount() > 0) {
					// 判断选择的对象是否跨图层
					if (isChanged) {
						if (layerChangeID != i && layerChangeID != -1) {
							isMutiObjectClip = false;
						}
					} else {
						layerChangeID = i;
					}

					selectedGeoregions.add(geoRegionTemp.clone());

					for (int j = 0; j < geoRegionTemp.getPartCount(); j++) {
						geoClipRegion.addPart(geoRegionTemp.getPart(j).clone());
						isChanged = true;
					}
				}
			}
			recordset.dispose();
		}
		if (isChanged) {
			geoRegion = geoClipRegion;
			// 当获得GeoRegion后，弹出地图裁剪对话框
			if (!isMutiObjectClip || geoRegion.getPartCount() <= 1) {  //选择的对象如果跨图层或者选择对象的个数小于1，则不支持多对象拆分裁剪操作
				DialogMapClip dialogMapClip = new DialogMapClip(geoRegion);
				dialogMapClip.showDialog();
			} else {
				Layer layer = layers.get(layerChangeID);
				Recordset recordset = layer.getSelection().toRecordset();
				FieldInfos fieldInfos = recordset.getFieldInfos();
				String fieldCaptions[] = new String[fieldInfos.getCount()];
				for (int i = 0; i < fieldInfos.getCount(); i++) {
					fieldCaptions[i] = fieldInfos.get(i).getCaption();
				}
				DialogMapClip dialogMapClip = new DialogMapClip(geoRegion, true, fieldCaptions, recordset,selectedGeoregions);
				dialogMapClip.showDialog();
			}
		}
	}

	/**
	 * 得到复合对象的面对象
	 */
	private GeoRegion getCompoundGeoRegion(GeoCompound geoCompound) {
		GeoRegion geoRegionResult = new GeoRegion();
		GeoRegion geoRegionTemp = null;
		for (int i = 0; i < geoCompound.getPartCount(); i++) {
			Geometry geometry = geoCompound.getPart(i);
			if (geometry instanceof GeoPie) {
				geoRegionTemp = ((GeoPie) geometry).convertToRegion(SEGMENTCOUNT);
			} else if (geometry instanceof GeoRegion) {
				geoRegionTemp = (GeoRegion) geometry;
			} else if (geometry instanceof GeoEllipse) {
				geoRegionTemp = ((GeoEllipse) geometry).convertToRegion(SEGMENTCOUNT);
			}
		}
		if (geoRegionTemp != null && geoRegionTemp.getPartCount() > 0) {
			for (int j = 0; j < geoRegionTemp.getPartCount(); j++) {
				geoRegionResult.addPart(geoRegionTemp.getPart(j).clone());
			}
		}
		return geoRegionResult;
	}
}
