package com.supermap.desktop.process.parameters.ParameterPanels;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.parameter.ipls.ParameterFile;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.ui.controls.FileChooserPathChangedListener;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.JFileChooserControl;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

/**
 * @author XiaJT
 * 给文件选择器添加单选框，用于文件选择器负责创建或者选用的判断-yuanR2017.8.24
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.FILE)
public class ParameterFilePanel extends SwingPanel {
	private ParameterFile parameterFile;

	private ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton radioButtonCreateFile = new JRadioButton();
	private JRadioButton radioButtonSelectFile = new JRadioButton();

	private JFileChooserControl fileChooserControl = new JFileChooserControl();
	private SmFileChoose fileChoose = null;
	private boolean isSelectingFile = false;
	private JLabel label = new JLabel();


	/**
	 * 通过单选框的选择，来实现不同类型（保存/打开）文件选择器的选择
	 */
	private ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == radioButtonCreateFile) {
				fileChoose = new SmFileChoose(parameterFile.getModuleName() + "CreateFile");
				fileChooserControl.setFileChooser(fileChoose);
			} else if (e.getSource() == radioButtonSelectFile) {
				fileChoose = new SmFileChoose(parameterFile.getModuleName() + "OpenFile");
				fileChooserControl.setFileChooser(fileChoose);
			}
		}
	};

	public ParameterFilePanel(IParameter parameterFile) {
		super(parameterFile);
		this.parameterFile = (ParameterFile) parameterFile;
		if (this.parameterFile.getSelectedItem() != null) {
			fileChooserControl.setPath(this.parameterFile.getSelectedItem().toString());
		}
		fileChooserControl.setEnabled(((ParameterFile) parameterFile).isEnabled());

		if (!SmFileChoose.isModuleExist(this.parameterFile.getModuleName() + "CreateFile")) {
			SmFileChoose.addNewNode(this.parameterFile.getFilters(), "", this.parameterFile.getTitle(), this.parameterFile.getModuleName() + "CreateFile", "SaveOne");
		}
		if (!SmFileChoose.isModuleExist(this.parameterFile.getModuleName() + "OpenFile")) {
			SmFileChoose.addNewNode(this.parameterFile.getFilters(), "", this.parameterFile.getTitle(), this.parameterFile.getModuleName() + "OpenFile", "OpenOne");
		}

		buttonGroup.add(radioButtonSelectFile);
		buttonGroup.add(radioButtonCreateFile);
		radioButtonCreateFile.setText(ControlsProperties.getString("String_RadioButtonText_CreateFile"));
		radioButtonSelectFile.setText(ControlsProperties.getString("String_RadioButtonText_SelectFile"));
		radioButtonSelectFile.setSelected(true);

		label.setText(this.parameterFile.getDescribe());
		label.setToolTipText(this.parameterFile.getDescribe());
		initListener();
		initLayout();
	}

	private void initListener() {
		this.fileChooserControl.addFileChangedListener(new FileChooserPathChangedListener() {
			@Override
			public void pathChanged() {
				if (!isSelectingFile) {
					try {
						isSelectingFile = true;
						parameterFile.setSelectedItem(fileChooserControl.getPath());
					} finally {
						isSelectingFile = false;
					}
				}
			}
		});

		parameterFile.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectingFile && Objects.equals(evt.getPropertyName(), AbstractParameter.PROPERTY_VALE)) {
					try {
						isSelectingFile = true;
						if (evt.getNewValue() instanceof Boolean) {
							fileChooserControl.setEnabled((Boolean) evt.getNewValue());
						} else {
							fileChooserControl.setPath(evt.getNewValue().toString());
						}
					} finally {
						isSelectingFile = false;
					}
				}
			}
		});

		radioButtonSelectFile.addActionListener(actionListener);
		radioButtonCreateFile.addActionListener(actionListener);
	}

	private void initLayout() {
		label.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
		fileChooserControl.setPreferredSize(new Dimension(20, 23));
		panel.setLayout(new GridBagLayout());
		if (StringUtilities.isNullOrEmpty(label.getText())) {
			panel.add(radioButtonSelectFile, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0).setFill(GridBagConstraints.HORIZONTAL));
			panel.add(radioButtonCreateFile, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0).setFill(GridBagConstraints.HORIZONTAL));
			panel.add(fileChooserControl, new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0).setFill(GridBagConstraints.HORIZONTAL));
		} else {
			panel.add(radioButtonSelectFile, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0).setFill(GridBagConstraints.HORIZONTAL));
			panel.add(radioButtonCreateFile, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0).setFill(GridBagConstraints.HORIZONTAL));
			panel.add(label, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.NONE));
			panel.add(fileChooserControl, new GridBagConstraintsHelper(1, 1, 2, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0).setFill(GridBagConstraints.HORIZONTAL));
		}
	}
}
