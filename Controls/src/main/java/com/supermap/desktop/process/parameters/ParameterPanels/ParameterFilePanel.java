package com.supermap.desktop.process.parameters.ParameterPanels;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.implement.ParameterFile;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.ui.controls.FileChooserPathChangedListener;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.JFileChooserControl;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

/**
 * @author XiaJT
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.FILE)
public class ParameterFilePanel extends SwingPanel {
	private ParameterFile parameterFile;
	private JFileChooserControl fileChooserControl = new JFileChooserControl();
	private boolean isSelectingFile = false;
	private JLabel label = new JLabel();

	public ParameterFilePanel(IParameter parameterFile) {
		super(parameterFile);
		this.parameterFile = (ParameterFile) parameterFile;
		if (this.parameterFile.getSelectedItem() != null) {
			fileChooserControl.setPath(this.parameterFile.getSelectedItem().toString());
		}
		fileChooserControl.setEnabled(((ParameterFile) parameterFile).isEnabled());

		SmFileChoose fileChoose = null;
		if (!SmFileChoose.isModuleExist(this.parameterFile.getModuleName())) {
			SmFileChoose.addNewNode(this.parameterFile.getFilters(), "", this.parameterFile.getTitle(), this.parameterFile.getModuleName(), this.parameterFile.getType());
		}

		fileChoose = new SmFileChoose(this.parameterFile.getModuleName());
		fileChooserControl.setFileChooser(fileChoose);
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
	}

	private void initLayout() {
		label.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
		fileChooserControl.setPreferredSize(new Dimension(20, 23));
		panel.setLayout(new GridBagLayout());
		if (StringUtilities.isNullOrEmpty(label.getText())) {
			panel.add(fileChooserControl, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0).setFill(GridBagConstraints.HORIZONTAL));
		} else {
			panel.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.NONE));
			panel.add(fileChooserControl, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0).setFill(GridBagConstraints.HORIZONTAL));
		}
	}
}
