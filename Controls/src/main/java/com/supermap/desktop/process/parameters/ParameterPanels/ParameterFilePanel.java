package com.supermap.desktop.process.parameters.ParameterPanels;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.parameter.ipls.ParameterFile;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.FileChooserPathChangedListener;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.JFileChooserControl;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.Objects;

/**
 * @author XiaJT
 * 当设置为必要参数时，重绘显示-yuanR 2017.8.29
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.FILE)
public class ParameterFilePanel extends SwingPanel {
	private ParameterFile parameterFile;

	private JFileChooserControl fileChooserControl = new JFileChooserControl();
	private SmFileChoose createFileChoose = null;
	private SmFileChoose openFileChoose = null;
	private boolean isSelectingFile = false;
	private JLabel label = new JLabel();

	public ParameterFilePanel(IParameter parameterFile) {
		super(parameterFile);
		this.parameterFile = (ParameterFile) parameterFile;
		if (this.parameterFile.getSelectedItem() != null) {
			fileChooserControl.setPath(this.parameterFile.getSelectedItem().toString());
		}
		fileChooserControl.setEnabled(((ParameterFile) parameterFile).isEnabled());

		String createName = this.parameterFile.getModuleName() + "CreateFile";
		String openName = this.parameterFile.getModuleName() + "OpenFile";
		try {
			if (!SmFileChoose.isModuleExist(createName)) {
				String fileFilters = SmFileChoose.bulidFileFilters(
						SmFileChoose.createFileFilter(ProcessProperties.getString("String_SWMFilePath"), "swmb"));
				SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
						ProcessProperties.getString("String_SWMFile"), createName, "SaveOne");
			}
			if (!SmFileChoose.isModuleExist(openName)) {
				String fileFilters = SmFileChoose.bulidFileFilters(
						SmFileChoose.createFileFilter(ProcessProperties.getString("String_SWMFilePath"), "swmb"));
				SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
						ProcessProperties.getString("String_SWMFile"), openName, "OpenOne");
			}

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}

		createFileChoose = new SmFileChoose(createName);
		openFileChoose = new SmFileChoose(openName);

		if (this.parameterFile.getCreateNewFile()) {
			fileChooserControl.setFileChooser(createFileChoose);
		} else {
			fileChooserControl.setFileChooser(openFileChoose);
		}

		label.setText(getDescribe());
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

	}

	private void initLayout() {
		label.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
		fileChooserControl.setPreferredSize(new Dimension(20, 23));
		panel.setLayout(new GridBagLayout());
		if (StringUtilities.isNullOrEmpty(label.getText())) {
			panel.add(fileChooserControl, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0).setFill(GridBagConstraints.HORIZONTAL));
		} else {
			panel.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.NONE));
			panel.add(fileChooserControl, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0).setFill(GridBagConstraints.HORIZONTAL));
		}
	}

	/**
	 * @return 判断是否为必要参数
	 */
	private String getDescribe() {
		String describe = parameterFile.getDescribe();
		if (parameterFile.isRequisite()) {
			return MessageFormat.format(CommonProperties.getString("String_IsRequiredLable"), describe);
		} else {
			return describe;
		}
	}
}
