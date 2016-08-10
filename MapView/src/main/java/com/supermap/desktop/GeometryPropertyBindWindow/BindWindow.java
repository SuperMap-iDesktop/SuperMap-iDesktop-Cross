package com.supermap.desktop.GeometryPropertyBindWindow;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Recordset;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Selection;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class BindWindow implements IBindWindow {
	private IFormTabular formTabular;

	private boolean selectionHasChanged;

	private Layer layer;
	private MouseAdapter tabularTableListener;
	private KeyAdapter tabularTableKeyListener;
	private MouseMotionAdapter listMouseMotionListener;
	private MouseListener listMouseListener;

	private Vector<MapSelectionChangeListener> mapSelectionChangeListeners;

	public BindWindow(IFormTabular formTabular) {
		this.formTabular = formTabular;
		registEvents();
	}

	private void registEvents() {
		this.tabularTableListener = new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				queryMap(formTabular.getjTableTabular());
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
			idRows[i] = (int) this.formTabular.getRowIndexMap().get(selectRows[i]);
		}
		if (!layer.isDiposed() && null != this.layer.getDataset()) {
			DatasetVector datasetVector = (DatasetVector) this.layer.getDataset();
			Recordset recordset = datasetVector.query(idRows, CursorType.STATIC);
			Selection selection = new Selection();
			selection.fromRecordset(recordset);
			if (null != selection) {
				fireSelectionChanged(selection, layer);
			}
			selection.clear();
			recordset.dispose();
		}
	}

	@Override
	public void refreshFormTabular(int[] addRows) {
		List<Integer> tempRows = new ArrayList<Integer>();
		for (int i = 0; i < addRows.length; i++) {
			if (null != this.formTabular.getIdMap().get(addRows[i])) {
				tempRows.add(this.formTabular.getIdMap().get(addRows[i]));
			}
		}
		if (this.formTabular.getRowCount() > 0) {
			this.formTabular.addRows(tempRows);
		}
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
		this.layer = null;
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
	}

	@Override
	public synchronized void removeMapSelectionChangeListener(MapSelectionChangeListener l) {
		if (null != mapSelectionChangeListeners && mapSelectionChangeListeners.contains(l)) {
			mapSelectionChangeListeners.remove(l);
		}
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
	public void setActiveLayer(Layer layer) {
		this.layer = layer;
	}

	@Override
	public IFormTabular getTabular() {
		return this.formTabular;
	}

	@Override
	public Layer getActiveLayer() {
		return this.layer;
	}

}
