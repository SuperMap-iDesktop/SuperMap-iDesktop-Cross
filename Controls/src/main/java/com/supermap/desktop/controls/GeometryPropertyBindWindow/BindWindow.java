package com.supermap.desktop.controls.GeometryPropertyBindWindow;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Selection;

import javax.swing.*;
import java.awt.event.*;
import java.util.Vector;

public class BindWindow implements IBindWindow {
	private IFormTabular formTabular;

	private boolean selectionHasChanged = false;

	private Layer[] layers;
	private MouseAdapter tabularTableListener;
	private KeyAdapter tabularTableKeyListener;
	private MouseMotionAdapter listMouseMotionListener;
	private MouseListener listMouseListener;

	private Vector<MapSelectionChangeListener> mapSelectionChangeListeners;

	public BindWindow(IFormTabular formTabular) {
		this.formTabular = formTabular;
	}

	private void registEvents() {
		this.tabularTableListener = new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getSource() == formTabular.getjTableTabular().getTableHeader()) {
					// do nothing
//					if (GlobalParameters.isHeadClickedSelectedColumn()) {
//						queryMap(formTabular.getjTableTabular());
//					}
				} else {
					queryMap(formTabular.getjTableTabular());
				}
			}
		};
		this.tabularTableKeyListener = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				queryMap(formTabular.getjTableTabular());
			}
		};
		this.listMouseMotionListener = new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				queryMap(formTabular.getjTableTabular());
			}
		};
		this.listMouseListener = new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				queryMap(formTabular.getjTableTabular());
			}
		};
		removeEvents();
		this.formTabular.getjTableTabular().addMouseListener(this.tabularTableListener);
		this.formTabular.getjTableTabular().getTableHeader().addMouseListener(this.tabularTableListener);
		this.formTabular.getjTableTabular().addKeyListener(this.tabularTableKeyListener);
		this.formTabular.getRowHeader().addMouseMotionListener(this.listMouseMotionListener);
		this.formTabular.getRowHeader().addMouseListener(this.listMouseListener);
	}

	public void queryMap(JTable tabularTable) {
		int[] selectRows = tabularTable.getSelectedRows();
		int[] idRows = new int[selectRows.length];
		for (int i = 0; i < selectRows.length; i++) {
			idRows[i] = this.formTabular.getSmId(selectRows[i]);
		}
		for (int i = 0; i < layers.length; i++) {
			if (!layers[i].isDisposed() && null != this.layers[i].getDataset()) {
				DatasetVector datasetVector = (DatasetVector) this.layers[i].getDataset();
				Recordset recordset = datasetVector.query(idRows, CursorType.STATIC);
				Selection selection = new Selection();
				selection.fromRecordset(recordset);
				fireSelectionChanged(selection, layers[i]);
				selection.clear();
				recordset.dispose();
			}
		}
	}

	@Override
	public void refreshFormTabular(int[] addRows) {
		IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
		for (int i = 0; i < formManager.getCount(); i++) {
			if (this.formTabular.hashCode() == formManager.get(i).hashCode()) {
				this.formTabular = (IFormTabular) formManager.get(i);
			}
		}
		int[] tempRows = new int[addRows.length];
		for (int i = 0; i < addRows.length; i++) {
			tempRows[i] = getRowBySmId(addRows[i]);
		}
		if (this.formTabular.getRowCount() > 0) {
			this.formTabular.addRows(tempRows);
		}
	}

	private int getRowBySmId(int addRow) {
		int selectRow = -1;
		int rowCount = this.formTabular.getjTableTabular().getRowCount();
		for (int i = 0; i < rowCount; i++) {
			if (addRow == this.formTabular.getjTableTabular().getValueAt(i, 0)) {
				selectRow = i;
			}
		}

		return selectRow;
	}


	public boolean isSelectionHasChanged() {
		return selectionHasChanged;
	}

	@Override
	public void removeEvents() {
		this.formTabular.getjTableTabular().removeMouseListener(this.tabularTableListener);
		this.formTabular.getjTableTabular().removeKeyListener(this.tabularTableKeyListener);
		this.formTabular.getRowHeader().removeMouseMotionListener(this.listMouseMotionListener);
		this.formTabular.getRowHeader().removeMouseListener(this.listMouseListener);
	}

	@Override
	public void dispose() {
		removeEvents();
		this.layers = null;
		this.formTabular = null;
		this.listMouseListener = null;
		this.listMouseMotionListener = null;
		this.tabularTableKeyListener = null;
		this.tabularTableListener = null;
		this.mapSelectionChangeListeners.removeAllElements();
		this.mapSelectionChangeListeners = null;
	}

	@Override
	public synchronized void addMapSelectionChangeListener(MapSelectionChangeListener l) {
		if (null == mapSelectionChangeListeners) {
			mapSelectionChangeListeners = new Vector<MapSelectionChangeListener>();
		}
		if (!mapSelectionChangeListeners.contains(l)) {
			mapSelectionChangeListeners.add(l);
		}
		registEvents();
	}

	@Override
	public synchronized void removeMapSelectionChangeListener(MapSelectionChangeListener l) {
		if (null != mapSelectionChangeListeners && mapSelectionChangeListeners.contains(l)) {
			mapSelectionChangeListeners.remove(l);
		}
		removeEvents();
	}

	@Override
	public void fireSelectionChanged(Selection selection, Layer layer) {
		if (null != mapSelectionChangeListeners) {
			Vector<MapSelectionChangeListener> listeners = mapSelectionChangeListeners;
			for (int i = 0; i < listeners.size(); i++) {
				listeners.elementAt(i).selectionChanged(selection, layer);
			}
		}
	}

	@Override
	public void setActiveLayers(Layer... layers) {
		this.layers = layers;
	}

	@Override
	public IFormTabular getTabular() {
		return this.formTabular;
	}

	@Override
	public Layer[] getActiveLayers() {
		return this.layers;
	}

}
