package com.supermap.desktop.CtrlAction.settings;

import com.supermap.desktop.Application;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.frame.FrameProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * 选项...
 *
 * @author XiaJT
 */
public class JDialogSettings extends SmDialog {
	public static final String[] lists = new String[]{
			FrameProperties.getString("String_Option_Common"),
			FrameProperties.getString("String_Option_Environment"),
			FrameProperties.getString("String_Option_Server"),
			FrameProperties.getString("String_Option_Edit"),
			FrameProperties.getString("String_Option_Help"),
			FrameProperties.getString("String_Resources"),

	};

	private JList<String> jList = new JList<>();
	private JPanel panelRight = new JPanel();
	private JPanel panelButtons = new JPanel();
	private SmButton smButtonOk = new SmButton();
	private SmButton smButtonCancle = new SmButton();

	private HashMap<String, BaseSettingPanel> rightPanels = new HashMap<>();
	private DefaultListModel<String> listModel;

	public JDialogSettings() {
		init();
	}

	private void init() {
		initComponents();
		initLayout();
		initListeners();
		initResource();
		initComponentState();
	}

	private void initComponents() {
		this.setSize(600, 400);
		this.setLocationRelativeTo(null);
		this.setTitle(CoreProperties.getString("String_MessageBox_Title"));
		listModel = new DefaultListModel<>();
		jList.setModel(listModel);
		jList.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel listCellRendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				listCellRendererComponent.setHorizontalAlignment(CENTER);
				listCellRendererComponent.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
				return listCellRendererComponent;
			}
		});
		for (String list : lists) {
			if (getRightPanel(list) != null) {
				listModel.addElement(list);
			}
		}
	}

	private void initLayout() {
		panelButtons.setLayout(new GridBagLayout());
		panelButtons.add(smButtonOk, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE));
		panelButtons.add(smButtonCancle, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.NONE).setInsets(0, 5, 0, 0));

		panelRight.setLayout(new GridBagLayout());

		this.setLayout(new GridBagLayout());
		this.add(jList, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.CENTER).setInsets(10, 10, 0, 0).setFill(GridBagConstraints.BOTH).setIpad(50, 0));
		this.add(panelRight, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(10, 5, 0, 10).setFill(GridBagConstraints.BOTH));
		this.add(panelButtons, new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(5, 10, 10, 10).setFill(GridBagConstraints.BOTH));
	}

	private void initListeners() {
		jList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				panelRight.removeAll();
				panelRight.add(getRightPanel(jList.getSelectedValue()), new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
			}
		});
		smButtonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (BaseSettingPanel iSetting : rightPanels.values()) {
					iSetting.apply();
					iSetting.dispose();
				}
				GlobalParameters.save();
				dispose();
			}
		});
		smButtonCancle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (BaseSettingPanel iSetting : rightPanels.values()) {
					iSetting.dispose();
				}
				dispose();
			}
		});
	}

	private void initResource() {
		smButtonOk.setText(CommonProperties.getString(CommonProperties.OK));
		smButtonCancle.setText(CommonProperties.getString(CommonProperties.Cancel));
	}

	private void initComponentState() {
		jList.clearSelection();
		jList.setSelectedIndex(0);
	}

	private BaseSettingPanel getRightPanel(String panelName) {
		if (rightPanels.get(panelName) != null) {
			return rightPanels.get(panelName);
		}
		if (panelName.equals(FrameProperties.getString("String_Option_Common"))) {
			JPanelSettingCommon jPanelSettingCommon = new JPanelSettingCommon();
			rightPanels.put(FrameProperties.getString("String_Option_Common"), jPanelSettingCommon);
			return jPanelSettingCommon;
		}
		if (panelName.equals(FrameProperties.getString("String_Option_Environment"))) {
			JPanelSettingEnvironment jPanelSettingEnvironment = new JPanelSettingEnvironment();
			rightPanels.put(FrameProperties.getString("String_Option_Environment"), jPanelSettingEnvironment);
			return jPanelSettingEnvironment;
		}
		if (panelName.equals(FrameProperties.getString("String_Option_Server"))) {
			JPanelSettingServer jPanelSettingServer = new JPanelSettingServer();
			rightPanels.put(FrameProperties.getString("String_Option_Server"), jPanelSettingServer);
			return jPanelSettingServer;
		}
		if (panelName.equals(FrameProperties.getString("String_Option_Edit"))) {
			JPanelSettingEdit panelSettingEdit = new JPanelSettingEdit();
			rightPanels.put(FrameProperties.getString("String_Option_Edit"), panelSettingEdit);
			return panelSettingEdit;
		}
		if (panelName.equals(FrameProperties.getString("String_Option_Help"))) {
			JPanelSettingHelp panelSettingHelp = new JPanelSettingHelp();
			rightPanels.put(FrameProperties.getString("String_Resources"), panelSettingHelp);
			return panelSettingHelp;
		}
		if (panelName.equals(FrameProperties.getString("String_Resources"))) {
			JPanelSettingResources jPanelSettingResources = new JPanelSettingResources();
			rightPanels.put(FrameProperties.getString("String_Resources"), jPanelSettingResources);
			return jPanelSettingResources;
		}
		Application.getActiveApplication().getOutput().output(panelName + "don't have panel.");
		return new EmptySetting();
	}

	private class EmptySetting extends BaseSettingPanel {


		@Override
		public void apply() {

		}

		@Override
		public void dispose() {

		}
	}

}
