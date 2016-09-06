package com.supermap.desktop.ui.controls.datasetChoose;

import com.supermap.data.Maps;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.ui.controls.CellRenders.TableMapCellRender;
import com.supermap.mapping.Map;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * DatasetChooser可选模块，不建议直接新建
 *
 * @author XiaJT
 */
public class DatasetChooserMap implements IDatasetChoose {
	private DatasetChooser datasetChooser;
	private MapTableModel tableModel;
	private TableMapCellRender tableMapCellRender;


	public DatasetChooserMap(DatasetChooser datasetChooser) {
		this.datasetChooser = datasetChooser;
		tableModel = new MapTableModel();
	}

	@Override
	public void initTable() {
		JTable table = datasetChooser.getTable();
		table.setModel(tableModel);
		table.getColumnModel().getColumn(0).setCellRenderer(getTableMapCellRenderer());
	}

	private TableMapCellRender getTableMapCellRenderer() {
		if (tableMapCellRender == null) {
			tableMapCellRender = new TableMapCellRender();
		}
		return tableMapCellRender;
	}

	@Override
	public void initializeTableInfo(Object currentNode) {
		if (currentNode != null && currentNode instanceof Maps) {
			tableModel.removeAll();
			Maps maps = (Maps) currentNode;
			for (int i = 0; i < maps.getCount(); i++) {
				if (isAllowedMap(maps.get(i))) {
					tableModel.addMap(maps.get(i));
				}
			}
		}
	}

	private boolean isAllowedMap(String s) {
		return isAllowedMapName(s) && datasetChooser.isAllowedSearchName(s);
	}

	protected boolean isAllowedMapName(String s) {
		return true;
	}


	@Override
	public List<Object> getSelectedValues(int[] selectedRows) {
		return tableModel.getSelectedMap(selectedRows);
	}

	private class MapTableModel extends DefaultTableModel {

		List<Map> dataList = new ArrayList<>();
		private HashMap<String, Map> cache = new HashMap<>();

		private void addMap(String s) {
			Map map = cache.get(s);
			if (map == null) {
				map = new Map(Application.getActiveApplication().getWorkspace());
				map.open(s);
				cache.put(s, map);
			}
			dataList.add(map);
			fireTableRowsInserted(dataList.size() - 1, dataList.size() - 1);
		}

		@Override
		public int getRowCount() {
			if (dataList == null) {
				return 0;
			}
			return dataList.size();
		}

		@Override
		public int getColumnCount() {
			return 1;
		}

		@Override
		public String getColumnName(int column) {
			return ControlsProperties.getString("String_mapName");
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

		@Override
		public Object getValueAt(int row, int column) {
			return dataList.get(row);
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return Map.class;
		}

		public void removeAll() {
			int size = dataList.size();
			if (size > 0) {

				dataList.clear();
				fireTableRowsDeleted(0, size - 1);
			}
		}

		public List<Object> getSelectedMap(int[] selectedRows) {
			ArrayList<Object> objects = new ArrayList<>();
			for (int selectedRow : selectedRows) {
				Map selectedMap = dataList.get(selectedRow);
				Map map = new Map(selectedMap.getWorkspace());
				map.open(selectedMap.getName());
				objects.add(map);
			}
			return objects;
		}

		public void dispose() {
			for (int i = dataList.size() - 1; i >= 0; i--) {
				dataList.get(i).close();
			}
			removeAll();
		}
	}

	@Override
	public void dispose() {
		tableModel.dispose();
	}
}
