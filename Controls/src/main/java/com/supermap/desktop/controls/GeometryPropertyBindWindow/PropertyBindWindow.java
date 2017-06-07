package com.supermap.desktop.controls.GeometryPropertyBindWindow;

import com.supermap.data.Dataset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Selection;

public class PropertyBindWindow implements IPropertyBindWindow {

	private IBindWindow bindWindow;
	private IBindProperty bindProperty;
	private IFormMap formMap;
	// 是否要选中属性表中的某些行
	private PropertySelectChangeListener selectRowsChangeListener;
	private MapSelectionChangeListener selectionChangeListener;

	public PropertyBindWindow() {
		// 只需要初始化一次
		this.selectRowsChangeListener = new PropertySelectChangeListener() {

			@Override
			public void selectChanged(int[] selectRows, Dataset dataset) {
				if (null != dataset) {
					if (bindWindow.getActiveLayers() != null) {
						for (int i = 0; i < bindWindow.getActiveLayers().length; i++) {
							if (!bindWindow.getActiveLayers()[i].isDisposed() && dataset.equals(bindWindow.getActiveLayers()[i].getDataset())) {
								bindWindow.refreshFormTabular(selectRows);
							}
						}

					}
				} else if (null == dataset) {
					bindWindow.refreshFormTabular(selectRows);
				}
			}
		};
		this.selectionChangeListener = new MapSelectionChangeListener() {

			@Override
			public void selectionChanged(Selection selection, Layer layer) {
				bindProperty.refreshMap(selection, layer);
			}
		};
	}

	@Override
	public void registEvents() {
		removeEvents();
		bindProperty.addPropertySelectChangeListener(selectRowsChangeListener);
		bindWindow.addMapSelectionChangeListener(selectionChangeListener);
	}

	@Override
	public void removeEvents() {
		if (null != formMap.getMapControl()) {
			bindProperty.removePropertySelectChangeListener(selectRowsChangeListener);
			bindWindow.removeMapSelectionChangeListener(selectionChangeListener);
		}
	}

	@Override
	public IBindWindow getBindWindow() {
		return bindWindow;
	}

	@Override
	public void setBindWindow(IBindWindow bindWindow, Layer... layers) {
		this.bindWindow = bindWindow;
		this.bindWindow.setActiveLayers(layers);
	}

	@Override
	public IBindProperty getBindProperty() {
		return bindProperty;
	}

	@Override
	public void setBindProperty(IBindProperty bindProperty) {
		this.bindProperty = bindProperty;
	}

	public IFormMap getFormMap() {
		return formMap;
	}

	public void setFormMap(IFormMap formMap) {
		this.formMap = formMap;
	}

}
