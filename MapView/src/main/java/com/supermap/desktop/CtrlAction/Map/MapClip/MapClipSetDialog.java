package com.supermap.desktop.CtrlAction.Map.MapClip;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.borderPanel.PanelButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author YuanR
 *         地图裁剪统一设置Dialog
 */
public class MapClipSetDialog extends SmDialog {

	private JCheckBox checkBoxAimDataDatasource;
	private DatasourceComboBox aimDataDatasourceComboBox;
	private JCheckBox checkBoxClipMode;
	private JRadioButton insideRegion;
	private JRadioButton outsideRegion;
	private JCheckBox clip;
	private JCheckBox wipeClipRegionCheckBox;
	private JCheckBox exactClippingCheckBox;
	private PanelButton panelButton;

	public static final int DEFAULT_PREFERREDSIZE_GAP = 10;

	private ItemListener checkBoxListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource().equals(checkBoxAimDataDatasource)) {
				aimDataDatasourceComboBox.setEnabled(checkBoxAimDataDatasource.isSelected());
			} else if (e.getSource().equals(checkBoxClipMode)) {
				insideRegion.setEnabled(checkBoxClipMode.isSelected());
				outsideRegion.setEnabled(checkBoxClipMode.isSelected());
			}
		}
	};

	public MapClipSetDialog() {
		initComponent();
		initLayout();
		initResources();
		initListeners();

		this.pack();
		this.setSize(new Dimension(450, this.getPreferredSize().height));
		this.setLocationRelativeTo(null);
	}

	private void initComponent() {
		this.checkBoxAimDataDatasource = new JCheckBox();
		this.aimDataDatasourceComboBox = new DatasourceComboBox();
		this.aimDataDatasourceComboBox.setEnabled(false);
		this.checkBoxClipMode = new JCheckBox();
		this.insideRegion = new JRadioButton();
		this.insideRegion.setEnabled(false);
		this.outsideRegion = new JRadioButton();
		this.outsideRegion.setEnabled(false);

		ButtonGroup mapClipButtonGroup = new ButtonGroup();
		mapClipButtonGroup.add(this.insideRegion);
		mapClipButtonGroup.add(this.outsideRegion);
		this.clip=new JCheckBox();
		this.wipeClipRegionCheckBox = new JCheckBox();
		this.exactClippingCheckBox = new JCheckBox();
		this.panelButton = new PanelButton();

	}

	private void initLayout() {
		JPanel mainPanel = new JPanel();
		GroupLayout panelLayout = new GroupLayout(mainPanel);
		panelLayout.setAutoCreateContainerGaps(true);
		panelLayout.setAutoCreateGaps(true);
		mainPanel.setLayout(panelLayout);

		//@formatter:off
		panelLayout.setHorizontalGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(this.clip)
				.addGroup(panelLayout.createSequentialGroup()
						.addComponent(this.checkBoxAimDataDatasource).addGap(30)
						.addComponent(this.aimDataDatasourceComboBox))
				.addGroup(panelLayout.createSequentialGroup()
						.addComponent(this.checkBoxClipMode).addGap(40)
						.addComponent(this.insideRegion).addGap(30)
						.addComponent(this.outsideRegion))
				.addComponent(this.wipeClipRegionCheckBox)
				.addComponent(this.exactClippingCheckBox));
		panelLayout.setVerticalGroup(panelLayout.createSequentialGroup()
				.addComponent(this.clip).addGap(DEFAULT_PREFERREDSIZE_GAP)
				.addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.checkBoxAimDataDatasource)
						.addComponent(this.aimDataDatasourceComboBox,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)).addGap(DEFAULT_PREFERREDSIZE_GAP)
				.addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.checkBoxClipMode)
						.addComponent(this.insideRegion)
						.addComponent(this.outsideRegion)).addGap(DEFAULT_PREFERREDSIZE_GAP)
				.addComponent(this.wipeClipRegionCheckBox).addGap(DEFAULT_PREFERREDSIZE_GAP)
				.addComponent(this.exactClippingCheckBox)
				.addGap(0,0,Short.MAX_VALUE));

		//@formatter:on
		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(this.panelButton, BorderLayout.SOUTH);
	}

	private void initResources() {
		this.setTitle(MapViewProperties.getString("String_MapClip_GiveSameValue"));
		this.checkBoxAimDataDatasource.setText(ControlsProperties.getString("String_Label_TargetDatasource"));
		this.checkBoxClipMode.setText(MapViewProperties.getString("String_MapClip_ClipTypeD"));
		this.insideRegion.setText(MapViewProperties.getString("String_MapClip_InsideRegion"));
		this.outsideRegion.setText(MapViewProperties.getString("String_MapClip_OutsideRegion"));

		this.clip.setText(MapViewProperties.getString("String_MapClip_IsClip"));
		this.wipeClipRegionCheckBox.setText(MapViewProperties.getString("String_MapClip_EraseCheck"));
		this.exactClippingCheckBox.setText(MapViewProperties.getString("String_MapClip_Image_ExactClip"));
	}

	private void initListeners() {
		removeListeners();
		checkBoxAimDataDatasource.addItemListener(checkBoxListener);
		checkBoxClipMode.addItemListener(checkBoxListener);
	}

	private void removeListeners() {
		checkBoxAimDataDatasource.removeItemListener(checkBoxListener);
		checkBoxClipMode.removeItemListener(checkBoxListener);
	}
}
