package com.supermap.desktop.CtrlAction.Map.MapClip;

import com.supermap.data.DatasetType;
import com.supermap.data.GeoRegion;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.ComponentBorderPanel.CompTitledPane;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.borderPanel.PanelButton;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.mapping.Layer;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import static com.supermap.desktop.CtrlAction.Map.MapClip.MapClipTableModel.COLUMN_INDEX_EXACTCLIP;
import static com.supermap.desktop.CtrlAction.Map.MapClip.MapClipTableModel.COLUMN_INDEX_LAYERCAPTION;

/**
 * @author YuanR
 *         2017.3.21
 *         地图裁剪主窗体
 */
public class DialogMapClip extends SmDialog {
	private GeoRegion geoRegion;

	private JToolBar toolBar;
	private JButton buttonSelectAll;
	private JButton buttonInvertSelect;
	private JButton buttonSet;
	private MapClipSetDialog mapClipSetDialog;
	private JScrollPane scrollPane;
	private MapClipJTable mapClipJTable;

	private MapClipSaveMapPanel mapClipSaveMapPanel;
	private CompTitledPane saveMapcompTitledPane;
	private JPanel clipSetPanel;
	private JCheckBox exactClip;

	private PanelButton panelButton;

	private Vector layerInfo;

	/**
	 * ToolBar按钮响应事件
	 */
	private ActionListener toolBarActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
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
			}
		}
	};

	/**
	 * checkBox相应事件
	 */
	private ActionListener checkBoxActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(mapClipSaveMapPanel.getCheckBox())) {
				if (mapClipSaveMapPanel.getCheckBox().isSelected()) {
					// 当保存地图复选框选中是，给保存地图文本框添加监听，主要判断其内容是否为空
					mapClipSaveMapPanel.getSaveMapTextField().addCaretListener(textFiledCaretListener);
					// 设置确定按钮不可用，此时保存地图文本框为空
					panelButton.getButtonOk().setEnabled(false);
				} else {
					mapClipSaveMapPanel.getSaveMapTextField().removeCaretListener(textFiledCaretListener);
					panelButton.getButtonOk().setEnabled(true);
				}
			} else if (e.getSource().equals(exactClip)) {
				// 获得选中的全部 栅格图层，设置其是否精确裁剪
				int[] tempSelectedRows = mapClipJTable.getSelectedRows();
				for (int i = 0; i < tempSelectedRows.length; i++) {
					Boolean exact = exactClip.isSelected();
					((Vector) layerInfo.get(tempSelectedRows[i])).remove(COLUMN_INDEX_EXACTCLIP);
					((Vector) layerInfo.get(tempSelectedRows[i])).add(COLUMN_INDEX_EXACTCLIP, exact);
				}
			}
		}
	};

	/**
	 * textFiled相应事件
	 */
	private CaretListener textFiledCaretListener = new CaretListener() {
		@Override
		public void caretUpdate(CaretEvent e) {
			if (e.getSource().equals(mapClipSaveMapPanel.getSaveMapTextField())) {
				if (StringUtilities.isNullOrEmpty(mapClipSaveMapPanel.getSaveMapTextField().getText())) {
					panelButton.getButtonOk().setEnabled(false);
				} else {
					panelButton.getButtonOk().setEnabled(true);
				}
			}
		}
	};

	/**
	 * JTableSelection改变事件
	 */
	private ListSelectionListener tableSelectionListener = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			// 当JTable选择的行改变时，需要根据选中行的情况获得精确裁剪是否可用，以及属性值
			initInfo();
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
	 * 确定取消按钮事件
	 */
	private ActionListener OKButtonActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {

		}
	};

	public DialogMapClip(GeoRegion region) {
		super();
		this.geoRegion = region;
		initComponent();
		initLayout();
		initResources();
		registEvents();

		initInfo();

		this.pack();
		this.setSize(new Dimension(650, 450));
		this.setLocationRelativeTo(null);
	}

	/**
	 * 初始化地图裁剪控件属性
	 * 目前主要设置是否精确裁剪，需要根据选中数据的类型决定
	 */
	private void initInfo() {
		// 通过JTable选中行的数据集类型控制其精确裁剪复选框是否可用
		// 当选中行数大于0时才进行方法，防止反选的时候清除选择后报错
		if (this.mapClipJTable.getSelectedRowCount() > 0) {
			Layer selectedLayer = (Layer) this.mapClipJTable.getValueAt(this.mapClipJTable.getSelectedRow(), COLUMN_INDEX_LAYERCAPTION);
			if ((selectedLayer.getDataset().getType()).equals(DatasetType.GRID)) {
				this.exactClip.setEnabled(true);
				// 获得数据
				this.layerInfo = this.mapClipJTable.getMapClipTableModel().getLayerInfo();
				// 获得是否精确裁剪属性信息
				Boolean exactClip = (Boolean) ((Vector) this.layerInfo.get(this.mapClipJTable.getSelectedRow())).get(COLUMN_INDEX_EXACTCLIP);
				// 设置精确裁剪复选框状态属性
				this.exactClip.setSelected(exactClip);
			} else {
				this.exactClip.setSelected(false);
				this.exactClip.setEnabled(false);
			}
		}
	}


	private void initComponent() {
		initToolbar();
		// 地图裁剪JTable
		this.mapClipJTable = new MapClipJTable();
		this.scrollPane = new JScrollPane(this.mapClipJTable);
		// 地图裁剪保存地图面板
		this.mapClipSaveMapPanel = new MapClipSaveMapPanel();
		this.saveMapcompTitledPane = mapClipSaveMapPanel.getCompTitledPane();
		// 地图裁剪影像栅格数据集裁剪设置
		intiClipSetPanel();
		// 确定取消按钮面板
		this.panelButton = new PanelButton();

	}

	private void initToolbar() {
		this.toolBar = new JToolBar();
		this.buttonSelectAll = new JButton();
		this.buttonInvertSelect = new JButton();
		this.buttonSet = new JButton();

		this.toolBar.setFloatable(false);
		this.toolBar.add(this.buttonSelectAll);
		this.toolBar.add(this.buttonInvertSelect);
		this.toolBar.addSeparator();
		this.toolBar.add(this.buttonSet);
	}

	//栅格、影像数据集裁剪设置——布局
	private void intiClipSetPanel() {
		this.clipSetPanel = new JPanel();
		this.exactClip = new JCheckBox();
		GroupLayout clipSetPanelLayout = new GroupLayout(this.clipSetPanel);
		clipSetPanelLayout.setAutoCreateContainerGaps(true);
		clipSetPanelLayout.setAutoCreateGaps(true);
		this.clipSetPanel.setLayout(clipSetPanelLayout);
		//@formatter:off
		clipSetPanelLayout.setHorizontalGroup(clipSetPanelLayout.createParallelGroup()
				.addGroup(clipSetPanelLayout.createSequentialGroup()
						.addComponent(this.exactClip)
						.addGap(5,5,Short.MAX_VALUE)));
		clipSetPanelLayout.setVerticalGroup(clipSetPanelLayout.createSequentialGroup()
				.addGap(20)
				.addComponent(this.exactClip)
				.addGap(5,5,Short.MAX_VALUE));
		//@formatter:on
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
						.addComponent(clipSetPanel))
				.addComponent(this.panelButton));
		panelLayout.setVerticalGroup(panelLayout.createSequentialGroup()
				.addComponent(this.toolBar,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
				.addComponent(this.scrollPane)
				.addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(saveMapcompTitledPane)
						.addComponent(clipSetPanel))
				.addComponent(this.panelButton,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE));
		//@formatter:on

		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.CENTER);
	}

	private void registEvents() {
		removeEvents();
		this.buttonSelectAll.addActionListener(this.toolBarActionListener);
		this.buttonInvertSelect.addActionListener(this.toolBarActionListener);
		this.buttonSet.addActionListener(this.toolBarActionListener);
		this.mapClipJTable.getSelectionModel().addListSelectionListener(this.tableSelectionListener);
		this.mapClipSaveMapPanel.getCheckBox().addActionListener(this.checkBoxActionListener);
		this.exactClip.addActionListener(checkBoxActionListener);
		this.panelButton.getButtonOk().addActionListener(OKButtonActionListener);
		this.panelButton.getButtonCancel().addActionListener(cancelButtonActionListener);

	}

	private void removeEvents() {
		this.buttonSelectAll.removeActionListener(this.toolBarActionListener);
		this.buttonInvertSelect.removeActionListener(this.toolBarActionListener);
		this.buttonSet.removeActionListener(this.toolBarActionListener);
		this.mapClipJTable.getSelectionModel().removeListSelectionListener(this.tableSelectionListener);
		this.mapClipSaveMapPanel.getCheckBox().removeActionListener(this.checkBoxActionListener);
		this.exactClip.removeActionListener(checkBoxActionListener);

		this.panelButton.getButtonOk().removeActionListener(OKButtonActionListener);
		this.panelButton.getButtonCancel().removeActionListener(cancelButtonActionListener);
	}

	private void initResources() {
		this.setTitle(MapViewProperties.getString("String_MapClip_MapClip"));
		this.buttonSelectAll.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectAll.png"));
		this.buttonInvertSelect.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_SelectInverse.png"));
		this.buttonSet.setIcon(CoreResources.getIcon("/coreresources/ToolBar/Image_ToolButton_Setting.PNG"));
		this.buttonSelectAll.setToolTipText(CommonProperties.getString("String_ToolBar_SelectAll"));
		this.buttonInvertSelect.setToolTipText(CommonProperties.getString("String_ToolBar_SelectInverse"));
		this.buttonSet.setToolTipText(CommonProperties.getString("String_ToolBar_SetBatch"));
		this.clipSetPanel.setBorder(BorderFactory.createTitledBorder(MapViewProperties.getString("String_MapClip_Image_ClipSetting")));
		this.exactClip.setText(MapViewProperties.getString("String_MapClip_ExactClip"));
	}
}

