package com.supermap.desktop.GeometryPropertyBindWindow;

import java.awt.event.*;
import java.util.Vector;

import javax.swing.JTable;

import com.supermap.data.*;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.mapping.*;

public class BindWindow implements IBindWindow {
	private IFormTabular formTabular;

	private boolean selectionHasChanged;

	private Dataset dataset;
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
		this.formTabular.getjTableTabular().addKeyListener(this.tabularTableKeyListener);
		this.formTabular.getRowHeader().addMouseMotionListener(this.listMouseMotionListener);
		this.formTabular.getRowHeader().addMouseListener(this.listMouseListener);
	}

	public void queryMap(JTable tabularTable) {
		int[] selectRows = tabularTable.getSelectedRows();
		int[] idRows = new int[selectRows.length];
		for (int i = 0; i < selectRows.length; i++) {
			idRows[i] = selectRows[i] + 1;
		}
		if (null != this.dataset) {
			DatasetVector datasetVector = (DatasetVector) this.dataset;
			Recordset maxIdSet = datasetVector.query("SmId", CursorType.STATIC);
			int idCount = (int) maxIdSet.statistic("SmId", StatisticMode.MAX);
			if (!BindUtilties.isRightRows(idRows, idCount)) {
				return;
			}
			Recordset recordset = datasetVector.query(idRows, CursorType.STATIC);
			Selection selection = new Selection();
			selection.fromRecordset(recordset);
			if (null != selection) {
				fireSelectionChanged(selection);
			}
			selection.clear();
			recordset.dispose();
		}
	}

	@Override
	public void refreshFormTabular(int[] addRows) {
		if (BindUtilties.isRightRows(addRows, formTabular.getjTableTabular().getRowCount()) && dataset.equals(formTabular.getRecordset().getDataset())) {
			this.formTabular.addRow(addRows);
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
		this.dataset = null;
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
	public void fireSelectionChanged(Selection selection) {
		if (null != mapSelectionChangeListeners) {
			Vector<MapSelectionChangeListener> listeners = mapSelectionChangeListeners;
			for (int i = 0; i < listeners.size(); i++) {
				listeners.elementAt(i).selectionChanged(selection);
			}
		}
	}

	@Override
	public void setActiveDataset(Dataset dataset) {
		this.dataset = dataset;
	}

}
