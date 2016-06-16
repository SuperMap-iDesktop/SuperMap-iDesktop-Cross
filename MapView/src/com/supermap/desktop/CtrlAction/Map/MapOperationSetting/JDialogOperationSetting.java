package com.supermap.desktop.CtrlAction.Map.MapOperationSetting;

import com.supermap.desktop.Application;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilties.SelectionModeUtilities;
import com.supermap.desktop.utilties.StringUtilities;
import com.supermap.ui.MapControl;
import com.supermap.ui.SelectionMode;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 地图操作设置面板
 */
public class JDialogOperationSetting extends SmDialog {

	// region 变量定义
	private FormMap formMap;

	private JCheckBox checkBoxRollingWheelWithoutDelay;
	private JCheckBox checkBoxAutoScroll;
	private JCheckBox checkBoxShowNavigationBar;
	private JCheckBox checkBoxRefreshInInvalidArea;
	private JCheckBox checkBoxRefreshAtTracked;

	private JLabel labelSelectionMode;
	private JComboBox comboBoxSelectionMode;

	private JLabel labelSelectionLerance;
	private SmTextFieldLegit textFieldSelectionLerance;

	private JPanel panelButton;
	private SmButton buttonOk;
	private SmButton buttonCancel;

	private static final String[] SELECTION_MODE_STRINGS = new String[] { SelectionModeUtilities.toString(SelectionMode.CONTAIN_INNER_POINT),
			SelectionModeUtilities.toString(SelectionMode.CONTAIN_OBJECT), SelectionModeUtilities.toString(SelectionMode.INTERSECT) };
	private WindowAdapter windowAdapter;
	private ActionListener actionListener;
	private ActionListener buttonOkListener;

	// endregion

	public JDialogOperationSetting() {
		super();
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof FormMap) {
			this.formMap = (FormMap) activeForm;
		}
		init();
		this.componentList.add(buttonOk);
		this.componentList.add(buttonCancel);
		this.setFocusTraversalPolicy(policy);
	}

	/**
	 * 初始化
	 */
	private void init() {
		initComponents();
		initLayout();
		initListeners();
		// initComponentStates();
		initResources();
	}

	/**
	 * 初始化控件
	 */
	private void initComponents() {
		this.setTitle(MapViewProperties.getString("String_MapBrowseSetting"));
		this.setMinimumSize(new Dimension(300, 200));
		this.setLocationRelativeTo(null);
		this.checkBoxRollingWheelWithoutDelay = new JCheckBox();
		this.checkBoxAutoScroll = new JCheckBox();
		this.checkBoxShowNavigationBar = new JCheckBox();
		this.checkBoxRefreshInInvalidArea = new JCheckBox();
		this.checkBoxRefreshAtTracked = new JCheckBox();

		this.checkBoxRollingWheelWithoutDelay.setPreferredSize(new Dimension(120, 23));
		this.checkBoxAutoScroll.setPreferredSize(new Dimension(120, 23));
		this.checkBoxShowNavigationBar.setPreferredSize(new Dimension(120, 23));
		this.checkBoxRefreshInInvalidArea.setPreferredSize(new Dimension(120, 23));
		this.checkBoxRefreshAtTracked.setPreferredSize(new Dimension(120, 23));

		this.labelSelectionMode = new JLabel();
		this.comboBoxSelectionMode = new JComboBox();
		this.labelSelectionLerance = new JLabel();
		this.textFieldSelectionLerance = new SmTextFieldLegit();

		this.panelButton = new JPanel();
		this.buttonOk = new SmButton();
		this.buttonCancel = new SmButton();

		this.comboBoxSelectionMode.setModel(new DefaultComboBoxModel<>(SELECTION_MODE_STRINGS));
		this.textFieldSelectionLerance.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				if (!StringUtilities.isNullOrEmpty(textFieldValue)) {
					try {
						Double aDouble = Double.valueOf(textFieldValue);
						return aDouble >= 0;
					} catch (Exception e) {
						return false;
					}
				}
				return false;
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				if (StringUtilities.isNullOrEmpty(currentValue)) {
					return "0";
				}
				return backUpValue;
			}
		});
		getRootPane().setDefaultButton(this.buttonOk);
	}

	/**
	 * 初始化布局
	 */
	private void initLayout() {
		initPanelButton();

		JPanel panelCheckBox = new JPanel();
		panelCheckBox.setLayout(new GridBagLayout());
		panelCheckBox.add(checkBoxRollingWheelWithoutDelay,
				new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1));
		panelCheckBox.add(checkBoxAutoScroll, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER)
				.setWeight(1, 1));

		// panelCheckBox.add(checkBoxShowNavigationBar, new GridBagConstraintsHelper(0, 1, 1,
		// 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1));

		panelCheckBox.add(checkBoxRefreshInInvalidArea,
				new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1));
		panelCheckBox.add(checkBoxRefreshAtTracked,
				new GridBagConstraintsHelper(1, 1, 1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1));

		JPanel panelLabel = new JPanel();
		panelLabel.setLayout(new GridBagLayout());
		panelLabel.add(
				labelSelectionMode,
				new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE)
						.setInsets(0, 0, 0, 10));
		panelLabel.add(comboBoxSelectionMode,
				new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL));

		panelLabel.add(
				labelSelectionLerance,
				new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE)
						.setInsets(0, 0, 0, 10));
		panelLabel.add(textFieldSelectionLerance,
				new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL));

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(panelCheckBox, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1));
		panel.add(panelLabel, new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1)
				.setInsets(0, 10, 0, 0));
		panel.add(this.panelButton,
				new GridBagConstraintsHelper(0, 2, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setWeight(1, 0));

		this.getContentPane().setLayout(new GridBagLayout());
		this.getContentPane().add(panel,
				new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setInsets(10));
	}

	private void initPanelButton() {
		this.panelButton.setLayout(new GridBagLayout());

		this.panelButton.add(
				buttonOk,
				new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST)
						.setInsets(0, 0, 0, 5));
		this.panelButton.add(buttonCancel,
				new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.EAST));
	}

	/**
	 * 初始化监听事件
	 */
	private void initListeners() {
		this.buttonOkListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonOkClicked();
			}
		};

		this.actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		};

		this.windowAdapter = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		};

		this.textFieldSelectionLerance.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char keyChar = e.getKeyChar();
				if (keyChar == KeyEvent.VK_ENTER) {
					buttonOkClicked();
				} else if (keyChar != '.' && (keyChar < KeyEvent.VK_0 || keyChar > KeyEvent.VK_9)) {
					e.consume();
				}
			}
		});
		this.buttonOk.addActionListener(this.buttonOkListener);
		this.buttonCancel.addActionListener(this.actionListener);
		this.addWindowListener(this.windowAdapter);
	}

	private void removeListeners() {
		this.buttonOk.removeActionListener(this.buttonOkListener);
		this.buttonCancel.removeActionListener(this.actionListener);
		this.removeWindowListener(this.windowAdapter);
	}

	private void buttonOkClicked() {
		if (this.formMap != null) {
			MapControl mapControl = this.formMap.getMapControl();
			mapControl.setRollingWheelWithoutDelay(!this.checkBoxRollingWheelWithoutDelay.isSelected());
			mapControl.setMarginPanEnabled(this.checkBoxAutoScroll.isSelected());
			// todo 导航条
			mapControl.setRefreshInInvalidArea(this.checkBoxRefreshInInvalidArea.isSelected());
			mapControl.setRefreshAtTracked(this.checkBoxRefreshAtTracked.isSelected());
			SelectionMode selectionMode = SelectionModeUtilities.getValue(String.valueOf(comboBoxSelectionMode.getSelectedItem()));
			if (selectionMode != null) {
				mapControl.setSelectionMode(selectionMode);
			}
			mapControl.setSelectionTolerance(Double.valueOf(textFieldSelectionLerance.getText()));
		}
		this.setDialogResult(DialogResult.OK);
		dispose();
	}

	/**
	 * 初始化控件状态
	 */
	private void initComponentStates() {
		if (formMap != null) {
			MapControl mapControl = formMap.getMapControl();
			this.checkBoxRollingWheelWithoutDelay.setSelected(!mapControl.getRollingWheelWithoutDelay());// 鼠标延迟滚动
			this.checkBoxAutoScroll.setSelected(mapControl.getMarginPanEnabled());// 自动滚屏
			// todo 导航条
			this.checkBoxShowNavigationBar.setSelected(false);
			this.checkBoxShowNavigationBar.setVisible(false);
			this.checkBoxRefreshInInvalidArea.setSelected(mapControl.refreshInInvalidArea());// 局部刷新
			this.checkBoxRefreshAtTracked.setSelected(mapControl.refreshAtTracked());// 编辑实时更新
			this.comboBoxSelectionMode.setSelectedItem(SelectionModeUtilities.toString(mapControl.getSelectionMode()));// 选择模式
			this.textFieldSelectionLerance.setText(String.valueOf(mapControl.getSelectionTolerance()));// 点选容限

		}
	}

	/**
	 * 资源化
	 */
	private void initResources() {
		this.checkBoxRollingWheelWithoutDelay.setText(MapViewProperties.getString("String_CheckBox_RollingWheelWithoutDelay"));
		this.checkBoxAutoScroll.setText(MapViewProperties.getString("String_CheckBox_MarginPan"));
		this.checkBoxShowNavigationBar.setText(MapViewProperties.getString("String_MapProperty_ShowNavigationBar") + "*");
		this.checkBoxRefreshInInvalidArea.setText(MapViewProperties.getString("String_MapProperty_RefreshInInvalidArea"));
		this.checkBoxRefreshAtTracked.setText(MapViewProperties.getString("String_MapProperty_RefreshAtTracked"));

		this.labelSelectionMode.setText(MapViewProperties.getString("String_Label_SelectionMode"));
		this.labelSelectionLerance.setText(MapViewProperties.getString("String_Label_SelectionTolerance"));

		this.buttonOk.setText(CommonProperties.getString(CommonProperties.OK));
		this.buttonCancel.setText(CommonProperties.getString(CommonProperties.Cancel));
	}

	@Override
	public DialogResult showDialog() {
		initComponentStates();
		return super.showDialog();
	}

	@Override
	public void dispose() {
		removeListeners();
		super.dispose();
	}

}
