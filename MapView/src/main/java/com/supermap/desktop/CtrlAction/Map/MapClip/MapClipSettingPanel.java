package com.supermap.desktop.CtrlAction.Map.MapClip;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.ProviderLabel.WarningOrHelpProvider;

import javax.swing.*;

/**
 * @author YuanR
 *         2017.3.22
 *         夭折的地图裁剪设置面板
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
	private JRadioButton insideRegion;
	private JRadioButton outsideRegion;

	private WarningOrHelpProvider warningOrHelpProvider;

	public static final int DEFAULT_PREFERREDSIZE_GAP = 5;

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
		this.aimDatasetTextField = new JTextField();
		this.saveMapTextField = new JTextField();

		this.saveMapCheckBox = new JCheckBox("SaveMap");
		this.wipeClipRegionCheckBox = new JCheckBox("WipeClipRegion");
		this.exactClippingCheckBox = new JCheckBox("ExactClipping");
		this.insideRegion = new JRadioButton("InsideRegion");
		this.outsideRegion = new JRadioButton("OutsideRegion");

		ButtonGroup mapClipButtonGroup = new ButtonGroup();
		mapClipButtonGroup.add(this.insideRegion);
		mapClipButtonGroup.add(this.outsideRegion);

		this.warningOrHelpProvider = new WarningOrHelpProvider(MapViewProperties.getString("String_MapClip_SaveMap_Info"), false);
	}

	private void initLayout() {
		initClipModePanel();

		// 地图裁剪左半部分布局设计
		JPanel panel = new JPanel();
		GroupLayout panelLayout = new GroupLayout(panel);
		panelLayout.setAutoCreateContainerGaps(true);
		panelLayout.setAutoCreateGaps(true);
		panel.setLayout(panelLayout);

		//@formatter:off
		panelLayout.setHorizontalGroup(panelLayout.createSequentialGroup()
				.addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.labelDatasource)
						.addComponent(this.labelDataset)
						.addGroup(panelLayout.createSequentialGroup()
								.addComponent(this.saveMapCheckBox)
								.addComponent(this.warningOrHelpProvider))
						.addComponent(this.wipeClipRegionCheckBox)
						.addComponent(this.exactClippingCheckBox))
				.addGroup(panelLayout.createSequentialGroup()
						.addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(this.aimDataDatasourceComboBox)
								.addComponent(this.aimDatasetTextField)
								.addComponent(this.saveMapTextField))));

		panelLayout.setVerticalGroup(panelLayout.createSequentialGroup()

				.addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelDatasource)
						.addComponent(this.aimDataDatasourceComboBox,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)).addGap(DEFAULT_PREFERREDSIZE_GAP)
				.addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelDataset)
						.addComponent(this.aimDatasetTextField,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)).addGap(DEFAULT_PREFERREDSIZE_GAP)
				.addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.saveMapCheckBox)
						.addComponent(this.warningOrHelpProvider)
						.addComponent(this.saveMapTextField,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)).addGap(DEFAULT_PREFERREDSIZE_GAP)
				.addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.wipeClipRegionCheckBox)).addGap(DEFAULT_PREFERREDSIZE_GAP)
				.addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.exactClippingCheckBox)).addGap(DEFAULT_PREFERREDSIZE_GAP)
				.addGap(5,5,Short.MAX_VALUE));

		//@formatter:on


		this.setBorder(BorderFactory.createTitledBorder(MapViewProperties.getString("String_MapClip_GiveSameValue")));

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);

		//@formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addComponent(panel,0,60,Short.MAX_VALUE)
				.addComponent(this.clipModePanel,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE));
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
		this.clipModePanel.setLayout(clipModePanelLayout);

		//@formatter:off
		clipModePanelLayout.setHorizontalGroup(clipModePanelLayout.createSequentialGroup()
						.addComponent(this.insideRegion).addGap(60)
						.addComponent(this.outsideRegion).addGap(60));
		clipModePanelLayout.setVerticalGroup(clipModePanelLayout.createSequentialGroup()
					.addGroup(clipModePanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.insideRegion)
						.addComponent(this.outsideRegion))
					.addGap(0,0,Short.MAX_VALUE));
		//@formatter:on
	}

	private void initResources() {

		this.labelDatasource.setText(ControlsProperties.getString("String_Label_TargetDatasource"));
		this.labelDataset.setText(ControlsProperties.getString("String_Label_TargetDataset"));
		this.saveMapCheckBox.setText(MapViewProperties.getString("String_MapClip_SaveMap"));
		this.wipeClipRegionCheckBox.setText(MapViewProperties.getString("String_MapClip_EraseCheck"));
		this.exactClippingCheckBox.setText(MapViewProperties.getString("String_MapClip_ExactClip"));
		this.insideRegion.setText(MapViewProperties.getString("String_MapClip_InsideRegion"));
		this.outsideRegion.setText(MapViewProperties.getString("String_MapClip_OutsideRegion"));
	}
}
