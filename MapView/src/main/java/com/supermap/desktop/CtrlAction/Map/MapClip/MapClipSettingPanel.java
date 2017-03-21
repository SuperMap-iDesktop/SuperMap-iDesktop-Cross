package com.supermap.desktop.CtrlAction.Map.MapClip;

import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.DatasourceComboBox;

import javax.swing.*;

/**
 * @author YuanR
 */
public class MapClipSettingPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel clipModePanel;
	private JLabel labelDataset;
	private JLabel labelDatasource;

	private DatasourceComboBox aimDataDatasourceComboBox;
	private JTextField aimDatasetTextField;
	private JTextField saveMapTextField;

	private JCheckBox saveMapCheckBox;
	private JCheckBox wipeClipRegionCheckBox;
	private JCheckBox exactClippingCheckBox;
	private JCheckBox insideRegion;
	private JCheckBox outsideRegion;

	private JLabel xx = new JLabel("xx");


	public MapClipSettingPanel() {
		initComponent();
		initLayout();
		initResources();
	}

	private void initComponent() {
		this.clipModePanel = new JPanel();
		this.labelDatasource = new JLabel("AimDatasource");
		this.labelDataset = new JLabel("AimDataset");

		this.aimDataDatasourceComboBox = new DatasourceComboBox();
		this.aimDatasetTextField = new JTextField("AimDataset");
		this.saveMapTextField = new JTextField("SaveMap");

		this.saveMapCheckBox = new JCheckBox("SaveMap");
		this.wipeClipRegionCheckBox = new JCheckBox("WipeClipRegion");
		this.exactClippingCheckBox = new JCheckBox("ExactClipping");
		this.insideRegion = new JCheckBox("InsideRegion");
		this.outsideRegion = new JCheckBox("OutsideRegion");

	}

	private void initLayout() {
		initClipModePanel();

		// 地图裁剪左半部分布局设计
		JPanel panel = new JPanel();
		GroupLayout panelLayout = new GroupLayout(panel);
//		panelLayout.setAutoCreateContainerGaps(true);
		panelLayout.setAutoCreateGaps(true);
		panel.setLayout(panelLayout);

		//@formatter:off
		panelLayout.setHorizontalGroup(panelLayout.createSequentialGroup()
				.addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.labelDatasource)
						.addComponent(this.labelDataset)
						.addComponent(this.saveMapCheckBox)
						.addComponent(this.wipeClipRegionCheckBox)
						.addComponent(this.exactClippingCheckBox))
				.addGroup(panelLayout.createSequentialGroup()
						.addGroup(panelLayout.createSequentialGroup()
								.addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(this.aimDataDatasourceComboBox)
										.addComponent(this.aimDatasetTextField)
										.addComponent(this.saveMapTextField))
								.addGroup(panelLayout.createSequentialGroup()
										.addGap(5,5,Short.MAX_VALUE)
										.addComponent(this.xx)))
						.addGap(5,5,Short.MAX_VALUE)));

		panelLayout.setVerticalGroup(panelLayout.createSequentialGroup()

				.addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelDatasource)
						.addComponent(this.aimDataDatasourceComboBox))
				.addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelDataset)
						.addComponent(this.aimDatasetTextField))
				.addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.saveMapCheckBox)
						.addComponent(this.xx)
						.addComponent(this.saveMapTextField))
				.addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.wipeClipRegionCheckBox))
				.addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.exactClippingCheckBox))
				.addGap(5,5,Short.MAX_VALUE));

		//@formatter:on


		this.setBorder(BorderFactory.createTitledBorder(MapViewProperties.getString("String_MapClip_GiveSameValue")));

		GroupLayout groupLayout = new GroupLayout(this.clipModePanel);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);

		//@formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addComponent(panel,0,180,Short.MAX_VALUE)
				.addComponent(this.clipModePanel,0,180,Short.MAX_VALUE));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(panel)
						.addComponent(this.clipModePanel)));
		//@formatter:on


	}

	private void initClipModePanel() {
		this.clipModePanel.setBorder(BorderFactory.createTitledBorder(MapViewProperties.getString("String_MapClip_ClipMode")));

		GroupLayout clipModePanelLayout = new GroupLayout(this.clipModePanel);
		clipModePanelLayout.setAutoCreateContainerGaps(true);
		clipModePanelLayout.setAutoCreateGaps(true);
		this.setLayout(clipModePanelLayout);

		//@formatter:off
		clipModePanelLayout.setHorizontalGroup(clipModePanelLayout.createSequentialGroup()
						.addComponent(this.insideRegion)
						.addComponent(this.outsideRegion));
		clipModePanelLayout.setVerticalGroup(clipModePanelLayout.createSequentialGroup()
					.addGroup(clipModePanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.insideRegion)
						.addComponent(this.outsideRegion))
					.addGap(5,5,Short.MAX_VALUE));
		//@formatter:on
	}


	private void initResources() {
		this.labelDatasource.setText(MapViewProperties.getString("String_MapClip_TargetDatasource"));
		this.labelDataset.setText(MapViewProperties.getString("String_MapClip_TargetDataset"));
		this.saveMapCheckBox = new JCheckBox(MapViewProperties.getString("String_MapClip_SaveMap"));
		this.wipeClipRegionCheckBox = new JCheckBox(MapViewProperties.getString("String_MapClip_EraseCheck"));
		this.exactClippingCheckBox = new JCheckBox(MapViewProperties.getString("String_MapClip_ExactClip"));
		this.insideRegion = new JCheckBox(MapViewProperties.getString("String_MapClip_InsideRegion"));
		this.outsideRegion = new JCheckBox(MapViewProperties.getString("String_MapClip_OutsideRegion"));
	}
}
