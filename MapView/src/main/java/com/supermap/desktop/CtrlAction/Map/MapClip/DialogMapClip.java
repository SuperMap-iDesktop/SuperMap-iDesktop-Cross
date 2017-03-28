package com.supermap.desktop.CtrlAction.Map.MapClip;

import com.supermap.data.GeoRegion;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.ComponentBorderPanel.CompTitledPane;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.borderPanel.PanelButton;
import com.supermap.desktop.utilities.CoreResources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

	public DialogMapClip(GeoRegion region) {
		super();
		this.geoRegion = region;
		initComponent();
		initLayout();
		initResources();
		registEvents();

		this.pack();
		this.setSize(new Dimension(650, 450));
		this.setLocationRelativeTo(null);
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
				.addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
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
	}

	private void removeEvents() {
		this.buttonSelectAll.removeActionListener(this.toolBarActionListener);
		this.buttonInvertSelect.removeActionListener(this.toolBarActionListener);
		this.buttonSet.removeActionListener(this.toolBarActionListener);
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

