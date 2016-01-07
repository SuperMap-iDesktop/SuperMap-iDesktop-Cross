package com.supermap.desktop.ui.controls;

import com.supermap.data.SymbolType;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * 符号基本信息面板
 *
 * @author xuzw
 */
class SymbolBasicInfoPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private transient LayersTree layersTree = null;

	private JLabel jLabelSymbolID;
	private JLabel jLabelSymbolName;

	private JComboBox jComboBoxSymbolID;
	private JComboBox jComboBoxSymbolName;

	private JTextField jTextFieldSymbolID;
	private JTextField jTextFieldSymbolName;

	private JPanel jPanelSymbolInfo;

	private transient SymbolPanel jPanelSymbol;

	// symbolIDComboBox的监听器
	private transient ActionListener symbolIDListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			abstractSymbolIDLisenter();
		}
	};

	private void abstractSymbolIDLisenter() {
		try {
			String idStr = jComboBoxSymbolID.getSelectedItem().toString();
			if (idStr.trim().length() == 0) {
				jTextFieldSymbolID.setText("");
				jTextFieldSymbolName.setText("");
				return;
			}
			try {
				Integer integer = Integer.valueOf(idStr.trim());
			} catch (NumberFormatException e) {
				jTextFieldSymbolID.setText("");
				jTextFieldSymbolName.setText("");
				return;
			}
			Integer id = Integer.valueOf(idStr.trim());
			LabelInfo labelInfo = jPanelSymbol.getLabelInfoBySymbolID(id);
			if (labelInfo != null) {
				jPanelSymbol.changeCurrentLabel(labelInfo.getLabel());
				jTextFieldSymbolName.setText(labelInfo.getSymbolName());

				Rectangle rectangle = jPanelSymbol.getCellRect(labelInfo.getRow(), labelInfo.getColumn());
				if (rectangle != null) {
					jPanelSymbol.getPanelSymbolsView().scrollRectToVisible(rectangle);
				}
			} else {
				jTextFieldSymbolID.setText("");
				jTextFieldSymbolName.setText("");
			}
		} catch (Exception ex) {
			jTextFieldSymbolID.setText("");
			jTextFieldSymbolName.setText("");
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	// symbolNameComboBox对应的监听器
	private transient ActionListener m_symbolNameListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			abstractSymbolNameListener();
		}
	};

	private void abstractSymbolNameListener() {
		int index = jComboBoxSymbolName.getSelectedIndex();
		if (index == -1) {
			String nameStr = jComboBoxSymbolName.getSelectedItem().toString();
			if (nameStr.trim().length() == 0) {
				jTextFieldSymbolID.setText("");
				jTextFieldSymbolName.setText("");
				return;
			}

			LabelInfo labelInfo = jPanelSymbol.getLabelInfoBySymbolName(nameStr);
			if (labelInfo != null) {
				jPanelSymbol.changeCurrentLabel(labelInfo.getLabel());
				jTextFieldSymbolID.setText(String.valueOf(labelInfo.getSymbolID()));

				Rectangle rectangle = jPanelSymbol.getCellRect(labelInfo.getRow(), labelInfo.getColumn());
				if (rectangle != null) {
					jPanelSymbol.getPanelSymbolsView().scrollRectToVisible(rectangle);
				}
			} else {
				jTextFieldSymbolID.setText("");
				jTextFieldSymbolName.setText("");
			}
		} else {
			LabelInfo labelInfo = jPanelSymbol.getLabelInfoArray().get(index);
			if (labelInfo != null) {
				jPanelSymbol.changeCurrentLabel(labelInfo.getLabel());
				jTextFieldSymbolID.setText(String.valueOf(labelInfo.getSymbolID()));

				Rectangle rectangle = jPanelSymbol.getCellRect(labelInfo.getRow(), labelInfo.getColumn());
				if (rectangle != null) {
					jPanelSymbol.getPanelSymbolsView().scrollRectToVisible(rectangle);
				}
			} else {
				jTextFieldSymbolID.setText("");
				jTextFieldSymbolName.setText("");
			}
		}
	}

	public SymbolBasicInfoPanel(SymbolPanel symbolPanel) {
		super();
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(175, 80));
		setMinimumSize(new Dimension(175, 80));
		setMaximumSize(new Dimension(250, 80));
		jPanelSymbol = symbolPanel;
		add(getBasicInfoPanel(), BorderLayout.CENTER);
	}

	protected JPanel getBasicInfoPanel() {
		if (jPanelSymbolInfo == null) {
			jPanelSymbolInfo = new JPanel();

			jPanelSymbolInfo.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(Color.gray, 1),
					ControlsProperties.getString("String_BasicInfo"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
			jPanelSymbolInfo.setLayout(new GridBagLayout());
			jLabelSymbolName = new JLabel();
			jLabelSymbolName.setText(ControlsProperties.getString("String_Label_SymbolName"));
			jLabelSymbolID = new JLabel();
			jLabelSymbolID.setText(ControlsProperties.getString("String_Label_SymbolID"));

			// 网格组布局可以实现任意大小
			GridBagConstraints constrains = new GridBagConstraints();
			constrains.weightx = 100;
			constrains.weighty = 100;
			constrains.fill = GridBagConstraints.WEST;
			constrains.anchor = GridBagConstraints.WEST;
			constrains.gridx = 0;
			constrains.gridy = 0;
			jPanelSymbolInfo.add(jLabelSymbolID, constrains);
			constrains.gridx = 0;
			constrains.gridy = 1;
			jPanelSymbolInfo.add(jLabelSymbolName, constrains);
			constrains.gridx = 1;
			constrains.gridy = 0;
			jPanelSymbolInfo.add(getSymbolIDComboBox(), constrains);
			constrains.gridx = 1;
			constrains.gridy = 1;
			jPanelSymbolInfo.add(getSymbolNameComboBox(), constrains);
		}
		return jPanelSymbolInfo;
	}

	protected JComboBox getSymbolIDComboBox() {
		if (jComboBoxSymbolID == null) {
			jComboBoxSymbolID = new JComboBox();
			jComboBoxSymbolID.setEditable(true);
			if (jPanelSymbol.getType().equals(SymbolType.FILL)) {
				jComboBoxSymbolID.setPreferredSize(new Dimension(155, 20));
			} else {
				jComboBoxSymbolID.setPreferredSize(new Dimension(100, 20));
			}
			jTextFieldSymbolID = (JTextField) jComboBoxSymbolID.getEditor().getEditorComponent();
		}
		return jComboBoxSymbolID;
	}

	protected JComboBox getSymbolNameComboBox() {
		if (jComboBoxSymbolName == null) {
			jComboBoxSymbolName = new JComboBox();
			if (jPanelSymbol.getType().equals(SymbolType.FILL)) {
				jComboBoxSymbolName.setPreferredSize(new Dimension(155, 20));
			} else {
				jComboBoxSymbolName.setPreferredSize(new Dimension(100, 20));
			}
			jComboBoxSymbolName.setEditable(true);
			jTextFieldSymbolName = (JTextField) jComboBoxSymbolName.getEditor().getEditorComponent();
		}
		return jComboBoxSymbolName;
	}

	/**
	 * 刷新两个JComboBox，先移除监听，避免操作两个JComboBox时不停的触发事件
	 *
	 * @param arrayList
	 */
	public void refreshComboBox(ArrayList<LabelInfo> arrayList) {
		jComboBoxSymbolID.removeActionListener(symbolIDListener);
		jComboBoxSymbolName.removeActionListener(m_symbolNameListener);
		jComboBoxSymbolID.removeAllItems();
		jComboBoxSymbolName.removeAllItems();
		for (int i = 0; i < arrayList.size(); i++) {
			LabelInfo labelInfo = arrayList.get(i);
			jComboBoxSymbolID.addItem(String.valueOf(labelInfo.getSymbolID()));
			jComboBoxSymbolName.addItem(labelInfo);
		}
		jComboBoxSymbolID.addActionListener(symbolIDListener);
		jComboBoxSymbolName.addActionListener(m_symbolNameListener);
	}

	/**
	 * 刷新基本信息面板
	 *
	 * @param symbolID
	 * @param symbolName
	 */
	public void refreshBasicInfo(int symbolID, String symbolName) {
		jTextFieldSymbolID.setText(String.valueOf(symbolID));
		jTextFieldSymbolName.setText(symbolName);
	}

	public LayersTree getLayersTree() {
		return layersTree;
	}

	public void setLayersTree(LayersTree layersTree) {
		this.layersTree = layersTree;
	}
}
