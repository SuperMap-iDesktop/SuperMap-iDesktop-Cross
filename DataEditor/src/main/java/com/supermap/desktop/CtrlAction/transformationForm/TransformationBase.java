package com.supermap.desktop.CtrlAction.transformationForm;

import com.supermap.data.Dataset;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.event.ActiveLayersChangedListener;
import com.supermap.desktop.ui.LayersComponentManager;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.mapping.Layer;
import com.supermap.ui.MapControl;

/**
 * @author XiaJT
 */
public class TransformationBase implements IFormMap {

	@Override
	public String getText() {
		return getMapControl().getMap().getName();
	}


	@Override
	public WindowType getWindowType() {
		return WindowType.TRANSFORMATION;
	}

	@Override
	public void actived() {
		addListeners();
		LayersComponentManager layersComponentManager = UICommonToolkit.getLayersManager();
		layersComponentManager.setMap(getMapControl().getMap());
	}

	private void addListeners() {

	}

	@Override
	public void deactived() {
		removeListeners();
		LayersComponentManager layersComponentManager = UICommonToolkit.getLayersManager();
		layersComponentManager.setMap(null);
	}

	private void removeListeners() {

	}

	@Override
	public void windowShown() {

	}

	@Override
	public void windowHidden() {

	}

	@Override
	public void clean() {

	}

	@Override
	public boolean isClosed() {
		return false;
	}

	@Override
	public MapControl getMapControl() {
		return null;
	}

	@Override
	public Layer[] getActiveLayers() {
		return new Layer[0];
	}

	@Override
	public void setActiveLayers(Layer... activeLayers) {

	}

	@Override
	public void addActiveLayersChangedListener(ActiveLayersChangedListener listener) {

	}

	@Override
	public void removeActiveLayersChangedListener(ActiveLayersChangedListener listener) {

	}

	@Override
	public void removeActiveLayersByDatasets(Dataset... datasets) {

	}

	@Override
	public void dontShowPopupMenu() {

	}

	@Override
	public void showPopupMenu() {

	}

	@Override
	public int getIsShowPopupMenu() {
		return 0;
	}

	@Override
	public void updataSelectNumber() {

	}

	@Override
	public void setSelectedGeometryProperty() {

	}

	@Override
	public void openMap(String mapName) {

	}

	@Override
	public int getSelectedCount() {
		return 0;
	}

	@Override
	public boolean save() {
		return false;
	}

	@Override
	public void setText(String text) {

	}

	@Override
	public boolean save(boolean notify, boolean isNewWindow) {
		return false;
	}

	@Override
	public boolean saveFormInfos() {
		return false;
	}

	@Override
	public boolean saveAs(boolean isNewWindow) {
		return false;
	}

	@Override
	public boolean isNeedSave() {
		return false;
	}

	@Override
	public void setNeedSave(boolean needSave) {

	}

	@Override
	public boolean isActivated() {
		return false;
	}
}
