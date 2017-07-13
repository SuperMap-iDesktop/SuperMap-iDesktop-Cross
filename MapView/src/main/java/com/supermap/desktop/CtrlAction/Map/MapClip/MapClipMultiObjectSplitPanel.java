package com.supermap.desktop.CtrlAction.Map.MapClip;

import com.supermap.desktop.implement.SmComboBox;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.ComponentBorderPanel.CompTitledPane;
import com.supermap.desktop.ui.controls.ProviderLabel.WarningOrHelpProvider;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by lixiaoyao on 2017/7/4.
 */
public class MapClipMultiObjectSplitPanel extends JPanel {
	private CompTitledPane compTitledPane;
	private JCheckBox multiObjectSplitCheckBox;
	private JLabel resultCaptionLabel;
	private JComboBox fieldCaptionCombobox;
	private WarningOrHelpProvider warningOrHelpProvider;

	public MapClipMultiObjectSplitPanel(boolean isEnabled) {
		initComponent();
		initLayout();
		initResources();
		setPanelComponetsEnable(isEnabled);
		removeEvents();
		registerEvents();
	}

	private void initComponent() {
		this.multiObjectSplitCheckBox = new JCheckBox("MutiObjectSplit");
		this.resultCaptionLabel = new JLabel("ResultCaption");
		this.fieldCaptionCombobox = new JComboBox();
		this.compTitledPane = new CompTitledPane(this.multiObjectSplitCheckBox, this);
		this.warningOrHelpProvider = new WarningOrHelpProvider(MapViewProperties.getString("String_MapClip_MutiObjectClipTip"), false);
	}

	private void initLayout() {
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);

		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addComponent(this.resultCaptionLabel)
				.addComponent(this.warningOrHelpProvider)
				.addComponent(this.fieldCaptionCombobox));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(this.resultCaptionLabel)
				.addComponent(this.warningOrHelpProvider)
				.addComponent(this.fieldCaptionCombobox, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE));
	}

	private void initResources() {
		this.multiObjectSplitCheckBox.setText(MapViewProperties.getString("String_MapClip_MutiObjectSplit"));
		this.resultCaptionLabel.setText(MapViewProperties.getString("String_MapClip_ResultCaptionLabel"));
	}

	private void registerEvents() {
		this.multiObjectSplitCheckBox.addActionListener(this.isMutiObjectListener);
	}

	private void removeEvents() {
		this.multiObjectSplitCheckBox.removeActionListener(this.isMutiObjectListener);
	}

	public void setPanelComponetsEnable(boolean isEnabled) {
		this.multiObjectSplitCheckBox.setEnabled(isEnabled);
		setLabelAndComboboxEnable();
	}

	private void setLabelAndComboboxEnable() {
		if (this.multiObjectSplitCheckBox.isEnabled() && this.multiObjectSplitCheckBox.isSelected()) {
			this.resultCaptionLabel.setEnabled(true);
			this.fieldCaptionCombobox.setEnabled(true);
		} else {
			this.resultCaptionLabel.setEnabled(false);
			this.fieldCaptionCombobox.setEnabled(false);
		}
	}

	public void setFieldCaption(String fieldCaptions[]) {
		if (this.multiObjectSplitCheckBox.isEnabled()) {
			for (int i = 0; i < fieldCaptions.length; i++) {
				this.fieldCaptionCombobox.addItem(fieldCaptions[i]);
			}
		}
	}

	public String getCurrentSelectedFieldCaption() {
		if (this.multiObjectSplitCheckBox.isEnabled() && this.multiObjectSplitCheckBox.isSelected()) {
			return this.fieldCaptionCombobox.getSelectedItem().toString();
		} else {
			return "";
		}
	}

	public CompTitledPane getCompTitledPane() {
		return this.compTitledPane;
	}

	private ActionListener isMutiObjectListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			setLabelAndComboboxEnable();
		}
	};
}
