package com.supermap.desktop.CtrlAction.Map.MapClip;

import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.ComponentBorderPanel.CompTitledPane;
import com.supermap.desktop.ui.controls.ProviderLabel.WarningOrHelpProvider;
import com.supermap.desktop.Interface.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author YuanR
 *         地图裁剪功能—保存地图面板
 */
public class MapClipSaveMapPanel extends JPanel {


	private CompTitledPane compTitledPane;
	private JCheckBox resultMapCheckBox;
	private JLabel resultMapCaption;
	private WarningOrHelpProvider warningOrHelpProvider;
	private SmTextFieldLegit saveMapTextField;
	private ArrayList<SaveMapStatusChangedListener> listeners = new ArrayList<>();

	public String getMapName() {
		if (saveMapTextField == null) {
			return null;
		}
		return saveMapTextField.getText();
	}

	public void setMapName(String name) {
		if (saveMapTextField != null) {
			saveMapTextField.setText(name);
		}
	}

	public boolean isSaveMap() {
		if (resultMapCheckBox != null) {
			return resultMapCheckBox.isSelected();
		}
		return false;
	}

	public void setSaveMap(boolean isSave) {
		if (resultMapCheckBox != null) {
			resultMapCheckBox.setSelected(isSave);
		}
	}


	public boolean addSaveMapStatusChangedListener(SaveMapStatusChangedListener listener) {
		if (listener != null) {
			return listeners.add(listener);
		}
		return false;
	}

	public boolean removeSaveMapStatusChangedListener(SaveMapStatusChangedListener listener) {
		return listeners.remove(listener);
	}

	private void fireStatusChangedEvent() {
		for (SaveMapStatusChangedListener listener : listeners) {
			listener.statusChange();
		}
	}

	private ActionListener checkBoxActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (resultMapCheckBox.isSelected()) {
				saveMapTextField.setEnabled(true);
				if (StringUtilities.isNullOrEmpty(getMapName())) {
					String tempMapName = getMapName();
					if (tempMapName == null || tempMapName.length() <= 0) {
						tempMapName = "MapClip";
					}
					tempMapName = MapUtilities.getAvailableMapName(tempMapName, true);
					setMapName(tempMapName);
				}
			} else {
				saveMapTextField.setEnabled(false);
			}
			fireStatusChangedEvent();
		}
	};

	private DocumentListener mapNameTextListener = new DocumentListener() {
		@Override
		public void insertUpdate(DocumentEvent e) {
			fireStatusChangedEvent();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			fireStatusChangedEvent();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			fireStatusChangedEvent();
		}
	};

	public boolean isLegalStatus() {
		if (!this.resultMapCheckBox.isSelected()) {
			return true;
		}
		if (this.saveMapTextField.isLegitValue(saveMapTextField.getText())) {
			return true;
		}
		return false;
	}


	public CompTitledPane getCompTitledPane() {
		return compTitledPane;
	}

	public MapClipSaveMapPanel() {
		initComponent();
		initLayout();
		initResources();
		registEvents();

	}

	private void initComponent() {
		this.resultMapCheckBox = new JCheckBox("ResultMap");
		this.resultMapCaption = new JLabel("ResultMapCaption");
		this.warningOrHelpProvider = new WarningOrHelpProvider(MapViewProperties.getString("String_MapClip_SaveMap_Info"), false);
		this.saveMapTextField = new SmTextFieldLegit();
		saveMapTextField.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldVaue) {
				String name = saveMapTextField.getText();
				if (name == null || name.length() == 0) {
					return false;
				}
				// 用获得的文件名
				String tempText = MapUtilities.getAvailableMapName(name, true);
				return tempText.equals(name);
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				String name = saveMapTextField.getText();
				if (name == null || name.length() == 0) {
					name = "MapClip";
				}
				return MapUtilities.getAvailableMapName(name, true);
			}
		});
		this.saveMapTextField.setEnabled(false);
		this.compTitledPane = new CompTitledPane(this.resultMapCheckBox, this);
	}

	private void initLayout() {
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);

		//@formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addComponent(resultMapCaption)
				.addComponent(this.warningOrHelpProvider)
				.addComponent(this.saveMapTextField));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(resultMapCaption)
				.addComponent(this.warningOrHelpProvider)
				.addComponent(this.saveMapTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE));
		//@formatter:on

	}

	private void initResources() {
		this.resultMapCheckBox.setText(MapViewProperties.getString("String_MapClip_SaveMap"));
		this.resultMapCaption.setText(MapViewProperties.getString("String_MapClip_ResultMapCaption"));
	}

	private void registEvents() {
		removeEvents();
		this.resultMapCheckBox.addActionListener(this.checkBoxActionListener);
		this.saveMapTextField.getDocument().addDocumentListener(mapNameTextListener);
	}

	private void removeEvents() {
		this.resultMapCheckBox.removeActionListener(this.checkBoxActionListener);
		this.saveMapTextField.getDocument().removeDocumentListener(mapNameTextListener);
	}
}
