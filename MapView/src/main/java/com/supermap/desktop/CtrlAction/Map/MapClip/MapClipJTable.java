package com.supermap.desktop.CtrlAction.Map.MapClip;

import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetImage;
import com.supermap.data.DatasetVector;
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
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static com.supermap.desktop.CtrlAction.Map.MapClip.MapClipTableModel.*;

/**
 * @author YuanR
 *         2017.3.28
 */
public class MapClipJTable extends MutiTable {

	private TableColumn parClip;
	private TableColumn layerCaption;
	private TableColumn aimDatasourceColumn;
	private TableColumn clipTypeColumn;
	private TableColumn eraseColumn;
	public MapClipTableModel mapClipTableModel;

	public MapClipTableModel getMapClipTableModel() {
		return mapClipTableModel;
	}

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

		this.parClip = this.getColumn(this.getModel().getColumnName(COLUMN_INDEX_ISCLIP));
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

		// 给是否参与裁剪表头添加CheckBox
		this.parClip.setHeaderRenderer(new CheckHeaderCellRenderer(this, MapViewProperties.getString("String_MapClip_IsClip"), false));

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
			// 对获得的图层进行遍历，当数据集类型属于矢量和影像时才进行添加
			if (resultLayer.get(i).getDataset() instanceof DatasetVector ||
					resultLayer.get(i).getDataset() instanceof DatasetImage ||
					resultLayer.get(i).getDataset() instanceof DatasetGrid) {
				boolean clip = false;
				Layer layerCaption = resultLayer.get(i);
				Datasource targetDatasource = resultLayer.get(i).getDataset().getDatasource();
				// 当初始化的时候就通过判断设置好结果数据集的名称
				String targetDataset = resultLayer.get(i).getDataset().getName();
				while (!targetDatasource.getDatasets().isAvailableDatasetName(targetDataset)) {
					targetDataset = targetDataset + "_1";
				}
				String clipType = MapViewProperties.getString("String_MapClip_In");
				String erase = MapViewProperties.getString("String_MapClip_No");
				boolean exactClip = false;
				this.mapClipTableModel.addRowLayerInfo(clip, layerCaption, targetDatasource, targetDataset, clipType, erase, exactClip);
				this.updateUI();
			} else {
				continue;
			}
		}
		// 设置首行记录被选中
		this.setRowSelectionInterval(0, 0);
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

	/**
	 * 负责对“参与裁剪”列头做渲染
	 */
	class CheckHeaderCellRenderer implements TableCellRenderer {
		AbstractTableModel tableModel;
		JTableHeader tableHeader;
		final JCheckBox checkBox;

		/*
		*parm table要渲染的table，titletable标题，isSelectedCheckBox是否选中checkbox
		 */
		public CheckHeaderCellRenderer(JTable table, String title, boolean isSelectedCheckBox) {
			this.tableModel = (AbstractTableModel) table.getModel();
			this.tableHeader = table.getTableHeader();
			this.checkBox = new JCheckBox(tableModel.getColumnName(0));
			this.checkBox.setSelected(isSelectedCheckBox);
			this.checkBox.setText(title);
			if (isSelectedCheckBox) {
				for (int i = 0; i < tableModel.getRowCount(); i++) {
					tableModel.setValueAt(isSelectedCheckBox, i, COLUMN_INDEX_ISCLIP);
				}
				tableHeader.repaint();
			}
			this.tableHeader.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() > 0) {
						//获得选中列
						int selectColumn = tableHeader.columnAtPoint(e.getPoint());
						if (selectColumn == 0) {
							boolean value = !checkBox.isSelected();
							checkBox.setSelected(value);
							for (int i = 0; i < tableModel.getRowCount(); i++) {
								tableModel.setValueAt(value, i, COLUMN_INDEX_ISCLIP);
							}
							tableHeader.repaint();
						}
					}
				}
			});
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
		                                               boolean isSelected, boolean hasFocus, int row, int column) {
			String valueStr = (String) value;
			JLabel label = new JLabel(valueStr);
			label.setHorizontalAlignment(SwingConstants.CENTER); // 表头标签居左边
			this.checkBox.setHorizontalAlignment(SwingConstants.CENTER);// 表头checkBox居中
//			checkBox.setBorderPainted(true);
			JComponent component = (column == 0) ? checkBox : label;

//			component.setForeground(table.getForeground());
//			component.setBackground(table.getBackground());
//			component.setFont(table.getFont());
//			component.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			return component;
		}
	}
}
