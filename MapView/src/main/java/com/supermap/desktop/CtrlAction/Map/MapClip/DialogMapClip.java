package com.supermap.desktop.CtrlAction.Map.MapClip;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.ComponentBorderPanel.CompTitledPane;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.borderPanel.PanelButton;
import com.supermap.desktop.ui.controls.progress.FormProgressTotal;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.TableUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import static com.supermap.desktop.CtrlAction.Map.MapClip.MapClipTableModel.*;

/**
 * @author YuanR
 *         2017.3.21
 *         地图裁剪主窗体
 */
public class DialogMapClip extends SmDialog {
	private GeoRegion geoRegion;

	private JToolBar toolBar;
	private JButton buttonAddLayers;
	private JButton buttonSelectAll;
	private JButton buttonInvertSelect;
	private JButton buttonSet;
	private JButton buttonDelete;
	private MapClipSetDialog mapClipSetDialog;
	private MapClipAddLayersDialog mapClipAddLayersDialog;
	private JScrollPane scrollPane;
	private MapClipJTable mapClipJTable;

	private MapClipSaveMapPanel mapClipSaveMapPanel;
	private CompTitledPane saveMapcompTitledPane;

	private PanelButton panelButton;

	private Vector layerJTableInfo; //  从JTable中拿到的vector
	private Vector resultInfo;       //  处理后的vector
	private Vector deletedInfo;
	private Vector selectedDeletedInfo;

	private String saveMapName;
	private Map resultMap = null;
	private boolean isSaveMapNameIsValid = true;
	private boolean isDatasetNameIsValid = true;

	private MapClipProgressCallable mapClipProgressCallable;

	public static final int COLUMN_INDEX_SOURCEDATASET = 0;
	public static final int COLUMN_INDEX_USERREGION = 1;
	public static final int COLUMN_INDEX_ISCLIPINREGION = 2;
	public static final int COLUMN_INDEX_ISEXACTCLIPorISERASESOURCE = 3;
	public static final int COLUMN_INDEX_TARGETDATASETSOURCE = 4;
	public static final int COLUMN_INDEX_TARGETDATASETNAME = 5;
	public static final int COLUMN_LAYER = 6;

	/**
	 * 对JTable中model数据进行处理,得到适用于裁剪接口的数据
	 *
	 * @return
	 */
	private void initResultVectorInfo() {
		this.layerJTableInfo = this.mapClipJTable.getMapClipTableModel().getDataVector();
		if (this.layerJTableInfo != null) {
			try {
				Vector tempVector = new Vector();
				for (int i = 0; i < this.layerJTableInfo.size(); i++) {
					Dataset targetDataset;
					GeoRegion userRegion;
					boolean isClipInRegion = false;
					boolean isExactClipOrIsEarsesource = false;
					Datasource targetDatasource;
					String targetDatasetName;
					// 获得源数据集
					Layer layer = (Layer) ((Vector) (this.layerJTableInfo.get(i))).get(COLUMN_INDEX_LAYERCAPTION);
					targetDataset = layer.getDataset();
					// 获得裁剪范围
					userRegion = this.geoRegion;
					// 获得是否区域内裁剪
					String clipType = (String) ((Vector) (this.layerJTableInfo.get(i))).get(COLUMN_INDEX_CLIPTYPE);
					if (clipType.equals(MapViewProperties.getString("String_MapClip_In"))) {
						isClipInRegion = true;
					}
					// 获得是否擦除或者精确裁剪，需要根据数据集类型分别赋值
					if (targetDataset instanceof DatasetVector) {
						String isErase = (String) ((Vector) (this.layerJTableInfo.get(i))).get(COLUMN_INDEX_ERASE);
						if (isErase.equals(MapViewProperties.getString("String_MapClip_Yes"))) {
							isExactClipOrIsEarsesource = true;
						}
					} else {
						String isExactClip = (String) ((Vector) (this.layerJTableInfo.get(i))).get(COLUMN_INDEX_EXACTCLIP);
						if (isExactClip.equals(MapViewProperties.getString("String_MapClip_Yes"))) {
							isExactClipOrIsEarsesource = true;
						}
//                        isExactClipOrIsEarsesource = (Boolean) ((Vector) (this.layerJTableInfo.get(i))).get(COLUMN_INDEX_EXACTCLIP);
					}
					// 获得目标数据源
					targetDatasource = (Datasource) ((Vector) (this.layerJTableInfo.get(i))).get(COLUMN_INDEX_AIMDATASOURCE);
					//获得数据集名称，此时获得目标数据集名称时
					targetDatasetName = (String) ((Vector) (this.layerJTableInfo.get(i))).get(COLUMN_INDEX_AIMDATASET);


					// 将处理后的数据添加到新的Vector
					Vector v = new Vector(7);
					v.add(COLUMN_INDEX_SOURCEDATASET, targetDataset);
					v.add(COLUMN_INDEX_USERREGION, userRegion);
					v.add(COLUMN_INDEX_ISCLIPINREGION, isClipInRegion);
					v.add(COLUMN_INDEX_ISEXACTCLIPorISERASESOURCE, isExactClipOrIsEarsesource);
					v.add(COLUMN_INDEX_TARGETDATASETSOURCE, targetDatasource);
					v.add(COLUMN_INDEX_TARGETDATASETNAME, targetDatasetName);
					v.add(COLUMN_LAYER, layer);
					tempVector.add(v);
				}
				this.resultInfo = tempVector;
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
				this.resultInfo = null;
			}
		} else {
			this.resultInfo = null;
		}
		if (this.deletedInfo != null && this.deletedInfo.size() >= 1 && this.resultMap != null) {
			for (int i = 0; i < deletedInfo.size(); i++) {
				Layer layer = (Layer) ((Vector) (this.deletedInfo.get(i))).get(COLUMN_INDEX_LAYERCAPTION);
				MapUtilities.removeLayer(this.resultMap, layer.getName());
			}
		}
		//   过滤掉不支持的数据集类型
		if (this.resultMap != null) {
			ArrayList<Layer> layers = MapUtilities.getLayers(this.resultMap);
			for (int i = 0; i < layers.size(); i++) {
				if (layers.get(i).getDataset().getType() != DatasetType.NETWORK &&
						layers.get(i).getDataset().getType() != DatasetType.NETWORK3D &&
						layers.get(i).getDataset().getType() != DatasetType.LINEM &&
						layers.get(i).getDataset().getType() != DatasetType.POINT3D &&
						layers.get(i).getDataset().getType() != DatasetType.LINE3D &&
						layers.get(i).getDataset().getType() != DatasetType.REGION3D
						) {

				} else {
					MapUtilities.removeLayer(this.resultMap, layers.get(i).getName());
				}
			}
		}

	}

	private void checkButtonState() {
		buttonSet.setEnabled(mapClipJTable.getSelectedRowCount() > 0);
		buttonDelete.setEnabled(mapClipJTable.getSelectedRowCount() > 0);
		buttonSelectAll.setEnabled(mapClipJTable.getRowCount() > 0);
		buttonInvertSelect.setEnabled(mapClipJTable.getRowCount() > 0);
	}

	/**
	 * ToolBar按钮响应事件
	 */
	private ActionListener toolBarActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (mapClipJTable.isEditing()) {
				mapClipJTable.getCellEditor().stopCellEditing();
			}
			if (e.getSource().equals(buttonSelectAll)) {
				// 全选
				if (mapClipJTable.getRowCount() - 1 < 0) {
					mapClipJTable.setRowSelectionAllowed(true);
				} else {
					mapClipJTable.setRowSelectionAllowed(true);
					// 设置所有项全部选中
					mapClipJTable.setRowSelectionInterval(0, mapClipJTable.getRowCount() - 1);
				}

			} else if (e.getSource().equals(buttonInvertSelect)) {
				//反选
				int[] temp = mapClipJTable.getSelectedRows();
				ArrayList<Integer> selectedRows = new ArrayList<Integer>();
				for (int index = 0; index < temp.length; index++) {
					selectedRows.add(temp[index]);
				}
				ListSelectionModel selectionModel = mapClipJTable.getSelectionModel();
				selectionModel.clearSelection();
				for (int index = 0; index < mapClipJTable.getRowCount(); index++) {
					if (!selectedRows.contains(index)) {
						selectionModel.addSelectionInterval(index, index);
					}
				}
			} else if (e.getSource().equals(buttonSet)) {
				// 弹出统一设置面板
				mapClipSetDialog = new MapClipSetDialog(mapClipJTable);
				mapClipSetDialog.showDialog();
			} else if (e.getSource().equals(buttonAddLayers)) {
				mapClipAddLayersDialog = new MapClipAddLayersDialog(deletedInfo);
				DialogResult dialogResult = mapClipAddLayersDialog.showDialog();
				if (dialogResult == DialogResult.OK) {
					selectedDeletedInfo = new Vector(6);
					selectedDeletedInfo = mapClipAddLayersDialog.getResultAddLayers();
					MapClipTableModel mapClipTableModel = (MapClipTableModel) mapClipJTable.getModel();
					for (int i = 0; i < selectedDeletedInfo.size(); i++) {
						mapClipTableModel.addRow((Vector) selectedDeletedInfo.get(i));
						deletedInfo.remove(selectedDeletedInfo.get(i));
					}
					mapClipJTable.updateUI();
					mapClipJTable.setRowSelectionInterval(mapClipJTable.getRowCount() - 1, mapClipJTable.getRowCount() - 1);
					isCanRun();
				}
			} else if (e.getSource().equals(buttonDelete)) {
				if (deletedInfo == null) {
					deletedInfo = new Vector(6);
				}
				int[] selectedRowsIndex = mapClipJTable.getSelectedRows();
				for (int i = selectedRowsIndex.length - 1; i >= 0; i--) {
					MapClipTableModel mapClipTableModel = (MapClipTableModel) mapClipJTable.getModel();
					int rowIndex = mapClipJTable.convertRowIndexToModel(selectedRowsIndex[i]);
					Vector e1 = ((Vector) mapClipJTable.getMapClipTableModel().getDataVector().get(rowIndex));
					deletedInfo.add(e1);
					mapClipTableModel.removeRow(rowIndex);
				}
				isCanRun();
				if (mapClipJTable.getRowCount() == selectedRowsIndex[0] && mapClipJTable.getRowCount() >= 1) {
					mapClipJTable.setRowSelectionInterval(selectedRowsIndex[0] - 1, selectedRowsIndex[0] - 1);
				} else if (mapClipJTable.getRowCount() > 0) {
					mapClipJTable.setRowSelectionInterval(selectedRowsIndex[0], selectedRowsIndex[0]);
				}
			}
		}
	};

	/**
	 * checkBox相应事件
	 */

	/**
	 * model内容改变监听，主要对数据集名称做以判断
	 */
	private TableModelListener tableModelListener = new TableModelListener() {
		@Override
		public void tableChanged(TableModelEvent e) {
			isCanRun();
			if (mapClipJTable.getModel().getRowCount() <= 0) return;
			if (e.getColumn() == COLUMN_INDEX_AIMDATASOURCE) {
				// 当数据源改变，重新设置文件名称
				int[] selectedRows = mapClipJTable.getSelectedRows();
				int selectedRow[] = new int[selectedRows.length];
				for (int i = 0; i < selectedRows.length; i++) {
					selectedRow[i] = mapClipJTable.convertRowIndexToModel(selectedRows[i]);
				}

				for (int i = 0; i < selectedRow.length; i++) {
					Datasource tempDatasource = (Datasource) mapClipJTable.getModel().getValueAt(selectedRow[i], COLUMN_INDEX_AIMDATASOURCE);
					String tempDatasetName = ((String) mapClipJTable.getModel().getValueAt(selectedRow[i], COLUMN_INDEX_AIMDATASET));

					//判断是否是original_n的形式，如果是就重新按照当前数据源计算，否则按照用户自定义判断合法性
					Boolean isFormatedName = false;
					Layer layer = (Layer) mapClipJTable.getModel().getValueAt(selectedRow[i], COLUMN_INDEX_LAYERCAPTION);
					Dataset dataset = layer.getDataset();
					if (StringUtilities.isNullOrEmpty(tempDatasetName)) {
						tempDatasetName = dataset.getName();
						isFormatedName = true;
					} else {
						if (tempDatasetName.startsWith(dataset.getName() + "_")) {
							String cutDatasetName = tempDatasetName.replaceFirst(dataset.getName() + "_", "");
							try {
								int n = Integer.parseInt(cutDatasetName);
								isFormatedName = true;
							} catch (NumberFormatException e1) {
								isFormatedName = false;
							}
						}
					}
					//_n格式的名称，切换数据源根据数据集名称重新计算
					if (isFormatedName) {
						tempDatasetName = mapClipJTable.getUsableDatasetName(dataset.getName(), tempDatasource, dataset);
					}
					//非_n格式的，只有在目标数据源下不合法时根据输入的名称重新计算
					else if (!mapClipJTable.isUsableDatasetName(tempDatasetName, tempDatasource, dataset)) {
						tempDatasetName = mapClipJTable.getUsableDatasetName(tempDatasetName, tempDatasource, dataset);
					}
					mapClipJTable.getModel().setValueAt(tempDatasetName, selectedRow[i], COLUMN_INDEX_AIMDATASET);
				}
			}
//			int selectedRowView = mapClipJTable.getSelectedRow();
//			if (selectedRowView < 0) return;
//			int selectedRow = mapClipJTable.convertRowIndexToModel(selectedRowView);
			if (e.getColumn() == COLUMN_INDEX_AIMDATASET) {
				//此时手动修改了结果数据集名称
				int selectedRowView = mapClipJTable.getSelectedRow();
				if (selectedRowView < 0) return;
				int selectedRow = mapClipJTable.convertRowIndexToModel(selectedRowView);
				Datasource tempDatasource = (Datasource) mapClipJTable.getModel().getValueAt(selectedRow, COLUMN_INDEX_AIMDATASOURCE);
				String tempDatasetName = (String) mapClipJTable.getModel().getValueAt(selectedRow, COLUMN_INDEX_AIMDATASET);
				Layer layer = (Layer) mapClipJTable.getModel().getValueAt(selectedRow, COLUMN_INDEX_LAYERCAPTION);
				Dataset dataset = layer.getDataset();
				Boolean isFormatedName = false;
				if (StringUtilities.isNullOrEmpty(tempDatasetName)) {
					tempDatasetName = dataset.getName();
					isFormatedName = true;
				} else {
					if (tempDatasetName.startsWith(dataset.getName() + "_")) {
						String cutDatasetName = tempDatasetName.replaceFirst(dataset.getName() + "_", "");
						try {
							int n = Integer.parseInt(cutDatasetName);
							isFormatedName = true;
						} catch (NumberFormatException e1) {
							isFormatedName = false;
						}
					}
				}
				//手动输入名称时，之后不合法才重新计算
				if (!mapClipJTable.isUsableDatasetName(tempDatasetName, tempDatasource, dataset)) {
					//_n格式的名称根据源数据集名计算
					if (isFormatedName) {
						tempDatasetName = mapClipJTable.getUsableDatasetName(dataset.getName(), tempDatasource, dataset);
					}
					//非_n格式的，根据输入的名称重新计算
					else {
						tempDatasetName = mapClipJTable.getUsableDatasetName(tempDatasetName, tempDatasource, dataset);
					}
				}

                /*
                boolean usableDatasetName = mapClipJTable.isUsableDatasetName(tempDatasetName, tempDatasource, dataset);
	            if (!usableDatasetName) {
		            tempDatasetName = mapClipJTable.getUsableDatasetName(dataset.getName(), tempDatasource, dataset);
	            }*/
				// 当手动修改JTable中结果数据集名称时，需要做一个判断，如果JTable中有相同数据集的条目，同时修改其结果数据集名称
				ArrayList<Integer> array = new ArrayList<>();
				for (int i = 0; i < mapClipJTable.getModel().getRowCount(); i++) {
					Layer tempLayer = (Layer) mapClipJTable.getModel().getValueAt(i, COLUMN_INDEX_LAYERCAPTION);
					Dataset tempDataset = tempLayer.getDataset();
					if (tempDataset.equals(dataset)) {
						array.add(i);
					}
				}
				if (array.size() > 0) {
					//改变值之前先移除监听
					try {
						mapClipJTable.getModel().removeTableModelListener(tableModelListener);
						for (int i = 0; i < array.size(); i++) {
							int row = array.get(i);
							mapClipJTable.getModel().setValueAt(tempDatasetName, row, COLUMN_INDEX_AIMDATASET);
						}
					} finally {
						mapClipJTable.getModel().addTableModelListener(tableModelListener);
					}
				}

			}
			if (e.getColumn() == COLUMN_INDEX_CLIPTYPE) {
				int selectedRowView = mapClipJTable.getSelectedRow();
				if (selectedRowView < 0) return;
				int selectedRow = mapClipJTable.convertRowIndexToModel(selectedRowView);
				String clipType = (String) mapClipJTable.getModel().getValueAt(selectedRow, COLUMN_INDEX_CLIPTYPE);
				Layer layer = (Layer) mapClipJTable.getModel().getValueAt(selectedRow, COLUMN_INDEX_LAYERCAPTION);
				Dataset dataset = layer.getDataset();
				ArrayList<Integer> array = new ArrayList<>();
				for (int i = 0; i < mapClipJTable.getModel().getRowCount(); i++) {
					if (i == selectedRow) {
						continue;
					}
					Layer tempLayer = (Layer) mapClipJTable.getModel().getValueAt(i, COLUMN_INDEX_LAYERCAPTION);
					Dataset tempDataset = tempLayer.getDataset();
					if (tempDataset.equals(dataset)) {
						array.add(i);
					}
				}
				if (array.size() > 0) {
					//改变值之前先移除监听
					mapClipJTable.getModel().removeTableModelListener(tableModelListener);
					for (int i = 0; i < array.size(); i++) {
						int row = array.get(i);
						mapClipJTable.getModel().setValueAt(clipType, row, COLUMN_INDEX_CLIPTYPE);
					}
					mapClipJTable.getModel().addTableModelListener(tableModelListener);
				}
			}
			if (e.getColumn() == COLUMN_INDEX_ERASE) {
				int selectedRowView = mapClipJTable.getSelectedRow();
				if (selectedRowView < 0) return;
				int selectedRow = mapClipJTable.convertRowIndexToModel(selectedRowView);
				String isEarse = (String) mapClipJTable.getModel().getValueAt(selectedRow, COLUMN_INDEX_ERASE);
				Layer layer = (Layer) mapClipJTable.getModel().getValueAt(selectedRow, COLUMN_INDEX_LAYERCAPTION);
				Dataset dataset = layer.getDataset();
				ArrayList<Integer> array = new ArrayList<>();
				for (int i = 0; i < mapClipJTable.getModel().getRowCount(); i++) {
					if (i == selectedRow) {
						continue;
					}
					Layer tempLayer = (Layer) mapClipJTable.getModel().getValueAt(i, COLUMN_INDEX_LAYERCAPTION);
					Dataset tempDataset = tempLayer.getDataset();
					if (tempDataset.equals(dataset)) {
						array.add(i);
					}
				}
				if (array.size() > 0) {
					//改变值之前先移除监听
					mapClipJTable.getModel().removeTableModelListener(tableModelListener);
					for (int i = 0; i < array.size(); i++) {
						int row = array.get(i);
						mapClipJTable.getModel().setValueAt(isEarse, row, COLUMN_INDEX_ERASE);
					}
					mapClipJTable.getModel().addTableModelListener(tableModelListener);
				}
			}
			if (e.getColumn() == COLUMN_INDEX_EXACTCLIP) {
				int selectedRowView = mapClipJTable.getSelectedRow();
				if (selectedRowView < 0) return;
				int selectedRow = mapClipJTable.convertRowIndexToModel(selectedRowView);
				String isExactClip = (String) mapClipJTable.getModel().getValueAt(selectedRow, COLUMN_INDEX_EXACTCLIP);
				Layer layer = (Layer) mapClipJTable.getModel().getValueAt(selectedRow, COLUMN_INDEX_LAYERCAPTION);
				Dataset dataset = layer.getDataset();
				ArrayList<Integer> array = new ArrayList<>();
				for (int i = 0; i < mapClipJTable.getModel().getRowCount(); i++) {
					if (i == selectedRow) {
						continue;
					}
					Layer tempLayer = (Layer) mapClipJTable.getModel().getValueAt(i, COLUMN_INDEX_LAYERCAPTION);
					Dataset tempDataset = tempLayer.getDataset();
					if (tempDataset.equals(dataset)) {
						array.add(i);
					}
				}
				if (array.size() > 0) {
					//改变值之前先移除监听
					mapClipJTable.getModel().removeTableModelListener(tableModelListener);
					for (int i = 0; i < array.size(); i++) {
						int row = array.get(i);
						mapClipJTable.getModel().setValueAt(isExactClip, row, COLUMN_INDEX_EXACTCLIP);
					}
					mapClipJTable.getModel().addTableModelListener(tableModelListener);
				}
			}

		}
	};


	/**
	 * 确定取消按钮事件
	 */
	private ActionListener cancelButtonActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			removeEvents();
			DialogMapClip.this.dispose();
		}
	};

	/**
	 * 确定按钮事件
	 */
	private ActionListener OKButtonActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			TableUtilities.stopEditing(mapClipJTable);
			// 当点击了确定按钮，进行裁剪操作，首先对JTable中model数据进行处理,得到适用于裁剪接口的数据
			saveMapName = mapClipSaveMapPanel.getMapName();
			IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			if (mapClipSaveMapPanel.isSaveMap() && !StringUtilities.isNullOrEmpty(saveMapName)) {
				resultMap = new Map(formMap.getMapControl().getMap().getWorkspace());
				resultMap.fromXML(formMap.getMapControl().getMap().toXML());
				resultMap.setName(saveMapName);
			}
			initResultVectorInfo();
			if (resultInfo != null && resultInfo.size() > 0) {
				FormProgressTotal formProgress = new FormProgressTotal();
				formProgress.setTitle(MapViewProperties.getString("String_MapClip_MapClip"));
				//获得要保存的地图名称
				mapClipProgressCallable = new MapClipProgressCallable(resultInfo, resultMap);
				formProgress.doWork(mapClipProgressCallable);
				// 获得采集后的数据集，添加到地图
				DialogMapClip.this.dispose();
			} else {
				Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_MapClip_SelectLayer"));
			}
		}
	};

	private void isCanRun() {
		if (this.mapClipJTable.getRowCount() >= 1 && this.isSaveMapNameIsValid && this.isDatasetNameIsValid) {
			this.panelButton.getButtonOk().setEnabled(true);
		} else {
			this.panelButton.getButtonOk().setEnabled(false);
		}
	}

	private boolean isRepeatDatasetName() {
		boolean result = true;

		String tempDatasetName = (String) mapClipJTable.getModel().getValueAt(mapClipJTable.getSelectedRow(), COLUMN_INDEX_AIMDATASET);
		Layer layer = (Layer) mapClipJTable.getModel().getValueAt(mapClipJTable.getSelectedRow(), COLUMN_INDEX_LAYERCAPTION);
		Dataset dataset = layer.getDataset();
		for (int i = 0; i < mapClipJTable.getModel().getRowCount(); i++) {
			if (i == mapClipJTable.getSelectedRow()) {
				continue;
			}
			Layer tempLayer = (Layer) mapClipJTable.getModel().getValueAt(i, COLUMN_INDEX_LAYERCAPTION);
			Dataset tempDataset = tempLayer.getDataset();
			String otherDatasetName = (String) mapClipJTable.getModel().getValueAt(i, COLUMN_INDEX_AIMDATASET);
			if (!tempDataset.equals(dataset) && tempDatasetName.equals(otherDatasetName)) {
				result = false;
				Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_MapClip_NotSameDatasetWithSameName"));
				break;
			}
		}
		return result;
	}

	public DialogMapClip(GeoRegion region) {
		super();
		this.geoRegion = region;
		initComponent();
		initLayout();
		initResources();
		registEvents();
		this.pack();
		this.setSize(new Dimension(750, 450));
		this.setLocationRelativeTo(null);
		this.componentList.add(this.panelButton.getButtonOk());
		this.componentList.add(this.panelButton.getButtonCancel());
		this.setFocusTraversalPolicy(policy);
		isCanRun();
	}

	private void initComponent() {
		initToolbar();

		this.mapClipJTable = new MapClipJTable();

		this.scrollPane = new JScrollPane(this.mapClipJTable);
		// 地图裁剪保存地图面板
		this.mapClipSaveMapPanel = new MapClipSaveMapPanel();
		this.saveMapcompTitledPane = mapClipSaveMapPanel.getCompTitledPane();
		// 确定取消按钮面板
		this.panelButton = new PanelButton();

	}

	private void initToolbar() {
		this.toolBar = new JToolBar();
		this.buttonAddLayers = new JButton();
		this.buttonDelete = new JButton();
		this.buttonSelectAll = new JButton();
		this.buttonInvertSelect = new JButton();
		this.buttonSet = new JButton();

		this.toolBar.setFloatable(false);
		this.toolBar.add(this.buttonAddLayers);
		this.toolBar.add(this.buttonDelete);
		this.toolBar.addSeparator();
		this.toolBar.add(this.buttonSelectAll);
		this.toolBar.add(this.buttonInvertSelect);
		this.toolBar.addSeparator();
		this.toolBar.add(this.buttonSet);
	}

	private void initLayout() {
		JPanel mainPanel = new JPanel();
		GroupLayout panelLayout = new GroupLayout(mainPanel);
		panelLayout.setAutoCreateContainerGaps(true);
		panelLayout.setAutoCreateGaps(true);
		mainPanel.setLayout(panelLayout);
		//@formatter:off
		panelLayout.setHorizontalGroup(panelLayout.createParallelGroup()
				.addComponent(this.toolBar)
				.addComponent(this.scrollPane)
				.addGroup(panelLayout.createSequentialGroup()
						.addComponent(saveMapcompTitledPane)
				)
				.addComponent(this.panelButton));
		panelLayout.setVerticalGroup(panelLayout.createSequentialGroup()
				.addComponent(this.toolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addComponent(this.scrollPane)
				.addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(saveMapcompTitledPane)
				)
				.addComponent(this.panelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE));
		//@formatter:on

		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.CENTER);
	}

	private void registEvents() {
		removeEvents();
		this.buttonAddLayers.addActionListener(this.toolBarActionListener);
		this.buttonDelete.addActionListener(this.toolBarActionListener);
		this.buttonSelectAll.addActionListener(this.toolBarActionListener);
		this.buttonInvertSelect.addActionListener(this.toolBarActionListener);
		this.buttonSet.addActionListener(this.toolBarActionListener);
		this.mapClipJTable.getModel().addTableModelListener(tableModelListener);
		this.mapClipSaveMapPanel.addSaveMapStatusChangedListener(new SaveMapStatusChangedListener() {
			@Override
			public void statusChange() {
				isSaveMapNameIsValid = mapClipSaveMapPanel.isLegalStatus();
				isCanRun();
			}
		});
		this.mapClipJTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				checkButtonState();
			}
		});
		this.panelButton.getButtonOk().addActionListener(OKButtonActionListener);
		this.panelButton.getButtonCancel().addActionListener(cancelButtonActionListener);
	}

	private void removeEvents() {
		this.buttonAddLayers.removeActionListener(this.toolBarActionListener);
		this.buttonDelete.removeActionListener(this.toolBarActionListener);
		this.buttonSelectAll.removeActionListener(this.toolBarActionListener);
		this.buttonInvertSelect.removeActionListener(this.toolBarActionListener);
		this.buttonSet.removeActionListener(this.toolBarActionListener);
		this.mapClipJTable.getModel().removeTableModelListener(tableModelListener);
		this.panelButton.getButtonOk().removeActionListener(OKButtonActionListener);
		this.panelButton.getButtonCancel().removeActionListener(cancelButtonActionListener);
	}

	private void initResources() {
		this.setTitle(MapViewProperties.getString("String_MapClip_MapClip"));
		this.buttonAddLayers.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_AddItem.png"));
		this.buttonAddLayers.setToolTipText(MapViewProperties.getString("String_MapClip_AddLayers"));
		this.buttonDelete.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Delete.png"));
		this.buttonDelete.setToolTipText(CommonProperties.getString("String_Delete"));
		this.buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
		this.buttonInvertSelect.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
		this.buttonSet.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Setting.PNG"));
		this.buttonSelectAll.setToolTipText(CommonProperties.getString("String_ToolBar_SelectAll"));
		this.buttonInvertSelect.setToolTipText(CommonProperties.getString("String_ToolBar_SelectInverse"));
		this.buttonSet.setToolTipText(CommonProperties.getString("String_ToolBar_SetBatch"));
	}
}

