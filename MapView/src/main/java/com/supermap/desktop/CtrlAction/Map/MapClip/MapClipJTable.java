package com.supermap.desktop.CtrlAction.Map.MapClip;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.CellRenders.TableDataCellRender;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.SortTable.BlankIcon;
import com.supermap.desktop.ui.controls.SortTable.SortButtonRenderer;
import com.supermap.desktop.ui.controls.SortTable.SortTable;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.ui.MapControl;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import static com.supermap.desktop.Application.getActiveApplication;
import static com.supermap.desktop.CtrlAction.Map.MapClip.MapClipTableModel.*;

/**
 * @author YuanR
 *         2017.3.28
 */
public class MapClipJTable extends SortTable {

	private TableColumn layerCaption;
	private TableColumn aimDatasourceColumn;
	private TableColumn aimDatasetColumn;
	private TableColumn clipTypeColumn;
	private TableColumn eraseColumn;
	private TableColumn acurrentClipColumn;
	public MapClipTableModel mapClipTableModel;
	private ArrayList<Datasource> isCanUseDatasources;
	private HashMap<String, String> datasetsName = new HashMap<>();

	public MapClipTableModel getMapClipTableModel() {
		return mapClipTableModel;
	}

    public MapClipJTable() {
        super();
        initComponents();
        initLayerInfo();
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

		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		this.isCanUseDatasources = new ArrayList<>();
		for (int i = 0; i < datasources.getCount(); i++) {
			if (!datasources.get(i).isReadOnly()) {
				this.isCanUseDatasources.add(datasources.get(i));
			}
		}
		DatasourceComboBox datasourceComboBox = null;
		if (this.isCanUseDatasources != null && this.isCanUseDatasources.size() > 0) {
			datasourceComboBox = new DatasourceComboBox(this.isCanUseDatasources);
		} else {
			return;
		}

		String[] clipType = {MapViewProperties.getString("String_MapClip_Out"), MapViewProperties.getString("String_MapClip_In")};
		JComboBox clipTypeComboBox = new JComboBox(clipType);
		String[] erase = {MapViewProperties.getString("String_MapClip_Yes"), MapViewProperties.getString("String_MapClip_No")};
		JComboBox eraseComboBox = new JComboBox(erase);
		String[] acurrent = {MapViewProperties.getString("String_MapClip_Yes"), MapViewProperties.getString("String_MapClip_No")};
		JComboBox acurrentComboBox = new JComboBox(acurrent);

		this.layerCaption = this.getColumn(this.getModel().getColumnName(COLUMN_INDEX_LAYERCAPTION));
		this.aimDatasetColumn = this.getColumn(this.getModel().getColumnName(COLUMN_INDEX_AIMDATASET));
		this.aimDatasourceColumn = this.getColumn(this.getModel().getColumnName(COLUMN_INDEX_AIMDATASOURCE));
		this.clipTypeColumn = this.getColumn(this.getModel().getColumnName(COLUMN_INDEX_CLIPTYPE));
		this.eraseColumn = this.getColumn(this.getModel().getColumnName(COLUMN_INDEX_ERASE));
		this.acurrentClipColumn = this.getColumn(this.getModel().getColumnName(COLUMN_INDEX_EXACTCLIP));

		DefaultCellEditor defaultCellEditorAimDatasourceColumn = new DefaultCellEditor(datasourceComboBox);
		defaultCellEditorAimDatasourceColumn.setClickCountToStart(2);
		this.aimDatasourceColumn.setCellEditor(defaultCellEditorAimDatasourceColumn);
		DefaultCellEditor defaultCellEditorClipTypeColumn = new DefaultCellEditor(clipTypeComboBox);
		defaultCellEditorClipTypeColumn.setClickCountToStart(2);
		this.clipTypeColumn.setCellEditor(defaultCellEditorClipTypeColumn);
		DefaultCellEditor defaultCellEditorEraseColumn = new DefaultCellEditor(eraseComboBox);
		defaultCellEditorEraseColumn.setClickCountToStart(2);
		this.eraseColumn.setCellEditor(defaultCellEditorEraseColumn);
		DefaultCellEditor defaultCellEditorAcurrentClipColumn = new DefaultCellEditor(acurrentComboBox);
		defaultCellEditorAcurrentClipColumn.setClickCountToStart(2);
		this.acurrentClipColumn.setCellEditor(defaultCellEditorAcurrentClipColumn);

		//设置渲染器
		this.aimDatasourceColumn.setCellRenderer(new TableDataCellRender());
		this.layerCaption.setCellRenderer(new MapClipLayerCaptionTableRender());
		this.aimDatasetColumn.setCellRenderer(new TargetDatasetTableRender());
		//header 图片tip
		JTableHeader jTableHeader = new JTableHeader(this.getColumnModel()) {
			Point point;
			int column;

			@Override
			public String getToolTipText(MouseEvent event) {
				point = event.getPoint();
				column = this.columnAtPoint(point);
				String toolTipText = super.getToolTipText(event);
				if (column == COLUMN_INDEX_CLIPTYPE
						|| column == COLUMN_INDEX_ERASE
						|| column == COLUMN_INDEX_EXACTCLIP
						&& toolTipText == null) {
					return "";
				}
				return toolTipText;
			}

			@Override
			public JToolTip createToolTip() {
				if (column == COLUMN_INDEX_CLIPTYPE) {
					JToolTip tip = super.createToolTip();
					tip.setLayout(new BorderLayout());
					tip.add(new JLabel(MapViewProperties.getString("String_MapClip_ClipTypeExplain")), BorderLayout.NORTH);
					tip.add(new JLabel(ControlsResources.getIcon("/controlsresources/Image_ClipType.png")), BorderLayout.SOUTH);
					tip.setPreferredSize(new Dimension(250, 186));
					return tip;
				}
				if (column == COLUMN_INDEX_ERASE) {
					JToolTip tip = super.createToolTip();
					tip.setLayout(new BorderLayout());
					JLabel jLabel = new JLabel(MapViewProperties.getString("String_MapClip_EraseChangeOrigionDataset"));
					tip.add(jLabel, BorderLayout.CENTER);
					tip.setPreferredSize(new Dimension(150, 23));
					return tip;
				}
				return null;
			}
		};
		this.setTableHeader(jTableHeader);

		this.setSortButtonRenderer(new ClipTypeColumnCellRenderer());
		ImageAndTextTableHeaderCell eraseTip = new ImageAndTextTableHeaderCell(MapViewProperties.getString("String_MapClip_Erase"), ControlsResources.getIcon("/controlsresources/Icon_Warning.png"));
		eraseTip.setToolTipText(""); //  用来激活表头显示的tip，并且只能为空字符串，如果非空则会覆盖掉tip
		this.eraseColumn.setCellRenderer(new MapClipCellRender());
		this.acurrentClipColumn.setCellRenderer(new MapClipCellRender());
	}

	/**
	 * 初始化JTable内容
	 */
	private void initLayerInfo() {
		ArrayList<Layer> resultLayer = new ArrayList();
		MapControl activeMapControl = ((IFormMap) getActiveApplication().getActiveForm()).getMapControl();
		ArrayList<Layer> arrayList;
		arrayList = MapUtilities.getLayers(activeMapControl.getMap(), true);
		//是否存在图层
		if (arrayList.size() > 0) {
			for (int i = 0; i < arrayList.size(); i++) {
				if (arrayList.get(i).getDataset() != null &&
						(arrayList.get(i).getDataset().getType() == DatasetType.POINT ||
								arrayList.get(i).getDataset().getType() == DatasetType.LINE ||
								arrayList.get(i).getDataset().getType() == DatasetType.REGION ||
								arrayList.get(i).getDataset().getType() == DatasetType.TEXT ||
								arrayList.get(i).getDataset().getType() == DatasetType.CAD ||
								arrayList.get(i).getDataset().getType() == DatasetType.GRID ||
								arrayList.get(i).getDataset().getType() == DatasetType.IMAGE)) {
					resultLayer.add(arrayList.get(i));
				}
			}
		}
		for (int i = 0; i < resultLayer.size(); i++) { // 对获得的图层进行遍历，当数据集类型属于矢量和影像时才进行添加
			Layer layerCaption = resultLayer.get(i);
			Dataset dataset = resultLayer.get(i).getDataset();
			Datasource targetDatasource = dataset.getDatasource();
			if (targetDatasource.isReadOnly()) {
				if (this.isCanUseDatasources != null && this.isCanUseDatasources.size() > 0) {
					targetDatasource = this.isCanUseDatasources.get(0);
				}
			}

			// 当初始化的时候就通过判断设置好结果数据集的名称
			String origionalDatasetName = resultLayer.get(i).getDataset().getName();
			String targetDatasetName = this.datasetsName.get(origionalDatasetName);
			if (targetDatasetName == null) {
				targetDatasetName = getUsableDatasetName(resultLayer.get(i).getDataset().getName(), dataset.getDatasource(), dataset);
				this.datasetsName.put(origionalDatasetName, targetDatasetName);
			}

			String clipType = MapViewProperties.getString("String_MapClip_In");
			String erase = MapViewProperties.getString("String_MapClip_No");
			String exactClip = MapViewProperties.getString("String_MapClip_No");
			if (dataset instanceof DatasetVector) {
				exactClip = MapViewProperties.getString("String_MapClip_Yes");
			}
			this.mapClipTableModel.addRowLayerInfo(layerCaption, targetDatasource, targetDatasetName, clipType, erase, exactClip);
		}
		if (this.mapClipTableModel.getRowCount() >= 1) {
			this.setRowSelectionInterval(0, 0);// 设置首行记录被选中
		}
	}

	public boolean isUsableDatasetName(String name, Datasource targetDatasource, Dataset dataset) {
		if (name == null || dataset == null || targetDatasource == null) {
			return false;
		}
		//在目标数据源中可用
		if (!targetDatasource.getDatasets().isAvailableDatasetName(name)) {
			return false;
		}
		//table中未被占用
		Vector layerInfos = mapClipTableModel.getLayerInfo();
		for (Object layerInfo : layerInfos) {
			Datasource datasource = (Datasource) ((Vector) layerInfo).get(COLUMN_INDEX_AIMDATASOURCE);
			String targetDatasetName = (String) ((Vector) layerInfo).get(COLUMN_INDEX_AIMDATASET);
			Layer layer = (Layer) ((Vector) layerInfo).get(COLUMN_INDEX_LAYERCAPTION);
			Dataset originalDataset = layer.getDataset();
			if (targetDatasource == datasource && dataset != originalDataset && name.equals(targetDatasetName)) {
				return false;
			}
		}
		return true;
	}

	public String[] getUsedDatasetNames(Datasource datasource, Dataset dataset) {
		ArrayList<String> usedNames = new ArrayList<>();
		if (datasource == null || dataset == null) {
			return usedNames.toArray(new String[usedNames.size()]);
		}
		Vector layerInfos = mapClipTableModel.getLayerInfo();
		for (Object layerInfo : layerInfos) {
			Datasource targetDatasource = (Datasource) ((Vector) layerInfo).get(COLUMN_INDEX_AIMDATASOURCE);
			String targetDatasetName = (String) ((Vector) layerInfo).get(COLUMN_INDEX_AIMDATASET);
			Layer layer = (Layer) ((Vector) layerInfo).get(COLUMN_INDEX_LAYERCAPTION);
			Dataset originalDataset = layer.getDataset();
			if (targetDatasource == datasource && dataset != originalDataset) {
				usedNames.add(targetDatasetName);
			}
		}
		return usedNames.toArray(new String[usedNames.size()]);

	}

	public String getUsableDatasetName(String source, Datasource targetDatasource, Dataset originalDataset) {
		return DatasetUtilities.getAvailableDatasetName(targetDatasource, source, getUsedDatasetNames(targetDatasource, originalDataset));
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
			this.setToolTipText(((Layer) value).getCaption());
			return this;
		}
	}

	class TargetDatasetTableRender extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value,
		                                               boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
					row, column);
			this.setToolTipText(value.toString());
			return this;
		}
	}

	class ImageAndTextTableHeaderCell extends JLabel {
		JLabel labelCaption;
		JLabel labelTip;

		public ImageAndTextTableHeaderCell(String showText, Icon showIcon) {
			this.labelCaption = new JLabel(showText);
			this.labelTip = new JLabel(showIcon);
			initLayout();
		}

		private void initLayout() {
			this.labelTip.setMinimumSize(new Dimension(23, 23));
			this.labelTip.setMaximumSize(new Dimension(23, 23));
			this.setLayout(new BorderLayout());
			this.add(this.labelCaption, BorderLayout.WEST);
			this.add(this.labelTip, BorderLayout.EAST);
		}
	}

	private class ClipTypeColumnCellRenderer extends SortButtonRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (component instanceof JButton) {
				if (column == COLUMN_INDEX_CLIPTYPE
						|| column == COLUMN_INDEX_ERASE
						|| column == COLUMN_INDEX_EXACTCLIP) {
					if (!((JButton) component).getModel().isPressed()) {
						((JButton) component).setIcon(ControlsResources.getIcon("/controlsresources/Icon_Help.png"));
					}
				} else {
					((JButton) component).setIcon(new BlankIcon());
				}
			}
			return component;
		}
	}

	class MapClipCellRender extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component tableCellRendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			//不可编辑单元格置灰
			if (table.getModel() != null && !table.getModel().isCellEditable(row, column)) {
				Color non_editableColor = UIManager.getColor("Table.shadow");
				tableCellRendererComponent.setForeground(non_editableColor);
			} else {
				if (isSelected) {
					tableCellRendererComponent.setForeground(table.getSelectionForeground());
				} else {
					tableCellRendererComponent.setForeground(table.getForeground());
				}
			}
			return tableCellRendererComponent;
		}
	}

}
