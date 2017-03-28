package com.supermap.desktop.CtrlAction.Map.MapClip;

import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.CellRenders.TableDataCellRender;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTable;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.ui.MapControl;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;

import static com.supermap.desktop.CtrlAction.Map.MapClip.MapClipTableModel.*;

/**
 * @author YuanR
 * 2017.3.28
 */
public class MapClipJTable extends MutiTable {

	private TableColumn layerCaption;
	private TableColumn aimDatasourceColumn;
	private TableColumn clipTypeColumn;
	private TableColumn eraseColumn;

	private MapClipTableModel mapClipTableModel;


	public MapClipJTable() {
		super();
		initComponents();
		initLayerInfo();
		registEvents();
	}

	/**
	 * 初始化JTable控件
	 */
	private void initComponents() {
		//设置标题行不能移动
		this.getTableHeader().setReorderingAllowed(false);
		this.setRowHeight(23);
		mapClipTableModel = new MapClipTableModel();
		this.setModel(mapClipTableModel);

		//设置单元格为下拉列表样式
		DatasourceComboBox datasourceComboBox = new DatasourceComboBox();

		String[] clipType = {MapViewProperties.getString("String_MapClip_Out"), MapViewProperties.getString("String_MapClip_In")};
		JComboBox clipTypeComboBox = new JComboBox(clipType);
		String[] erase = {MapViewProperties.getString("String_MapClip_Yes"), MapViewProperties.getString("String_MapClip_No")};
		JComboBox eraseComboBox = new JComboBox(erase);

		this.layerCaption = this.getColumn(this.getModel().getColumnName(COLUMN_INDEX_LAYERCAPTION));
		this.aimDatasourceColumn = this.getColumn(this.getModel().getColumnName(COLUMN_INDEX_AIMDATASOURCE));
		this.clipTypeColumn = this.getColumn(this.getModel().getColumnName(COLUMN_INDEX_CLIPTYPE));
		this.eraseColumn = this.getColumn(this.getModel().getColumnName(COLUMN_INDEX_ERASE));

		this.aimDatasourceColumn.setCellEditor(new DefaultCellEditor(datasourceComboBox));
		this.clipTypeColumn.setCellEditor(new DefaultCellEditor(clipTypeComboBox));
		this.eraseColumn.setCellEditor(new DefaultCellEditor(eraseComboBox));

		//设置渲染器
		this.aimDatasourceColumn.setCellRenderer(new TableDataCellRender());
		this.layerCaption.setCellRenderer(new MapClipLayerCaptionTableRender());
	}

	/**
	 * 初始化JTable内容
	 */
	private void initLayerInfo() {
		ArrayList<Layer> resultLayer = new ArrayList();
		MapControl activeMapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
		ArrayList<Layer> arrayList;
		arrayList = MapUtilities.getLayers(activeMapControl.getMap(), true);
		//是否存在图层
		if (arrayList.size() > 0) {
			for (int i = 0; i < arrayList.size(); i++) {
				if (arrayList.get(i).getDataset() == null) {
					continue;
				}
				resultLayer.add(arrayList.get(i));
			}
		}

		for (int i = 0; i < resultLayer.size(); i++) {
			boolean clip = false;
			Layer layerCaption = resultLayer.get(i);
			Datasource targetDatasource = resultLayer.get(i).getDataset().getDatasource();
			String targetDataset = resultLayer.get(i).getDataset().getName() + "_1";
			String clipType = MapViewProperties.getString("String_MapClip_In");
			String erase = MapViewProperties.getString("String_MapClip_No");
			mapClipTableModel.addRowLayerInfo(clip, layerCaption, targetDatasource, targetDataset, clipType, erase);
			this.updateUI();
		}
	}

	private void registEvents() {
		removeEvents();
	}

	public void removeEvents() {

	}

	/**
	 * 负责对layerCaption列渲染的内部类
	 */
	class MapClipLayerCaptionTableRender extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value,
		                                               boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
					row, column);
			this.setText(((Layer) value).getCaption());
			return this;
		}
	}
}
