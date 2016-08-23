package com.supermap.desktop.CtrlAction.settings;

import com.supermap.desktop.CtrlAction.WorkspaceTempSave;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.frame.FrameProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * 常用选项
 *
 * @author XiaJT
 */
public class JPanelSettingCommon extends BaseSettingPanel {

	/**
	 * 自动新建窗口浏览数据集数据
	 */
	private JCheckBox checkBoxShowDataInNowWindow;
	/**
	 * 关闭窗口提示保存
	 */
	private JCheckBox checkBoxIsShowFormClosingInfo;
	/**
	 * 工作空间关闭提示保存
	 */
	private JCheckBox checkBoxWorkspaceCloseNotify;
	/**
	 * 关闭工作空间提示内存数据源
	 */
	private JCheckBox checkBoxCloseMemoryDatasourceNotify;
	/**
	 * 自动关闭没有图层的地图窗口
	 */
	private JCheckBox checkBoxIsAutoCloseEmptyMap;

//	/**
//	 * 自动保存工作空间暂时关闭
//	 */
//	private JCheckBox checkBoxAutoSaveWorkspace;

//	private JLabel labelAutoSaveTime;
//	private SmTextFieldLegit smTextFieldLegitAutoSaveTime;
//	private JLabel labelAutoSaveTimeUnit;

	/**
	 * 工作空间崩溃恢复
	 */
	private JCheckBox checkBoxWorkspaceRecovery;
//	private JPanel panelWorkspaceRecovery;
//	private CompTitledPane compTitledPane;
//	private JCheckBox checkBoxSymbolLibraryRecovery;
//	private SmTextFieldLegit smTextFieldLegitSymbolSaveTime;
//	private JLabel labelSymbolSaveTimeUnit;

	private ItemListener itemListener;

	@Override
	protected void initComponents() {
		checkBoxShowDataInNowWindow = new JCheckBox();
		checkBoxIsShowFormClosingInfo = new JCheckBox();
		checkBoxWorkspaceCloseNotify = new JCheckBox();
		checkBoxCloseMemoryDatasourceNotify = new JCheckBox();
		checkBoxIsAutoCloseEmptyMap = new JCheckBox();
//		checkBoxAutoSaveWorkspace = new JCheckBox();
//		labelAutoSaveTime = new JLabel();
//		smTextFieldLegitAutoSaveTime = new SmTextFieldLegit();
//		labelAutoSaveTimeUnit = new JLabel();

//		Dimension size = new Dimension(100, 23);
//		smTextFieldLegitAutoSaveTime.setPreferredSize(size);
//		smTextFieldLegitAutoSaveTime.setMinimumSize(size);
//		smTextFieldLegitAutoSaveTime.setMaximumSize(size);


//		panelWorkspaceRecovery = new JPanel();
		checkBoxWorkspaceRecovery = new JCheckBox();
//		compTitledPane = new CompTitledPane(checkBoxWorkspaceRecovery, panelWorkspaceRecovery);
//		checkBoxSymbolLibraryRecovery = new JCheckBox();
//		smTextFieldLegitSymbolSaveTime = new SmTextFieldLegit();
//		labelSymbolSaveTimeUnit = new JLabel();
//
//		smTextFieldLegitSymbolSaveTime.setPreferredSize(size);
//		smTextFieldLegitSymbolSaveTime.setMinimumSize(size);
//		smTextFieldLegitSymbolSaveTime.setMaximumSize(size);

		this.setBorder(BorderFactory.createTitledBorder(FrameProperties.getString("String_CaptionOperate")));
		itemListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Object source = e.getSource();
				if (source != null && source instanceof Component) {
					if (changedValues.contains(source)) {
						changedValues.remove(source);
					} else {
						changedValues.add((Component) source);
					}
				}
			}
		};
	}

	@Override
	protected void initLayout() {
//		panelWorkspaceRecovery.setLayout(new GridBagLayout());
//		panelWorkspaceRecovery.add(checkBoxSymbolLibraryRecovery, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 0, 0));
//		panelWorkspaceRecovery.add(smTextFieldLegitSymbolSaveTime, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 0, 0));
//		panelWorkspaceRecovery.add(labelSymbolSaveTimeUnit, new GridBagConstraintsHelper(2, 0, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 0, 0));
//		panelWorkspaceRecovery.add(new JPanel(), new GridBagConstraintsHelper(3, 0, 1, 1).setWeight(1, 1));

		this.setLayout(new GridBagLayout());
		this.add(checkBoxShowDataInNowWindow, new GridBagConstraintsHelper(0, 0, 3, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));
		this.add(checkBoxIsShowFormClosingInfo, new GridBagConstraintsHelper(0, 1, 3, 1).setWeight(1, 0).setInsets(5, 0, 0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));
		this.add(checkBoxWorkspaceCloseNotify, new GridBagConstraintsHelper(0, 2, 3, 1).setWeight(1, 0).setInsets(5, 0, 0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));
		this.add(checkBoxCloseMemoryDatasourceNotify, new GridBagConstraintsHelper(0, 3, 3, 1).setWeight(1, 0).setInsets(5, 0, 0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));
		this.add(checkBoxIsAutoCloseEmptyMap, new GridBagConstraintsHelper(0, 4, 3, 1).setWeight(1, 0).setInsets(5, 0, 0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));
		this.add(checkBoxWorkspaceRecovery, new GridBagConstraintsHelper(0, 5, 3, 1).setWeight(1, 0).setInsets(5, 0, 0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));

//		this.add(checkBoxAutoSaveWorkspace, new GridBagConstraintsHelper(0, 6, 3, 1).setWeight(1, 0).setInsets(5, 0, 0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));
//
//		this.add(labelAutoSaveTime, new GridBagConstraintsHelper(0, 7, 1, 1).setWeight(0, 0).setInsets(5, 0, 0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));
//		this.add(smTextFieldLegitAutoSaveTime, new GridBagConstraintsHelper(1, 7, 1, 1).setWeight(0, 0).setInsets(5, 5, 0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));
//		this.add(labelAutoSaveTimeUnit, new GridBagConstraintsHelper(2, 7, 1, 1).setWeight(1, 0).setInsets(5, 5, 0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));

		this.add(new JPanel(), new GridBagConstraintsHelper(0, 8, 3, 1).setWeight(1, 1).setInsets(5, 0, 0, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));
	}

	@Override
	protected void initListeners() {
//		smTextFieldLegitAutoSaveTime.setSmTextFieldLegit(new ISmTextFieldLegit() {
//			@Override
//			public boolean isTextFieldValueLegit(String textFieldValue) {
//				if (StringUtilities.isNullOrEmpty(textFieldValue)) {
//					return false;
//				}
//				try {
//					Integer integer = Integer.valueOf(textFieldValue);
//					if (integer <= 0) {
//						return false;
//					}
//					if (integer == GlobalParameters.getWorkspaceAutoSaveTime()) {
//						changedValues.remove(smTextFieldLegitAutoSaveTime);
//					} else {
//						changedValues.add(smTextFieldLegitAutoSaveTime);
//					}
//				} catch (Exception e) {
//					return false;
//				}
//				return true;
//			}
//
//			@Override
//			public String getLegitValue(String currentValue, String backUpValue) {
//				return backUpValue;
//			}
//		});
//		smTextFieldLegitSymbolSaveTime.setSmTextFieldLegit(new ISmTextFieldLegit() {
//			@Override
//			public boolean isTextFieldValueLegit(String textFieldValue) {
//				if (StringUtilities.isNullOrEmpty(textFieldValue)) {
//					return false;
//				}
//				try {
//					Integer integer = Integer.valueOf(textFieldValue);
//					if (integer <= 0) {
//						return false;
//					}
//					if (integer == GlobalParameters.getSymbolSaveTime()) {
//						changedValues.remove(smTextFieldLegitSymbolSaveTime);
//					} else {
//						changedValues.add(smTextFieldLegitSymbolSaveTime);
//					}
//				} catch (Exception e) {
//					return false;
//				}
//				return true;
//			}
//
//			@Override
//			public String getLegitValue(String currentValue, String backUpValue) {
//				return backUpValue;
//			}
//		});
		checkBoxShowDataInNowWindow.addItemListener(itemListener);
		checkBoxIsShowFormClosingInfo.addItemListener(itemListener);
		checkBoxWorkspaceCloseNotify.addItemListener(itemListener);
		checkBoxCloseMemoryDatasourceNotify.addItemListener(itemListener);
		checkBoxIsAutoCloseEmptyMap.addItemListener(itemListener);
//		checkBoxAutoSaveWorkspace.addItemListener(itemListener);
//		checkBoxSymbolLibraryRecovery.addItemListener(itemListener);
		checkBoxWorkspaceRecovery.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (changedValues.contains(checkBoxWorkspaceRecovery)) {
					changedValues.remove(checkBoxWorkspaceRecovery);
				} else {
					changedValues.add(checkBoxWorkspaceRecovery);
				}
//				boolean selected = checkBoxWorkspaceRecovery.isSelected();
//				checkBoxSymbolLibraryRecovery.setEnabled(selected);
//				smTextFieldLegitSymbolSaveTime.setEditable(selected);

			}
		});
	}

	@Override
	protected void initResources() {
		checkBoxShowDataInNowWindow.setText(FrameProperties.getString("String_ShowDataInNowWindow"));
		checkBoxIsShowFormClosingInfo.setText(FrameProperties.getString("String_ShowCloseInfoForm"));
		checkBoxWorkspaceCloseNotify.setText(FrameProperties.getString("String_CloseWorkspaceNotyfy"));
		checkBoxCloseMemoryDatasourceNotify.setText(FrameProperties.getString("String_ShowWorkspaceMemorytTip"));
		checkBoxIsAutoCloseEmptyMap.setText(FrameProperties.getString("String_CloseEmptyWindow"));
		checkBoxWorkspaceRecovery.setText(FrameProperties.getString("String_WorkspaceRecovery"));
//		checkBoxAutoSaveWorkspace.setText(FrameProperties.getString("String_AutoSaveWorkspace"));
//		labelAutoSaveTime.setText(FrameProperties.getString("String_AutoSaveWorkspaceTime"));
//		labelAutoSaveTimeUnit.setText(CoreProperties.getString("String_Time_Minutes"));
//		checkBoxSymbolLibraryRecovery.setText(FrameProperties.getString("String_checkBoxSymbolLibraryRecovery"));
//		labelSymbolSaveTimeUnit.setText(CoreProperties.getString("String_Time_Minutes"));
	}

	@Override
	protected void initComponentStates() {
//		checkBoxSymbolLibraryRecovery.setSelected(GlobalParameters.isSaveSymbol());
//		smTextFieldLegitSymbolSaveTime.setText(String.valueOf(GlobalParameters.getSymbolSaveTime()));
		checkBoxShowDataInNowWindow.setSelected(GlobalParameters.isShowDataInNewWindow());
		checkBoxIsShowFormClosingInfo.setSelected(GlobalParameters.isShowFormClosingInfo());
		checkBoxWorkspaceCloseNotify.setSelected(GlobalParameters.isWorkspaceCloseNotify());
		checkBoxCloseMemoryDatasourceNotify.setSelected(GlobalParameters.isCloseMemoryDatasourceNotify());
		checkBoxIsAutoCloseEmptyMap.setSelected(GlobalParameters.isAutoCloseEmptyWindow());
		checkBoxWorkspaceRecovery.setSelected(GlobalParameters.isWorkspaceRecovery());
//		checkBoxAutoSaveWorkspace.setSelected(GlobalParameters.isWorkspaceAutoSave());
//		smTextFieldLegitAutoSaveTime.setText(String.valueOf(GlobalParameters.getWorkspaceAutoSaveTime()));
//		checkBoxSymbolLibraryRecovery.setEnabled(checkBoxWorkspaceRecovery.isSelected());
//		smTextFieldLegitSymbolSaveTime.setEnabled(checkBoxWorkspaceRecovery.isSelected());
	}

	@Override
	public void apply() {
//		if (changedValues.contains(smTextFieldLegitAutoSaveTime)) {
//			changedValues.remove(smTextFieldLegitAutoSaveTime);
//			GlobalParameters.setWorkspaceAutoSaveTime(Integer.valueOf(smTextFieldLegitAutoSaveTime.getText()));
//			if (changedValues.contains(checkBoxAutoSaveWorkspace)) {
//				changedValues.remove(checkBoxAutoSaveWorkspace);
//				applyIsAutoSaveWorkSpace();
//			} else {
//				WorkspaceAutoSave.getInstance().exit();
//				WorkspaceAutoSave.getInstance().start();
//			}
//		} else if (changedValues.contains(checkBoxAutoSaveWorkspace)) {
//			changedValues.remove(checkBoxAutoSaveWorkspace);
//			applyIsAutoSaveWorkSpace();
//		}
		for (Component component : changedValues) {
			if (component == checkBoxShowDataInNowWindow) {
				GlobalParameters.setIsShowDataInNewWindow(checkBoxShowDataInNowWindow.isSelected());
			} else if (component == checkBoxIsShowFormClosingInfo) {
				GlobalParameters.setIsShowFormClosingInfo(checkBoxIsShowFormClosingInfo.isSelected());
			} else if (component == checkBoxWorkspaceCloseNotify) {
				GlobalParameters.setIsWorkspaceCloseNotify(checkBoxWorkspaceCloseNotify.isSelected());
			} else if (component == checkBoxCloseMemoryDatasourceNotify) {
				GlobalParameters.setIsCloseMemoryDatasourceNotify(checkBoxCloseMemoryDatasourceNotify.isSelected());
			} else if (component == checkBoxIsAutoCloseEmptyMap) {
				GlobalParameters.setIsAutoCloseEmptyWindow(checkBoxIsAutoCloseEmptyMap.isSelected());
			} else if (component == checkBoxWorkspaceRecovery) {
				GlobalParameters.setIsWorkspaceRecovery(checkBoxWorkspaceRecovery.isSelected());
				if (checkBoxWorkspaceRecovery.isSelected()) {
					WorkspaceTempSave.getInstance().start();
				} else {
					WorkspaceTempSave.getInstance().exit();
				}
			}
//			else if (component == checkBoxSymbolLibraryRecovery) {
//				GlobalParameters.setIsSaveSymbol(checkBoxSymbolLibraryRecovery.isSelected());
//			} else if (component == smTextFieldLegitSymbolSaveTime) {
//				GlobalParameters.setSymbolSaveTime(Integer.valueOf(smTextFieldLegitSymbolSaveTime.getBackUpValue()));
//				WorkspaceTempSave.getInstance().setSymbolSaveCount(Integer.valueOf(smTextFieldLegitSymbolSaveTime.getBackUpValue()));
//			}
		}
	}

//	private void applyIsAutoSaveWorkSpace() {
//		changedValues.remove(checkBoxAutoSaveWorkspace);
//		GlobalParameters.setIsWorkspaceAutoSave(checkBoxAutoSaveWorkspace.isSelected());
//		if (checkBoxAutoSaveWorkspace.isSelected()) {
//			WorkspaceAutoSave.getInstance().start();
//		} else {
//			WorkspaceAutoSave.getInstance().exit();
//		}
//	}

	@Override
	public void dispose() {

	}
}
