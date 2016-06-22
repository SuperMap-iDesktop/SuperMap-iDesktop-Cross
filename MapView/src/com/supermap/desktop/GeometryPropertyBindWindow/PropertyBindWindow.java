package com.supermap.desktop.GeometryPropertyBindWindow;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.supermap.data.Dataset;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.ui.docking.SplitWindow;
import com.supermap.mapping.Selection;

public class PropertyBindWindow implements IPropertyBindWindow {

	private IBindWindow bindWindow;
	private IBindProperty bindProperty;
	private IFormMap formMap;
	private SplitWindow splitWindow;
	// 是否要选中属性表中的某些行
	private boolean addRow;
	private PropertySelectChangeListener selectRowsChangeListener;
	private MapSelectionChangeListener selectionChangeListener;
	private MouseAdapter formMapMouseListener;

	public PropertyBindWindow() {

	}

	@Override
	public void registEvents() {
		this.selectRowsChangeListener = new PropertySelectChangeListener() {

			@Override
			public void selectChanged(int[] selectRows, Dataset dataset) {
				if (!addRow && dataset.equals(bindWindow.getActiveDataset())) {
					bindWindow.refreshFormTabular(selectRows);
				}
			}
		};
		this.selectionChangeListener = new MapSelectionChangeListener() {

			@Override
			public void selectionChanged(Selection selection) {
				addRow = true;
				bindProperty.refreshMap(selection);
			}
		};
		this.formMapMouseListener = new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) {
					bindWindow.refreshFormTabular(new int[0]);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				addRow = false;
			}

		};
		removeEvents();
		formMap.getMapControl().addMouseListener(formMapMouseListener);
		bindProperty.addPropertySelectChangeListener(selectRowsChangeListener);
		bindWindow.addMapSelectionChangeListener(selectionChangeListener);
	}

	@Override
	public void removeEvents() {
		if (null != formMap.getMapControl()) {
			formMap.getMapControl().removeMouseListener(formMapMouseListener);
			bindProperty.removePropertySelectChangeListener(selectRowsChangeListener);
			bindWindow.removeMapSelectionChangeListener(selectionChangeListener);
		}
	}

	@Override
	public IBindWindow getBindWindow() {
		return bindWindow;
	}

	@Override
	public void setBindWindow(IBindWindow bindWindow, Dataset dataset) {
		this.bindWindow = bindWindow;
		this.bindWindow.setActiveDataset(dataset);
	}

	@Override
	public IBindProperty getBindProperty() {
		return bindProperty;
	}

	@Override
	public void setBindProperty(IBindProperty bindProperty) {
		this.bindProperty = bindProperty;
	}

	public SplitWindow getSplitWindow() {
		return splitWindow;
	}

	public void setSplitWindow(SplitWindow splitWindow) {
		this.splitWindow = splitWindow;
	}

	public IFormMap getFormMap() {
		return formMap;
	}

	public void setFormMap(IFormMap formMap) {
		this.formMap = formMap;
	}

	public boolean isAddRow() {
		return addRow;
	}

	public void setAddRow(boolean addRow) {
		this.addRow = addRow;
	}

}
