package com.supermap.desktop.CtrlAction.LayerSetting;

import java.awt.Rectangle;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetImage;
import com.supermap.data.DatasetType;
import com.supermap.data.Point2D;
import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerGroup;

public class CtrlActionLayerRasterResolution extends CtrlAction {

	public CtrlActionLayerRasterResolution(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();

			Layer rasterLayer = formMap.getActiveLayers()[0];
			Rectangle2D layerBounds = rasterLayer.getBounds();

			Dataset dataset = rasterLayer.getDataset();
			int datasetWidth = 0;
			int datasetHeight = 0;
			if (dataset instanceof DatasetImage) {
				datasetWidth = ((DatasetImage) dataset).getWidth();
				datasetHeight = ((DatasetImage) dataset).getHeight();
			} else if (dataset instanceof DatasetGrid) {
				datasetWidth = ((DatasetGrid) dataset).getWidth();
				datasetHeight = ((DatasetGrid) dataset).getHeight();
			} else { // 出问题了

			}

			if (datasetWidth != 0 && datasetHeight != 0) {
				Double resolutionX = layerBounds.getWidth() / datasetWidth;
				Double resolutionY = layerBounds.getHeight() / datasetHeight;

				Rectangle mapControlBounds = formMap.getMapControl().getBounds();
				Rectangle2D newViewBounds = formMap.getMapControl().getMap().getViewBounds();
				Point2D mapCenter = newViewBounds.getCenter();

				newViewBounds.setLeft(mapCenter.getX() - resolutionX * mapControlBounds.getWidth() * 0.5);
				newViewBounds.setTop(mapCenter.getY() + resolutionY * mapControlBounds.getHeight() * 0.5);
				newViewBounds.setRight(mapCenter.getX() + resolutionX * mapControlBounds.getWidth() * 0.5);
				newViewBounds.setBottom(mapCenter.getY() - resolutionY * mapControlBounds.getHeight() * 0.5);

				formMap.getMapControl().getMap().setViewBounds(newViewBounds);
				formMap.getMapControl().getMap().refresh();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		boolean enable = false;
		IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
		if (formMap.getActiveLayers().length == 1
				&& formMap.getActiveLayers()[0].getDataset() != null
				&& (formMap.getActiveLayers()[0].getDataset().getType() == DatasetType.IMAGE || formMap.getActiveLayers()[0].getDataset().getType() == DatasetType.GRID)) {
			enable = true;
		}
		return enable;
	}
	
}
